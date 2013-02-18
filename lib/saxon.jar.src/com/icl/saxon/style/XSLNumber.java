// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLNumber.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.Loader;
import com.icl.saxon.expr.*;
import com.icl.saxon.functions.Round;
import com.icl.saxon.number.*;
import com.icl.saxon.om.Navigator;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.pattern.Pattern;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import java.util.Vector;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames

public class XSLNumber extends StyleElement
{

            private static final int SINGLE = 0;
            private static final int MULTI = 1;
            private static final int ANY = 2;
            private static final int SIMPLE = 3;
            private int level;
            private Pattern count;
            private Pattern from;
            private Expression expr;
            private Expression format;
            private Expression groupSize;
            private Expression groupSeparator;
            private Expression letterValue;
            private Expression lang;
            private NumberFormatter formatter;
            private Numberer numberer;
            private static Numberer defaultNumberer = new Numberer_en();

            public XSLNumber()
            {
/*  24*/        count = null;
/*  25*/        from = null;
/*  26*/        expr = null;
/*  27*/        format = null;
/*  28*/        groupSize = null;
/*  29*/        groupSeparator = null;
/*  30*/        letterValue = null;
/*  31*/        lang = null;
/*  32*/        formatter = null;
/*  33*/        numberer = null;
            }

            public boolean isInstruction()
            {
/*  43*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  49*/        StandardNames standardnames = getStandardNames();
/*  50*/        AttributeCollection attributecollection = getAttributeList();
/*  52*/        String s = null;
/*  53*/        String s1 = null;
/*  54*/        String s2 = null;
/*  55*/        String s3 = null;
/*  56*/        String s4 = null;
/*  57*/        String s5 = null;
/*  58*/        String s6 = null;
/*  59*/        String s7 = null;
/*  60*/        String s8 = null;
/*  62*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  63*/            int j = attributecollection.getNameCode(i);
/*  64*/            int k = j & 0xfffff;
/*  65*/            if(k == standardnames.VALUE)
/*  66*/                s = attributecollection.getValue(i);
/*  67*/            else
/*  67*/            if(k == standardnames.COUNT)
/*  68*/                s1 = attributecollection.getValue(i);
/*  69*/            else
/*  69*/            if(k == standardnames.FROM)
/*  70*/                s2 = attributecollection.getValue(i);
/*  71*/            else
/*  71*/            if(k == standardnames.LEVEL)
/*  72*/                s3 = attributecollection.getValue(i);
/*  73*/            else
/*  73*/            if(k == standardnames.FORMAT)
/*  74*/                s4 = attributecollection.getValue(i);
/*  75*/            else
/*  75*/            if(k == standardnames.LANG)
/*  76*/                s7 = attributecollection.getValue(i);
/*  77*/            else
/*  77*/            if(k == standardnames.LETTER_VALUE)
/*  78*/                s8 = attributecollection.getValue(i);
/*  79*/            else
/*  79*/            if(k == standardnames.GROUPING_SIZE)
/*  80*/                s5 = attributecollection.getValue(i);
/*  81*/            else
/*  81*/            if(k == standardnames.GROUPING_SEPARATOR)
/*  82*/                s6 = attributecollection.getValue(i);
/*  84*/            else
/*  84*/                checkUnknownAttribute(j);
                }

/*  88*/        if(s != null)
/*  89*/            expr = makeExpression(s);
/*  92*/        if(s1 != null)
/*  93*/            count = makePattern(s1);
/*  96*/        if(s2 != null)
/*  97*/            from = makePattern(s2);
/* 100*/        if(s3 == null)
/* 101*/            level = 0;
/* 102*/        else
/* 102*/        if(s3.equals("single"))
/* 103*/            level = 0;
/* 104*/        else
/* 104*/        if(s3.equals("multiple"))
/* 105*/            level = 1;
/* 106*/        else
/* 106*/        if(s3.equals("any"))
/* 107*/            level = 2;
/* 109*/        else
/* 109*/            compileError("Invalid value for level attribute");
/* 112*/        if(level == 0 && from == null && count == null)
/* 113*/            level = 3;
/* 116*/        if(s4 != null)
                {
/* 117*/            format = makeAttributeValueTemplate(s4);
/* 118*/            if(format instanceof StringValue)
                    {
/* 119*/                formatter = new NumberFormatter();
/* 120*/                formatter.prepare(((StringValue)format).asString());
                    }
                } else
                {
/* 124*/            formatter = new NumberFormatter();
/* 125*/            formatter.prepare("1");
                }
/* 128*/        if(s6 != null && s5 != null)
                {
/* 130*/            groupSize = makeAttributeValueTemplate(s5);
/* 131*/            groupSeparator = makeAttributeValueTemplate(s6);
                }
/* 134*/        if(s7 == null)
                {
/* 135*/            numberer = defaultNumberer;
                } else
                {
/* 137*/            lang = makeAttributeValueTemplate(s7);
/* 138*/            if(lang instanceof StringValue)
/* 139*/                numberer = makeNumberer(((StringValue)lang).asString());
                }
/* 143*/        if(s8 != null)
/* 144*/            letterValue = makeAttributeValueTemplate(s8);
            }

            public void validate()
                throws TransformerConfigurationException
            {
/* 150*/        checkWithinTemplate();
/* 151*/        checkEmpty();
            }

            public void process(Context context)
                throws TransformerException
            {
/* 156*/        com.icl.saxon.om.NodeInfo nodeinfo = context.getCurrentNodeInfo();
/* 157*/        int i = -1;
/* 158*/        Vector vector = null;
/* 160*/        if(expr != null)
                {
/* 161*/            double d = expr.evaluateAsNumber(context);
/* 162*/            if(d < 0.5D || Double.isNaN(d) || Double.isInfinite(d) || d > 2147483647D)
                    {
/* 164*/                context.getOutputter().writeContent((new NumericValue(d)).asString());
/* 165*/                return;
                    }
/* 167*/            i = (int)Round.round(d);
                } else
/* 172*/        if(level == 3)
/* 173*/            i = Navigator.getNumberSimple(nodeinfo, context);
/* 174*/        else
/* 174*/        if(level == 0)
                {
/* 175*/            i = Navigator.getNumberSingle(nodeinfo, count, from, context);
/* 176*/            if(i == 0)
/* 177*/                vector = new Vector();
                } else
/* 179*/        if(level == 2)
                {
/* 180*/            i = Navigator.getNumberAny(nodeinfo, count, from, context);
/* 181*/            if(i == 0)
/* 182*/                vector = new Vector();
                } else
/* 184*/        if(level == 1)
/* 185*/            vector = Navigator.getNumberMulti(nodeinfo, count, from, context);
/* 189*/        int j = 0;
/* 190*/        String s = "";
/* 194*/        if(groupSize != null)
                {
/* 195*/            String s2 = groupSize.evaluateAsString(context);
/* 197*/            try
                    {
/* 197*/                j = Integer.parseInt(s2);
                    }
/* 199*/            catch(NumberFormatException numberformatexception)
                    {
/* 199*/                throw styleError("group-size must be numeric");
                    }
                }
/* 203*/        if(groupSeparator != null)
/* 204*/            s = groupSeparator.evaluateAsString(context);
/* 209*/        if(vector == null && format == null && j == 0 && lang == null)
                {
/* 210*/            context.getOutputter().writeContent("" + i);
/* 211*/            return;
                }
/* 214*/        if(numberer == null)
/* 215*/            numberer = makeNumberer(lang.evaluateAsString(context));
                String s1;
/* 218*/        if(letterValue == null)
                {
/* 219*/            s1 = "";
                } else
                {
/* 221*/            s1 = letterValue.evaluateAsString(context);
/* 222*/            if(!s1.equals("alphabetic") && !s1.equals("traditional"))
/* 223*/                throw styleError("letter-value must be \"traditional\" or \"alphabetic\"");
                }
/* 227*/        if(vector == null)
                {
/* 228*/            vector = new Vector();
/* 229*/            vector.addElement(new Integer(i));
                }
                NumberFormatter numberformatter;
/* 233*/        if(formatter == null)
                {
/* 234*/            numberformatter = new NumberFormatter();
/* 235*/            numberformatter.prepare(format.evaluateAsString(context));
                } else
                {
/* 237*/            numberformatter = formatter;
                }
/* 240*/        String s3 = numberformatter.format(vector, j, s, s1, numberer);
/* 241*/        context.getOutputter().writeContent(s3);
            }

            protected static Numberer makeNumberer(String s)
            {
                Numberer numberer1;
/* 251*/        if(s.equals("en"))
                {
/* 252*/            numberer1 = defaultNumberer;
                } else
                {
/* 254*/            String s1 = "com.icl.saxon.number.Numberer_";
/* 255*/            for(int i = 0; i < s.length(); i++)
/* 256*/                if(Character.isLetter(s.charAt(i)))
/* 257*/                    s1 = s1 + s.charAt(i);

/* 261*/            try
                    {
/* 261*/                numberer1 = (Numberer)Loader.getInstance(s1);
                    }
/* 263*/            catch(Exception exception)
                    {
/* 263*/                numberer1 = defaultNumberer;
                    }
                }
/* 267*/        return numberer1;
            }

}
