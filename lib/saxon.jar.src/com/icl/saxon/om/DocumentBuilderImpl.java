// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DocumentBuilderImpl.java

package com.icl.saxon.om;

import com.icl.saxon.output.Emitter;
import com.icl.saxon.tinytree.TinyBuilder;
import com.icl.saxon.tinytree.TinyDocumentImpl;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.*;

// Referenced classes of package com.icl.saxon.om:
//            NamePool, Builder, AbstractNode

public class DocumentBuilderImpl extends DocumentBuilder
{

            public DocumentBuilderImpl()
            {
            }

            public boolean isNamespaceAware()
            {
/*  18*/        return true;
            }

            public boolean isValidating()
            {
/*  22*/        return false;
            }

            public Document newDocument()
            {
/*  28*/        return new TinyDocumentImpl();
            }

            public Document parse(InputSource inputsource)
                throws SAXException
            {
/*  33*/        try
                {
/*  33*/            TinyBuilder tinybuilder = new TinyBuilder();
/*  34*/            tinybuilder.setNamePool(NamePool.getDefaultNamePool());
/*  35*/            return (Document)tinybuilder.build(new SAXSource(inputsource));
                }
/*  37*/        catch(TransformerException transformerexception)
                {
/*  37*/            throw new SAXException(transformerexception);
                }
            }

            public void setEntityResolver(EntityResolver entityresolver)
            {
            }

            public void setErrorHandler(ErrorHandler errorhandler)
            {
            }

            public DOMImplementation getDOMImplementation()
            {
/*  50*/        return (new TinyDocumentImpl()).getImplementation();
            }
}
