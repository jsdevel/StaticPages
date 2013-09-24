/*
 * Copyright 2013 Joseph Spencer.
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
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joseph Spencer
 */
public class FileIterator {
   private final FilePath baseDirPath;
   private final String baseDir;
   private final FileUtils fileUtils;
   private final String extension;
   private final boolean recursive;
   private final List<Path> filePaths;
   private volatile boolean isAnalyzed=false;

   public FileIterator(
      FilePath baseDirPath,
      FileUtils fileUtils,
      String extension,
      boolean recursive
   ) throws IOException
   {
      this.baseDirPath=baseDirPath;
      this.baseDir=baseDirPath.toUnix();
      this.fileUtils=fileUtils;
      this.extension=extension;
      this.recursive=recursive;
      filePaths=fileUtils.filePaths(
         baseDirPath,
         extension,
         recursive
      );
   }

   public boolean hasNext(){
      return !filePaths.isEmpty();
   }

   public void takeNextIntoVariableContext(
      VariableManager manager
   ){
      Path path = filePaths.remove(0);
      File file = path.toFile();
      String relativePath = file.
         getAbsolutePath().
         substring(baseDir.length());

      manager.setVariable(
         "fileName",
         file.getName()
      );
      manager.setVariable(
         "fileNameWithoutExtension",
         file.getName().replaceFirst("[.][^.]++$", "")
      );
      manager.setVariable(
         "relativeFilePath",
         relativePath
      );
   }

}
