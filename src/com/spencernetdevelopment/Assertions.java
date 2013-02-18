/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import java.io.File;
import java.nio.file.Path;

/**
 *
 * @author Joseph Spencer
 */
public class Assertions {
   public static boolean fileExists(File file){
      if(file==null || !file.isFile() || !file.exists()){
         return false;
      }
      return true;
   }
   public static boolean fileExists(Path file){
      return fileExists(file.toFile());
   }
}
