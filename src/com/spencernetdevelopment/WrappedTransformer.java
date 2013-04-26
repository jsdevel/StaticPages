/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author Joseph Spencer
 */
public class WrappedTransformer {
   private boolean isTransformed;
   private final Transformer transformer;
   private final DOMSource xmlDoc;
   private final StreamResult resultStream;

   public WrappedTransformer(Transformer transformer, DOMSource xmlDoc, StreamResult resultStream) {
      this.transformer = transformer;
      this.xmlDoc = xmlDoc;
      this.resultStream = resultStream;
   }
   public void setParameter(String name, Object value){
      transformer.setParameter(name, value);
   }
   public void transform() throws TransformerException{
      if(!isTransformed){
         transformer.transform(xmlDoc, resultStream);
         isTransformed=true;
      }
   }
}