/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author Joseph Spencer
 */
public class AssetManager {
   private static final Logger logger = Logger.getLogger(AssetManager.class);
   private FilePath assetPath;
   private FilePath buildPath;
   public AssetManager(FilePath assets, FilePath build) throws IOException{
      assetPath=assets;
      buildPath=build;
   }

   /**
    * Transfers an asset from the asset directory to the build directory.  The
    * asset isn't transferred if it's last modified time is less than the
    * last modified time of the corresponding asset in build.
    *
    * @param path
    */
   public void transferAsset(String path) throws IOException {
      File from = assetPath.resolve(path).toFile();
      File to = buildPath.resolve(path).toFile();
      String fromPath = from.getAbsolutePath();
      String toPath = to.getAbsolutePath();
      String preamble = "Couldn't transfer the following asset because it";
      if(!from.exists()){
         throw new IOException(preamble+" doesn't exist: "+fromPath);
      }
      if(!from.isFile()){
         throw new IOException(preamble+" isn't a file: "+fromPath);
      }
      if(to.isDirectory()){
         throw new IOException(preamble+"'s target under build is a directory: "+toPath);
      }
      if(from.lastModified() > to.lastModified()){
         FileUtils.copyFile(from, to);
      } else {
         logger.info("The following asset wasn't transferred because it is older than the target: " + fromPath);
      }
   }
   public String getAsset(String path) throws IOException {
      File file = StaticPages.assetsDirPath.resolve(path).toFile();
      return FileUtils.getString(file).replace("ASSET_PREFIX_IN_BROWSER", StaticPages.assetPrefixInBrowser);
   }
}
