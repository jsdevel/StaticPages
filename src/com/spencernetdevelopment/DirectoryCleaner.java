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
