// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TinyProcInstImpl.java

package com.icl.saxon.tinytree;

import com.icl.saxon.om.AbstractNode;
import com.icl.saxon.output.Outputter;
import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;
import org.w3c.dom.ProcessingInstruction;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyNodeImpl, TinyDocumentImpl

final class TinyProcInstImpl extends TinyNodeImpl
    implements ProcessingInstruction
{

            public TinyProcInstImpl(TinyDocumentImpl tinydocumentimpl, int i)
            {
/*  19*/        super.document = tinydocumentimpl;
/*  20*/        super.nodeNr = i;
            }

            public String getStringValue()
            {
/*  24*/        int i = super.document.offset[super.nodeNr];
/*  25*/        int j = super.document.length[super.nodeNr];
/*  26*/        if(j == 0)
                {
/*  27*/            return "";
                } else
                {
/*  29*/            char ac[] = new char[j];
/*  30*/            super.document.commentBuffer.getChars(i, i + j, ac, 0);
/*  31*/            return new String(ac, 0, j);
                }
            }

            public final short getNodeType()
            {
/*  35*/        return 7;
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/*  43*/        outputter.writePI(getDisplayName(), getStringValue());
            }

            public String getTarget()
            {
/*  55*/        return getDisplayName();
            }

            public String getData()
            {
/*  65*/        return getStringValue();
            }

            public void setData(String s)
                throws DOMException
            {
/*  75*/        disallowUpdate();
            }
}
