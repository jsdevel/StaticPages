/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Joseph Spencer
 */
public class HTMLBuilder {

    private final FilePath buildDirPath;
    private final FilePath xmlPagesDirPath;
    private final String xmlPagesDirString;
    private final int xmlPagesDirStringLength;
    private File defaultStylesheet;
    private final DocumentBuilderFactory docBuilderFactory;
    private final DocumentBuilder docBuilder;
    private final TransformerFactory transformerFactory;
    private StreamSource xslStream;
    private Transformer defaultXSLTransformer;
    private Map<String, Transformer> pageTransformers;

    public HTMLBuilder(FilePath buildDirPath, FilePath pagesDirPath) throws ParserConfigurationException {
        docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setNamespaceAware(true);
        docBuilder = docBuilderFactory.newDocumentBuilder();
        transformerFactory = TransformerFactory.newInstance();

        this.buildDirPath = buildDirPath;
        this.xmlPagesDirPath = pagesDirPath;
        xmlPagesDirString = xmlPagesDirPath.toString();
        xmlPagesDirStringLength = xmlPagesDirString.length();
    }

    public void setDefaultStylesheet(File defaultStylesheet) throws IOException, TransformerConfigurationException {
        assertStylesheetExists(defaultStylesheet);
        this.defaultStylesheet = defaultStylesheet;
        xslStream = new StreamSource(defaultStylesheet);
        defaultXSLTransformer = transformerFactory.newTransformer(xslStream);
        addDefaultParameters(defaultXSLTransformer);
    }

    public void buildPages() throws IOException, SAXException, TransformerException {
        if (defaultStylesheet == null) {
            throw new IllegalStateException("A default stylesheet is required to process xml files.");
        }

        ArrayList<Path> xmlPagesToBuild = new ArrayList<>();

        FileUtils.filePathsToArrayList(xmlPagesDirPath.toFile(), xmlPagesToBuild, ".xml");

        for (Path xmlFilePath : xmlPagesToBuild) {
            buildPage(xmlFilePath);
        }
    }

    /**
     * Builds an xml file within the xmlPagesDir. Converts it to HTML in the
     * build directory.
     *
     * @param xmlFilePath
     * @throws SAXException
     * @throws TransformerException
     * @throws IOException
     */
    public void buildPage(Path xmlFilePath) throws SAXException, TransformerException, IOException {
        if(xmlFilePath != null && !xmlFilePath.toFile().getName().startsWith(StaticPages.prefixToIgnoreFilesWith)){
            Document xmlDocument = docBuilder.parse(xmlFilePath.toFile());
            FilePath outputFilePath = buildDirPath.resolve(xmlFilePath.toString().substring(xmlPagesDirStringLength + 1).replaceFirst("\\.xml$", ".html"));
            File htmlFile = outputFilePath.toFile();

            Node alternateNameNode = xmlDocument.getFirstChild().getAttributes().getNamedItem("alternate-name");

            if (alternateNameNode != null) {
                String alternateName = alternateNameNode.getNodeValue();
                transform(xmlDocument, outputFilePath.getParent().resolve(alternateName + ".html").toFile(), xmlFilePath);
            }
            transform(xmlDocument, htmlFile, xmlFilePath);
        }
    }

    public void transform(Document xmlDocument, File outputFile, Path outputFilePath) throws TransformerException, IOException {
        FileUtils.createFile(outputFile);
        DOMSource xmlDoc = new DOMSource(xmlDocument);
        StreamResult resultStream = new StreamResult(outputFile);

        Node stylesheetAttribute = xmlDocument.getFirstChild().getAttributes().getNamedItem("stylesheet");
        Transformer transformer;
        if (stylesheetAttribute != null) {
            String stylesheet = stylesheetAttribute.getNodeValue();
            if (stylesheet.trim().length() > 0) {
                transformer = getTransformer(stylesheet);
            } else {
                throw new IllegalArgumentException("stylesheet attributes may not be empty.");
            }
        } else {
            transformer = defaultXSLTransformer;
        }
        transformer.setParameter("pagePath", outputFilePath.toString());
        transformer.transform(xmlDoc, resultStream);
    }

    /**
     * Get a transformer from a path relative to src/xsl. The path gets '.xsl'
     * appended to it prior to processing.
     *
     * @param stylesheet
     * @return Transformer
     * @throws TransformerConfigurationException
     * @throws IOException
     */
    private Transformer getTransformer(String stylesheet) throws
            TransformerConfigurationException,
            IOException {
        if (stylesheet == null) {
            throw new IllegalArgumentException("stylsheet may not be null or empty.");
        }

        if (pageTransformers == null) {
            pageTransformers = new HashMap<>();
        }

        if (pageTransformers.containsKey(stylesheet)) {
            return pageTransformers.get(stylesheet);
        } else {
            FilePath stylesheetPath = StaticPages.xslDirPath.resolve(stylesheet.concat(".xsl"));
            File stylesheetFile = stylesheetPath.toFile();

            assertStylesheetExists(stylesheetFile);

            StreamSource stylesheetStream = new StreamSource(stylesheetFile);
            Transformer XSLTransformer = transformerFactory.newTransformer(stylesheetStream);
            pageTransformers.put(stylesheet, XSLTransformer);
            addDefaultParameters(XSLTransformer);
            return XSLTransformer;
        }
    }

    /**
     * Asserts that a file exists.
     *
     * @param stylesheet
     * @throws IOException
     */
    private void assertStylesheetExists(File stylesheet) throws IOException {
        if (!Assertions.fileExists(stylesheet)) {
            if (stylesheet == null) {
                throw new IOException("stylesheet was null.");
            }
            throw new IOException("This stylesheet doesn't exist:\n   " + stylesheet.getAbsolutePath());
        }
    }

    /**
     * Adds default parameters to a Transformer.
     *
     * @param xslt
     */
    private void addDefaultParameters(Transformer xslt) {
        xslt.setParameter("assetPrefixInBrowser", StaticPages.assetPrefixInBrowser);
        xslt.setParameter("enableDevMode", StaticPages.enableDevMode);
    }
}
