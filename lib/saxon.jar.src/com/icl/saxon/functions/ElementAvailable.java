// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ElementAvailable.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;

public class ElementAvailable extends Function
{

            public ElementAvailable()
            {
            }

            public String getName()
            {
/*  17*/        return "element-available";
            }

            public int getDataType()
            {
/*  26*/        return 1;
            }

            public Expression simplify()
                throws XPathException
            {
/*  35*/        checkArgumentCount(1, 1);
/*  36*/        super.argument[0] = super.argument[0].simplify();
/*  37*/        if(super.argument[0] instanceof Value)
/*  38*/            return evaluate(null);
/*  40*/        else
/*  40*/            return this;
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  48*/        String s = super.argument[0].evaluateAsString(context);
/*  49*/        return getStaticContext().isElementAvailable(s);
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  57*/        return new BooleanValue(evaluateAsBoolean(context));
            }

            public int getDependencies()
            {
/*  65*/        return super.argument[0].getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  73*/        ElementAvailable elementavailable = new ElementAvailable();
/*  74*/        elementavailable.addArgument(super.argument[0].reduce(i, context));
/*  75*/        elementavailable.setStaticContext(getStaticContext());
/*  76*/        return elementavailable.simplify();
            }
}
