// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Concat.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;

public class Concat extends Function
{

            public Concat()
            {
            }

            public String getName()
            {
/*  14*/        return "concat";
            }

            public int getDataType()
            {
/*  23*/        return 3;
            }

            public Expression simplify()
                throws XPathException
            {
/*  32*/        int i = checkArgumentCount(2, 0x7fffffff);
/*  33*/        boolean flag = true;
/*  34*/        for(int j = 0; j < i; j++)
                {
/*  35*/            super.argument[j] = super.argument[j].simplify();
/*  36*/            if(!(super.argument[j] instanceof Value))
/*  37*/                flag = false;
                }

/*  40*/        if(flag)
/*  41*/            return evaluate(null);
/*  43*/        else
/*  43*/            return this;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  51*/        int i = getNumberOfArguments();
/*  53*/        StringBuffer stringbuffer = new StringBuffer();
/*  54*/        for(int j = 0; j < i; j++)
/*  55*/            stringbuffer.append(super.argument[j].evaluateAsString(context));

/*  58*/        return stringbuffer.toString();
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  66*/        return new StringValue(evaluateAsString(context));
            }

            public int getDependencies()
            {
/*  74*/        int i = getNumberOfArguments();
/*  75*/        int j = 0;
/*  76*/        for(int k = 0; k < i; k++)
/*  77*/            j |= super.argument[k].getDependencies();

/*  79*/        return j;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  87*/        Concat concat = new Concat();
/*  88*/        int j = getNumberOfArguments();
/*  89*/        for(int k = 0; k < j; k++)
/*  90*/            concat.addArgument(super.argument[k].reduce(i, context));

/*  92*/        concat.setStaticContext(getStaticContext());
/*  93*/        return concat.simplify();
            }
}
