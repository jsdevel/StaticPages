// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Function.java

package com.icl.saxon.expr;

import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            Expression, XPathException

public abstract class Function extends Expression
{

            protected Expression argument[];
            private int numberOfArguments;

            public Function()
            {
/*  14*/        argument = new Expression[6];
/*  15*/        numberOfArguments = 0;
            }

            public void addArgument(Expression expression)
            {
/*  22*/        if(numberOfArguments >= argument.length)
                {
/*  23*/            Expression aexpression[] = new Expression[argument.length * 2];
/*  24*/            System.arraycopy(argument, 0, aexpression, 0, numberOfArguments);
/*  25*/            argument = aexpression;
                }
/*  27*/        argument[numberOfArguments++] = expression;
            }

            public int getNumberOfArguments()
            {
/*  35*/        return numberOfArguments;
            }

            public abstract String getName();

            protected int checkArgumentCount(int i, int j)
                throws XPathException
            {
/*  57*/        int k = numberOfArguments;
/*  58*/        if(i == j && k != i)
/*  59*/            throw new XPathException("Function " + getName() + " must have " + i + pluralArguments(i));
/*  61*/        if(k < i)
/*  62*/            throw new XPathException("Function " + getName() + " must have at least " + i + pluralArguments(i));
/*  64*/        if(k > j)
/*  65*/            throw new XPathException("Function " + getName() + " must have no more than " + j + pluralArguments(j));
/*  67*/        else
/*  67*/            return k;
            }

            private String pluralArguments(int i)
            {
/*  75*/        if(i == 1)
/*  75*/            return " argument";
/*  76*/        else
/*  76*/            return " arguments";
            }

            public void display(int i)
            {
/*  84*/        System.err.println(Expression.indent(i) + "function " + getName());
/*  85*/        for(int j = 0; j < numberOfArguments; j++)
/*  86*/            argument[j].display(i + 1);

            }
}
