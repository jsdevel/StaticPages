// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   UnionEnumeration.java

package com.icl.saxon.expr;

import com.icl.saxon.Controller;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;

// Referenced classes of package com.icl.saxon.expr:
//            NodeSetExtent, XPathException, NodeSetValue

public class UnionEnumeration
    implements NodeEnumeration
{

            private NodeEnumeration p1;
            private NodeEnumeration p2;
            private NodeEnumeration e1;
            private NodeEnumeration e2;
            private NodeInfo nextNode1;
            private NodeInfo nextNode2;
            private Controller controller;

            public UnionEnumeration(NodeEnumeration nodeenumeration, NodeEnumeration nodeenumeration1, Controller controller1)
                throws XPathException
            {
/*  17*/        nextNode1 = null;
/*  18*/        nextNode2 = null;
/*  23*/        p1 = nodeenumeration;
/*  24*/        p2 = nodeenumeration1;
/*  25*/        controller = controller1;
/*  26*/        e1 = nodeenumeration;
/*  27*/        e2 = nodeenumeration1;
/*  29*/        if(!e1.isSorted())
/*  30*/            e1 = (new NodeSetExtent(e1, controller1)).sort().enumerate();
/*  32*/        if(!e2.isSorted())
/*  33*/            e2 = (new NodeSetExtent(e2, controller1)).sort().enumerate();
/*  36*/        if(e1.hasMoreElements())
/*  37*/            nextNode1 = e1.nextElement();
/*  39*/        if(e2.hasMoreElements())
/*  40*/            nextNode2 = e2.nextElement();
            }

            public boolean hasMoreElements()
            {
/*  45*/        return nextNode1 != null || nextNode2 != null;
            }

            public NodeInfo nextElement()
                throws XPathException
            {
/*  52*/        if(nextNode1 != null && nextNode2 != null)
                {
/*  53*/            int i = controller.compare(nextNode1, nextNode2);
/*  54*/            if(i < 0)
                    {
/*  55*/                NodeInfo nodeinfo2 = nextNode1;
/*  56*/                if(e1.hasMoreElements())
/*  57*/                    nextNode1 = e1.nextElement();
/*  59*/                else
/*  59*/                    nextNode1 = null;
/*  61*/                return nodeinfo2;
                    }
/*  63*/            if(i > 0)
                    {
/*  64*/                NodeInfo nodeinfo3 = nextNode2;
/*  65*/                if(e2.hasMoreElements())
/*  66*/                    nextNode2 = e2.nextElement();
/*  68*/                else
/*  68*/                    nextNode2 = null;
/*  70*/                return nodeinfo3;
                    }
/*  73*/            NodeInfo nodeinfo4 = nextNode2;
/*  74*/            if(e2.hasMoreElements())
/*  75*/                nextNode2 = e2.nextElement();
/*  77*/            else
/*  77*/                nextNode2 = null;
/*  79*/            if(e1.hasMoreElements())
/*  80*/                nextNode1 = e1.nextElement();
/*  82*/            else
/*  82*/                nextNode1 = null;
/*  84*/            return nodeinfo4;
                }
/*  90*/        if(nextNode1 != null)
                {
/*  91*/            NodeInfo nodeinfo = nextNode1;
/*  92*/            if(e1.hasMoreElements())
/*  93*/                nextNode1 = e1.nextElement();
/*  95*/            else
/*  95*/                nextNode1 = null;
/*  97*/            return nodeinfo;
                }
/*  99*/        if(nextNode2 != null)
                {
/* 100*/            NodeInfo nodeinfo1 = nextNode2;
/* 101*/            if(e2.hasMoreElements())
/* 102*/                nextNode2 = e2.nextElement();
/* 104*/            else
/* 104*/                nextNode2 = null;
/* 106*/            return nodeinfo1;
                } else
                {
/* 108*/            return null;
                }
            }

            public boolean isSorted()
            {
/* 112*/        return true;
            }

            public boolean isReverseSorted()
            {
/* 116*/        return false;
            }

            public boolean isPeer()
            {
/* 120*/        return false;
            }
}
