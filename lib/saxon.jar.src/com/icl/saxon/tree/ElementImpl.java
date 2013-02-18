// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ElementImpl.java

package com.icl.saxon.tree;

import com.icl.saxon.om.*;
import com.icl.saxon.output.Outputter;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

// Referenced classes of package com.icl.saxon.tree:
//            ParentNodeImpl, DocumentImpl, NamespaceImpl, NodeImpl, 
//            AttributeCollection

public class ElementImpl extends ParentNodeImpl
    implements Element
{

            private static AttributeCollection emptyAtts = new AttributeCollection((NamePool)null);
            protected int nameCode;
            protected DocumentImpl root;

            public ElementImpl()
            {
            }

            public void setNameCode(int i)
            {
/*  48*/        nameCode = i;
            }

            public void initialise(int i, AttributeCollection attributecollection, NodeInfo nodeinfo, String s, int j, int k)
            {
/*  60*/        nameCode = i;
/*  61*/        super.parent = (ParentNodeImpl)nodeinfo;
/*  62*/        super.sequence = k;
/*  63*/        root = (DocumentImpl)nodeinfo.getDocumentRoot();
/*  64*/        root.setLineNumber(k, j);
/*  65*/        root.setSystemId(k, s);
            }

            public void setSystemId(String s)
            {
/*  75*/        root.setSystemId(super.sequence, s);
            }

            public DocumentInfo getDocumentRoot()
            {
/*  83*/        return root;
            }

            public final String getSystemId()
            {
/*  91*/        return ((DocumentImpl)getDocumentRoot()).getSystemId(super.sequence);
            }

            public String getBaseURI()
            {
/* 100*/        String s = getAttributeValue("http://www.w3.org/XML/1998/namespace", "base");
/* 101*/        if(s != null)
/* 102*/            return s;
/* 104*/        String s1 = getSystemId();
/* 105*/        String s2 = super.parent.getSystemId();
/* 106*/        if(s1.equals(s2))
/* 107*/            return super.parent.getBaseURI();
/* 109*/        else
/* 109*/            return s1;
            }

            public void setLineNumber(int i)
            {
/* 118*/        ((DocumentImpl)getDocumentRoot()).setLineNumber(super.sequence, i);
            }

            public int getLineNumber()
            {
/* 127*/        return ((DocumentImpl)getDocumentRoot()).getLineNumber(super.sequence);
            }

            public int getNameCode()
            {
/* 136*/        return nameCode;
            }

            public String generateId()
            {
/* 146*/        return "e" + super.sequence;
            }

            public short getURICodeForPrefix(String s)
                throws NamespaceException
            {
/* 160*/        if(s.equals("xml"))
/* 160*/            return 1;
/* 161*/        if(super.parent.getNodeType() == 9)
                {
/* 162*/            if(s.equals(""))
/* 163*/                return 0;
/* 165*/            else
/* 165*/                throw new NamespaceException(s);
                } else
                {
/* 167*/            return ((ElementImpl)super.parent).getURICodeForPrefix(s);
                }
            }

            public String getPrefixForURI(String s)
            {
/* 180*/        if(super.parent.getNodeType() == 9)
/* 181*/            return null;
/* 183*/        else
/* 183*/            return ((ElementImpl)super.parent).getPrefixForURI(s);
            }

            public final int makeNameCode(String s, boolean flag)
                throws NamespaceException
            {
/* 200*/        NamePool namepool = getNamePool();
/* 201*/        String s1 = Name.getPrefix(s);
/* 202*/        if(s1.equals(""))
                {
/* 203*/            short word0 = 0;
/* 205*/            if(flag)
/* 206*/                word0 = getURICodeForPrefix(s1);
/* 209*/            return namepool.allocate(s1, word0, s);
                } else
                {
/* 212*/            String s2 = Name.getLocalName(s);
/* 213*/            short word1 = getURICodeForPrefix(s1);
/* 214*/            return namepool.allocate(s1, word1, s2);
                }
            }

            public void addNamespaceNodes(ElementImpl elementimpl, Vector vector, boolean flag)
            {
/* 234*/        if(super.parent.getNodeType() != 9)
/* 235*/            ((ElementImpl)super.parent).addNamespaceNodes(elementimpl, vector, false);
/* 237*/        if(flag)
                {
/* 238*/            int i = 0x10001;
/* 239*/            vector.addElement(new NamespaceImpl(this, i, vector.size() + 1));
                }
            }

            public void outputNamespaceNodes(Outputter outputter, boolean flag)
                throws TransformerException
            {
/* 255*/        if(flag && !(super.parent instanceof DocumentInfo))
/* 257*/            ((ElementImpl)super.parent).outputNamespaceNodes(outputter, true);
            }

            public final short getNodeType()
            {
/* 270*/        return 1;
            }

            public AttributeCollection getAttributeList()
            {
/* 281*/        return emptyAtts;
            }

            public String getAttributeValue(String s)
            {
/* 295*/        return null;
            }

            public void setAttribute(String s, String s1)
                throws DOMException
            {
/* 305*/        disallowUpdate();
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/* 313*/        copy(outputter, true);
            }

            public void copy(Outputter outputter, boolean flag)
                throws TransformerException
            {
/* 323*/        int i = getNameCode();
/* 324*/        outputter.writeStartTag(i);
/* 328*/        outputNamespaceNodes(outputter, flag);
/* 332*/        for(NodeImpl nodeimpl = (NodeImpl)getFirstChild(); nodeimpl != null; nodeimpl = (NodeImpl)nodeimpl.getNextSibling())
/* 334*/            if(nodeimpl instanceof ElementImpl)
/* 335*/                ((ElementImpl)nodeimpl).copy(outputter, false);
/* 337*/            else
/* 337*/                nodeimpl.copy(outputter);

/* 342*/        outputter.writeEndTag(i);
            }

}
