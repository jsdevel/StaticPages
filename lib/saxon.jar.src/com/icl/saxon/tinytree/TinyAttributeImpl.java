// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TinyAttributeImpl.java

package com.icl.saxon.tinytree;

import com.icl.saxon.om.NamePool;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.output.Outputter;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Attr;
import org.w3c.dom.TypeInfo;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyNodeImpl, TinyDocumentImpl

final class TinyAttributeImpl extends TinyNodeImpl
    implements Attr
{

            public TinyAttributeImpl(TinyDocumentImpl tinydocumentimpl, int i)
            {
/*  19*/        super.document = tinydocumentimpl;
/*  20*/        super.nodeNr = i;
            }

            public NodeInfo getParent()
            {
/*  28*/        return super.document.getNode(super.document.attParent[super.nodeNr]);
            }

            protected long getSequenceNumber()
            {
/*  40*/        long l = ((TinyNodeImpl)getParent()).getSequenceNumber() + 32768L + (long)(super.nodeNr - super.document.offset[super.document.attParent[super.nodeNr]]);
/*  44*/        return l;
            }

            public final short getNodeType()
            {
/*  54*/        return 2;
            }

            public String getStringValue()
            {
/*  63*/        return super.document.attValue[super.nodeNr];
            }

            public int getFingerprint()
            {
/*  71*/        return super.document.attCode[super.nodeNr] & 0xfffff;
            }

            public int getNameCode()
            {
/*  79*/        return super.document.attCode[super.nodeNr];
            }

            public String getPrefix()
            {
/*  88*/        int i = super.document.attCode[super.nodeNr];
/*  89*/        if((i >> 20 & 0xff) == 0)
/*  89*/            return "";
/*  90*/        else
/*  90*/            return super.document.getNamePool().getPrefix(i);
            }

            public String getDisplayName()
            {
/* 101*/        return super.document.getNamePool().getDisplayName(super.document.attCode[super.nodeNr]);
            }

            public String getLocalName()
            {
/* 112*/        return super.document.getNamePool().getLocalName(super.document.attCode[super.nodeNr]);
            }

            public final String getURI()
            {
/* 122*/        return super.document.getNamePool().getURI(super.document.attCode[super.nodeNr]);
            }

            public String generateId()
            {
/* 130*/        return getParent().generateId() + "_a" + super.nodeNr;
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/* 138*/        int i = super.document.attCode[super.nodeNr];
/* 139*/        if((i >> 20 & 0xff) != 0)
/* 141*/            i = outputter.checkAttributePrefix(i);
/* 143*/        outputter.writeAttribute(i, getStringValue());
            }

            public int getLineNumber()
            {
/* 151*/        return getParent().getLineNumber();
            }

            public TypeInfo getSchemaTypeInfo()
            {
/* 164*/        return null;
            }

            public boolean isId()
            {
/* 215*/        return false;
            }
}
