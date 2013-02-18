// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   AnyNodeTest.java

package com.icl.saxon.pattern;

import com.icl.saxon.om.NodeInfo;

// Referenced classes of package com.icl.saxon.pattern:
//            NodeTest, Pattern

public final class AnyNodeTest extends NodeTest
{

            static AnyNodeTest instance = new AnyNodeTest();

            public AnyNodeTest()
            {
/*  16*/        super.originalText = "node()";
            }

            public static AnyNodeTest getInstance()
            {
/*  24*/        return instance;
            }

            public final boolean matches(NodeInfo nodeinfo)
            {
/*  32*/        return true;
            }

            public final boolean matches(short word0, int i)
            {
/*  42*/        return true;
            }

            public final double getDefaultPriority()
            {
/*  50*/        return -0.5D;
            }

}
