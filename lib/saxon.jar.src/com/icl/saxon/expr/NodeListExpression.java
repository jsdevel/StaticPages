// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NodeListExpression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.om.NodeEnumeration;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            NodeSetExpression, EmptyNodeSet, SingletonNodeSet, XPathException, 
//            Expression

public class NodeListExpression extends NodeSetExpression
{

            private Expression baseExpression;

            public NodeListExpression(Expression expression)
            {
/*  24*/        baseExpression = expression;
            }

            public Expression simplify()
                throws XPathException
            {
/*  33*/        baseExpression = baseExpression.simplify();
/*  34*/        if(baseExpression instanceof EmptyNodeSet)
/*  35*/            return baseExpression;
/*  36*/        if(baseExpression instanceof SingletonNodeSet)
/*  37*/            return baseExpression;
/*  39*/        else
/*  39*/            return this;
            }

            public int getDependencies()
            {
/*  50*/        return baseExpression.getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  63*/        if((getDependencies() & i) != 0)
/*  64*/            return new NodeListExpression(baseExpression.reduce(i, context));
/*  66*/        else
/*  66*/            return this;
            }

            public NodeEnumeration enumerate(Context context, boolean flag)
                throws XPathException
            {
/*  79*/        return baseExpression.enumerate(context, true);
            }

            public void display(int i)
            {
/*  87*/        System.err.println(Expression.indent(i) + "NodeListExpression");
/*  88*/        baseExpression.display(i + 1);
            }
}
