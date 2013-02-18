// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   BooleanValue.java

package com.icl.saxon.expr;

import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            Value, XPathException, Expression

public final class BooleanValue extends Value
{

            private boolean value;

            public BooleanValue(boolean flag)
            {
/*  18*/        value = flag;
            }

            public String asString()
            {
/*  27*/        return value ? "true" : "false";
            }

            public double asNumber()
            {
/*  36*/        return value ? 1.0D : 0.0D;
            }

            public boolean asBoolean()
            {
/*  45*/        return value;
            }

            public int getDataType()
            {
/*  55*/        return 1;
            }

            public int conversionPreference(Class class1)
            {
/*  65*/        if(class1 == (java.lang.Object.class))
/*  65*/            return 50;
/*  66*/        if(class1.isAssignableFrom(com.icl.saxon.expr.BooleanValue.class))
/*  66*/            return 0;
/*  68*/        if(class1 == Boolean.TYPE)
/*  68*/            return 0;
/*  69*/        if(class1 == (java.lang.Boolean.class))
/*  69*/            return 0;
/*  70*/        if(class1 == Byte.TYPE)
/*  70*/            return 3;
/*  71*/        if(class1 == (java.lang.Byte.class))
/*  71*/            return 4;
/*  72*/        if(class1 == Character.TYPE)
/*  72*/            return 0x7fffffff;
/*  73*/        if(class1 == (java.lang.Character.class))
/*  73*/            return 0x7fffffff;
/*  74*/        if(class1 == Double.TYPE)
/*  74*/            return 3;
/*  75*/        if(class1 == (java.lang.Double.class))
/*  75*/            return 4;
/*  76*/        if(class1 == Float.TYPE)
/*  76*/            return 3;
/*  77*/        if(class1 == (java.lang.Float.class))
/*  77*/            return 4;
/*  78*/        if(class1 == Integer.TYPE)
/*  78*/            return 3;
/*  79*/        if(class1 == (java.lang.Integer.class))
/*  79*/            return 4;
/*  80*/        if(class1 == Long.TYPE)
/*  80*/            return 3;
/*  81*/        if(class1 == (java.lang.Long.class))
/*  81*/            return 4;
/*  82*/        if(class1 == Short.TYPE)
/*  82*/            return 3;
/*  83*/        if(class1 == (java.lang.Short.class))
/*  83*/            return 4;
/*  84*/        return class1 != (java.lang.String.class) ? 0x7fffffff : 2;
            }

            public Object convertToJava(Class class1)
                throws XPathException
            {
/*  94*/        if(class1 == (java.lang.Object.class))
/*  95*/            return new Boolean(value);
/*  96*/        if(class1.isAssignableFrom(com.icl.saxon.expr.BooleanValue.class))
/*  97*/            return this;
/*  98*/        if(class1 == Boolean.TYPE)
/*  99*/            return new Boolean(value);
/* 100*/        if(class1 == (java.lang.Boolean.class))
/* 101*/            return new Boolean(value);
/* 102*/        if(class1 == (java.lang.Object.class))
/* 103*/            return new Boolean(value);
/* 104*/        if(class1 == (java.lang.String.class))
/* 105*/            return asString();
/* 106*/        if(class1 == Double.TYPE)
/* 107*/            return new Double(asNumber());
/* 108*/        if(class1 == (java.lang.Double.class))
/* 109*/            return new Double(asNumber());
/* 110*/        if(class1 == Float.TYPE)
/* 111*/            return new Float(asNumber());
/* 112*/        if(class1 == (java.lang.Float.class))
/* 113*/            return new Float(asNumber());
/* 114*/        if(class1 == Long.TYPE)
/* 115*/            return new Long(value ? 1L : 0L);
/* 116*/        if(class1 == (java.lang.Long.class))
/* 117*/            return new Long(value ? 1L : 0L);
/* 118*/        if(class1 == Integer.TYPE)
/* 119*/            return new Integer(value ? 1 : 0);
/* 120*/        if(class1 == (java.lang.Integer.class))
/* 121*/            return new Integer(value ? 1 : 0);
/* 122*/        if(class1 == Short.TYPE)
/* 123*/            return new Short(value ? 1 : 0);
/* 124*/        if(class1 == (java.lang.Short.class))
/* 125*/            return new Short(value ? 1 : 0);
/* 126*/        if(class1 == Byte.TYPE)
/* 127*/            return new Byte(((byte)(value ? 1 : 0)));
/* 128*/        if(class1 == (java.lang.Byte.class))
/* 129*/            return new Byte(((byte)(value ? 1 : 0)));
/* 131*/        else
/* 131*/            throw new XPathException("Conversion of boolean to " + class1.getName() + " is not supported");
            }

            public void display(int i)
            {
/* 141*/        System.err.println(Expression.indent(i) + "boolean (" + asString() + ")");
            }
}
