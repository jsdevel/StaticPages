// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLWithParam.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.expr.Value;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.tree.NodeImpl;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            XSLGeneralVariable, XSLApplyTemplates, XSLCallTemplate, XSLApplyImports, 
//            StyleElement

public class XSLWithParam extends XSLGeneralVariable
{

            public XSLWithParam()
            {
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  20*/        super.validate();
/*  22*/        NodeInfo nodeinfo = getParent();
/*  23*/        if(!(nodeinfo instanceof XSLApplyTemplates) && !(nodeinfo instanceof XSLCallTemplate) && !(nodeinfo instanceof XSLApplyImports))
/*  26*/            compileError("xsl:with-param cannot appear as a child of " + nodeinfo.getDisplayName());
/*  31*/        for(NodeImpl nodeimpl = (NodeImpl)getPreviousSibling(); nodeimpl != null; nodeimpl = (NodeImpl)nodeimpl.getPreviousSibling())
/*  33*/            if((nodeimpl instanceof XSLWithParam) && super.variableFingerprint == ((XSLGeneralVariable) ((XSLWithParam)nodeimpl)).variableFingerprint)
/*  35*/                compileError("Duplicate parameter name");

            }

            public void process(Context context)
                throws TransformerException
            {
            }

            public Value getParamValue(Context context)
                throws TransformerException
            {
/*  48*/        return getSelectValue(context);
            }
}
