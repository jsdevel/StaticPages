// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLComment.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            XSLStringConstructor, StyleElement

public final class XSLComment extends XSLStringConstructor
{

            public XSLComment()
            {
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  18*/        AttributeCollection attributecollection = getAttributeList();
/*  19*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  20*/            int j = attributecollection.getNameCode(i);
/*  21*/            checkUnknownAttribute(j);
                }

            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  26*/        checkWithinTemplate();
/*  27*/        optimize();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  32*/        String s = expandChildren(context);
/*  36*/        do
                {
/*  36*/            int i = s.indexOf("--");
/*  37*/            if(i < 0)
/*  37*/                break;
/*  38*/            context.getController().reportRecoverableError("Invalid characters (--) in comment", this);
/*  39*/            s = s.substring(0, i + 1) + " " + s.substring(i + 1);
                } while(true);
/*  41*/        if(s.length() > 0 && s.charAt(s.length() - 1) == '-')
                {
/*  42*/            context.getController().reportRecoverableError("Invalid character (-) at end of comment", this);
/*  43*/            s = s + " ";
                }
/*  45*/        context.getOutputter().writeComment(s);
            }
}
