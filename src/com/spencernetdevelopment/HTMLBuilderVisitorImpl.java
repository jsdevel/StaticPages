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

import javax.xml.transform.Transformer;

/**
 *
 * @author Joseph Spencer
 */
public class HTMLBuilderVisitorImpl implements HTMLBuilderVisitor {
   private final AssetManager assetManager;
   private final AssetResolver assetResolver;
   private final Extensions extensions;
   private final RewriteManager rewriteManager;
   private final StaticPagesConfiguration config;
   private final GroupedAssetTransactionManager groupedAssetTransactionManager;
   private final LinkValidator linkValidator;

   public HTMLBuilderVisitorImpl(
      AssetManager assetManager,
      AssetResolver assetResolver,
      Extensions extensions,
      RewriteManager rewriteManager,
      StaticPagesConfiguration config,
      GroupedAssetTransactionManager groupedAssetTransactionManager,
      LinkValidator linkValidator
   ){
      this.assetManager = assetManager;
      this.assetResolver = assetResolver;
      this.extensions=extensions;
      this.rewriteManager = rewriteManager;
      this.config = config;
      this.groupedAssetTransactionManager = groupedAssetTransactionManager;
      this.linkValidator=linkValidator;
   }

   @Override
   public void addDefaultParametersTo(WrappedTransformer transformer){
      transformer.setParameter("AM", assetManager);
      transformer.setParameter("AR", assetResolver);
      transformer.setParameter("E", extensions);
      transformer.setParameter("RM", rewriteManager);
      transformer.setParameter("LV", linkValidator);
      transformer.setParameter(
         "assetPrefixInBrowser",
         config.getAssetPrefixInBrowser()
      );
      transformer.setParameter(
         "enableDevMode",
         config.isEnableDevMode()
      );
      transformer.setParameter(
         "GATM",
         groupedAssetTransactionManager
      );
   }
}
