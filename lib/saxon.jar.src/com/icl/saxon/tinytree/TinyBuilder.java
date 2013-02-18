// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TinyBuilder.java

package com.icl.saxon.tinytree;

import com.icl.saxon.*;
import com.icl.saxon.expr.SingletonNodeSet;
import com.icl.saxon.om.*;
import com.icl.saxon.output.Emitter;
import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyDocumentImpl

public class TinyBuilder extends Builder
{

            private int currentDepth;
            private int nodeNr;
            private int attributeNodeNr;
            private int namespaceNodeNr;
            private boolean ended;
            private int prevAtDepth[];

            public TinyBuilder()
            {
/*  23*/        currentDepth = 0;
/*  24*/        nodeNr = 0;
/*  25*/        attributeNodeNr = 0;
/*  26*/        namespaceNodeNr = 0;
/*  27*/        ended = false;
/*  29*/        prevAtDepth = new int[100];
            }

            public void createDocument()
            {
/*  32*/        super.currentDocument = new TinyDocumentImpl();
/*  33*/        if(super.locator == null)
/*  34*/            super.locator = this;
/*  36*/        TinyDocumentImpl tinydocumentimpl = (TinyDocumentImpl)super.currentDocument;
/*  37*/        tinydocumentimpl.setSystemId(super.locator.getSystemId());
/*  38*/        tinydocumentimpl.setNamePool(super.namePool);
            }

            public void startDocument()
                throws TransformerException
            {
/*  48*/        super.failed = false;
/*  49*/        if(super.started)
/*  51*/            return;
/*  53*/        super.started = true;
/*  55*/        if(super.currentDocument == null)
                {
/*  57*/            createDocument();
                } else
                {
/*  60*/            if(!(super.currentDocument instanceof TinyDocumentImpl))
/*  61*/                throw new TransformerException("Root node supplied is of wrong type");
/*  63*/            if(super.currentDocument.hasChildNodes())
/*  64*/                throw new TransformerException("Supplied document is not empty");
/*  66*/            super.currentDocument.setNamePool(super.namePool);
                }
/*  70*/        currentDepth = 0;
/*  71*/        nodeNr = 0;
/*  73*/        TinyDocumentImpl tinydocumentimpl = (TinyDocumentImpl)super.currentDocument;
/*  74*/        if(super.lineNumbering)
/*  75*/            tinydocumentimpl.setLineNumbering();
/*  78*/        tinydocumentimpl.addNode((short)9, 0, 0, 0, -1);
/*  79*/        prevAtDepth[0] = 0;
/*  80*/        tinydocumentimpl.next[0] = -1;
/*  82*/        currentDepth++;
/*  83*/        nodeNr++;
            }

            public void endDocument()
                throws TransformerException
            {
/*  95*/        if(ended)
/*  95*/            return;
/*  96*/        ended = true;
/*  98*/        TinyDocumentImpl tinydocumentimpl = (TinyDocumentImpl)super.currentDocument;
/*  99*/        int i = prevAtDepth[currentDepth];
/* 100*/        if(i > 0)
/* 101*/            tinydocumentimpl.next[i] = -1;
/* 103*/        prevAtDepth[currentDepth] = -1;
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/* 120*/        TinyDocumentImpl tinydocumentimpl = (TinyDocumentImpl)super.currentDocument;
/* 124*/        int k = j != 0 ? tinydocumentimpl.numberOfNamespaces : -1;
/* 125*/        for(int l = 0; l < j; l++)
/* 126*/            tinydocumentimpl.addNamespace(nodeNr, ai[l]);

/* 129*/        j = 0;
/* 133*/        int i1 = attributes.getLength();
/* 134*/        int j1 = i1 != 0 ? tinydocumentimpl.numberOfAttributes : -1;
/* 136*/        tinydocumentimpl.addNode((short)1, currentDepth, j1, k, i);
/* 138*/        for(int k1 = 0; k1 < i1; k1++)
                {
/* 139*/            int l1 = super.namePool.allocate(Name.getPrefix(attributes.getQName(k1)), attributes.getURI(k1), attributes.getLocalName(k1));
/* 143*/            tinydocumentimpl.addAttribute(nodeNr, l1, attributes.getType(k1), attributes.getValue(k1));
                }

/* 151*/        int i2 = prevAtDepth[currentDepth];
/* 152*/        if(i2 > 0)
/* 153*/            tinydocumentimpl.next[i2] = nodeNr;
/* 155*/        prevAtDepth[currentDepth] = nodeNr;
/* 156*/        currentDepth++;
/* 158*/        if(currentDepth == prevAtDepth.length)
                {
/* 159*/            int ai1[] = new int[currentDepth * 2];
/* 160*/            System.arraycopy(prevAtDepth, 0, ai1, 0, currentDepth);
/* 161*/            prevAtDepth = ai1;
                }
/* 163*/        prevAtDepth[currentDepth] = -1;
/* 165*/        if(super.locator != null)
                {
/* 166*/            tinydocumentimpl.setSystemId(nodeNr, super.locator.getSystemId());
/* 167*/            if(super.lineNumbering)
/* 168*/                tinydocumentimpl.setLineNumber(nodeNr, super.locator.getLineNumber());
                }
/* 171*/        nodeNr++;
            }

