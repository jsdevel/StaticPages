// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   PrecedingEnumeration.java

package com.icl.saxon.tree;

import com.icl.saxon.om.AbstractNode;
import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tree:
//            TreeEnumeration, NodeImpl

final class PrecedingEnumeration extends TreeEnumeration
{

            NodeImpl nextAncestor;

            public PrecedingEnumeration(NodeImpl nodeimpl, NodeTest nodetest)
            {
/*   9*/        super(nodeimpl, nodetest);
/*  12*/        nextAncestor = (NodeImpl)nodeimpl.getParent();
/*  13*/        advance();
            }

            protected boolean conforms(NodeImpl nodeimpl)
            {
/*  24*/        if(nodeimpl != null && nodeimpl.isSameNode(nextAncestor))
                {
/*  26*/            nextAncestor = (NodeImpl)nextAncestor.getParent();
/*  27*/            return false;
                } else
                {
/*  30*/            return super.conforms(nodeimpl);
                }
            }

            protected void step()
            {
/*  34*/        super.next = super.next.getPreviousInDocument();
            }

            public int getLastPosition()
            {
/*  42*/        if(super.last >= 0)
                {
/*  42*/            return super.last;
                } else
                {
/*  43*/            PrecedingEnumeration precedingenumeration = new PrecedingEnumeration(super.start, super.nodeTest);
/*  45*/            return precedingenumeration.count();
                }
            }
}
