// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NameFn.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;

public class NameFn extends Function
{

            public NameFn()
            {
            }

            public String getName()
            {
/*  18*/        return "name";
            }

            public int getDataType()
            {
/*  27*/        return 3;
            }

            public Expression simplify()
                throws XPathException
            {
/*  35*/        int i = checkArgumentCount(0, 1);
/*  36*/        if(i == 1)
/*  37*/            super.argument[0] = super.argument[0].simplify();
/*  39*/        return this;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  47*/        if(getNumberOfArguments() == 1)
                {
/*  48*/            NodeEnumeration nodeenumeration = super.argument[0].enumerate(context, true);
/*  49*/            if(nodeenumeration.hasMoreElements())
/*  50*/                return nodeenumeration.nextElement().getDisplayName();
/*  52*/            else
/*  52*/                return "";
                } else
                {
/*  55*/            return context.getContextNodeInfo().getDisplayName();
                }
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  64*/        return new StringValue(evaluateAsString(context));
            }

            public int getDependencies()
            {
/*  72*/        if(getNumberOfArguments() == 1)
/*  73*/            return super.argument[0].getDependencies();
/*  75*/        else
/*  75*/            return 8;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  84*/        if(getNumberOfArguments() == 1)
                {
/*  85*/            NameFn namefn = new NameFn();
/*  86*/            namefn.addArgument(super.argument[0].reduce(i, context));
/*  87*/            namefn.setStaticContext(getStaticContext());
/*  88*/            return namefn.simplify();
                }
/*  90*/        if((i & 8) != 0)
/*  91*/            return evaluate(context);
/*  93*/        else
/*  93*/            return this;
            }
}
