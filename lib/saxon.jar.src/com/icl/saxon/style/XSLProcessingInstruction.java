// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLProcessingInstruction.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.om.Name;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            XSLStringConstructor, StyleElement, StandardNames

public class XSLProcessingInstruction extends XSLStringConstructor
{

            Expression name;

            public XSLProcessingInstruction()
            {
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  22*/        String s = null;
/*  24*/        StandardNames standardnames = getStandardNames();
/*  25*/        AttributeCollection attributecollection = getAttributeList();
/*  27*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  28*/            int j = attributecollection.getNameCode(i);
/*  29*/            int k = j & 0xfffff;
/*  30*/            if(k == standardnames.NAME)
/*  31*/                s = attributecollection.getValue(i);
/*  33*/            else
/*  33*/                checkUnknownAttribute(j);
                }

/*  36*/        if(s == null)
/*  37*/            reportAbsence("name");
/*  39*/        else
/*  39*/            name = makeAttributeValueTemplate(s);
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  44*/        checkWithinTemplate();
/*  45*/        optimize();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  51*/        String s = name.evaluateAsString(context);
/*  53*/        if(!Name.isNCName(s) || s.equalsIgnoreCase("xml"))
                {
/*  54*/            context.getController().reportRecoverableError("Processing instruction name is invalid: " + s, this);
/*  56*/            return;
                }
/*  59*/        String s1 = expandChildren(context);
/*  61*/        int i = s1.indexOf("?>");
/*  62*/        if(i >= 0)
                {
/*  63*/            context.getController().reportRecoverableError("Invalid characters (?>) in processing instruction", this);
/*  65*/            s1 = s1.substring(0, i + 1) + " " + s1.substring(i + 1);
                }
/*  68*/        context.getOutputter().writePI(s, s1);
            }
}
