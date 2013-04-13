/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment.xsl;

import com.spencernetdevelopment.FilePath;
import static com.spencernetdevelopment.xsl.FileFunctions.assertFileExists;
import static com.spencernetdevelopment.xsl.FileFunctions.assertPathHasLength;
import com.spencernetdevelopment.StaticPages;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Joseph Spencer
 */
public class Assets {
   public static final Logger LOGGER = Logger.getLogger(Assets.class);
   private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
   private static XPathFactory xpathFactory = XPathFactory.newInstance();

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
   public static String getCSS(String path, boolean compress) {
      try {
         return StaticPages.assetManager.getCSS(path, compress);
      } catch (IOException ex){
         LOGGER.fatal("Couldn't get: "+path);
         System.exit(1);
      }
      return null;
   }
   public static String getJS(String path, boolean compress) {
      try {
         return StaticPages.assetManager.getJS(path, compress);
      } catch (IOException ex){
         LOGGER.fatal("Couldn't get: "+path);
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
   public static void transferCSS(String path, boolean compress) throws IOException {
      try {
         StaticPages.assetManager.transferCSS(path, compress);
      } catch (IOException ex){
         LOGGER.fatal(ex.getLocalizedMessage());
         System.exit(1);
      }
   }
   public static void transferJS(String path, boolean compress) throws IOException {
      try {
         StaticPages.assetManager.transferJS(path, compress);
      } catch (IOException ex){
         LOGGER.fatal(ex.getLocalizedMessage());
         System.exit(1);
      }
   }
   public static void transferImage(String path) throws IOException {
      try {
         StaticPages.assetManager.transferImage(path);
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

   /**
    * Validates the existence of a document fragment within an xml file.  The
    * fragment must exist within the body element.
    *
    * @param path
    */
   public static void validateFragmentReference(String path, String fragment) {
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
      } catch (IOException ex) {
         LOGGER.fatal(ex.getMessage());
         System.exit(1);
      } catch (ParserConfigurationException|SAXException|XPathExpressionException ex) {
         LOGGER.fatal("Couldn't parse the following xml file: "+path, ex);
         System.exit(1);
      }
   }

}
