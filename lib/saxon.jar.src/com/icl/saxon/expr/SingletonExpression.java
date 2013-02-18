// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SingletonExpression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.om.*;

// Referenced classes of package com.icl.saxon.expr:
//            NodeSetExpression, SingletonNodeSet, XPathException, NodeSetValue

public abstract class SingletonExpression extends NodeSetExpression
{

            public SingletonExpression()
            {
            }

            public boolean isContextDocumentNodeSet()
            {
/*  20*/        return true;
            }

            public abstract NodeInfo getNode(Context context)
                throws XPathException;

            public NodeInfo selectFirst(Context context)
                throws XPathException
            {
/*  37*/        return getNode(context);
            }

            public NodeEnumeration enumerate(Context context, boolean flag)
                throws XPathException
            {
/*  47*/        return new SingletonEnumeration(getNode(context));
            }

            public NodeSetValue evaluateAsNodeSet(Context context)
                throws XPathException
            {
/*  57*/        return new SingletonNodeSet(getNode(context));
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  67*/        NodeInfo nodeinfo = getNode(context);
/*  68*/        if(nodeinfo == null)
/*  68*/            return "";
/*  69*/        else
/*  69*/            return nodeinfo.getStringValue();
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  80*/        return getNode(context) != null;
            }
}
