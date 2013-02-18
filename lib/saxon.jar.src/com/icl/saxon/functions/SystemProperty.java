// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SystemProperty.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.Version;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.Name;

public class SystemProperty extends Function
{

            public SystemProperty()
            {
            }

            public String getName()
            {
/*  16*/        return "system-property";
            }

            public int getDataType()
            {
/*  25*/        return -1;
            }

            public Expression simplify()
                throws XPathException
            {
/*  33*/        checkArgumentCount(1, 1);
/*  34*/        super.argument[0] = super.argument[0].simplify();
/*  35*/        if(super.argument[0] instanceof Value)
/*  36*/            return evaluate(null);
/*  38*/        else
/*  38*/            return this;
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  46*/        String s = super.argument[0].evaluateAsString(context);
/*  47*/        if(!Name.isQName(s))
/*  48*/            throw new XPathException("Argument " + s + " is not a valid QName");
/*  50*/        String s1 = Name.getPrefix(s);
/*  51*/        String s2 = Name.getLocalName(s);
                String s3;
/*  53*/        if(s1.equals(""))
/*  54*/            s3 = "";
/*  56*/        else
/*  56*/            s3 = getStaticContext().getURIForPrefix(s1);
/*  58*/        return getProperty(s3, s2);
            }

            public static Value getProperty(String s, String s1)
            {
/*  66*/        if(s.equals("http://www.w3.org/1999/XSL/Transform"))
                {
/*  67*/            if(s1.equals("version"))
/*  68*/                return new NumericValue(Version.getXSLVersion());
/*  69*/            if(s1.equals("vendor"))
/*  70*/                return new StringValue(Version.getProductName());
/*  71*/            if(s1.equals("vendor-url"))
/*  72*/                return new StringValue(Version.getWebSiteAddress());
/*  73*/            else
/*  73*/                return new StringValue("");
                }
/*  75*/        if(s.equals(""))
                {
/*  76*/            String s2 = System.getProperty(s1);
/*  77*/            if(s2 == null)
/*  77*/                s2 = "";
/*  78*/            return new StringValue(s2);
                } else
                {
/*  80*/            return new StringValue("");
                }
            }

            public int getDependencies()
            {
/*  89*/        return super.argument[0].getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  97*/        SystemProperty systemproperty = new SystemProperty();
/*  98*/        systemproperty.addArgument(super.argument[0].reduce(i, context));
/*  99*/        systemproperty.setStaticContext(getStaticContext());
/* 100*/        return systemproperty;
            }
}
