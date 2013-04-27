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

/**
 *
 * @author Joseph Spencer
 */
public class Assertions {
   public static boolean fileExists(File file){
      if(file==null || !file.isFile()){
         return false;
      }
      return true;
   }
   public static boolean fileExists(FilePath file){
      return fileExists(file.toFile());
   }
   public static boolean dirExists(File dir){
      if(dir==null || !dir.isDirectory()){
         return false;
      }
      return true;
   }

   public static void fileExistsOrFail(File file) throws IOException {
      if(!fileExists(file)){
         if(file == null){
            throw new IOException("No file to assert existence of, null passed...");
         }
         throw new IOException("The followig file doesn't exist: "+file.getAbsolutePath());
      }
   }
}
