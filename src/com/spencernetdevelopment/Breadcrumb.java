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
public class Breadcrumb {
   private final String displayName;
   private final String domainRelativeLink;
   private final String xmlPagesDirRelativePagePath;

   public Breadcrumb(
      String displayName,
      String domainRelativeLink,
      String xmlPagesDirRelativePagePath
   ) {
      this.displayName = displayName;
      this.domainRelativeLink = domainRelativeLink;
      this.xmlPagesDirRelativePagePath = xmlPagesDirRelativePagePath;
   }

   public String getDisplayName() {
      return displayName;
   }
   public String getDomainRelativeLink() {
      return domainRelativeLink;
   }
   public String getXmlPagesRelativePath() {
      return xmlPagesDirRelativePagePath;
   }
}
