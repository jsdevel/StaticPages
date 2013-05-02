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

/**
 *
 * @author Joseph Spencer
 */
public class Logger {
   public static boolean isDebug=false;
   public static boolean isInfo=true;
   public static boolean isWarn=true;
   public static boolean isError=true;
   public static boolean isFatal=true;
   public static int logLevel;
   private static final String NAME = Logger.class.getName();

   public static void debug(String msg){
      if(isDebug){
         msg("DEBUG "+msg);
      }
   }
   public static void info(String msg){
      if(isInfo){
         msg("INFO "+msg);
      }
   }
   public static void warn(String msg){
      if(isWarn){
         msg("WARN "+msg);
      }
   }
   public static void error(String msg){
      if(isError){
         msg("ERROR "+msg);
      }
   }
   public static void fatal(String msg, Integer code){
       if(isFatal){
         msg("FATAL "+msg);
         if(code != null){
            System.exit(code);
         }
      }
   }
   public static void fatal(String msg){
      fatal(msg, null);
   }
   private static void msg(String msg){
      if(logLevel > 0){
         StackTraceElement[] stackTrace = new Throwable().getStackTrace();
         for(StackTraceElement element: stackTrace){
            String callingClassName = element.getClassName();
            if(!NAME.equals(callingClassName)){
               msg+="\n  -> Class  : "+callingClassName;
               if(logLevel > 1){
               msg+="\n  -> Line   : "+element.getLineNumber();
               }
               if(logLevel > 2){
               msg+="\n  -> File   : "+element.getFileName();
               }
               if(logLevel > 3){
               msg+="\n  -> Method : "+element.getMethodName();
               }
               break;
            }
         }
      }
      System.out.println(msg);
   }
}