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
import java.net.URISyntaxException;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 *
 * @author Joseph Spencer
 */
public class AssetResolverTest {
   AssetResolver resolver;
   AssetResolver resolverWithFingerprint;
   StaticPagesConfiguration config;
   FilePath pagesDirPath;
   FilePath viewDirsPath;
   public AssetResolverTest() throws IOException {
      config = mock(StaticPagesConfiguration.class);
      pagesDirPath=FilePath.getFilePath("/pages");
      viewDirsPath=FilePath.getFilePath("/test");
      when(config.getAssetFingerprint()).thenReturn("");
      when(config.getPagesDirPath()).thenReturn(pagesDirPath);
      when(config.getViewsDirPath()).thenReturn(viewDirsPath);
      resolver = new AssetResolver(config);
      when(config.getAssetFingerprint()).thenReturn(".555555");
      resolverWithFingerprint = new AssetResolver(config);
   }
   @Test(expected = NullPointerException.class)
   public void asset_path_should_not_allow_null() throws URISyntaxException {
      resolver.getAssetPath(null);
   }
   @Test(expected = IllegalArgumentException.class)
   public void asset_path_should_require_file_extension() throws URISyntaxException {
      resolver.getAssetPath("asdf");
   }
   @Test(expected = IllegalArgumentException.class)
   public void asset_path_should_require_file_name() throws URISyntaxException {
      resolver.getAssetPath(".asdf");
   }
   @Test
   public void asset_path_should_be_retrieveable() throws URISyntaxException {
      String path = resolver.getAssetPath("asdf/asdf/asdf.asdf");
      assertEquals("asdf/asdf/asdf.asdf", path);
   }
   @Test
   public void asset_path_should_allow_fingerprint() throws URISyntaxException {
      String path = resolverWithFingerprint.getAssetPath("asdf/asdf/asdf.asdf");
      assertEquals("asdf/asdf/asdf.555555.asdf", path);
   }
   @Test(expected = IllegalArgumentException.class)
   public void asset_path_should_not_allow_trailing_forward_slashes() throws URISyntaxException {
      resolver.getAssetPath("asdf/");
   }
   @Test
   public void clean_css_path_resolves_to_the_right_place() throws URISyntaxException {
      String path = resolver.getCleanCSSPath("boo");
      assertEquals("css/boo.css", path);
   }
   @Test(expected = IllegalArgumentException.class)
   public void clean_css_path_should_not_allow_trailing_forward_slashes() throws URISyntaxException {
      resolver.getCleanCSSPath("asdf/");
   }
   @Test
   public void clean_image_path_resolves_to_the_right_place() throws URISyntaxException {
      String path = resolver.getCleanImagePath("boo");
      assertEquals("images/boo", path);
   }
   @Test(expected = IllegalArgumentException.class)
   public void clean_image_path_should_not_allow_trailing_forward_slashes() throws URISyntaxException {
      resolver.getCleanImagePath("asdf/");
   }
   @Test
   public void clean_js_path_resolves_to_the_right_place() throws URISyntaxException {
      String path = resolver.getCleanJSPath("boo");
      assertEquals("js/boo.js", path);
   }
   @Test(expected = IllegalArgumentException.class)
   public void clean_js_path_should_not_allow_trailing_forward_slashes() throws URISyntaxException {
      resolver.getCleanJSPath("asdf/");
   }
   @Test
   public void get_css_should_return_fingerprint_when_fingerprint_configured() throws URISyntaxException {
      String path = resolverWithFingerprint.getCSSPath("boo");
      assertEquals("css/boo.555555.css", path);
   }
   @Test
   public void get_css_should_not_return_fingerprint_when_fingerprint_configured() throws URISyntaxException {
      String path = resolver.getCSSPath("boo");
      assertEquals("css/boo.css", path);
   }
   @Test(expected = IllegalArgumentException.class)
   public void get_css_should_not_allow_trailing_forward_slashes() throws URISyntaxException {
      resolver.getCSSPath("asdf/");
   }
   @Test
   public void get_image_should_return_fingerprint_when_fingerprint_configured() throws URISyntaxException {
      String path = resolverWithFingerprint.getImagePath("boo.png");
      assertEquals("images/boo.555555.png", path);
   }
   @Test
   public void get_image_should_not_return_fingerprint_when_fingerprint_configured() throws URISyntaxException {
      String path = resolver.getImagePath("boo.png");
      assertEquals("images/boo.png", path);
   }
   @Test(expected = IllegalArgumentException.class)
   public void get_image_should_not_allow_trailing_forward_slashes() throws URISyntaxException {
      resolver.getImagePath("asdf/");
   }
   @Test
   public void get_js_should_return_fingerprint_when_fingerprint_configured() throws URISyntaxException {
      String path = resolverWithFingerprint.getJSPath("boo");
      assertEquals("js/boo.555555.js", path);
   }
   @Test
   public void get_js_should_not_return_fingerprint_when_fingerprint_configured() throws URISyntaxException {
      String path = resolver.getJSPath("boo");
      assertEquals("js/boo.js", path);
   }
   @Test(expected = IllegalArgumentException.class)
   public void get_js_should_not_allow_trailing_forward_slashes() throws URISyntaxException {
      resolver.getJSPath("asdf/");
   }
   @Test
   public void rewrite_paths_should_be_normalized() throws URISyntaxException {
      String path;
      path = resolver.getNormalizedRewritePath("asdf/asdf");
      assertEquals("/asdf/asdf/", path);
      path = resolver.getNormalizedRewritePath("//asdf/asdf/");
      assertEquals("/asdf/asdf/", path);
      path = resolver.getNormalizedRewritePath("asdf/asdf.html");
      assertEquals("/asdf/asdf.html", path);
   }
   @Test
   public void page_dir_path_should_be_normalized() throws IOException, URISyntaxException {
      String path;
      path = resolver.getPagePath("asdf/asdf");
      assertEquals("/pages/asdf/asdf.xml", path);
   }
   @Test
   public void view_dir_path_should_be_normalized() throws IOException, URISyntaxException {
      String path;
      path = resolver.getViewPath("../../asdfasdf/asdf/asdf");
      assertEquals("/test/asdfasdf/asdf/asdf.xml", path);
      try {
         resolver.getViewPath("../../asdfasdf/asdf/asdf/");
         fail("trailing slashes should not be allowed.");
      }catch(IllegalArgumentException ex){}
   }
   @Test
   public void forced_relative_file_paths_should_be_normalized() throws URISyntaxException {
      String path;
      path = resolver.forceRelativeFilePath("../../asdfasdf/asdf/asdf");
      assertEquals("asdfasdf/asdf/asdf", path);
   }
   @Test(expected = IllegalArgumentException.class)
   public void file_paths_should_not_end_in_forward_slashes() throws URISyntaxException {
      resolver.forceRelativeFilePath("asdf/");
   }
   @Test
   public void forced_relative_paths_should_be_normalized() throws URISyntaxException {
      String path;
      path = resolver.forceRelativePath("../../asdfasdf/asdf/asdf");
      assertEquals("asdfasdf/asdf/asdf", path);
      path = resolver.forceRelativePath("/asdfasdf/asdf/asdf");
      assertEquals("asdfasdf/asdf/asdf", path);
      path = resolver.forceRelativePath("./asdfasdf/asdf/asdf");
      assertEquals("asdfasdf/asdf/asdf", path);
   }
}