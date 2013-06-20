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

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.spencernetdevelopment.Logger.*;

/**
 *
 * @author Joseph Spencer
 */
public class AssetManager {
   private static final Pattern CSS_URL = Pattern.compile("url\\(\\s*(['\"])?/((?:(?!\\1\\)).)+)\\1?\\)");
   private static final Pattern VARIABLES = Pattern.compile(
      "\\$\\{([a-zA-Z_][0-9a-zA-Z_]*)\\}"
   );

   private final String assetPrefixInBrowser;
   private final Properties variables;
   private final int maxDataURISizeInBytes;
   private final FilePath assetPath;
   private final FilePath buildPath;
   private final AssetResolver assetResolver;
   private final FileUtils fileUtils;
   private final StaticPagesConfiguration config;
   private final Object TRANSFER_ASSET_LOCK = new Object();

   public AssetManager(
      FilePath assets,
      FilePath build,
      FileUtils fileUtils,
      Properties variables,
      StaticPagesConfiguration config,
      AssetResolver assetResolver
   )
      throws IOException
   {
      assetPath=assets;
      buildPath=build;
      this.variables=variables;
      this.assetPrefixInBrowser=config.getAssetPrefixInBrowser();
      this.maxDataURISizeInBytes=config.getMaxDataURISizeInBytes();
      this.assetResolver=assetResolver;
      this.fileUtils=fileUtils;
      this.config=config;
   }

   public String getAsset(File file) throws IOException {
      return fileUtils.getString(file);
   }
   public String getAsset(String path) throws IOException {
      if(isDebug)debug("getAsset called with path: "+path);
      File file = assetPath.resolve(path).toFile();
      return getAsset(file);
   }

   public String getCSS(File file, boolean compress) throws IOException, URISyntaxException {
      return handleCSS(getAsset(file), compress);
   }
   public String getCSS(String path, String compress) throws IOException, URISyntaxException {
      return getCSS(path, isCompressionFromAttributeValue(compress));
   }
   public String getCSS(String path, boolean compress) throws IOException, URISyntaxException {
      if(isDebug)debug("getCSS called with path: "+path);
      return handleCSS(getAsset(
         assetResolver.getCleanCSSPath(path)
      ), compress);
   }
   private String handleCSS(String contents, boolean compress) throws IOException, URISyntaxException {
      String contentsToReturn;
      if(compress){
         StringReader reader = new StringReader(contents);
         StringWriter writer = new StringWriter(contents.length());
         CssCompressor cssCompressor = new com.yahoo.platform.yui.compressor.CssCompressor(reader);
         cssCompressor.compress(writer, -1);
         contentsToReturn = writer.toString();
      } else {
         contentsToReturn = contents;
      }
      //Use this to ensure we don't keep replacing the same URL over and over
      //again in the css file when we're not going to embed the data uri.
      Set<String> seive = new HashSet<>();
      //This transferes all images over that are defined in css files.
      Matcher urls = CSS_URL.matcher(contents);
      while(urls.find()){
         String url = new URI(urls.group(2)).getPath();
         String dataType=url.toLowerCase().replaceFirst(".*\\.([^\\.]+)$", "$1");
         String mimeType=null;

         if(compress){
            switch(dataType){
               case "gif":
               case "jpeg":
               case "jpg":
               case "png":
                  mimeType="image/"+dataType;
                  break;
            }

            if(mimeType != null){
               byte[] bytes = fileUtils.getBytes(
                  assetPath.resolve(url).toFile()
               );
               String encoded = Base64.encodeToString(bytes, false);
               byte[] encodedBytes = encoded.getBytes();
               if(maxDataURISizeInBytes > 0 &&
                  encodedBytes.length <= maxDataURISizeInBytes
               ){
                  contentsToReturn = contentsToReturn.replace(
                     urls.group(0),
                     "url(data:"+mimeType+";base64,"+encoded+")"
                  );
                  continue;
               } else {
                  Logger.info(
                     "The following resource exceeded the max size in bytes "+
                     "specified and can not be embedded within CSS: "+url);
               }
            }
         }

         if(!seive.contains(url)){
            seive.add(url);
            contentsToReturn = contentsToReturn.replace(
               "/"+url,
               assetPrefixInBrowser+"/"+assetResolver.getAssetPath(url)
            );
            transferAsset(url, assetResolver.getAssetPath(url));
         }
      }
      return contentsToReturn;
   }

   public String getJS(File file, boolean compress) throws IOException {
      return handleJS(getAsset(file), compress);
   }
   public String getJS(String path, String compress) throws IOException, URISyntaxException {
      return getJS(path, isCompressionFromAttributeValue(compress));
   }
   public String getJS(String path, boolean compress) throws IOException, URISyntaxException {
      if(isDebug)debug("getJS called with path: "+path);
      return handleJS(getAsset(
         assetResolver.getCleanJSPath(path)
      ), compress);
   }
   private String handleJS(String contents, boolean compress) throws IOException {
      String minified=null;
      if(compress){
         StringReader reader = new StringReader(contents);
         StringWriter writer = new StringWriter(contents.length());
         JavaScriptCompressor javaScriptCompressor = new JavaScriptCompressor(reader, JSErrorReporter.INSTANCE);
         javaScriptCompressor.compress(writer, -1, true, false, false, false);
         minified=writer.toString();
      }
      return (minified == null ? contents : minified).replace(
                  "ASSET_PREFIX_IN_BROWSER",
                  assetPrefixInBrowser
               );
   }

