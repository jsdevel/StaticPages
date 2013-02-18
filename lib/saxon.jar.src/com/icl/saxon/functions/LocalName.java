// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   LocalName.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;

public class LocalName extends Function
{

            public LocalName()
            {
            }

            public String getName()
            {
/*  14*/        return "local-name";
            }

            public int getDataType()
            {
/*  23*/        return 3;
            }

            public Expression simplify()
                throws XPathException
            {
/*  31*/        int i = checkArgumentCount(0, 1);
/*  32*/        if(i == 1)
/*  33*/            super.argument[0] = super.argument[0].simplify();
/*  35*/        return this;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  43*/        if(getNumberOfArguments() == 1)
                {
/*  44*/            NodeEnumeration nodeenumeration = super.argument[0].enumerate(context, true);
/*  45*/            if(nodeenumeration.hasMoreElements())
/*  46*/                return nodeenumeration.nextElement().getLocalName();
/*  48*/            else
/*  48*/                return "";
                } else
                {
/*  51*/            return context.getContextNodeInfo().getLocalName();
                }
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  60*/        return new StringValue(evaluateAsString(context));
            }

            public int getDependencies()
            {
/*  68*/        if(getNumberOfArguments() == 1)
/*  69*/            return super.argument[0].getDependencies();
/*  71*/        else
/*  71*/            return 8;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  80*/        if(getNumberOfArguments() == 1)
                {
/*  81*/            LocalName localname = new LocalName();
/*  82*/            localname.addArgument(super.argument[0].reduce(i, context));
/*  83*/            localname.setStaticContext(getStaticContext());
/*  84*/            return localname.simplify();
                }
/*  86*/        if((i & 8) != 0)
/*  87*/            return evaluate(context);
/*  89*/        else
/*  89*/            return this;
            }
}
