// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Ceiling.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;

public class Ceiling extends Function
{

            public Ceiling()
            {
            }

            public String getName()
            {
/*  19*/        return "ceiling";
            }

            public int getDataType()
            {
/*  28*/        return 2;
            }

            public Expression simplify()
                throws XPathException
            {
/*  37*/        checkArgumentCount(1, 1);
/*  38*/        super.argument[0] = super.argument[0].simplify();
/*  39*/        if(super.argument[0] instanceof Value)
/*  40*/            return evaluate(null);
/*  42*/        else
/*  42*/            return this;
            }

            public double evaluateAsNumber(Context context)
                throws XPathException
            {
/*  50*/        return Math.ceil(super.argument[0].evaluateAsNumber(context));
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  58*/        return new NumericValue(evaluateAsNumber(context));
            }

            public int getDependencies()
            {
/*  66*/        return super.argument[0].getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  74*/        Ceiling ceiling = new Ceiling();
/*  75*/        ceiling.addArgument(super.argument[0].reduce(i, context));
/*  76*/        ceiling.setStaticContext(getStaticContext());
/*  77*/        return ceiling.simplify();
            }
}
