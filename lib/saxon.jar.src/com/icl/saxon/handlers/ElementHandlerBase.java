// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ElementHandlerBase.java

package com.icl.saxon.handlers;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.om.NodeInfo;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.handlers:
//            ElementHandler

public class ElementHandlerBase extends ElementHandler
{

            public ElementHandlerBase()
            {
            }

            public void start(NodeInfo nodeinfo, Context context)
                throws TransformerException
            {
/*  24*/        if(nodeinfo.getNodeType() != 1)
                {
/*  25*/            throw new TransformerException("Element Handler called for a node that is not an element");
                } else
                {
/*  26*/            startElement(nodeinfo, context);
/*  27*/            return;
                }
            }

            public void startElement(NodeInfo nodeinfo, Context context)
                throws TransformerException
            {
/*  37*/        context.getController().applyTemplates(context, null, context.getMode(), null);
            }

            public boolean needsStackFrame()
            {
/*  42*/        return false;
            }
}
