// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Translate.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;

public class Translate extends Function
{

            public Translate()
            {
            }

            public String getName()
            {
/*  14*/        return "translate";
            }

            public int getDataType()
            {
/*  23*/        return 3;
            }

            public Expression simplify()
                throws XPathException
            {
/*  31*/        int i = checkArgumentCount(3, 3);
/*  32*/        super.argument[0] = super.argument[0].simplify();
/*  33*/        super.argument[1] = super.argument[1].simplify();
/*  34*/        super.argument[2] = super.argument[2].simplify();
/*  36*/        boolean flag = (super.argument[0] instanceof Value) && (super.argument[1] instanceof Value) && (super.argument[2] instanceof Value);
/*  40*/        if(flag)
/*  41*/            return evaluate(null);
/*  43*/        else
/*  43*/            return this;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  52*/        String s = super.argument[0].evaluateAsString(context);
/*  53*/        String s1 = super.argument[1].evaluateAsString(context);
/*  54*/        String s2 = super.argument[2].evaluateAsString(context);
/*  56*/        return translate(s, s1, s2);
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  64*/        return new StringValue(evaluateAsString(context));
            }

            public int getDependencies()
            {
/*  72*/        return super.argument[0].getDependencies() | super.argument[1].getDependencies() | super.argument[2].getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  82*/        Translate translate1 = new Translate();
/*  83*/        translate1.addArgument(super.argument[0].reduce(i, context));
/*  84*/        translate1.addArgument(super.argument[1].reduce(i, context));
/*  85*/        translate1.addArgument(super.argument[2].reduce(i, context));
/*  86*/        translate1.setStaticContext(getStaticContext());
/*  87*/        return translate1.simplify();
            }

            private static String translate(String s, String s1, String s2)
            {
/*  97*/        int i = StringValue.getLength(s);
/*  98*/        int j = StringValue.getLength(s1);
/*  99*/        int k = StringValue.getLength(s2);
/* 100*/        if(s.length() != i || s1.length() != j || s2.length() != k)
/* 103*/            return slowTranslate(s, s1, s2);
/* 106*/        StringBuffer stringbuffer = new StringBuffer();
/* 107*/        int l = s2.length();
/* 108*/        for(int i1 = 0; i1 < s.length(); i1++)
                {
/* 109*/            char c = s.charAt(i1);
/* 110*/            int j1 = s1.indexOf(c);
/* 111*/            if(j1 < l)
/* 112*/                stringbuffer.append(j1 >= 0 ? s2.charAt(j1) : c);
                }

/* 115*/        return stringbuffer.toString();
            }

            private static String slowTranslate(String s, String s1, String s2)
            {
/* 123*/        int ai[] = StringValue.expand(s);
/* 124*/        int ai1[] = StringValue.expand(s1);
/* 125*/        int ai2[] = StringValue.expand(s2);
/* 126*/        StringBuffer stringbuffer = new StringBuffer();
/* 127*/        for(int i = 0; i < ai.length; i++)
                {
/* 128*/            int j = ai[i];
/* 129*/            int k = -1;
/* 130*/            for(int l = 0; l < ai1.length; l++)
                    {
/* 131*/                if(ai1[l] != j)
/* 132*/                    continue;
/* 132*/                k = l;
/* 133*/                break;
                    }

/* 136*/            int i1 = -1;
/* 137*/            if(k < 0)
/* 138*/                i1 = ai[i];
/* 139*/            else
/* 139*/            if(k < ai2.length)
/* 140*/                i1 = ai2[k];
/* 145*/            if(i1 >= 0)
/* 146*/                if(i1 < 0x10000)
                        {
/* 147*/                    stringbuffer.append((char)i1);
                        } else
                        {
/* 153*/                    i1 -= 0x10000;
/* 154*/                    stringbuffer.append((char)(i1 / 1024 + 55296));
/* 155*/                    stringbuffer.append((char)(i1 % 1024 + 56320));
                        }
                }

/* 159*/        return stringbuffer.toString();
            }
}