   public void transferCSS(String src, String compress)
      throws IOException,
             URISyntaxException
   {
      transferCSS(src,isCompressionFromAttributeValue(compress));
   }

   public void transferCSS(String src, boolean compress)
      throws IOException,
             URISyntaxException
   {
      transferCSS(
         assetResolver.getCleanCSSPath(src),
         assetResolver.getCSSPath(src),
         compress
      );
   }
   public void transferCSS(
      String srcPath,
      String targetPath,
      boolean compress
   ) throws
      IOException, URISyntaxException
   {
      AtomicReference<File> source = new AtomicReference<>();
      AtomicReference<File> target = new AtomicReference<>();
      if(prepareAssetTransfer(
         srcPath,
         source,
         targetPath,
         target
      )){
         if(isDebug)debug(
            "transferCSS called with path: "+srcPath+"\n"+
            "and compress: "+compress
         );
         String css = getCSS(source.get(), compress);
         fileUtils.putString(target.get(), css);
      }

   }
   public void transferImage(String path)
      throws IOException,
             URISyntaxException
   {
      transferAsset(path, assetResolver.getAssetPath(path));
   }
   public void transferJS(String src, String compress)
      throws IOException,
             URISyntaxException
   {
      transferJS(src, isCompressionFromAttributeValue(compress));
   }
   public void transferJS(String src, boolean compress)
      throws IOException,
             URISyntaxException
   {
      transferJS(
         assetResolver.getCleanJSPath(src),
         assetResolver.getJSPath(src),
      compress);
   }
   public void transferJS(
      String srcPath,
      String targetPath,
      boolean compress
   ) throws
      IOException
   {
      AtomicReference<File> source = new AtomicReference<>();
      AtomicReference<File> target = new AtomicReference<>();
      if(prepareAssetTransfer(
         srcPath,
         source,
         targetPath,
         target
      )){
         if(isDebug)debug(
            "transferJS called with path: "+srcPath+"\n"+
            "and compress: "+compress
         );
         fileUtils.putString(target.get(), getJS(source.get(), compress));
      }
   }

   /**
    * Transfers assets from the src dir to the build dir.
    *
    * @param path
    * @throws IOException
    */
   public void transferAsset(String path) throws IOException {
      transferAsset(path, path);
   }

   /**
    * Transfers assets as char arrays.
    *
    * @param path
    * @throws IOException
    */
   public void transferAsset(
      String srcPath,
      String targetPath
   ) throws
      IOException
   {
      srcPath = assetPath.resolve(srcPath).toString();
      targetPath = buildPath.resolve(targetPath).toString();
      if(fileUtils.isDirectory(srcPath)){
         fileUtils.copyDirContentsToDir(srcPath, targetPath);
      } else {
         AtomicReference<File> source = new AtomicReference<>();
         AtomicReference<File> target = new AtomicReference<>();
         if(prepareAssetTransfer(srcPath, source, targetPath, target)){
            if(isDebug)debug("transferAsset called with path: "+srcPath);
            fileUtils.copyFile(source.get(), target.get());
         }
      }
   }

   /**
    * Prepares assets for transferring if the source is newer than the target.
    *
    * @param path
    * @return true the asset can be transferred.
    */
   private boolean prepareAssetTransfer(
      String srcPath,
      AtomicReference<File> source,
      String targetPath,
      AtomicReference<File> target
   ) throws
      IOException
   {
      File from = assetPath.resolve(srcPath).toFile();
      File to = buildPath.resolve(targetPath).toFile();
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
      synchronized(TRANSFER_ASSET_LOCK){
         if(from.lastModified() > to.lastModified() || !to.exists()){
            source.set(from);
            target.set(to);
            return true;
         }
         return false;
      }
   }

   public String expandVariables(String text){
      String returnText = text;
      if(text == null){
         return "";
      }
      Matcher vars = VARIABLES.matcher(text);
      while(vars.find()){
         String var = vars.group(1);
         if(variables.containsKey(var)){
            Object value;
            if(variables.get(var) != null){
               value = variables.get(var);
            } else {
               value = "";
            }
            returnText = returnText.replace(vars.group(0), value.toString());
         }
      }
      return returnText.replaceAll(VARIABLES.pattern(), "");
   }

   public boolean isCompressionFromAttributeValue(String bool){
      return isBoolean(bool, config.isEnableCompression());
   }

   public boolean isBoolean(String bool, boolean fallback){
      if("true".equals(bool)){
         return true;
      } else if("false".equals(bool)){
         return false;
      } else {
         return fallback;
      }
   }
}
