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
import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Joseph Spencer
 */
public class GroupedAssetTransactionManagerTest {
   private GroupedAssetTransactionManager manager;
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
      when(buildDirPath.resolve(anyString())).thenAnswer(new Answer<FilePath>(){
         @Override
         public FilePath answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            String arg = (String)args[0];
            FilePath mock = mock(FilePath.class);
            when(mock.toString()).thenReturn(arg);
            return mock;
         }
      });
      resolver = mock(AssetResolver.class);
      when(resolver.getCSSPath(anyString())).thenAnswer(new Answer<String>(){
         @Override
         public String answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            String arg = (String)args[0];
            return "css/"+arg+".css";
         }
      });
      when(resolver.getJSPath(anyString())).thenAnswer(new Answer<String>(){
         @Override
         public String answer(InvocationOnMock invocation) throws Throwable {
            Object[] args = invocation.getArguments();
            String arg = (String)args[0];
            return "js/"+arg+".js";
         }
      });
      transaction = mock(GroupedAssetTransaction.class);
      config = mock(StaticPagesConfiguration.class);
      when(config.getBuildDirPath()).thenReturn(buildDirPath);
      futils=mock(FileUtils.class);
      manager = new GroupedAssetTransactionManager(
         assetManager,
         resolver,
         config,
         futils
      );
   }

   @Test(expected = IllegalArgumentException.class)
   public void constructor_does_not_allow_null_asset_manager(){
      new GroupedAssetTransactionManager(null, resolver, config, futils);
   }
   @Test(expected = IllegalArgumentException.class)
   public void constructor_does_not_allow_null_asset_resolver(){
      new GroupedAssetTransactionManager(assetManager, null, config, futils);
   }
   @Test(expected = IllegalArgumentException.class)
   public void constructor_does_not_allow_null_config(){
      new GroupedAssetTransactionManager(assetManager, resolver, null, futils);
   }
   @Test(expected = IllegalArgumentException.class)
   public void constructor_does_not_allow_null_file_utils(){
      new GroupedAssetTransactionManager(assetManager, resolver, config, null);
   }
   @Test
   public void manager_can_start_transactions(){
      transaction = manager.startTransaction("js", "true");
      assertTrue(
         transaction != null &&
         transaction instanceof GroupedAssetTransaction
      );
   }
   @Test
   public void transactions_should_not_be_handled_when_they_are_not_closed() throws IOException, URISyntaxException{
      transaction = manager.startTransaction("js", "false");
      manager.processTransactions();
      verify(futils, times(0)).putString(anyString(), anyString());
   }
   @Test
   public void closed_transactions_should_be_output() throws IOException, URISyntaxException {
      when(assetManager.getJS("foo", false)).thenReturn("asdf");
      when(assetManager.getCSS("foo", false)).thenReturn("zzzz");
      transaction = manager.startTransaction("js", "false");
      transaction.addURL("foo");
      String id1 = transaction.getIdentifier();
      transaction = manager.startTransaction("css", "false");
      transaction.addURL("foo");
      String id2 = transaction.getIdentifier();
      manager.processTransactions();

      verify(futils, times(1)).putString("js/"+id1+".js", "asdf");
      verify(futils, times(1)).putString("css/"+id2+".css", "zzzz");
   }
   @Test
   public void similar_transactions_should_not_be_output() throws IOException, URISyntaxException {
      when(assetManager.getJS("foo", false)).thenReturn("asdf");
      transaction = manager.startTransaction("js", "false");
      transaction.addURL("foo");
      String id1 = transaction.getIdentifier();
      transaction = manager.startTransaction("js", "false");
      transaction.addURL("foo");
      manager.processTransactions();
      verify(futils, times(1)).putString("js/"+id1+".js", "asdf");
   }

}