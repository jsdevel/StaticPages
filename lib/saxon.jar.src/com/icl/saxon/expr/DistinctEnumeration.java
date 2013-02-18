// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DistinctEnumeration.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;
import java.util.Hashtable;

// Referenced classes of package com.icl.saxon.expr:
//            NodeSetExtent, XPathException, NodeSetValue, Expression

public class DistinctEnumeration
    implements NodeEnumeration
{

            private NodeEnumeration p1;
            private NodeEnumeration e1;
            private Hashtable lookup;
            private Context context;
            private Expression expression;
            private Controller controller;
            NodeInfo nextNode;

            public DistinctEnumeration(NodeEnumeration nodeenumeration, Controller controller1)
                throws XPathException
            {
/*  18*/        lookup = new Hashtable();
/*  23*/        nextNode = null;
/*  31*/        p1 = nodeenumeration;
/*  32*/        context = null;
/*  33*/        expression = null;
/*  34*/        controller = controller1;
/*  35*/        e1 = nodeenumeration;
/*  36*/        if(!e1.isSorted())
/*  38*/            e1 = (new NodeSetExtent(e1, controller1)).sort().enumerate();
/*  43*/        if(e1.hasMoreElements())
                {
/*  44*/            nextNode = e1.nextElement();
/*  45*/            advance();
                }
            }

            public DistinctEnumeration(Context context1, NodeEnumeration nodeenumeration, Expression expression1)
                throws XPathException
            {
/*  18*/        lookup = new Hashtable();
/*  23*/        nextNode = null;
/*  51*/        p1 = nodeenumeration;
/*  52*/        context = context1.newContext();
/*  53*/        expression = expression1;
/*  54*/        controller = context1.getController();
/*  55*/        e1 = nodeenumeration;
/*  56*/        if(!e1.isSorted())
/*  57*/            e1 = (new NodeSetExtent(e1, controller)).sort().enumerate();
/*  62*/        if(e1.hasMoreElements())
                {
/*  63*/            nextNode = e1.nextElement();
/*  64*/            advance();
                }
            }

            public boolean hasMoreElements()
            {
/*  69*/        return nextNode != null;
            }

            public NodeInfo nextElement()
                throws XPathException
            {
/*  73*/        NodeInfo nodeinfo = nextNode;
/*  74*/        advance();
/*  75*/        return nodeinfo;
            }

            private void advance()
                throws XPathException
            {
/*  80*/        while(nextNode != null) 
                {
                    String s;
/*  82*/            if(expression == null)
                    {
/*  83*/                s = nextNode.getStringValue();
                    } else
                    {
/*  85*/                context.setContextNode(nextNode);
/*  86*/                context.setPosition(1);
/*  87*/                context.setLast(1);
/*  88*/                s = expression.evaluateAsString(context);
                    }
/*  90*/            if(lookup.get(s) == null)
                    {
/*  91*/                lookup.put(s, nextNode);
/*  92*/                return;
                    }
/*  94*/            if(e1.hasMoreElements())
/*  95*/                nextNode = e1.nextElement();
/*  97*/            else
/*  97*/                nextNode = null;
                }
            }

            public boolean isSorted()
            {
/* 105*/        return true;
            }

            public boolean isReverseSorted()
            {
/* 109*/        return false;
            }

            public boolean isPeer()
            {
/* 113*/        return false;
            }
}
