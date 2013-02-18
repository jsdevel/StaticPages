// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ProxyEmitter.java

package com.icl.saxon.output;

import com.icl.saxon.om.NamePool;
import java.io.Writer;
import java.util.Properties;
import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

// Referenced classes of package com.icl.saxon.output:
//            Emitter

public abstract class ProxyEmitter extends Emitter
{

            protected Emitter baseEmitter;
            protected Properties outputProperties;

            public ProxyEmitter()
            {
            }

            public void setUnderlyingEmitter(Emitter emitter)
            {
/*  25*/        baseEmitter = emitter;
/*  26*/        if(super.namePool != null)
/*  27*/            baseEmitter.setNamePool(super.namePool);
            }

            public void setNamePool(NamePool namepool)
            {
/*  36*/        super.setNamePool(namepool);
/*  37*/        if(baseEmitter != null)
/*  38*/            baseEmitter.setNamePool(namepool);
            }

            public void setWriter(Writer writer)
            {
/*  47*/        super.writer = writer;
/*  48*/        if(baseEmitter != null)
/*  49*/            baseEmitter.setWriter(writer);
            }

            public void startDocument()
                throws TransformerException
            {
/*  57*/        if(baseEmitter == null)
                {
/*  58*/            throw new TransformerException("ProxyEmitter.startDocument(): no underlying emitter provided");
                } else
                {
/*  60*/            baseEmitter.startDocument();
/*  61*/            return;
                }
            }

            public void endDocument()
                throws TransformerException
            {
/*  68*/        if(baseEmitter != null)
/*  69*/            baseEmitter.endDocument();
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/*  79*/        if(baseEmitter != null)
/*  80*/            baseEmitter.startElement(i, attributes, ai, j);
            }

            public void endElement(int i)
                throws TransformerException
            {
/*  89*/        if(baseEmitter != null)
/*  90*/            baseEmitter.endElement(i);
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/*  99*/        if(baseEmitter != null)
/* 100*/            baseEmitter.characters(ac, i, j);
            }

            public void processingInstruction(String s, String s1)
                throws TransformerException
            {
/* 110*/        if(baseEmitter != null)
/* 111*/            baseEmitter.processingInstruction(s, s1);
            }

            public void comment(char ac[], int i, int j)
                throws TransformerException
            {
/* 120*/        if(baseEmitter != null)
/* 121*/            baseEmitter.comment(ac, i, j);
            }

            public void setEscaping(boolean flag)
                throws TransformerException
            {
/* 133*/        if(baseEmitter != null)
/* 134*/            baseEmitter.setEscaping(flag);
            }

            public void setOutputProperties(Properties properties)
            {
/* 143*/        outputProperties = properties;
/* 144*/        if(baseEmitter != null)
/* 145*/            baseEmitter.setOutputProperties(properties);
            }

            public void setUnparsedEntity(String s, String s1)
                throws TransformerException
            {
/* 154*/        if(baseEmitter != null)
/* 155*/            baseEmitter.setUnparsedEntity(s, s1);
            }

            public void setDocumentLocator(Locator locator)
            {
/* 165*/        if(baseEmitter != null)
/* 166*/            baseEmitter.setDocumentLocator(locator);
            }
}
