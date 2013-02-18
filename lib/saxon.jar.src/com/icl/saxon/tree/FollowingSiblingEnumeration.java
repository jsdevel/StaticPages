// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   FollowingSiblingEnumeration.java

package com.icl.saxon.tree;

import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tree:
//            TreeEnumeration, NodeImpl

final class FollowingSiblingEnumeration extends TreeEnumeration
{

            public FollowingSiblingEnumeration(NodeImpl nodeimpl, NodeTest nodetest)
            {
/*   7*/        super(nodeimpl, nodetest);
/*   8*/        advance();
            }

            protected void step()
            {
/*  12*/        super.next = (NodeImpl)super.next.getNextSibling();
            }

            public boolean isSorted()
            {
/*  16*/        return true;
            }

            public int getLastPosition()
            {
/*  24*/        if(super.last >= 0)
                {
/*  24*/            return super.last;
                } else
                {
/*  25*/            FollowingSiblingEnumeration followingsiblingenumeration = new FollowingSiblingEnumeration(super.start, super.nodeTest);
/*  27*/            return followingsiblingenumeration.count();
                }
            }
}
