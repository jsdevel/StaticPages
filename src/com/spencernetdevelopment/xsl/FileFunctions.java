/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment.xsl;

import com.spencernetdevelopment.FileUtils;
import com.spencernetdevelopment.StaticPages;
import java.io.*;

/**
 *
 * @author Joseph Spencer
 */
public class FileFunctions {
   public static String getAsset(String path) throws IOException {
      File file = StaticPages.assetsDirPath.resolve(path).toFile();
      return FileUtils.getString(file);
   }
}
