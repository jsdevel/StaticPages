// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TinyElementImpl.java

package com.icl.saxon.tinytree;

import com.icl.saxon.om.*;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.pattern.AnyNodeTest;
import com.icl.saxon.tree.DOMExceptionImpl;
import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyParentNodeImpl, TinyNodeImpl, TinyDocumentImpl, TinyAttributeImpl

final class TinyElementImpl extends TinyParentNodeImpl
    implements Element
{

            public TinyElementImpl(TinyDocumentImpl tinydocumentimpl, int i)
            {
/*  30*/        super.document = tinydocumentimpl;
/*  31*/        super.nodeNr = i;
            }

            public final short getNodeType()
            {
/*  40*/        return 1;
            }

            public String getBaseURI()
            {
/*  49*/        String s = getAttributeValue("http://www.w3.org/XML/1998/namespace", "base");
/*  50*/        if(s != null)
/*  51*/            return s;
/*  53*/        String s1 = getSystemId();
/*  54*/        NodeInfo nodeinfo = getParent();
/*  55*/        String s2 = nodeinfo.getSystemId();
/*  56*/        if(s1.equals(s2))
/*  57*/            return nodeinfo.getBaseURI();
/*  59*/        else
/*  59*/            return s1;
            }

            public void outputNamespaceNodes(Outputter outputter, boolean flag)
                throws TransformerException
            {
/*  74*/        int i = super.document.length[super.nodeNr];
/*  75*/        if(i > 0)
/*  78*/            for(; i < super.document.numberOfNamespaces && super.document.namespaceParent[i] == super.nodeNr; i++)
                    {
/*  78*/                int j = super.document.namespaceCode[i];
/*  79*/                outputter.writeNamespaceDeclaration(j);
                    }

/*  87*/        if(flag && super.document.isUsingNamespaces())
/*  88*/            getParent().outputNamespaceNodes(outputter, true);
            }

            public boolean hasAttributes()
            {
/* 101*/        return super.document.offset[super.nodeNr] >= 0;
            }

            public String getAttributeValue(String s, String s1)
            {
/* 114*/        int i = super.document.getNamePool().getFingerprint(s, s1);
/* 115*/        return getAttributeValue(i);
            }

            public String getAttributeValue(int i)
            {
/* 125*/        int j = super.document.offset[super.nodeNr];
/* 126*/        if(j < 0)
/* 126*/            return null;
/* 128*/        for(; j < super.document.numberOfAttributes && super.document.attParent[j] == super.nodeNr; j++)
/* 128*/            if((super.document.attCode[j] & 0xfffff) == i)
/* 129*/                return super.document.attValue[j];

/* 133*/        return null;
            }

            public TinyAttributeImpl makeAttributeNode(int i)
            {
/* 143*/        int j = super.document.offset[super.nodeNr];
/* 144*/        if(j < 0)
/* 144*/            return null;
/* 145*/        else
/* 145*/            return super.document.getAttributeNode(j + i);
            }

            public void setAttribute(String s, String s1)
                throws DOMException
            {
/* 157*/        throw new DOMExceptionImpl((short)9999, "Saxon DOM is not updateable");
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/* 165*/        copy(outputter, true);
            }

            public void copy(Outputter outputter, boolean flag)
                throws TransformerException
            {
/* 180*/        int i = getNameCode();
/* 181*/        outputter.writeStartTag(i);
/* 185*/        outputNamespaceNodes(outputter, flag);
/* 189*/        int j = super.document.offset[super.nodeNr];
/* 190*/        if(j >= 0)
/* 192*/            for(; j < super.document.numberOfAttributes && super.document.attParent[j] == super.nodeNr; j++)
/* 192*/                super.document.getAttributeNode(j).copy(outputter);

/* 199*/        for(AxisEnumeration axisenumeration = getEnumeration((byte)3, AnyNodeTest.getInstance()); axisenumeration.hasMoreElements();)
                {
/* 203*/            NodeInfo nodeinfo = axisenumeration.nextElement();
/* 204*/            if(nodeinfo instanceof TinyElementImpl)
/* 205*/                ((TinyElementImpl)nodeinfo).copy(outputter, false);
/* 208*/            else
/* 208*/                nodeinfo.copy(outputter);
                }

/* 211*/        outputter.writeEndTag(i);
            }
}
