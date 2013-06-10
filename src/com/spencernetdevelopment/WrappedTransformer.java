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
   public synchronized void transform() throws TransformerException{
      if(!isTransformed){
         transformer.transform(xmlDoc, resultStream);
         isTransformed=true;
      }
   }
}