// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Sets.java

package com.icl.saxon.exslt;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeEnumeration;
import java.util.Vector;

public abstract class Sets
{

            public Sets()
            {
            }

            public static NodeEnumeration intersection(Context context, NodeEnumeration nodeenumeration, NodeEnumeration nodeenumeration1)
                throws XPathException
            {
/*  25*/        return new IntersectionEnumeration(nodeenumeration, nodeenumeration1, context.getController());
            }

            public static NodeEnumeration difference(Context context, NodeEnumeration nodeenumeration, NodeEnumeration nodeenumeration1)
                throws XPathException
            {
/*  36*/        return new DifferenceEnumeration(nodeenumeration, nodeenumeration1, context.getController());
            }

            public static boolean hasSameNode(Context context, NodeEnumeration nodeenumeration, NodeEnumeration nodeenumeration1)
                throws XPathException
            {
/*  48*/        IntersectionEnumeration intersectionenumeration = new IntersectionEnumeration(nodeenumeration, nodeenumeration1, context.getController());
/*  50*/        return intersectionenumeration.hasMoreElements();
            }

            public static NodeEnumeration distinct(Context context, NodeEnumeration nodeenumeration)
                throws XPathException
            {
/*  58*/        return new DistinctEnumeration(nodeenumeration, context.getController());
            }

            public static NodeSetValue leading(Context context, NodeEnumeration nodeenumeration, NodeEnumeration nodeenumeration1)
                throws XPathException
            {
/*  70*/        Controller controller = context.getController();
/*  71*/        if(!nodeenumeration1.hasMoreElements())
/*  72*/            return new NodeSetExtent(nodeenumeration, controller);
/*  74*/        com.icl.saxon.om.NodeInfo nodeinfo = nodeenumeration1.nextElement();
/*  76*/        Vector vector = new Vector();
                com.icl.saxon.om.NodeInfo nodeinfo1;
/*  78*/        for(; nodeenumeration.hasMoreElements(); vector.addElement(nodeinfo1))
                {
/*  78*/            nodeinfo1 = nodeenumeration.nextElement();
/*  79*/            if(controller.compare(nodeinfo1, nodeinfo) >= 0)
/*  80*/                break;
                }

/*  85*/        return new NodeSetExtent(vector, controller);
            }

            public static NodeSetValue trailing(Context context, NodeEnumeration nodeenumeration, NodeEnumeration nodeenumeration1)
                throws XPathException
            {
/*  98*/        if(!nodeenumeration1.hasMoreElements())
/*  99*/            return new EmptyNodeSet();
/* 101*/        com.icl.saxon.om.NodeInfo nodeinfo = nodeenumeration1.nextElement();
/* 102*/        Controller controller = context.getController();
/* 104*/        Vector vector = new Vector();
/* 105*/        boolean flag = false;
/* 107*/        while(nodeenumeration.hasMoreElements()) 
                {
/* 107*/            com.icl.saxon.om.NodeInfo nodeinfo1 = nodeenumeration.nextElement();
/* 108*/            if(flag)
/* 109*/                vector.addElement(nodeinfo1);
/* 110*/            else
/* 110*/            if(controller.compare(nodeinfo1, nodeinfo) > 0)
                    {
/* 111*/                flag = true;
/* 112*/                vector.addElement(nodeinfo1);
                    }
                }
/* 115*/        return new NodeSetExtent(vector, controller);
            }
}
