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

import java.io.File;
import java.io.IOException;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Joseph Spencer
 */
public class TestUtils {

   /**
    *
    * @param base
    * @param file This should be a mock object.
    * @return
    * @throws IOException
    */
   public static FilePath getResolvingFilePath(final String base, final File file) throws IOException {
      FilePath mock = mock(FilePath.class);
      when(file.getAbsolutePath()).thenReturn(base);
      when(mock.toString()).thenReturn(base);
      when(mock.toUnix()).thenReturn(base);
      when(mock.toFile()).thenReturn(file);
      when(mock.resolve(anyString())).thenAnswer(new Answer<FilePath>(){
         @Override
         public FilePath answer(InvocationOnMock invocation) throws Throwable {
            String path = (String)invocation.getArguments()[0];
            return getResolvingFilePath(base+"/"+path, file);
         }
      });
      return mock;
   }
}
