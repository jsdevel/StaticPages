// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StartsWith.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;

public class StartsWith extends Function
{

            public StartsWith()
            {
            }

            public String getName()
            {
/*  17*/        return "starts-with";
            }

            public int getDataType()
            {
/*  26*/        return 1;
            }

            public Expression simplify()
                throws XPathException
            {
/*  35*/        checkArgumentCount(2, 2);
/*  36*/        super.argument[0] = super.argument[0].simplify();
/*  37*/        super.argument[1] = super.argument[1].simplify();
/*  39*/        if((super.argument[0] instanceof Value) && (super.argument[1] instanceof Value))
/*  40*/            return evaluate(null);
/*  43*/        if((super.argument[1] instanceof Value) && ((Value)super.argument[1]).asString().equals(""))
/*  45*/            return new BooleanValue(true);
/*  48*/        else
/*  48*/            return this;
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  56*/        String s = super.argument[0].evaluateAsString(context);
/*  57*/        String s1 = super.argument[1].evaluateAsString(context);
/*  58*/        return s.startsWith(s1);
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  66*/        return new BooleanValue(evaluateAsBoolean(context));
            }

            public int getDependencies()
            {
/*  74*/        return super.argument[0].getDependencies() | super.argument[1].getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  82*/        StartsWith startswith = new StartsWith();
/*  83*/        startswith.addArgument(super.argument[0].reduce(i, context));
/*  84*/        startswith.addArgument(super.argument[1].reduce(i, context));
/*  85*/        startswith.setStaticContext(getStaticContext());
/*  86*/        return startswith.simplify();
            }
}
