/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 *
 * @author Joseph Spencer
 */
public class DirectoryCleaner implements FileVisitor<Path> {
   private final Path basePath;
   private final boolean preserveBasePath;

   public DirectoryCleaner(Path basePath) {
      this(basePath, true);
   }
   public DirectoryCleaner(Path basePath, boolean preserveBasePath) {
      this.basePath = basePath;
      this.preserveBasePath = preserveBasePath;
   }

   @Override
   public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
      return FileVisitResult.CONTINUE;
   }

   @Override
   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      file.toFile().delete();
      return FileVisitResult.CONTINUE;
   }

   @Override
   public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
      return FileVisitResult.CONTINUE;
   }

   @Override
   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
      if(!preserveBasePath || !dir.equals(basePath)){
         dir.toFile().delete();
      }
      return FileVisitResult.CONTINUE;
   }
}
