// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   IntersectionEnumeration.java

package com.icl.saxon.expr;

import com.icl.saxon.Controller;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;

// Referenced classes of package com.icl.saxon.expr:
//            NodeSetExtent, XPathException, NodeSetValue

public class IntersectionEnumeration
    implements NodeEnumeration
{

            private NodeEnumeration p1;
            private NodeEnumeration p2;
            private NodeEnumeration e1;
            private NodeEnumeration e2;
            private NodeInfo nextNode1;
            private NodeInfo nextNode2;
            private NodeInfo nextNode;
            private Controller controller;

            public IntersectionEnumeration(NodeEnumeration nodeenumeration, NodeEnumeration nodeenumeration1, Controller controller1)
                throws XPathException
            {
/*  23*/        nextNode1 = null;
/*  24*/        nextNode2 = null;
/*  26*/        nextNode = null;
/*  38*/        p1 = nodeenumeration;
/*  39*/        p2 = nodeenumeration1;
/*  40*/        controller = controller1;
/*  41*/        e1 = nodeenumeration;
/*  42*/        e2 = nodeenumeration1;
/*  43*/        if(!e1.isSorted())
/*  44*/            e1 = (new NodeSetExtent(e1, controller1)).sort().enumerate();
/*  46*/        if(!e2.isSorted())
/*  47*/            e2 = (new NodeSetExtent(e2, controller1)).sort().enumerate();
/*  52*/        if(e1.hasMoreElements())
/*  53*/            nextNode1 = e1.nextElement();
/*  55*/        if(e2.hasMoreElements())
/*  56*/            nextNode2 = e2.nextElement();
/*  61*/        advance();
            }

            public boolean hasMoreElements()
            {
/*  66*/        return nextNode != null;
            }

            public NodeInfo nextElement()
                throws XPathException
            {
/*  70*/        NodeInfo nodeinfo = nextNode;
/*  71*/        advance();
/*  72*/        return nodeinfo;
            }

            private void advance()
                throws XPathException
            {
/*  80*/        while(nextNode1 != null && nextNode2 != null) 
                {
/*  81*/            int i = controller.compare(nextNode1, nextNode2);
/*  82*/            if(i < 0)
                    {
/*  83*/                NodeInfo nodeinfo = nextNode1;
/*  84*/                if(e1.hasMoreElements())
                        {
/*  85*/                    nextNode1 = e1.nextElement();
                        } else
                        {
/*  87*/                    nextNode1 = null;
/*  88*/                    nextNode = null;
                        }
                    } else
/*  91*/            if(i > 0)
                    {
/*  92*/                NodeInfo nodeinfo1 = nextNode2;
/*  93*/                if(e2.hasMoreElements())
                        {
/*  94*/                    nextNode2 = e2.nextElement();
                        } else
                        {
/*  96*/                    nextNode2 = null;
/*  97*/                    nextNode = null;
                        }
                    } else
                    {
/* 102*/                nextNode = nextNode2;
/* 103*/                if(e2.hasMoreElements())
/* 104*/                    nextNode2 = e2.nextElement();
/* 106*/                else
/* 106*/                    nextNode2 = null;
/* 108*/                if(e1.hasMoreElements())
/* 109*/                    nextNode1 = e1.nextElement();
/* 111*/                else
/* 111*/                    nextNode1 = null;
/* 114*/                return;
                    }
                }
/* 117*/        nextNode = null;
            }

            public boolean isSorted()
            {
/* 121*/        return true;
            }

            public boolean isReverseSorted()
            {
/* 125*/        return false;
            }

            public boolean isPeer()
            {
/* 129*/        return false;
            }
}
