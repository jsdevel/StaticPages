// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   PrecedingOrAncestorEnumeration.java

package com.icl.saxon.tree;

import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tree:
//            TreeEnumeration, NodeImpl

final class PrecedingOrAncestorEnumeration extends TreeEnumeration
{

            public PrecedingOrAncestorEnumeration(NodeImpl nodeimpl, NodeTest nodetest)
            {
/*  14*/        super(nodeimpl, nodetest);
/*  15*/        advance();
            }

            protected void step()
            {
/*  19*/        super.next = super.next.getPreviousInDocument();
            }

            public int getLastPosition()
            {
/*  27*/        if(super.last >= 0)
                {
/*  27*/            return super.last;
                } else
                {
/*  28*/            PrecedingOrAncestorEnumeration precedingorancestorenumeration = new PrecedingOrAncestorEnumeration(super.start, super.nodeTest);
/*  30*/            return precedingorancestorenumeration.count();
                }
            }
}
