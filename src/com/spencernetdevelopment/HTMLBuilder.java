/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;

/**
 *
 * @author Joseph Spencer
 */
public class HTMLBuilder {
   private final File buildDir;
   private final Path buildDirPath;
   private final Path xmlPagesDirPath;
   private final String xmlPagesDirString;
   private final int xmlPagesDirStringLength;
   private File defaultStylesheet;
   private final DocumentBuilderFactory docBuilderFactory;
   private final DocumentBuilder docBuilder;
   private final TransformerFactory transformerFactory;
   private StreamSource xslStream;
   private Transformer xslTransformer;

   public HTMLBuilder(Path buildDirPath, Path pagesDirPath) throws Exception {
      docBuilderFactory = DocumentBuilderFactory.newInstance();
      docBuilderFactory.setNamespaceAware(true);
      docBuilder = docBuilderFactory.newDocumentBuilder();
      transformerFactory = TransformerFactory.newInstance();

      this.buildDirPath = buildDirPath;
      buildDir=buildDirPath.toFile();
      this.xmlPagesDirPath = pagesDirPath;
      xmlPagesDirString=xmlPagesDirPath.toString();
      xmlPagesDirStringLength=xmlPagesDirString.length();
   }

   public void setDefaultStylesheet(File defaultStylesheet) throws Exception{
      if(defaultStylesheet==null || !defaultStylesheet.isFile() || !defaultStylesheet.exists()){
         throw new Exception("Can't set a stylesheet that isn't an existing file.");
      }
      this.defaultStylesheet=defaultStylesheet;
      xslStream = new StreamSource(defaultStylesheet);
      xslTransformer = transformerFactory.newTransformer(xslStream);
   }

   public void buildPages() throws IOException, Exception {
      if(defaultStylesheet==null){
         throw new Exception("A default stylesheet is required to process xml files.");
      }

      ArrayList<Path> xmlPagesToBuild = new ArrayList<Path>();

      FileUtils.clearDirectory(buildDir);
      FileUtils.filePathsToArrayList(xmlPagesDirPath.toFile(), xmlPagesToBuild, ".xml");

      for(Path xmlFilePath : xmlPagesToBuild){
         buildPage(xmlFilePath);
      }
   }

   public void buildPage(Path xmlFilePath) throws Exception {
      Document xmlDocument = docBuilder.parse(xmlFilePath.toFile());
      DOMSource xmlDoc = new DOMSource(xmlDocument);
      File htmlFile = buildDirPath.resolve(xmlFilePath.toString().substring(xmlPagesDirStringLength+1).replaceFirst("\\.xml$", ".html")).toFile();
      FileUtils.createFile(htmlFile);

      StreamResult resultStream = new StreamResult(htmlFile);
      xslTransformer.transform(xmlDoc, resultStream);
   }
}
