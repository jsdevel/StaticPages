// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   FollowingEnumeration.java

package com.icl.saxon.tree;

import com.icl.saxon.om.AbstractNode;
import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tree:
//            TreeEnumeration, DocumentImpl, NodeImpl

final class FollowingEnumeration extends TreeEnumeration
{

            private NodeImpl root;

            public FollowingEnumeration(NodeImpl nodeimpl, NodeTest nodetest)
            {
/*  10*/        super(nodeimpl, nodetest);
/*  11*/        root = (DocumentImpl)nodeimpl.getDocumentRoot();
/*  13*/        short word0 = nodeimpl.getNodeType();
/*  14*/        if(word0 == 2 || word0 == 13)
/*  15*/            super.next = ((NodeImpl)nodeimpl.getParentNode()).getNextInDocument(root);
/*  18*/        else
/*  18*/            do
                    {
/*  18*/                super.next = (NodeImpl)nodeimpl.getNextSibling();
/*  19*/                if(super.next == null)
/*  19*/                    nodeimpl = (NodeImpl)nodeimpl.getParentNode();
                    } while(super.next == null && nodeimpl != null);
/*  23*/        for(; !conforms(super.next); step());
            }

            protected void step()
            {
/*  28*/        super.next = super.next.getNextInDocument(root);
            }

            public boolean isSorted()
            {
/*  32*/        return true;
            }

            public int getLastPosition()
            {
/*  40*/        if(super.last >= 0)
                {
/*  40*/            return super.last;
                } else
                {
/*  41*/            FollowingEnumeration followingenumeration = new FollowingEnumeration(super.start, super.nodeTest);
/*  43*/            return followingenumeration.count();
                }
            }
}
