/*
 * Copyright 2012 Joseph Spencer.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spencernetdevelopment;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class manages GroupedAssetTransaction[s].  It ensured that a transaction
 * is only built once within it's session.
 *
 * @author Joseph Spencer
 */
public class GroupedAssetTransactionManager {
   private final AssetManager assetManager;
   private final AssetResolver assetResolver;
   private final List<GroupedAssetTransaction> transactions = new ArrayList<>();
   private final Set<String> processedItems = new HashSet<>();
   private final StaticPagesConfiguration config;
   private final FileUtils fileUtils;

   public GroupedAssetTransactionManager(
      AssetManager assetManager,
      AssetResolver assetResolver,
      StaticPagesConfiguration config,
      FileUtils fileUtils
   )
      throws IllegalArgumentException
   {
      if(assetManager == null){
         throw new IllegalArgumentException("assetManager was null");
      }
      if(assetResolver == null){
         throw new IllegalArgumentException("assetResolver was null");
      }
      if(config == null){
         throw new IllegalArgumentException("config was null");
      }
      if(fileUtils == null){
         throw new IllegalArgumentException("fileUtils was null");
      }
      this.assetManager=assetManager;
      this.assetResolver=assetResolver;
      this.config=config;
      this.fileUtils=fileUtils;
   }

   /**
    * Processes the transactions currently waiting to be processed and outputs
    * the contents of the processed asset to disk.
    *
    * @return
    * @throws IOException See the following methods:<br/>
    * {@link AssetManager#getJS(java.lang.String, boolean)}<br/>
    * {@link AssetManager#getCSS(java.lang.String, boolean)}<br/>
    */
   public List<GroupedAssetTask<Object>> getGroupedAssetTasks()
      throws IOException, IllegalArgumentException, URISyntaxException
   {
      List<GroupedAssetTask<Object>> tasks = new ArrayList();
      List<GroupedAssetTransaction> transactionsToConsider=new ArrayList<>();
      synchronized(transactions){
         if(transactions.size()<1){
            return tasks;
         }
         for(GroupedAssetTransaction transaction:transactions){
            if(transaction.isClosed()){
               transactionsToConsider.add(transaction);
            }
         }
         for(GroupedAssetTransaction transaction:transactionsToConsider){
            transactions.remove(transaction);
         }
      }
      for(GroupedAssetTransaction transaction:transactionsToConsider){
         String identifier = transaction.getIdentifier();
         String type = transaction.getType();
         switch(type){
            case "js":
               identifier = assetResolver.getJSPath(identifier);
               break;
            case "css":
               identifier = assetResolver.getCSSPath(identifier);
               break;
         }
         synchronized(processedItems){
            if(processedItems.contains(identifier)){
               continue;
            } else {
               processedItems.add(identifier);
            }
         }

         FilePath proposedFilePath=config.getBuildDirPath().resolve(identifier);

         /*
          * Here we see if any of the transactions have a file that is in fact
          * newer than the target file under build.  If there isn't a newer
          * file, then we can skip this transaction.
          */
         if(proposedFilePath.toFile().isFile()){
            FilePath assetDirPath = config.getAssetsDirPath();
            File proposedFile = proposedFilePath.toFile();
            long lastModified = proposedFile.lastModified();
            boolean hasNewer=false;
            for(String path:transaction.toArray()){
               FilePath pathToCheck=null;
               switch(type){
                  case "js":
                     pathToCheck = assetDirPath.resolve(
                        assetResolver.getCleanJSPath(path)
                     );
                     break;
                  case "css":
                     pathToCheck = assetDirPath.resolve(
                        assetResolver.getCleanCSSPath(path)
                     );
                     break;
               }
               if(
                  pathToCheck != null &&
                  pathToCheck.toFile().lastModified() > lastModified
               ){
                  hasNewer=true;
                  break;
               }
            }
            if(!hasNewer){
               continue;//no elements of this transaction need to be processed.
            }
         }
         GroupedAssetTask<Object> transactionToProcess =
            new GroupedAssetTask<>(
               transaction,
               assetManager,
               fileUtils,
               proposedFilePath
            );
         tasks.add(transactionToProcess);
      }
      return tasks;
   }

   public GroupedAssetTransaction startTransaction(
      String type,
      String compress
   ){
      return startTransaction(type, compress, "false");
   }

   public GroupedAssetTransaction startTransaction(
      String type,
      String compress,
      String shouldWrapJsInClosure
   ){
      boolean useCompress="true".equals(compress) || "false".equals(compress);
      GroupedAssetTransaction transaction =
         new GroupedAssetTransaction(
         type,
         useCompress?
            "true".equals(compress):
            config.isEnableCompression(),
         "true".equals(shouldWrapJsInClosure)
      );
      synchronized(transactions){
         transactions.add(transaction);
      }
      return transaction;
   }
}
