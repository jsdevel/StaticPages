// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DocumentBuilderFactoryImpl.java

package com.icl.saxon.om;

import javax.xml.parsers.*;

// Referenced classes of package com.icl.saxon.om:
//            DocumentBuilderImpl

public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory
{

            public DocumentBuilderFactoryImpl()
            {
/*  17*/        setCoalescing(true);
/*  18*/        setExpandEntityReferences(true);
/*  19*/        setIgnoringComments(false);
/*  20*/        setIgnoringElementContentWhitespace(false);
/*  21*/        setNamespaceAware(true);
/*  22*/        setValidating(false);
            }

            public Object getAttribute(String s)
            {
/*  26*/        throw new IllegalArgumentException("Unrecognized attribute name: " + s);
            }

            public DocumentBuilder newDocumentBuilder()
                throws ParserConfigurationException
            {
/*  33*/        if(!isExpandEntityReferences())
/*  34*/            throw new ParserConfigurationException("Saxon parser always expands entity references");
/*  37*/        if(isIgnoringComments())
/*  38*/            throw new ParserConfigurationException("Saxon parser does not allow comments to be ignored");
/*  41*/        if(isIgnoringElementContentWhitespace())
/*  42*/            throw new ParserConfigurationException("Saxon parser does not allow whitespace in element content to be ignored");
/*  45*/        if(!isNamespaceAware())
/*  46*/            throw new ParserConfigurationException("Saxon parser is always namespace aware");
/*  49*/        if(isValidating())
/*  50*/            throw new ParserConfigurationException("Saxon parser is non-validating");
/*  54*/        else
/*  54*/            return new DocumentBuilderImpl();
            }

            public void setAttribute(String s, Object obj)
            {
/*  58*/        throw new IllegalArgumentException("Unrecognized attribute name: " + s);
            }

            public boolean getFeature(String s)
                throws ParserConfigurationException
            {
/*  81*/        throw new ParserConfigurationException("Unsupported feature: " + s);
            }

            public void setFeature(String s, boolean flag)
                throws ParserConfigurationException
            {
/* 122*/        if(s.equals("http://javax.xml.XMLConstants/feature/secure-processing") && !flag)
/* 123*/            return;
/* 125*/        else
/* 125*/            throw new ParserConfigurationException("Unsupported feature: " + s);
            }
}
