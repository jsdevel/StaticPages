// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Lang.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeInfo;

public class Lang extends Function
{

            public Lang()
            {
            }

            public String getName()
            {
/*  21*/        return "lang";
            }

            public int getDataType()
            {
/*  30*/        return 1;
            }

            public Expression simplify()
                throws XPathException
            {
/*  38*/        checkArgumentCount(1, 1);
/*  39*/        super.argument[0] = super.argument[0].simplify();
/*  40*/        return this;
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  48*/        return isLang(super.argument[0].evaluateAsString(context), context);
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  56*/        return new BooleanValue(evaluateAsBoolean(context));
            }

            public int getDependencies()
            {
/*  64*/        return 8 | super.argument[0].getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  72*/        Lang lang = new Lang();
/*  73*/        lang.addArgument(super.argument[0].reduce(i, context));
/*  74*/        lang.setStaticContext(getStaticContext());
/*  75*/        return lang.simplify();
            }

            private static boolean isLang(String s, Context context)
                throws XPathException
            {
/*  86*/        NodeInfo nodeinfo = context.getContextNodeInfo();
/*  88*/        String s1 = null;
/*  91*/        for(; nodeinfo != null; nodeinfo = nodeinfo.getParent())
                {
/*  91*/            s1 = nodeinfo.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");
/*  92*/            if(s1 != null)
/*  92*/                break;
                }

/*  96*/        if(s1 == null)
/*  96*/            return false;
/*  98*/        if(s.equalsIgnoreCase(s1))
/*  98*/            return true;
/*  99*/        int i = s1.indexOf("-");
/* 100*/        if(i < 0)
/* 100*/            return false;
/* 101*/        s1 = s1.substring(0, i);
/* 102*/        return s.equalsIgnoreCase(s1);
            }
}
