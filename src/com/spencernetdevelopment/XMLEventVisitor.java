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

import java.nio.file.Path;

/**
 *
 * @author Joseph Spencer
 */
public interface XMLEventVisitor {
   void visitAddedDirectory(Path newDirectory);
   void visitRemovedDirectory(Path removedDirectory);
   void visitModifiedXMLFile(Path modifiedXMLFile);
   void visitRemovedXMLFile(Path removedXMLFile);
   void visitModifiedXSLFile(Path modifiedXSLFile);
}
