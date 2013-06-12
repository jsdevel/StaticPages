package com.spencernetdevelopment.arguments;

import java.io.File;
import java.lang.String;
public class StaticPagesArguments {

   private File projectdir=null;
   private File newproject=null;
   private String assetprefixinbrowser=null;
   private String devassetprefixinbrowser=null;
   private boolean enablecompression=false;
   private boolean clean=false;
   private boolean enableassetfingerprinting=false;
   private boolean enabledevmode=false;
   private int maxdataurisizeinbytes=32768;
   private int maxwaittimetovalidateexternallink=5000;
   private boolean enableexternallinkvalidation=true;
   private String prefixtoignorefiles="_";
   private boolean enablelogginginfo=true;
   private boolean enableloggingwarn=true;
   private boolean enableloggingerror=true;
   private boolean enableloggingfatal=true;
   private boolean enableloggingdebug=false;
   private int logginglevel=0;
   private File variables=null;

   public StaticPagesArguments(
      final File projectdir,
      final File newproject,
      final String assetprefixinbrowser,
      final String devassetprefixinbrowser,
      final boolean enablecompression,
      final boolean clean,
      final boolean enableassetfingerprinting,
      final boolean enabledevmode,
      final int maxdataurisizeinbytes,
      final int maxwaittimetovalidateexternallink,
      final boolean enableexternallinkvalidation,
      final String prefixtoignorefiles,
      final boolean enablelogginginfo,
      final boolean enableloggingwarn,
      final boolean enableloggingerror,
      final boolean enableloggingfatal,
      final boolean enableloggingdebug,
      final int logginglevel,
      final File variables
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
      this.devassetprefixinbrowser=devassetprefixinbrowser;
      this.enablecompression=enablecompression;
      this.clean=clean;
      this.enableassetfingerprinting=enableassetfingerprinting;
      this.enabledevmode=enabledevmode;
      this.maxdataurisizeinbytes=maxdataurisizeinbytes;
      this.maxwaittimetovalidateexternallink=maxwaittimetovalidateexternallink;
      this.enableexternallinkvalidation=enableexternallinkvalidation;
      this.prefixtoignorefiles=prefixtoignorefiles;
      this.enablelogginginfo=enablelogginginfo;
      this.enableloggingwarn=enableloggingwarn;
      this.enableloggingerror=enableloggingerror;
      this.enableloggingfatal=enableloggingfatal;
      this.enableloggingdebug=enableloggingdebug;
      this.logginglevel=logginglevel;
      this.variables=variables;
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
   public String getDevassetprefixinbrowser(){
      return devassetprefixinbrowser;
   }
   public boolean hasDevassetprefixinbrowser(){
      return devassetprefixinbrowser!=null;
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
   public boolean getEnableexternallinkvalidation(){
      return enableexternallinkvalidation;
   }
   public boolean hasEnableexternallinkvalidation(){
      return enableexternallinkvalidation;
   }
   public String getPrefixtoignorefiles(){
      return prefixtoignorefiles;
   }
   public boolean hasPrefixtoignorefiles(){
      return prefixtoignorefiles!=null;
   }
   public boolean getEnablelogginginfo(){
      return enablelogginginfo;
   }
   public boolean hasEnablelogginginfo(){
      return enablelogginginfo;
   }
   public boolean getEnableloggingwarn(){
      return enableloggingwarn;
   }
   public boolean hasEnableloggingwarn(){
      return enableloggingwarn;
   }
   public boolean getEnableloggingerror(){
      return enableloggingerror;
   }
   public boolean hasEnableloggingerror(){
      return enableloggingerror;
   }
   public boolean getEnableloggingfatal(){
      return enableloggingfatal;
   }
   public boolean hasEnableloggingfatal(){
      return enableloggingfatal;
   }
   public boolean getEnableloggingdebug(){
      return enableloggingdebug;
   }
   public boolean hasEnableloggingdebug(){
      return enableloggingdebug;
   }
   public int getLogginglevel(){
      return logginglevel;
   }
   public boolean hasLogginglevel(){
      return logginglevel!=0;
   }
   public File getVariables(){
      return variables;
   }
   public boolean hasVariables(){
      return variables!=null;
   }
}
