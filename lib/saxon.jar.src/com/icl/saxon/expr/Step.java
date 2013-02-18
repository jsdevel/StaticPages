// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Step.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.functions.Last;
import com.icl.saxon.om.*;
import com.icl.saxon.pattern.NodeTest;
import com.icl.saxon.pattern.Pattern;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            Expression, Value, NumericValue, IsLastExpression, 
//            FilterEnumerator, XPathException

public final class Step
{

            private byte axis;
            private NodeTest test;
            private Expression filters[];
            private int numberOfFilters;

            public Step(byte byte0, NodeTest nodetest)
            {
/*  18*/        filters = new Expression[3];
/*  19*/        numberOfFilters = 0;
/*  22*/        axis = byte0;
/*  23*/        test = nodetest;
            }

            public Step addFilter(Expression expression)
            {
/*  27*/        if(numberOfFilters == filters.length)
                {
/*  28*/            Expression aexpression[] = new Expression[numberOfFilters * 2];
/*  29*/            System.arraycopy(filters, 0, aexpression, 0, numberOfFilters);
/*  30*/            filters = aexpression;
                }
/*  32*/        filters[numberOfFilters++] = expression;
/*  33*/        return this;
            }

            public void setFilters(Expression aexpression[], int i)
            {
/*  37*/        filters = aexpression;
/*  38*/        numberOfFilters = i;
            }

            public byte getAxis()
            {
/*  42*/        return axis;
            }

            public NodeTest getNodeTest()
            {
/*  46*/        return test;
            }

            public Expression[] getFilters()
            {
/*  50*/        return filters;
            }

            public int getNumberOfFilters()
            {
/*  54*/        return numberOfFilters;
            }

            public Step simplify()
                throws XPathException
            {
/*  64*/        for(int i = numberOfFilters - 1; i >= 0; i--)
                {
/*  65*/            Expression expression = filters[i].simplify();
/*  66*/            filters[i] = expression;
/*  71*/            if((expression instanceof Value) && !(expression instanceof NumericValue))
/*  72*/                if(((Value)expression).asBoolean())
                        {
/*  74*/                    if(i == numberOfFilters - 1)
/*  75*/                        numberOfFilters--;
                        } else
                        {
/*  79*/                    return null;
                        }
/*  85*/            if(expression instanceof Last)
/*  86*/                filters[i] = new IsLastExpression(true);
                }

/*  90*/        return this;
            }

            public NodeEnumeration enumerate(NodeInfo nodeinfo, Context context)
                throws XPathException
            {
/* 104*/        Object obj = nodeinfo.getEnumeration(axis, test);
/* 105*/        if(((NodeEnumeration) (obj)).hasMoreElements())
                {
/* 106*/            for(int i = 0; i < numberOfFilters; i++)
/* 107*/                obj = new FilterEnumerator(((NodeEnumeration) (obj)), filters[i], context, false);

                }
/* 111*/        return ((NodeEnumeration) (obj));
            }

            public void display(int i)
            {
/* 120*/        System.err.println(Expression.indent(i) + "Step " + Axis.axisName[axis] + "::" + test.toString() + (numberOfFilters <= 0 ? "" : " ["));
/* 122*/        for(int j = 0; j < numberOfFilters; j++)
/* 123*/            filters[j].display(i + 1);

            }
}
