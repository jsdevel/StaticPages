// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SAXParserFactoryImpl.java

package com.icl.saxon.aelfred;

import javax.xml.parsers.*;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

// Referenced classes of package com.icl.saxon.aelfred:
//            SAXDriver, SAXParserImpl

public class SAXParserFactoryImpl extends SAXParserFactory
{

            public SAXParserFactoryImpl()
            {
/*  16*/        setNamespaceAware(true);
/*  17*/        setValidating(false);
            }

            public boolean getFeature(String s)
                throws SAXNotRecognizedException, SAXNotSupportedException
            {
/*  22*/        return (new SAXDriver()).getFeature(s);
            }

            public void setFeature(String s, boolean flag)
                throws SAXNotRecognizedException, SAXNotSupportedException
            {
/*  28*/        (new SAXDriver()).setFeature(s, flag);
            }

            public SAXParser newSAXParser()
                throws ParserConfigurationException
            {
/*  33*/        if(isValidating())
/*  34*/            throw new ParserConfigurationException("AElfred parser is non-validating");
/*  36*/        if(!isNamespaceAware())
/*  37*/            throw new ParserConfigurationException("AElfred parser is namespace-aware");
/*  39*/        else
/*  39*/            return new SAXParserImpl();
            }
}
