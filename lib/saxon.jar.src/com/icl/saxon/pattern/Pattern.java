// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Pattern.java

package com.icl.saxon.pattern;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeInfo;

public abstract class Pattern
{

            protected StaticContext staticContext;
            protected String originalText;

            public Pattern()
            {
            }

            public static Pattern make(String s, StaticContext staticcontext)
                throws XPathException
            {
/*  29*/        Pattern pattern = (new ExpressionParser()).parsePattern(s, staticcontext).simplify();
/*  31*/        pattern.staticContext = staticcontext;
/*  34*/        pattern.setOriginalText(s);
/*  35*/        return pattern;
            }

            public void setOriginalText(String s)
            {
/*  43*/        originalText = s;
            }

            public Pattern simplify()
                throws XPathException
            {
/*  53*/        return this;
            }

            public final void setStaticContext(StaticContext staticcontext)
            {
/*  61*/        staticContext = staticcontext;
            }

            public StaticContext getStaticContext()
            {
/*  69*/        return staticContext;
            }

            public abstract boolean matches(NodeInfo nodeinfo, Context context)
                throws XPathException;

            public short getNodeType()
            {
/*  89*/        return 0;
            }

            public int getFingerprint()
            {
/* 100*/        return -1;
            }

            public double getDefaultPriority()
            {
/* 109*/        return 0.5D;
            }

            public String getSystemId()
            {
/* 117*/        return staticContext.getSystemId();
            }

            public int getLineNumber()
            {
/* 125*/        return staticContext.getLineNumber();
            }

            public String toString()
            {
/* 133*/        return originalText;
            }
}
