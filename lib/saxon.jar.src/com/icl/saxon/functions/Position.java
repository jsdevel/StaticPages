// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Position.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;

public class Position extends Function
{

            public Position()
            {
            }

            public String getName()
            {
/*  15*/        return "position";
            }

            public int getDataType()
            {
/*  24*/        return 2;
            }

            public Expression simplify()
                throws XPathException
            {
/*  32*/        checkArgumentCount(0, 0);
/*  33*/        return this;
            }

            public double evaluateAsNumber(Context context)
                throws XPathException
            {
/*  41*/        return (double)context.getContextPosition();
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  49*/        return new NumericValue(evaluateAsNumber(context));
            }

            public int getDependencies()
            {
/*  57*/        return 16;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  65*/        if((i & 0x10) != 0)
/*  66*/            return new NumericValue(context.getContextPosition());
/*  68*/        else
/*  68*/            return this;
            }
}
