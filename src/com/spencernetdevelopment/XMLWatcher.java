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

import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Joseph Spencer
 */
public class XMLWatcher {

   private final Map<WatchKey, Path> keys = new HashMap<WatchKey, Path>();
   private final WatchService watcher;
   private final Path xmlDir;

   public XMLWatcher(Path srcDir) throws Exception {
      this.watcher = FileSystems.getDefault().newWatchService();
      this.xmlDir = srcDir;
      registerAll(srcDir);
   }

   @SuppressWarnings("unchecked")
   static <T> WatchEvent<T> cast(WatchEvent<?> event) {
      return (WatchEvent<T>) event;
   }

   /**
    * Process all events for keys queued to the watcher
    */
   void processEvents(final XMLEventVisitor visitor) {
      for (;;) {
         // wait for key to be signalled
         WatchKey key;
         try {
            key = watcher.take();
         } catch (InterruptedException x) {
            return;
         }

         Path dir = keys.get(key);
         if (dir == null) {
            System.err.println("WatchKey not recognized!!");
            continue;
         }


         for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent.Kind kind = event.kind();

            // TBD - provide example of how OVERFLOW event is handled
            if (kind == OVERFLOW) {
               continue;
            }

            // Context for directory entry event is the file name of entry
            WatchEvent<Path> ev = cast(event);
            Path shortName = ev.context();
            Path fullPath = dir.resolve(shortName);

            // print out event
            //System.out.format("%s: %s\n", event.kind().name(), fullPath);

            if(hasXMLExtension(fullPath)){
               if(kind == ENTRY_CREATE || kind == ENTRY_MODIFY){
                  visitor.visitModifiedXMLFile(fullPath);
               } else if(kind == ENTRY_DELETE){
                  visitor.visitRemovedXMLFile(fullPath);
               }
            } else if(hasXSLExtension(fullPath)){
               visitor.visitModifiedXSLFile(fullPath);
            } else if(Files.isDirectory(fullPath, NOFOLLOW_LINKS)) {
               if(kind == ENTRY_CREATE){
                  try {
                     registerAll(fullPath);
                     visitor.visitAddedDirectory(fullPath);
                  } catch (IOException exc){}
               } else if(kind == ENTRY_DELETE){
                  visitor.visitRemovedDirectory(fullPath);
               }
            } else if(Files.isRegularFile(fullPath, NOFOLLOW_LINKS)){

            }
         }

         // reset key and remove from set if directory no longer accessible
         boolean valid = key.reset();
         if (!valid) {
            keys.remove(key);
            visitor.visitRemovedDirectory(dir);
            // all directories are inaccessible
            if (keys.isEmpty()) {
               break;
            }
         }
      }
   }

   /**
    * Register the given directory with the WatchService
    */
   private void register(Path dir) throws IOException {
      WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
      keys.put(key, dir);
   }

   private boolean hasXMLExtension(Path path){
      if(path==null){
         return false;
      }
      return path.toString().endsWith(".xml");
   }
   private boolean hasXSLExtension(Path path){
      if(path==null){
         return false;
      }
      return path.toString().endsWith(".xsl");
   }

   /**
    * Register the given directory, and all its sub-directories, with the
    * WatchService.
    */
   private void registerAll(final Path start) throws IOException {
      // register directory and sub-directories
      Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
         @Override
         public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                 throws IOException {
            register(dir);
            return FileVisitResult.CONTINUE;
         }
      });
   }
}
