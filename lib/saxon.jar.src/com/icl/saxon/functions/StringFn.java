// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StringFn.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeInfo;

public class StringFn extends Function
{

            public StringFn()
            {
            }

            public String getName()
            {
/*  17*/        return "string";
            }

            public int getDataType()
            {
/*  26*/        return 3;
            }

            public Expression simplify()
                throws XPathException
            {
/*  35*/        int i = checkArgumentCount(0, 1);
/*  36*/        if(i == 1)
                {
/*  37*/            super.argument[0] = super.argument[0].simplify();
/*  38*/            if(super.argument[0].getDataType() == 3)
/*  39*/                return super.argument[0];
/*  41*/            if(super.argument[0] instanceof Value)
/*  42*/                return new StringValue(((Value)super.argument[0]).asString());
                }
/*  45*/        return this;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  53*/        if(getNumberOfArguments() == 1)
/*  54*/            return super.argument[0].evaluateAsString(context);
/*  56*/        else
/*  56*/            return context.getContextNodeInfo().getStringValue();
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
/*  86*/            StringFn stringfn = new StringFn();
/*  87*/            stringfn.addArgument(super.argument[0].reduce(i, context));
/*  88*/            stringfn.setStaticContext(getStaticContext());
/*  89*/            return stringfn.simplify();
                }
/*  91*/        if((i & 8) != 0)
/*  92*/            return evaluate(context);
/*  94*/        else
/*  94*/            return this;
            }
}
