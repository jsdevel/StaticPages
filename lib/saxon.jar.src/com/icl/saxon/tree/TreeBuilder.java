// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TreeBuilder.java

package com.icl.saxon.tree;

import com.icl.saxon.*;
import com.icl.saxon.expr.SingletonNodeSet;
import com.icl.saxon.om.*;
import com.icl.saxon.output.Emitter;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

// Referenced classes of package com.icl.saxon.tree:
//            DocumentImpl, AttributeCollection, NodeImpl, ParentNodeImpl, 
//            TextImpl, ProcInstImpl, CommentImpl, NodeFactory, 
//            ElementImpl, ElementWithAttributes

public class TreeBuilder extends Builder
{
    private class DefaultNodeFactory
        implements NodeFactory
    {

                public ElementImpl makeElementNode(NodeInfo nodeinfo, int i, AttributeCollection attributecollection, int ai[], int j, Locator locator, int k)
                {
/* 303*/            if(attributecollection.getLength() == 0 && j == 0)
                    {
/* 307*/                ElementImpl elementimpl = new ElementImpl();
/* 308*/                String s = null;
/* 309*/                int l = -1;
/* 311*/                if(locator != null)
                        {
/* 312*/                    s = locator.getSystemId();
/* 313*/                    l = locator.getLineNumber();
                        }
/* 316*/                elementimpl.initialise(i, attributecollection, nodeinfo, s, l, k);
/* 318*/                return elementimpl;
                    }
/* 321*/            ElementWithAttributes elementwithattributes = new ElementWithAttributes();
/* 322*/            String s1 = null;
/* 323*/            int i1 = -1;
/* 325*/            if(locator != null)
                    {
/* 326*/                s1 = locator.getSystemId();
/* 327*/                i1 = locator.getLineNumber();
                    }
/* 330*/            elementwithattributes.setNamespaceDeclarations(ai, j);
/* 332*/            elementwithattributes.initialise(i, attributecollection, nodeinfo, s1, i1, k);
/* 334*/            return elementwithattributes;
                }

                private DefaultNodeFactory()
                {
                }

    }


            private static AttributeCollection emptyAttributeCollection = new AttributeCollection((NamePool)null);
            private ParentNodeImpl currentNode;
            private NodeFactory nodeFactory;
            private int size[];
            private int depth;
            private Vector arrays;
            private boolean previousText;
            private StringBuffer charBuffer;
            private int nextNodeNumber;

            public TreeBuilder()
            {
/*  29*/        size = new int[100];
/*  30*/        depth = 0;
/*  31*/        arrays = new Vector();
/*  35*/        nextNodeNumber = 1;
/*  42*/        nodeFactory = new DefaultNodeFactory();
            }

            public void setNodeFactory(NodeFactory nodefactory)
            {
/*  50*/        nodeFactory = nodefactory;
            }

            public void startDocument()
                throws TransformerException
            {
/*  64*/        super.failed = false;
/*  65*/        super.started = true;
                DocumentImpl documentimpl;
/*  68*/        if(super.currentDocument == null)
                {
/*  70*/            documentimpl = new DocumentImpl();
/*  71*/            super.currentDocument = documentimpl;
                } else
                {
/*  74*/            if(!(super.currentDocument instanceof DocumentImpl))
/*  75*/                throw new TransformerException("Root node supplied is of wrong type");
/*  77*/            documentimpl = (DocumentImpl)super.currentDocument;
/*  78*/            if(documentimpl.getFirstChild() != null)
/*  79*/                throw new TransformerException("Supplied document is not empty");
                }
/*  83*/        if(super.locator == null || super.locator.getSystemId() == null)
/*  84*/            super.locator = this;
/*  86*/        documentimpl.setSystemId(super.locator.getSystemId());
/*  87*/        documentimpl.setNamePool(super.namePool);
/*  88*/        documentimpl.setNodeFactory(nodeFactory);
/*  89*/        currentNode = documentimpl;
/*  90*/        depth = 0;
/*  91*/        size[depth] = 0;
/*  92*/        documentimpl.sequence = 0;
/*  93*/        charBuffer = new StringBuffer(super.estimatedLength);
/*  94*/        documentimpl.setCharacterBuffer(charBuffer);
/*  95*/        if(super.lineNumbering)
/*  96*/            documentimpl.setLineNumbering();
            }

