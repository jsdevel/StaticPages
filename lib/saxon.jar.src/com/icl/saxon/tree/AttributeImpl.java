// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   AttributeImpl.java

package com.icl.saxon.tree;

import com.icl.saxon.om.AbstractNode;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.output.Outputter;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

// Referenced classes of package com.icl.saxon.tree:
//            NodeImpl, ElementImpl, AttributeCollection, ParentNodeImpl

final class AttributeImpl extends NodeImpl
    implements Attr
{

            private int nameCode;
            private String value;

            public AttributeImpl(ElementImpl elementimpl, int i)
            {
/*  28*/        super.parent = elementimpl;
/*  29*/        super.index = i;
/*  30*/        AttributeCollection attributecollection = elementimpl.getAttributeList();
/*  31*/        nameCode = attributecollection.getNameCode(i);
/*  32*/        value = attributecollection.getValue(i);
            }

            public int getNameCode()
            {
/*  40*/        return nameCode;
            }

            public boolean isSameNodeInfo(NodeInfo nodeinfo)
            {
/*  50*/        if(!(nodeinfo instanceof AttributeImpl))
/*  50*/            return false;
/*  51*/        if(this == nodeinfo)
                {
/*  51*/            return true;
                } else
                {
/*  52*/            AttributeImpl attributeimpl = (AttributeImpl)nodeinfo;
/*  53*/            return super.parent.isSameNode(((NodeImpl) (attributeimpl)).parent) && (nameCode & 0xfffff) == (attributeimpl.nameCode & 0xfffff);
                }
            }

            protected long getSequenceNumber()
            {
/*  65*/        return super.parent.getSequenceNumber() + 32768L + (long)super.index;
            }

            public final short getNodeType()
            {
/*  75*/        return 2;
            }

            public String getStringValue()
            {
/*  84*/        return value;
            }

            public Node getNextSibling()
            {
/*  92*/        return null;
            }

            public Node getPreviousSibling()
            {
/* 100*/        return null;
            }

            public NodeImpl getPreviousInDocument()
            {
/* 108*/        return (NodeImpl)getParent();
            }

            public NodeImpl getNextInDocument(NodeImpl nodeimpl)
            {
/* 116*/        if(this == nodeimpl)
/* 116*/            return null;
/* 117*/        else
/* 117*/            return ((NodeImpl)getParent()).getNextInDocument(nodeimpl);
            }

            public String generateId()
            {
/* 125*/        return super.parent.generateId() + "_a" + getFingerprint();
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/* 133*/        int i = getNameCode();
/* 134*/        if((i >> 20 & 0xff) != 0)
/* 136*/            i = outputter.checkAttributePrefix(i);
/* 138*/        outputter.writeAttribute(i, getStringValue());
            }
}
