// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Emitter.java

package com.icl.saxon.output;

import com.icl.saxon.Loader;
import com.icl.saxon.om.NamePool;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import org.xml.sax.*;

// Referenced classes of package com.icl.saxon.output:
//            DocumentHandlerProxy, ContentHandlerProxy

public abstract class Emitter
    implements Result
{

            protected NamePool namePool;
            protected String systemId;
            protected Writer writer;
            protected OutputStream outputStream;
            protected Properties outputProperties;
            protected Locator locator;

            public Emitter()
            {
            }

            public void setNamePool(NamePool namepool)
            {
/*  39*/        namePool = namepool;
            }

            public NamePool getNamePool()
            {
/*  47*/        return namePool;
            }

            public void setSystemId(String s)
            {
/*  55*/        systemId = s;
            }

            public String getSystemId()
            {
/*  63*/        return systemId;
            }

            public void setOutputProperties(Properties properties)
            {
/*  71*/        outputProperties = properties;
            }

            public Properties getOutputProperties()
            {
/*  79*/        return outputProperties;
            }

            public boolean usesWriter()
            {
/*  88*/        return true;
            }

            public void setWriter(Writer writer1)
            {
/*  96*/        writer = writer1;
            }

            public Writer getWriter()
            {
/* 104*/        return writer;
            }

            public void setOutputStream(OutputStream outputstream)
            {
/* 112*/        outputStream = outputstream;
            }

            public OutputStream getOutputStream()
            {
/* 120*/        return outputStream;
            }

            public abstract void startDocument()
                throws TransformerException;

            public abstract void endDocument()
                throws TransformerException;

            public abstract void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException;

            public abstract void endElement(int i)
                throws TransformerException;

            public abstract void characters(char ac[], int i, int j)
                throws TransformerException;

            public abstract void processingInstruction(String s, String s1)
                throws TransformerException;

            public abstract void comment(char ac[], int i, int j)
                throws TransformerException;

            public void setEscaping(boolean flag)
                throws TransformerException
            {
            }

            public void setDocumentLocator(Locator locator1)
            {
/* 190*/        locator = locator1;
            }

            public void setUnparsedEntity(String s, String s1)
                throws TransformerException
            {
            }

            public static Emitter makeEmitter(String s)
                throws TransformerException
            {
/* 206*/        Object obj = Loader.getInstance(s);
/* 208*/        if(obj instanceof Emitter)
/* 209*/            return (Emitter)obj;
/* 210*/        if(obj instanceof DocumentHandler)
                {
/* 211*/            DocumentHandlerProxy documenthandlerproxy = new DocumentHandlerProxy();
/* 212*/            documenthandlerproxy.setUnderlyingDocumentHandler((DocumentHandler)obj);
/* 213*/            return documenthandlerproxy;
                }
/* 214*/        if(obj instanceof ContentHandler)
                {
/* 215*/            ContentHandlerProxy contenthandlerproxy = new ContentHandlerProxy();
/* 216*/            contenthandlerproxy.setUnderlyingContentHandler((ContentHandler)obj);
/* 217*/            return contenthandlerproxy;
                } else
                {
/* 219*/            throw new TransformerException("Failed to load emitter " + s + ": it is not a SAX DocumentHandler or SAX2 ContentHandler");
                }
            }
}
