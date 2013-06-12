/*
 * Copyright 2012 Joseph Spencer.
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class rewrites files within a given directory.
 *
 * @author Joseph Spencer
 */
public class RewriteManager {
   private final FilePath baseFilePath;
   private final FileUtils fileUtils;
   private final Map<String, Set<String>> sourceToTargets = new HashMap<>();
   private final Map<String, String> targetsToSource = new HashMap<>();
   private final List<String> rewritesToApply = Collections.synchronizedList(new ArrayList<String>());

   public RewriteManager(FilePath baseFilePath, FileUtils fileUtils){
      if(baseFilePath == null){
         throw new NullPointerException("baseFilePath was null");
      }
      if(fileUtils == null){
         throw new NullPointerException("fileUtils was null");
      }
      this.baseFilePath=baseFilePath;
      this.fileUtils=fileUtils;
   }
   /**
    * Rewrites the given src to the destination, so that when the browser
    * requests the destination, it will see src.  This method manages an
    * internal queue only, and does <b>not</b> create any files.
    *
    * @param src  file path to source
    * @param target HTTP URL format I.E. "some/url-to/rewrite"
    * @throws IOException
    */
   public synchronized void queueRewrite(String src, String target) {
      if(src == null){
         throw new NullPointerException("src was null");
      }
      if(target == null){
         throw new NullPointerException("dest was null");
      }

      String relativeSrc = fileUtils.getForcedRelativePath(src);
      String relativeTarget = fileUtils.getForcedRelativePath(target);
      if(!relativeTarget.endsWith(".html")){
         relativeTarget = relativeTarget.replaceAll("/+$", "");
         relativeTarget+= "/index.html";
      }

      String rewrittenSrc = targetsToSource.get(relativeTarget);
      Set<String> srcRewrites = sourceToTargets.get(relativeSrc);
      if(!sourceToTargets.containsKey(relativeSrc)){
         srcRewrites = new HashSet<>();
         sourceToTargets.put(relativeSrc, srcRewrites);
      }

      if(rewrittenSrc != null && !relativeSrc.equals(rewrittenSrc)){
         throw new IllegalArgumentException(
            "Can't rewrite src because another file uses the same rewrite.\n"+
            "The src was: "+src+"\n"+
            "The target was: "+target
         );
      } else if(!srcRewrites.contains(relativeTarget)){
         rewritesToApply.add(relativeTarget);
         targetsToSource.put(relativeTarget, relativeSrc);
         srcRewrites.add(relativeTarget);
      }
   }
   /**
    * Flushes the current queue of rewrites that are waiting to be applied and
    * applies them to the file system.
    */
   public synchronized void applyRewrites() throws IOException {
      int currentSizeOfList = rewritesToApply.size();
      if(currentSizeOfList > 0){
         List<String> currentRewrites = rewritesToApply.subList(0, currentSizeOfList);
         for(String target: currentRewrites){
            String src = targetsToSource.get(target);
            FilePath srcPath = baseFilePath.resolve(src);
            File file = srcPath.toFile();
            boolean fileExists = file.isFile();
            if(!fileExists){
               throw new IOException(
                  "Can't rewrite src because the src file doesn't exist.\n"+
                  "Src was: "+src
               );
            }
            FilePath targetPath;
            targetPath = baseFilePath.resolve(target);
            String from = srcPath.toString();
            String to = targetPath.toString();
            fileUtils.copyFile(from, to);
         }
         rewritesToApply.removeAll(currentRewrites);
      }
   }
}
