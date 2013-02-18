// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SAXParserImpl.java

package com.icl.saxon.aelfred;

import javax.xml.parsers.SAXParser;
import org.xml.sax.*;

// Referenced classes of package com.icl.saxon.aelfred:
//            SAXDriver

public class SAXParserImpl extends SAXParser
{

            private SAXDriver parser;

            public SAXParserImpl()
            {
/*  11*/        parser = new SAXDriver();
            }

            public Parser getParser()
                throws SAXException
            {
/*  15*/        throw new SAXException("The AElfred parser is a SAX2 XMLReader");
            }

            public Object getProperty(String s)
                throws SAXNotRecognizedException, SAXNotSupportedException
            {
/*  20*/        return parser.getProperty(s);
            }

            public XMLReader getXMLReader()
            {
/*  24*/        return parser;
            }

            public boolean isNamespaceAware()
            {
/*  28*/        return true;
            }

            public boolean isValidating()
            {
/*  32*/        return false;
            }

            public void setProperty(String s, Object obj)
                throws SAXNotRecognizedException, SAXNotSupportedException
            {
/* 105*/        parser.setProperty(s, obj);
            }
}
