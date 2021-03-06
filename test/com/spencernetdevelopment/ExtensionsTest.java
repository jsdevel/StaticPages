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

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Joseph Spencer
 */
public class ExtensionsTest {
   BreadcrumbFactory breadcrumbFactory;
   FileIteratorFactory fileIteratorFactory;
   Extensions extensions;

   @Before
   public void before(){
      breadcrumbFactory=mock(BreadcrumbFactory.class);
      fileIteratorFactory=mock(FileIteratorFactory.class);
      extensions = new Extensions(breadcrumbFactory, fileIteratorFactory);
   }

   @Test
   public void breadcrumbs_should_call_makeBreadcrumbs_on_factory(){
      extensions.makeBreadcrumbs(null, null);
      verify(breadcrumbFactory, times(1)).makeBreadcrumbs(null, null);
   }

}