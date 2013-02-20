package com.spencernetdevelopment;

import com.spencernetdevelopment.arguments.StaticPagesArguments;
import com.spencernetdevelopment.arguments.StaticPagesTerminal;
import java.io.File;
import java.nio.file.Path;

/**
 *
 * @author Joseph Spencer
 */
public class StaticPages {
   public static final int exit_code_bad_argument=1;
   public static final int exit_code_missing_default_stylesheet=2;
   public static FilePath jarDir;

   public static FilePath assetsDirPath;
   public static FilePath buildDirPath;
   public static FilePath pagesDirPath;
   public static FilePath viewsDirPath;
   public static FilePath projectDirPath;
   public static FilePath srcDirPath;

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      try {
         jarDir = FilePath.getFilePath(StaticPages.class.getResource("/arguments.xml").getPath().replaceAll("^(?:file:)?(?:/(?=[A-Z]:/))?|^jar:|![^!]+$", "")).getParent();
         System.out.println(jarDir);

        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
             "com.icl.saxon.om.DocumentBuilderFactoryImpl");
         StaticPagesArguments arguments = StaticPagesTerminal.getArguments(args);

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
            File projectDir = arguments.getProjectdir();
            projectDirPath = FilePath.getFilePath(projectDir.getAbsolutePath());
            pagesDirPath=projectDirPath.resolve("src/xml/pages");
            viewsDirPath=projectDirPath.resolve("src/xml/views");
            buildDirPath=projectDirPath.resolve("build");
            srcDirPath=projectDirPath.resolve("src");
            assetsDirPath=srcDirPath.resolve("assets");
            FilePath buildDirPath = projectDirPath.resolve("build");
            FilePath pagesDirPath = projectDirPath.resolve("src/xml/pages");
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
      System.out.println(message);
      System.exit(code);
   }
}
