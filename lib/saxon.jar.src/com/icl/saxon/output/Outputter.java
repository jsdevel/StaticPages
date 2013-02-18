// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Outputter.java

package com.icl.saxon.output;

import java.util.Properties;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.output:
//            Emitter

public abstract class Outputter
{

            protected Emitter emitter;

            public Outputter()
            {
            }

            public Emitter getEmitter()
                throws TransformerException
            {
/*  31*/        reset();
/*  32*/        return emitter;
            }

            public abstract void reset()
                throws TransformerException;

            public abstract Properties getOutputProperties();

            public final void setEscaping(boolean flag)
                throws TransformerException
            {
/*  49*/        emitter.setEscaping(flag);
            }

            public final void open()
                throws TransformerException
            {
/*  58*/        emitter.startDocument();
            }

            public abstract void write(String s)
                throws TransformerException;

            public abstract void writeContent(String s)
                throws TransformerException;

            public abstract void writeContent(char ac[], int i, int j)
                throws TransformerException;

            public abstract void writeStartTag(int i)
                throws TransformerException;

            public abstract int checkAttributePrefix(int i)
                throws TransformerException;

            public abstract void writeNamespaceDeclaration(int i)
                throws TransformerException;

            public abstract void copyNamespaceNode(int i)
                throws TransformerException;

            public abstract boolean thereIsAnOpenStartTag();

            public void writeAttribute(int i, String s)
                throws TransformerException
            {
/* 152*/        writeAttribute(i, s, false);
            }

            public abstract void writeAttribute(int i, String s, boolean flag)
                throws TransformerException;

            public abstract void writeEndTag(int i)
                throws TransformerException;

            public abstract void writeComment(String s)
                throws TransformerException;

            public abstract void writePI(String s, String s1)
                throws TransformerException;

            public abstract void close()
                throws TransformerException;
}
