// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   AncestorEnumeration.java

package com.icl.saxon.tree;

import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tree:
//            TreeEnumeration, NodeImpl

final class AncestorEnumeration extends TreeEnumeration
{

            private boolean includeSelf;

            public AncestorEnumeration(NodeImpl nodeimpl, NodeTest nodetest, boolean flag)
            {
/*   9*/        super(nodeimpl, nodetest);
/*  10*/        includeSelf = flag;
/*  11*/        if(!flag || !conforms(nodeimpl))
/*  12*/            advance();
            }

            protected void step()
            {
/*  17*/        super.next = (NodeImpl)super.next.getParent();
            }

            public int getLastPosition()
            {
/*  25*/        if(super.last >= 0)
                {
/*  25*/            return super.last;
                } else
                {
/*  26*/            AncestorEnumeration ancestorenumeration = new AncestorEnumeration(super.start, super.nodeTest, includeSelf);
/*  28*/            super.last = ancestorenumeration.count();
/*  29*/            return super.last;
                }
            }
}
