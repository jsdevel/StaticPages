/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
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
   public static void copyDirContentsToDir(File fromDir, File toDir) throws IOException{
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
   public static void copyFile(File inputFile, File outputFile) throws IOException {
      if(inputFile == null){
         throw new IOException("Input file was null.");
      }
      if(!inputFile.isFile()){
         throw new IOException("Can't copy a non-existent file: "+inputFile.getAbsolutePath());
      }
      createFile(outputFile);
      FileChannel source = null;
      FileChannel destination = null;
      try {
         source = new FileInputStream(inputFile).getChannel();
         destination = new FileOutputStream(outputFile).getChannel();
         destination.transferFrom(source, 0, source.size());
      } finally {
         if(source != null) {
               source.close();
         }
         if(destination != null) {
               destination.close();
         }
      }
   }
   public static void createDir(File dir) throws IOException{
      if(dir != null){
         if(!dir.exists()){
            dir.mkdirs();
         } else if(!dir.isDirectory()){
            throw new IOException("Can't convert a file to a directory: "+dir.getAbsolutePath());
         }
      } else {
         throw new IOException("Can't create a dir from null.");
      }
   }
   public static void createFile(File file) throws IOException {
      if(file == null){
         throw new IOException("Can't create a file from null.");
      }
      if(file.isDirectory()){
         throw new IOException("Can't create a file that is a directory: "+file.getAbsolutePath());
      }
      if(!file.isFile()) {
         file.getParentFile().mkdirs();
         file.createNewFile();
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

   /**
    * Forces paths to fit the following format:
    * <ul>
    *    <li>Leading File.separators are removed</li>
    *    <li>Proceeding File.separators are removed</li>
    * </ul>
    * @param path
    * @return
    */
   public static String getForcedRelativePath(String path){
      if(path == null){
         throw new NullPointerException(
            "Can't force a relative path from null"
         );
      }
      String pathToReturn = path;
      if(path.startsWith(File.separator)){
         pathToReturn = path.substring(1);
      }
      pathToReturn = pathToReturn.replaceFirst(File.separator+"+$", "");
      return pathToReturn;
   }
   public static String getString(File file) throws IOException {
      return new String(getChars(file));
   }
   public static byte[] getBytes(File file) throws IOException {
      return Files.readAllBytes(file.toPath());
   }
   public static char[] getChars(File file)
      throws IOException
   {
      String str;

      Assertions.fileExistsOrFail(file);

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
      createFile(file);
      try (FileWriter output = new FileWriter( file )) {
         output.write( contents, 0, contents.length() );
      }
   }
}
