// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ErrorEmitter.java

package com.icl.saxon.output;

import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

// Referenced classes of package com.icl.saxon.output:
//            Emitter

public class ErrorEmitter extends Emitter
{

            public ErrorEmitter()
            {
            }

            public void startDocument()
                throws TransformerException
            {
            }

            public void endDocument()
                throws TransformerException
            {
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/*  41*/        error();
            }

            public void endElement(int i)
                throws TransformerException
            {
/*  50*/        error();
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/*  60*/        error();
            }

            public void processingInstruction(String s, String s1)
                throws TransformerException
            {
/*  71*/        error();
            }

            public void comment(char ac[], int i, int j)
                throws TransformerException
            {
/*  80*/        error();
            }

            private void error()
                throws TransformerException
            {
/*  88*/        throw new TransformerException("Cannot write to result tree while executing a function");
            }
}
