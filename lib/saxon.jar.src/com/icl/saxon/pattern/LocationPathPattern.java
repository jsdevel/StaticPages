// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   LocationPathPattern.java

package com.icl.saxon.pattern;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.pattern:
//            Pattern, AnyNodeTest, NodeTest

public final class LocationPathPattern extends Pattern
{

            public Pattern parentPattern;
            public Pattern ancestorPattern;
            public NodeTest nodeTest;
            protected Expression filters[];
            protected int numberOfFilters;
            protected Expression equivalentExpr;
            protected boolean firstElementPattern;
            protected boolean lastElementPattern;
            protected boolean specialFilter;

            public LocationPathPattern()
            {
/*  17*/        parentPattern = null;
/*  18*/        ancestorPattern = null;
/*  19*/        nodeTest = AnyNodeTest.getInstance();
/*  20*/        filters = null;
/*  21*/        numberOfFilters = 0;
/*  22*/        equivalentExpr = null;
/*  23*/        firstElementPattern = false;
/*  24*/        lastElementPattern = false;
/*  25*/        specialFilter = false;
            }

            public void addFilter(Expression expression)
            {
/*  33*/        if(filters == null)
/*  34*/            filters = new Expression[3];
/*  35*/        else
/*  35*/        if(numberOfFilters == filters.length)
                {
/*  36*/            Expression aexpression[] = new Expression[numberOfFilters * 2];
/*  37*/            System.arraycopy(filters, 0, aexpression, 0, numberOfFilters);
/*  38*/            filters = aexpression;
                }
/*  40*/        filters[numberOfFilters++] = expression;
            }

            public Pattern simplify()
                throws XPathException
            {
/*  51*/        if(parentPattern == null && ancestorPattern == null && filters == null)
                {
/*  54*/            nodeTest.setStaticContext(getStaticContext());
/*  55*/            return nodeTest;
                }
/*  60*/        if(parentPattern != null)
/*  60*/            parentPattern = parentPattern.simplify();
/*  61*/        if(ancestorPattern != null)
/*  61*/            ancestorPattern = ancestorPattern.simplify();
/*  62*/        if(filters != null)
                {
/*  63*/            for(int i = numberOfFilters - 1; i >= 0; i--)
                    {
/*  64*/                Expression expression = filters[i].simplify();
/*  65*/                filters[i] = expression;
/*  67*/                if((expression instanceof BooleanValue) && ((Value)expression).asBoolean() && i == numberOfFilters - 1)
/*  69*/                    numberOfFilters--;
                    }

                }
/*  77*/        if(nodeTest.getNodeType() == 1 && numberOfFilters == 1 && (filters[0] instanceof NumericValue) && (int)((NumericValue)filters[0]).asNumber() == 1)
                {
/*  81*/            firstElementPattern = true;
/*  82*/            specialFilter = true;
/*  83*/            numberOfFilters = 0;
/*  84*/            filters = null;
                }
/*  90*/        if(nodeTest.getNodeType() == 1 && numberOfFilters == 1 && (filters[0] instanceof IsLastExpression) && ((IsLastExpression)filters[0]).getCondition())
                {
/*  94*/            lastElementPattern = true;
/*  95*/            specialFilter = true;
/*  96*/            numberOfFilters = 0;
/*  97*/            filters = null;
                }
/* 100*/        if(isRelative())
                {
/* 101*/            makeEquivalentExpression();
/* 102*/            specialFilter = true;
                }
/* 105*/        return this;
            }

            private void makeEquivalentExpression()
                throws XPathException
            {
/* 114*/        byte byte0 = ((byte)(nodeTest.getNodeType() != 2 ? 3 : 2));
/* 117*/        Step step = new Step(byte0, nodeTest);
/* 118*/        step.setFilters(filters, numberOfFilters);
/* 119*/        equivalentExpr = new PathExpression(new ParentNodeExpression(), step);
            }

