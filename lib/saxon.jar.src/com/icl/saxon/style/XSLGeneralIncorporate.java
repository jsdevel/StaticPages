// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLGeneralIncorporate.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.om.*;
import com.icl.saxon.output.Emitter;
import com.icl.saxon.tree.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import org.w3c.dom.Node;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, XSLStyleSheet, StyleNodeFactory, LiteralResultElement, 
//            StandardNames

public abstract class XSLGeneralIncorporate extends StyleElement
{

            String href;
            DocumentImpl includedDoc;

            public XSLGeneralIncorporate()
            {
            }

            public abstract boolean isImport();

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  40*/        StandardNames standardnames = getStandardNames();
/*  41*/        AttributeCollection attributecollection = getAttributeList();
/*  43*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  44*/            int j = attributecollection.getNameCode(i);
/*  45*/            int k = j & 0xfffff;
/*  46*/            if(k == standardnames.HREF)
/*  47*/                href = attributecollection.getValue(i);
/*  49*/            else
/*  49*/                checkUnknownAttribute(j);
                }

/*  53*/        if(href == null)
/*  54*/            reportAbsence("href");
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  61*/        checkEmpty();
/*  62*/        checkTopLevel();
            }

            public XSLStyleSheet getIncludedStyleSheet(XSLStyleSheet xslstylesheet, int i)
                throws TransformerConfigurationException
            {
/*  68*/        if(href == null)
/*  70*/            return null;
/*  73*/        checkEmpty();
/*  74*/        checkTopLevel();
/*  77*/        try
                {
/*  77*/            XSLStyleSheet xslstylesheet1 = (XSLStyleSheet)getParentNode();
/*  78*/            DocumentInfo documentinfo = getDocumentRoot();
/*  79*/            TransformerFactoryImpl transformerfactoryimpl = getPreparedStyleSheet().getTransformerFactory();
/*  81*/            Object obj = transformerfactoryimpl.getURIResolver().resolve(href, getBaseURI());
/*  85*/            if(obj == null)
/*  86*/                obj = (new StandardURIResolver(transformerfactoryimpl)).resolve(href, getBaseURI());
/*  89*/            if(obj instanceof NodeInfo)
/*  90*/                if(obj instanceof Node)
/*  91*/                    obj = new DOMSource((Node)obj);
/*  93*/                else
/*  93*/                    throw new TransformerException("URIResolver must not return a " + obj.getClass());
/*  96*/            SAXSource saxsource = transformerfactoryimpl.getSAXSource(((javax.xml.transform.Source) (obj)), true);
/* 100*/            for(XSLStyleSheet xslstylesheet2 = xslstylesheet1; xslstylesheet2 != null; xslstylesheet2 = xslstylesheet2.getImporter())
/* 102*/                if(saxsource.getSystemId().equals(xslstylesheet2.getSystemId()))
                        {
/* 103*/                    compileError("A stylesheet cannot " + getLocalName() + " itself");
/* 104*/                    return null;
                        }

/* 111*/            com.icl.saxon.om.NamePool namepool = getDocumentRoot().getNamePool();
/* 112*/            StylesheetStripper stylesheetstripper = new StylesheetStripper();
/* 113*/            stylesheetstripper.setStylesheetRules(namepool);
/* 115*/            TreeBuilder treebuilder = new TreeBuilder();
/* 116*/            treebuilder.setNamePool(namepool);
/* 117*/            treebuilder.setStripper(stylesheetstripper);
/* 118*/            treebuilder.setNodeFactory(new StyleNodeFactory(namepool));
/* 119*/            treebuilder.setDiscardCommentsAndPIs(true);
/* 120*/            treebuilder.setLineNumbering(true);
/* 122*/            includedDoc = (DocumentImpl)treebuilder.build(saxsource);
/* 126*/            ElementImpl elementimpl = (ElementImpl)includedDoc.getDocumentElement();
/* 127*/            if(elementimpl instanceof LiteralResultElement)
                    {
/* 128*/                includedDoc = ((LiteralResultElement)elementimpl).makeStyleSheet(getPreparedStyleSheet());
/* 129*/                elementimpl = (ElementImpl)includedDoc.getDocumentElement();
                    }
/* 132*/            if(!(elementimpl instanceof XSLStyleSheet))
                    {
/* 133*/                compileError("Included document " + href + " is not a stylesheet");
/* 134*/                return null;
                    }
/* 136*/            XSLStyleSheet xslstylesheet3 = (XSLStyleSheet)elementimpl;
/* 138*/            if(((StyleElement) (xslstylesheet3)).validationError != null)
                    {
/* 139*/                int j = ((StyleElement) (xslstylesheet3)).reportingCircumstances;
/* 140*/                if(j == 1)
/* 141*/                    compileError(((StyleElement) (xslstylesheet3)).validationError);
/* 142*/                else
/* 142*/                if(j == 2 && !xslstylesheet3.forwardsCompatibleModeIsEnabled())
/* 144*/                    compileError(((StyleElement) (xslstylesheet3)).validationError);
                    }
/* 148*/            xslstylesheet3.setPrecedence(i);
/* 149*/            xslstylesheet3.setImporter(xslstylesheet);
/* 150*/            xslstylesheet3.spliceIncludes();
/* 152*/            return xslstylesheet3;
                }
/* 155*/        catch(TransformerException transformerexception)
                {
/* 155*/            compileError(transformerexception);
                }
/* 156*/        return null;
            }

            public void process(Context context)
            {
            }
}
