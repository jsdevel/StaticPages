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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author Joseph Spencer
 */
public class FileIteratorTest {
   FilePath mockBaseDirPath;
   FileUtils mockFileUtils;
   FileIterator iterator;
   List<Path> paths;
   VariableManager mockVariableManager;

   @Before
   public void before() throws IOException {
      mockBaseDirPath=mock(FilePath.class);
      when(mockBaseDirPath.toUnix()).thenReturn("/foo/bar");
      mockFileUtils=mock(FileUtils.class);
      paths = new ArrayList();
      when(mockFileUtils.filePaths(mockBaseDirPath, null, true)).thenReturn(paths);
      iterator=new FileIterator(
         mockBaseDirPath,
         mockFileUtils,
         null,
         true
      );
   }

   @Test
   public void the_constructor_should_not_interact_with_collaborators() {
      verify(mockBaseDirPath).toUnix();
      verifyNoMoreInteractions(mockBaseDirPath);
   }

   @Test
   public void hasNext_should_return_false_when_no_files_exist(){
      assertFalse(iterator.hasNext());
   }

   @Test
   public void hasNext_should_return_true_when_files_exist(){
      paths.add(mock(Path.class));
      assertTrue(iterator.hasNext());
   }

   @Test
   public void iterator_should_be_able_to_iterate_files_and_communicate_with_a_variable_context(){
      Path mockPath = mock(Path.class);
      File mockFile = mock(File.class);
      when(mockPath.toFile()).thenReturn(mockFile);
      when(mockFile.getAbsolutePath()).thenReturn("/foo/bar/test.js");
      when(mockFile.getName()).thenReturn("test.js");
      paths.add(mockPath);
      assertTrue(iterator.hasNext());
      mockVariableManager=mock(VariableManager.class);
      iterator.takeNextIntoVariableContext(mockVariableManager);
      assertFalse(iterator.hasNext());
      verify(mockVariableManager).setVariable("fileName", "test.js");
      verify(mockVariableManager).setVariable("fileNameWithoutExtension", "test");
      verify(mockVariableManager).setVariable("relativeFilePath", "/test.js");
   }

}