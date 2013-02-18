// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TinyTextImpl.java

package com.icl.saxon.tinytree;

import com.icl.saxon.output.Outputter;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Text;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyNodeImpl, TinyDocumentImpl

final class TinyTextImpl extends TinyNodeImpl
    implements Text
{

            public TinyTextImpl(TinyDocumentImpl tinydocumentimpl, int i)
            {
/*  17*/        super.document = tinydocumentimpl;
/*  18*/        super.nodeNr = i;
            }

            public String getStringValue()
            {
/*  27*/        int i = super.document.offset[super.nodeNr];
/*  28*/        int j = super.document.length[super.nodeNr];
/*  29*/        return new String(super.document.charBuffer, i, j);
            }

            public final short getNodeType()
            {
/*  38*/        return 3;
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/*  46*/        int i = super.document.offset[super.nodeNr];
/*  47*/        int j = super.document.length[super.nodeNr];
/*  48*/        outputter.writeContent(super.document.charBuffer, i, j);
            }

            public void copyStringValue(Outputter outputter)
                throws TransformerException
            {
/*  56*/        int i = super.document.offset[super.nodeNr];
/*  57*/        int j = super.document.length[super.nodeNr];
/*  58*/        outputter.writeContent(super.document.charBuffer, i, j);
            }
}
