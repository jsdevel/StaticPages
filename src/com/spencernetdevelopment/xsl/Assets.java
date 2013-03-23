/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment.xsl;

import com.spencernetdevelopment.FilePath;
import com.spencernetdevelopment.FileUtils;
import static com.spencernetdevelopment.xsl.FileFunctions.assertFileExists;
import static com.spencernetdevelopment.xsl.FileFunctions.assertPathHasLength;
import com.spencernetdevelopment.StaticPages;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Joseph Spencer
 */
public class Assets {
   public static void transferAsset(String path) throws IOException {
      FileUtils.copyFile(
         StaticPages.assetsDirPath.resolve(path).toFile(),
         StaticPages.buildDirPath.resolve(path).toFile()
      );
   }
   public static void validatePageReference(String path) throws IOException {
      assertPathHasLength(path);
      FilePath page = StaticPages.pagesDirPath.resolve(path +".xml");
      assertFileExists(page);
   }

   public static void validateAssetReference(String path) throws IOException {
      assertPathHasLength(path);
      FilePath page = StaticPages.assetsDirPath.resolve(path);
      assertFileExists(page);
   }

   public static boolean assertXmlResourceExists(String path) throws IOException {
      assertPathHasLength(path);
      FilePath _path = StaticPages.xmlResourcesDirPath.resolve(path);
      try {
         assertFileExists(_path);
         return true;
      } catch(Throwable e){
         return false;
      }
   }

   public static String getViewPath(String path) throws IOException {
      FilePath fpath = StaticPages.viewsDirPath.resolve(path+".xml");
      return fpath.toUnix();
   }
   public static String getAsset(String path) throws IOException {
      File file = StaticPages.assetsDirPath.resolve(path).toFile();
      return FileUtils.getString(file).replace("ASSET_PREFIX_IN_BROWSER", StaticPages.assetPrefixInBrowser);
   }
}
