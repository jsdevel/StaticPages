package com.spencernetdevelopment.arguments;

import java.io.File;
public class StaticPagesTerminal {
   private static final boolean __showHelpOnNoArgs=true;

   public static StaticPagesArguments getArguments(String[] args) throws Throwable {
      File projectdir=null;
      File newproject=null;
      if(__showHelpOnNoArgs && args.length == 0){
         System.out.print(StaticPagesHelp.getHelpMenu());
         System.exit(0);
      }
      int len = args.length;
      int i=0;
      for(;i+1<len;i+=2){
         String key = args[i];
         String val = args[i+1];
         if("--project-dir".equals(key)){
            String newPath = getPath(val);
            projectdir = new File(newPath);
            continue;
         }
         if("--new-project".equals(key)){
            String newPath = getPath(val);
            newproject = new File(newPath);
            continue;
         }
         throw new IllegalArgumentException("Unknown argument: "+key);
      }
      if(i - len != 0){
         throw new IllegalArgumentException("An even number of arguments must be given.");
      }
      return new StaticPagesArguments(
            projectdir,
            newproject
      );
   }
   public static final String getPath(String path){
      String pathToUse;
      if(path.startsWith("/")){
         pathToUse = path;
      } else {
         pathToUse = System.getProperty("user.dir")+"/"+path;
      }
      return pathToUse;
   }
   public static final boolean getBoolean(String bool){
      if(bool != null){
         String s = bool.toLowerCase();
         if("true".equals(bool) || "yes".equals(bool) || "1".equals(bool)){
            return true;
         }
      }
      return false;
   }
}
