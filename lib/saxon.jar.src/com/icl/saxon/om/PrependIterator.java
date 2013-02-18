// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   PrependIterator.java

package com.icl.saxon.om;


// Referenced classes of package com.icl.saxon.om:
//            AxisEnumeration, NodeEnumeration, NodeInfo

public class PrependIterator
    implements AxisEnumeration
{

            NodeInfo start;
            AxisEnumeration base;
            int position;

            public PrependIterator(NodeInfo nodeinfo, AxisEnumeration axisenumeration)
            {
/*  15*/        position = 0;
/*  18*/        start = nodeinfo;
/*  19*/        base = axisenumeration;
            }

            public boolean hasMoreElements()
            {
/*  31*/        if(position == 0)
/*  32*/            return true;
/*  34*/        else
/*  34*/            return base.hasMoreElements();
            }

            public NodeInfo nextElement()
            {
/*  45*/        if(position == 0)
                {
/*  46*/            position = 1;
/*  47*/            return start;
                }
/*  49*/        NodeInfo nodeinfo = base.nextElement();
/*  50*/        if(nodeinfo == null)
/*  51*/            position = -1;
/*  53*/        else
/*  53*/            position++;
/*  55*/        return nodeinfo;
            }

            public boolean isPeer()
            {
/*  66*/        return false;
            }

            public boolean isReverseSorted()
            {
/*  77*/        return base.isReverseSorted();
            }

            public boolean isSorted()
            {
/*  87*/        return base.isSorted();
            }

            public int getLastPosition()
            {
/*  95*/        return base.getLastPosition() + 1;
            }
}
