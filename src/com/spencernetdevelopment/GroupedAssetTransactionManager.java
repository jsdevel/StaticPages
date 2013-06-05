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
import java.util.HashSet;
import java.util.Set;

/**
 * This class manages GroupedAssetTransaction[s].  It ensured that a transaction
 * is only built once within it's session.
 *
 * @author Joseph Spencer
 */
public class GroupedAssetTransactionManager {
   private final AssetManager assetManager;
   private final Set<String> processedItems = new HashSet<>();

   public GroupedAssetTransactionManager(AssetManager assetManager)
      throws IllegalArgumentException
   {
      if(assetManager == null){
         throw new IllegalArgumentException("assetManager was null");
      }
      this.assetManager=assetManager;
   }

   /**
    * Processes the transaction and returns the contents of the processed asset.
    * @param transaction
    * @param assetManager
    * @return
    * @throws IOException See the following methods:<br/>
    * {@link AssetManager#getJS(java.lang.String, boolean)}<br/>
    * {@link AssetManager#getCSS(java.lang.String, boolean)}<br/>
    * @throws IllegalArgumentException If the transaction hasn't been closed,
    * or if the transaction was already built.
    */
   public synchronized String process(GroupedAssetTransaction transaction)
      throws IOException, IllegalArgumentException, URISyntaxException
   {
      if(!transaction.isClosed()){
         throw new IllegalArgumentException("transaction wasn't closed.");
      }
      if(!processedItems.contains(transaction.getIdentifier())){
         StringBuilder builder = new StringBuilder();
         switch(transaction.getType()){
            case "js":
               for(String url:transaction.toArray()){
                  String js = assetManager.getJS(
                     "js/"+url+".js",
                     transaction.isCompressed()
                  );
                  builder.append(js);
               }
               break;
            case "css":
               for(String url:transaction.toArray()){
                  String css = assetManager.getCSS(
                     "css/"+url+".css",
                     transaction.isCompressed()
                  );
                  builder.append(css);
               }
               break;
         }
         processedItems.add(transaction.getIdentifier());
         return builder.toString();
      }
      throw new IllegalArgumentException("transaction already built");
   }
}
