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
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.AdditionalAnswers.*;

/**
 *
 * @author Joseph Spencer
 */
public class RewriteManagerTest {
   private static FilePath baseFilePath;
   private RewriteManager manager;
   private FileUtils fileUtils;
   private boolean srcExists=false;
   private String[] rewritesToApply = {
      "bla-test/boo",
      "bla-test/doo",
      "bla-test/coo",
      "bltest/boo",
      "bltest/doaaaao",
      "bltesasdt/coo",
   };

   @Before
   public void setUp() throws IOException {
      baseFilePath = mock(FilePath.class);
      when(baseFilePath.resolve(anyString())).thenAnswer(new Answer<FilePath>(){
         @Override
         public FilePath answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            String arg = (String)args[0];
            FilePath filePath = mock(FilePath.class);
            File file = mock(File.class);
            when(filePath.toFile()).thenReturn(file);
            when(file.isFile()).thenReturn(srcExists);
            when(filePath.toString()).thenReturn(arg);
            return filePath;
         }
      });
      fileUtils = mock(FileUtils.class);
      when(fileUtils.getForcedRelativePath(anyString())).then(returnsFirstArg());
      manager = new RewriteManager(baseFilePath, fileUtils);
   }

   @Test(expected = NullPointerException.class)
   public void constructor_should_not_accept_a_null_base_path(){
      new RewriteManager(null, fileUtils);
   }
   @Test(expected = NullPointerException.class)
   public void constructor_should_not_accept_a_null_file_utils(){
      new RewriteManager(baseFilePath, null);
   }

   @Test(expected = NullPointerException.class)
   public void rewriting_with_null_source_fails(){
      manager.queueRewrite(null, "");
   }
   @Test(expected = NullPointerException.class)
   public void rewriting_with_null_target_fails(){
      manager.queueRewrite("", null);
   }
   @Test
   public void rewriting_source_to_multiple_targets_is_ok(){
      manager.queueRewrite("index.html", "bla-test/boo");
      manager.queueRewrite("index.html", "bla-test/coo");
      manager.queueRewrite("index.html", "bla-test/doo");
   }
   @Test(expected = IllegalArgumentException.class)
   public void rewriting_multiple_sources_to_same_target_fails(){
      manager.queueRewrite("index.html", "bla-test/boo");
      manager.queueRewrite("hndex.html", "bla-test/boo");
   }
   @Test
   public void applying_rewrites_creates_files() throws IOException {
      srcExists=true;
      String src = "rewriteMe.html";
      String targetPostfix = "index.html";
      for(String target:rewritesToApply){
         manager.queueRewrite(src, target);
      }
      manager.applyRewrites();
      for(String target:rewritesToApply){
         verify(fileUtils, times(1)).copyFile(src, target+"/"+targetPostfix);
      }
   }
   @Test(expected = IOException.class)
   public void rewrites_fail_when_the_source_does_not_exist() throws IOException {
      srcExists=false;
      String src = "rewriteMe.html";
      for(String target:rewritesToApply){
         manager.queueRewrite(src, target);
      }
      manager.applyRewrites();
   }
}