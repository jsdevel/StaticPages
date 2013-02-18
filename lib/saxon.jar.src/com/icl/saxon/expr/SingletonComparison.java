// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SingletonComparison.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            Expression, BooleanValue, StringValue, FragmentValue, 
//            TextFragmentValue, XPathException, NumericValue, SingletonExpression, 
//            NodeSetValue, Value, Tokenizer

public class SingletonComparison extends Expression
{

            SingletonExpression node;
            int operator;
            Value value;

            public SingletonComparison(SingletonExpression singletonexpression, int i, Value value1)
            {
/*  17*/        node = singletonexpression;
/*  18*/        operator = i;
/*  19*/        value = value1;
            }

            public Expression simplify()
                throws XPathException
            {
/*  28*/        return this;
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  38*/        return new BooleanValue(evaluateAsBoolean(context));
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  48*/        boolean flag = node.evaluateAsBoolean(context);
/*  49*/        if(flag)
                {
/*  50*/            if((value instanceof StringValue) || (value instanceof FragmentValue) || (value instanceof TextFragmentValue))
                    {
/*  53*/                switch(operator)
                        {
/*  55*/                case 11: // '\013'
/*  55*/                    return node.evaluateAsString(context).equals(value.asString());

/*  57*/                case 34: // '"'
/*  57*/                    return !node.evaluateAsString(context).equals(value.asString());

/*  59*/                case 22: // '\026'
/*  59*/                    return node.evaluateAsNumber(context) < value.asNumber();

/*  61*/                case 24: // '\030'
/*  61*/                    return node.evaluateAsNumber(context) <= value.asNumber();

/*  63*/                case 21: // '\025'
/*  63*/                    return node.evaluateAsNumber(context) > value.asNumber();

/*  65*/                case 23: // '\027'
/*  65*/                    return node.evaluateAsNumber(context) >= value.asNumber();
                        }
/*  67*/                throw new XPathException("Bad operator in singleton comparison");
                    }
/*  69*/            if(value instanceof NumericValue)
                    {
/*  70*/                switch(operator)
                        {
/*  72*/                case 11: // '\013'
/*  72*/                    return node.evaluateAsNumber(context) == value.asNumber();

/*  74*/                case 34: // '"'
/*  74*/                    return node.evaluateAsNumber(context) != value.asNumber();

/*  76*/                case 22: // '\026'
/*  76*/                    return node.evaluateAsNumber(context) < value.asNumber();

/*  78*/                case 24: // '\030'
/*  78*/                    return node.evaluateAsNumber(context) <= value.asNumber();

/*  80*/                case 21: // '\025'
/*  80*/                    return node.evaluateAsNumber(context) > value.asNumber();

/*  82*/                case 23: // '\027'
/*  82*/                    return node.evaluateAsNumber(context) >= value.asNumber();
                        }
/*  84*/                throw new XPathException("Bad operator in singleton comparison");
                    } else
                    {
/*  87*/                throw new XPathException("Unrecognized type in singleton comparison");
                    }
                } else
                {
/*  91*/            return false;
                }
            }

            public int getDataType()
            {
/* 101*/        return 1;
            }

            public int getDependencies()
            {
/* 111*/        return node.getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/* 124*/        if((node.getDependencies() & i) != 0)
                {
/* 125*/            Object obj = node.reduce(i, context);
/* 126*/            if(obj instanceof SingletonExpression)
                    {
/* 127*/                obj = new SingletonComparison((SingletonExpression)obj, operator, value);
/* 131*/                ((Expression) (obj)).setStaticContext(getStaticContext());
/* 132*/                return ((Expression) (obj)).simplify();
                    }
/* 133*/            if(obj instanceof NodeSetValue)
/* 134*/                return new BooleanValue(((NodeSetValue)obj).compare(operator, value));
/* 136*/            else
/* 136*/                throw new XPathException("Failed to reduce SingletonComparison: returned " + obj.getClass());
                } else
                {
/* 139*/            return this;
                }
            }

            public void display(int i)
            {
/* 147*/        System.err.println(Expression.indent(i) + "SingletonComparison " + Tokenizer.tokens[operator]);
/* 148*/        node.display(i + 1);
/* 149*/        value.display(i + 1);
            }
}
