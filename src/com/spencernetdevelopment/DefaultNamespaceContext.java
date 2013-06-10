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

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author Joseph Spencer
 */
public class DefaultNamespaceContext implements NamespaceContext {

   @Override
   public String getNamespaceURI(String prefix) {
      if("d".equals(prefix)){
         return "default";
      }
      throw new IllegalArgumentException("Unknown prefix: d");
   }

   @Override
   public String getPrefix(String namespaceURI) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public Iterator getPrefixes(String namespaceURI) {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}