// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLApplyImports.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.tree.*;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, XSLWithParam, XSLGeneralVariable, XSLTemplate

public class XSLApplyImports extends StyleElement
{

            public XSLApplyImports()
            {
            }

            public boolean isInstruction()
            {
/*  23*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  28*/        AttributeCollection attributecollection = getAttributeList();
/*  30*/        Object obj = null;
/*  32*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  33*/            int j = attributecollection.getNameCode(i);
/*  34*/            checkUnknownAttribute(j);
                }

            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  40*/        checkWithinTemplate();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  47*/        ParameterSet parameterset = null;
/*  49*/        if(hasChildNodes())
                {
/*  50*/            NodeImpl nodeimpl = (NodeImpl)getFirstChild();
/*  51*/            parameterset = new ParameterSet();
/*  53*/            for(; nodeimpl != null; nodeimpl = (NodeImpl)nodeimpl.getNextSibling())
/*  53*/                if(nodeimpl instanceof XSLWithParam)
                        {
/*  54*/                    XSLWithParam xslwithparam = (XSLWithParam)nodeimpl;
/*  55*/                    parameterset.put(xslwithparam.getVariableFingerprint(), xslwithparam.getParamValue(context));
                        }

                }
/*  61*/        XSLTemplate xsltemplate = context.getCurrentTemplate();
/*  62*/        if(xsltemplate == null)
                {
/*  63*/            throw new TransformerException("There is no current template");
                } else
                {
/*  66*/            int i = xsltemplate.getMinImportPrecedence();
/*  67*/            int j = xsltemplate.getPrecedence() - 1;
/*  68*/            context.getController().applyImports(context, context.getMode(), i, j, parameterset);
/*  73*/            context.setCurrentTemplate(xsltemplate);
/*  74*/            return;
                }
            }
}
