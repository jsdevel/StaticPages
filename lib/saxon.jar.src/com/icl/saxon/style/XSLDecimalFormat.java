// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLDecimalFormat.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.DecimalFormatManager;
import com.icl.saxon.om.Name;
import com.icl.saxon.om.NamespaceException;
import com.icl.saxon.tree.*;
import java.text.DecimalFormatSymbols;
import javax.xml.transform.TransformerConfigurationException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames, XSLStyleSheet

public class XSLDecimalFormat extends StyleElement
{

            String name;
            String decimalSeparator;
            String groupingSeparator;
            String infinity;
            String minusSign;
            String NaN;
            String percent;
            String perMille;
            String zeroDigit;
            String digit;
            String patternSeparator;

            public XSLDecimalFormat()
            {
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  31*/        StandardNames standardnames = getStandardNames();
/*  32*/        AttributeCollection attributecollection = getAttributeList();
/*  34*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  35*/            int j = attributecollection.getNameCode(i);
/*  36*/            int k = j & 0xfffff;
/*  37*/            if(k == standardnames.NAME)
/*  38*/                name = attributecollection.getValue(i);
/*  39*/            else
/*  39*/            if(k == standardnames.DECIMAL_SEPARATOR)
/*  40*/                decimalSeparator = attributecollection.getValue(i);
/*  41*/            else
/*  41*/            if(k == standardnames.GROUPING_SEPARATOR)
/*  42*/                groupingSeparator = attributecollection.getValue(i);
/*  43*/            else
/*  43*/            if(k == standardnames.INFINITY)
/*  44*/                infinity = attributecollection.getValue(i);
/*  45*/            else
/*  45*/            if(k == standardnames.MINUS_SIGN)
/*  46*/                minusSign = attributecollection.getValue(i);
/*  47*/            else
/*  47*/            if(k == standardnames.NAN)
/*  48*/                NaN = attributecollection.getValue(i);
/*  49*/            else
/*  49*/            if(k == standardnames.PERCENT)
/*  50*/                percent = attributecollection.getValue(i);
/*  51*/            else
/*  51*/            if(k == standardnames.PER_MILLE)
/*  52*/                perMille = attributecollection.getValue(i);
/*  53*/            else
/*  53*/            if(k == standardnames.ZERO_DIGIT)
/*  54*/                zeroDigit = attributecollection.getValue(i);
/*  55*/            else
/*  55*/            if(k == standardnames.DIGIT)
/*  56*/                digit = attributecollection.getValue(i);
/*  57*/            else
/*  57*/            if(k == standardnames.PATTERN_SEPARATOR)
/*  58*/                patternSeparator = attributecollection.getValue(i);
/*  60*/            else
/*  60*/                checkUnknownAttribute(j);
                }

            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  66*/        checkTopLevel();
            }

            public void preprocess()
                throws TransformerConfigurationException
            {
/*  72*/        DecimalFormatSymbols decimalformatsymbols = new DecimalFormatSymbols();
/*  73*/        DecimalFormatManager.setDefaults(decimalformatsymbols);
/*  74*/        if(decimalSeparator != null)
/*  75*/            decimalformatsymbols.setDecimalSeparator(toChar(decimalSeparator));
/*  77*/        if(groupingSeparator != null)
/*  78*/            decimalformatsymbols.setGroupingSeparator(toChar(groupingSeparator));
/*  80*/        if(infinity != null)
/*  81*/            decimalformatsymbols.setInfinity(infinity);
/*  83*/        if(minusSign != null)
/*  84*/            decimalformatsymbols.setMinusSign(toChar(minusSign));
/*  86*/        if(NaN != null)
/*  87*/            decimalformatsymbols.setNaN(NaN);
/*  89*/        if(percent != null)
/*  90*/            decimalformatsymbols.setPercent(toChar(percent));
/*  92*/        if(perMille != null)
/*  93*/            decimalformatsymbols.setPerMill(toChar(perMille));
/*  95*/        if(zeroDigit != null)
/*  96*/            decimalformatsymbols.setZeroDigit(toChar(zeroDigit));
/*  98*/        if(digit != null)
/*  99*/            decimalformatsymbols.setDigit(toChar(digit));
/* 101*/        if(patternSeparator != null)
/* 102*/            decimalformatsymbols.setPatternSeparator(toChar(patternSeparator));
/* 105*/        DecimalFormatManager decimalformatmanager = getPrincipalStyleSheet().getDecimalFormatManager();
/* 106*/        if(name == null)
                {
/* 107*/            decimalformatmanager.setDefaultDecimalFormat(decimalformatsymbols);
                } else
                {
/* 109*/            if(!Name.isQName(name))
/* 110*/                compileError("Name of decimal-format must be a valid QName");
                    int i;
/* 114*/            try
                    {
/* 114*/                i = makeNameCode(name, false) & 0xfffff;
                    }
/* 116*/            catch(NamespaceException namespaceexception)
                    {
/* 116*/                compileError(namespaceexception.getMessage());
/* 117*/                return;
                    }
/* 119*/            decimalformatmanager.setNamedDecimalFormat(i, decimalformatsymbols);
                }
            }

            public void process(Context context)
            {
            }

            private char toChar(String s)
                throws TransformerConfigurationException
            {
/* 126*/        if(s.length() != 1)
/* 127*/            compileError("Attribute \"" + s + "\" should be a single character");
/* 128*/        return s.charAt(0);
            }
}
