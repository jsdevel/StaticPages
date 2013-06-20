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

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Joseph Spencer
 */
public class VariableManager {
   private static final Pattern VARIABLES = Pattern.compile(
      "\\$\\{([a-zA-Z_][0-9a-zA-Z_]*)\\}"
   );
   private final Properties variables;

   public VariableManager(Properties variables) {
      this.variables = variables;
   }

   public String expandVariables(String text){
      String returnText = text;
      if(text == null){
         return "";
      }
      Matcher vars = VARIABLES.matcher(text);
      while(vars.find()){
         String var = variables.getProperty(vars.group(1));
         if(var != null){
            returnText = returnText.replace(vars.group(0), var);
         }
      }
      return returnText.replaceAll(VARIABLES.pattern(), "");
   }

}
