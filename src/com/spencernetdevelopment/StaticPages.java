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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

/**
 *
 * @author Joseph Spencer
 */
public class StaticPages {
   public static FilePath jarDir;

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      try {
         final FileUtils fileUtils = new FileUtils();
         String argumentsXmlPathString = StaticPages.class.
                 getResource("/arguments.xml").getPath().
                 replaceAll("^(?:file:)?(?:/(?=[A-Z]:/))?|^jar:|![^!]+$", "");
         final StaticPagesArguments arguments = StaticPagesTerminal.getArguments(args);
         final StaticPagesConfiguration builtConfig =
            buildStaticPagesConfiguration(arguments);
         final FilePath buildDirPath = builtConfig.getBuildDirPath();
         final FilePath projectDirPath = builtConfig.getProjectDirPath();

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
         if(isDebug)debug("jarDir = "+jarDir.toString());

         if(arguments.getClean()){
            fileUtils.clearDirectory(buildDirPath.toFile());
         }

         if(arguments.hasNewproject()){
            File sampleProjectDir = jarDir.resolve("project-template").toFile();
            if(isDebug)debug("sample project dir: "+sampleProjectDir.getAbsolutePath());
            if(!sampleProjectDir.exists()){
               fatal("Couldn't create a new project.  The project-template wasn't found next to the jar.", 1);
            }
            fileUtils.copyDirContentsToDir(sampleProjectDir, arguments.getNewproject());
            info("project-template copied to: "+arguments.getNewproject().toPath());
         }

         if(arguments.hasProjectdir()){

            showFilePathDebugStatements(builtConfig);
            if(!buildDirPath.toFile().isDirectory()){
               if(isDebug)debug("buildDir didn't exist.  Creating it now...");
               fileUtils.createDir(buildDirPath.toFile());
            }

            FilePath defaultStylesheet = projectDirPath.resolve("src/xsl/pages/default.xsl");
            if(isDebug)debug("defaultStylesheet: "+defaultStylesheet.toString());
            if(!defaultStylesheet.toFile().isFile()){
               fatal("No default stylesheet found.", 1);
            }

            ExecutorService executorService = Executors.newCachedThreadPool();
            AssetResolver assetResolver = new AssetResolver(builtConfig);
            AssetManager assetManager = new AssetManager(
               builtConfig.getAssetsDirPath(),
               buildDirPath,
               fileUtils,
               getVariables(arguments),
               builtConfig,
               assetResolver
            );
            GroupedAssetTransactionManager groupedAssetTransactionManager =
               new GroupedAssetTransactionManager(
                  assetManager,
                  assetResolver,
                  builtConfig,
                  new FileUtils()
               );
            RewriteManager rewriteManager = new RewriteManager(
               buildDirPath,
               fileUtils
            );

            StreamSource pageXSD = new StreamSource(
                    StaticPages.class.getResourceAsStream("/page.xsd"));
            SchemaFactory schemaFactory = SchemaFactory
               .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schemaFile = schemaFactory.newSchema(pageXSD);

            System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
               "com.icl.saxon.om.DocumentBuilderFactoryImpl");
            DefaultNamespaceContext defaultNamespaceContext =
               new DefaultNamespaceContext();
            Map<String, ExternalLinkValidator<Object>> validators = Collections
               .synchronizedMap(new HashMap<String, ExternalLinkValidator<Object>>());
            LinkValidator linkValidator = new LinkValidator(
               validators,
               builtConfig,
               defaultNamespaceContext
            );
            HTMLBuilderVisitorImpl htmlBuilderVisitor =
               new HTMLBuilderVisitorImpl(
                  assetManager,
                  assetResolver,
                  rewriteManager,
                  builtConfig,
                  groupedAssetTransactionManager,
                  linkValidator
               );
            HTMLBuilder htmlBuilder = new HTMLBuilder(
               executorService,
               buildDirPath,
               builtConfig.getPagesDirPath(),
               fileUtils,
               schemaFile,
               builtConfig,
               htmlBuilderVisitor
            );
            htmlBuilder.setDefaultStylesheet(defaultStylesheet.toFile());

            info("Building all pages...");
            htmlBuilder.buildPages();
            info("Finished building pages.");

            List<Callable<Object>> postOperations = new ArrayList<>();
            synchronized(validators){
               if(validators.size() > 0){
                  for(Callable<Object> obj:validators.values()){
                     postOperations.add(obj);
                  }
               }
            }
            List<GroupedAssetTask<Object>> groupedAssetTasks =
               groupedAssetTransactionManager.getGroupedAssetTasks();

            synchronized(groupedAssetTasks){
               if(groupedAssetTasks.size() > 0){
                  for(Callable<Object> obj: groupedAssetTasks){
                     postOperations.add(obj);
                  }
               }
            }

            synchronized(postOperations){
               if(postOperations.size() > 0){
                  executorService.invokeAll(postOperations);
               }
            }
            executorService.shutdown();
            rewriteManager.applyRewrites();
            if(builtConfig.isEnableDevMode()){
               createRefreshJS(
                  buildDirPath.resolve("refresh.js").toFile(),
                  System.currentTimeMillis(),
                  fileUtils
               );
            }
         } else {
            Logger.warn("No project dir was specified.  Some arguments may be ignored.");
         }
      } catch(Throwable exc){
         fatal("Failed for the following reason: ", 1, exc);
      }
   }

   private static Properties getVariables(StaticPagesArguments arguments) throws IOException {
      Properties variables = new Properties();
      variables.load(
         StaticPages.class.getResourceAsStream(
            "/default_variables.properties"
         )
      );

      if(arguments.hasVariables()){
         File variablesFile = arguments.getVariables();
         if(variablesFile.isFile()){
            if(isDebug){
               debug("Attempting to use user supplied variables.");
               debug(
                  "The path to the variables is: "+
                  variablesFile.getAbsolutePath()
               );
            }
            variables.load(new FileReader(variablesFile));
         } else {
            throw new IOException(
               "The supplied variables path didn't result in a file: "+
               "'"+variablesFile.getAbsolutePath()+"' was the path."
            );
         }
      }
      return variables;
   }

   private static StaticPagesConfiguration buildStaticPagesConfiguration(
      StaticPagesArguments arguments
   ) throws IOException{
      StaticPagesConfiguration.Builder config =
            new StaticPagesConfiguration.Builder();

      config
         .setEnableCompression(arguments.getEnablecompression())
         .setEnableDevMode(arguments.getEnabledevmode());

      if(
         arguments.getEnabledevmode() &&
         arguments.hasDevassetprefixinbrowser()
      ){
         config.setAssetPrefixInBrowser(
            arguments.getDevassetprefixinbrowser()
         );
      } else if(arguments.hasAssetprefixinbrowser()){
         config.setAssetPrefixInBrowser(arguments.getAssetprefixinbrowser());
      }

      if(arguments.getEnableassetfingerprinting()){
         config.setAssetFingerprint(
            ".UTC"+(System.currentTimeMillis()/1000)
         );
      }

      config.setPrefixToIgnoreFilesWith(arguments.getPrefixtoignorefiles());

      File projectDir = arguments.getProjectdir();
      if(isDebug)debug("projectDir: "+projectDir.getAbsolutePath());
      FilePath projectDirPath = FilePath.getFilePath(
               projectDir.getAbsolutePath()
            );
      FilePath buildDirPath = projectDirPath.resolve("build");
      FilePath srcDirPath = projectDirPath.resolve("src");
      config
         .setProjectDirPath(projectDirPath)
         .setPagesDirPath(projectDirPath.resolve("src/xml/pages"))
         .setViewsDirPath(projectDirPath.resolve("src/xml/views"))
         .setXmlResourcesDirPath(
            projectDirPath.resolve("src/xml/resources")
         )
         .setBuildDirPath(buildDirPath)
         .setSrcDirPath(srcDirPath)
         .setXslDirPath(srcDirPath.resolve("xsl"))
         .setAssetsDirPath(srcDirPath.resolve("assets"))
         .setMaxDataURISizeInBytes(
            arguments.getMaxdataurisizeinbytes()
         )
         .setMaxTimeToWaitForExternalLinkValidation(
            arguments.getMaxwaittimetovalidateexternallink()
         );
      return config.build();
   }

   private static void showFilePathDebugStatements(
      StaticPagesConfiguration config
   ){
      FilePath projectDirPath = config.getProjectDirPath();
      FilePath pagesDirPath = config.getPagesDirPath();
      FilePath viewsDirPath = config.getViewsDirPath();
      FilePath xmlResourcesDirPath = config.getXmlResourcesDirPath();
      FilePath buildDirPath = config.getBuildDirPath();
      FilePath srcDirPath = config.getSrcDirPath();
      FilePath xslDirPath = config.getXslDirPath();
      FilePath assetsDirPath = config.getAssetsDirPath();
      if(isDebug)debug("asset prefix in browser: "+config.getAssetPrefixInBrowser());
      if(isDebug)debug("asset fingerprint: "+config.getAssetFingerprint());
      if(isDebug)debug("prefix to ignore files with: "+config.getPrefixToIgnoreFilesWith());
      if(isDebug)debug("projectDirPath: "+projectDirPath.toString());
      if(isDebug)debug("pagesDirPath: "+pagesDirPath.toString());
      if(isDebug)debug("viewsDirPath: "+viewsDirPath.toString());
      if(isDebug)debug("xmlResourcesDirPath: "+xmlResourcesDirPath.toString());
      if(isDebug)debug("buildDirPath: "+buildDirPath.toString());
      if(isDebug)debug("srcDirPath: "+srcDirPath.toString());
      if(isDebug)debug("xslDirPath: "+xslDirPath.toString());
      if(isDebug)debug("assetDirPath: "+assetsDirPath.toString());
   }

   private static void createRefreshJS(
      File output,
      long timestamp,
      FileUtils fileUtils
   )
      throws IOException
   {
      String content;
      if(isDebug)debug("creating refresh.js");
      InputStream refreshJS = StaticPages.class.getResourceAsStream(
                                                   "/refresh.js");
      if(refreshJS != null){
         content = new Scanner(refreshJS, "UTF-8").useDelimiter("\\A").next().
            replace("stamp=0", "stamp="+timestamp);
         fileUtils.createFile(output);
         fileUtils.putString(output, content);
      } else {
         if(isDebug)debug("couldn't find refresh.js in the jar");
      }
   }
}
