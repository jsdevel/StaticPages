// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   UnionExpression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.om.NodeEnumeration;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            NodeSetExpression, EmptyNodeSet, UnionEnumeration, XPathException, 
//            Expression

class UnionExpression extends NodeSetExpression
{

            protected Expression p1;
            protected Expression p2;

            public UnionExpression(Expression expression, Expression expression1)
            {
/*  24*/        p1 = expression;
/*  25*/        p2 = expression1;
            }

            public Expression simplify()
                throws XPathException
            {
/*  34*/        p1 = p1.simplify();
/*  35*/        p2 = p2.simplify();
/*  36*/        if(p1 instanceof EmptyNodeSet)
/*  36*/            return p2;
/*  37*/        if(p2 instanceof EmptyNodeSet)
/*  37*/            return p1;
/*  38*/        else
/*  38*/            return this;
            }

            public NodeEnumeration enumerate(Context context, boolean flag)
                throws XPathException
            {
/*  50*/        return new UnionEnumeration(p1.enumerate(context, true), p2.enumerate(context, true), context.getController());
            }

            public int getDependencies()
            {
/*  62*/        return p1.getDependencies() | p2.getDependencies();
            }

            public boolean isContextDocumentNodeSet()
            {
/*  72*/        return p1.isContextDocumentNodeSet() && p2.isContextDocumentNodeSet();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  85*/        if((getDependencies() & i) != 0)
                {
/*  86*/            UnionExpression unionexpression = new UnionExpression(p1.reduce(i, context), p2.reduce(i, context));
/*  89*/            unionexpression.setStaticContext(getStaticContext());
/*  90*/            return unionexpression;
                } else
                {
/*  92*/            return this;
                }
            }

            public void display(int i)
            {
/* 101*/        System.err.println(Expression.indent(i) + "union");
/* 102*/        p1.display(i + 1);
/* 103*/        p2.display(i + 1);
            }
}
