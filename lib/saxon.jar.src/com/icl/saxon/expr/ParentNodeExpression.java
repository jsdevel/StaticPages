// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ParentNodeExpression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.om.NodeInfo;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            SingletonExpression, SingletonNodeSet, XPathException, Expression

public class ParentNodeExpression extends SingletonExpression
{

            public ParentNodeExpression()
            {
            }

            public NodeInfo getNode(Context context)
                throws XPathException
            {
/*  18*/        return context.getContextNodeInfo().getParent();
            }

            public int getDependencies()
            {
/*  28*/        return 8;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  38*/        NodeInfo nodeinfo = context.getContextNodeInfo().getParent();
/*  39*/        if(nodeinfo == null)
/*  39*/            return "";
/*  40*/        else
/*  40*/            return nodeinfo.getStringValue();
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  51*/        return context.getContextNodeInfo().getParent() != null;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  64*/        if((i & 8) != 0)
/*  65*/            return new SingletonNodeSet(context.getContextNodeInfo().getParent());
/*  67*/        else
/*  67*/            return this;
            }

            public void display(int i)
            {
/*  76*/        System.err.println(Expression.indent(i) + "..");
            }
}
