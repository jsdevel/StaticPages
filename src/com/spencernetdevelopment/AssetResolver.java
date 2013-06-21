/*
 * Copyright 2013 Joseph Spencer.
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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author Joseph Spencer
 */
public class AssetResolver {
   private final String fingerprint;
   private final FilePath pagesDirPath;
   private final FilePath resourcesDirPath;
   private final FilePath viewsDirPath;

   public AssetResolver(StaticPagesConfiguration config){
      fingerprint=config.getAssetFingerprint();
      pagesDirPath=config.getPagesDirPath();
      resourcesDirPath=config.getXmlResourcesDirPath();
      viewsDirPath=config.getViewsDirPath();
   }

   /**
    * Returns a URL with an optional fingerprint depending on what is
    * configured.
    *
    * @param url
    * @throws NullPointerException if the URL is null.
    * @throws IllegalArgumentException if the URL has no file extension.
    * @throws IllegalArgumentException if the URL no path
    * @return
    */
   public String getAssetPath(String url) throws URISyntaxException {
      if(url == null){
         throw new NullPointerException("url was null");
      }
      url=forceRelativeFilePath(url);
      String fileExtension = url
         .replaceFirst("(?:.+(\\.[a-zA-Z0-9_]+))?$", "$1");
      url = url.substring(0, url.length()-fileExtension.length());
      String pathWithoutExtension = url
         .replaceFirst("(?:(.+)(?:\\.[a-zA-Z0-9_]+))?$", "$1");
      if(fileExtension.length() == 0){
         throw new IllegalArgumentException("no file extension was found");
      }
      if(pathWithoutExtension.length() == 0){
         throw new IllegalArgumentException("no path was found");
      }
      return pathWithoutExtension+fingerprint+fileExtension;
   }

   /**
    * Returns the path to the desired css resource without a fingerprint.
    *
    * @param path
    * @return
    */
   public String getCleanCSSPath(String path) throws URISyntaxException {
      return "css/"+forceRelativeFilePath(path)+".css";
   }

   /**
    * Returns the path to the desired image resource without a fingerprint.
    *
    * @param path
    * @return
    */
   public String getCleanImagePath(String path) throws URISyntaxException {
      return "images/"+forceRelativeFilePath(path);
   }

   /**
    * Returns the path to the desired javascript resource without a fingerprint.
    *
    * @param path
    * @return
    */
   public String getCleanJSPath(String path) throws URISyntaxException {
      return "js/"+forceRelativeFilePath(path)+".js";
   }


   /**
    * Returns the path to the desired css resource.  This path may or may
    * not have a fingerprint depending on the arguments given to the program.
    * @param path
    * @return
    */
   public String getCSSPath(String path) throws URISyntaxException {
      return "css/"+forceRelativeFilePath(path)+fingerprint+".css";
   }

   /**
    * Returns the path to the desired image resource.  This path may or may
    * not have a fingerprint depending on the arguments given to the program.
    * @param path
    * @return
    */
   public String getImagePath(String path) throws URISyntaxException {
      return "images/"+getAssetPath(forceRelativePath(path));
   }

   /**
    * Returns the path to the desired javascript resource.  This path may or may
    * not have a fingerprint depending on the arguments given to the program.
    * @param path
    * @return
    */
   public String getJSPath(String path) throws URISyntaxException {
      return "js/"+forceRelativeFilePath(path)+fingerprint+".js";
   }

   public String getNormalizedRewritePath(String rewritePath)
      throws URISyntaxException
   {
      String normalizedPath = "/"+forceRelativePath(rewritePath);
      if(!normalizedPath.endsWith(".html")){
         normalizedPath += "/";
      }
      return new URI(normalizedPath).normalize().toString();
   }
   /**
    *
    * @param prefix
    * @param page
    * @return
    * @throws IOException
    * @throws IllegalArgumentException if prefix ends in "/"
    */
   public String getPageLink(String prefix, String page) throws IOException {
      String path;
      if(prefix.endsWith("/")){
         throw new IllegalArgumentException("prefix must not end in \"/\".");
      }
      if(page.endsWith("/")){
         FilePath dirPath = pagesDirPath.resolve(page);
         FilePath defaultDirFile = dirPath.resolve("index.xml");
         File defaultFile = defaultDirFile.toFile();
         boolean isFile = defaultFile.isFile();
         if(!isFile){
            throw new IOException(
               "The following directory reference didn't point to a "+
               "default file: "+page
            );
         }
         path = prefix + "/" + page;
      } else {
         FilePath pagePath = pagesDirPath.resolve(page+".xml");
         File pageFile = pagePath.toFile();
         if(!pageFile.isFile()){
            throw new IOException(
               "The following page reference didn't point to an existing file:"+
               page
            );
         }
         path = prefix + "/" + page + ".html";
      }
      return path;
   }
   public String getPagePath(String path)
      throws IOException, URISyntaxException
   {
      FilePath fpath = pagesDirPath.resolve(
         path.endsWith("/")?
            path + "index.xml":
            path + ".xml"
      );
      return fpath.toUnix();
   }

   public String getResourcePath(String path)
      throws URISyntaxException,
             IOException
   {
      FilePath fPath = resourcesDirPath
              .resolve(forceRelativeFilePath(path)+".xml");
      return fPath.toUnix();
   }

   public String getViewPath(String path)
      throws IOException, URISyntaxException
   {
      FilePath fpath = viewsDirPath.resolve(forceRelativeFilePath(path)+".xml");
      return fpath.toUnix();
   }

   /**
    * This method doesn't allow the path to end in '/'.  Trailing white space is
    * removed.
    *
    * @param path
    * @return
    * @throws URISyntaxException
    */
   public String forceRelativeFilePath(String path) throws URISyntaxException {
      path = path.trim();
      if(path.endsWith("/")){
         throw new IllegalArgumentException("the path should not end in '/'");
      }
      return forceRelativePath(path);
   }

   public String forceRelativePath(String path) throws URISyntaxException {
      String normalized = new URI(
            null,
            null,
            path,
            null,
            null
         ).normalize().toString();
      normalized = normalized.replaceAll("^(?:\\.\\.[/\\\\])+|^[/\\\\]+", "");
      return normalized;
   }
}
