// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   RootExpression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.om.NodeInfo;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            SingletonExpression, SingletonNodeSet, XPathException, Expression

public class RootExpression extends SingletonExpression
{

            public RootExpression()
            {
            }

            public Expression simplify()
                throws XPathException
            {
/*  20*/        return this;
            }

            public NodeInfo getNode(Context context)
                throws XPathException
            {
/*  31*/        return context.getContextNodeInfo().getDocumentRoot();
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  41*/        return context.getContextNodeInfo().getDocumentRoot().getStringValue();
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  51*/        return true;
            }

            public int getDependencies()
            {
/*  61*/        return 136;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  74*/        if((i & 0x88) != 0)
/*  75*/            return new SingletonNodeSet(context.getContextNodeInfo().getDocumentRoot());
/*  78*/        else
/*  78*/            return this;
            }

            public void display(int i)
            {
/*  87*/        System.err.println(Expression.indent(i) + "root()");
            }
}
