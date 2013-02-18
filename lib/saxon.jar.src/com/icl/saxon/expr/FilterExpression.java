// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   FilterExpression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.functions.Last;
import com.icl.saxon.om.NodeEnumeration;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            NodeSetExpression, EmptyNodeSet, Value, NumericValue, 
//            IsLastExpression, NodeSetIntent, PositionRange, SingletonNodeSet, 
//            XPathException, FilterEnumerator, Expression

class FilterExpression extends NodeSetExpression
{

            private Expression start;
            private Expression filter;
            private int dependencies;

            public FilterExpression(Expression expression, Expression expression1)
            {
/*  16*/        dependencies = -1;
/*  26*/        start = expression;
/*  27*/        filter = expression1;
            }

            public Expression simplify()
                throws XPathException
            {
/*  36*/        start = start.simplify();
/*  37*/        filter = filter.simplify();
/*  40*/        if(start instanceof EmptyNodeSet)
/*  41*/            return start;
/*  45*/        if((filter instanceof Value) && !(filter instanceof NumericValue))
                {
/*  46*/            boolean flag = ((Value)filter).asBoolean();
/*  47*/            if(flag)
/*  48*/                return start;
/*  50*/            else
/*  50*/                return new EmptyNodeSet();
                }
/*  57*/        if(filter instanceof Last)
/*  58*/            filter = new IsLastExpression(true);
/*  66*/        if((start instanceof NodeSetIntent) && (filter instanceof PositionRange))
                {
/*  68*/            PositionRange positionrange = (PositionRange)filter;
/*  69*/            if(positionrange.getMinPosition() == 2 && positionrange.getMaxPosition() == 0x7fffffff)
                    {
/*  71*/                NodeSetIntent nodesetintent = (NodeSetIntent)start;
/*  73*/                if(nodesetintent.getNodeSetExpression() instanceof FilterExpression)
                        {
/*  74*/                    FilterExpression filterexpression = (FilterExpression)nodesetintent.getNodeSetExpression();
/*  75*/                    if(filterexpression.filter instanceof PositionRange)
                            {
/*  76*/                        PositionRange positionrange1 = (PositionRange)filterexpression.filter;
/*  77*/                        if(positionrange1.getMaxPosition() == 0x7fffffff)
/*  79*/                            return new FilterExpression(filterexpression.start, new PositionRange(positionrange1.getMinPosition() + 1, 0x7fffffff));
                            }
                        }
                    }
                }
/*  87*/        return this;
            }

            public NodeEnumeration enumerate(Context context, boolean flag)
                throws XPathException
            {
/* 103*/        int i = getDependencies();
/* 104*/        int j = 0;
/* 106*/        if((i & 0x45) != 0)
/* 107*/            j |= 0x45;
/* 110*/        if(start.isContextDocumentNodeSet() && (i & 0x80) != 0)
/* 111*/            j |= 0x80;
/* 114*/        if(j != 0)
/* 115*/            return reduce(j, context).enumerate(context, flag);
/* 118*/        if(!flag && (filter.getDataType() == 2 || filter.getDataType() == -1 || (filter.getDependencies() & 0x30) != 0))
/* 123*/            flag = true;
/* 127*/        if((start instanceof SingletonNodeSet) && !((SingletonNodeSet)start).isGeneralUseAllowed())
/* 129*/            throw new XPathException("To use a result tree fragment in a filter expression, either use exsl:node-set() or specify version='1.1'");
/* 133*/        NodeEnumeration nodeenumeration = start.enumerate(context, flag);
/* 134*/        if(!nodeenumeration.hasMoreElements())
/* 135*/            return nodeenumeration;
/* 138*/        else
/* 138*/            return new FilterEnumerator(nodeenumeration, filter, context, false);
            }

            public int getDependencies()
            {
/* 150*/        if(dependencies == -1)
/* 151*/            dependencies = start.getDependencies() | filter.getDependencies() & 0x45;
/* 155*/        return dependencies;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/* 168*/        if((i & getDependencies()) != 0)
                {
/* 169*/            Expression expression = start.reduce(i, context);
/* 170*/            Expression expression1 = filter.reduce(i & 0x45, context);
/* 171*/            FilterExpression filterexpression = new FilterExpression(expression, expression1);
/* 172*/            filterexpression.setStaticContext(getStaticContext());
/* 173*/            return filterexpression.simplify();
                } else
                {
/* 175*/            return this;
                }
            }

            public boolean isContextDocumentNodeSet()
            {
/* 186*/        return start.isContextDocumentNodeSet();
            }

            public void display(int i)
            {
/* 194*/        System.err.println(Expression.indent(i) + "filter");
/* 195*/        start.display(i + 1);
/* 196*/        filter.display(i + 1);
            }
}
