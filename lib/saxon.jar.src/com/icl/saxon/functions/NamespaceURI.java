// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NamespaceURI.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;

public class NamespaceURI extends Function
{

            public NamespaceURI()
            {
            }

            public String getName()
            {
/*  17*/        return "namespace-uri";
            }

            public int getDataType()
            {
/*  26*/        return 3;
            }

            public Expression simplify()
                throws XPathException
            {
/*  34*/        int i = checkArgumentCount(0, 1);
/*  35*/        if(i == 1)
/*  36*/            super.argument[0] = super.argument[0].simplify();
/*  38*/        return this;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
                String s;
/*  47*/        if(getNumberOfArguments() == 1)
                {
/*  48*/            NodeEnumeration nodeenumeration = super.argument[0].enumerate(context, true);
/*  49*/            if(nodeenumeration.hasMoreElements())
/*  50*/                s = nodeenumeration.nextElement().getURI();
/*  52*/            else
/*  52*/                s = null;
                } else
                {
/*  55*/            s = context.getContextNodeInfo().getURI();
                }
/*  57*/        return s != null ? s : "";
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  65*/        return new StringValue(evaluateAsString(context));
            }

            public int getDependencies()
            {
/*  73*/        if(getNumberOfArguments() == 1)
/*  74*/            return super.argument[0].getDependencies();
/*  76*/        else
/*  76*/            return 8;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  85*/        if(getNumberOfArguments() == 1)
                {
/*  86*/            NamespaceURI namespaceuri = new NamespaceURI();
/*  87*/            namespaceuri.addArgument(super.argument[0].reduce(i, context));
/*  88*/            namespaceuri.setStaticContext(getStaticContext());
/*  89*/            return namespaceuri.simplify();
                }
/*  91*/        if((i & 8) != 0)
/*  92*/            return evaluate(context);
/*  94*/        else
/*  94*/            return this;
            }
}
