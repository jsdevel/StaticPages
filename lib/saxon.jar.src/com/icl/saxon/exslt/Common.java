// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Common.java

package com.icl.saxon.exslt;

import com.icl.saxon.expr.*;

public abstract class Common
{

            public Common()
            {
            }

            public static NodeSetValue nodeSet(Value value)
                throws XPathException
            {
/*  18*/        if(value instanceof SingletonNodeSet)
/*  19*/            ((SingletonNodeSet)value).allowGeneralUse();
/*  21*/        if(value instanceof NodeSetValue)
/*  22*/            return (NodeSetValue)value;
/*  24*/        else
/*  24*/            throw new XPathException("exslt:node-set(): argument must be a node-set or tree");
            }

            public static String objectType(Value value)
            {
/*  34*/        if((value instanceof FragmentValue) || (value instanceof TextFragmentValue))
/*  35*/            return "RTF";
/*  36*/        if(value instanceof NodeSetValue)
/*  37*/            return "node-set";
/*  38*/        if(value instanceof StringValue)
/*  39*/            return "string";
/*  40*/        if(value instanceof NumericValue)
/*  41*/            return "number";
/*  42*/        if(value instanceof BooleanValue)
/*  43*/            return "boolean";
/*  45*/        else
/*  45*/            return "external";
            }
}
