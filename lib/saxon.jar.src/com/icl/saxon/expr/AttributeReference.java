// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   AttributeReference.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.pattern.NameTest;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            SingletonExpression, XPathException, Expression

class AttributeReference extends SingletonExpression
{

            private int fingerprint;
            private NodeInfo boundParentNode;

            private AttributeReference()
            {
/*  15*/        boundParentNode = null;
            }

            public AttributeReference(int i)
            {
/*  15*/        boundParentNode = null;
/*  26*/        fingerprint = i;
            }

            public void bindParentNode(NodeInfo nodeinfo)
            {
/*  34*/        boundParentNode = nodeinfo;
            }

            private NodeInfo getParentNode(Context context)
            {
/*  42*/        if(boundParentNode == null)
/*  43*/            return context.getContextNodeInfo();
/*  45*/        else
/*  45*/            return boundParentNode;
            }

            public NodeInfo getNode(Context context)
                throws XPathException
            {
/*  55*/        NodeInfo nodeinfo = getParentNode(context);
/*  56*/        if(nodeinfo.getNodeType() == 1)
                {
/*  57*/            NameTest nametest = new NameTest((short)2, fingerprint);
/*  58*/            com.icl.saxon.om.AxisEnumeration axisenumeration = nodeinfo.getEnumeration((byte)2, nametest);
/*  59*/            if(axisenumeration.hasMoreElements())
/*  60*/                return axisenumeration.nextElement();
/*  62*/            else
/*  62*/                return null;
                } else
                {
/*  64*/            return null;
                }
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  75*/        NodeInfo nodeinfo = getParentNode(context);
/*  76*/        if(nodeinfo.getNodeType() == 1)
/*  77*/            return nodeinfo.getAttributeValue(fingerprint) != null;
/*  79*/        else
/*  79*/            return false;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  89*/        NodeInfo nodeinfo = getParentNode(context);
/*  90*/        if(nodeinfo.getNodeType() == 1)
                {
/*  91*/            String s = nodeinfo.getAttributeValue(fingerprint);
/*  92*/            if(s == null)
/*  92*/                return "";
/*  93*/            else
/*  93*/                return s;
                } else
                {
/*  95*/            return "";
                }
            }

            public int getDependencies()
            {
/* 106*/        return boundParentNode != null ? 0 : 8;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/* 123*/        if(boundParentNode == null && (i & 8) != 0)
                {
/* 124*/            AttributeReference attributereference = new AttributeReference();
/* 125*/            attributereference.fingerprint = fingerprint;
/* 126*/            attributereference.bindParentNode(context.getContextNodeInfo());
/* 127*/            attributereference.setStaticContext(getStaticContext());
/* 128*/            return attributereference;
                } else
                {
/* 130*/            return this;
                }
            }

            public void display(int i)
            {
/* 139*/        System.err.println(Expression.indent(i) + "@" + fingerprint);
            }
}
