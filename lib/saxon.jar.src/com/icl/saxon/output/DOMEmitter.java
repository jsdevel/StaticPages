// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DOMEmitter.java

package com.icl.saxon.output;

import com.icl.saxon.om.NamePool;
import javax.xml.transform.TransformerException;
import org.w3c.dom.*;
import org.xml.sax.Attributes;

// Referenced classes of package com.icl.saxon.output:
//            Emitter

public class DOMEmitter extends Emitter
{

            protected Node currentNode;
            protected Document document;
            private boolean canNormalize;

            public DOMEmitter()
            {
/*  17*/        canNormalize = true;
            }

            public void startDocument()
            {
            }

            public void endDocument()
            {
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/*  45*/        String s = super.namePool.getDisplayName(i);
/*  48*/        try
                {
/*  48*/            Element element = document.createElement(s);
/*  49*/            currentNode.appendChild(element);
/*  50*/            currentNode = element;
/*  54*/            for(int k = 0; k < j; k++)
                    {
/*  55*/                String s1 = super.namePool.getPrefixFromNamespaceCode(ai[k]);
/*  56*/                String s2 = super.namePool.getURIFromNamespaceCode(ai[k]);
/*  57*/                if(!s2.equals("http://www.w3.org/XML/1998/namespace"))
/*  58*/                    if(s1.equals(""))
/*  59*/                        element.setAttribute("xmlns", s2);
/*  61*/                    else
/*  61*/                        element.setAttribute("xmlns:" + s1, s2);
                    }

/*  68*/            for(int l = 0; l < attributes.getLength(); l++)
/*  69*/                element.setAttribute(attributes.getQName(l), attributes.getValue(l));

                }
/*  75*/        catch(DOMException domexception)
                {
/*  75*/            throw new TransformerException(domexception);
                }
            }

            public void endElement(int i)
                throws TransformerException
            {
/*  86*/        if(canNormalize)
/*  88*/            try
                    {
/*  88*/                currentNode.normalize();
                    }
/*  90*/            catch(Throwable throwable)
                    {
/*  90*/                canNormalize = false;
                    }
/*  94*/        currentNode = currentNode.getParentNode();
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/* 106*/        try
                {
/* 106*/            org.w3c.dom.Text text = document.createTextNode(new String(ac, i, j));
/* 107*/            currentNode.appendChild(text);
                }
/* 109*/        catch(DOMException domexception)
                {
/* 109*/            throw new TransformerException(domexception);
                }
            }

            public void processingInstruction(String s, String s1)
                throws TransformerException
            {
/* 122*/        try
                {
/* 122*/            org.w3c.dom.ProcessingInstruction processinginstruction = document.createProcessingInstruction(s, s1);
/* 124*/            currentNode.appendChild(processinginstruction);
                }
/* 126*/        catch(DOMException domexception)
                {
/* 126*/            throw new TransformerException(domexception);
                }
            }

            public void comment(char ac[], int i, int j)
                throws TransformerException
            {
/* 137*/        try
                {
/* 137*/            org.w3c.dom.Comment comment1 = document.createComment(new String(ac, i, j));
/* 138*/            currentNode.appendChild(comment1);
                }
/* 140*/        catch(DOMException domexception)
                {
/* 140*/            throw new TransformerException(domexception);
                }
            }

            public void setNode(Node node)
            {
/* 149*/        currentNode = node;
/* 150*/        if(node.getNodeType() == 9)
/* 151*/            document = (Document)node;
/* 153*/        else
/* 153*/            document = currentNode.getOwnerDocument();
            }
}
