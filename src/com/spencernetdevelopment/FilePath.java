package com.spencernetdevelopment;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Bobby
 */
public class FilePath {
   private static final String separator = File.separator;
   private static final String root      = new File(File.separator).getAbsolutePath();
   private static final boolean isUnix   = "/".equals(separator);
   private static final String cwd       = System.getProperty("user.dir");

   private final String path;

   private FilePath(String path) throws IOException {
      assertSuppliedPath(path);
      this.path = path;
   }

   @Override
   public boolean equals(Object obj) {
      if(obj == null){
         return false;
      }
      return path.equals(obj.toString());
   }

   @Override
   public int hashCode() {
      return path.hashCode();
   }

   @Override
   public String toString() {
      return path;
   }

   public FilePath getParent() throws IOException {
      return resolve("..");
   }

   public File toFile(){
      return new File(path);
   }

   private static void assertSuppliedPath(String path) throws IOException {
      if(path == null || path.length() == 0){
         throw new IOException("Can't construct a FilePath from an empty path.");
      }
   }
   private static String getAbsolutePath(String parent, String child) throws IOException {
      return new File(parent, child).getCanonicalPath();
   }
   public static FilePath getFilePath(String path) throws IOException {
      assertSuppliedPath(path);
      String pathToUse;

      if(path.startsWith(root)){
         pathToUse=path;
      } else {
         pathToUse=getAbsolutePath(cwd, path);
      }

      FilePath instance = new FilePath(pathToUse);
      return instance;
   }
   public FilePath resolve(String path) throws IOException {
      return new FilePath(getAbsolutePath(this.path, path));
   }
}
