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
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Joseph Spencer
 */
public class RewriteManagerTest {
   private static FilePath buildFilePath;
   private static FilePath inputFile;
   private RewriteManager manager;

   @BeforeClass
   public static void setUpClass() throws IOException {
      buildFilePath = FilePath.getFilePath("/tmp/StaticPageTests/output");
      inputFile = buildFilePath.resolve("index.html");

      FileUtils.clearDirectory(buildFilePath.toFile());
      FileUtils.createDir(buildFilePath.toFile());
      FileUtils.createFile(inputFile.toFile());
   }

   @AfterClass
   public static void tearDownClass() {
   }

   @Before
   public void setUp() throws IOException {
      manager = new RewriteManager(buildFilePath);
   }

   @After
   public void tearDown() {
   }

   @Test
   public void constructor_requires_existing_base_file_path() {
      try {
         new RewriteManager(FilePath.getFilePath("/asdfasdfasdf"));
         fail("no IOExceptions were thrown for non-existent directories");
      } catch (IOException ex) {}
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
      String[] rewritesToApply = {
         "bla-test/boo",
         "bla-test/doo",
         "bla-test/coo",
         "bltest/boo",
         "bltest/doaaaao",
         "bltesasdt/coo",
      };
      String src = "rewriteMe.html";
      FileUtils.createFile(buildFilePath.resolve(src).toFile());
      String targetPostfix = "index.html";
      for(String target:rewritesToApply){
         manager.queueRewrite(src, target);
      }
      manager.applyRewrites();
      for(String target:rewritesToApply){
         File toFile = buildFilePath.resolve(target+"/"+targetPostfix).toFile();
         assertTrue(toFile.isFile());
      }
   }
}