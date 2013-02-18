// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ChildEnumeration.java

package com.icl.saxon.tree;

import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tree:
//            TreeEnumeration, NodeImpl

final class ChildEnumeration extends TreeEnumeration
{

            public ChildEnumeration(NodeImpl nodeimpl, NodeTest nodetest)
            {
/*   8*/        super(nodeimpl, nodetest);
/*   9*/        for(super.next = (NodeImpl)nodeimpl.getFirstChild(); !conforms(super.next); step());
            }

            protected void step()
            {
/*  16*/        super.next = (NodeImpl)super.next.getNextSibling();
            }

            public boolean isSorted()
            {
/*  20*/        return true;
            }

            public boolean isPeer()
            {
/*  24*/        return true;
            }

            public int getLastPosition()
            {
/*  32*/        if(super.last >= 0)
                {
/*  32*/            return super.last;
                } else
                {
/*  33*/            ChildEnumeration childenumeration = new ChildEnumeration(super.start, super.nodeTest);
/*  35*/            return childenumeration.count();
                }
            }
}
