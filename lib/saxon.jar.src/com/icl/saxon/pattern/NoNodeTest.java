// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NoNodeTest.java

package com.icl.saxon.pattern;

import com.icl.saxon.om.NodeInfo;

// Referenced classes of package com.icl.saxon.pattern:
//            NodeTest

public final class NoNodeTest extends NodeTest
{

            private static NoNodeTest instance = new NoNodeTest();

            public NoNodeTest()
            {
            }

            public static NoNodeTest getInstance()
            {
/*  21*/        return instance;
            }

            public final short getNodeType()
            {
/*  25*/        return 9999;
            }

            public final boolean matches(NodeInfo nodeinfo)
            {
/*  33*/        return false;
            }

            public boolean matches(short word0, int i)
            {
/*  43*/        return false;
            }

            public final double getDefaultPriority()
            {
/*  51*/        return -0.5D;
            }

}
