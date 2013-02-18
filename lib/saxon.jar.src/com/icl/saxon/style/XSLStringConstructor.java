// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLStringConstructor.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.om.AbstractNode;
import com.icl.saxon.tree.NodeImpl;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, XSLValueOf

public abstract class XSLStringConstructor extends StyleElement
{

            private String stringValue;
            private Expression valueExpression;

            public XSLStringConstructor()
            {
/*  19*/        stringValue = null;
/*  20*/        valueExpression = null;
            }

            public boolean isInstruction()
            {
/*  28*/        return true;
            }

            public boolean mayContainTemplateBody()
            {
/*  37*/        return true;
            }

            protected void optimize()
                throws TransformerConfigurationException
            {
/*  41*/        NodeImpl nodeimpl = (NodeImpl)getFirstChild();
/*  42*/        if(nodeimpl == null)
                {
/*  44*/            stringValue = "";
                } else
                {
/*  46*/            NodeImpl nodeimpl1 = (NodeImpl)nodeimpl.getNextSibling();
/*  47*/            if(nodeimpl1 == null)
/*  49*/                if(nodeimpl.getNodeType() == 3)
/*  51*/                    stringValue = nodeimpl.getStringValue();
/*  52*/                else
/*  52*/                if(nodeimpl instanceof XSLValueOf)
                        {
/*  54*/                    XSLValueOf xslvalueof = (XSLValueOf)nodeimpl;
/*  55*/                    valueExpression = xslvalueof.getSelectExpression();
/*  56*/                    if(xslvalueof.getDisableOutputEscaping())
/*  57*/                        xslvalueof.compileError("disable-output-escaping is not allowed for a non-text node");
                        }
                }
            }

            public String expandChildren(Context context)
                throws TransformerException
            {
/*  72*/        if(stringValue != null)
/*  73*/            return stringValue;
/*  75*/        if(valueExpression != null)
                {
/*  76*/            return valueExpression.evaluateAsString(context);
                } else
                {
/*  79*/            Controller controller = context.getController();
/*  80*/            com.icl.saxon.output.Outputter outputter = controller.getOutputter();
/*  81*/            StringBuffer stringbuffer = new StringBuffer();
/*  82*/            controller.changeToTextOutputDestination(stringbuffer);
/*  83*/            processChildren(context);
/*  84*/            controller.resetOutputDestination(outputter);
/*  85*/            return stringbuffer.toString();
                }
            }
}
