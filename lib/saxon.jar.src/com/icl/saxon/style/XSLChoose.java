// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLChoose.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.trace.TraceListener;
import com.icl.saxon.tree.*;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, XSLWhen, XSLOtherwise

public class XSLChoose extends StyleElement
{

            private StyleElement otherwise;

            public XSLChoose()
            {
            }

            public boolean isInstruction()
            {
/*  25*/        return true;
            }

            public boolean doesPostProcessing()
            {
/*  34*/        return false;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  38*/        AttributeCollection attributecollection = getAttributeList();
/*  39*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  40*/            int j = attributecollection.getNameCode(i);
/*  41*/            checkUnknownAttribute(j);
                }

            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  46*/        checkWithinTemplate();
/*  48*/        NodeImpl nodeimpl = null;
/*  50*/        for(NodeImpl nodeimpl1 = (NodeImpl)getFirstChild(); nodeimpl1 != null; nodeimpl1 = (NodeImpl)nodeimpl1.getNextSibling())
/*  52*/            if(nodeimpl1 instanceof XSLWhen)
                    {
/*  53*/                if(otherwise != null)
/*  54*/                    compileError("xsl:otherwise must come last");
/*  56*/                nodeimpl = nodeimpl1;
                    } else
/*  57*/            if(nodeimpl1 instanceof XSLOtherwise)
                    {
/*  58*/                if(otherwise != null)
/*  59*/                    compileError("Only one xsl:otherwise allowed in an xsl:choose");
/*  61*/                else
/*  61*/                    otherwise = (StyleElement)nodeimpl1;
                    } else
                    {
/*  64*/                compileError("Only xsl:when and xsl:otherwise are allowed here");
                    }

/*  69*/        if(nodeimpl == null)
/*  70*/            compileError("xsl:choose must contain at least one xsl:when");
            }

            public void process(Context context)
                throws TransformerException
            {
/*  75*/        boolean flag = context.getController().isTracing();
/*  76*/        for(StyleElement styleelement = (StyleElement)getFirstChild(); styleelement != null;)
                {
                    boolean flag1;
/*  83*/            if(styleelement instanceof XSLWhen)
/*  84*/                flag1 = ((XSLWhen)styleelement).getCondition().evaluateAsBoolean(context);
/*  86*/            else
/*  86*/                flag1 = true;
/*  89*/            if(flag1)
                    {
/*  90*/                if(flag)
                        {
/*  91*/                    TraceListener tracelistener = context.getController().getTraceListener();
/*  92*/                    tracelistener.enter(styleelement, context);
/*  93*/                    styleelement.process(context);
/*  94*/                    tracelistener.leave(styleelement, context);
                        } else
                        {
/*  96*/                    styleelement.process(context);
                        }
/*  98*/                styleelement = null;
                    } else
                    {
/* 100*/                styleelement = (StyleElement)styleelement.getNextSibling();
                    }
                }

            }
}
