// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Current.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;

public class Current extends Function
{

            public Current()
            {
            }

            public String getName()
            {
/*  15*/        return "current";
            }

            public int getDataType()
            {
/*  24*/        return 4;
            }

            public boolean isContextDocumentNodeSet()
            {
/*  34*/        return true;
            }

            public Expression simplify()
                throws XPathException
            {
/*  42*/        checkArgumentCount(0, 0);
/*  43*/        return this;
            }

            public NodeSetValue evaluateAsNodeSet(Context context)
                throws XPathException
            {
/*  51*/        return new SingletonNodeSet(context.getCurrentNodeInfo());
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  59*/        return evaluateAsNodeSet(context);
            }

            public int getDependencies()
            {
/*  67*/        return 4;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  75*/        if((i & 4) != 0)
/*  76*/            return evaluateAsNodeSet(context);
/*  78*/        else
/*  78*/            return this;
            }
}
