package com.spencernetdevelopment;

import com.spencernetdevelopment.arguments.StaticPagesArguments;
import com.spencernetdevelopment.arguments.StaticPagesTerminal;
import java.io.File;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Joseph Spencer
 */
public class StaticPages {
   public static final int exit_code_bad_argument=1;
   public static final int exit_code_missing_default_stylesheet=2;
   private static final Logger logger = Logger.getLogger(StaticPages.class.getName());
   public static FilePath jarDir;
   public static AssetManager assetManager;

   public static FilePath assetsDirPath;
   public static FilePath buildDirPath;
   public static FilePath pagesDirPath;
   public static FilePath viewsDirPath;
   public static FilePath projectDirPath;
   public static FilePath xmlResourcesDirPath;
   public static FilePath srcDirPath;
   public static FilePath xslDirPath;
   public static String assetPrefixInBrowser="/";

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

         {//setup log4j.
            FilePath propFile = null;
            if(arguments.hasLogjproperties()){
               propFile = jarDir.resolve(arguments.getLogjproperties());
            } else if(jarDir.resolve("log4j.properties").toFile().isFile()){
               propFile = jarDir.resolve("log4j.properties");
            }

            if(propFile != null){
               if(arguments.hasLogjinterval()){
                  PropertyConfigurator.configureAndWatch(propFile.toString(), Long.parseLong(arguments.getLogjinterval()));
               } else {
                  PropertyConfigurator.configureAndWatch(propFile.toString());
               }
            } else {
               PropertyConfigurator.configure(StaticPages.class.getResourceAsStream("/log4j.properties"));
               logger.info("Using the internal log4j.properties file.  Run java -jar StaticPages.jar for more info.");
            }
         }

         if(logger.isDebugEnabled()){
            logger.debug("jarDir = "+jarDir.toString());
         }

         if(arguments.hasAssetprefixinbrowser()){
            assetPrefixInBrowser=arguments.getAssetprefixinbrowser();
            if(assetPrefixInBrowser.endsWith("/")){
               logger.warn("--asset-prefix-in-browser ended in '/'.  Removing '/' from the end now.");
               assetPrefixInBrowser = assetPrefixInBrowser.replaceAll("/+$", "");
            }
         }

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
            assetManager = new AssetManager(assetsDirPath, buildDirPath, arguments.getEnablecompression());
            FilePath defaultStylesheet = projectDirPath.resolve("src/xsl/pages/default.xsl");

            if(!Assertions.fileExists(defaultStylesheet.toFile())){
               end("No default stylesheet found.", exit_code_missing_default_stylesheet);
            }

            HTMLBuilder htmlBuilder = new HTMLBuilder(buildDirPath, pagesDirPath);
            htmlBuilder.setDefaultStylesheet(defaultStylesheet.toFile());
            htmlBuilder.buildPages();
         }
      } catch(Throwable exc){
         end("Failed for the following reason: "+exc, 1);
      }
   }


   public static void msg(String message){
      System.out.println(message);
   }
   public static void end(String message, int code){
      logger.log(Level.FATAL, message);
      System.exit(code);
   }
}
