/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
