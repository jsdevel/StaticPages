// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   BooleanFn.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;

public class BooleanFn extends Function
{

            public BooleanFn()
            {
            }

            public String getName()
            {
/*  17*/        return "boolean";
            }

            public int getDataType()
            {
/*  26*/        return 1;
            }

            public Expression simplify()
                throws XPathException
            {
/*  35*/        checkArgumentCount(1, 1);
/*  36*/        super.argument[0] = super.argument[0].simplify();
/*  37*/        if(super.argument[0].getDataType() == 1)
/*  38*/            return super.argument[0];
/*  40*/        if(super.argument[0] instanceof Value)
/*  41*/            return new BooleanValue(((Value)super.argument[0]).asBoolean());
/*  43*/        else
/*  43*/            return this;
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  51*/        return super.argument[0].evaluateAsBoolean(context);
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  59*/        return new BooleanValue(evaluateAsBoolean(context));
            }

            public int getDependencies()
            {
/*  67*/        return super.argument[0].getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  75*/        BooleanFn booleanfn = new BooleanFn();
/*  76*/        booleanfn.addArgument(super.argument[0].reduce(i, context));
/*  77*/        booleanfn.setStaticContext(getStaticContext());
/*  78*/        return booleanfn.simplify();
            }
}
