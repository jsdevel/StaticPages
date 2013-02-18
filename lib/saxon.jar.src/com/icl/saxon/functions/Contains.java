// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Contains.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;

public class Contains extends Function
{

            public Contains()
            {
            }

            public String getName()
            {
/*  16*/        return "contains";
            }

            public int getDataType()
            {
/*  25*/        return 1;
            }

            public Expression simplify()
                throws XPathException
            {
/*  34*/        checkArgumentCount(2, 2);
/*  35*/        super.argument[0] = super.argument[0].simplify();
/*  36*/        super.argument[1] = super.argument[1].simplify();
/*  38*/        if((super.argument[0] instanceof Value) && (super.argument[1] instanceof Value))
/*  39*/            return evaluate(null);
/*  42*/        if((super.argument[1] instanceof Value) && ((Value)super.argument[1]).asString().equals(""))
/*  44*/            return new BooleanValue(true);
/*  47*/        else
/*  47*/            return this;
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  55*/        String s = super.argument[0].evaluateAsString(context);
/*  56*/        String s1 = super.argument[1].evaluateAsString(context);
/*  57*/        return s.indexOf(s1) >= 0;
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  65*/        return new BooleanValue(evaluateAsBoolean(context));
            }

            public int getDependencies()
            {
/*  73*/        return super.argument[0].getDependencies() | super.argument[1].getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  81*/        Contains contains = new Contains();
/*  82*/        contains.addArgument(super.argument[0].reduce(i, context));
/*  83*/        contains.addArgument(super.argument[1].reduce(i, context));
/*  84*/        contains.setStaticContext(getStaticContext());
/*  85*/        return contains.simplify();
            }
}
