// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Not.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;

public class Not extends Function
{

            public Not()
            {
            }

            public String getName()
            {
/*  16*/        return "not";
            }

            public int getDataType()
            {
/*  25*/        return 1;
            }

            public Expression simplify()
                throws XPathException
            {
/*  34*/        checkArgumentCount(1, 1);
/*  35*/        super.argument[0] = super.argument[0].simplify();
/*  37*/        if(super.argument[0] instanceof Value)
/*  38*/            return new BooleanValue(!((Value)super.argument[0]).asBoolean());
/*  40*/        else
/*  40*/            return this;
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  48*/        return !super.argument[0].evaluateAsBoolean(context);
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  56*/        return new BooleanValue(evaluateAsBoolean(context));
            }

            public int getDependencies()
            {
/*  64*/        return super.argument[0].getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  72*/        Not not = new Not();
/*  73*/        not.addArgument(super.argument[0].reduce(i, context));
/*  74*/        not.setStaticContext(getStaticContext());
/*  75*/        return not.simplify();
            }
}
