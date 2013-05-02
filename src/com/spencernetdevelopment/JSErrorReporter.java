/*
 * Copyright 2012 Joseph Spencer.
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

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

/**
 *
 * @author Joseph Spencer
 */
public class JSErrorReporter implements ErrorReporter {
   public static final JSErrorReporter INSTANCE = new JSErrorReporter();
   private JSErrorReporter(){}

   @Override
   public void warning(String message, String source, int line, String lineContent, int lineOffset) {
      Logger.warn(reportError(message, source, line, lineContent, lineOffset));
   }

   @Override
   public void error(String message, String source, int line, String lineContent, int lineOffset) {
      Logger.error(reportError(message, source, line, lineContent, lineOffset));
   }

   @Override
   public EvaluatorException runtimeError(String message, String source, int line, String lineContent, int lineOffset) {
      return new EvaluatorException(message, lineContent, lineOffset, lineContent, lineOffset);
   }

   private String reportError(String message, String source, int line, String lineContent, int lineOffset){
      return "\nFound the following javascript error:\n"+message+"\nWhile parsing:\n"+source+
              "\nAt line: "+line+"At offset: "+lineOffset+(lineContent!=null?"\nHere is the line: \n"+lineContent:"")+"\n\n";

   }
}
