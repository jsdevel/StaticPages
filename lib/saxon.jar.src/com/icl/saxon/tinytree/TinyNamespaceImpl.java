// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TinyNamespaceImpl.java

package com.icl.saxon.tinytree;

import com.icl.saxon.om.NamePool;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.output.Outputter;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyNodeImpl, TinyDocumentImpl

final class TinyNamespaceImpl extends TinyNodeImpl
{

            private int parentNode;
            private int nameCode;

            public TinyNamespaceImpl(TinyDocumentImpl tinydocumentimpl, int i)
            {
/*  26*/        super.document = tinydocumentimpl;
/*  27*/        super.nodeNr = i;
/*  28*/        nameCode = super.document.getNamePool().allocate("", "", getLocalName());
            }

            public int getNamespaceCode()
            {
/*  36*/        return super.document.namespaceCode[super.nodeNr];
            }

            public int getFingerprint()
            {
/*  44*/        return nameCode & 0xfffff;
            }

            void setParentNode(int i)
            {
/*  52*/        parentNode = i;
            }

            public int getNameCode()
            {
/*  60*/        return nameCode;
            }

            public String getPrefix()
            {
/*  69*/        return null;
            }

            public String getDisplayName()
            {
/*  79*/        return getLocalName();
            }

            public String getLocalName()
            {
/*  88*/        return super.document.getNamePool().getPrefixFromNamespaceCode(super.document.namespaceCode[super.nodeNr]);
            }

            public String getURI()
            {
/*  98*/        return null;
            }

            public NodeInfo getParent()
            {
/* 106*/        return super.document.getNode(parentNode);
            }

            public final boolean isSameNodeInfo(NodeInfo nodeinfo)
            {
/* 116*/        if(!(nodeinfo instanceof TinyNamespaceImpl))
/* 116*/            return false;
/* 117*/        if(this == nodeinfo)
                {
/* 117*/            return true;
                } else
                {
/* 118*/            TinyNamespaceImpl tinynamespaceimpl = (TinyNamespaceImpl)nodeinfo;
/* 119*/            return parentNode == ((TinyNamespaceImpl)nodeinfo).parentNode && super.document == ((TinyNodeImpl) (tinynamespaceimpl)).document && super.nodeNr == ((TinyNodeImpl) ((TinyNamespaceImpl)nodeinfo)).nodeNr;
                }
            }

            public final short getNodeType()
            {
/* 130*/        return 13;
            }

            public final String getStringValue()
            {
/* 139*/        return super.document.getNamePool().getURIFromNamespaceCode(super.document.namespaceCode[super.nodeNr]);
            }

            public String generateId()
            {
/* 148*/        return getParent().generateId() + "_xmlns_" + getFingerprint();
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/* 156*/        outputter.copyNamespaceNode(getNamespaceCode());
            }

            protected long getSequenceNumber()
            {
/* 167*/        return ((TinyNodeImpl)getParent()).getSequenceNumber() + (long)super.nodeNr + 1L;
            }
}
