// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StringLength.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeInfo;

public class StringLength extends Function
{

            public StringLength()
            {
            }

            public String getName()
            {
/*  14*/        return "string-length";
            }

            public int getDataType()
            {
/*  23*/        return 2;
            }

            public Expression simplify()
                throws XPathException
            {
/*  32*/        int i = checkArgumentCount(0, 1);
/*  33*/        if(i == 1)
                {
/*  34*/            super.argument[0] = super.argument[0].simplify();
/*  35*/            if(super.argument[0] instanceof Value)
/*  36*/                return evaluate(null);
                }
/*  39*/        return this;
            }

            public double evaluateAsNumber(Context context)
                throws XPathException
            {
/*  47*/        if(getNumberOfArguments() == 1)
/*  48*/            return (double)StringValue.getLength(super.argument[0].evaluateAsString(context));
/*  51*/        else
/*  51*/            return (double)StringValue.getLength(context.getContextNodeInfo().getStringValue());
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  61*/        return new NumericValue(evaluateAsNumber(context));
            }

            public int getDependencies()
            {
/*  69*/        if(getNumberOfArguments() == 1)
/*  70*/            return super.argument[0].getDependencies();
/*  72*/        else
/*  72*/            return 8;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  81*/        if(getNumberOfArguments() == 1)
                {
/*  82*/            StringLength stringlength = new StringLength();
/*  83*/            stringlength.addArgument(super.argument[0].reduce(i, context));
/*  84*/            stringlength.setStaticContext(getStaticContext());
/*  85*/            return stringlength.simplify();
                }
/*  87*/        if((i & 8) != 0)
/*  88*/            return evaluate(context);
/*  90*/        else
/*  90*/            return this;
            }
}
