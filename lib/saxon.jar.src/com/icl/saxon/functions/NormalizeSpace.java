// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NormalizeSpace.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeInfo;
import java.util.StringTokenizer;

public class NormalizeSpace extends Function
{

            public NormalizeSpace()
            {
            }

            public String getName()
            {
/*  18*/        return "normalize-space";
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
                {
/*  37*/            super.argument[0] = super.argument[0].simplify();
/*  38*/            if(super.argument[0] instanceof Value)
/*  39*/                return evaluate(null);
                }
/*  42*/        return this;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  50*/        if(getNumberOfArguments() == 1)
/*  51*/            return normalize(super.argument[0].evaluateAsString(context));
/*  53*/        else
/*  53*/            return normalize(context.getContextNodeInfo().getStringValue());
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  62*/        return new StringValue(evaluateAsString(context));
            }

            public int getDependencies()
            {
/*  70*/        if(getNumberOfArguments() == 1)
/*  71*/            return super.argument[0].getDependencies();
/*  73*/        else
/*  73*/            return 8;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  82*/        if(getNumberOfArguments() == 1)
                {
/*  83*/            NormalizeSpace normalizespace = new NormalizeSpace();
/*  84*/            normalizespace.addArgument(super.argument[0].reduce(i, context));
/*  85*/            normalizespace.setStaticContext(getStaticContext());
/*  86*/            return normalizespace.simplify();
                }
/*  88*/        if((i & 8) != 0)
/*  89*/            return evaluate(context);
/*  91*/        else
/*  91*/            return this;
            }

            private static String normalize(String s)
            {
/* 101*/        StringBuffer stringbuffer = new StringBuffer();
/* 102*/        for(StringTokenizer stringtokenizer = new StringTokenizer(s); stringtokenizer.hasMoreTokens();)
                {
/* 104*/            stringbuffer.append(stringtokenizer.nextToken());
/* 105*/            if(stringtokenizer.hasMoreTokens())
/* 105*/                stringbuffer.append(" ");
                }

/* 107*/        return stringbuffer.toString();
            }
}
