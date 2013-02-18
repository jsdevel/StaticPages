// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   PrecedingSiblingEnumeration.java

package com.icl.saxon.tree;

import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tree:
//            TreeEnumeration, NodeImpl

final class PrecedingSiblingEnumeration extends TreeEnumeration
{

            public PrecedingSiblingEnumeration(NodeImpl nodeimpl, NodeTest nodetest)
            {
/*   7*/        super(nodeimpl, nodetest);
/*   8*/        advance();
            }

            protected void step()
            {
/*  12*/        super.next = (NodeImpl)super.next.getPreviousSibling();
            }

            public int getLastPosition()
            {
/*  20*/        if(super.last >= 0)
                {
/*  20*/            return super.last;
                } else
                {
/*  21*/            PrecedingSiblingEnumeration precedingsiblingenumeration = new PrecedingSiblingEnumeration(super.start, super.nodeTest);
/*  23*/            return precedingsiblingenumeration.count();
                }
            }
}
