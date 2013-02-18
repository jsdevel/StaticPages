// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NodeTypeTest.java

package com.icl.saxon.pattern;

import com.icl.saxon.om.NodeInfo;

// Referenced classes of package com.icl.saxon.pattern:
//            NodeTest, Pattern

public class NodeTypeTest extends NodeTest
{

            private short type;

            public NodeTypeTest(short word0)
            {
/*  17*/        type = word0;
/*  18*/        switch(word0)
                {
/*  20*/        case 9: // '\t'
/*  20*/            super.originalText = "/";
                    break;

/*  24*/        case 1: // '\001'
/*  24*/        case 2: // '\002'
/*  24*/            super.originalText = "*";
                    break;

/*  27*/        case 8: // '\b'
/*  27*/            super.originalText = "comment()";
                    break;

/*  30*/        case 3: // '\003'
/*  30*/            super.originalText = "text()";
                    break;

/*  33*/        case 7: // '\007'
/*  33*/            super.originalText = "processing-instruction()";
                    break;

/*  36*/        case 13: // '\r'
/*  36*/            super.originalText = "namespace()";
                    break;
                }
            }

            public final boolean matches(NodeInfo nodeinfo)
            {
/*  46*/        return type == nodeinfo.getNodeType();
            }

            public boolean matches(short word0, int i)
            {
/*  56*/        return type == word0;
            }

            public final double getDefaultPriority()
            {
/*  64*/        return -0.5D;
            }

            public short getNodeType()
            {
/*  73*/        return type;
            }
}