            public void endDocument()
                throws TransformerException
            {
/* 108*/        if(currentNode == null)
                {
/* 108*/            return;
                } else
                {
/* 109*/            currentNode.compact(size[depth]);
/* 110*/            currentNode = null;
/* 114*/            arrays = null;
/* 119*/            return;
                }
            }

            public void setDocumentLocator(Locator locator)
            {
/* 127*/        super.locator = locator;
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/* 144*/        int k = attributes.getLength();
                AttributeCollection attributecollection;
/* 145*/        if(k == 0)
/* 146*/            attributecollection = emptyAttributeCollection;
/* 148*/        else
/* 148*/            attributecollection = new AttributeCollection(super.namePool, attributes);
/* 152*/        ElementImpl elementimpl = nodeFactory.makeElementNode(currentNode, i, attributecollection, ai, j, super.locator, nextNodeNumber++);
/* 165*/        for(; depth >= arrays.size(); arrays.addElement(new NodeImpl[20]));
/* 167*/        elementimpl.useChildrenArray((NodeImpl[])arrays.elementAt(depth));
/* 169*/        currentNode.addChild(elementimpl, size[depth]++);
/* 170*/        if(depth >= size.length - 1)
                {
/* 171*/            int ai1[] = new int[size.length * 2];
/* 172*/            System.arraycopy(size, 0, ai1, 0, size.length);
/* 173*/            size = ai1;
                }
/* 175*/        size[++depth] = 0;
/* 177*/        j = 0;
/* 179*/        if(currentNode instanceof DocumentInfo)
/* 180*/            ((DocumentImpl)currentNode).setDocumentElement(elementimpl);
/* 184*/        currentNode = elementimpl;
            }

            public void endElement(int i)
                throws TransformerException
            {
/* 194*/        currentNode.compact(size[depth]);
/* 197*/        if(super.previewManager != null && super.previewManager.isPreviewElement(currentNode.getFingerprint()))
                {
/* 201*/            com.icl.saxon.Context context = super.controller.makeContext(currentNode);
/* 202*/            super.controller.applyTemplates(context, new SingletonNodeSet(currentNode), super.controller.getRuleManager().getMode(super.previewManager.getPreviewMode()), null);
/* 207*/            currentNode.dropChildren();
                }
/* 211*/        depth--;
/* 212*/        currentNode = (ParentNodeImpl)currentNode.getParentNode();
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/* 222*/        if(j > 0)
                {
/* 223*/            int k = charBuffer.length();
/* 228*/            TextImpl textimpl = new TextImpl(currentNode, new String(ac, i, j));
/* 229*/            currentNode.addChild(textimpl, size[depth]++);
/* 230*/            previousText = true;
                }
            }

            public void processingInstruction(String s, String s1)
            {
/* 243*/        if(!super.discardComments)
                {
/* 244*/            int i = super.namePool.allocate("", "", s);
/* 245*/            ProcInstImpl procinstimpl = new ProcInstImpl(i, s1);
/* 246*/            currentNode.addChild(procinstimpl, size[depth]++);
/* 247*/            if(super.locator != null)
/* 248*/                procinstimpl.setLocation(super.locator.getSystemId(), super.locator.getLineNumber());
                }
            }

            public void comment(char ac[], int i, int j)
                throws TransformerException
            {
/* 259*/        if(!super.discardComments)
                {
/* 260*/            CommentImpl commentimpl = new CommentImpl(new String(ac, i, j));
/* 261*/            currentNode.addChild(commentimpl, size[depth]++);
                }
            }

            public void graftElement(ElementImpl elementimpl)
                throws TransformerException
            {
/* 274*/        currentNode.addChild(elementimpl, size[depth]++);
            }

            public void setUnparsedEntity(String s, String s1)
            {
/* 282*/        ((DocumentImpl)super.currentDocument).setUnparsedEntity(s, s1);
            }

}
