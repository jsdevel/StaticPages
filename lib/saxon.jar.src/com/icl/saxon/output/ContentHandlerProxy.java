// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ContentHandlerProxy.java

package com.icl.saxon.output;

import com.icl.saxon.om.NamePool;
import java.util.Properties;
import javax.xml.transform.TransformerException;
import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;

// Referenced classes of package com.icl.saxon.output:
//            Emitter

public class ContentHandlerProxy extends Emitter
    implements Locator
{

            protected ContentHandler handler;
            protected LexicalHandler lexicalHandler;
            protected Locator locator;
            private int depth;
            protected boolean requireWellFormed;

            public ContentHandlerProxy()
            {
/*  28*/        locator = this;
/*  29*/        depth = 0;
/*  30*/        requireWellFormed = true;
            }

            public void setUnderlyingContentHandler(ContentHandler contenthandler)
            {
/*  37*/        handler = contenthandler;
/*  38*/        if(contenthandler instanceof LexicalHandler)
/*  39*/            lexicalHandler = (LexicalHandler)contenthandler;
            }

            public void setOutputProperties(Properties properties)
            {
/*  48*/        super.setOutputProperties(properties);
/*  49*/        if("no".equals(properties.getProperty("{http://saxon.sf.net/}require-well-formed")))
/*  50*/            requireWellFormed = false;
            }

            public void setLexicalHandler(LexicalHandler lexicalhandler)
            {
/*  60*/        lexicalHandler = lexicalhandler;
            }

            public void setRequireWellFormed(boolean flag)
            {
/*  69*/        requireWellFormed = flag;
            }

            public void setDocumentLocator(Locator locator1)
            {
/*  77*/        locator = locator1;
            }

            public void startDocument()
                throws TransformerException
            {
/*  86*/        if(handler == null)
/*  87*/            throw new TransformerException("ContentHandlerProxy.startDocument(): no underlying handler provided");
/*  90*/        try
                {
/*  90*/            handler.setDocumentLocator(locator);
/*  91*/            handler.startDocument();
                }
/*  93*/        catch(SAXException saxexception)
                {
/*  93*/            throw new TransformerException(saxexception);
                }
/*  95*/        depth = 0;
            }

            public void endDocument()
                throws TransformerException
            {
/* 104*/        try
                {
/* 104*/            handler.endDocument();
                }
/* 106*/        catch(SAXException saxexception)
                {
/* 106*/            throw new TransformerException(saxexception);
                }
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/* 117*/        depth++;
/* 119*/        try
                {
/* 119*/            if(depth <= 0 && requireWellFormed)
/* 120*/                notifyNotWellFormed();
/* 122*/            if(depth > 0 || !requireWellFormed)
                    {
/* 123*/                for(int k = 0; k < j; k++)
                        {
/* 124*/                    String s = super.namePool.getPrefixFromNamespaceCode(ai[k]);
/* 125*/                    String s1 = super.namePool.getURIFromNamespaceCode(ai[k]);
/* 126*/                    handler.startPrefixMapping(s, s1);
                        }

/* 129*/                handler.startElement(super.namePool.getURI(i), super.namePool.getLocalName(i), super.namePool.getDisplayName(i), attributes);
                    }
                }
/* 136*/        catch(SAXException saxexception)
                {
/* 136*/            throw new TransformerException(saxexception);
                }
            }

            public void endElement(int i)
                throws TransformerException
            {
/* 145*/        if(depth > 0)
/* 147*/            try
                    {
/* 147*/                handler.endElement(super.namePool.getURI(i), super.namePool.getLocalName(i), super.namePool.getDisplayName(i));
                    }
/* 152*/            catch(SAXException saxexception)
                    {
/* 152*/                throw new TransformerException(saxexception);
                    }
/* 155*/        depth--;
/* 158*/        if(requireWellFormed && depth <= 0)
/* 159*/            depth = 0x80000000;
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/* 170*/        try
                {
/* 170*/            if(depth <= 0 && requireWellFormed)
                    {
/* 171*/                boolean flag = (new String(ac, i, j)).trim().length() == 0;
/* 172*/                if(!flag)
                        {
/* 175*/                    notifyNotWellFormed();
/* 176*/                    if(!requireWellFormed)
/* 177*/                        handler.characters(ac, i, j);
                        }
                    } else
                    {
/* 181*/                handler.characters(ac, i, j);
                    }
                }
/* 184*/        catch(SAXException saxexception)
                {
/* 184*/            throw new TransformerException(saxexception);
                }
            }

            protected void notifyNotWellFormed()
                throws SAXException
            {
/* 198*/        try
                {
/* 198*/            handler.processingInstruction("saxon:warning", "Output suppressed because it is not well-formed");
                }
/* 201*/        catch(SAXException saxexception)
                {
/* 201*/            if(saxexception.getMessage().equals("continue"))
/* 202*/                requireWellFormed = false;
/* 204*/            else
/* 204*/                throw saxexception;
                }
            }

            public void processingInstruction(String s, String s1)
                throws TransformerException
            {
/* 217*/        try
                {
/* 217*/            handler.processingInstruction(s, s1);
                }
/* 219*/        catch(SAXException saxexception)
                {
/* 219*/            throw new TransformerException(saxexception);
                }
            }

            public void comment(char ac[], int i, int j)
                throws TransformerException
            {
/* 231*/        try
                {
/* 231*/            if(lexicalHandler != null)
/* 232*/                lexicalHandler.comment(ac, i, j);
                }
/* 235*/        catch(SAXException saxexception)
                {
/* 235*/            throw new TransformerException(saxexception);
                }
            }

            public void setEscaping(boolean flag)
            {
/* 249*/        try
                {
/* 249*/            handler.processingInstruction(flag ? "javax.xml.transform.enable-output-escaping" : "javax.xml.transform.disable-output-escaping", "");
                }
/* 252*/        catch(SAXException saxexception) { }
            }

            public String getPublicId()
            {
/* 260*/        return null;
            }

            public int getLineNumber()
            {
/* 264*/        return -1;
            }

            public int getColumnNumber()
            {
/* 268*/        return -1;
            }
}
