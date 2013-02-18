// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StringOutputter.java

package com.icl.saxon.output;

import java.util.Properties;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.output:
//            Outputter, StringEmitter, TextFragment

public final class StringOutputter extends Outputter
{

            StringBuffer buffer;
            int ignoreElements;
            ErrorListener errorListener;

            public StringOutputter(StringBuffer stringbuffer)
            {
/*  18*/        ignoreElements = 0;
/*  19*/        errorListener = null;
/*  22*/        buffer = stringbuffer;
/*  24*/        super.emitter = new StringEmitter(stringbuffer);
            }

            public void setErrorListener(ErrorListener errorlistener)
            {
/*  28*/        errorListener = errorlistener;
            }

            public void reset()
                throws TransformerException
            {
            }

            public Properties getOutputProperties()
            {
/*  37*/        return TextFragment.getProperties();
            }

            public void write(String s)
                throws TransformerException
            {
/*  48*/        if(ignoreElements == 0)
/*  49*/            buffer.append(s);
            }

            public void writeContent(String s)
                throws TransformerException
            {
/*  62*/        if(s == null)
/*  62*/            return;
/*  63*/        if(ignoreElements == 0)
/*  64*/            buffer.append(s);
            }

            public void writeContent(char ac[], int i, int j)
                throws TransformerException
            {
/*  79*/        if(ignoreElements == 0)
/*  80*/            buffer.append(ac, i, j);
            }

            public void writeStartTag(int i)
                throws TransformerException
            {
/*  90*/        reportRecoverableError();
/*  91*/        ignoreElements++;
            }

            private void reportRecoverableError()
                throws TransformerException
            {
/*  95*/        if(errorListener != null)
/*  96*/            errorListener.warning(new TransformerException("Non-text output nodes are ignored when writing an attribute, comment, or PI"));
            }

            public int checkAttributePrefix(int i)
                throws TransformerException
            {
/* 111*/        return i;
            }

            public void writeNamespaceDeclaration(int i)
                throws TransformerException
            {
            }

            public void copyNamespaceNode(int i)
                throws TransformerException
            {
            }

            public boolean thereIsAnOpenStartTag()
            {
/* 145*/        return false;
            }

            public void writeAttribute(int i, String s, boolean flag)
                throws TransformerException
            {
/* 160*/        reportRecoverableError();
            }

            public void writeEndTag(int i)
                throws TransformerException
            {
/* 170*/        ignoreElements--;
            }

            public void writeComment(String s)
                throws TransformerException
            {
/* 179*/        reportRecoverableError();
            }

            public void writePI(String s, String s1)
                throws TransformerException
            {
/* 188*/        reportRecoverableError();
            }

            public void close()
                throws TransformerException
            {
            }
}
