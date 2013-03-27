/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import org.apache.log4j.Logger;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

/**
 *
 * @author Joseph Spencer
 */
public class JSErrorReporter implements ErrorReporter {
   public static final JSErrorReporter INSTANCE = new JSErrorReporter();
   private static final Logger LOGGER = Logger.getLogger(JSErrorReporter.class.getName());
   private JSErrorReporter(){}

   @Override
   public void warning(String message, String source, int line, String lineContent, int lineOffset) {
      LOGGER.warn(reportError(message, source, line, lineContent, lineOffset));
   }

   @Override
   public void error(String message, String source, int line, String lineContent, int lineOffset) {
      LOGGER.error(reportError(message, source, line, lineContent, lineOffset));
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
