// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Last.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;

public class Last extends Function
{

            public Last()
            {
            }

            public String getName()
            {
/*  16*/        return "last";
            }

            public int getDataType()
            {
/*  25*/        return 2;
            }

            public Expression simplify()
                throws XPathException
            {
/*  33*/        checkArgumentCount(0, 0);
/*  34*/        return this;
            }

            public double evaluateAsNumber(Context context)
                throws XPathException
            {
/*  42*/        return (double)context.getLast();
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  50*/        return new NumericValue(evaluateAsNumber(context));
            }

            public int getDependencies()
            {
/*  58*/        return 32;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  66*/        if((i & 0x20) != 0)
/*  67*/            return new NumericValue(context.getLast());
/*  69*/        else
/*  69*/            return this;
            }
}
