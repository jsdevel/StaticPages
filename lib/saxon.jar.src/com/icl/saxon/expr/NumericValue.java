// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NumericValue.java

package com.icl.saxon.expr;

import java.io.PrintStream;
import java.math.BigDecimal;

// Referenced classes of package com.icl.saxon.expr:
//            Value, XPathException, Expression

public final class NumericValue extends Value
{

            private double value;

            public NumericValue(double d)
            {
/*  20*/        value = d;
            }

            public NumericValue(String s)
            {
/*  29*/        value = Value.stringToNumber(s);
            }

            public String asStringOLD()
            {
/*  40*/        if(Double.isNaN(value))
/*  40*/            return "NaN";
/*  41*/        if(Double.isInfinite(value))
/*  41*/            return value <= 0.0D ? "-Infinity" : "Infinity";
/*  42*/        if(value == 0.0D)
/*  42*/            return "0";
/*  44*/        double d = Math.abs(value);
/*  45*/        StringBuffer stringbuffer = new StringBuffer();
/*  46*/        if(value < 0.0D)
/*  46*/            stringbuffer.append('-');
/*  47*/        int i = value >= 0.0D ? 0 : 1;
/*  48*/        double d1 = Math.floor(d);
/*  49*/        double d2 = d - d1;
/*  50*/        if(d1 >= 1.0D)
/*  52*/            for(; d1 >= 1.0D; d1 = Math.floor(d1 / 10D))
                    {
/*  52*/                int j = (int)(d1 % 10D);
/*  53*/                char c = (char)(j + 48);
/*  54*/                stringbuffer.insert(i, c);
                    }

/*  59*/        else
/*  59*/            stringbuffer.append('0');
/*  61*/        if(d2 > 0.0D)
                {
/*  62*/            stringbuffer.append('.');
                    double d3;
/*  64*/            for(; d2 > 0.0D; d2 = d3 % 1.0D)
                    {
/*  64*/                d3 = d2 * 10D;
/*  65*/                if(d3 < 1.0000000000010001D && d3 > 0.99999999999900002D)
/*  65*/                    d3 = 1.0D;
/*  66*/                double d4 = Math.floor(d3);
/*  67*/                char c1 = (char)((int)d4 + 48);
/*  68*/                stringbuffer.append(c1);
                    }

                }
/*  72*/        return stringbuffer.toString();
            }

            public String asString()
            {
/*  83*/        if(!Double.isInfinite(value) && (value >= 9007199254740992D || -value >= 9007199254740992D))
/*  86*/            return (new BigDecimal(value)).toString();
/*  88*/        String s = Double.toString(value);
/*  89*/        int i = s.length();
/*  90*/        if(s.charAt(i - 2) == '.' && s.charAt(i - 1) == '0')
                {
/*  91*/            s = s.substring(0, i - 2);
/*  92*/            if(s.equals("-0"))
/*  93*/                return "0";
/*  94*/            else
/*  94*/                return s;
                }
/*  96*/        int j = s.indexOf('E');
/*  97*/        if(j < 0)
/*  98*/            return s;
/*  99*/        int k = Integer.parseInt(s.substring(j + 1));
                String s1;
/* 101*/        if(s.charAt(0) == '-')
                {
/* 102*/            s1 = "-";
/* 103*/            s = s.substring(1);
/* 104*/            j--;
                } else
                {
/* 107*/            s1 = "";
                }
/* 109*/        int l = j - 2;
/* 110*/        if(k >= l)
/* 111*/            return s1 + s.substring(0, 1) + s.substring(2, j) + zeros(k - l);
/* 112*/        if(k > 0)
/* 113*/            return s1 + s.substring(0, 1) + s.substring(2, 2 + k) + "." + s.substring(2 + k, j);
/* 116*/        for(; s.charAt(j - 1) == '0'; j--);
/* 117*/        return s1 + "0." + zeros(-1 - k) + s.substring(0, 1) + s.substring(2, j);
            }

            private static String zeros(int i)
            {
/* 122*/        char ac[] = new char[i];
/* 123*/        for(int j = 0; j < i; j++)
/* 124*/            ac[j] = '0';

/* 125*/        return new String(ac);
            }

            public double asNumber()
            {
/* 134*/        return value;
            }

            public boolean asBoolean()
            {
/* 143*/        return value != 0.0D && !Double.isNaN(value);
            }

