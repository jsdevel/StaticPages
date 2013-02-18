/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
