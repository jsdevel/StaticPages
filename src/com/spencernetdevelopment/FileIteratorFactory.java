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
public class FileIteratorFactory {
   private final FilePath assetPath;
   private final FileUtils fileUtils;

   public FileIteratorFactory(FilePath assetPath, FileUtils fileUtils) {
      this.assetPath = assetPath;
      this.fileUtils = fileUtils;
   }

   public FileIterator makeAssetIterator(
      String base,
      String extension,
      boolean recursive
   )
      throws IOException
   {
      return new FileIterator(
         assetPath.resolve(base),
         fileUtils,
         extension,
         recursive
      );
   }
}
