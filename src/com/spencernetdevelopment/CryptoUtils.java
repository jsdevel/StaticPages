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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A utility class to provide various convenient crypto methods.
 * @author Joseph Spencer
 */
public class CryptoUtils {
   public static final String md5(String md5) {
      try {
         java.security.MessageDigest md = MessageDigest.getInstance("MD5");
         byte[] array = md.digest(md5.getBytes("UTF-8"));
         StringBuilder sb = new StringBuilder();
         for (int i = 0; i < array.length; ++i) {
            sb.append(
               Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3)
            );
         }
         return sb.toString();
      } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {}
      return null;
   }
}
