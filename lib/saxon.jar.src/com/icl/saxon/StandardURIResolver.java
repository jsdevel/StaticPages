// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StandardURIResolver.java

package com.icl.saxon;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLFilterImpl;

// Referenced classes of package com.icl.saxon:
//            IDFilter, TransformerFactoryImpl

public class StandardURIResolver
    implements URIResolver
{

            private TransformerFactoryImpl factory;

            protected StandardURIResolver()
            {
/*  25*/        this(null);
            }

            public StandardURIResolver(TransformerFactoryImpl transformerfactoryimpl)
            {
/*  22*/        factory = null;
/*  29*/        factory = transformerfactoryimpl;
            }

            public Source resolve(String s, String s1)
                throws TransformerException
            {
/*  44*/        String s2 = s;
/*  45*/        String s3 = null;
/*  46*/        int i = s.indexOf('#');
/*  47*/        if(i >= 0)
                {
/*  48*/            s2 = s.substring(0, i);
/*  49*/            s3 = s.substring(i + 1);
                }
                URL url;
/*  55*/        try
                {
/*  55*/            if(s1 == null)
                    {
/*  56*/                url = new URL(s2);
                    } else
                    {
/*  60*/                URL url1 = new URL(s1);
/*  61*/                url = s2.length() != 0 ? new URL(url1, s2) : url1;
                    }
                }
/*  70*/        catch(MalformedURLException malformedurlexception)
                {
/*  70*/            String s4 = tryToExpand(s1);
/*  71*/            if(!s4.equals(s1))
/*  72*/                return resolve(s, s4);
/*  75*/            else
/*  75*/                throw new TransformerException("Malformed URL [" + s2 + "] - base [" + s1 + "]", malformedurlexception);
                }
/*  79*/        SAXSource saxsource = new SAXSource();
/*  80*/        saxsource.setInputSource(new InputSource(url.toString()));
/*  82*/        if(s3 != null)
                {
/*  83*/            IDFilter idfilter = new IDFilter(s3);
                    org.xml.sax.XMLReader xmlreader;
/*  85*/            if(factory == null)
/*  87*/                try
                        {
/*  87*/                    xmlreader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
                        }
/*  89*/                catch(Exception exception)
                        {
/*  89*/                    throw new TransformerException(exception);
                        }
/*  92*/            else
/*  92*/                xmlreader = factory.getSourceParser();
/*  94*/            idfilter.setParent(xmlreader);
/*  95*/            saxsource.setXMLReader(idfilter);
                }
/*  97*/        return saxsource;
            }

            private String tryToExpand(String s)
            {
/* 108*/        if(s == null)
/* 109*/            s = "";
                String s2;
/* 112*/        try
                {
/* 112*/            URL url = new URL(s);
/* 113*/            return s;
                }
/* 115*/        catch(MalformedURLException malformedurlexception)
                {
/* 115*/            String s1 = System.getProperty("user.dir");
/* 116*/            if(s1.startsWith("/"))
/* 117*/                s1 = "file://" + s1;
/* 119*/            else
/* 119*/                s1 = "file:///" + s1;
/* 121*/            if(!s1.endsWith("/") && !s.startsWith("/"))
/* 122*/                s1 = s1 + "/";
/* 124*/            s2 = s1 + s;
                }
/* 126*/        try
                {
/* 126*/            URL url1 = new URL(s2);
/* 128*/            return s2;
                }
/* 131*/        catch(MalformedURLException malformedurlexception1)
                {
/* 131*/            return s;
                }
            }
}
