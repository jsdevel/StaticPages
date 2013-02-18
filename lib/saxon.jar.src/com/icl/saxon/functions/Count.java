// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Count.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeEnumeration;

public class Count extends Function
{

            public Count()
            {
            }

            public String getName()
            {
/*  13*/        return "count";
            }

            public int getDataType()
            {
/*  22*/        return 2;
            }

            public Expression simplify()
                throws XPathException
            {
/*  30*/        checkArgumentCount(1, 1);
/*  31*/        super.argument[0] = super.argument[0].simplify();
/*  33*/        if(super.argument[0] instanceof Value)
/*  34*/            return evaluate(null);
/*  36*/        else
/*  36*/            return this;
            }

            public double evaluateAsNumber(Context context)
                throws XPathException
            {
/*  44*/        int i = 0;
/*  45*/        for(NodeEnumeration nodeenumeration = super.argument[0].enumerate(context, true); nodeenumeration.hasMoreElements();)
                {
/*  47*/            nodeenumeration.nextElement();
/*  48*/            i++;
                }

/*  50*/        return (double)i;
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  58*/        return new NumericValue(evaluateAsNumber(context));
            }

            public int getDependencies()
            {
/*  66*/        return super.argument[0].getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  74*/        Count count = new Count();
/*  75*/        count.addArgument(super.argument[0].reduce(i, context));
/*  76*/        count.setStaticContext(getStaticContext());
/*  77*/        return count.simplify();
            }
}