            public void endElement(int i)
                throws TransformerException
            {
/* 182*/        TinyDocumentImpl tinydocumentimpl = (TinyDocumentImpl)super.currentDocument;
/* 185*/        if(super.previewManager != null)
                {
/* 187*/            int j = i & 0xfffff;
/* 188*/            if(super.previewManager.isPreviewElement(j))
                    {
/* 189*/                TinyNodeImpl tinynodeimpl = tinydocumentimpl.getNode(prevAtDepth[currentDepth - 1]);
/* 191*/                com.icl.saxon.Context context = super.controller.makeContext(tinynodeimpl);
/* 192*/                super.controller.applyTemplates(context, new SingletonNodeSet(tinynodeimpl), super.controller.getRuleManager().getMode(super.previewManager.getPreviewMode()), null);
/* 199*/                nodeNr = prevAtDepth[currentDepth - 1] + 1;
/* 200*/                tinydocumentimpl.truncate(nodeNr);
                    }
                }
/* 205*/        int k = prevAtDepth[currentDepth];
/* 206*/        if(k > 0)
/* 207*/            tinydocumentimpl.next[k] = -1;
/* 209*/        prevAtDepth[currentDepth] = -1;
/* 211*/        currentDepth--;
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/* 221*/        TinyDocumentImpl tinydocumentimpl = (TinyDocumentImpl)super.currentDocument;
/* 222*/        if(j > 0)
                {
/* 223*/            int k = tinydocumentimpl.charBufferLength;
/* 224*/            tinydocumentimpl.appendChars(ac, i, j);
/* 225*/            tinydocumentimpl.addNode((short)3, currentDepth, k, j, -1);
/* 227*/            int l = prevAtDepth[currentDepth];
/* 228*/            if(l > 0)
/* 229*/                tinydocumentimpl.next[l] = nodeNr;
/* 231*/            prevAtDepth[currentDepth] = nodeNr;
/* 233*/            nodeNr++;
                }
            }

            public void processingInstruction(String s, String s1)
                throws TransformerException
            {
/* 250*/        TinyDocumentImpl tinydocumentimpl = (TinyDocumentImpl)super.currentDocument;
/* 251*/        if(!super.discardComments)
                {
/* 252*/            int i = tinydocumentimpl.commentBuffer.length();
/* 253*/            tinydocumentimpl.commentBuffer.append(s1);
/* 254*/            int j = super.namePool.allocate("", "", s);
/* 255*/            tinydocumentimpl.addNode((short)7, currentDepth, i, s1.length(), j);
/* 258*/            int k = prevAtDepth[currentDepth];
/* 259*/            if(k > 0)
/* 260*/                tinydocumentimpl.next[k] = nodeNr;
/* 262*/            prevAtDepth[currentDepth] = nodeNr;
/* 264*/            nodeNr++;
                }
            }

            public void comment(char ac[], int i, int j)
                throws TransformerException
            {
/* 279*/        addComment(new String(ac, i, j));
            }

            private void addComment(String s)
                throws TransformerException
            {
/* 283*/        TinyDocumentImpl tinydocumentimpl = (TinyDocumentImpl)super.currentDocument;
/* 284*/        if(!super.discardComments && !super.inDTD)
                {
/* 285*/            int i = tinydocumentimpl.commentBuffer.length();
/* 286*/            tinydocumentimpl.commentBuffer.append(s);
/* 287*/            tinydocumentimpl.addNode((short)8, currentDepth, i, s.length(), -1);
/* 289*/            int j = prevAtDepth[currentDepth];
/* 290*/            if(j > 0)
/* 291*/                tinydocumentimpl.next[j] = nodeNr;
/* 293*/            prevAtDepth[currentDepth] = nodeNr;
/* 295*/            nodeNr++;
                }
            }

            public void setUnparsedEntity(String s, String s1)
            {
/* 305*/        ((TinyDocumentImpl)super.currentDocument).setUnparsedEntity(s, s1);
            }
}
