// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Axis.java

package com.icl.saxon.om;

import com.icl.saxon.expr.XPathException;

public final class Axis
{

            public static final byte ANCESTOR = 0;
            public static final byte ANCESTOR_OR_SELF = 1;
            public static final byte ATTRIBUTE = 2;
            public static final byte CHILD = 3;
            public static final byte DESCENDANT = 4;
            public static final byte DESCENDANT_OR_SELF = 5;
            public static final byte FOLLOWING = 6;
            public static final byte FOLLOWING_SIBLING = 7;
            public static final byte NAMESPACE = 8;
            public static final byte PARENT = 9;
            public static final byte PRECEDING = 10;
            public static final byte PRECEDING_SIBLING = 11;
            public static final byte SELF = 12;
            public static final byte PRECEDING_OR_ANCESTOR = 13;
            public static final short principalNodeType[] = {
/*  38*/        1, 1, 2, 1, 1, 1, 1, 1, 13, 1, 
/*  38*/        1, 1, 1, 1
            };
            public static final boolean isForwards[] = {
/*  60*/        false, false, true, true, true, true, true, true, false, true, 
/*  60*/        false, false, true, false
            };
            public static final boolean isReverse[] = {
/*  82*/        true, true, false, false, false, false, false, false, false, true, 
/*  82*/        true, true, true, true
            };
            public static final boolean isPeerAxis[] = {
/* 105*/        false, false, true, true, false, false, false, true, false, true, 
/* 105*/        false, true, true, false
            };
            public static final boolean isSubtreeAxis[] = {
/* 128*/        false, false, true, true, true, true, false, false, false, false, 
/* 128*/        false, false, true, false
            };
            public static final String axisName[] = {
/* 150*/        "ancestor", "ancestor-or-self", "attribute", "child", "descendant", "descendant-or-self", "following", "following-sibling", "namespace", "parent", 
/* 150*/        "preceding", "preceding-sibling", "self", "preceding-or-ancestor"
            };

            public Axis()
            {
            }

            public static byte getAxisNumber(String s)
                throws XPathException
            {
/* 173*/        if(s.equals("ancestor"))
/* 173*/            return 0;
/* 174*/        if(s.equals("ancestor-or-self"))
/* 174*/            return 1;
/* 175*/        if(s.equals("attribute"))
/* 175*/            return 2;
/* 176*/        if(s.equals("child"))
/* 176*/            return 3;
/* 177*/        if(s.equals("descendant"))
/* 177*/            return 4;
/* 178*/        if(s.equals("descendant-or-self"))
/* 178*/            return 5;
/* 179*/        if(s.equals("following"))
/* 179*/            return 6;
/* 180*/        if(s.equals("following-sibling"))
/* 180*/            return 7;
/* 181*/        if(s.equals("namespace"))
/* 181*/            return 8;
/* 182*/        if(s.equals("parent"))
/* 182*/            return 9;
/* 183*/        if(s.equals("preceding"))
/* 183*/            return 10;
/* 184*/        if(s.equals("preceding-sibling"))
/* 184*/            return 11;
/* 185*/        if(s.equals("self"))
/* 185*/            return 12;
/* 187*/        else
/* 187*/            throw new XPathException("Unknown axis name: " + s);
            }

}
