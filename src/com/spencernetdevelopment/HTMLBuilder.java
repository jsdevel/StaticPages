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
   private final File buildDir;
   private final FilePath buildDirPath;
   private final FilePath xmlPagesDirPath;
   private final String xmlPagesDirString;
   private final int xmlPagesDirStringLength;
   private File defaultStylesheet;
   private final DocumentBuilderFactory docBuilderFactory;
   private final DocumentBuilder docBuilder;
   private final TransformerFactory transformerFactory;
   private StreamSource xslStream;
   private Transformer xslTransformer;

   public HTMLBuilder(FilePath buildDirPath, FilePath pagesDirPath) throws ParserConfigurationException {
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

   public void setDefaultStylesheet(File defaultStylesheet) throws IOException, TransformerConfigurationException {
      if(defaultStylesheet==null || !defaultStylesheet.isFile() || !defaultStylesheet.exists()){
         throw new IOException("Can't set a stylesheet that isn't an existing file.");
      }
      this.defaultStylesheet=defaultStylesheet;
      xslStream = new StreamSource(defaultStylesheet);
      xslTransformer = transformerFactory.newTransformer(xslStream);
   }

   public void buildPages() throws IOException, SAXException, TransformerException {
      if(defaultStylesheet==null){
         throw new IOException("A default stylesheet is required to process xml files.");
      }

      ArrayList<Path> xmlPagesToBuild = new ArrayList<Path>();

      FileUtils.clearDirectory(buildDir);
      FileUtils.filePathsToArrayList(xmlPagesDirPath.toFile(), xmlPagesToBuild, ".xml");

      for(Path xmlFilePath : xmlPagesToBuild){
         buildPage(xmlFilePath);
      }
   }

   public void buildPage(Path xmlFilePath) throws SAXException, TransformerException, IOException {
      Document xmlDocument = docBuilder.parse(xmlFilePath.toFile());
      FilePath outputFilePath = buildDirPath.resolve(xmlFilePath.toString().substring(xmlPagesDirStringLength+1).replaceFirst("\\.xml$", ".html"));
      File htmlFile = outputFilePath.toFile();
      File alternateHTMLFile=null;

      Node alternateNameNode = xmlDocument.getFirstChild().getAttributes().getNamedItem("alternate-name");

      if(alternateNameNode != null){
         String alternateName = alternateNameNode.getNodeValue();
         transform(xmlDocument, outputFilePath.getParent().resolve(alternateName+".html").toFile());
      }
      transform(xmlDocument, htmlFile);
   }

   public void transform(Document xmlDocument, File outputFile) throws TransformerException, IOException {
      FileUtils.createFile(outputFile);
      DOMSource xmlDoc = new DOMSource(xmlDocument);
      StreamResult resultStream = new StreamResult(outputFile);
      xslTransformer.transform(xmlDoc, resultStream);
   }
}
