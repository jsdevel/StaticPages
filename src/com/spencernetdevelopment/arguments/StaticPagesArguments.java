package com.spencernetdevelopment.arguments;

import java.io.File;
public class StaticPagesArguments {

   private File projectdir=null;
   private File newproject=null;

   public StaticPagesArguments(
      final File projectdir,
      final File newproject
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
}
