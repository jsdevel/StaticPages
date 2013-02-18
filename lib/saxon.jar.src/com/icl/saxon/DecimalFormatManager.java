// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DecimalFormatManager.java

package com.icl.saxon;

import java.text.DecimalFormatSymbols;
import java.util.Hashtable;
import javax.xml.transform.TransformerConfigurationException;

public class DecimalFormatManager
{

            private DecimalFormatSymbols defaultDFS;
            private Hashtable formatTable;
            private boolean usingOriginalDefault;

            public DecimalFormatManager()
            {
/*  17*/        usingOriginalDefault = true;
/*  24*/        formatTable = new Hashtable();
/*  25*/        DecimalFormatSymbols decimalformatsymbols = new DecimalFormatSymbols();
/*  26*/        setDefaults(decimalformatsymbols);
/*  27*/        defaultDFS = decimalformatsymbols;
            }

            public static void setDefaults(DecimalFormatSymbols decimalformatsymbols)
            {
/*  35*/        decimalformatsymbols.setDecimalSeparator('.');
/*  36*/        decimalformatsymbols.setGroupingSeparator(',');
/*  37*/        decimalformatsymbols.setInfinity("Infinity");
/*  38*/        decimalformatsymbols.setMinusSign('-');
/*  39*/        decimalformatsymbols.setNaN("NaN");
/*  40*/        decimalformatsymbols.setPercent('%');
/*  41*/        decimalformatsymbols.setPerMill('\u2030');
/*  42*/        decimalformatsymbols.setZeroDigit('0');
/*  43*/        decimalformatsymbols.setDigit('#');
/*  44*/        decimalformatsymbols.setPatternSeparator(';');
            }

            public void setDefaultDecimalFormat(DecimalFormatSymbols decimalformatsymbols)
                throws TransformerConfigurationException
            {
/*  55*/        if(!usingOriginalDefault && !decimalformatsymbols.equals(defaultDFS))
                {
/*  57*/            throw new TransformerConfigurationException("There are two conflicting definitions of the default decimal format");
                } else
                {
/*  61*/            defaultDFS = decimalformatsymbols;
/*  62*/            usingOriginalDefault = false;
/*  63*/            return;
                }
            }

            public DecimalFormatSymbols getDefaultDecimalFormat()
            {
/*  70*/        return defaultDFS;
            }

            public void setNamedDecimalFormat(int i, DecimalFormatSymbols decimalformatsymbols)
                throws TransformerConfigurationException
            {
/*  81*/        Integer integer = new Integer(i);
/*  82*/        DecimalFormatSymbols decimalformatsymbols1 = (DecimalFormatSymbols)formatTable.get(integer);
/*  83*/        if(decimalformatsymbols1 != null && !decimalformatsymbols.equals(decimalformatsymbols1))
                {
/*  85*/            throw new TransformerConfigurationException("Duplicate declaration of decimal-format");
                } else
                {
/*  88*/            formatTable.put(integer, decimalformatsymbols);
/*  89*/            return;
                }
            }

            public DecimalFormatSymbols getNamedDecimalFormat(int i)
            {
/*  99*/        return (DecimalFormatSymbols)formatTable.get(new Integer(i));
            }
}