            public boolean matchesX(NodeInfo nodeinfo, Context context)
                throws XPathException
            {
/* 130*/        System.err.println("Matching node " + nodeinfo + " against LP pattern " + this);
/* 131*/        System.err.println("Node types " + nodeinfo.getNodeType() + " / " + getNodeType());
/* 132*/        boolean flag = matches(nodeinfo, context);
/* 133*/        System.err.println(flag ? "matches" : "no match");
/* 134*/        return flag;
            }

            public boolean matches(NodeInfo nodeinfo, Context context)
                throws XPathException
            {
/* 139*/label0:
                {
/* 139*/            if(!nodeTest.matches(nodeinfo))
/* 139*/                return false;
/* 141*/            if(parentPattern != null)
                    {
/* 142*/                NodeInfo nodeinfo1 = nodeinfo.getParent();
/* 143*/                if(nodeinfo1 == null)
/* 143*/                    return false;
/* 144*/                if(!parentPattern.matches(nodeinfo1, context))
/* 144*/                    return false;
                    }
/* 147*/            if(ancestorPattern == null)
/* 148*/                break label0;
/* 148*/            NodeInfo nodeinfo2 = nodeinfo.getParent();
/* 150*/            do
                    {
/* 150*/                if(ancestorPattern.matches(nodeinfo2, context))
/* 150*/                    break label0;
/* 151*/                nodeinfo2 = nodeinfo2.getParent();
                    } while(nodeinfo2 != null);
/* 152*/            return false;
                }
/* 156*/        if(specialFilter)
                {
/* 157*/            if(firstElementPattern)
                    {
/* 158*/                com.icl.saxon.om.AxisEnumeration axisenumeration = nodeinfo.getEnumeration((byte)11, nodeTest);
/* 159*/                return !axisenumeration.hasMoreElements();
                    }
/* 162*/            if(lastElementPattern)
                    {
/* 163*/                com.icl.saxon.om.AxisEnumeration axisenumeration1 = nodeinfo.getEnumeration((byte)7, nodeTest);
/* 164*/                return !axisenumeration1.hasMoreElements();
                    }
/* 167*/            if(equivalentExpr != null)
                    {
/* 173*/                Context context1 = context.newContext();
/* 174*/                context1.setContextNode(nodeinfo);
/* 175*/                context1.setPosition(1);
/* 176*/                context1.setLast(1);
/* 177*/                for(NodeEnumeration nodeenumeration = equivalentExpr.enumerate(context1, false); nodeenumeration.hasMoreElements();)
                        {
/* 179*/                    NodeInfo nodeinfo3 = nodeenumeration.nextElement();
/* 180*/                    if(nodeinfo3.isSameNodeInfo(nodeinfo))
/* 181*/                        return true;
                        }

/* 184*/                return false;
                    }
                }
/* 188*/        if(filters != null)
                {
/* 189*/            Context context2 = context.newContext();
/* 190*/            context2.setContextNode(nodeinfo);
/* 191*/            context2.setPosition(1);
/* 192*/            context2.setLast(1);
/* 194*/            for(int i = 0; i < numberOfFilters; i++)
/* 195*/                if(!filters[i].evaluateAsBoolean(context2))
/* 195*/                    return false;

                }
/* 199*/        return true;
            }

            public short getNodeType()
            {
/* 209*/        return nodeTest.getNodeType();
            }

            public int getFingerprint()
            {
/* 219*/        return nodeTest.getFingerprint();
            }

            public boolean isRelative()
                throws XPathException
            {
/* 229*/        if(filters == null)
/* 229*/            return false;
/* 230*/        for(int i = 0; i < numberOfFilters; i++)
                {
/* 231*/            int j = filters[i].getDataType();
/* 232*/            if(j == 2 || j == -1)
/* 232*/                return true;
/* 233*/            if((filters[i].getDependencies() & 0x30) != 0)
/* 234*/                return true;
                }

/* 236*/        return false;
            }
}
