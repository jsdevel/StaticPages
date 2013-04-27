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
package com.spencernetdevelopment.xsl;

import com.spencernetdevelopment.FilePath;
import java.io.*;

/**
 *
 * @author Joseph Spencer
 */
public class FileFunctions {
   public static void assertPathHasLength(String path) throws IOException {
      if(path == null || path.trim().isEmpty()){
         throw new IOException("Invalid Path: '"+path+"'.");
      }
   }

   public static void assertFileExists(FilePath path) throws IOException {
      if(!path.toFile().exists()){
         throw new IOException("\nThis file doesn't exist: "+path);
      }
   }
}
