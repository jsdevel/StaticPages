// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ContextNodeExpression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.om.NodeInfo;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            SingletonExpression, SingletonNodeSet, XPathException, Expression

public final class ContextNodeExpression extends SingletonExpression
{

            public ContextNodeExpression()
            {
            }

            public NodeInfo getNode(Context context)
                throws XPathException
            {
/*  20*/        return context.getContextNodeInfo();
            }

            public int getDependencies()
            {
/*  30*/        return 8;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  43*/        if((i & 8) != 0)
/*  44*/            return new SingletonNodeSet(context.getContextNodeInfo());
/*  46*/        else
/*  46*/            return this;
            }

            public void display(int i)
            {
/*  55*/        System.err.println(Expression.indent(i) + ".");
            }
}
