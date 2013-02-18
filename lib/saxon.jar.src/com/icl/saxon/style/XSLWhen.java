// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLWhen.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.om.AbstractNode;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, XSLChoose, StandardNames

public class XSLWhen extends StyleElement
{

            private Expression test;

            public XSLWhen()
            {
            }

            public Expression getCondition()
            {
/*  18*/        return test;
            }

            public boolean doesPostProcessing()
            {
/*  27*/        return false;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  31*/        String s = null;
/*  33*/        StandardNames standardnames = getStandardNames();
/*  34*/        AttributeCollection attributecollection = getAttributeList();
/*  36*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  37*/            int j = attributecollection.getNameCode(i);
/*  38*/            int k = j & 0xfffff;
/*  39*/            if(k != standardnames.NAME)
/*  41*/                if(k == standardnames.TEST)
/*  42*/                    s = attributecollection.getValue(i);
/*  44*/                else
/*  44*/                    checkUnknownAttribute(j);
                }

/*  48*/        if(s == null)
/*  49*/            reportAbsence("test");
/*  51*/        else
/*  51*/            test = makeExpression(s);
            }

            public boolean mayContainTemplateBody()
            {
/*  61*/        return true;
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  65*/        if(!(getParentNode() instanceof XSLChoose))
/*  66*/            compileError("xsl:when must be immediately within xsl:choose");
            }

            public void process(Context context)
                throws TransformerException
            {
/*  72*/        processChildren(context);
            }
}
