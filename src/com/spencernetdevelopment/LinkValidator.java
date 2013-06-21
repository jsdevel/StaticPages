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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
public class LinkValidator {
   private final Set<String> failedFragments = new HashSet<>();
   private final Set<String> validatedFragments = new HashSet<>();
   private final Set<String> failedPages = new HashSet<>();
   private final Set<String> validatedPages = new HashSet<>();
   private final Map<String, ExternalLinkValidator<Object>> validators;
   private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
   private XPathFactory xpathFactory = XPathFactory.newInstance();
   private DefaultNamespaceContext defaultNamespaceContext;
   private StaticPagesConfiguration config;

   public LinkValidator(
      Map<String, ExternalLinkValidator<Object>> validators,
      StaticPagesConfiguration config,
      DefaultNamespaceContext defaultNamespaceContext
   ){
      this.validators=validators;
      this.config=config;
      this.defaultNamespaceContext=defaultNamespaceContext;
   }

   public void validateExternalURL(String path) throws MalformedURLException {
      synchronized(validators){
         if(!validators.containsKey(path)){
            validators.put(
               path,
               new ExternalLinkValidator(config, new URL(path))
            );
         }
      }
   }

   public void validatePageReference(String path) throws IOException {
      if(hasBeenValidated(path, validatedPages, failedPages)){
         return;
      }
      FilePath page = getPageFilePath(path);
      if(!page.toFile().isFile()){
         synchronized(failedPages){
            failedPages.add(path);
         }
         throw new IOException("The following page doesn't exist: "+page);
      }
   }

   /**
    * Validates the existence of a document fragment within an xml file.  The
    * fragment must exist within the body element.
    *
    * @param path
    */
   public void validateFragmentReference(String path, String fragment)
      throws IOException
   {
      String key = path+"#"+fragment;
      if(hasBeenValidated(key, validatedFragments, failedFragments)){
         return;
      }
      validatePageReference(path);
      try {
         FilePath page = getPageFilePath(path);
         if(fragment != null && fragment.trim().isEmpty()){
            throw new IllegalArgumentException(
               "Empty document fragment not allowed: "+page+"#"+fragment
            );
         }
         File toFile = page.toFile();
         if(!toFile.isFile()){
            throw new IOException(
               "The following page doesn't exist: "+page.toString()
            );
         }
         DocumentBuilder builder = factory.newDocumentBuilder();
         Document xml = builder.parse(toFile);
         XPath xpath = xpathFactory.newXPath();
         xpath.setNamespaceContext(defaultNamespaceContext);
         Node elementById = (Node)xpath
            .evaluate(
               "//d:body//d:*[@id='"+fragment+"']",
               xml,
               XPathConstants.NODE
            );
         if(elementById == null){
            throw new IllegalArgumentException(
               "Couldn't find the following fragment: "+page+"#"+fragment
            );
         }
      } catch(
         ParserConfigurationException|
         SAXException|
         IllegalArgumentException|
         XPathExpressionException ex
      ){
         synchronized(failedFragments){
            failedFragments.add(key);
         }
         throw new IOException(
            "The following error occurred: "+ex.getMessage()
         );
      }
   }

   public boolean hasBeenValidated(
      String key,
      Set<String> validatedSet,
      Set<String> failedSet
   )
      throws IOException
   {
      synchronized(failedSet){
         if(failedSet.contains(key)){
            throw new IOException(
               "Validation for the following link has already failed: "+key
            );
         }
      }
      synchronized(validatedSet){
         if(validatedSet.contains(key)){
            return true;
         }
         validatedSet.add(key);
         return false;
      }
   }

   private FilePath getPageFilePath(String path) throws IOException {
      return config.getPagesDirPath().resolve(
         path.endsWith("/")?
            path + "index.xml":
            path.endsWith(".xml")?
               path:
               path + ".xml"
      );
   }
}