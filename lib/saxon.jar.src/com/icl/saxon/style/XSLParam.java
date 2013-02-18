// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLParam.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.expr.Value;
import com.icl.saxon.om.AbstractNode;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.tree.NodeImpl;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            XSLGeneralVariable, XSLTemplate, SAXONFunction, StyleElement, 
//            XSLStyleSheet, Procedure

public class XSLParam extends XSLGeneralVariable
    implements Binding
{

            private int slotNumber;

            public XSLParam()
            {
            }

            public int getSlotNumber()
            {
/*  21*/        return slotNumber;
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  26*/        super.validate();
/*  28*/        NodeInfo nodeinfo = (NodeInfo)getParentNode();
/*  29*/        boolean flag = (nodeinfo instanceof XSLTemplate) || (nodeinfo instanceof SAXONFunction);
/*  31*/        if(!flag && !super.global)
/*  32*/            compileError("xsl:param must be immediately within a template, function or stylesheet");
/*  35*/        checkDuplicateDeclaration();
/*  37*/        if(super.global && !super.redundant)
                {
/*  38*/            slotNumber = getPrincipalStyleSheet().allocateSlotNumber();
                } else
                {
/*  40*/            Procedure procedure = getOwningProcedure();
/*  41*/            slotNumber = procedure.allocateSlotNumber();
                }
/*  44*/        if(!super.global)
                {
/*  45*/            NodeInfo nodeinfo1 = (NodeInfo)getPreviousSibling();
/*  46*/            if(nodeinfo1 != null && !(nodeinfo1 instanceof XSLParam))
/*  47*/                compileError("xsl:param must be the first element within a template");
                }
            }

            public void process(Context context)
                throws TransformerException
            {
/*  55*/        if(super.redundant)
/*  55*/            return;
/*  57*/        Bindery bindery = context.getBindery();
                boolean flag;
/*  60*/        if(super.global)
/*  61*/            flag = bindery.useGlobalParameter(super.variableFingerprint, this);
/*  63*/        else
/*  63*/            flag = bindery.useLocalParameter(super.variableFingerprint, this);
/*  69*/        if(!flag)
/*  70*/            if(super.global)
                    {
/*  71*/                if(!super.redundant)
                        {
/*  72*/                    Value value = getSelectValue(context);
/*  73*/                    bindery.defineGlobalVariable(this, value);
                        }
                    } else
                    {
/*  76*/                Value value1 = getSelectValue(context);
/*  77*/                bindery.defineLocalVariable(this, value1);
                    }
            }

            public int getDataType()
            {
/*  88*/        return -1;
            }

            public Value constantValue()
            {
/*  97*/        return null;
            }
}
