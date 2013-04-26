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
   public  static final String cwd       = System.getProperty("user.dir");

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

   public String toUnix(){
      if(isUnix){
         return path;
      } else {
         String unixPath=(separator+path.substring(root.length())).replace(separator, "/");
         return unixPath;
      }
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
      if(child == null){
         throw new NullPointerException("child path must not be null");
      }
      if(child.startsWith(separator)){
         return new File(child).getCanonicalPath();
      } else {
         return new File(parent, child).getCanonicalPath();
      }
   }
   public static FilePath getFilePath(String path) throws IOException {
      assertSuppliedPath(path);

      String pathToUse= new File(path).getPath();
      if(pathToUse.indexOf(root) == -1){
         pathToUse=getAbsolutePath(cwd, path);
      }

      FilePath instance = new FilePath(pathToUse);
      return instance;
   }
   public static FilePath getFilePath() throws IOException {
      return getFilePath(cwd);
   }
   public FilePath resolve(String path) throws IOException {
      return new FilePath(getAbsolutePath(this.path, path));
   }
}
