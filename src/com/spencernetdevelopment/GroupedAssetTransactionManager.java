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
   public void processTransactions()
      throws IOException, IllegalArgumentException, URISyntaxException
   {
      List<GroupedAssetTransaction> transactionsToProcess=new ArrayList<>();
      synchronized(transactions){
         if(transactions.size()<1){
            return;
         }
         for(GroupedAssetTransaction transaction:transactions){
            if(transaction.isClosed()){
               transactionsToProcess.add(transaction);
            }
         }
         for(GroupedAssetTransaction transaction:transactionsToProcess){
            transactions.remove(transaction);
         }
      }
      for(GroupedAssetTransaction transaction:transactionsToProcess){
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

         StringBuilder builder = new StringBuilder();
         switch(type){
         case "js":
            for(String jsPath:transaction.toArray()){
               String js = assetManager.getJS(
                  jsPath,
                  transaction.isCompressed()
               );
               builder.append(js);
            }
            fileUtils.putString(
               config.getBuildDirPath().resolve(identifier).toString(),
               builder.toString()
            );
            break;
         case "css":
            for(String cssPath:transaction.toArray()){
               String css = assetManager.getCSS(
                  cssPath,
                  transaction.isCompressed()
               );
               builder.append(css);
            }
            fileUtils.putString(
               config.getBuildDirPath().resolve(identifier).toString(),
               builder.toString()
            );
            break;
         }
      }
   }

   public GroupedAssetTransaction startTransaction(
      String type,
      String compress
   ){
      boolean useCompress="true".equals(compress) || "false".equals(compress);
      GroupedAssetTransaction transaction =
         new GroupedAssetTransaction(
         type,
         useCompress?
            "true".equals(compress):
            config.isEnableCompression()
      );
      synchronized(transactions){
         transactions.add(transaction);
      }
      return transaction;
   }
}
