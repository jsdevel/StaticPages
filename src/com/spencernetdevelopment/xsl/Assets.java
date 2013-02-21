/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment.xsl;

import com.spencernetdevelopment.FilePath;
import com.spencernetdevelopment.FileUtils;
import com.spencernetdevelopment.StaticPages;
import java.io.IOException;

/**
 *
 * @author Joseph Spencer
 */
public class Assets {
   public static void assetReference(String path) throws IOException {
      FileUtils.copyFile(
         StaticPages.assetsDirPath.resolve(path).toFile(),
         StaticPages.buildDirPath.resolve(path).toFile()
      );
   }
   public static void validatePageReference(String path) throws IOException {
      if(path == null || path.length() == 0){
         throw new IOException("Invalid Path: '"+path+"'.");
      }

      FilePath page = StaticPages.pagesDirPath.resolve(path +".xml");
      if(!page.toFile().exists()){
         throw new IOException("The following page doesn't exist: "+page);
      }
   }

   public static String getViewPath(String path) throws IOException {
      FilePath fpath = StaticPages.viewsDirPath.resolve(path+".xml");
      return fpath.toUnix();
   }
}
