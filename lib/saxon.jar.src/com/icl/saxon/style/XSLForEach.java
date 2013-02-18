// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLForEach.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.trace.TraceListener;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames

public class XSLForEach extends StyleElement
{

            Expression select;

            public XSLForEach()
            {
/*  20*/        select = null;
            }

            public boolean isInstruction()
            {
/*  28*/        return true;
            }

            public boolean mayContainTemplateBody()
            {
/*  37*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  42*/        StandardNames standardnames = getStandardNames();
/*  43*/        AttributeCollection attributecollection = getAttributeList();
/*  45*/        String s = null;
/*  47*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  48*/            int j = attributecollection.getNameCode(i);
/*  49*/            int k = j & 0xfffff;
/*  50*/            if(k == standardnames.SELECT)
/*  51*/                s = attributecollection.getValue(i);
/*  53*/            else
/*  53*/                checkUnknownAttribute(j);
                }

/*  57*/        if(s == null)
/*  58*/            reportAbsence("select");
/*  60*/        else
/*  60*/            select = makeExpression(s);
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  66*/        checkWithinTemplate();
/*  67*/        select = handleSortKeys(select);
            }

            public void process(Context context)
                throws TransformerException
            {
/*  72*/        XSLTemplate xsltemplate = context.getCurrentTemplate();
/*  73*/        context.setCurrentTemplate(null);
/*  74*/        Object obj = select.enumerate(context, false);
/*  75*/        if(!(obj instanceof LastPositionFinder))
/*  76*/            obj = new LookaheadEnumerator(((NodeEnumeration) (obj)));
/*  79*/        Context context1 = context.newContext();
/*  80*/        context1.setLastPositionFinder((LastPositionFinder)obj);
/*  81*/        int i = 1;
/*  83*/        if(context.getController().isTracing())
                {
/*  84*/            TraceListener tracelistener = context.getController().getTraceListener();
/*  86*/            for(; ((NodeEnumeration) (obj)).hasMoreElements(); context.setReturnValue(context1.getReturnValue()))
                    {
/*  86*/                com.icl.saxon.om.NodeInfo nodeinfo1 = ((NodeEnumeration) (obj)).nextElement();
/*  87*/                context1.setPosition(i++);
/*  88*/                context1.setCurrentNode(nodeinfo1);
/*  89*/                context1.setContextNode(nodeinfo1);
/*  90*/                tracelistener.enterSource(null, context1);
/*  91*/                processChildren(context1);
/*  92*/                tracelistener.leaveSource(null, context1);
                    }

                } else
                {
/*  97*/            for(; ((NodeEnumeration) (obj)).hasMoreElements(); context.setReturnValue(context1.getReturnValue()))
                    {
/*  97*/                com.icl.saxon.om.NodeInfo nodeinfo = ((NodeEnumeration) (obj)).nextElement();
/*  98*/                context1.setPosition(i++);
/*  99*/                context1.setCurrentNode(nodeinfo);
/* 100*/                context1.setContextNode(nodeinfo);
/* 101*/                processChildren(context1);
                    }

                }
/* 105*/        context.setCurrentTemplate(xsltemplate);
            }
}
