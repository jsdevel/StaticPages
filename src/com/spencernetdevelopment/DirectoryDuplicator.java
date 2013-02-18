/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author Joseph Spencer
 */
public class DirectoryDuplicator extends SimpleFileVisitor<Path> {
   private final File destination;
   private final Path destinationPath;
   private final File source;
   private final Path sourcePath;
   private final String sourcePathString;
   private final int sourcePathStringLength;

   public DirectoryDuplicator(File source, File destination) throws Exception{
      if(source == null || destination == null){
         throw new Exception("Both source and destination must not be null.");
      }
      if(!source.isDirectory() || !source.isDirectory()){
         throw new Exception("Both source and destination must be directories.");
      }
      if(!source.exists() || !source.exists()){
         throw new Exception("Both source and destination must exist.");
      }
      this.destination=destination;
      this.destinationPath=destination.toPath();
      this.source=source;
      this.sourcePath=source.toPath();
      sourcePathString=sourcePath.toString();
      sourcePathStringLength=sourcePathString.length();
   }

   @Override
   public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
      String dirPathString = getRelativePath(dir);
      File targetDir = destinationPath.resolve(dirPathString).toFile();
      if(!targetDir.exists()){
         targetDir.mkdirs();
      } else if(!targetDir.isDirectory()){
         throw new IOException(
            "Target directory isn't a directory:\n   "
            +targetDir.toString()
            +"\nRemoving this will probably solve the issue.");
      }
      return FileVisitResult.CONTINUE;
   }

   @Override
   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      String filePathString = getRelativePath(file);
      File targetFile = destinationPath.resolve(filePathString).toFile();
      char[] srcFileChars = FileUtils.getChars(file.toFile());
      FileUtils.putString(targetFile, new String(srcFileChars));
      return FileVisitResult.CONTINUE;
   }
   private String removeStartingSeperator(String input){
      if(input != null && input.startsWith(File.separator)){
         return input.substring(1);
      }
      return input;
   }
   private String getRelativePath(Path resource){
      String initialPath = resource.toString().substring(sourcePathStringLength);
      String newPath = removeStartingSeperator(initialPath);
      return newPath;
   }
}
