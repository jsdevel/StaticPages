// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DescendantEnumeration.java

package com.icl.saxon.tree;

import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tree:
//            TreeEnumeration, NodeImpl

final class DescendantEnumeration extends TreeEnumeration
{

            private NodeImpl root;
            private boolean includeSelf;

            public DescendantEnumeration(NodeImpl nodeimpl, NodeTest nodetest, boolean flag)
            {
/*  10*/        super(nodeimpl, nodetest);
/*  11*/        root = nodeimpl;
/*  12*/        includeSelf = flag;
/*  13*/        if(!flag || !conforms(nodeimpl))
/*  14*/            advance();
            }

            public boolean isSorted()
            {
/*  19*/        return true;
            }

            protected void step()
            {
/*  23*/        super.next = super.next.getNextInDocument(root);
            }

            public int getLastPosition()
            {
/*  31*/        if(super.last >= 0)
                {
/*  31*/            return super.last;
                } else
                {
/*  32*/            DescendantEnumeration descendantenumeration = new DescendantEnumeration(super.start, super.nodeTest, includeSelf);
/*  34*/            return descendantenumeration.count();
                }
            }
}
