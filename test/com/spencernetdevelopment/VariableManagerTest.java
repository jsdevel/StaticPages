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
import java.util.Properties;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Joseph Spencer
 */
public class VariableManagerTest {
   VariableManager manager;
   Properties vars;

   @Before
   public void before(){
      vars = new Properties();
      vars.setProperty("foo", "5");
      manager = new VariableManager(vars);
   }

   @Test
   public void variable_expansion_should_happen_when_the_manager_has_the_variable_loaded() throws IOException {
      String expanded = manager.expandVariables("${foo}${foo}");
      assertEquals("55", expanded);
   }

   @Test
   public void all_variable_references_should_be_removed_regardless_if_they_have_been_defined_or_not() throws IOException {
      String expanded = manager.expandVariables("${boo}${foo}");
      assertEquals("5", expanded);

   }
   @Test
   public void variable_references_should_be_resolved_against_inherited_properties(){
      vars = new Properties(vars);
      vars.setProperty("boo", "4");
      manager = new VariableManager(vars);
      String expanded = manager.expandVariables("${boo}${foo}");
      assertEquals("45", expanded);
   }
}