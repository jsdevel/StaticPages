// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NamespaceTest.java

package com.icl.saxon.pattern;

import com.icl.saxon.om.NamePool;
import com.icl.saxon.om.NodeInfo;

// Referenced classes of package com.icl.saxon.pattern:
//            NodeTest

public final class NamespaceTest extends NodeTest
{

            private NamePool namePool;
            private short type;
            private short uriCode;

            public NamespaceTest(NamePool namepool, short word0, short word1)
            {
/*  20*/        namePool = namepool;
/*  21*/        type = word0;
/*  22*/        uriCode = word1;
            }

            public final boolean matches(NodeInfo nodeinfo)
            {
/*  30*/        int i = nodeinfo.getFingerprint();
/*  31*/        if(i == -1)
/*  31*/            return false;
/*  32*/        else
/*  32*/            return type == nodeinfo.getNodeType() && uriCode == namePool.getURICode(i);
            }

            public boolean matches(short word0, int i)
            {
/*  43*/        if(i == -1)
/*  43*/            return false;
/*  44*/        if(word0 != type)
/*  44*/            return false;
/*  45*/        else
/*  45*/            return uriCode == namePool.getURICode(i);
            }

            public final double getDefaultPriority()
            {
/*  53*/        return -0.25D;
            }

            public short getNodeType()
            {
/*  63*/        return type;
            }
}
