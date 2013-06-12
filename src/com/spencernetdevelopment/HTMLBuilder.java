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
import static com.spencernetdevelopment.Logger.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.validation.Schema;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author Joseph Spencer
 */
public class HTMLBuilder {

   private final FilePath buildDirPath;
   private final FilePath xmlPagesDirPath;
   private final FileUtils fileUtils;
   private final String xmlPagesDirString;
   private final int xmlPagesDirStringLength;
   private File defaultStylesheet;
   private final DocumentBuilderFactory docBuilderFactory;
   private final DocumentBuilder docBuilder;
   private final TransformerFactory transformerFactory;
   private final Schema schema;
   private final StaticPagesConfiguration config;
   private final HTMLBuilderVisitor transformerVisitor;
   private final ExecutorService executorService;

   public HTMLBuilder(
      ExecutorService executorService,
      FilePath buildDirPath,
      FilePath pagesDirPath,
      FileUtils fileUtils,
      Schema schema,
      StaticPagesConfiguration config,
      HTMLBuilderVisitor transformerVisitor
   ) throws ParserConfigurationException,
            SAXException
   {
      docBuilderFactory = DocumentBuilderFactory.newInstance();
      docBuilderFactory.setNamespaceAware(true);
      docBuilder = docBuilderFactory.newDocumentBuilder();
      transformerFactory = TransformerFactory.newInstance();

      this.executorService=executorService;
      this.buildDirPath = buildDirPath;
      this.xmlPagesDirPath = pagesDirPath;
      this.fileUtils=fileUtils;
      xmlPagesDirString = xmlPagesDirPath.toString();
      xmlPagesDirStringLength = xmlPagesDirString.length();
      this.schema=schema;
      this.config=config;
      this.transformerVisitor=transformerVisitor;
   }

   public void setDefaultStylesheet(File defaultStylesheet) throws IOException, TransformerConfigurationException {
      assertStylesheetExists(defaultStylesheet);
      this.defaultStylesheet = defaultStylesheet;
   }

   public void buildPages()
      throws IOException,
             SAXException,
             TransformerException,
             URISyntaxException
   {
      if (defaultStylesheet == null) {
         throw new IllegalStateException("A default stylesheet is required to process xml files.");
      }

      ArrayList<Path> xmlPagesToBuild = new ArrayList<>();

      fileUtils.filePathsToArrayList(xmlPagesDirPath.toFile(), xmlPagesToBuild, ".xml");
      List<HTMLTask<Object>> htmlTasks = new ArrayList<>();

      for (Path xmlFilePath : xmlPagesToBuild) {
         htmlTasks.add(new HTMLTask(this, xmlFilePath));
      }
      try {
         executorService.invokeAll(htmlTasks);
      } catch (InterruptedException ex) {
         Logger.getLogger(HTMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
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
      if(isDebug)debug("preparing to build xml file: "+
              (xmlFilePath != null ? xmlFilePath.toString() : null));
      if (
         xmlFilePath != null &&
         !xmlFilePath.toFile().getName().startsWith(
            config.getPrefixToIgnoreFilesWith()
         )
      ) {
         Document xmlDocument = docBuilder.parse(xmlFilePath.toFile());
         xmlDocument.normalize();
         schema.newValidator().validate(new DOMSource(xmlDocument));
         FilePath outputFilePath = buildDirPath.resolve(xmlFilePath.toString().substring(xmlPagesDirStringLength + 1).replaceFirst("\\.xml$", ".html"));
         File htmlFile = outputFilePath.toFile();
         Node firstChild = xmlDocument.getDocumentElement();
         if(isDebug && firstChild == null)debug("firstChild was null");
         else if(isDebug)debug("firstChildName: "+firstChild.getLocalName());

         WrappedTransformer transformer;
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
      fileUtils.createFile(outputFile);
      xmlDocument.normalize();
      DOMSource xmlDoc = new DOMSource(xmlDocument);
      StreamResult resultStream = new StreamResult(outputFile);
      Node firstChild = xmlDocument.getDocumentElement();
      NamedNodeMap attributes = firstChild.getAttributes();
      Node stylesheetAttribute = attributes.getNamedItem("stylesheet");
      Transformer transformer;
      if (stylesheetAttribute != null) {
         String stylesheet = stylesheetAttribute.getNodeValue();
         if (stylesheet.trim().length() > 0) {
            FilePath stylesheetPath =
               config.getXslDirPath().resolve(stylesheet.concat(".xsl"));
            File stylesheetFile = stylesheetPath.toFile();
            assertStylesheetExists(stylesheetFile);
            StreamSource stylesheetStream = new StreamSource(stylesheetFile);
            transformer = transformerFactory.newTransformer(stylesheetStream);
         } else {
            throw new IllegalArgumentException("stylesheet attributes may not be empty.");
         }
      } else {
         transformer = transformerFactory.newTransformer(new StreamSource(defaultStylesheet));;
      }
      transformerVisitor.addDefaultParametersTo(transformer);
      transformer.setParameter("pagePath", outputFilePath.toString());
      transformer.setParameter("domainRelativePagePath", outputFile.getAbsolutePath().substring(buildDirPath.toString().length()));
      return new WrappedTransformer(transformer, xmlDoc, resultStream);
   }

   /**
    * Asserts that a file exists.
    *
    * @param stylesheet
    * @throws IOException
    */
   private void assertStylesheetExists(File stylesheet) throws IOException {
      if (stylesheet == null) {
         throw new NullPointerException("stylesheet was null.");
      }
      if(!stylesheet.isFile()) {
         throw new IOException(
            "This stylesheet doesn't exist or is not a file:\n   " +
            stylesheet.getAbsolutePath()
         );
      }
   }

}
