// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NodeTest.java

package com.icl.saxon.pattern;

import com.icl.saxon.Context;
import com.icl.saxon.om.NodeInfo;

// Referenced classes of package com.icl.saxon.pattern:
//            Pattern

public abstract class NodeTest extends Pattern
{

            public NodeTest()
            {
            }

            public abstract boolean matches(NodeInfo nodeinfo);

            public abstract boolean matches(short word0, int i);

            public final boolean matches(NodeInfo nodeinfo, Context context)
            {
/*  39*/        return matches(nodeinfo);
            }
}
