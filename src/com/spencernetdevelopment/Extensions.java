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

import java.io.IOException;

/**
 *
 * @author Joseph Spencer
 */
public class Extensions {
   private final BreadcrumbFactory breadcrumbFactory;
   private final FileIteratorFactory fileIteratorFactory;

   public Extensions(
      BreadcrumbFactory breadcrumbFactory,
      FileIteratorFactory fileIteratorFactory
   ){
      this.breadcrumbFactory=breadcrumbFactory;
      this.fileIteratorFactory=fileIteratorFactory;
   }

   public Breadcrumbs makeBreadcrumbs(String prefix, String pagePath) {
      return breadcrumbFactory.makeBreadcrumbs(prefix, pagePath);
   }

   public FileIterator makeAssetsIterator(
      String dirPath,
      String extension,
      String recursive
   )
      throws IOException
   {
      return fileIteratorFactory.makeAssetIterator(
         dirPath,
         extension,
         "true".equals(recursive)
      );
   }
}
