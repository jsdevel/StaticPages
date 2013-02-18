// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StringEmitter.java

package com.icl.saxon.output;

import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

// Referenced classes of package com.icl.saxon.output:
//            Emitter

final class StringEmitter extends Emitter
{

            private int ignoreElements;
            private StringBuffer buffer;

            protected StringEmitter(StringBuffer stringbuffer)
            {
/*  20*/        ignoreElements = 0;
/*  24*/        buffer = stringbuffer;
            }

            public void startDocument()
                throws TransformerException
            {
            }

            public void endDocument()
                throws TransformerException
            {
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/*  49*/        if(ignoreElements == 0)
/*  50*/            buffer.append(ac, i, j);
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/*  62*/        ignoreElements++;
            }

            public void endElement(int i)
                throws TransformerException
            {
/*  73*/        ignoreElements--;
            }

            public void processingInstruction(String s, String s1)
                throws TransformerException
            {
            }

            public void comment(char ac[], int i, int j)
                throws TransformerException
            {
            }
}
