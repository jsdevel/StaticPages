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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
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
   private final FileUtils fileUtils;

   public DirectoryDuplicator(
      File source,
      File destination,
      FileUtils fileUtils
   ) throws IOException{
      if(source == null || destination == null){
         throw new IOException("Both source and destination must not be null.");
      }
      if(!source.isDirectory() || !source.isDirectory()){
         throw new IOException("Both source and destination must be directories.");
      }
      if(!source.exists() || !source.exists()){
         throw new IOException("Both source and destination must exist.");
      }
      if(fileUtils == null){
         throw new NullPointerException("fileUtils was null.");
      }
      this.destination=destination;
      this.destinationPath=destination.toPath();
      this.source=source;
      this.sourcePath=source.toPath();
      sourcePathString=sourcePath.toString();
      sourcePathStringLength=sourcePathString.length();
      this.fileUtils=fileUtils;
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
      fileUtils.copyFile(file.toFile(), targetFile);
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
