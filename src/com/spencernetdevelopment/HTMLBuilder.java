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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Joseph Spencer
 */
public class HTMLBuilder {

   private final FilePath buildDirPath;
   private final FilePath xmlPagesDirPath;
   private final String xmlPagesDirString;
   private final int xmlPagesDirStringLength;
   private File defaultStylesheet;
   private final DocumentBuilderFactory docBuilderFactory;
   private final DocumentBuilder docBuilder;
   private final TransformerFactory transformerFactory;
   private StreamSource xslStream;
   private Transformer defaultXSLTransformer;
   private Map<String, Transformer> pageTransformers;

   public HTMLBuilder(FilePath buildDirPath, FilePath pagesDirPath) throws ParserConfigurationException {
      docBuilderFactory = DocumentBuilderFactory.newInstance();
      docBuilderFactory.setNamespaceAware(true);
      docBuilder = docBuilderFactory.newDocumentBuilder();
      transformerFactory = TransformerFactory.newInstance();

      this.buildDirPath = buildDirPath;
      this.xmlPagesDirPath = pagesDirPath;
      xmlPagesDirString = xmlPagesDirPath.toString();
      xmlPagesDirStringLength = xmlPagesDirString.length();
   }

   public void setDefaultStylesheet(File defaultStylesheet) throws IOException, TransformerConfigurationException {
      assertStylesheetExists(defaultStylesheet);
      this.defaultStylesheet = defaultStylesheet;
      xslStream = new StreamSource(defaultStylesheet);
      defaultXSLTransformer = transformerFactory.newTransformer(xslStream);
      addDefaultParameters(defaultXSLTransformer);
   }

   public void buildPages() throws IOException, SAXException, TransformerException {
      if (defaultStylesheet == null) {
         throw new IllegalStateException("A default stylesheet is required to process xml files.");
      }

      ArrayList<Path> xmlPagesToBuild = new ArrayList<>();

      FileUtils.filePathsToArrayList(xmlPagesDirPath.toFile(), xmlPagesToBuild, ".xml");

      for (Path xmlFilePath : xmlPagesToBuild) {
         buildPage(xmlFilePath);
      }
   }

   /**
    * Builds an xml file within the xmlPagesDir. Converts it to HTML in the
    * build directory.
    *
    * @param xmlFilePath
    * @throws SAXException
    * @throws TransformerException
    * @throws IOException
    */
   public void buildPage(Path xmlFilePath) throws SAXException, TransformerException, IOException {
      if (xmlFilePath != null && !xmlFilePath.toFile().getName().startsWith(StaticPages.prefixToIgnoreFilesWith)) {
         Document xmlDocument = docBuilder.parse(xmlFilePath.toFile());
         FilePath outputFilePath = buildDirPath.resolve(xmlFilePath.toString().substring(xmlPagesDirStringLength + 1).replaceFirst("\\.xml$", ".html"));
         File htmlFile = outputFilePath.toFile();

         Node alternateNameNode = xmlDocument.getFirstChild().getAttributes().getNamedItem("alternate-name");

         WrappedTransformer transformer;
         if (alternateNameNode != null) {
            String alternateName = alternateNameNode.getNodeValue();
            transformer = getTransformer(xmlDocument, outputFilePath.getParent().resolve(alternateName + ".html").toFile(), xmlFilePath);
            transformer.setParameter("enableRewrites", false);
            transformer.transform();
         }
         transformer = getTransformer(xmlDocument, htmlFile, xmlFilePath);
         transformer.setParameter("enableRewrites", true);
         transformer.transform();
      }
   }

   /**
    * Get a transformer from a path relative to src/xsl. The path gets '.xsl'
    * appended to it prior to processing.
    *
    * @param stylesheet
    * @return Transformer
    * @throws TransformerConfigurationException
    * @throws IOException
    */
   public WrappedTransformer getTransformer(
      Document xmlDocument,
      File outputFile,
      Path outputFilePath
   ) throws TransformerException, IOException {
      FileUtils.createFile(outputFile);
      DOMSource xmlDoc = new DOMSource(xmlDocument);
      StreamResult resultStream = new StreamResult(outputFile);

      Node stylesheetAttribute = xmlDocument.getFirstChild().getAttributes().getNamedItem("stylesheet");
      Transformer transformer;
      if (stylesheetAttribute != null) {
         String stylesheet = stylesheetAttribute.getNodeValue();
         if (stylesheet.trim().length() > 0) {
            if (pageTransformers == null) {
               pageTransformers = new HashMap<>();
            }

            if (pageTransformers.containsKey(stylesheet)) {
               transformer = pageTransformers.get(stylesheet);
            } else {
               FilePath stylesheetPath = StaticPages.xslDirPath.resolve(stylesheet.concat(".xsl"));
               File stylesheetFile = stylesheetPath.toFile();

               assertStylesheetExists(stylesheetFile);

               StreamSource stylesheetStream = new StreamSource(stylesheetFile);
               Transformer XSLTransformer = transformerFactory.newTransformer(stylesheetStream);
               pageTransformers.put(stylesheet, XSLTransformer);
               addDefaultParameters(XSLTransformer);
               transformer = XSLTransformer;
            }
         } else {
            throw new IllegalArgumentException("stylesheet attributes may not be empty.");
         }
      } else {
         transformer = defaultXSLTransformer;
      }
      transformer.setParameter("pagePath", outputFilePath.toString());
      transformer.setParameter("domainRelativePagePath", outputFile.getAbsolutePath().substring(StaticPages.buildDirPath.toString().length()));
      return new WrappedTransformer(transformer, xmlDoc, resultStream);
   }

   /**
    * Asserts that a file exists.
    *
    * @param stylesheet
    * @throws IOException
    */
   private void assertStylesheetExists(File stylesheet) throws IOException {
      if (!Assertions.fileExists(stylesheet)) {
         if (stylesheet == null) {
            throw new IOException("stylesheet was null.");
         }
         throw new IOException("This stylesheet doesn't exist:\n   " + stylesheet.getAbsolutePath());
      }
   }

   /**
    * Adds default parameters to a Transformer.
    *
    * @param xslt
    */
   private void addDefaultParameters(Transformer xslt) {
      xslt.setParameter("assetPrefixInBrowser", StaticPages.assetPrefixInBrowser);
      xslt.setParameter("enableDevMode", StaticPages.enableDevMode);
   }
}
