// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   FunctionAvailable.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;

public class FunctionAvailable extends Function
{

            public FunctionAvailable()
            {
            }

            public String getName()
            {
/*  16*/        return "function-available";
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
/*  36*/        if(super.argument[0] instanceof Value)
/*  37*/            return evaluate(null);
/*  39*/        else
/*  39*/            return this;
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  47*/        String s = super.argument[0].evaluateAsString(context);
/*  48*/        return getStaticContext().isFunctionAvailable(s);
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  57*/        return new BooleanValue(evaluateAsBoolean(context));
            }

            public int getDependencies()
            {
/*  65*/        return super.argument[0].getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  73*/        FunctionAvailable functionavailable = new FunctionAvailable();
/*  74*/        functionavailable.addArgument(super.argument[0].reduce(i, context));
/*  75*/        functionavailable.setStaticContext(getStaticContext());
/*  76*/        return functionavailable.simplify();
            }
}
