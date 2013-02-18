// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   PIGrabber.java

package com.icl.saxon;

import com.icl.saxon.om.ProcInstParser;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

// Referenced classes of package com.icl.saxon:
//            StandardURIResolver

public class PIGrabber extends DefaultHandler
{

            private String reqMedia;
            private String reqTitle;
            private String baseURI;
            private URIResolver uriResolver;
            private Vector stylesheets;

            public PIGrabber()
            {
/*  19*/        reqMedia = null;
/*  20*/        reqTitle = null;
/*  21*/        baseURI = null;
/*  22*/        uriResolver = null;
/*  23*/        stylesheets = new Vector();
            }

            public void setCriteria(String s, String s1, String s2)
            {
/*  26*/        reqMedia = s;
/*  27*/        reqTitle = s1;
            }

            public void setBaseURI(String s)
            {
/*  35*/        baseURI = s;
            }

            public void setURIResolver(URIResolver uriresolver)
            {
/*  43*/        uriResolver = uriresolver;
            }

            public void startElement(String s, String s1, String s2, Attributes attributes)
                throws SAXException
            {
/*  54*/        throw new SAXException("#start#");
            }

            public void processingInstruction(String s, String s1)
                throws SAXException
            {
/*  64*/        if(s.equals("xml-stylesheet"))
                {
/*  66*/            String s2 = ProcInstParser.getPseudoAttribute(s1, "media");
/*  67*/            String s3 = ProcInstParser.getPseudoAttribute(s1, "title");
/*  68*/            String s4 = ProcInstParser.getPseudoAttribute(s1, "type");
/*  69*/            String s5 = ProcInstParser.getPseudoAttribute(s1, "alternate");
/*  71*/            if(s4 == null)
/*  71*/                return;
/*  75*/            if((s4.equals("text/xml") || s4.equals("application/xml") || s4.equals("text/xsl") || s4.equals("applicaton/xsl")) && (reqMedia == null || s2 == null || reqMedia.equals(s2)) && (s3 == null && (s5 == null || s5.equals("no")) || reqTitle == null || s3 != null && s3.equals(reqTitle)))
                    {
/*  84*/                String s6 = ProcInstParser.getPseudoAttribute(s1, "href");
/*  85*/                if(s6 == null)
/*  86*/                    throw new SAXException("xml-stylesheet PI has no href attribute");
/*  90*/                if(s3 == null && (s5 == null || s5.equals("no")))
/*  91*/                    stylesheets.insertElementAt(s6, 0);
/*  93*/                else
/*  93*/                    stylesheets.addElement(s6);
                    }
                }
            }

            public SAXSource[] getAssociatedStylesheets()
                throws TransformerException
            {
/* 108*/        if(stylesheets.size() == 0)
/* 109*/            return null;
/* 111*/        if(uriResolver == null)
/* 112*/            uriResolver = new StandardURIResolver();
/* 114*/        SAXSource asaxsource[] = new SAXSource[stylesheets.size()];
/* 115*/        for(int i = 0; i < stylesheets.size(); i++)
                {
/* 116*/            String s = (String)stylesheets.elementAt(i);
/* 117*/            javax.xml.transform.Source source = uriResolver.resolve(s, baseURI);
/* 118*/            if(!(source instanceof SAXSource))
/* 119*/                throw new TransformerException("Associated stylesheet URI must yield a SAX source");
/* 121*/            asaxsource[i] = (SAXSource)source;
                }

/* 123*/        return asaxsource;
            }

            public String[] getStylesheetURIs()
                throws SAXException
            {
/* 131*/        if(stylesheets.size() == 0)
/* 132*/            return null;
/* 134*/        String as[] = new String[stylesheets.size()];
/* 135*/        for(int i = 0; i < stylesheets.size(); i++)
/* 136*/            as[i] = (String)stylesheets.elementAt(i);

/* 138*/        return as;
            }
}
