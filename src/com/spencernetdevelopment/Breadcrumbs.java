/*
 * Copyright 2013 Joseph Spencer.
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class isn't thread safe.
 *
 * @author Joseph Spencer
 */
public class Breadcrumbs {
   public final Pattern CRUMB_REGEX;
   private final List<Breadcrumb> crumbs;
   private final String browserPrefix;
   private final String domainRelativeHtmlPagePath;
   private final BreadcrumbFactory factory;
   private boolean crumbsAreFilled;
   //used to prevent an infinite loop in crumb processing
   private int crumbsProcessed;
   private int maxCrumbs = 20;

   public Breadcrumbs(
      String browserPrefix,
      String domainRelativeHtmlPagePath,
      BreadcrumbFactory factory
   ) {
      if(browserPrefix == null){
         throw new NullPointerException("browserPrefix was null");
      }
      if(domainRelativeHtmlPagePath == null){
         throw new NullPointerException("domainRelativeHtmlPagePath was null");
      }
      if(browserPrefix.endsWith("/")){
         throw new IllegalArgumentException("browserPrefix ended with \"/\".");
      }
      this.browserPrefix=browserPrefix;
      this.domainRelativeHtmlPagePath=domainRelativeHtmlPagePath;
      this.crumbs = new ArrayList<>();
      this.factory=factory;
      this.CRUMB_REGEX=Pattern
         .compile("^/(?:(?:[^/]+/)*([^/]+)/)?([^/]+)\\.html$");
   }

   public boolean isEmpty(){
      fillCrumbs();
      return crumbs.isEmpty();
   }
   public int size(){
      fillCrumbs();
      return crumbs.size();
   }
   public Breadcrumb take(){
      fillCrumbs();
      return crumbs.remove(0);
   }

   /**
    * It's not safe to call this without obtaining a lock on crumbs first.
    * @throws IllegalArgumentException if maxCrumbs is exceeded during crumb
    *    processing.
    */
   private void fillCrumbs(){
      String crumb=domainRelativeHtmlPagePath;
      if(
         !crumbsAreFilled &&
         crumb != null
      ){
         crumbsAreFilled = true;
         while(!crumb.isEmpty()){
            crumbsProcessed++;
            if(crumbsProcessed > maxCrumbs){
               throw new IllegalArgumentException(
                  "The max number of crumbs to process was exceeded."
               );
            }
            switch(crumb){
               case "":
               case "/":
               case "/index.html":
                  return;
               default:
                  Matcher matcher = CRUMB_REGEX.matcher(crumb);
                  if(matcher.find()){
                     String dirname = matcher.group(1);
                     String filename = matcher.group(2);
                     if("index".equals(filename)){
                        String pageLink = crumb.replaceFirst("index.html$", "");
                        String xmlPath = crumb
                           .replaceFirst("index.html$", "index.xml")
                           .replaceFirst("^/", "");
                        crumbs.add(0, factory.makeBreadcrumb(
                           dirname,
                           browserPrefix+pageLink,
                           xmlPath
                        ));
                        crumb = crumb
                           .replaceFirst(dirname+"/index.html$", "index.html");
                     } else {
                        String xmlPath = crumb
                           .replaceFirst(".html$", ".xml")
                           .replaceFirst("^/", "");
                        crumbs.add(0, factory.makeBreadcrumb(
                           filename,
                           browserPrefix+crumb,
                           xmlPath
                        ));
                        crumb = crumb
                           .replaceFirst(filename+".html$", "index.html");
                     }
                  } else {
                     return;
                  }
            }
         }
      }
   }
}
