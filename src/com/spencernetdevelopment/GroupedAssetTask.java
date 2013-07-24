/*
 * Copyright 2013 Joseph Spencer.
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

import java.util.concurrent.Callable;

/**
 *
 * @author Joseph Spencer
 */
public class GroupedAssetTask<T> implements Callable<T> {
   private final GroupedAssetTransaction transaction;
   private final AssetManager assetManager;
   private final FileUtils fileUtils;
   private final FilePath proposedFilePath;

   public GroupedAssetTask(
      GroupedAssetTransaction transaction,
      AssetManager assetManager,
      FileUtils fileUtils,
      FilePath proposedFilePath
   ){
      this.transaction = transaction;
      this.assetManager = assetManager;
      this.fileUtils = fileUtils;
      this.proposedFilePath = proposedFilePath;
   }


   @Override
   public T call() throws Exception {
      StringBuilder builder = new StringBuilder();
      switch(transaction.getType()){
      case "js":
         if(transaction.shouldWrapJsInClosure()){
            builder.append("!function(){");
         }
         for(String jsPath:transaction.toArray()){
            String js = assetManager.getJS(
               jsPath,
               transaction.isCompressed()
            );
            builder.append(js);
         }
         if(transaction.shouldWrapJsInClosure()){
            builder.append("}();");
         }
         fileUtils.putString(
            proposedFilePath.toString(),
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
            proposedFilePath.toString(),
            builder.toString()
         );
         break;
      }
      return null;
   }

}
