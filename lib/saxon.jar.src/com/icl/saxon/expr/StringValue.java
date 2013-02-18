// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StringValue.java

package com.icl.saxon.expr;

import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            Value, XPathException, Expression

public final class StringValue extends Value
{

            private String value;

            public StringValue(String s)
            {
/*  18*/        value = s != null ? s : "";
            }

            public String asString()
            {
/*  26*/        return value;
            }

            public double asNumber()
            {
/*  34*/        return Value.stringToNumber(value);
            }

            public boolean asBoolean()
            {
/*  43*/        return value.length() > 0;
            }

            public int getDataType()
            {
/*  52*/        return 3;
            }

            public int getLength()
            {
/*  61*/        return getLength(value);
            }

            public static int getLength(String s)
            {
/*  71*/        int i = 0;
/*  72*/        for(int j = 0; j < s.length(); j++)
                {
/*  73*/            char c = s.charAt(j);
/*  74*/            if(c < '\uD800' || c > '\uDBFF')
/*  74*/                i++;
                }

/*  76*/        return i;
            }

            public static int[] expand(String s)
            {
/*  84*/        int ai[] = new int[getLength(s)];
/*  85*/        int i = 0;
/*  86*/        for(int j = 0; j < s.length(); j++)
                {
/*  88*/            char c = s.charAt(j);
                    int k;
/*  89*/            if(c >= '\uD800' && c <= '\uDBFF')
                    {
/*  91*/                k = (c - 55296) * 1024 + (s.charAt(j + 1) - 56320) + 0x10000;
/*  92*/                j++;
                    } else
                    {
/*  94*/                k = c;
                    }
/*  96*/            ai[i++] = k;
                }

/*  98*/        return ai;
            }

            public boolean equals(StringValue stringvalue)
            {
/* 106*/        return value.equals(stringvalue.value);
            }

            public int conversionPreference(Class class1)
            {
/* 116*/        if(class1 == (java.lang.Object.class))
/* 116*/            return 50;
/* 117*/        if(class1.isAssignableFrom(com.icl.saxon.expr.StringValue.class))
/* 117*/            return 0;
/* 119*/        if(class1 == Boolean.TYPE)
/* 119*/            return 6;
/* 120*/        if(class1 == (java.lang.Boolean.class))
/* 120*/            return 7;
/* 121*/        if(class1 == Byte.TYPE)
/* 121*/            return 4;
/* 122*/        if(class1 == (java.lang.Byte.class))
/* 122*/            return 5;
/* 123*/        if(class1 == Character.TYPE)
/* 123*/            return 2;
/* 124*/        if(class1 == (java.lang.Character.class))
/* 124*/            return 3;
/* 125*/        if(class1 == Double.TYPE)
/* 125*/            return 4;
/* 126*/        if(class1 == (java.lang.Double.class))
/* 126*/            return 5;
/* 127*/        if(class1 == Float.TYPE)
/* 127*/            return 4;
/* 128*/        if(class1 == (java.lang.Float.class))
/* 128*/            return 5;
/* 129*/        if(class1 == Integer.TYPE)
/* 129*/            return 4;
/* 130*/        if(class1 == (java.lang.Integer.class))
/* 130*/            return 5;
/* 131*/        if(class1 == Long.TYPE)
/* 131*/            return 4;
/* 132*/        if(class1 == (java.lang.Long.class))
/* 132*/            return 5;
/* 133*/        if(class1 == Short.TYPE)
/* 133*/            return 4;
/* 134*/        if(class1 == (java.lang.Short.class))
/* 134*/            return 5;
/* 135*/        return class1 != (java.lang.String.class) ? 0x7fffffff : 0;
            }

            public Object convertToJava(Class class1)
                throws XPathException
            {
/* 145*/        if(class1 == (java.lang.Object.class))
/* 146*/            return value;
/* 147*/        if(class1.isAssignableFrom(com.icl.saxon.expr.StringValue.class))
/* 148*/            return this;
/* 149*/        if(class1 == Boolean.TYPE)
/* 150*/            return new Boolean(asBoolean());
/* 151*/        if(class1 == (java.lang.Boolean.class))
/* 152*/            return new Boolean(asBoolean());
/* 153*/        if(class1 == (java.lang.String.class))
/* 154*/            return value;
/* 155*/        if(class1 == Double.TYPE)
/* 156*/            return new Double(asNumber());
/* 157*/        if(class1 == (java.lang.Double.class))
/* 158*/            return new Double(asNumber());
/* 159*/        if(class1 == Float.TYPE)
/* 160*/            return new Float(asNumber());
/* 161*/        if(class1 == (java.lang.Float.class))
/* 162*/            return new Float(asNumber());
/* 163*/        if(class1 == Long.TYPE)
/* 164*/            return new Long((long)asNumber());
/* 165*/        if(class1 == (java.lang.Long.class))
/* 166*/            return new Long((long)asNumber());
/* 167*/        if(class1 == Integer.TYPE)
/* 168*/            return new Integer((int)asNumber());
/* 169*/        if(class1 == (java.lang.Integer.class))
/* 170*/            return new Integer((int)asNumber());
/* 171*/        if(class1 == Short.TYPE)
/* 172*/            return new Short((short)(int)asNumber());
/* 173*/        if(class1 == (java.lang.Short.class))
/* 174*/            return new Short((short)(int)asNumber());
/* 175*/        if(class1 == Byte.TYPE)
/* 176*/            return new Byte((byte)(int)asNumber());
/* 177*/        if(class1 == (java.lang.Byte.class))
/* 178*/            return new Byte((byte)(int)asNumber());
/* 179*/        if(class1 == Character.TYPE || class1 == (java.lang.Character.class))
                {
/* 180*/            if(value.length() == 1)
/* 181*/                return new Character(value.charAt(0));
/* 183*/            else
/* 183*/                throw new XPathException("Cannot convert string to Java char unless length is 1");
                } else
                {
/* 186*/            throw new XPathException("Conversion of string to " + class1.getName() + " is not supported");
                }
            }

            public void display(int i)
            {
/* 196*/        System.err.println(Expression.indent(i) + "string (\"" + value + "\")");
            }
}
