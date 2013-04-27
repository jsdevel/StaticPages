/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Joseph Spencer
 */
public class GroupedAssetTransactionTest {
   private GroupedAssetTransaction transaction;

   public GroupedAssetTransactionTest() {
   }

   @BeforeClass
   public static void setUpClass() {
   }

   @AfterClass
   public static void tearDownClass() {
   }

   @Before
   public void setUp() {
      transaction = new GroupedAssetTransaction("js");
   }

   @After
   public void tearDown() {
   }

   //Before group is closed
   @Test
   public void constructor_allows_certain_types(){
      new GroupedAssetTransaction("css");
      new GroupedAssetTransaction("js");
   }
   @Test(expected = IllegalArgumentException.class)
   public void constructor_only_allows_certain_types(){
      new GroupedAssetTransaction("foo");
   }
   @Test
   public void compressed_by_default(){
      GroupedAssetTransaction groupedAssetTransaction =
         new GroupedAssetTransaction("js");
      assertTrue(groupedAssetTransaction.isCompressed());
   }
   @Test
   public void can_disable_compression(){
      GroupedAssetTransaction groupedAssetTransaction =
         new GroupedAssetTransaction("js", false);
      assertFalse(groupedAssetTransaction.isCompressed());
   }
   @Test
   public void can_get_type(){
      assertEquals("js", transaction.getType());
   }
   @Test
   public void can_add_url_before_group_is_closed(){
      transaction.addURL("/script");
      transaction.addURL("/script2");
   }
   @Test
   public void can_add_similar_urls_before_group_is_closed(){
      transaction.addURL("/script");
      transaction.addURL("/script");
   }
   @Test(expected = NullPointerException.class)
   public void add_null_url_before_group_is_closed_throws_exception(){
      transaction.addURL(null);
   }
   @Test(expected = IllegalArgumentException.class)
   public void add_empty_url_before_group_is_closed_throws_exception(){
      transaction.addURL("    \n \t ");
   }
   @Test(expected = IllegalStateException.class)
   public void can_not_get_identifier_until_group_is_closed(){
      transaction.addURL("script");
      transaction.getIdentifier();
   }
   @Test(expected = IllegalStateException.class)
   public void can_not_get_array_until_group_is_closed(){
      transaction.addURL("script");
      transaction.toArray();
   }
   @Test(expected = IllegalStateException.class)
   public void group_must_have_urls_before_closing(){
      transaction.close();
   }

   //After group is closed
   @Test
   public void group_with_urls_can_close(){
      transaction.addURL("script");
      transaction.close();
   }
   @Test(expected = IllegalStateException.class)
   public void can_not_add_urls_after_closing(){
      transaction.addURL("script");
      transaction.close();
      transaction.addURL("script");
   }
   @Test
   public void can_get_array_when_closed(){
      transaction.addURL("script");
      transaction.addURL("script2");
      transaction.close();
      String[] array = transaction.toArray();
      assertEquals("script", array[0]);
      assertEquals("script2", array[1]);
   }
   /**
    * This probably never gets to toArray, but it's here to ensure that it will
    * never happen.
    */
   @Test(expected = IllegalStateException.class)
   public void must_add_urls_and_close_group_to_get_array(){
      transaction.close();
      transaction.toArray();
   }
   @Test
   public void can_get_identifier_when_group_is_closed(){
      transaction.addURL("script");
      transaction.addURL("script");
      transaction.close();
      String identifier = transaction.getIdentifier();
      assertEquals("Ml9zY3JpcHRfc2NyaXB0", identifier);
   }
   @Test
   public void identifiers_avoid_collisions(){
      String attemptToDuplicate = "Ml9zY3JpcHRfc2NyaXB0";
      String identifier;
      transaction.addURL("script");
      transaction.addURL("script");
      transaction.close();
      identifier = transaction.getIdentifier();
      assertEquals(attemptToDuplicate, identifier);

      transaction = new GroupedAssetTransaction("js");
      transaction.addURL("script_script");
      transaction.close();
      identifier = transaction.getIdentifier();
      assertFalse(attemptToDuplicate.equals(identifier));

      transaction = new GroupedAssetTransaction("js");
      transaction.addURL("scrip");
      transaction.addURL("tscript");
      transaction.close();
      identifier = transaction.getIdentifier();
      assertFalse(attemptToDuplicate.equals(identifier));
   }
   @Test
   public void can_determine_if_group_is_closed(){
      transaction.addURL("boo");
      assertFalse(transaction.isClosed());
      transaction.close();
      assertTrue(transaction.isClosed());
   }
}