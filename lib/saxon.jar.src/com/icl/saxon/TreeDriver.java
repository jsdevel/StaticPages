// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TreeDriver.java

package com.icl.saxon;

import com.icl.saxon.om.DocumentInfo;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.output.GeneralOutputter;
import com.icl.saxon.output.Outputter;
import java.util.Properties;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;
import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

// Referenced classes of package com.icl.saxon:
//            DOMDriver

public class TreeDriver extends DOMDriver
{

            private Outputter outputter;

            public TreeDriver()
            {
            }

            public void setDocument(Document document)
            {
/*  34*/        super.root = document;
/*  35*/        if(!(document instanceof DocumentInfo))
/*  36*/            throw new IllegalArgumentException("TreeDriver can only be used with a Saxon tree");
/*  38*/        else
/*  38*/            return;
            }

            public void parse()
                throws SAXException
            {
/*  56*/        if(super.root == null)
/*  57*/            throw new SAXException("TreeDriver: no start node defined");
/*  59*/        if(super.contentHandler == null)
/*  60*/            throw new SAXException("DOMDriver: no content handler defined");
/*  62*/        super.contentHandler.setDocumentLocator(this);
/*  63*/        DocumentInfo documentinfo = (DocumentInfo)super.root;
/*  65*/        try
                {
/*  65*/            GeneralOutputter generaloutputter = new GeneralOutputter(documentinfo.getNamePool());
/*  66*/            SAXResult saxresult = new SAXResult(super.contentHandler);
/*  67*/            saxresult.setSystemId(super.systemId);
/*  68*/            generaloutputter.setOutputDestination(new Properties(), saxresult);
/*  69*/            documentinfo.copy(generaloutputter);
/*  70*/            generaloutputter.close();
                }
/*  72*/        catch(TransformerException transformerexception)
                {
/*  72*/            throw new SAXException(transformerexception);
                }
            }
}
