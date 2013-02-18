// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ErrorExpression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            Expression, XPathException, Value

public class ErrorExpression extends Expression
{

            private XPathException exception;

            public ErrorExpression(XPathException xpathexception)
            {
/*  22*/        exception = xpathexception;
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  31*/        throw exception;
            }

            public int getDataType()
            {
/*  40*/        return -1;
            }

            public int getDependencies()
            {
/*  50*/        return 0;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  63*/        return this;
            }

            public void display(int i)
            {
/*  71*/        System.err.println(Expression.indent(i) + "**ERROR** (" + exception.getMessage() + ")");
            }
}
