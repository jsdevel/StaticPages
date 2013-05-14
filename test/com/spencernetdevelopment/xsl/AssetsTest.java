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
package com.spencernetdevelopment.xsl;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Joseph Spencer
 */
public class AssetsTest {

   @Test
   public void all_white_space_in_text_should_be_normalized_to_a_single_space() {
      String test = "f\n\n\n   \n\n\na";
      assertEquals("f a", Assets.normalizeSpace(test));
   }

   @Test
   public void text_of_all_white_space_should_be_ignored() {
      String test = "  \n\n\t    ";
      assertEquals("", Assets.normalizeSpace(test));
   }

}