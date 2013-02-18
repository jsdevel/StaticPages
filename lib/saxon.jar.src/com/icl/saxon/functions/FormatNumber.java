// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   FormatNumber.java

package com.icl.saxon.functions;

import com.icl.saxon.*;
import com.icl.saxon.expr.*;
import java.text.*;

public class FormatNumber extends Function
{

            private DecimalFormat decimalFormat;
            private String previousFormat;
            private DecimalFormatSymbols previousDFS;
            private Controller boundController;

            public FormatNumber()
            {
/*  13*/        decimalFormat = new DecimalFormat();
/*  14*/        previousFormat = "[null]";
/*  15*/        previousDFS = null;
/*  16*/        boundController = null;
            }

            public String getName()
            {
/*  19*/        return "format-number";
            }

            public int getDataType()
            {
/*  28*/        return 3;
            }

            public Expression simplify()
                throws XPathException
            {
/*  36*/        int i = checkArgumentCount(2, 3);
/*  41*/        super.argument[0] = super.argument[0].simplify();
/*  42*/        super.argument[1] = super.argument[1].simplify();
/*  43*/        if(i == 3)
/*  44*/            super.argument[2] = super.argument[2].simplify();
/*  46*/        return this;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  55*/        int i = getNumberOfArguments();
/*  57*/        Controller controller = boundController;
/*  58*/        if(controller == null)
/*  59*/            controller = context.getController();
/*  61*/        DecimalFormatManager decimalformatmanager = controller.getDecimalFormatManager();
/*  64*/        double d = super.argument[0].evaluateAsNumber(context);
/*  65*/        String s = super.argument[1].evaluateAsString(context);
                DecimalFormatSymbols decimalformatsymbols;
/*  67*/        if(i == 2)
                {
/*  68*/            decimalformatsymbols = decimalformatmanager.getDefaultDecimalFormat();
                } else
                {
/*  70*/            String s1 = super.argument[2].evaluateAsString(context);
/*  71*/            int j = getStaticContext().getFingerprint(s1, false);
/*  72*/            decimalformatsymbols = decimalformatmanager.getNamedDecimalFormat(j);
/*  73*/            if(decimalformatsymbols == null)
/*  74*/                throw new XPathException("format-number function: decimal-format " + s1 + " not registered");
                }
/*  78*/        return formatNumber(d, s, decimalformatsymbols);
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  86*/        return new StringValue(evaluateAsString(context));
            }

            public synchronized String formatNumber(double d, String s, DecimalFormatSymbols decimalformatsymbols)
                throws XPathException
            {
/* 102*/        try
                {
/* 102*/            DecimalFormat decimalformat = decimalFormat;
/* 103*/            if(decimalformatsymbols != previousDFS || !s.equals(previousFormat))
                    {
/* 104*/                decimalformat.setDecimalFormatSymbols(decimalformatsymbols);
/* 105*/                decimalformat.applyLocalizedPattern(s);
/* 106*/                previousDFS = decimalformatsymbols;
/* 107*/                previousFormat = s;
                    }
/* 109*/            return decimalformat.format(d);
                }
/* 111*/        catch(Exception exception)
                {
/* 111*/            throw new XPathException("Unable to interpret format pattern " + s + " (" + exception + ")");
                }
            }

            public int getDependencies()
            {
/* 120*/        int i = 0;
/* 121*/        if(boundController == null)
/* 122*/            i = 64;
/* 124*/        i |= super.argument[0].getDependencies();
/* 125*/        i |= super.argument[1].getDependencies();
/* 126*/        if(getNumberOfArguments() == 3)
/* 127*/            i |= super.argument[2].getDependencies();
/* 129*/        return i;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/* 137*/        FormatNumber formatnumber = new FormatNumber();
/* 138*/        formatnumber.addArgument(super.argument[0].reduce(i, context));
/* 139*/        formatnumber.addArgument(super.argument[1].reduce(i, context));
/* 140*/        if(getNumberOfArguments() == 3)
/* 141*/            formatnumber.addArgument(super.argument[2].reduce(i, context));
/* 143*/        if((i & 0x40) != 0)
/* 144*/            formatnumber.boundController = context.getController();
/* 146*/        formatnumber.setStaticContext(getStaticContext());
/* 147*/        return formatnumber;
            }
}
