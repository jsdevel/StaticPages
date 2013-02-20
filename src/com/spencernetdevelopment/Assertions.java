/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
      if(file==null || !file.exists() || !file.isFile()){
         return false;
      }
      return true;
   }
   public static boolean fileExists(FilePath file){
      return fileExists(file.toFile());
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
