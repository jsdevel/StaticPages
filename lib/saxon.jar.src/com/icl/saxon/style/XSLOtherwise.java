// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLOtherwise.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.om.AbstractNode;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, XSLChoose

public class XSLOtherwise extends StyleElement
{

            public XSLOtherwise()
            {
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  15*/        AttributeCollection attributecollection = getAttributeList();
/*  16*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  17*/            int j = attributecollection.getNameCode(i);
/*  18*/            checkUnknownAttribute(j);
                }

            }

            public boolean doesPostProcessing()
            {
/*  28*/        return false;
            }

            public boolean mayContainTemplateBody()
            {
/*  37*/        return true;
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  41*/        if(!(getParentNode() instanceof XSLChoose))
/*  42*/            compileError("xsl:otherwise must be immediately within xsl:choose");
            }

            public void process(Context context)
                throws TransformerException
            {
/*  48*/        processChildren(context);
            }
}
