// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SAXONFunction.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.expr.StringValue;
import com.icl.saxon.expr.Value;
import com.icl.saxon.om.NamePool;
import com.icl.saxon.om.NamespaceException;
import com.icl.saxon.output.ErrorEmitter;
import com.icl.saxon.trace.TraceListener;
import com.icl.saxon.tree.*;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, Procedure, XSLParam, StandardNames, 
//            XSLStyleSheet, XSLGeneralVariable

public class SAXONFunction extends StyleElement
{

            int functionFingerprint;
            Procedure procedure;

            public SAXONFunction()
            {
/*  27*/        functionFingerprint = -1;
/*  28*/        procedure = new Procedure();
            }

            protected void processExtensionElementAttribute(int i)
                throws TransformerConfigurationException
            {
/*  39*/        super.extensionNamespaces = new short[1];
/*  40*/        NamePool namepool = getNamePool();
/*  41*/        short word0 = namepool.getURICode(getNameCode());
/*  42*/        super.extensionNamespaces[0] = word0;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  47*/        StandardNames standardnames = getStandardNames();
/*  48*/        AttributeCollection attributecollection = getAttributeList();
/*  50*/        String s = null;
/*  52*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  53*/            int j = attributecollection.getNameCode(i);
/*  54*/            int k = j & 0xfffff;
/*  55*/            if(k == standardnames.NAME)
                    {
/*  56*/                s = attributecollection.getValue(i);
/*  57*/                if(s.indexOf(':') < 0)
/*  58*/                    compileError("Function name must have a namespace prefix");
/*  61*/                try
                        {
/*  61*/                    int l = makeNameCode(s, false);
/*  62*/                    functionFingerprint = l & 0xfffff;
                        }
/*  64*/                catch(NamespaceException namespaceexception)
                        {
/*  64*/                    compileError(namespaceexception.getMessage());
                        }
                    } else
                    {
/*  67*/                checkUnknownAttribute(j);
                    }
                }

/*  71*/        if(s == null)
/*  72*/            reportAbsence("name");
            }

            public boolean mayContainTemplateBody()
            {
/*  82*/        return true;
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  86*/        checkTopLevel();
            }

            public void preprocess()
                throws TransformerConfigurationException
            {
/*  90*/        getPrincipalStyleSheet().allocateLocalSlots(procedure.getNumberOfVariables());
            }

            public void process(Context context)
            {
            }

            public Procedure getProcedure()
            {
/* 100*/        return procedure;
            }

            public int getFunctionFingerprint()
            {
/* 104*/        if(functionFingerprint == -1)
/* 107*/            try
                    {
/* 107*/                prepareAttributes();
                    }
/* 109*/            catch(TransformerConfigurationException transformerconfigurationexception)
                    {
/* 109*/                return -1;
                    }
/* 112*/        return functionFingerprint;
            }

            public int getNthParameter(int i)
            {
/* 121*/        NodeImpl nodeimpl = (NodeImpl)getFirstChild();
/* 122*/        int j = 0;
/* 124*/        for(; nodeimpl != null; nodeimpl = (NodeImpl)nodeimpl.getNextSibling())
/* 124*/            if(nodeimpl instanceof XSLParam)
                    {
/* 125*/                if(j == i)
/* 126*/                    return ((XSLParam)nodeimpl).getVariableFingerprint();
/* 128*/                j++;
                    }

/* 133*/        return -1;
            }

            public Value call(ParameterSet parameterset, Context context)
                throws TransformerException
            {
/* 141*/        Bindery bindery = context.getBindery();
/* 142*/        bindery.openStackFrame(parameterset);
/* 143*/        Controller controller = context.getController();
/* 144*/        com.icl.saxon.output.Outputter outputter = controller.getOutputter();
/* 145*/        controller.changeOutputDestination(null, new ErrorEmitter());
/* 147*/        if(controller.isTracing())
                {
/* 148*/            TraceListener tracelistener = controller.getTraceListener();
/* 149*/            tracelistener.enter(this, context);
/* 150*/            processChildren(context);
/* 151*/            tracelistener.leave(this, context);
                } else
                {
/* 153*/            processChildren(context);
                }
/* 156*/        controller.resetOutputDestination(outputter);
/* 157*/        bindery.closeStackFrame();
/* 158*/        Object obj = context.getReturnValue();
/* 159*/        if(obj == null)
/* 160*/            obj = new StringValue("");
/* 162*/        context.setReturnValue(null);
/* 163*/        return ((Value) (obj));
            }
}
