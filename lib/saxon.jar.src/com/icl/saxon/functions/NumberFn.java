// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NumberFn.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeInfo;

public class NumberFn extends Function
{

            public NumberFn()
            {
            }

            public String getName()
            {
/*  18*/        return "number";
            }

            public int getDataType()
            {
/*  27*/        return 2;
            }

            public Expression simplify()
                throws XPathException
            {
/*  36*/        int i = checkArgumentCount(0, 1);
/*  37*/        if(i == 1)
                {
/*  38*/            super.argument[0] = super.argument[0].simplify();
/*  39*/            if(super.argument[0].getDataType() == 2)
/*  40*/                return super.argument[0];
/*  42*/            if(super.argument[0] instanceof Value)
/*  43*/                return new NumericValue(((Value)super.argument[0]).asNumber());
                }
/*  46*/        return this;
            }

            public double evaluateAsNumber(Context context)
                throws XPathException
            {
/*  54*/        if(getNumberOfArguments() == 1)
/*  55*/            return super.argument[0].evaluateAsNumber(context);
/*  57*/        else
/*  57*/            return Value.stringToNumber(context.getContextNodeInfo().getStringValue());
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  67*/        return new NumericValue(evaluateAsNumber(context));
            }

            public int getDependencies()
            {
/*  75*/        if(getNumberOfArguments() == 1)
/*  76*/            return super.argument[0].getDependencies();
/*  78*/        else
/*  78*/            return 8;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  87*/        if(getNumberOfArguments() == 1)
                {
/*  88*/            NumberFn numberfn = new NumberFn();
/*  89*/            numberfn.addArgument(super.argument[0].reduce(i, context));
/*  90*/            numberfn.setStaticContext(getStaticContext());
/*  91*/            return numberfn.simplify();
                }
/*  93*/        if((i & 8) != 0)
/*  94*/            return evaluate(context);
/*  96*/        else
/*  96*/            return this;
            }
}
