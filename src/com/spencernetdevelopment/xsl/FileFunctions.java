/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment.xsl;

import com.spencernetdevelopment.FileUtils;
import com.spencernetdevelopment.StaticPages;
import java.io.IOException;

/**
 *
 * @author Joseph Spencer
 */
public class FileFunctions {
   public static String getAsset(String path) throws IOException {
      return FileUtils.getString(StaticPages.assetsDirPath.resolve(path).toFile());
   }
}
