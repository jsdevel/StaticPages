/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Joseph Spencer
 */
public class GroupedAssetTransactionManagerTest {
   private GroupedAssetTransactionManager manager;
   private AssetManager assetManager;
   private GroupedAssetTransaction transaction;

   public GroupedAssetTransactionManagerTest() {
   }

   @BeforeClass
   public static void setUpClass() {
   }

   @AfterClass
   public static void tearDownClass() {
   }

   @Before
   public void setUp(){
      assetManager = mock(AssetManager.class);
      transaction = mock(GroupedAssetTransaction.class);
      manager = new GroupedAssetTransactionManager(assetManager);
   }

   @After
   public void tearDown(){}

   @Test(expected = IllegalArgumentException.class)
   public void constructor_does_not_allow_null_asset_manager(){
      new GroupedAssetTransactionManager(null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void adding_transactions_fails_when_transaction_not_closed()
      throws IOException
   {
      when(transaction.isClosed()).thenReturn(false);
      manager.process(transaction);
   }
   @Test(expected = IllegalArgumentException.class)
   public void processing_js_transaction_when_transaction_already_processed()
      throws IOException
   {
      playTransaction(transaction, "js");
      verify(assetManager, times(1)).getJS("js/boo.js", true);
      verify(assetManager, times(1)).getJS("js/coo.js", true);

      //now throw the exception
      manager.process(transaction);
   }
   @Test(expected = IllegalArgumentException.class)
   public void processing_css_transaction_when_transaction_already_processed()
      throws IOException
   {
      playTransaction(transaction, "css");
      verify(assetManager, times(1)).getCSS("css/boo.css", true);
      verify(assetManager, times(1)).getCSS("css/coo.css", true);

      //now throw the exception
      manager.process(transaction);
   }

   private void playTransaction(
      GroupedAssetTransaction t,
      String type
   ) throws IOException{
      when(t.isClosed()).thenReturn(true);
      when(t.isCompressed()).thenReturn(true);
      when(t.getIdentifier()).thenReturn("foo");
      when(t.getType()).thenReturn(type);
      when(t.toArray()).thenReturn(new String[]{
         "boo",
         "coo",
      });

      manager.process(t);
      verify(t, times(1)).toArray();
   }
}