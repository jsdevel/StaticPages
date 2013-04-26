package com.spencernetdevelopment.arguments;

import java.io.File;
import java.lang.String;
public class StaticPagesTerminal {
   private static final boolean __showHelpOnNoArgs=true;

   public static StaticPagesArguments getArguments(String[] args) throws Throwable {
      File projectdir=null;
      File newproject=null;
      String assetprefixinbrowser=null;
      String logjproperties=null;
      String logjinterval=null;
      boolean enablecompression=false;
      boolean enabledevmode=false;
      int maxdataurisizeinbytes=32768;
      int maxwaittimetovalidateexternallink=5000;
      String prefixtoignorefiles="_";
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
         if("--asset-prefix-in-browser".equals(key)){
            assetprefixinbrowser = val;
            continue;
         }
         if("--log4j-properties".equals(key)){
            logjproperties = val;
            continue;
         }
         if("--log4j-interval".equals(key)){
            logjinterval = val;
            continue;
         }
         if("--enable-compression".equals(key)){
            enablecompression = getBoolean(val);
            continue;
         }
         if("--enable-dev-mode".equals(key)){
            enabledevmode = getBoolean(val);
            continue;
         }
         if("--max-data-uri-size-in-bytes".equals(key)){
            maxdataurisizeinbytes = getInt(val);
            continue;
         }
         if("--max-wait-time-to-validate-external-link".equals(key)){
            maxwaittimetovalidateexternallink = getInt(val);
            continue;
         }
         if("--prefix-to-ignore-files".equals(key)){
            prefixtoignorefiles = val;
            continue;
         }
         throw new IllegalArgumentException("Unknown argument: "+key);
      }
      if(i - len != 0){
         throw new IllegalArgumentException("An even number of arguments must be given.");
      }
      return new StaticPagesArguments(
            projectdir,
            newproject,
            assetprefixinbrowser,
            logjproperties,
            logjinterval,
            enablecompression,
            enabledevmode,
            maxdataurisizeinbytes,
            maxwaittimetovalidateexternallink,
            prefixtoignorefiles
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
   public static final int getInt(String in){
      if(in != null && !in.isEmpty()){
         return Integer.parseInt(in);
      }
      return 0;
   }
}