            public int getDataType()
            {
/* 153*/        return 2;
            }

            public int conversionPreference(Class class1)
            {
/* 164*/        if(class1 == (java.lang.Object.class))
/* 164*/            return 17;
/* 165*/        if(class1.isAssignableFrom(com.icl.saxon.expr.NumericValue.class))
/* 165*/            return 0;
/* 167*/        if(class1 == Boolean.TYPE)
/* 167*/            return 14;
/* 168*/        if(class1 == (java.lang.Boolean.class))
/* 168*/            return 15;
/* 169*/        if(class1 == Byte.TYPE)
/* 169*/            return 12;
/* 170*/        if(class1 == (java.lang.Byte.class))
/* 170*/            return 13;
/* 171*/        if(class1 == Character.TYPE)
/* 171*/            return 10;
/* 172*/        if(class1 == (java.lang.Character.class))
/* 172*/            return 11;
/* 173*/        if(class1 == Double.TYPE)
/* 173*/            return 0;
/* 174*/        if(class1 == (java.lang.Double.class))
/* 174*/            return 1;
/* 175*/        if(class1 == Float.TYPE)
/* 175*/            return 2;
/* 176*/        if(class1 == (java.lang.Float.class))
/* 176*/            return 3;
/* 177*/        if(class1 == Integer.TYPE)
/* 177*/            return 6;
/* 178*/        if(class1 == (java.lang.Integer.class))
/* 178*/            return 7;
/* 179*/        if(class1 == Long.TYPE)
/* 179*/            return 4;
/* 180*/        if(class1 == (java.lang.Long.class))
/* 180*/            return 5;
/* 181*/        if(class1 == Short.TYPE)
/* 181*/            return 8;
/* 182*/        if(class1 == (java.lang.Short.class))
/* 182*/            return 9;
/* 183*/        return class1 != (java.lang.String.class) ? 0x7fffffff : 16;
            }

            public Object convertToJava(Class class1)
                throws XPathException
            {
/* 192*/        if(class1 == (java.lang.Object.class))
/* 193*/            return new Double(value);
/* 194*/        if(class1.isAssignableFrom(com.icl.saxon.expr.NumericValue.class))
/* 195*/            return this;
/* 196*/        if(class1 == Boolean.TYPE)
/* 197*/            return new Boolean(asBoolean());
/* 198*/        if(class1 == (java.lang.Boolean.class))
/* 199*/            return new Boolean(asBoolean());
/* 200*/        if(class1 == (java.lang.String.class))
/* 201*/            return asString();
/* 202*/        if(class1 == Double.TYPE)
/* 203*/            return new Double(value);
/* 204*/        if(class1 == (java.lang.Double.class))
/* 205*/            return new Double(value);
/* 206*/        if(class1 == Float.TYPE)
/* 207*/            return new Float(value);
/* 208*/        if(class1 == (java.lang.Float.class))
/* 209*/            return new Float(value);
/* 210*/        if(class1 == Long.TYPE)
/* 211*/            return new Long((long)value);
/* 212*/        if(class1 == (java.lang.Long.class))
/* 213*/            return new Long((long)value);
/* 214*/        if(class1 == Integer.TYPE)
/* 215*/            return new Integer((int)value);
/* 216*/        if(class1 == (java.lang.Integer.class))
/* 217*/            return new Integer((int)value);
/* 218*/        if(class1 == Short.TYPE)
/* 219*/            return new Short((short)(int)value);
/* 220*/        if(class1 == (java.lang.Short.class))
/* 221*/            return new Short((short)(int)value);
/* 222*/        if(class1 == Byte.TYPE)
/* 223*/            return new Byte((byte)(int)value);
/* 224*/        if(class1 == (java.lang.Byte.class))
/* 225*/            return new Byte((byte)(int)value);
/* 226*/        if(class1 == Character.TYPE)
/* 227*/            return new Character((char)(int)value);
/* 228*/        if(class1 == (java.lang.Character.class))
/* 229*/            return new Character((char)(int)value);
/* 231*/        else
/* 231*/            throw new XPathException("Conversion of number to " + class1.getName() + " is not supported");
            }

            public void display(int i)
            {
/* 241*/        System.err.println(Expression.indent(i) + "number (" + asString() + ")");
            }
}
