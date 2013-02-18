// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NodeSetIntent.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;

// Referenced classes of package com.icl.saxon.expr:
//            NodeSetValue, LastPositionFinder, NodeSetExtent, XPathException, 
//            Expression, NodeSetExpression

public class NodeSetIntent extends NodeSetValue
{

            private NodeSetExpression expression;
            private NodeSetExtent extent;
            private Controller controller;
            private boolean sorted;
            private int useCount;

            public NodeSetIntent(NodeSetExpression nodesetexpression, Controller controller1)
                throws XPathException
            {
/*  15*/        extent = null;
/*  17*/        sorted = false;
/*  18*/        useCount = 0;
/*  25*/        if(nodesetexpression.getDependencies() != 0)
                {
/*  26*/            nodesetexpression.display(10);
/*  27*/            throw new UnsupportedOperationException("Cannot create intensional node-set with context dependencies: " + nodesetexpression.getClass() + ":" + nodesetexpression.getDependencies());
                } else
                {
/*  29*/            expression = nodesetexpression;
/*  30*/            controller = controller1;
/*  31*/            return;
                }
            }

            private Context makeContext()
            {
/*  38*/        Context context = new Context(controller);
/*  39*/        context.setStaticContext(expression.getStaticContext());
/*  40*/        return context;
            }

            public NodeSetExpression getNodeSetExpression()
            {
/*  48*/        return expression;
            }

            public void setSorted(boolean flag)
            {
/*  59*/        sorted = flag;
            }

            public boolean isSorted()
                throws XPathException
            {
/*  69*/        return sorted || expression.enumerate(makeContext(), false).isSorted();
            }

            public boolean isContextDocumentNodeSet()
            {
/*  79*/        return expression.isContextDocumentNodeSet();
            }

            public String asString()
                throws XPathException
            {
/*  89*/        NodeInfo nodeinfo = getFirst();
/*  90*/        return nodeinfo != null ? nodeinfo.getStringValue() : "";
            }

            public boolean asBoolean()
                throws XPathException
            {
/*  99*/        return enumerate().hasMoreElements();
            }

            public int getCount()
                throws XPathException
            {
/* 108*/        if(extent == null)
                {
/* 109*/            NodeEnumeration nodeenumeration = expression.enumerate(makeContext(), false);
/* 110*/            if((nodeenumeration instanceof LastPositionFinder) && nodeenumeration.isSorted())
/* 111*/                return ((LastPositionFinder)nodeenumeration).getLastPosition();
/* 113*/            extent = new NodeSetExtent(nodeenumeration, controller);
                }
/* 115*/        return extent.getCount();
            }

            private void fix()
                throws XPathException
            {
/* 119*/        if(extent == null)
                {
/* 120*/            NodeEnumeration nodeenumeration = expression.enumerate(makeContext(), false);
/* 121*/            extent = new NodeSetExtent(nodeenumeration, controller);
                }
            }

            public NodeSetValue sort()
                throws XPathException
            {
/* 133*/        if(sorted)
                {
/* 133*/            return this;
                } else
                {
/* 134*/            fix();
/* 135*/            return extent.sort();
                }
            }

            public NodeInfo getFirst()
                throws XPathException
            {
/* 144*/        if(extent != null)
/* 144*/            return extent.getFirst();
/* 146*/        NodeEnumeration nodeenumeration = expression.enumerate(makeContext(), false);
/* 147*/        if(sorted || nodeenumeration.isSorted())
                {
/* 148*/            sorted = true;
/* 149*/            if(nodeenumeration.hasMoreElements())
/* 150*/                return nodeenumeration.nextElement();
/* 152*/            else
/* 152*/                return null;
                }
/* 155*/        NodeInfo nodeinfo = null;
/* 157*/        while(nodeenumeration.hasMoreElements()) 
                {
/* 157*/            NodeInfo nodeinfo1 = nodeenumeration.nextElement();
/* 158*/            if(nodeinfo == null || controller.compare(nodeinfo1, nodeinfo) < 0)
/* 159*/                nodeinfo = nodeinfo1;
                }
/* 162*/        return nodeinfo;
            }

            public NodeInfo selectFirst(Context context)
                throws XPathException
            {
/* 174*/        return getFirst();
            }

            public NodeEnumeration enumerate()
                throws XPathException
            {
/* 182*/        if(extent != null)
/* 183*/            return extent.enumerate();
/* 187*/        useCount++;
/* 188*/        if(useCount < 3)
                {
/* 189*/            return expression.enumerate(makeContext(), false);
                } else
                {
/* 191*/            fix();
/* 192*/            return extent.enumerate();
                }
            }
}
