// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SAXONWhile.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames

public class SAXONWhile extends StyleElement
{

            private Expression test;

            public SAXONWhile()
            {
            }

            public boolean isInstruction()
            {
/*  24*/        return true;
            }

            public boolean mayContainTemplateBody()
            {
/*  33*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  38*/        String s = null;
/*  40*/        StandardNames standardnames = getStandardNames();
/*  41*/        AttributeCollection attributecollection = getAttributeList();
/*  43*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  44*/            int j = attributecollection.getNameCode(i);
/*  45*/            int k = j & 0xfffff;
/*  46*/            if(k != standardnames.NAME)
/*  48*/                if(k == standardnames.TEST)
/*  49*/                    s = attributecollection.getValue(i);
/*  51*/                else
/*  51*/                    checkUnknownAttribute(j);
                }

/*  55*/        if(s == null)
                {
/*  56*/            reportAbsence("test");
/*  57*/            return;
                } else
                {
/*  59*/            test = makeExpression(s);
/*  60*/            return;
                }
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  63*/        checkWithinTemplate();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  70*/        for(; test.evaluateAsBoolean(context); processChildren(context));
            }
}
