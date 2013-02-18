// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Math.java

package com.icl.saxon.exslt;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;
import java.util.Vector;

public abstract class Math
{

            public Math()
            {
            }

            public static double max(NodeEnumeration nodeenumeration)
                throws XPathException
            {
/*  21*/        double d = (-1.0D / 0.0D);
/*  23*/        while(nodeenumeration.hasMoreElements()) 
                {
/*  23*/            double d1 = Value.stringToNumber(nodeenumeration.nextElement().getStringValue());
/*  24*/            if(Double.isNaN(d1))
/*  24*/                return d1;
/*  25*/            if(d1 > d)
/*  25*/                d = d1;
                }
/*  27*/        return d;
            }

            public static double min(NodeEnumeration nodeenumeration)
                throws XPathException
            {
/*  36*/        double d = (1.0D / 0.0D);
/*  38*/        while(nodeenumeration.hasMoreElements()) 
                {
/*  38*/            double d1 = Value.stringToNumber(nodeenumeration.nextElement().getStringValue());
/*  39*/            if(Double.isNaN(d1))
/*  39*/                return d1;
/*  40*/            if(d1 < d)
/*  40*/                d = d1;
                }
/*  42*/        return d;
            }

            public static NodeSetValue highest(Context context, NodeEnumeration nodeenumeration)
                throws XPathException
            {
/*  51*/        double d = (-1.0D / 0.0D);
/*  52*/        Vector vector = new Vector();
/*  54*/        while(nodeenumeration.hasMoreElements()) 
                {
/*  54*/            NodeInfo nodeinfo = nodeenumeration.nextElement();
/*  55*/            double d1 = Value.stringToNumber(nodeinfo.getStringValue());
/*  56*/            if(Double.isNaN(d1))
/*  56*/                return new EmptyNodeSet();
/*  57*/            if(d1 == d)
/*  58*/                vector.addElement(nodeinfo);
/*  59*/            else
/*  59*/            if(d1 > d)
                    {
/*  60*/                d = d1;
/*  61*/                vector.removeAllElements();
/*  62*/                vector.addElement(nodeinfo);
                    }
                }
/*  65*/        return new NodeSetExtent(vector, context.getController());
            }

            public static NodeSetValue lowest(Context context, NodeEnumeration nodeenumeration)
                throws XPathException
            {
/*  75*/        double d = (1.0D / 0.0D);
/*  76*/        Vector vector = new Vector();
/*  78*/        while(nodeenumeration.hasMoreElements()) 
                {
/*  78*/            NodeInfo nodeinfo = nodeenumeration.nextElement();
/*  79*/            double d1 = Value.stringToNumber(nodeinfo.getStringValue());
/*  80*/            if(Double.isNaN(d1))
/*  80*/                return new EmptyNodeSet();
/*  81*/            if(d1 == d)
/*  82*/                vector.addElement(nodeinfo);
/*  83*/            else
/*  83*/            if(d1 < d)
                    {
/*  84*/                d = d1;
/*  85*/                vector.removeAllElements();
/*  86*/                vector.addElement(nodeinfo);
                    }
                }
/*  89*/        return new NodeSetExtent(vector, context.getController());
            }

            public static double abs(double d)
                throws XPathException
            {
/*  98*/        return Math.abs(d);
            }

            public static double sqrt(double d)
                throws XPathException
            {
/* 108*/        return Math.sqrt(d);
            }

            public static double power(double d, double d1)
                throws XPathException
            {
/* 117*/        return Math.pow(d, d1);
            }

            public static double constant(String s, double d)
                throws XPathException
            {
/* 127*/        String s1 = new String();
/* 129*/        if(s.equals("PI"))
/* 130*/            s1 = "3.1415926535897932384626433832795028841971693993751";
/* 131*/        else
/* 131*/        if(s.equals("E"))
/* 132*/            s1 = "2.71828182845904523536028747135266249775724709369996";
/* 133*/        else
/* 133*/        if(s.equals("SQRRT2"))
/* 134*/            s1 = "1.41421356237309504880168872420969807856967187537694";
/* 135*/        else
/* 135*/        if(s.equals("LN2"))
/* 136*/            s1 = "0.69314718055994530941723212145817656807550013436025";
/* 137*/        else
/* 137*/        if(s.equals("LN10"))
/* 138*/            s1 = "2.302585092994046";
/* 139*/        else
/* 139*/        if(s.equals("LOG2E"))
/* 140*/            s1 = "1.4426950408889633";
/* 141*/        else
/* 141*/        if(s.equals("SQRT1_2"))
/* 142*/            s1 = "0.7071067811865476";
/* 144*/        int i = (int)d;
/* 145*/        String s2 = s1.substring(0, i + 2);
/* 146*/        double d1 = (new Double(s2)).doubleValue();
/* 147*/        return d1;
            }

            public static double log(double d)
                throws XPathException
            {
/* 156*/        return Math.log(d);
            }

            public static double random()
                throws XPathException
            {
/* 166*/        return Math.random();
            }

            public static double sin(double d)
                throws XPathException
            {
/* 175*/        return Math.sin(d);
            }

            public static double cos(double d)
                throws XPathException
            {
/* 184*/        return Math.cos(d);
            }

            public static double tan(double d)
                throws XPathException
            {
/* 193*/        return Math.tan(d);
            }

            public static double asin(double d)
                throws XPathException
            {
/* 202*/        return Math.asin(d);
            }

            public static double acos(double d)
                throws XPathException
            {
/* 211*/        return Math.acos(d);
            }

            public static double atan(double d)
                throws XPathException
            {
/* 220*/        return Math.atan(d);
            }

            public static double atan2(double d, double d1)
                throws XPathException
            {
/* 229*/        return Math.atan2(d, d1);
            }

            public static double exp(double d)
                throws XPathException
            {
/* 238*/        return Math.exp(d);
            }
}
