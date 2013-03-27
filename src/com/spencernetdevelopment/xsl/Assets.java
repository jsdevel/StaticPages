/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment.xsl;

import com.spencernetdevelopment.FilePath;
import static com.spencernetdevelopment.xsl.FileFunctions.assertFileExists;
import static com.spencernetdevelopment.xsl.FileFunctions.assertPathHasLength;
import com.spencernetdevelopment.StaticPages;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author Joseph Spencer
 */
public class Assets {
   public static final Logger LOGGER = Logger.getLogger(Assets.class);

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
   public static String getAsset(String path) {
      try {
         return StaticPages.assetManager.getAsset(path);
      } catch (IOException ex){
         LOGGER.fatal("Couldn't find: "+path);
         System.exit(1);
      }
      return null;
   }
   public static String getViewPath(String path) throws IOException {
      FilePath fpath = StaticPages.viewsDirPath.resolve(path+".xml");
      return fpath.toUnix();
   }
   public static void transferAsset(String path) throws IOException {
      try {
         StaticPages.assetManager.transferAsset(path);
      } catch (IOException ex){
         LOGGER.fatal(ex.getLocalizedMessage());
         System.exit(1);
      }
   }
   public static void validatePageReference(String path) {
      try {
         assertPathHasLength(path);
         FilePath page = StaticPages.pagesDirPath.resolve(path +".xml");
         assertFileExists(page);
      } catch (IOException ex) {
         LOGGER.fatal("The following page doesn't exist: "+path);
         System.exit(1);
      }
   }
}
