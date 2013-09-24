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
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Joseph Spencer
 */
public class FileIteratorFactoryTest {
   FileIteratorFactory factory;
   FilePath mockAssetPath;
   FileUtils mockFileUtils;

   @Before
   public void before() throws IOException {
      mockAssetPath = mock(FilePath.class);
      mockFileUtils = mock(FileUtils.class);
      when(mockAssetPath.resolve(anyString())).thenReturn(mockAssetPath);
      factory = new FileIteratorFactory(mockAssetPath, mockFileUtils);
   }

   @Test
   public void make_asset_iterator_sets_base_directory_appropriately() throws IOException {
      factory.makeAssetIterator("foo", ".js", true);
      verify(mockAssetPath).resolve("foo");
   }
}