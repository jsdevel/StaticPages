// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SingletonEnumeration.java

package com.icl.saxon.om;


// Referenced classes of package com.icl.saxon.om:
//            AxisEnumeration, NodeInfo

public class SingletonEnumeration
    implements AxisEnumeration
{

            private NodeInfo theNode;
            private boolean gone;
            private int count;

            public SingletonEnumeration(NodeInfo nodeinfo)
            {
/*  14*/        theNode = nodeinfo;
/*  15*/        gone = nodeinfo == null;
/*  16*/        count = nodeinfo != null ? 1 : 0;
            }

            public boolean hasMoreElements()
            {
/*  20*/        return !gone;
            }

            public NodeInfo nextElement()
            {
/*  24*/        gone = true;
/*  25*/        return theNode;
            }

            public boolean isSorted()
            {
/*  29*/        return true;
            }

            public boolean isReverseSorted()
            {
/*  33*/        return true;
            }

            public boolean isPeer()
            {
/*  37*/        return true;
            }

            public int getLastPosition()
            {
/*  41*/        return count;
            }
}
