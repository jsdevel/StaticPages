// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   BinaryExpression.java

package com.icl.saxon.expr;

import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            Expression, Value, XPathException, Tokenizer

abstract class BinaryExpression extends Expression
{

            protected Expression p1;
            protected Expression p2;
            protected int operator;

            public BinaryExpression()
            {
            }

            public BinaryExpression(Expression expression, int i, Expression expression1)
            {
/*  29*/        p1 = expression;
/*  30*/        p2 = expression1;
/*  31*/        operator = i;
            }

            public void setDetails(Expression expression, int i, Expression expression1)
            {
/*  42*/        p1 = expression;
/*  43*/        p2 = expression1;
/*  44*/        operator = i;
            }

            public Expression simplify()
                throws XPathException
            {
/*  53*/        p1 = p1.simplify();
/*  54*/        p2 = p2.simplify();
/*  57*/        if((p1 instanceof Value) && (p2 instanceof Value))
/*  58*/            return evaluate(null);
/*  60*/        else
/*  60*/            return this;
            }

            public int getDependencies()
            {
/*  70*/        return p1.getDependencies() | p2.getDependencies();
            }

            public void display(int i)
            {
/*  78*/        System.err.println(Expression.indent(i) + Tokenizer.tokens[operator]);
/*  79*/        p1.display(i + 1);
/*  80*/        p2.display(i + 1);
            }
}
