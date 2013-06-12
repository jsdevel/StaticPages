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

/**
 *
 * @author Joseph Spencer
 */
public class StaticPagesConfiguration {
   private final boolean enableDevMode;
   private final boolean enableCompression;
   private final String assetFingerprint;
   private final FilePath assetsDirPath;
   private final FilePath buildDirPath;
   private final FilePath pagesDirPath;
   private final FilePath viewsDirPath;
   private final FilePath projectDirPath;
   private final FilePath xmlResourcesDirPath;
   private final FilePath srcDirPath;
   private final FilePath xslDirPath;
   private final String assetPrefixInBrowser;
   private final String prefixToIgnoreFilesWith;
   private final int maxDataURISizeInBytes;
   private final int maxTimeToWaitForExternalLinkValidation;

   public StaticPagesConfiguration(Builder builder){
      enableDevMode=builder.enableDevMode;
      enableCompression=builder.enableCompression;
      assetFingerprint=builder.assetFingerprint;
      assetsDirPath=builder.assetsDirPath;
      buildDirPath=builder.buildDirPath;
      pagesDirPath=builder.pagesDirPath;
      viewsDirPath=builder.viewsDirPath;
      projectDirPath=builder.projectDirPath;
      xmlResourcesDirPath=builder.xmlResourcesDirPath;
      srcDirPath=builder.srcDirPath;
      xslDirPath=builder.xslDirPath;
      assetPrefixInBrowser=builder.assetPrefixInBrowser;
      prefixToIgnoreFilesWith=builder.prefixToIgnoreFilesWith;
      maxDataURISizeInBytes=builder.maxDataURISizeInBytes;
      maxTimeToWaitForExternalLinkValidation=
                                 builder.maxTimeToWaitForExternalLinkValidation;

   }

   public String getAssetFingerprint() {
      return assetFingerprint;
   }

   public String getAssetPrefixInBrowser() {
      return assetPrefixInBrowser;
   }

   public FilePath getAssetsDirPath() {
      return assetsDirPath;
   }

   public FilePath getBuildDirPath() {
      return buildDirPath;
   }

   public int getMaxDataURISizeInBytes() {
      return maxDataURISizeInBytes;
   }

   public int getMaxTimeToWaitForExternalLinkValidation() {
      return maxTimeToWaitForExternalLinkValidation;
   }

   public FilePath getPagesDirPath() {
      return pagesDirPath;
   }

   public String getPrefixToIgnoreFilesWith() {
      return prefixToIgnoreFilesWith;
   }

   public FilePath getProjectDirPath() {
      return projectDirPath;
   }

   public FilePath getSrcDirPath() {
      return srcDirPath;
   }

   public FilePath getViewsDirPath() {
      return viewsDirPath;
   }

   public FilePath getXmlResourcesDirPath() {
      return xmlResourcesDirPath;
   }

   public FilePath getXslDirPath() {
      return xslDirPath;
   }

   public boolean isEnableCompression() {
      return enableCompression;
   }

   public boolean isEnableDevMode() {
      return enableDevMode;
   }

   public static class Builder {
      private boolean enableDevMode;
      private boolean enableCompression;
      private String assetFingerprint="";
      private FilePath assetsDirPath;
      private FilePath buildDirPath;
      private FilePath pagesDirPath;
      private FilePath viewsDirPath;
      private FilePath projectDirPath;
      private FilePath xmlResourcesDirPath;
      private FilePath srcDirPath;
      private FilePath xslDirPath;
      private String assetPrefixInBrowser="";
      private String prefixToIgnoreFilesWith;
      private int maxDataURISizeInBytes;
      private int maxTimeToWaitForExternalLinkValidation=0;

      public Builder(){}

      public Builder setAssetFingerprint(String assetFingerprint) {
         this.assetFingerprint = assetFingerprint;
         return this;
      }

      public Builder setAssetPrefixInBrowser(String assetPrefixInBrowser) {
         if(
            "/".equals(assetPrefixInBrowser) ||
            assetPrefixInBrowser.endsWith("/")
         ){
            assetPrefixInBrowser = assetPrefixInBrowser.replaceAll("/+$", "");
         }
         this.assetPrefixInBrowser = assetPrefixInBrowser;
         return this;
      }

      public Builder setAssetsDirPath(FilePath assetsDirPath) {
         this.assetsDirPath = assetsDirPath;
         return this;
      }

      public Builder setBuildDirPath(FilePath buildDirPath) {
         this.buildDirPath = buildDirPath;
         return this;
      }

      public Builder setEnableCompression(boolean enableCompression) {
         this.enableCompression = enableCompression;
         return this;
      }

      public Builder setEnableDevMode(boolean enableDevMode) {
         this.enableDevMode = enableDevMode;
         return this;
      }

      public Builder setMaxDataURISizeInBytes(int maxDataURISizeInBytes) {
         this.maxDataURISizeInBytes = maxDataURISizeInBytes;
         return this;
      }

      public Builder setMaxTimeToWaitForExternalLinkValidation(
         int maxTimeToWaitForExternalLinkValidation)
      {
         this.maxTimeToWaitForExternalLinkValidation =
                 maxTimeToWaitForExternalLinkValidation;
         return this;
      }

      public Builder setPagesDirPath(FilePath pagesDirPath) {
         this.pagesDirPath = pagesDirPath;
         return this;
      }

      public Builder setPrefixToIgnoreFilesWith(String prefixToIgnoreFilesWith) {
         this.prefixToIgnoreFilesWith = prefixToIgnoreFilesWith;
         return this;
      }

      public Builder setProjectDirPath(FilePath projectDirPath) {
         this.projectDirPath = projectDirPath;
         return this;
      }

      public Builder setSrcDirPath(FilePath srcDirPath) {
         this.srcDirPath = srcDirPath;
         return this;
      }

      public Builder setViewsDirPath(FilePath viewsDirPath) {
         this.viewsDirPath = viewsDirPath;
         return this;
      }

      public Builder setXmlResourcesDirPath(FilePath xmlResourcesDirPath) {
         this.xmlResourcesDirPath = xmlResourcesDirPath;
         return this;
      }

      public Builder setXslDirPath(FilePath xslDirPath) {
         this.xslDirPath = xslDirPath;
         return this;
      }

      public StaticPagesConfiguration build(){
         return new StaticPagesConfiguration(this);
      }
   }
}
