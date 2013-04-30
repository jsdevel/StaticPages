package com.spencernetdevelopment.arguments;

import java.io.File;
import java.lang.String;
public class StaticPagesArguments {

   private File projectdir=null;
   private File newproject=null;
   private String assetprefixinbrowser=null;
   private String logjproperties=null;
   private String logjinterval=null;
   private boolean enablecompression=false;
   private boolean clean=false;
   private boolean enableassetfingerprinting=false;
   private boolean enabledevmode=false;
   private int maxdataurisizeinbytes=32768;
   private int maxwaittimetovalidateexternallink=5000;
   private String prefixtoignorefiles="_";

   public StaticPagesArguments(
      final File projectdir,
      final File newproject,
      final String assetprefixinbrowser,
      final String logjproperties,
      final String logjinterval,
      final boolean enablecompression,
      final boolean clean,
      final boolean enableassetfingerprinting,
      final boolean enabledevmode,
      final int maxdataurisizeinbytes,
      final int maxwaittimetovalidateexternallink,
      final String prefixtoignorefiles
   ) throws Throwable {
      if(projectdir != null){
         if(!projectdir.exists() || !projectdir.isDirectory()){
            projectdir.mkdirs();
         }
         if(!(projectdir.exists() && projectdir.isDirectory())){
            throw new IllegalArgumentException("Directory doesn't exist :'"+projectdir+"'.  Given by argument 'projectdir'.");
         }
      }
      if(newproject != null){
         if(!newproject.exists() || !newproject.isDirectory()){
            newproject.mkdirs();
         }
         if(!(newproject.exists() && newproject.isDirectory())){
            throw new IllegalArgumentException("Directory doesn't exist :'"+newproject+"'.  Given by argument 'newproject'.");
         }
      }
      this.projectdir=projectdir;
      this.newproject=newproject;
      this.assetprefixinbrowser=assetprefixinbrowser;
      this.logjproperties=logjproperties;
      this.logjinterval=logjinterval;
      this.enablecompression=enablecompression;
      this.clean=clean;
      this.enableassetfingerprinting=enableassetfingerprinting;
      this.enabledevmode=enabledevmode;
      this.maxdataurisizeinbytes=maxdataurisizeinbytes;
      this.maxwaittimetovalidateexternallink=maxwaittimetovalidateexternallink;
      this.prefixtoignorefiles=prefixtoignorefiles;
   }

   public File getProjectdir(){
      return projectdir;
   }
   public boolean hasProjectdir(){
      return projectdir!=null;
   }
   public File getNewproject(){
      return newproject;
   }
   public boolean hasNewproject(){
      return newproject!=null;
   }
   public String getAssetprefixinbrowser(){
      return assetprefixinbrowser;
   }
   public boolean hasAssetprefixinbrowser(){
      return assetprefixinbrowser!=null;
   }
   public String getLogjproperties(){
      return logjproperties;
   }
   public boolean hasLogjproperties(){
      return logjproperties!=null;
   }
   public String getLogjinterval(){
      return logjinterval;
   }
   public boolean hasLogjinterval(){
      return logjinterval!=null;
   }
   public boolean getEnablecompression(){
      return enablecompression;
   }
   public boolean hasEnablecompression(){
      return enablecompression;
   }
   public boolean getClean(){
      return clean;
   }
   public boolean hasClean(){
      return clean;
   }
   public boolean getEnableassetfingerprinting(){
      return enableassetfingerprinting;
   }
   public boolean hasEnableassetfingerprinting(){
      return enableassetfingerprinting;
   }
   public boolean getEnabledevmode(){
      return enabledevmode;
   }
   public boolean hasEnabledevmode(){
      return enabledevmode;
   }
   public int getMaxdataurisizeinbytes(){
      return maxdataurisizeinbytes;
   }
   public boolean hasMaxdataurisizeinbytes(){
      return maxdataurisizeinbytes!=0;
   }
   public int getMaxwaittimetovalidateexternallink(){
      return maxwaittimetovalidateexternallink;
   }
   public boolean hasMaxwaittimetovalidateexternallink(){
      return maxwaittimetovalidateexternallink!=0;
   }
   public String getPrefixtoignorefiles(){
      return prefixtoignorefiles;
   }
   public boolean hasPrefixtoignorefiles(){
      return prefixtoignorefiles!=null;
   }
}
