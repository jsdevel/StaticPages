// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   IsLastExpression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            Expression, BooleanValue, XPathException, Value

public final class IsLastExpression extends Expression
{

            private boolean condition;

            public IsLastExpression(boolean flag)
            {
/*  19*/        condition = flag;
            }

            public boolean getCondition()
            {
/*  23*/        return condition;
            }

            public Expression simplify()
            {
/*  27*/        return this;
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  31*/        return new BooleanValue(evaluateAsBoolean(context));
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  35*/        return condition == context.isAtLast();
            }

            public int getDataType()
            {
/*  44*/        return 1;
            }

            public int getDependencies()
            {
/*  52*/        return 48;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  65*/        if((0x30 & i) != 0)
/*  66*/            return new BooleanValue(context.isAtLast());
/*  68*/        else
/*  68*/            return this;
            }

            public void display(int i)
            {
/*  77*/        System.err.println(Expression.indent(i) + "isLast()");
            }
}
