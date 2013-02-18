// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SAXONHandler.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            XSLTemplate, StyleElement, StandardNames, XSLStyleSheet

public class SAXONHandler extends XSLTemplate
{

            private NodeHandler handler;

            public SAXONHandler()
            {
            }

            public void checkUnknownAttribute(int i)
                throws TransformerConfigurationException
            {
/*  20*/        StandardNames standardnames = getStandardNames();
/*  21*/        AttributeCollection attributecollection = getAttributeList();
/*  23*/        int j = i & 0xfffff;
/*  24*/        if(j == standardnames.HANDLER)
                {
/*  25*/            String s = attributecollection.getValueByFingerprint(j);
/*  26*/            handler = makeHandler(s);
                } else
                {
/*  28*/            super.checkUnknownAttribute(i);
                }
/*  31*/        if(handler == null)
/*  32*/            reportAbsence("handler");
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  39*/        if(handler == null)
/*  40*/            reportAbsence("handler");
/*  42*/        checkTopLevel();
            }

            public void preprocess()
                throws TransformerConfigurationException
            {
/*  51*/        RuleManager rulemanager = getPrincipalStyleSheet().getRuleManager();
/*  52*/        com.icl.saxon.Mode mode = rulemanager.getMode(super.modeNameCode);
/*  53*/        if(super.match != null)
/*  54*/            if(super.prioritySpecified)
/*  55*/                rulemanager.setHandler(super.match, handler, mode, getPrecedence(), super.priority);
/*  57*/            else
/*  57*/                rulemanager.setHandler(super.match, handler, mode, getPrecedence());
            }

            public void process(Context context)
                throws TransformerException
            {
            }

            public void expand(Context context)
                throws TransformerException
            {
/*  76*/        handler.start(context.getCurrentNodeInfo(), context);
            }

            protected NodeHandler makeHandler(String s)
                throws TransformerConfigurationException
            {
/*  87*/        try
                {
/*  87*/            return (NodeHandler)Loader.getInstance(s);
                }
/*  89*/        catch(TransformerException transformerexception)
                {
/*  89*/            compileError(transformerexception);
                }
/*  91*/        catch(ClassCastException classcastexception)
                {
/*  91*/            compileError("Node handler " + s + " does not implement the NodeHandler interface");
                }
/*  94*/        return null;
            }
}
