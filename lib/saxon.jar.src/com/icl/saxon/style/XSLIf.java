// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLIf.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames

public class XSLIf extends StyleElement
{

            private Expression test;

            public XSLIf()
            {
            }

            public boolean isInstruction()
            {
/*  24*/        return true;
            }

            public boolean doesPostProcessing()
            {
/*  33*/        return false;
            }

            public boolean mayContainTemplateBody()
            {
/*  42*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  47*/        String s = null;
/*  49*/        StandardNames standardnames = getStandardNames();
/*  50*/        AttributeCollection attributecollection = getAttributeList();
/*  52*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  53*/            int j = attributecollection.getNameCode(i);
/*  54*/            int k = j & 0xfffff;
/*  55*/            if(k != standardnames.NAME)
/*  57*/                if(k == standardnames.TEST)
/*  58*/                    s = attributecollection.getValue(i);
/*  60*/                else
/*  60*/                    checkUnknownAttribute(j);
                }

/*  64*/        if(s == null)
/*  65*/            reportAbsence("test");
/*  67*/        else
/*  67*/            test = makeExpression(s);
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  72*/        checkWithinTemplate();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  77*/        if(test.evaluateAsBoolean(context))
/*  78*/            processChildren(context);
            }
}
