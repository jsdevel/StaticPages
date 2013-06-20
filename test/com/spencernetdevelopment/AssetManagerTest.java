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

import java.io.IOException;
import java.util.Properties;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.*;

/**
 *
 * @author Joseph Spencer
 */
public class AssetManagerTest {
   AssetManager manager;
   StaticPagesConfiguration config;
   FileUtils fileUtils;
   FilePath assetDir;
   FilePath buildDir;
   FilePath spoofDir;

   @Before
   public void before() throws IOException {
      config = mock(StaticPagesConfiguration.class);
      fileUtils=mock(FileUtils.class);
      assetDir=mock(FilePath.class);
      buildDir=mock(FilePath.class);
      spoofDir=mock(FilePath.class);
      manager = new AssetManager(assetDir, buildDir, fileUtils, config, null);
   }

   @Test
   public void directories_should_be_transferrable() throws IOException {
      when(assetDir.resolve(anyString())).thenReturn(assetDir);
      when(buildDir.resolve(anyString())).thenReturn(buildDir);
      when(assetDir.toString()).thenReturn("src/foo");
      when(buildDir.toString()).thenReturn("build/foo");
      when(fileUtils.isDirectory(anyString())).thenReturn(true);
      manager.transferAsset("foo");
      verify(fileUtils, times(1)).copyDirContentsToDir("src/foo", "build/foo");
   }

}