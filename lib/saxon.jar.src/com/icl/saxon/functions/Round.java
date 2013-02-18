// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Round.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;

public class Round extends Function
{

            public Round()
            {
            }

            public String getName()
            {
/*  19*/        return "round";
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
/*  50*/        return round(super.argument[0].evaluateAsNumber(context));
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
/*  74*/        Round round1 = new Round();
/*  75*/        round1.addArgument(super.argument[0].reduce(i, context));
/*  76*/        round1.setStaticContext(getStaticContext());
/*  77*/        return round1.simplify();
            }

            public static double round(double d)
            {
/*  85*/        if(Double.isNaN(d))
/*  85*/            return d;
/*  86*/        if(Double.isInfinite(d))
/*  86*/            return d;
/*  87*/        if(d == 0.0D)
/*  87*/            return d;
/*  88*/        if(d > -0.5D && d < 0.0D)
/*  88*/            return -0D;
/*  89*/        if(d > -9.2233720368547758E+18D && d < 9.2233720368547758E+18D)
/*  90*/            return (double)Math.round(d);
/*  92*/        double d1 = d % 1.0D;
/*  93*/        if(d1 < 0.5D)
/*  93*/            return d - d1;
/*  94*/        else
/*  94*/            return (d - d1) + 1.0D;
            }
}
