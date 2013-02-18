// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TinyCommentImpl.java

package com.icl.saxon.tinytree;

import com.icl.saxon.output.Outputter;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Comment;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyNodeImpl, TinyDocumentImpl

final class TinyCommentImpl extends TinyNodeImpl
    implements Comment
{

            public TinyCommentImpl(TinyDocumentImpl tinydocumentimpl, int i)
            {
/*  21*/        super.document = tinydocumentimpl;
/*  22*/        super.nodeNr = i;
            }

            public final String getStringValue()
            {
/*  30*/        int i = super.document.offset[super.nodeNr];
/*  31*/        int j = super.document.length[super.nodeNr];
/*  32*/        if(j == 0)
                {
/*  32*/            return "";
                } else
                {
/*  33*/            char ac[] = new char[j];
/*  34*/            super.document.commentBuffer.getChars(i, i + j, ac, 0);
/*  35*/            return new String(ac, 0, j);
                }
            }

            public final short getNodeType()
            {
/*  44*/        return 8;
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/*  52*/        outputter.writeComment(getStringValue());
            }
}
