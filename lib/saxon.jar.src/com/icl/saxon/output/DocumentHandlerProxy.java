// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DocumentHandlerProxy.java

package com.icl.saxon.output;

import com.icl.saxon.om.NamePool;
import javax.xml.transform.TransformerException;
import org.xml.sax.*;
import org.xml.sax.helpers.AttributeListImpl;

// Referenced classes of package com.icl.saxon.output:
//            Emitter

public class DocumentHandlerProxy extends Emitter
{

            protected DocumentHandler handler;
            protected AttributeListImpl outputAtts;
            private int depth;

            public DocumentHandlerProxy()
            {
/*  23*/        outputAtts = new AttributeListImpl();
/*  24*/        depth = 0;
            }

            public void setUnderlyingDocumentHandler(DocumentHandler documenthandler)
            {
/*  31*/        handler = documenthandler;
            }

            public void setDocumentLocator(Locator locator)
            {
/*  39*/        if(handler != null)
/*  40*/            handler.setDocumentLocator(locator);
            }

            public void startDocument()
                throws TransformerException
            {
/*  48*/        if(handler == null)
/*  49*/            throw new TransformerException("DocumentHandlerProxy.startDocument(): no underlying handler provided");
/*  52*/        try
                {
/*  52*/            handler.startDocument();
                }
/*  54*/        catch(SAXException saxexception)
                {
/*  54*/            throw new TransformerException(saxexception);
                }
/*  57*/        depth = 0;
            }

            public void endDocument()
                throws TransformerException
            {
/*  66*/        try
                {
/*  66*/            handler.endDocument();
                }
/*  68*/        catch(SAXException saxexception)
                {
/*  68*/            throw new TransformerException(saxexception);
                }
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/*  78*/        depth++;
/*  79*/        outputAtts.clear();
/*  80*/        for(int k = 0; k < attributes.getLength(); k++)
/*  81*/            outputAtts.addAttribute(attributes.getQName(k), attributes.getType(k), attributes.getValue(k));

/*  86*/        if(depth > 0)
                {
/*  88*/            for(int l = 0; l < j; l++)
                    {
/*  89*/                String s = super.namePool.getPrefixFromNamespaceCode(ai[l]);
/*  90*/                String s1 = super.namePool.getURIFromNamespaceCode(ai[l]);
/*  91*/                if(s.equals(""))
/*  92*/                    outputAtts.addAttribute("xmlns", "NMTOKEN", s1);
/*  94*/                else
/*  94*/                    outputAtts.addAttribute("xmlns:" + s, "NMTOKEN", s1);
                    }

/*  98*/            try
                    {
/*  98*/                handler.startElement(super.namePool.getDisplayName(i), outputAtts);
                    }
/* 100*/            catch(SAXException saxexception)
                    {
/* 100*/                throw new TransformerException(saxexception);
                    }
                }
            }

            public void endElement(int i)
                throws TransformerException
            {
/* 110*/        if(depth > 0)
/* 112*/            try
                    {
/* 112*/                handler.endElement(super.namePool.getDisplayName(i));
                    }
/* 114*/            catch(SAXException saxexception)
                    {
/* 114*/                throw new TransformerException(saxexception);
                    }
/* 117*/        depth--;
/* 119*/        if(depth <= 0)
/* 120*/            depth = 0x80000000;
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/* 129*/        if(depth > 0)
/* 131*/            try
                    {
/* 131*/                handler.characters(ac, i, j);
                    }
/* 133*/            catch(SAXException saxexception)
                    {
/* 133*/                throw new TransformerException(saxexception);
                    }
            }

            public void processingInstruction(String s, String s1)
                throws TransformerException
            {
/* 154*/        try
                {
/* 154*/            handler.processingInstruction(s, s1);
                }
/* 156*/        catch(SAXException saxexception)
                {
/* 156*/            throw new TransformerException(saxexception);
                }
            }

            public void comment(char ac[], int i, int j)
            {
            }
}
