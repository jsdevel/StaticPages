// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLValueOf.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.expr.ContextNodeExpression;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames

public final class XSLValueOf extends StyleElement
{

            private Expression select;
            private boolean disable;

            public XSLValueOf()
            {
/*  24*/        disable = false;
            }

            public boolean isInstruction()
            {
/*  32*/        return true;
            }

            public Expression getSelectExpression()
            {
/*  36*/        if(select == null)
/*  37*/            return new ContextNodeExpression();
/*  39*/        else
/*  39*/            return select;
            }

            public boolean getDisableOutputEscaping()
            {
/*  44*/        return disable;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  49*/        String s = null;
/*  50*/        String s1 = null;
/*  52*/        StandardNames standardnames = getStandardNames();
/*  53*/        AttributeCollection attributecollection = getAttributeList();
/*  55*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  56*/            int j = attributecollection.getNameCode(i);
/*  57*/            int k = j & 0xfffff;
/*  58*/            if(k == standardnames.DISABLE_OUTPUT_ESCAPING)
/*  59*/                s1 = attributecollection.getValue(i);
/*  60*/            else
/*  60*/            if(k == standardnames.SELECT)
/*  61*/                s = attributecollection.getValue(i);
/*  63*/            else
/*  63*/                checkUnknownAttribute(j);
                }

/*  67*/        if(s == null)
                {
/*  68*/            reportAbsence("select");
/*  69*/            return;
                }
/*  71*/        if(s.trim().equals("."))
/*  72*/            select = null;
/*  74*/        else
/*  74*/            select = makeExpression(s);
/*  77*/        if(s1 != null)
/*  78*/            if(s1.equals("yes"))
/*  79*/                disable = true;
/*  80*/            else
/*  80*/            if(s1.equals("no"))
/*  81*/                disable = false;
/*  83*/            else
/*  83*/                compileError("disable-output-escaping attribute must be either yes or no");
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  91*/        checkWithinTemplate();
/*  92*/        checkEmpty();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  97*/        Outputter outputter = context.getOutputter();
/*  98*/        if(disable)
/*  98*/            outputter.setEscaping(false);
/* 100*/        if(select == null)
/* 101*/            context.getCurrentNodeInfo().copyStringValue(outputter);
/* 103*/        else
/* 103*/            select.outputStringValue(outputter, context);
/* 106*/        if(disable)
/* 106*/            outputter.setEscaping(true);
            }
}
