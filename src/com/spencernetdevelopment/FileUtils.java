/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;

/**
 *
 * @author Joseph Spencer
 */
public class FileUtils {
   public static void clearDirectory(File directory) throws IOException {
      clearDirectory(directory, true);
   }
   public static void clearDirectory(File directory, boolean preserveBase) throws IOException{
      if(directory.isDirectory()){
         Files.walkFileTree(directory.toPath(), new DirectoryCleaner(directory.toPath(), preserveBase));
      }
   }
   public static void copyDirContentsToDir(File fromDir, File toDir) throws Exception{
      createDir(fromDir);
      createDir(toDir);
      EnumSet<FileVisitOption> options = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
      Files.walkFileTree(
         fromDir.toPath(),
         options,
         Integer.MAX_VALUE,
         new DirectoryDuplicator(fromDir, toDir)
      );
   }
   public static void createDir(File dir) throws Exception{
      if(dir != null){
         if(!dir.exists()){
            dir.mkdirs();
         } else if(!dir.isDirectory()){
            throw new Exception("Can't convert a file to a directory: "+dir.getAbsolutePath());
         }
      } else {
         throw new Exception("Can't create a dir from null.");
      }
   }
   public static void createFile(File file) throws Exception {
      if(file == null){
         throw new Exception("Unable to create file from null.");
      }
      if(!file.exists()){
         file.getParentFile().mkdirs();
         file.createNewFile();
      } else if(!file.isFile()){
         throw new Exception("The file exists, but it isn't a file.");
      }
   }
   public static void filePathsToArrayList(File directory, ArrayList<Path> filePaths) throws IOException {
      filePathsToArrayList(directory, filePaths, null);
   }
   public static void filePathsToArrayList(File directory, final ArrayList<Path> filePaths, final String extension) throws IOException {
      if(directory!=null && directory.isDirectory() && filePaths != null){
         Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
               if(extension == null || file.toString().endsWith(extension)){
                  filePaths.add(file);
               }
               return FileVisitResult.CONTINUE;
            }
         });
      }
   }

   public static String getString(File file) throws IOException {
      return new String(getChars(file));
   }
   public static char[] getChars(File file)
      throws IOException
   {
      String str;

      FileReader reader = new FileReader(file);
      BufferedReader buffer = new BufferedReader(reader);
      String line;
      StringBuilder result = new StringBuilder();
      while( ( line = buffer.readLine() ) != null ) {
         result.append(line).append("\n");
      }
      str = result.toString();
      buffer.close();
      reader.close();

      return str.toCharArray();
   }
   public static void putString(File file, String contents ) throws IOException {
      FileWriter output = new FileWriter( file );
      output.write( contents, 0, contents.length() );
      output.close();
   }
}
