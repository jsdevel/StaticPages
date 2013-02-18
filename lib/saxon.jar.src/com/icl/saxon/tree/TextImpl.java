// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TextImpl.java

package com.icl.saxon.tree;

import com.icl.saxon.om.DocumentInfo;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.output.Outputter;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Text;

// Referenced classes of package com.icl.saxon.tree:
//            NodeImpl, ParentNodeImpl

final class TextImpl extends NodeImpl
    implements Text
{

            private NodeInfo parent;
            private String content;

            public TextImpl(ParentNodeImpl parentnodeimpl, String s)
            {
/*  21*/        parent = parentnodeimpl;
/*  22*/        content = s;
            }

            public DocumentInfo getDocumentRoot()
            {
/*  30*/        return parent.getDocumentRoot();
            }

            public String getStringValue()
            {
/*  39*/        return content;
            }

            public final short getNodeType()
            {
/*  48*/        return 3;
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/*  56*/        outputter.writeContent(content);
            }

            public void copyStringValue(Outputter outputter)
                throws TransformerException
            {
/*  64*/        outputter.writeContent(content);
            }

            public void truncateToStart()
            {
            }
}
