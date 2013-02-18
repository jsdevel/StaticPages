// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Value.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;

// Referenced classes of package com.icl.saxon.expr:
//            Expression, NodeSetValue, BooleanValue, NumericValue, 
//            XPathException

public abstract class Value extends Expression
{

            public static final int BOOLEAN = 1;
            public static final int NUMBER = 2;
            public static final int STRING = 3;
            public static final int NODESET = 4;
            public static final int OBJECT = 6;
            public static final int ANY = -1;

            public Value()
            {
            }

            public static double stringToNumber(String s)
            {
/*  19*/        if(s.indexOf('+') >= 0 || s.indexOf('e') >= 0 || s.indexOf('E') >= 0)
/*  22*/            return (0.0D / 0.0D);
/*  25*/        try
                {
/*  25*/            return (new Double(s.trim())).doubleValue();
                }
/*  27*/        catch(NumberFormatException numberformatexception)
                {
/*  27*/            return (0.0D / 0.0D);
                }
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  50*/        return this;
            }

            public Expression simplify()
            {
/*  59*/        return this;
            }

            public int getDependencies()
            {
/*  69*/        return 0;
            }

            public abstract String asString()
                throws XPathException;

            public abstract double asNumber()
                throws XPathException;

            public abstract boolean asBoolean()
                throws XPathException;

            public boolean equals(Value value)
                throws XPathException
            {
/* 105*/        if(value instanceof NodeSetValue)
/* 106*/            return value.equals(this);
/* 108*/        if((this instanceof BooleanValue) || (value instanceof BooleanValue))
/* 109*/            return asBoolean() == value.asBoolean();
/* 111*/        if((this instanceof NumericValue) || (value instanceof NumericValue))
/* 112*/            return asNumber() == value.asNumber();
/* 114*/        else
/* 114*/            return asString().equals(value.asString());
            }

            public boolean notEquals(Value value)
                throws XPathException
            {
/* 128*/        if(value instanceof NodeSetValue)
/* 129*/            return value.notEquals(this);
/* 131*/        else
/* 131*/            return !equals(value);
            }

            public boolean compare(int i, Value value)
                throws XPathException
            {
/* 143*/        if(i == 11)
/* 143*/            return equals(value);
/* 144*/        if(i == 34)
/* 144*/            return notEquals(value);
/* 146*/        if(value instanceof NodeSetValue)
/* 147*/            return value.compare(inverse(i), this);
/* 150*/        else
/* 150*/            return numericCompare(i, asNumber(), value.asNumber());
            }

            protected static final int inverse(int i)
            {
/* 159*/        switch(i)
                {
/* 161*/        case 22: // '\026'
/* 161*/            return 21;

/* 163*/        case 24: // '\030'
/* 163*/            return 23;

/* 165*/        case 21: // '\025'
/* 165*/            return 22;

/* 167*/        case 23: // '\027'
/* 167*/            return 24;
                }
/* 169*/        return i;
            }

            protected final boolean numericCompare(int i, double d, double d1)
            {
/* 175*/        switch(i)
                {
/* 177*/        case 22: // '\026'
/* 177*/            return d < d1;

/* 179*/        case 24: // '\030'
/* 179*/            return d <= d1;

/* 181*/        case 21: // '\025'
/* 181*/            return d > d1;

/* 183*/        case 23: // '\027'
/* 183*/            return d >= d1;
                }
/* 185*/        return false;
            }

            public Expression reduce(int i, Context context)
            {
/* 199*/        return this;
            }

            public abstract Object convertToJava(Class class1)
                throws XPathException;

            public abstract int conversionPreference(Class class1);
}
