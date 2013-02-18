// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   AttributeValueTemplate.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            Expression, StringValue, XPathException, StaticContext, 
//            Value

public final class AttributeValueTemplate extends Expression
{

            private Expression components[];
            private int numberOfComponents;

            private AttributeValueTemplate(Expression aexpression[], int i)
            {
/*  13*/        components = new Expression[10];
/*  23*/        components = new Expression[i];
/*  24*/        numberOfComponents = i;
/*  25*/        System.arraycopy(aexpression, 0, components, 0, i);
            }

            public static Expression make(String s, StaticContext staticcontext)
                throws XPathException
            {
/*  34*/        if(s.indexOf("{") < 0 && s.indexOf("}") < 0)
/*  35*/            return new StringValue(s);
/*  38*/        int i = 0;
/*  39*/        Expression aexpression[] = new Expression[s.length()];
/*  42*/        byte byte0 = 32;
/*  43*/        int l1 = 0;
/*  44*/        for(int k1 = s.length(); l1 < k1;)
                {
/*  46*/            int j = s.indexOf("{", l1);
/*  47*/            int k = s.indexOf("{{", l1);
/*  48*/            int i1 = s.indexOf("}", l1);
/*  49*/            int j1 = s.indexOf("}}", l1);
/*  52*/            if(i1 >= 0 && (j < 0 || i1 < j))
                    {
/*  53*/                if(i1 != j1)
/*  54*/                    throw new XPathException("Closing curly brace in attribute value template \"" + s + "\" must be doubled");
/*  56*/                aexpression[i++] = new StringValue(s.substring(l1, i1 + 1));
/*  57*/                l1 = i1 + 2;
                    } else
/*  58*/            if(k >= 0 && k == j)
                    {
/*  59*/                aexpression[i++] = new StringValue(s.substring(l1, k + 1));
/*  60*/                l1 = k + 2;
                    } else
/*  61*/            if(j >= 0)
                    {
/*  62*/                if(j > l1)
/*  63*/                    aexpression[i++] = new StringValue(s.substring(l1, j));
                        int l;
/*  65*/                for(l = j + 1; l < k1; l++)
                        {
/*  66*/                    if(s.charAt(l) == '"')
/*  66*/                        byte0 = 34;
/*  67*/                    if(s.charAt(l) == '\'')
/*  67*/                        byte0 = 39;
/*  68*/                    if(byte0 != 32)
                            {
/*  69*/                        for(l++; l < k1 && s.charAt(l) != byte0; l++);
/*  71*/                        byte0 = 32;
/*  71*/                        continue;
                            }
/*  73*/                    if(s.charAt(l) == '}')
/*  75*/                        break;
                        }

/*  79*/                if(l >= k1)
/*  80*/                    throw new XPathException("No closing \"}\" in attribute value template " + s);
/*  83*/                String s1 = s.substring(j + 1, l);
/*  84*/                Expression expression = Expression.make(s1, staticcontext);
/*  85*/                aexpression[i++] = expression;
/*  86*/                l1 = l + 1;
                    } else
                    {
/*  88*/                aexpression[i++] = new StringValue(s.substring(l1));
/*  89*/                l1 = k1;
                    }
                }

/*  92*/        return (new AttributeValueTemplate(aexpression, i)).simplify();
            }

            public Expression simplify()
                throws XPathException
            {
/* 104*/        if(numberOfComponents == 0)
/* 105*/            return new StringValue("");
/* 109*/        if(numberOfComponents == 1)
/* 110*/            return components[0];
/* 114*/        for(int i = 0; i < numberOfComponents; i++)
/* 115*/            components[i] = components[i].simplify();

/* 119*/        return this;
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/* 129*/        return new StringValue(evaluateAsString(context));
            }

            public int getDataType()
            {
/* 138*/        return 3;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/* 148*/        StringBuffer stringbuffer = new StringBuffer();
/* 149*/        for(int i = 0; i < numberOfComponents; i++)
/* 150*/            stringbuffer.append(components[i].evaluateAsString(context));

/* 152*/        return stringbuffer.toString();
            }

            public int getDependencies()
            {
/* 162*/        int i = 0;
/* 163*/        for(int j = 0; j < numberOfComponents; j++)
/* 164*/            i |= components[j].getDependencies();

/* 166*/        return i;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/* 179*/        throw new XPathException("Cannot reduce expressions in an attribute value template");
            }

            public void display(int i)
            {
/* 187*/        System.err.println(Expression.indent(i) + "{<AVT>}");
            }
}
