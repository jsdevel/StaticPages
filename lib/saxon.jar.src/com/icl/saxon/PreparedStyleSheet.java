// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   PreparedStyleSheet.java

package com.icl.saxon;

import com.icl.saxon.om.Builder;
import com.icl.saxon.om.DocumentInfo;
import com.icl.saxon.om.NamePool;
import com.icl.saxon.output.Emitter;
import com.icl.saxon.style.LiteralResultElement;
import com.icl.saxon.style.StyleElement;
import com.icl.saxon.style.StyleNodeFactory;
import com.icl.saxon.style.XSLStyleSheet;
import com.icl.saxon.tree.DocumentImpl;
import com.icl.saxon.tree.TreeBuilder;
import java.util.Hashtable;
import java.util.Properties;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;

// Referenced classes of package com.icl.saxon:
//            Controller, StylesheetStripper, TransformerFactoryImpl

public class PreparedStyleSheet
    implements Templates
{

            private DocumentImpl styleDoc;
            private TransformerFactoryImpl factory;
            private NamePool namePool;
            private StyleNodeFactory nodeFactory;
            private int errorCount;

            protected PreparedStyleSheet(TransformerFactoryImpl transformerfactoryimpl)
            {
/*  30*/        errorCount = 0;
/*  37*/        factory = transformerfactoryimpl;
            }

            public Transformer newTransformer()
            {
/*  45*/        Controller controller = new Controller(factory);
/*  46*/        controller.setPreparedStyleSheet(this);
/*  47*/        return controller;
            }

            public TransformerFactoryImpl getTransformerFactory()
            {
/*  55*/        return factory;
            }

            public void setNamePool(NamePool namepool)
            {
/*  63*/        namePool = namepool;
            }

            public NamePool getNamePool()
            {
/*  71*/        return namePool;
            }

            public StyleNodeFactory getStyleNodeFactory()
            {
/*  79*/        return nodeFactory;
            }

            protected void prepare(SAXSource saxsource)
                throws TransformerConfigurationException
            {
/*  88*/        if(namePool == null || namePool.isSealed())
/*  89*/            namePool = NamePool.getDefaultNamePool();
/*  92*/        nodeFactory = new StyleNodeFactory(namePool);
/*  95*/        StylesheetStripper stylesheetstripper = new StylesheetStripper();
/*  96*/        stylesheetstripper.setStylesheetRules(namePool);
/*  98*/        TreeBuilder treebuilder = new TreeBuilder();
/*  99*/        treebuilder.setNamePool(namePool);
/* 100*/        treebuilder.setErrorListener(factory.getErrorListener());
/* 101*/        treebuilder.setStripper(stylesheetstripper);
/* 102*/        treebuilder.setSystemId(saxsource.getSystemId());
/* 103*/        treebuilder.setNodeFactory(nodeFactory);
/* 104*/        treebuilder.setDiscardCommentsAndPIs(true);
/* 105*/        treebuilder.setLineNumbering(true);
                DocumentImpl documentimpl;
/* 111*/        try
                {
/* 111*/            documentimpl = (DocumentImpl)treebuilder.build(saxsource);
                }
/* 113*/        catch(TransformerException transformerexception)
                {
/* 113*/            Throwable throwable = transformerexception.getException();
/* 114*/            if(throwable != null)
                    {
/* 115*/                if(throwable instanceof SAXParseException)
/* 117*/                    throw new TransformerConfigurationException("Failed to parse stylesheet");
/* 118*/                if(throwable instanceof TransformerConfigurationException)
/* 119*/                    throw (TransformerConfigurationException)throwable;
/* 121*/                else
/* 121*/                    throw new TransformerConfigurationException(throwable);
                    } else
                    {
/* 124*/                throw new TransformerConfigurationException(transformerexception);
                    }
                }
/* 127*/        if(documentimpl.getDocumentElement() == null)
/* 128*/            throw new TransformerConfigurationException("Stylesheet is empty or absent");
/* 131*/        setStyleSheetDocument(documentimpl);
/* 133*/        if(errorCount > 0)
/* 134*/            throw new TransformerConfigurationException("Failed to compile stylesheet. " + errorCount + (errorCount != 1 ? " errors " : " error ") + "detected.");
/* 141*/        else
/* 141*/            return;
            }

            protected void setStyleSheetDocument(DocumentImpl documentimpl)
                throws TransformerConfigurationException
            {
/* 152*/        styleDoc = documentimpl;
/* 153*/        namePool = documentimpl.getNamePool();
/* 155*/        nodeFactory = new StyleNodeFactory(namePool);
/* 159*/        StyleElement styleelement = (StyleElement)styleDoc.getDocumentElement();
/* 160*/        if(styleelement instanceof LiteralResultElement)
/* 161*/            styleDoc = ((LiteralResultElement)styleelement).makeStyleSheet(this);
/* 164*/        if(!(styleDoc.getDocumentElement() instanceof XSLStyleSheet))
                {
/* 165*/            throw new TransformerConfigurationException("Top-level element of stylesheet is not xsl:stylesheet or xsl:transform or literal result element");
                } else
                {
/* 169*/            XSLStyleSheet xslstylesheet = (XSLStyleSheet)styleDoc.getDocumentElement();
/* 173*/            xslstylesheet.setPreparedStyleSheet(this);
/* 174*/            xslstylesheet.preprocess();
/* 175*/            return;
                }
            }

            public DocumentImpl getStyleSheetDocument()
            {
/* 182*/        return styleDoc;
            }

            public Properties getOutputProperties()
            {
/* 198*/        Properties properties = new Properties();
/* 201*/        properties.put("encoding", "utf-8");
/* 203*/        properties.put("omit-xml-declaration", "no");
/* 207*/        properties.put("cdata-section-elements", "");
/* 211*/        Properties properties1 = new Properties(properties);
/* 212*/        ((XSLStyleSheet)styleDoc.getDocumentElement()).gatherOutputProperties(properties1);
/* 213*/        return properties1;
            }

            public void reportError(TransformerException transformerexception)
                throws TransformerException
            {
/* 222*/        errorCount++;
/* 223*/        factory.getErrorListener().error(transformerexception);
            }

            public DocumentInfo stripWhitespace(Document document)
                throws TransformerException
            {
/* 232*/        XSLStyleSheet xslstylesheet = (XSLStyleSheet)styleDoc.getDocumentElement();
/* 233*/        if(xslstylesheet.stripsWhitespace() || !(document instanceof DocumentInfo))
                {
/* 234*/            Builder builder = ((Controller)newTransformer()).makeBuilder();
/* 235*/            builder.setNamePool(namePool);
/* 236*/            return builder.build(factory.getSAXSource(new DOMSource(document), false));
                } else
                {
/* 238*/            return (DocumentInfo)document;
                }
            }
}
