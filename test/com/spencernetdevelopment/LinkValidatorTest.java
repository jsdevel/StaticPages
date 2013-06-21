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

import java.util.Map;
import org.junit.Test;
import org.junit.Before;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static com.spencernetdevelopment.TestUtils.*;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Joseph Spencer
 */
public class LinkValidatorTest {
   LinkValidator validator;
   Map<String, ExternalLinkValidator<Object>> externals;
   StaticPagesConfiguration config;
   DefaultNamespaceContext nsContext;
   File file;

   @Before
   public void before() throws IOException {
      config = mock(StaticPagesConfiguration.class);
      file=mock(File.class);
      FilePath filePath = getResolvingFilePath("base", file);
      when(config.getPagesDirPath()).thenReturn(filePath);
      validator=new LinkValidator(externals, config, nsContext);
   }

   @Test
   public void validation_should_pass_when_file_exists() throws IOException {
      when(file.isFile()).thenReturn(true);
      validator.validatePageReference("foo");
   }
   @Test(expected = IOException.class)
   public void validation_should_fail_when_file_does_not_exist() throws IOException {
      when(file.isFile()).thenReturn(false);
      validator.validatePageReference("foo");
   }
   @Test
   public void paths_not_ending_in_dot_xml_should_be_normalized() throws IOException {
      when(file.isFile()).thenReturn(true);
      validator.validatePageReference("foo");
      assertEquals("base/foo.xml", file.getAbsolutePath());
   }

   @Test
   public void paths_ending_in_dot_xml_should_not_be_normalized() throws IOException {
      when(file.isFile()).thenReturn(true);
      validator.validatePageReference("foo.xml");
      assertEquals("base/foo.xml", file.getAbsolutePath());
   }

}