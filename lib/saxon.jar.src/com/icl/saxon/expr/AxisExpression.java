// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   AxisExpression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.om.*;
import com.icl.saxon.pattern.NodeTest;
import com.icl.saxon.pattern.Pattern;
import com.icl.saxon.sort.LocalOrderComparer;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            NodeSetExpression, NodeSetExtent, NodeSetIntent, XPathException, 
//            Expression, Value

final class AxisExpression extends NodeSetExpression
{

            private byte axis;
            private NodeTest test;
            private NodeInfo contextNode;

            public AxisExpression(byte byte0, NodeTest nodetest)
            {
/*  21*/        contextNode = null;
/*  32*/        axis = byte0;
/*  33*/        test = nodetest;
            }

            public Expression simplify()
            {
/*  42*/        return this;
            }

            public int getDependencies()
            {
/*  52*/        return contextNode != null ? 0 : 8;
            }

            public boolean isContextDocumentNodeSet()
            {
/*  66*/        return true;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  79*/        if(contextNode == null && (i & 8) != 0)
                {
/*  80*/            AxisExpression axisexpression = new AxisExpression(axis, test);
/*  81*/            axisexpression.contextNode = context.getContextNodeInfo();
/*  82*/            return axisexpression;
                } else
                {
/*  84*/            return this;
                }
            }

            public NodeEnumeration enumerate(Context context, boolean flag)
                throws XPathException
            {
                NodeInfo nodeinfo;
/*  96*/        if(contextNode == null)
/*  97*/            nodeinfo = context.getContextNodeInfo();
/*  99*/        else
/*  99*/            nodeinfo = contextNode;
/* 101*/        com.icl.saxon.om.AxisEnumeration axisenumeration = nodeinfo.getEnumeration(axis, test);
/* 102*/        if(flag && !axisenumeration.isSorted())
                {
/* 103*/            NodeSetExtent nodesetextent = new NodeSetExtent(axisenumeration, LocalOrderComparer.getInstance());
/* 104*/            nodesetextent.sort();
/* 105*/            return nodesetextent.enumerate();
                } else
                {
/* 107*/            return axisenumeration;
                }
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/* 116*/        NodeSetIntent nodesetintent = new NodeSetIntent((NodeSetExpression)reduce(8, context), context.getController());
/* 119*/        nodesetintent.setSorted(Axis.isForwards[axis]);
/* 120*/        return nodesetintent;
            }

            public void display(int i)
            {
/* 128*/        System.err.println(Expression.indent(i) + Axis.axisName[axis] + "::" + test.toString());
            }
}
