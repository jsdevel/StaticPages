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

   public StaticPagesArguments(
      final File projectdir,
      final File newproject,
      final String assetprefixinbrowser,
      final String logjproperties,
      final String logjinterval,
      final boolean enablecompression
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
}
