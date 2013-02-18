// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLFallback.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.om.AbstractNode;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement

public class XSLFallback extends StyleElement
{

            boolean active;

            public XSLFallback()
            {
            }

            public boolean isInstruction()
            {
/*  22*/        return true;
            }

            public boolean doesPostProcessing()
            {
/*  31*/        return false;
            }

            public boolean mayContainTemplateBody()
            {
/*  40*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  44*/        AttributeCollection attributecollection = getAttributeList();
/*  45*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  46*/            int j = attributecollection.getNameCode(i);
/*  47*/            checkUnknownAttribute(j);
                }

            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  52*/        checkWithinTemplate();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  56*/        if(((StyleElement)getParentNode()).validationError != null)
/*  57*/            processChildren(context);
            }
}
