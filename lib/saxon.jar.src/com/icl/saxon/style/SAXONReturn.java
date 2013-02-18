// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SAXONReturn.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.expr.StringValue;
import com.icl.saxon.om.AbstractNode;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.tree.*;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            XSLGeneralVariable, SAXONFunction, XSLFallback, StyleElement, 
//            StandardNames

public class SAXONReturn extends XSLGeneralVariable
{

            public SAXONReturn()
            {
            }

            public boolean isInstruction()
            {
/*  24*/        return true;
            }

            public int getVariableFingerprint()
            {
/*  28*/        return -1;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  33*/        StandardNames standardnames = getStandardNames();
/*  34*/        AttributeCollection attributecollection = getAttributeList();
/*  36*/        String s = null;
/*  38*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  39*/            int j = attributecollection.getNameCode(i);
/*  40*/            int k = j & 0xfffff;
/*  41*/            if(k == standardnames.SELECT)
/*  42*/                s = attributecollection.getValue(i);
/*  44*/            else
/*  44*/                checkUnknownAttribute(j);
                }

/*  48*/        if(s != null)
/*  49*/            super.select = makeExpression(s);
            }

            public void validate()
                throws TransformerConfigurationException
            {
                NodeInfo nodeinfo;
/*  63*/        for(nodeinfo = (NodeInfo)getParentNode(); nodeinfo != null; nodeinfo = nodeinfo.getParent())
                {
/*  65*/            if(nodeinfo instanceof SAXONFunction)
/*  65*/                break;
/*  66*/            if(nodeinfo instanceof XSLGeneralVariable)
/*  67*/                compileError(getDisplayName() + " must not be used within a variable definition");
                }

/*  72*/        if(nodeinfo == null)
/*  73*/            compileError(getDisplayName() + " must only be used within a function definition");
/*  79*/        NodeImpl nodeimpl = (NodeImpl)getNextSibling();
/*  80*/        if(nodeimpl != null && !(nodeimpl instanceof XSLFallback))
/*  81*/            compileError(getDisplayName() + " must be the last instruction in its template body");
/*  84*/        if(super.select == null && !hasChildNodes())
/*  86*/            super.select = new StringValue("");
            }

            public void process(Context context)
                throws TransformerException
            {
/*  97*/        com.icl.saxon.expr.Value value = getSelectValue(context);
/*  98*/        context.setReturnValue(value);
            }
}
