// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   AnyChildNodePattern.java

package com.icl.saxon.pattern;

import com.icl.saxon.om.NodeInfo;

// Referenced classes of package com.icl.saxon.pattern:
//            NodeTest

public final class AnyChildNodePattern extends NodeTest
{

            public AnyChildNodePattern()
            {
            }

            public boolean matches(NodeInfo nodeinfo)
            {
/*  21*/        short word0 = nodeinfo.getNodeType();
/*  22*/        return word0 == 1 || word0 == 3 || word0 == 8 || word0 == 7;
            }

            public boolean matches(short word0, int i)
            {
/*  35*/        return word0 == 1 || word0 == 3 || word0 == 8 || word0 == 7;
            }

            public short getNodeType()
            {
/*  47*/        return 0;
            }

            public double getDefaultPriority()
            {
/*  56*/        return -0.5D;
            }
}
