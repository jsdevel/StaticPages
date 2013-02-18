// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ObjectValue.java

package com.icl.saxon.expr;

import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            Value, XPathException, Expression

public class ObjectValue extends Value
{

            private Object value;

            public ObjectValue(Object obj)
            {
/*  20*/        value = obj;
            }

            public String asString()
            {
/*  29*/        return value != null ? value.toString() : "";
            }

            public double asNumber()
            {
/*  38*/        return value != null ? Value.stringToNumber(value.toString()) : (0.0D / 0.0D);
            }

            public boolean asBoolean()
            {
/*  47*/        return value != null ? value.toString().length() > 0 : false;
            }

            public int getDataType()
            {
/*  56*/        return 6;
            }

            public Object getObject()
            {
/*  64*/        return value;
            }

            public boolean equals(ObjectValue objectvalue)
            {
/*  72*/        return value.equals(objectvalue.value);
            }

            public int conversionPreference(Class class1)
            {
/*  81*/        if(class1 == Boolean.TYPE)
/*  81*/            return 0x7fffffff;
/*  82*/        if(class1 == (java.lang.Boolean.class))
/*  82*/            return 0x7fffffff;
/*  83*/        if(class1 == Byte.TYPE)
/*  83*/            return 4;
/*  84*/        if(class1 == (java.lang.Byte.class))
/*  84*/            return 5;
/*  85*/        if(class1 == Character.TYPE)
/*  85*/            return 2;
/*  86*/        if(class1 == (java.lang.Character.class))
/*  86*/            return 3;
/*  87*/        if(class1 == Double.TYPE)
/*  87*/            return 4;
/*  88*/        if(class1 == (java.lang.Double.class))
/*  88*/            return 5;
/*  89*/        if(class1 == Float.TYPE)
/*  89*/            return 4;
/*  90*/        if(class1 == (java.lang.Float.class))
/*  90*/            return 5;
/*  91*/        if(class1 == Integer.TYPE)
/*  91*/            return 4;
/*  92*/        if(class1 == (java.lang.Integer.class))
/*  92*/            return 5;
/*  93*/        if(class1 == Long.TYPE)
/*  93*/            return 4;
/*  94*/        if(class1 == (java.lang.Long.class))
/*  94*/            return 5;
/*  95*/        if(class1 == Short.TYPE)
/*  95*/            return 4;
/*  96*/        if(class1 == (java.lang.Short.class))
/*  96*/            return 5;
/*  97*/        if(class1 == (java.lang.String.class))
/*  97*/            return 1;
/*  98*/        if(class1 == (java.lang.Object.class))
/*  98*/            return 8;
/*  99*/        if(class1 == value.getClass())
/*  99*/            return -1;
/* 102*/        return !class1.isAssignableFrom(value.getClass()) ? 0x7fffffff : 0;
            }

            public Object convertToJava(Class class1)
                throws XPathException
            {
/* 112*/        if(value == null)
/* 112*/            return null;
/* 114*/        if(class1.isAssignableFrom(value.getClass()))
/* 115*/            return value;
/* 116*/        if(class1 == (com.icl.saxon.expr.Value.class) || class1 == (com.icl.saxon.expr.ObjectValue.class))
/* 117*/            return this;
/* 118*/        if(class1 == Boolean.TYPE)
/* 119*/            return new Boolean(asBoolean());
/* 120*/        if(class1 == (java.lang.Boolean.class))
/* 121*/            return new Boolean(asBoolean());
/* 122*/        if(class1 == (java.lang.String.class))
/* 123*/            return asString();
/* 124*/        if(class1 == Double.TYPE)
/* 125*/            return new Double(asNumber());
/* 126*/        if(class1 == (java.lang.Double.class))
/* 127*/            return new Double(asNumber());
/* 128*/        if(class1 == Float.TYPE)
/* 129*/            return new Float(asNumber());
/* 130*/        if(class1 == (java.lang.Float.class))
/* 131*/            return new Float(asNumber());
/* 132*/        if(class1 == Long.TYPE)
/* 133*/            return new Long((long)asNumber());
/* 134*/        if(class1 == (java.lang.Long.class))
/* 135*/            return new Long((long)asNumber());
/* 136*/        if(class1 == Integer.TYPE)
/* 137*/            return new Integer((int)asNumber());
/* 138*/        if(class1 == (java.lang.Integer.class))
/* 139*/            return new Integer((int)asNumber());
/* 140*/        if(class1 == Short.TYPE)
/* 141*/            return new Short((short)(int)asNumber());
/* 142*/        if(class1 == (java.lang.Short.class))
/* 143*/            return new Short((short)(int)asNumber());
/* 144*/        if(class1 == Byte.TYPE)
/* 145*/            return new Byte((byte)(int)asNumber());
/* 146*/        if(class1 == (java.lang.Byte.class))
/* 147*/            return new Byte((byte)(int)asNumber());
/* 148*/        if(class1 == Character.TYPE || class1 == (java.lang.Character.class))
                {
/* 149*/            String s = asString();
/* 150*/            if(s.length() == 1)
/* 151*/                return new Character(s.charAt(0));
/* 153*/            else
/* 153*/                throw new XPathException("Cannot convert string to Java char unless length is 1");
                } else
                {
/* 156*/            throw new XPathException("Conversion of external object to " + class1.getName() + " is not supported");
                }
            }

            public void display(int i)
            {
/* 166*/        System.err.println(Expression.indent(i) + "** external object **");
            }
}
