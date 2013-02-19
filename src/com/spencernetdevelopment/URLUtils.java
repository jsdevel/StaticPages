/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

public final class URLUtils {

   private URLUtils() {
   }
   /**
    * used for the encodeURIComponent function
    */
   private static final Set<Character> dontNeedEncoding = new HashSet<Character>();

   static {
      // a-z
      for (int i = 97; i <= 122; ++i) {
         dontNeedEncoding.add((char) i);
      }
      // A-Z
      for (int i = 65; i <= 90; ++i) {
         dontNeedEncoding.add((char) i);
      }
      // 0-9
      for (int i = 48; i <= 57; ++i) {
         dontNeedEncoding.add((char) i);
      }

      // '()*
      for (int i = 39; i <= 42; ++i) {
         dontNeedEncoding.add((char) i);
      }
      dontNeedEncoding.add((char) 33); // !
      dontNeedEncoding.add((char) 45); // -
      dontNeedEncoding.add((char) 46); // .
      dontNeedEncoding.add((char) 95); // _
      dontNeedEncoding.add((char) 126); // ~
   }

   /**
    * See char[] encodeURIComponent(final char[] unfiltered).
    * Encodes a string as UTF-8.
    *
    * @param input
    * @return UTF-8 encoded string.
    */
   public static String encodeURIComponent(String input) throws UnsupportedEncodingException {
      return new String(new String(encodeURIComponent(input.toCharArray())).getBytes(), "UTF-8");
   }

   /**
    * Escapes all characters except the following: alphabetic, decimal digits, -
    * _ . ! ~ * ' ( )
    *
    * @param input A component of a URI
    * @return the escaped URI component
    */
   public static char[] encodeURIComponent(final char[] unfiltered) {
      if (unfiltered == null) {
         return null;
      }

      char[] filtered = new char[unfiltered.length * 4];
      int filteredIndex = 0;

      char c;
      for (int i = 0; i < unfiltered.length; ++i) {
         c = unfiltered[i];
         if (dontNeedEncoding.contains(c)) {
            filtered[filteredIndex++] = c;
         } else {
            char[] encoded = Integer.toHexString((int) c).toUpperCase().toCharArray();
            filtered[filteredIndex++] = '%';

            for (int j = 0; j < encoded.length; j++) {
               filtered[filteredIndex++] = encoded[j];
            }
         }
      }
      return filtered;
   }
}