// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   PositionRange.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            Expression, BooleanValue, XPathException, Value

class PositionRange extends Expression
{

            private int minPosition;
            private int maxPosition;

            public PositionRange(int i, int j)
            {
/*  21*/        minPosition = i;
/*  22*/        maxPosition = j;
            }

            public Expression simplify()
                throws XPathException
            {
/*  31*/        return this;
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  41*/        return new BooleanValue(evaluateAsBoolean(context));
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  51*/        int i = context.getContextPosition();
/*  52*/        return i >= minPosition && i <= maxPosition;
            }

            public int getDataType()
            {
/*  61*/        return 1;
            }

            public int getDependencies()
            {
/*  69*/        return 16;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  83*/        if((0x10 & i) != 0)
/*  84*/            return evaluate(context);
/*  86*/        else
/*  86*/            return this;
            }

            protected int getMinPosition()
            {
/*  94*/        return minPosition;
            }

            protected int getMaxPosition()
            {
/* 102*/        return maxPosition;
            }

            public void display(int i)
            {
/* 110*/        System.err.println(Expression.indent(i) + "positionRange(" + minPosition + "," + maxPosition + ")");
            }
}
