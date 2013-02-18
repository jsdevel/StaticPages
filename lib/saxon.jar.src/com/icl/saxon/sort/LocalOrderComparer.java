// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   LocalOrderComparer.java

package com.icl.saxon.sort;

import com.icl.saxon.om.NodeInfo;

// Referenced classes of package com.icl.saxon.sort:
//            NodeOrderComparer

public final class LocalOrderComparer
    implements NodeOrderComparer
{

            private static LocalOrderComparer instance = new LocalOrderComparer();

            public LocalOrderComparer()
            {
            }

            public static LocalOrderComparer getInstance()
            {
/*  22*/        return instance;
            }

            public int compare(NodeInfo nodeinfo, NodeInfo nodeinfo1)
            {
/*  26*/        NodeInfo nodeinfo2 = nodeinfo;
/*  27*/        NodeInfo nodeinfo3 = nodeinfo1;
/*  28*/        return nodeinfo2.compareOrder(nodeinfo3);
            }

}
