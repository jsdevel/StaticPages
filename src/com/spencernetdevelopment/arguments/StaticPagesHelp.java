package com.spencernetdevelopment.arguments;

public class StaticPagesHelp {
   public static String getHelpMenu(){
return "TITLE\n   StaticPages \n\nCOPYRIGHT\n   © 2012 Joseph Spencer \n\nLICENSE\n\n   http://www.apache.org/licenses/LICENSE-2.0 \n\nDESCRIPTION\n   StaticPages builds static HTML files \n   using XML + XSL. \n\nEXAMPLE\n   java -jar --src-dir my/dir/src \n   --build-dir my/dir/build \n\nARGUMENTS\n   OPTIONAL\n      --project-dir\n         The base path of your project. \n\n      --new-project\n         Creates a default project in the \n         specified directory. \n\n";   }
}
