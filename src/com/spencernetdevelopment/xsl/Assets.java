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
package com.spencernetdevelopment.xsl;

import com.spencernetdevelopment.FilePath;
import com.spencernetdevelopment.FileUtils;
import com.spencernetdevelopment.GroupedAssetTransaction;
import com.spencernetdevelopment.HttpExternalLinkResponse;
import com.spencernetdevelopment.Logger;
import static com.spencernetdevelopment.xsl.FileFunctions.assertFileExists;
import static com.spencernetdevelopment.xsl.FileFunctions.assertPathHasLength;
import com.spencernetdevelopment.StaticPages;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Joseph Spencer
 */
public class Assets {
   private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
   private static XPathFactory xpathFactory = XPathFactory.newInstance();
   private static Map<String, HttpExternalLinkResponse> externalResponses = new HashMap<>();
   private static Set<String> validatedPages = new HashSet<>();
   private static Set<String> validatedFragments = new HashSet<>();
   private static Set<String> validatedXMLResources = new HashSet<>();

   /*
   public static boolean assertXmlResourceExists(String path) throws IOException {
      if(validatedXMLResources.contains(path)){
         return true;
      }
      assertPathHasLength(path);
      FilePath _path = StaticPages.xmlResourcesDirPath.resolve(path);
      try {
         assertFileExists(_path);
         validatedXMLResources.add(path);
         return true;
      } catch(Throwable e){
         return false;
      }
   }
   public static void buildGroupedAsset(GroupedAssetTransaction group){
      try {
         String contents = StaticPages.groupedAssetTransactionManager.
            process(group);
         String type = group.getType();
         String path;
         switch (type) {
            case "css":
               path = getCSSPath(group.getIdentifier());
               break;
            case "js":
               path = getJSPath(group.getIdentifier());
               break;
            default:
               return;//ignore
         }
         FileUtils.putString(
            StaticPages.buildDirPath.resolve(path).toFile(),
            contents
         );
      } catch (IOException ex) {
         Logger.fatal("Couldn't process group because of an IOException: "+ex.getMessage());
      } catch (IllegalArgumentException ex) {
         if(!group.isClosed()){
            Logger.fatal("The group wasn't closed.");
            System.exit(1);
         }
      }
   }
   public static String getAsset(String path) {
      try {
         return StaticPages.assetManager.getAsset(path);
      } catch (IOException ex){
         Logger.fatal("Couldn't find: "+path);
         System.exit(1);
      }
      return "";
   }
   */


   /**
    * Returns the contents of the requested css file.
    *
    * @param path Path relative to the src/assets/css directory without a file
    * extension.
    * @param compress
    * @return
    */
   /*
   public static String getCSS(String path, String compress) {
      path = getCleanCSSPath(path);
      boolean isCompress = getEnableCompression(compress);
      try {
         return StaticPages.assetManager.getCSS(path, isCompress);
      } catch (IOException ex){
         Logger.fatal("Couldn't get: "+path);
         System.exit(1);
      }
      return "";
   }
   */


   /**
    * Returns the contents of the requested js file.
    *
    * @param path Path relative to the src/assets/js directory without a file
    * extension.
    * @param compress
    * @return
    */
   /*
   public static String getJS(String path, String compress) {
      path = getCleanJSPath(path);
      boolean isCompress = getEnableCompression(compress);
      try {
         return StaticPages.assetManager.getJS(path, isCompress);
      } catch (IOException ex){
         Logger.fatal("Couldn't get: "+path);
         System.exit(1);
      }
      return "";
   }
   */

