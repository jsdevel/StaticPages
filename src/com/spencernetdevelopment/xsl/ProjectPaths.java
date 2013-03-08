package com.spencernetdevelopment.xsl;

import com.spencernetdevelopment.StaticPages;

/**
 *
 * @author Joseph Spencer
 */
public class ProjectPaths {
   public static String getProjectPath(){
      return StaticPages.projectDirPath.toUnix();
   }
   public static String getXmlResourcesPath(){
      return StaticPages.xmlResourcesDirPath.toUnix();
   }
}
