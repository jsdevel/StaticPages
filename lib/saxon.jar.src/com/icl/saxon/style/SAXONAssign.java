// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SAXONAssign.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.expr.XPathException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            XSLGeneralVariable, StyleElement

public class SAXONAssign extends XSLGeneralVariable
{

            private Binding binding;

            public SAXONAssign()
            {
            }

            public boolean isInstruction()
            {
/*  23*/        return true;
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  28*/        checkWithinTemplate();
/*  29*/        super.validate();
/*  31*/        try
                {
/*  31*/            binding = bindVariable(getVariableFingerprint());
                }
/*  34*/        catch(XPathException xpathexception)
                {
/*  34*/            compileError(xpathexception.getMessage());
/*  35*/            return;
                }
/*  37*/        if(!binding.isAssignable())
/*  38*/            compileError("Variable " + getVariableName() + " is not marked as assignable");
            }

            public boolean isAssignable()
            {
/*  43*/        return true;
            }

            public void process(Context context)
                throws TransformerException
            {
/*  48*/        com.icl.saxon.expr.Value value = getSelectValue(context);
/*  49*/        context.getController().getBindery().assignVariable(binding, value);
            }
}
