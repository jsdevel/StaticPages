// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   LookaheadEnumerator.java

package com.icl.saxon.expr;

import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;
import java.util.Vector;

// Referenced classes of package com.icl.saxon.expr:
//            LastPositionFinder, XPathException

public class LookaheadEnumerator
    implements NodeEnumeration, LastPositionFinder
{

            private NodeEnumeration base;
            private Vector reservoir;
            private int reservoirPosition;
            private int position;
            private int last;

            public LookaheadEnumerator(NodeEnumeration nodeenumeration)
            {
/*  29*/        reservoir = null;
/*  30*/        reservoirPosition = -1;
/*  31*/        position = 0;
/*  32*/        last = -1;
/*  41*/        base = nodeenumeration;
            }

            public boolean hasMoreElements()
            {
/*  49*/        if(reservoir == null)
/*  50*/            return base.hasMoreElements();
/*  52*/        else
/*  52*/            return reservoirPosition < reservoir.size();
            }

            public NodeInfo nextElement()
                throws XPathException
            {
/*  61*/        if(reservoir == null)
                {
/*  62*/            position++;
/*  63*/            return base.nextElement();
                }
/*  65*/        if(reservoirPosition < reservoir.size())
                {
/*  66*/            position++;
/*  67*/            return (NodeInfo)reservoir.elementAt(reservoirPosition++);
                } else
                {
/*  69*/            return null;
                }
            }

            public int getLastPosition()
                throws XPathException
            {
/*  79*/        if(last > 0)
/*  80*/            return last;
/*  83*/        reservoir = new Vector();
/*  84*/        reservoirPosition = 0;
/*  85*/        for(last = position; base.hasMoreElements(); last++)
/*  87*/            reservoir.addElement(base.nextElement());

/*  90*/        return last;
            }

            public boolean isSorted()
            {
/*  99*/        return base.isSorted();
            }

            public boolean isReverseSorted()
            {
/* 103*/        return base.isReverseSorted();
            }

            public boolean isPeer()
            {
/* 112*/        return base.isPeer();
            }
}