   /*
   public static void rewritePage(String page, String to){
      if(page == null || page.isEmpty()){
         Logger.fatal("Can't rewrite a non-existent page.  Page was: "+page+".  To was: "+to);
         System.exit(1);
      }
      if(to == null || to.isEmpty()){
         Logger.fatal("Can't rewrite a page to a non-existent location. Page was: "+page+".  To was: "+to);
         System.exit(1);
      }
      StaticPages.rewriteManager.queueRewrite(page, to);
   }
   public static void transferAsset(String path) throws IOException {
      try {
         StaticPages.assetManager.transferAsset(path, path);
      } catch (IOException ex){
         Logger.fatal(ex.getLocalizedMessage());
         System.exit(1);
      }
   }
   public static void transferCSS(String path, String compress) throws IOException {
      boolean isCompress = getEnableCompression(compress);
      try {
         StaticPages.assetManager.transferCSS(
            getCleanCSSPath(path),
            getCSSPath(path),
            isCompress
         );
      } catch (IOException ex){
         Logger.fatal(ex.getLocalizedMessage());
         System.exit(1);
      }
   }
   public static void transferImage(String path) throws IOException {
      try {
         StaticPages.assetManager.transferImage(
            FileUtils.getForcedRelativePath(path, "/")
         );
      } catch (IOException ex){
         Logger.fatal(ex.getLocalizedMessage());
         System.exit(1);
      }
   }
   public static void transferJS(String path, String compress) throws IOException {
      boolean isCompress = getEnableCompression(compress);
      try {
         StaticPages.assetManager.transferJS(
            getCleanJSPath(path),
            getJSPath(path),
            isCompress
         );
      } catch (IOException ex){
         Logger.fatal(ex.getLocalizedMessage());
         System.exit(1);
      }
   }

   public static void validateExternalURL(String path) {
      try {
         if(!externalResponses.containsKey(path)){
            Logger.info("Validating: "+path);
            assertPathHasLength(path);
            URL url = new URL(path);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setConnectTimeout(StaticPages.maxTimeToWaitForExternalLinkValidation);
            http.connect();

            externalResponses.put(path, new HttpExternalLinkResponse(path, "GET", http.getHeaderFields(), http.getResponseCode()));

            switch(http.getResponseCode()){
               case 200:
                  break;
               default:
                  Logger.fatal("External link validation failed for the following URL: "+path);
                  Logger.fatal("The status code of the http connection was: "+http.getResponseCode());
                  System.exit(1);
            }
         }
      } catch (SocketTimeoutException ex){
         Logger.fatal("A connection to the following URL couldn't be established during the configured timeout period: "+path);
         System.exit(1);
      } catch (IOException ex) {
         Logger.fatal("An IOException occurred while attempting to validate the following external URL: "+path);
         Logger.fatal("Here is the detailed message: "+ex.getMessage());
         System.exit(1);
      }
   }

   public static void validatePageReference(String path) {
      if(validatedPages.contains(path)){
         return;
      }
      try {
         assertPathHasLength(path);
         FilePath page = StaticPages.pagesDirPath.resolve(path +".xml");
         assertFileExists(page);
         validatedPages.add(path);
      } catch (IOException ex) {
         Logger.fatal("The following page doesn't exist: "+path);
         System.exit(1);
      }
   }
   */

   /**
    * Validates the existence of a document fragment within an xml file.  The
    * fragment must exist within the body element.
    *
    * @param path
    */
   /*
   public static void validateFragmentReference(String path, String fragment) {
      if(validatedFragments.contains(path+fragment)){
         return;
      }
      try {
         assertPathHasLength(path);
         FilePath page = StaticPages.pagesDirPath.resolve(path +".xml");
         if(fragment != null && fragment.trim().isEmpty()){
            throw new IOException("Empty document fragment not allowed: "+page+"#"+fragment);
         }
         assertFileExists(page);
         File toFile = page.toFile();
         DocumentBuilder builder = factory.newDocumentBuilder();
         Document xml = builder.parse(toFile);
         XPath xpath = xpathFactory.newXPath();
         xpath.setNamespaceContext(new NamespaceContext() {

            @Override
            public String getNamespaceURI(String prefix) {
               if("d".equals(prefix)){
                  return "default";
               }
               throw new IllegalArgumentException("Unknown prefix: d");
            }

            @Override
            public String getPrefix(String namespaceURI) {
               throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public Iterator getPrefixes(String namespaceURI) {
               throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
         });
         Node elementById = (Node)xpath.evaluate("//d:body//d:*[@id='"+fragment+"']", xml, XPathConstants.NODE);
         if(elementById == null){
            throw new IOException("Couldn't find the following fragment: "+page+"#"+fragment);
         }
         validatedFragments.add(path+fragment);
      } catch (IOException ex) {
         Logger.fatal(ex.getMessage());
         System.exit(1);
      } catch (ParserConfigurationException|SAXException|XPathExpressionException ex) {
         Logger.fatal("Couldn't parse the following xml file: "+path+"\n"+
                 "For the following reason: "+ex.getMessage(), 1);
         System.exit(1);
      }
   }
   */


}
