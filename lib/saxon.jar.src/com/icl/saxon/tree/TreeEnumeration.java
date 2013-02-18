// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TreeEnumeration.java

package com.icl.saxon.tree;

import com.icl.saxon.om.AxisEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tree:
//            NodeImpl

abstract class TreeEnumeration
    implements AxisEnumeration
{

            protected NodeImpl start;
            protected NodeImpl next;
            protected NodeTest nodeTest;
            protected int last;

            public TreeEnumeration(NodeImpl nodeimpl, NodeTest nodetest)
            {
/*  12*/        last = -1;
/*  20*/        next = nodeimpl;
/*  21*/        start = nodeimpl;
/*  22*/        nodeTest = nodetest;
            }

            protected boolean conforms(NodeImpl nodeimpl)
            {
/*  32*/        if(nodeimpl == null)
/*  32*/            return true;
/*  33*/        else
/*  33*/            return nodeTest.matches(nodeimpl);
            }

            protected final void advance()
            {
/*  42*/        do
/*  42*/            step();
/*  43*/        while(!conforms(next));
            }

            protected abstract void step();

            public final boolean hasMoreElements()
            {
/*  58*/        return next != null;
            }

            public final NodeInfo nextElement()
            {
/*  66*/        NodeImpl nodeimpl = next;
/*  67*/        advance();
/*  68*/        return nodeimpl;
            }

            public boolean isSorted()
            {
/*  76*/        return false;
            }

            public boolean isReverseSorted()
            {
/*  84*/        return !isSorted();
            }

            public boolean isPeer()
            {
/*  93*/        return false;
            }

            protected int count()
            {
                int i;
/* 104*/        for(i = 0; hasMoreElements(); i++)
/* 106*/            nextElement();

/* 109*/        return i;
            }

            public abstract int getLastPosition();
}
