// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SubstringBefore.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;

public class SubstringBefore extends Function
{

            public SubstringBefore()
            {
            }

            public String getName()
            {
/*  12*/        return "substring-before";
            }

            public int getDataType()
            {
/*  21*/        return 3;
            }

            public Expression simplify()
                throws XPathException
            {
/*  29*/        int i = checkArgumentCount(2, 2);
/*  30*/        super.argument[0] = super.argument[0].simplify();
/*  31*/        super.argument[1] = super.argument[1].simplify();
/*  32*/        boolean flag = (super.argument[0] instanceof Value) && (super.argument[1] instanceof Value);
/*  34*/        if(flag)
/*  35*/            return evaluate(null);
/*  37*/        else
/*  37*/            return this;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  46*/        String s = super.argument[0].evaluateAsString(context);
/*  47*/        String s1 = super.argument[1].evaluateAsString(context);
/*  48*/        return before(s, s1);
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  56*/        return new StringValue(evaluateAsString(context));
            }

            public int getDependencies()
            {
/*  65*/        int i = super.argument[0].getDependencies() | super.argument[1].getDependencies();
/*  66*/        return i;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  74*/        SubstringBefore substringbefore = new SubstringBefore();
/*  75*/        substringbefore.addArgument(super.argument[0].reduce(i, context));
/*  76*/        substringbefore.addArgument(super.argument[1].reduce(i, context));
/*  77*/        substringbefore.setStaticContext(getStaticContext());
/*  78*/        return substringbefore.simplify();
            }

            private static String before(String s, String s1)
            {
/*  86*/        int i = s.indexOf(s1);
/*  87*/        if(i < 0)
/*  87*/            return "";
/*  88*/        else
/*  88*/            return s.substring(0, i);
            }
}
