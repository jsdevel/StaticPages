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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.InOrder;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Joseph Spencer
 */
public class BreadcrumbsTest {
   Breadcrumbs breadcrumbs;
   BreadcrumbFactory factory;

   @Before
   public void before(){
      factory=mock(BreadcrumbFactory.class);
      when(factory.makeBreadcrumb(anyString(), anyString(), anyString()))
         .thenAnswer(new Answer<Breadcrumb>(){
            @Override
            public Breadcrumb answer(InvocationOnMock invocation) throws Throwable {
               Object[] args = invocation.getArguments();
               String displayName = (String)args[0];
               String link = (String)args[1];
               String path = (String)args[2];
               Breadcrumb mock = mock(Breadcrumb.class);
               when(mock.getDisplayName()).thenReturn(displayName);
               when(mock.getDomainRelativeLink()).thenReturn(link);
               when(mock.getXmlPagesRelativePath()).thenReturn(path);
               return mock;
            }
         });
   }

   @Test(expected = NullPointerException.class)
   public void constructor_should_throw_NP_when_displayName_is_null(){
      new Breadcrumbs(null, "", factory);
   }
   @Test(expected = NullPointerException.class)
   public void constructor_should_throw_NP_when_domainRelativeHtmlPagePath_is_null(){
      new Breadcrumbs("", null, factory);
   }
   @Test(expected = IllegalArgumentException.class)
   public void constructor_should_not_accept_a_browser_prefix_that_ends_with_slash(){
      new Breadcrumbs("/", "", factory);
   }
   @Test
   public void empty_path_should_return_empty_list() {
      breadcrumbs = new Breadcrumbs("", "", factory);
      assertTrue(breadcrumbs.isEmpty());
   }
   @Test
   public void null_path_should_return_empty_list() {
      breadcrumbs = new Breadcrumbs("", "", factory);
      assertTrue(breadcrumbs.isEmpty());
   }
   @Test
   public void root_should_return_empty_list() {
      breadcrumbs = new Breadcrumbs("", "/", factory);
      assertTrue(breadcrumbs.isEmpty());
   }
   @Test
   public void root_default_file_should_return_empty_list() {
      breadcrumbs = new Breadcrumbs("", "/index.html", factory);
      assertTrue(breadcrumbs.isEmpty());
   }
   @Test
   public void directories_should_create_breadcrumbs(){
      breadcrumbs = new Breadcrumbs("", "/foo/index.html", factory);
      assertEquals(1, breadcrumbs.size());
   }
   @Test
   public void files_should_create_breadcrumbs(){
      breadcrumbs = new Breadcrumbs("", "/foo.html", factory);
      assertEquals(1, breadcrumbs.size());
   }
   @Test
   public void prefix_should_be_used(){
      breadcrumbs = new Breadcrumbs("/asdf", "/foo/doo/goo.html", factory);
      breadcrumbs.size();
      InOrder crumbs = inOrder(factory);
      crumbs.verify(factory)
         .makeBreadcrumb("goo", "/asdf/foo/doo/goo.html", "foo/doo/goo.xml");
      crumbs.verify(factory)
         .makeBreadcrumb("doo", "/asdf/foo/doo/", "foo/doo/index.xml");
      crumbs.verify(factory)
         .makeBreadcrumb("foo", "/asdf/foo/", "foo/index.xml");
      assertEquals(3, breadcrumbs.size());
      crumbs.verifyNoMoreInteractions();

   }
   @Test
   public void long_paths_should_create_breadcrumbs(){
      breadcrumbs = new Breadcrumbs("", "/foo/woo/doo.html", factory);
      breadcrumbs.size();
      InOrder crumbs = inOrder(factory);
      crumbs.verify(factory)
         .makeBreadcrumb("doo", "/foo/woo/doo.html", "foo/woo/doo.xml");
      crumbs.verify(factory)
         .makeBreadcrumb("woo", "/foo/woo/", "foo/woo/index.xml");
      crumbs.verify(factory)
         .makeBreadcrumb("foo", "/foo/", "foo/index.xml");
      assertEquals(3, breadcrumbs.size());
      crumbs.verifyNoMoreInteractions();
   }
   @Test(expected = IndexOutOfBoundsException.class)
   public void take_should_throw_IndexOutOfBoundsException_when_no_crumbs_exist(){
      breadcrumbs = new Breadcrumbs("", "/index.html", factory);
      breadcrumbs.take();
   }
   @Test
   public void take_should_return_the_first_crumb(){
      breadcrumbs = new Breadcrumbs("", "/foo/boo/too.html", factory);
      Breadcrumb crumb;
      crumb = breadcrumbs.take();
      assertEquals("foo", crumb.getDisplayName());
      crumb = breadcrumbs.take();
      assertEquals("boo", crumb.getDisplayName());
      crumb = breadcrumbs.take();
      assertEquals("too", crumb.getDisplayName());
   }

   @Test
   public void regex(){
      breadcrumbs = new Breadcrumbs("", "", factory);
      Pattern crumb = breadcrumbs.CRUMB_REGEX;
      String[] emptyDirnames = {
         "/index.html",
         "/asdf.html"
      };
      for(String dirname:emptyDirnames){
         Matcher matcher = crumb.matcher(dirname);
         matcher.find();
         assertEquals(null, matcher.group(1));
      }
      String[] matchedDirnames = {
         "/foo/index.html",
         "/foo/asdf.html"
      };
      for(String dirname:matchedDirnames){
         Matcher matcher = crumb.matcher(dirname);
         matcher.find();
         assertEquals("foo", matcher.group(1));
      }
      String[] emptyFilenames = {
         "foo/.html",
         ".html"
      };
      for(String filename:emptyFilenames){
         Matcher matcher = crumb.matcher(filename);
         assertFalse(matcher.find());
      }
      String[] matchedFilenames = {
         "/foo/index.html",
         "/index.html",
         "/index.html"
      };
      for(String filename:matchedFilenames){
         Matcher matcher = crumb.matcher(filename);
         matcher.find();
         assertEquals("index", matcher.group(2));
      }
   }
}