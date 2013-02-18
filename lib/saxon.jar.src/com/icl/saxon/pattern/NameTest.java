// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NameTest.java

package com.icl.saxon.pattern;

import com.icl.saxon.om.NodeInfo;

// Referenced classes of package com.icl.saxon.pattern:
//            NodeTest

public class NameTest extends NodeTest
{

            private short nodeType;
            private int fingerprint;

            public NameTest(short word0, int i)
            {
/*  20*/        nodeType = word0;
/*  21*/        fingerprint = i & 0xfffff;
/*  22*/        String s = " ";
            }

            public NameTest(NodeInfo nodeinfo)
            {
/*  31*/        nodeType = nodeinfo.getNodeType();
/*  32*/        fingerprint = nodeinfo.getFingerprint();
            }

            public final boolean matches(NodeInfo nodeinfo)
            {
/*  40*/        return fingerprint == nodeinfo.getFingerprint() && nodeType == nodeinfo.getNodeType();
            }

            public boolean matches(short word0, int i)
            {
/*  53*/        return (i & 0xfffff) == fingerprint && word0 == nodeType;
            }

            public final double getDefaultPriority()
            {
/*  62*/        return 0.0D;
            }

            public int getFingerprint()
            {
/*  70*/        return fingerprint;
            }

            public short getNodeType()
            {
/*  80*/        return nodeType;
            }
}
