// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   FilterEnumerator.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;

// Referenced classes of package com.icl.saxon.expr:
//            NumericValue, PositionRange, LastPositionFinder, LookaheadEnumerator, 
//            XPathException, Expression, Value

public class FilterEnumerator
    implements NodeEnumeration
{

            private NodeEnumeration base;
            private Expression filter;
            private int position;
            private int last;
            int min;
            int max;
            private NodeInfo current;
            private Context filterContext;
            private int dataType;
            private boolean positional;
            private boolean finished;
            private boolean finishAfterReject;

            public FilterEnumerator(NodeEnumeration nodeenumeration, Expression expression, Context context, boolean flag)
                throws XPathException
            {
/*  19*/        position = 0;
/*  20*/        last = -1;
/*  21*/        min = 1;
/*  22*/        max = 0x7fffffff;
/*  23*/        current = null;
/*  25*/        dataType = -1;
/*  26*/        positional = false;
/*  27*/        finished = false;
/*  28*/        finishAfterReject = false;
/*  43*/        base = nodeenumeration;
/*  44*/        filter = expression;
/*  45*/        finishAfterReject = flag;
/*  47*/        filterContext = context.newContext();
/*  49*/        dataType = expression.getDataType();
/*  51*/        if(expression instanceof NumericValue)
                {
/*  53*/            double d = ((NumericValue)expression).asNumber();
/*  54*/            if(Math.floor(d) == d)
                    {
/*  55*/                min = (int)d;
/*  56*/                max = min;
/*  57*/                positional = true;
                    } else
                    {
/*  59*/                finished = true;
                    }
                } else
/*  61*/        if(expression instanceof PositionRange)
                {
/*  62*/            min = ((PositionRange)expression).getMinPosition();
/*  63*/            max = ((PositionRange)expression).getMaxPosition();
/*  64*/            positional = true;
                }
/*  67*/        if(nodeenumeration instanceof LastPositionFinder)
                {
/*  68*/            filterContext.setLastPositionFinder((LastPositionFinder)nodeenumeration);
                } else
                {
/*  71*/            base = new LookaheadEnumerator(nodeenumeration);
/*  72*/            filterContext.setLastPositionFinder((LastPositionFinder)base);
                }
/*  75*/        current = getNextMatchingElement();
            }

            public boolean hasMoreElements()
            {
/*  83*/        if(finished)
/*  83*/            return false;
/*  84*/        else
/*  84*/            return current != null;
            }

            public NodeInfo nextElement()
                throws XPathException
            {
/*  92*/        NodeInfo nodeinfo = current;
/*  93*/        current = getNextMatchingElement();
/*  94*/        return nodeinfo;
            }

            private NodeInfo getNextMatchingElement()
                throws XPathException
            {
/* 102*/        while(!finished && base.hasMoreElements()) 
                {
/* 103*/            NodeInfo nodeinfo = base.nextElement();
/* 104*/            position++;
/* 105*/            if(matches(nodeinfo))
/* 106*/                return nodeinfo;
/* 107*/            if(finishAfterReject)
/* 108*/                return null;
                }
/* 111*/        return null;
            }

            private boolean matches(NodeInfo nodeinfo)
                throws XPathException
            {
/* 119*/        if(positional)
                {
/* 120*/            if(position < min)
/* 121*/                return false;
/* 122*/            if(position > max)
                    {
/* 123*/                finished = true;
/* 124*/                return false;
                    } else
                    {
/* 126*/                return true;
                    }
                }
/* 129*/        filterContext.setPosition(position);
/* 130*/        filterContext.setContextNode(nodeinfo);
/* 136*/        if(dataType == 2)
                {
/* 137*/            double d = (int)filter.evaluateAsNumber(filterContext);
/* 138*/            return (double)position == d;
                }
/* 143*/        if(dataType == -1)
                {
/* 145*/            Value value = filter.evaluate(filterContext);
/* 146*/            if(value instanceof NumericValue)
/* 147*/                return (double)position == value.asNumber();
/* 149*/            else
/* 149*/                return value.asBoolean();
                } else
                {
/* 153*/            return filter.evaluateAsBoolean(filterContext);
                }
            }

            public boolean isSorted()
            {
/* 162*/        return base.isSorted();
            }

            public boolean isReverseSorted()
            {
/* 166*/        return base.isReverseSorted();
            }

            public boolean isPeer()
            {
/* 174*/        return base.isPeer();
            }
}
