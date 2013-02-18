// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   CommentImpl.java

package com.icl.saxon.tree;

import com.icl.saxon.output.Outputter;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Comment;

// Referenced classes of package com.icl.saxon.tree:
//            NodeImpl

final class CommentImpl extends NodeImpl
    implements Comment
{

            String comment;

            public CommentImpl(String s)
            {
/*  20*/        comment = s;
            }

            public final String getNodeName()
            {
/*  29*/        return "#comment";
            }

            public final String getStringValue()
            {
/*  33*/        return comment;
            }

            public final short getNodeType()
            {
/*  37*/        return 8;
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/*  45*/        outputter.writeComment(comment);
            }
}
