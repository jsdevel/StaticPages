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
import java.net.URISyntaxException;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalAnswers.*;

/**
 *
 * @author Joseph Spencer
 */
public class GroupedAssetTaskTest {
   private GroupedAssetTask<Object> task;
   private AssetManager assetManager;
   private AssetResolver resolver;
   private GroupedAssetTransaction transaction;
   private StaticPagesConfiguration config;
   private FilePath buildDirPath;
   private FileUtils futils;

   @Before
   public void setUp() throws IOException, URISyntaxException {
      assetManager = mock(AssetManager.class);
      buildDirPath=mock(FilePath.class);
      when(buildDirPath.toString()).thenReturn("somefoo");
      transaction = mock(GroupedAssetTransaction.class);
      futils=mock(FileUtils.class);
      task = new GroupedAssetTask<>(
         transaction,
         assetManager,
         futils,
         buildDirPath
      );
   }

   @Test
   public void only_js_and_css_types_should_be_considered() throws Exception {
      when(transaction.getType()).thenReturn("foo");
      task.call();
      verifyZeroInteractions(futils);
      verifyZeroInteractions(assetManager);
   }

   @Test
   public void collaborators_should_be_called_with_the_right_values_when_js_is_the_transaction_type() throws Exception{
      when(transaction.getType()).thenReturn("js");
      when(transaction.toArray()).thenReturn(new String[]{
         "foo",
         "boo"
      });
      when(transaction.isCompressed()).thenReturn(true);
      when(assetManager.getJS(anyString(), eq(true))).then(returnsFirstArg());
      task.call();
      verify(futils, times(1)).putString("somefoo", "fooboo");
   }
   @Test
   public void collaborators_should_be_called_with_the_right_values_when_css_is_the_transaction_type() throws Exception{
      when(transaction.getType()).thenReturn("css");
      when(transaction.toArray()).thenReturn(new String[]{
         "foo",
         "boo"
      });
      when(transaction.isCompressed()).thenReturn(false);
      when(assetManager.getCSS(anyString(), eq(false))).then(returnsFirstArg());
      task.call();
      verify(futils, times(1)).putString("somefoo", "fooboo");
   }

}