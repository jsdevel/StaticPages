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
package com.spencernetdevelopment.xsl;

/**
 *
 * @author Joseph Spencer
 */
public class Utils {

   /**
    * If the String is nothing but white space, then an empty string is
    * returned, otherwise the String is returned with all occurrences of white
    * space reduced to a single space.
    *
    * <li></li>
    * </ul>
    * @param text
    * @return
    */
   public static String normalizeSpace(String text){
      if(text == null){
         return "";
      }
      return text.replaceAll("^\\s+$", "").replaceAll("\\s+", " ");
   }
}
