// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ContentEmitter.java

package com.icl.saxon;

import com.icl.saxon.om.Name;
import com.icl.saxon.om.NamePool;
import com.icl.saxon.output.Emitter;
import java.net.URL;
import javax.xml.transform.TransformerException;
import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;

public class ContentEmitter
    implements ContentHandler, LexicalHandler, DTDHandler
{

            private NamePool pool;
            private Emitter emitter;
            private boolean inDTD;
            private Locator locator;
            private char buffer[];
            private int used;
            private int namespaces[];
            private int namespacesUsed;

            public ContentEmitter()
            {
/*  25*/        inDTD = false;
/*  30*/        buffer = new char[4096];
/*  31*/        used = 0;
/*  35*/        namespaces = new int[50];
/*  36*/        namespacesUsed = 0;
            }

            public void setEmitter(Emitter emitter1)
            {
/*  46*/        emitter = emitter1;
            }

            public void setNamePool(NamePool namepool)
            {
/*  50*/        pool = namepool;
            }

            public void startDocument()
                throws SAXException
            {
/*  60*/        try
                {
/*  60*/            used = 0;
/*  61*/            namespacesUsed = 0;
/*  62*/            emitter.setDocumentLocator(locator);
/*  63*/            emitter.startDocument();
                }
/*  65*/        catch(TransformerException transformerexception)
                {
/*  65*/            throw new SAXException(transformerexception);
                }
            }

            public void endDocument()
                throws SAXException
            {
/*  75*/        try
                {
/*  75*/            flush();
/*  76*/            emitter.endDocument();
                }
/*  78*/        catch(TransformerException transformerexception)
                {
/*  78*/            throw new SAXException(transformerexception);
                }
            }

            public void setDocumentLocator(Locator locator1)
            {
/*  87*/        locator = locator1;
            }

            public void startPrefixMapping(String s, String s1)
            {
/*  96*/        if(namespacesUsed >= namespaces.length)
                {
/*  97*/            int ai[] = new int[namespacesUsed * 2];
/*  98*/            System.arraycopy(namespaces, 0, ai, 0, namespacesUsed);
/*  99*/            namespaces = ai;
                }
/* 101*/        namespaces[namespacesUsed++] = pool.allocateNamespaceCode(s, s1);
            }

            public void endPrefixMapping(String s)
            {
            }

            public void startElement(String s, String s1, String s2, Attributes attributes)
                throws SAXException
            {
/* 119*/        try
                {
/* 119*/            flush();
/* 120*/            int i = getNameCode(s, s1, s2);
/* 121*/            emitter.startElement(i, attributes, namespaces, namespacesUsed);
/* 122*/            namespacesUsed = 0;
                }
/* 124*/        catch(TransformerException transformerexception)
                {
/* 124*/            throw new SAXException(transformerexception);
                }
            }

            private int getNameCode(String s, String s1, String s2)
            {
/* 129*/        String s3 = Name.getPrefix(s2);
/* 130*/        return pool.allocate(s3, s, s1);
            }

            public void endElement(String s, String s1, String s2)
                throws SAXException
            {
/* 139*/        try
                {
/* 139*/            flush();
/* 141*/            String s3 = Name.getPrefix(s2);
/* 142*/            int i = pool.allocate(s3, s, s1);
/* 143*/            emitter.endElement(i);
                }
/* 145*/        catch(TransformerException transformerexception)
                {
/* 145*/            throw new SAXException(transformerexception);
                }
            }

            public void characters(char ac[], int i, int j)
            {
                char ac1[];
/* 158*/        for(; used + j > buffer.length; buffer = ac1)
                {
/* 158*/            ac1 = new char[buffer.length * 2];
/* 159*/            System.arraycopy(buffer, 0, ac1, 0, used);
                }

/* 162*/        System.arraycopy(ac, i, buffer, used, j);
/* 163*/        used += j;
            }

            public void ignorableWhitespace(char ac[], int i, int j)
            {
/* 171*/        characters(ac, i, j);
            }

            public void processingInstruction(String s, String s1)
                throws SAXException
            {
/* 181*/        try
                {
/* 181*/            flush();
/* 182*/            if(!inDTD)
/* 183*/                if(s == null)
                        {
/* 185*/                    comment(s1.toCharArray(), 0, s1.length());
                        } else
                        {
/* 188*/                    if(!Name.isNCName(s))
/* 189*/                        throw new SAXException("Invalid processing instruction name (" + s + ")");
/* 191*/                    emitter.processingInstruction(s, s1);
                        }
                }
/* 195*/        catch(TransformerException transformerexception)
                {
/* 195*/            throw new SAXException(transformerexception);
                }
            }

            public void comment(char ac[], int i, int j)
                throws SAXException
            {
/* 205*/        try
                {
/* 205*/            flush();
/* 206*/            if(!inDTD)
/* 207*/                emitter.comment(ac, i, j);
                }
/* 210*/        catch(TransformerException transformerexception)
                {
/* 210*/            throw new SAXException(transformerexception);
                }
            }

            private void flush()
                throws TransformerException
            {
/* 219*/        if(used > 0)
                {
/* 220*/            emitter.characters(buffer, 0, used);
/* 221*/            used = 0;
                }
            }

            public void skippedEntity(String s)
            {
            }

            public void startDTD(String s, String s1, String s2)
            {
/* 235*/        inDTD = true;
            }

            public void endDTD()
            {
/* 244*/        inDTD = false;
            }

            public void startEntity(String s)
            {
            }

            public void endEntity(String s)
            {
            }

            public void startCDATA()
            {
            }

            public void endCDATA()
            {
            }

            public void notationDecl(String s, String s1, String s2)
            {
            }

            public void unparsedEntityDecl(String s, String s1, String s2, String s3)
                throws SAXException
            {
/* 276*/        String s4 = s2;
/* 277*/        if(locator != null)
/* 279*/            try
                    {
/* 279*/                String s5 = locator.getSystemId();
/* 280*/                URL url = new URL(new URL(s5), s2);
/* 281*/                s4 = url.toString();
                    }
/* 282*/            catch(Exception exception) { }
/* 285*/        try
                {
/* 285*/            emitter.setUnparsedEntity(s, s4);
                }
/* 287*/        catch(TransformerException transformerexception)
                {
/* 287*/            throw new SAXException(transformerexception);
                }
            }
}
