// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Sum.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;

public class Sum extends Function
{

            public Sum()
            {
            }

            public String getName()
            {
/*  17*/        return "sum";
            }

            public int getDataType()
            {
/*  26*/        return 2;
            }

            public Expression simplify()
                throws XPathException
            {
/*  34*/        checkArgumentCount(1, 1);
/*  35*/        super.argument[0] = super.argument[0].simplify();
/*  37*/        if(super.argument[0] instanceof Value)
/*  38*/            return evaluate(null);
/*  40*/        else
/*  40*/            return this;
            }

            public double evaluateAsNumber(Context context)
                throws XPathException
            {
/*  48*/        NodeEnumeration nodeenumeration = super.argument[0].enumerate(context, false);
/*  49*/        return total(nodeenumeration);
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  57*/        return new NumericValue(evaluateAsNumber(context));
            }

            public int getDependencies()
            {
/*  65*/        return super.argument[0].getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  73*/        Sum sum = new Sum();
/*  74*/        sum.addArgument(super.argument[0].reduce(i, context));
/*  75*/        sum.setStaticContext(getStaticContext());
/*  76*/        return sum.simplify();
            }

            private static double total(NodeEnumeration nodeenumeration)
                throws XPathException
            {
                double d;
                String s;
/*  85*/        for(d = 0.0D; nodeenumeration.hasMoreElements(); d += Value.stringToNumber(s))
/*  87*/            s = nodeenumeration.nextElement().getStringValue();

/*  90*/        return d;
            }
}
