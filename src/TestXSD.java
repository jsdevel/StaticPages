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

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 * This is a test class used to test XSD.
 *
 * @author Joseph Spencer
 */
public class TestXSD {
   public static void main(String[] args) {

      File pageXSD = new File("/home/joseph/NetBeansProjects/StaticPages/src/page.xsd");
      File xml = new File("/home/joseph/NetBeansProjects/StaticPages/src/index.xml");
      SchemaFactory schemaFactory = SchemaFactory
         .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schemaFile;
      try {
         schemaFile = schemaFactory.newSchema(pageXSD);
         Validator validator = schemaFile.newValidator();
         validator.validate(new StreamSource(xml));
      } catch (SAXException ex) {
         System.out.println(ex.getLocalizedMessage());
         System.out.println(ex.getMessage());
      } catch (IOException ex) {
         System.out.println("Couldn't read xml file.");
      }
   }
}
