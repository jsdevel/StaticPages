// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLTemplate.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.expr.XPathException;
import com.icl.saxon.om.*;
import com.icl.saxon.pattern.Pattern;
import com.icl.saxon.trace.TraceListener;
import com.icl.saxon.tree.*;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, Procedure, XSLStyleSheet, StandardNames

public class XSLTemplate extends StyleElement
    implements NodeHandler
{
    private static final class NoOpHandler
        implements NodeHandler
    {

                public void start(NodeInfo nodeinfo, Context context)
                {
                }

                public boolean needsStackFrame()
                {
/* 261*/            return false;
                }

                private NoOpHandler()
                {
                }

    }


            protected int modeNameCode;
            protected int templateFingerprint;
            protected Pattern match;
            protected boolean prioritySpecified;
            protected double priority;
            protected Procedure procedure;
            protected boolean needsStackFrame;

            public XSLTemplate()
            {
/*  18*/        modeNameCode = -1;
/*  19*/        templateFingerprint = -1;
/*  23*/        procedure = new Procedure();
            }

            public boolean mayContainTemplateBody()
            {
/*  32*/        return true;
            }

            public int getTemplateFingerprint()
            {
/*  44*/        try
                {
/*  44*/            if(templateFingerprint == -1)
                    {
/*  46*/                StandardNames standardnames = getStandardNames();
/*  47*/                String s = getAttributeList().getValue(standardnames.NAME);
/*  48*/                if(s != null)
/*  49*/                    templateFingerprint = makeNameCode(s, false) & 0xfffff;
                    }
/*  53*/            return templateFingerprint;
                }
/*  55*/        catch(NamespaceException namespaceexception)
                {
/*  55*/            return -1;
                }
            }

            public int getMinImportPrecedence()
            {
/*  60*/        return ((XSLStyleSheet)getDocumentElement()).getMinImportPrecedence();
            }

            public boolean needsStackFrame()
            {
/*  64*/        return needsStackFrame;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  69*/        String s = null;
/*  70*/        String s1 = null;
/*  71*/        String s2 = null;
/*  72*/        String s3 = null;
/*  74*/        StandardNames standardnames = getStandardNames();
/*  75*/        AttributeCollection attributecollection = getAttributeList();
/*  77*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  78*/            int j = attributecollection.getNameCode(i);
/*  79*/            int k = j & 0xfffff;
/*  80*/            if(k == standardnames.MODE)
/*  81*/                s = attributecollection.getValue(i);
/*  82*/            else
/*  82*/            if(k == standardnames.NAME)
/*  83*/                s1 = attributecollection.getValue(i);
/*  84*/            else
/*  84*/            if(k == standardnames.MATCH)
/*  85*/                s3 = attributecollection.getValue(i);
/*  86*/            else
/*  86*/            if(k == standardnames.PRIORITY)
/*  87*/                s2 = attributecollection.getValue(i);
/*  89*/            else
/*  89*/                checkUnknownAttribute(j);
                }

/*  93*/        try
                {
/*  93*/            if(s != null)
/*  94*/                if(!Name.isQName(s))
                        {
/*  95*/                    if(forwardsCompatibleModeIsEnabled())
/*  96*/                        s = null;
/*  98*/                    else
/*  98*/                        compileError("Mode name is not a valid QName");
                        } else
                        {
/* 101*/                    modeNameCode = makeNameCode(s, false);
                        }
/* 105*/            if(s1 != null)
                    {
/* 106*/                if(!Name.isQName(s1))
/* 107*/                    compileError("Template name is not a valid QName");
/* 109*/                templateFingerprint = makeNameCode(s1, false) & 0xfffff;
                    }
                }
/* 112*/        catch(NamespaceException namespaceexception)
                {
/* 112*/            compileError(namespaceexception.getMessage());
                }
/* 115*/        prioritySpecified = s2 != null;
/* 116*/        if(prioritySpecified)
/* 118*/            try
                    {
/* 118*/                priority = Double.parseDouble(s2.trim());
                    }
/* 120*/            catch(NumberFormatException numberformatexception)
                    {
/* 120*/                compileError("Invalid numeric value for priority (" + priority + ')');
                    }
/* 124*/        if(s3 != null)
/* 125*/            match = makePattern(s3);
/* 128*/        if(match == null && s1 == null)
/* 129*/            compileError("xsl:template must have a a name or match attribute (or both)");
            }

            public void validate()
                throws TransformerConfigurationException
            {
/* 135*/        checkTopLevel();
/* 139*/        if(templateFingerprint != -1)
                {
/* 140*/            for(NodeImpl nodeimpl = (NodeImpl)getPreviousSibling(); nodeimpl != null; nodeimpl = (NodeImpl)nodeimpl.getPreviousSibling())
/* 142*/                if(nodeimpl instanceof XSLTemplate)
                        {
/* 143*/                    XSLTemplate xsltemplate = (XSLTemplate)nodeimpl;
/* 144*/                    if(xsltemplate.getTemplateFingerprint() == templateFingerprint && xsltemplate.getPrecedence() == getPrecedence())
/* 146*/                        compileError("There is another template with the same name and precedence");
                        }

                }
            }

            public void preprocess()
                throws TransformerConfigurationException
            {
/* 162*/        RuleManager rulemanager = getPrincipalStyleSheet().getRuleManager();
/* 163*/        com.icl.saxon.Mode mode = rulemanager.getMode(modeNameCode);
/* 165*/        if(match != null)
                {
/* 166*/            Object obj = this;
/* 167*/            if(getFirstChild() == null)
/* 169*/                obj = new NoOpHandler();
/* 171*/            if(prioritySpecified)
/* 172*/                rulemanager.setHandler(match, ((NodeHandler) (obj)), mode, getPrecedence(), priority);
/* 174*/            else
/* 174*/                rulemanager.setHandler(match, ((NodeHandler) (obj)), mode, getPrecedence());
                }
/* 178*/        getPrincipalStyleSheet().allocateLocalSlots(procedure.getNumberOfVariables());
/* 179*/        needsStackFrame = procedure.getNumberOfVariables() > 0;
            }

            public void process(Context context)
                throws TransformerException
            {
            }

            public void start(NodeInfo nodeinfo, Context context)
                throws TransformerException
            {
/* 197*/        context.setCurrentTemplate(this);
/* 199*/        if(context.getController().isTracing())
/* 200*/            traceExpand(context);
/* 202*/        else
/* 202*/            expand(context);
            }

            protected void traceExpand(Context context)
                throws TransformerException
            {
/* 213*/        TraceListener tracelistener = context.getController().getTraceListener();
/* 215*/        tracelistener.enter(this, context);
/* 216*/        expand(context);
/* 217*/        tracelistener.leave(this, context);
            }

            protected void expand(Context context)
                throws TransformerException
            {
                com.icl.saxon.ParameterSet parameterset;
/* 228*/        do
                {
/* 228*/            context.setTailRecursion(null);
/* 229*/            processChildren(context);
/* 230*/            parameterset = context.getTailRecursion();
/* 231*/            if(parameterset != null)
                    {
/* 232*/                context.getBindery().closeStackFrame();
/* 233*/                context.getBindery().openStackFrame(parameterset);
                    }
                } while(parameterset != null);
            }

            public Binding bindVariable(int i)
                throws XPathException
            {
/* 243*/        throw new XPathException("The match pattern in xsl:template may not contain references to variables");
            }

            public Procedure getProcedure()
            {
/* 251*/        return procedure;
            }
}
