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

import com.spencernetdevelopment.arguments.StaticPagesArguments;
import com.spencernetdevelopment.arguments.StaticPagesTerminal;
import java.io.File;

/**
 *
 * @author Joseph Spencer
 */
public class StaticPages {
   public static FilePath jarDir;
   public static AssetManager assetManager;
   public static RewriteManager rewriteManager;
   public static GroupedAssetTransactionManager groupedAssetTransactionManager;

   public static boolean enableDevMode;
   public static boolean enableCompression;
   public static String assetFingerprint="";
   public static FilePath assetsDirPath;
   public static FilePath buildDirPath;
   public static FilePath pagesDirPath;
   public static FilePath viewsDirPath;
   public static FilePath projectDirPath;
   public static FilePath xmlResourcesDirPath;
   public static FilePath srcDirPath;
   public static FilePath xslDirPath;
   public static String assetPrefixInBrowser;
   public static String prefixToIgnoreFilesWith;
   public static int maxDataURISizeInBytes;
   public static int maxTimeToWaitForExternalLinkValidation;

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      try {
         String argumentsXmlPathString = StaticPages.class.getResource("/arguments.xml").getPath().replaceAll("^(?:file:)?(?:/(?=[A-Z]:/))?|^jar:|![^!]+$", "");
         FilePath argumentsXmlFilePath = FilePath.getFilePath(argumentsXmlPathString);

         jarDir = argumentsXmlFilePath.getParent();

         System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
             "com.icl.saxon.om.DocumentBuilderFactoryImpl");
         StaticPagesArguments arguments = StaticPagesTerminal.getArguments(args);

         Logger.isDebug = arguments.getEnableloggingdebug();
         Logger.isError = arguments.getEnableloggingerror();
         Logger.isFatal = arguments.getEnableloggingfatal();
         Logger.isInfo  = arguments.getEnablelogginginfo();
         Logger.isWarn  = arguments.getEnableloggingwarn();
         if(arguments.hasLogginglevel()){
            switch(arguments.getLogginglevel()){
               case 0:
               case 1:
               case 2:
               case 3:
               case 4:
                  Logger.logLevel = arguments.getLogginglevel();
                  break;
               default:
                  Logger.fatal("Invalid argument for logging level.  Expecting one of 0, 1, 2, 3 or 4.", 1);
            }
         }

         enableDevMode = arguments.getEnabledevmode();
         enableCompression = arguments.getEnablecompression();

         if(Logger.isDebug){
            Logger.debug("jarDir = "+jarDir.toString());
         }

         if(arguments.hasAssetprefixinbrowser()){
            assetPrefixInBrowser = arguments.getAssetprefixinbrowser();
            if("/".equals(assetPrefixInBrowser) || assetPrefixInBrowser.endsWith("/")){
               assetPrefixInBrowser = assetPrefixInBrowser.replaceAll("/+$", "");
            }
         } else {
            assetPrefixInBrowser="";
         }
         if(arguments.getEnableassetfingerprinting()){
            assetFingerprint = ".UTC"+(System.currentTimeMillis()/1000);
         }
         prefixToIgnoreFilesWith=arguments.getPrefixtoignorefiles();

         if(arguments.hasNewproject()){
            File sampleProjectDir = jarDir.resolve("project-template").toFile();
            if(!sampleProjectDir.exists()){
               end("Couldn't create a new project.  The project-template wasn't found next to the jar.", 1);
            }
            FileUtils.copyDirContentsToDir(sampleProjectDir, arguments.getNewproject());
            FileUtils.copyDirContentsToDir(sampleProjectDir, arguments.getNewproject().toPath().resolve("bin/project-template").toFile());
            msg("project-template created successfully in: "+arguments.getNewproject().toPath());
         }

         if(arguments.hasProjectdir()){
            System.out.println("Building all pages...");
            File projectDir = arguments.getProjectdir();
            projectDirPath = FilePath.getFilePath(projectDir.getAbsolutePath());
            pagesDirPath=projectDirPath.resolve("src/xml/pages");
            viewsDirPath=projectDirPath.resolve("src/xml/views");
            xmlResourcesDirPath=projectDirPath.resolve("src/xml/resources");
            buildDirPath=projectDirPath.resolve("build");
            srcDirPath=projectDirPath.resolve("src");
            xslDirPath=srcDirPath.resolve("xsl");
            assetsDirPath=srcDirPath.resolve("assets");
            assetManager = new AssetManager(assetsDirPath, buildDirPath);
            groupedAssetTransactionManager = new GroupedAssetTransactionManager(assetManager);
            rewriteManager = new RewriteManager(buildDirPath);
            maxDataURISizeInBytes=arguments.getMaxdataurisizeinbytes();
            maxTimeToWaitForExternalLinkValidation=arguments.getMaxwaittimetovalidateexternallink();
            FilePath defaultStylesheet = projectDirPath.resolve("src/xsl/pages/default.xsl");

            if(!Assertions.fileExists(defaultStylesheet.toFile())){
               end("No default stylesheet found.", 1);
            }

            if(arguments.getClean()){
               FileUtils.clearDirectory(buildDirPath.toFile());
            }
            HTMLBuilder htmlBuilder = new HTMLBuilder(buildDirPath, pagesDirPath);
            htmlBuilder.setDefaultStylesheet(defaultStylesheet.toFile());
            htmlBuilder.buildPages();
            rewriteManager.applyRewrites();
         } else {
            Logger.warn("No project dir was specified.  Some arguments may be ignored.");
         }
      } catch(Throwable exc){
         end("Failed for the following reason: "+exc, 1);
      }
   }


   public static void msg(String message){
      System.out.println(message);
   }
   public static void end(String message, int code){
      Logger.fatal(message, code);
   }
}
