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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is not thread safe.
 *
 * @author Joseph Spencer
 */
public class VariableManager {
   private static final Pattern VARIABLES = Pattern.compile(
      "\\$\\{([a-zA-Z_][0-9a-zA-Z_]*+)\\}"
   );
   private final List<Properties> contexts=new ArrayList<>();
   private Properties currentVariables;

   public VariableManager(Properties initialVariables) {
      this.currentVariables = initialVariables;
   }

   public String expandVariables(String text){
      String returnText = text;
      if(text == null){
         return "";
      }
      Matcher vars = VARIABLES.matcher(text);
      while(vars.find()){
         String var = currentVariables.getProperty(vars.group(1));
         if(var != null){
            returnText = returnText.replace(vars.group(0), var);
         }
      }
      return returnText.replaceAll(VARIABLES.pattern(), "");
   }

   public void addContext(){
      contexts.add(currentVariables);
      currentVariables = new Properties(currentVariables);
   }

   public void removeContext(){
      int size = contexts.size();
      if(size > 0) {
         currentVariables = contexts.remove(size-1);
      }
   }

   public String getVariable(String key){
      return currentVariables.getProperty(key);
   }

   public void setVariable(String key, String value){
      currentVariables.setProperty(key, value);
   }

}
