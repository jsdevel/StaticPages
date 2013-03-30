/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author Joseph Spencer
 */
public class AssetManager {
   private static final Logger logger = Logger.getLogger(AssetManager.class);
   private static final Pattern CSS_URL = Pattern.compile(
      "url\\(\\s*(['\"])?(/(?:(?!\\1\\)).)+)\\1?\\)");
   private FilePath assetPath;
   private FilePath buildPath;
   private final boolean compressAssets;
   public AssetManager(FilePath assets, FilePath build, boolean compress) throws IOException{
      assetPath=assets;
      buildPath=build;
      compressAssets=compress;
   }

   public String getAsset(File file) throws IOException {
      return FileUtils.getString(file);
   }
   public String getAsset(String path) throws IOException {
      File file = StaticPages.assetsDirPath.resolve(path).toFile();
      return getAsset(file);
   }

   public String getCSS(File file, boolean compress) throws IOException {
      return handleCSS(getAsset(file), compress);
   }
   public String getCSS(String path, boolean compress) throws IOException {
      return handleCSS(getAsset(path), compress);
   }
   private String handleCSS(String contents, boolean compress) throws IOException {
      String contentsToReturn;
      if((compress && compressAssets) || compressAssets){
         StringReader reader = new StringReader(contents);
         StringWriter writer = new StringWriter(contents.length());
         CssCompressor cssCompressor = new com.yahoo.platform.yui.compressor.CssCompressor(reader);
         cssCompressor.compress(writer, -1);
         contentsToReturn = writer.toString();
      } else {
         contentsToReturn = contents;
      }

      //This transferes all images over that are defined in css files.
      Matcher urls = CSS_URL.matcher(contents);
      while(urls.find()){
         String url = urls.group(2);
         if(compress){
            String encoded = Base64.encodeToString(FileUtils.getBytes(StaticPages.assetsDirPath.resolve(url).toFile()), false);
            String dataType=url.toLowerCase().replaceFirst(".*\\.([^\\.]+)$", "$1");
            switch(dataType){
               case "gif":
               case "jpeg":
               case "jpg":
               case "png":
                  contentsToReturn = contentsToReturn.replace(urls.group(0), "url(data:image/"+dataType+";base64,"+encoded+")");
                  break;
            default:
               throw new IOException("Invalid file extension detected: "+url);
            }
         } else {
            String prefix;
            if(StaticPages.assetPrefixInBrowser.endsWith("/")){
               if(StaticPages.assetPrefixInBrowser.length() > 1){
                  prefix = StaticPages.assetPrefixInBrowser.substring(0, StaticPages.assetPrefixInBrowser.length()-1);
               } else {
                  prefix = "";
               }
            } else {
               prefix = StaticPages.assetPrefixInBrowser;
            }
            contentsToReturn = contentsToReturn.replace(url, prefix+url);
            transferImage(url);
         }
      }
      return contentsToReturn;
   }

   public String getJS(File file, boolean compress) throws IOException {
      return handleJS(getAsset(file), compress);
   }
   public String getJS(String path, boolean compress) throws IOException {
      return handleJS(getAsset(path), compress);
   }
   private String handleJS(String contents, boolean compress) throws IOException {
      if((compress && compressAssets) || compressAssets){
         StringReader reader = new StringReader(contents);
         StringWriter writer = new StringWriter(contents.length());
         JavaScriptCompressor javaScriptCompressor = new JavaScriptCompressor(reader, JSErrorReporter.INSTANCE);
         javaScriptCompressor.compress(writer, -1, true, false, false, false);
         return writer.toString();
      }
      return contents.replace("ASSET_PREFIX_IN_BROWSER", StaticPages.assetPrefixInBrowser);
   }

   public void transferCSS(String path, boolean compress) throws IOException {
      AtomicReference<File> source = new AtomicReference<>();
      AtomicReference<File> target = new AtomicReference<>();
      if(prepareAssetTransfer(path, source, target)){
         FileUtils.putString(target.get(), getCSS(source.get(), compress));
      }

   }
   public void transferImage(String path) throws IOException {
      transferAsset(path);
   }
   public void transferJS(String path, boolean compress) throws IOException {
      AtomicReference<File> source = new AtomicReference<>();
      AtomicReference<File> target = new AtomicReference<>();
      if(prepareAssetTransfer(path, source, target)){
         FileUtils.putString(target.get(), getJS(source.get(), compress));
      }
   }

   /**
    * Transfers assets as char arrays.
    *
    * @param path
    * @throws IOException
    */
   public void transferAsset(String path) throws IOException {
      AtomicReference<File> source = new AtomicReference<>();
      AtomicReference<File> target = new AtomicReference<>();
      if(prepareAssetTransfer(path, source, target)){
         FileUtils.copyFile(source.get(), target.get());
      }
   }

   /**
    * Prepares assets for transferring if the source is newer than the target.
    *
    * @param path
    * @return true the asset can be transferred.
    */
   private boolean prepareAssetTransfer(String path, AtomicReference<File> source, AtomicReference<File> target) throws IOException {
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
      if(from.lastModified() > to.lastModified() || !to.exists()){
         source.set(from);
         target.set(to);
         return true;
      }
      logger.info("The following asset wasn't transferred because it is older than the target: " + fromPath);
      return false;
   }
}
