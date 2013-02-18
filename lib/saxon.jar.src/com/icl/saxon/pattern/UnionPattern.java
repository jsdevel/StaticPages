// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   UnionPattern.java

package com.icl.saxon.pattern;

import com.icl.saxon.Context;
import com.icl.saxon.expr.XPathException;
import com.icl.saxon.om.NodeInfo;

// Referenced classes of package com.icl.saxon.pattern:
//            Pattern

public class UnionPattern extends Pattern
{

            protected Pattern p1;
            protected Pattern p2;
            private short nodeType;

            public UnionPattern(Pattern pattern, Pattern pattern1)
            {
/*  13*/        nodeType = 0;
/*  22*/        p1 = pattern;
/*  23*/        p2 = pattern1;
/*  24*/        if(pattern.getNodeType() == pattern1.getNodeType())
/*  24*/            nodeType = pattern.getNodeType();
            }

            public Pattern simplify()
                throws XPathException
            {
/*  32*/        return new UnionPattern(p1.simplify(), p2.simplify());
            }

            public void setOriginalText(String s)
            {
/*  40*/        super.originalText = s;
/*  41*/        p1.setOriginalText(s);
/*  42*/        p2.setOriginalText(s);
            }

            public boolean matches(NodeInfo nodeinfo, Context context)
                throws XPathException
            {
/*  52*/        return p1.matches(nodeinfo, context) || p2.matches(nodeinfo, context);
            }

            public short getNodeType()
            {
/*  62*/        return nodeType;
            }

            public Pattern getLHS()
            {
/*  70*/        return p1;
            }

            public Pattern getRHS()
            {
/*  78*/        return p2;
            }
}
