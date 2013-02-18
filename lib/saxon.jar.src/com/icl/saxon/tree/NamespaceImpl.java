// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NamespaceImpl.java

package com.icl.saxon.tree;

import com.icl.saxon.om.*;
import com.icl.saxon.output.Outputter;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;

// Referenced classes of package com.icl.saxon.tree:
//            NodeImpl, ParentNodeImpl, ElementImpl

final class NamespaceImpl extends NodeImpl
{

            private int nsCode;
            private int nameCode;
            private int index;

            public NamespaceImpl(ElementImpl elementimpl, int i, int j)
            {
/*  30*/        super.parent = elementimpl;
/*  31*/        nsCode = i;
/*  32*/        NamePool namepool = getNamePool();
/*  33*/        nameCode = namepool.allocate("", "", namepool.getPrefixFromNamespaceCode(i));
/*  34*/        index = j;
            }

            public int getNameCode()
            {
/*  42*/        return nameCode;
            }

            public int getNamespaceCode()
            {
/*  50*/        return nsCode;
            }

            public boolean isSameNodeInfo(NodeInfo nodeinfo)
            {
/*  60*/        if(!(nodeinfo instanceof NamespaceImpl))
/*  60*/            return false;
/*  61*/        if(this == nodeinfo)
                {
/*  61*/            return true;
                } else
                {
/*  62*/            NamespaceImpl namespaceimpl = (NamespaceImpl)nodeinfo;
/*  63*/            return super.parent.isSameNode(((NodeImpl) (namespaceimpl)).parent) && nsCode == namespaceimpl.nsCode;
                }
            }

            public String getLocalName()
            {
/*  72*/        return getNamePool().getPrefixFromNamespaceCode(nsCode);
            }

            public void setNamespaceCode(int i)
            {
/*  81*/        NamePool namepool = getNamePool();
/*  82*/        nsCode = i;
/*  83*/        nameCode = namepool.allocate("", "", namepool.getPrefixFromNamespaceCode(i));
            }

            public final short getNodeType()
            {
/*  92*/        return 13;
            }

            public String getStringValue()
            {
/* 101*/        return getNamePool().getURIFromNamespaceCode(nsCode);
            }

            public String getNodeName()
            {
/* 111*/        return getLocalName();
            }

            public Node getNextSibling()
            {
/* 119*/        return null;
            }

            public Node getPreviousSibling()
            {
/* 127*/        return null;
            }

            public NodeImpl getPreviousInDocument()
            {
/* 135*/        return (NodeImpl)getParent();
            }

            public NodeImpl getNextInDocument(NodeImpl nodeimpl)
            {
/* 143*/        if(this == nodeimpl)
/* 143*/            return null;
/* 144*/        else
/* 144*/            return ((NodeImpl)getParent()).getNextInDocument(nodeimpl);
            }

            public String generateId()
            {
/* 153*/        return super.parent.generateId() + "_xmlns_" + getFingerprint();
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/* 161*/        outputter.copyNamespaceNode(nsCode);
            }

            protected long getSequenceNumber()
            {
/* 172*/        return super.parent.getSequenceNumber() + (long)index;
            }
}
