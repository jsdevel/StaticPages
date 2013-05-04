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
import static com.spencernetdevelopment.Logger.*;
import java.io.File;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

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
   public static String assetPrefixInBrowser="";
   public static String prefixToIgnoreFilesWith;
   public static int maxDataURISizeInBytes;
   public static int maxTimeToWaitForExternalLinkValidation;

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      try {
         String argumentsXmlPathString = StaticPages.class.getResource("/arguments.xml").getPath().replaceAll("^(?:file:)?(?:/(?=[A-Z]:/))?|^jar:|![^!]+$", "");
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
               case 5:
                  Logger.logLevel = arguments.getLogginglevel();
                  break;
               default:
                  Logger.fatal(
                     "Invalid argument for logging level.\n"+
                     "Expecting one of 0, 1, 2, 3, 4 or 5.",
                     1
                  );
            }
         }

         if(isDebug)debug("user.dir:  "+System.getProperty("user.dir"));
         if(isDebug)debug("user.home: "+System.getProperty("user.home"));
         if(isDebug)debug("user.name: "+System.getProperty("user.name"));
         if(isDebug)debug("path to arguments.xml: "+argumentsXmlPathString);

         FilePath argumentsXmlFilePath = FilePath.getFilePath(argumentsXmlPathString);
         jarDir = argumentsXmlFilePath.getParent();
         enableDevMode = arguments.getEnabledevmode();
         enableCompression = arguments.getEnablecompression();

         if(isDebug)debug("jarDir = "+jarDir.toString());

         if(enableDevMode && arguments.hasDevassetprefixinbrowser()){
            assetPrefixInBrowser = arguments.getDevassetprefixinbrowser();
         } else if(arguments.hasAssetprefixinbrowser()){
            assetPrefixInBrowser = arguments.getAssetprefixinbrowser();
         }

         if("/".equals(assetPrefixInBrowser) || assetPrefixInBrowser.endsWith("/")){
            assetPrefixInBrowser = assetPrefixInBrowser.replaceAll("/+$", "");
         }

         if(isDebug)debug("asset prefix in browser: "+assetPrefixInBrowser);

         if(arguments.getEnableassetfingerprinting()){
            assetFingerprint = ".UTC"+(System.currentTimeMillis()/1000);
         }
         if(isDebug)debug("asset fingerprint: "+assetFingerprint);

         prefixToIgnoreFilesWith=arguments.getPrefixtoignorefiles();

         if(isDebug)debug("prefix to ignore files with: "+prefixToIgnoreFilesWith);

         if(arguments.hasNewproject()){
            File sampleProjectDir = jarDir.resolve("project-template").toFile();
            if(isDebug)debug("sample project dir: "+sampleProjectDir.getAbsolutePath());
            if(!sampleProjectDir.exists()){
               fatal("Couldn't create a new project.  The project-template wasn't found next to the jar.", 1);
            }
            FileUtils.copyDirContentsToDir(sampleProjectDir, arguments.getNewproject());
            FileUtils.copyDirContentsToDir(sampleProjectDir, arguments.getNewproject().toPath().resolve("bin/project-template").toFile());
            msg("project-template created successfully in: "+arguments.getNewproject().toPath());
         }

         if(arguments.hasProjectdir()){
            System.out.println("Building all pages...");
            File projectDir = arguments.getProjectdir();
            if(isDebug)debug("projectDir: "+projectDir.getAbsolutePath());
            projectDirPath = FilePath.getFilePath(projectDir.getAbsolutePath());
            if(isDebug)debug("projectDirPath: "+projectDirPath.toString());
            pagesDirPath=projectDirPath.resolve("src/xml/pages");
            if(isDebug)debug("pagesDirPath: "+pagesDirPath.toString());
            viewsDirPath=projectDirPath.resolve("src/xml/views");
            if(isDebug)debug("viewsDirPath: "+viewsDirPath.toString());
            xmlResourcesDirPath=projectDirPath.resolve("src/xml/resources");
            if(isDebug)debug("xmlResourcesDirPath: "+xmlResourcesDirPath.toString());
            buildDirPath=projectDirPath.resolve("build");
            if(isDebug)debug("buildDirPath: "+buildDirPath.toString());
            srcDirPath=projectDirPath.resolve("src");
            if(isDebug)debug("srcDirPath: "+srcDirPath.toString());
            xslDirPath=srcDirPath.resolve("xsl");
            if(isDebug)debug("xslDirPath: "+xslDirPath.toString());
            assetsDirPath=srcDirPath.resolve("assets");
            if(isDebug)debug("assetDirPath: "+assetsDirPath.toString());
            FilePath defaultStylesheet = projectDirPath.resolve("src/xsl/pages/default.xsl");
            if(isDebug)debug("defaultStylesheet: "+defaultStylesheet.toString());

            assetManager = new AssetManager(assetsDirPath, buildDirPath);
            groupedAssetTransactionManager = new GroupedAssetTransactionManager(assetManager);
            rewriteManager = new RewriteManager(buildDirPath);

            maxDataURISizeInBytes=arguments.getMaxdataurisizeinbytes();
            maxTimeToWaitForExternalLinkValidation=arguments.getMaxwaittimetovalidateexternallink();

            if(!Assertions.fileExists(defaultStylesheet.toFile())){
               fatal("No default stylesheet found.", 1);
            }

            if(arguments.getClean()){
               FileUtils.clearDirectory(buildDirPath.toFile());
            }

            StreamSource pageXSD = new StreamSource(
                    StaticPages.class.getResourceAsStream("/page.xsd"));
            if(isDebug && pageXSD == null)debug("pageXSD was null");
            SchemaFactory schemaFactory = SchemaFactory
               .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schemaFile = schemaFactory.newSchema(pageXSD);
            Validator validator = schemaFile.newValidator();
            HTMLBuilder htmlBuilder = new HTMLBuilder(
                  buildDirPath,
                  pagesDirPath,
                  validator
               );
            htmlBuilder.setDefaultStylesheet(defaultStylesheet.toFile());
            htmlBuilder.buildPages();
            rewriteManager.applyRewrites();
         } else {
            Logger.warn("No project dir was specified.  Some arguments may be ignored.");
         }
      } catch(Throwable exc){
         fatal("Failed for the following reason: ", 1, exc);
      }
   }


   public static void msg(String message){
      System.out.println(message);
   }
}
