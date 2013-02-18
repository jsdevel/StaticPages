// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Substring.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;

// Referenced classes of package com.icl.saxon.functions:
//            Round

public class Substring extends Function
{

            public Substring()
            {
            }

            public String getName()
            {
/*  11*/        return "substring";
            }

            public int getDataType()
            {
/*  20*/        return 3;
            }

            public Expression simplify()
                throws XPathException
            {
/*  28*/        int i = checkArgumentCount(2, 3);
/*  29*/        super.argument[0] = super.argument[0].simplify();
/*  30*/        super.argument[1] = super.argument[1].simplify();
/*  31*/        boolean flag = (super.argument[0] instanceof Value) && (super.argument[1] instanceof Value);
/*  32*/        if(i == 3)
                {
/*  33*/            super.argument[2] = super.argument[2].simplify();
/*  34*/            flag = flag && (super.argument[2] instanceof Value);
                }
/*  36*/        if(flag)
/*  37*/            return evaluate(null);
/*  39*/        else
/*  39*/            return this;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  48*/        String s = super.argument[0].evaluateAsString(context);
/*  49*/        double d = super.argument[1].evaluateAsNumber(context);
/*  51*/        if(getNumberOfArguments() == 2)
                {
/*  52*/            return substring(s, d);
                } else
                {
/*  54*/            double d1 = super.argument[2].evaluateAsNumber(context);
/*  55*/            return substring(s, d, d1);
                }
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  64*/        return new StringValue(evaluateAsString(context));
            }

            private static String substring(String s, double d)
            {
/*  72*/        int i = s.length();
/*  73*/        int j = (i - (int)d) + 1;
/*  74*/        if(j < 0)
/*  74*/            j = 1;
/*  75*/        if(j > i)
/*  75*/            j = i;
/*  76*/        StringBuffer stringbuffer = new StringBuffer(j);
/*  77*/        int k = 1;
/*  78*/        int l = 0;
/*  79*/        double d1 = Round.round(d);
/*  82*/        while(l < i) 
                {
/*  82*/            if((double)k >= d1)
/*  83*/                stringbuffer.append(s.charAt(l));
/*  86*/            char c = s.charAt(l++);
/*  87*/            if(c < '\uD800' || c > '\uDBFF')
/*  87*/                k++;
                }
/*  89*/        return stringbuffer.toString();
            }

            private static String substring(String s, double d, double d1)
            {
/*  98*/        int i = s.length();
/*  99*/        int j = (int)d1;
/* 100*/        if(j < 0)
/* 100*/            j = 1;
/* 101*/        if(j > i)
/* 101*/            j = i;
/* 103*/        StringBuffer stringbuffer = new StringBuffer(j);
/* 104*/        int k = 1;
/* 105*/        int l = 0;
/* 106*/        double d2 = Round.round(d);
/* 107*/        double d3 = Round.round(d1);
/* 110*/        while(l < i) 
                {
/* 110*/            if((double)k >= d2)
                    {
/* 111*/                if((double)k >= d2 + d3)
/* 112*/                    break;
/* 112*/                stringbuffer.append(s.charAt(l));
                    }
/* 118*/            char c = s.charAt(l++);
/* 119*/            if(c < '\uD800' || c > '\uDBFF')
/* 119*/                k++;
                }
/* 122*/        return stringbuffer.toString();
            }

            public int getDependencies()
            {
/* 130*/        int i = super.argument[0].getDependencies() | super.argument[1].getDependencies();
/* 131*/        if(getNumberOfArguments() == 3)
/* 132*/            i |= super.argument[2].getDependencies();
/* 134*/        return i;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/* 142*/        Substring substring1 = new Substring();
/* 143*/        substring1.addArgument(super.argument[0].reduce(i, context));
/* 144*/        substring1.addArgument(super.argument[1].reduce(i, context));
/* 145*/        if(getNumberOfArguments() == 3)
/* 146*/            substring1.addArgument(super.argument[2].reduce(i, context));
/* 148*/        substring1.setStaticContext(getStaticContext());
/* 149*/        return substring1.simplify();
            }
}
