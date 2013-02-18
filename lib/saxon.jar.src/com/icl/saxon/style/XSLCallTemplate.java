// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLCallTemplate.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.om.*;
import com.icl.saxon.tree.*;
import java.util.Vector;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, XSLTemplate, XSLWithParam, StandardNames, 
//            XSLStyleSheet, XSLGeneralVariable

public class XSLCallTemplate extends StyleElement
{

            private int calledTemplateFingerprint;
            private XSLTemplate template;
            private boolean useTailRecursion;
            private Expression calledTemplateExpression;
            private String calledTemplateName;

            public XSLCallTemplate()
            {
/*  22*/        calledTemplateFingerprint = -1;
/*  23*/        template = null;
/*  24*/        useTailRecursion = false;
/*  26*/        calledTemplateName = null;
            }

            public boolean isInstruction()
            {
/*  34*/        return true;
            }

            public boolean doesPostProcessing()
            {
/*  43*/        return false;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  48*/        StandardNames standardnames = getStandardNames();
/*  49*/        AttributeCollection attributecollection = getAttributeList();
/*  51*/        String s = null;
/*  52*/        String s1 = null;
/*  54*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  55*/            int j = attributecollection.getNameCode(i);
/*  56*/            int k = j & 0xfffff;
/*  57*/            if(k == standardnames.NAME)
/*  58*/                s1 = attributecollection.getValue(i);
/*  59*/            else
/*  59*/            if(k == standardnames.SAXON_ALLOW_AVT)
/*  60*/                s = attributecollection.getValue(i);
/*  62*/            else
/*  62*/                checkUnknownAttribute(j);
                }

/*  66*/        if(s1 == null)
                {
/*  67*/            reportAbsence("name");
/*  68*/            return;
                }
/*  71*/        boolean flag = s != null && s.equals("yes");
/*  72*/        if(flag)
                {
/*  73*/            calledTemplateExpression = makeAttributeValueTemplate(s1);
                } else
                {
/*  75*/            if(!Name.isQName(s1))
/*  76*/                compileError("Name of called template must be a valid QName");
/*  78*/            calledTemplateName = s1;
/*  80*/            try
                    {
/*  80*/                calledTemplateFingerprint = makeNameCode(s1, false) & 0xfffff;
                    }
/*  83*/            catch(NamespaceException namespaceexception)
                    {
/*  83*/                compileError(namespaceexception.getMessage());
                    }
                }
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  89*/        checkWithinTemplate();
/*  91*/        if(calledTemplateExpression == null)
                {
/*  92*/            template = findTemplate(calledTemplateFingerprint);
/*  99*/            if(Navigator.isAncestor(template, this))
                    {
/* 100*/                useTailRecursion = true;
/* 101*/                for(Object obj = this; obj != template; obj = (StyleElement)((AbstractNode) (obj)).getParentNode())
                        {
/* 103*/                    if((!((StyleElement) (obj)).isInstruction() || ((NodeImpl) (obj)).getNextSibling() == null) && !((StyleElement) (obj)).doesPostProcessing())
/* 105*/                        continue;
/* 105*/                    useTailRecursion = false;
/* 106*/                    break;
                        }

                    }
                }
            }

            private XSLTemplate findTemplate(int i)
                throws TransformerConfigurationException
            {
/* 119*/        XSLStyleSheet xslstylesheet = getPrincipalStyleSheet();
/* 120*/        Vector vector = xslstylesheet.getTopLevel();
/* 125*/        XSLTemplate xsltemplate = null;
/* 126*/        for(int j = vector.size() - 1; j >= 0; j--)
                {
/* 127*/            if(!(vector.elementAt(j) instanceof XSLTemplate))
/* 128*/                continue;
/* 128*/            XSLTemplate xsltemplate1 = (XSLTemplate)vector.elementAt(j);
/* 129*/            if(xsltemplate != null)
                    {
/* 130*/                if(xsltemplate1.getPrecedence() < xsltemplate.getPrecedence())
/* 131*/                    break;
/* 132*/                if(xsltemplate1.getTemplateFingerprint() == i)
/* 133*/                    compileError("There are several templates named '" + calledTemplateName + "' with the same import precedence");
                    }
/* 137*/            if(xsltemplate1.getTemplateFingerprint() == i)
/* 138*/                xsltemplate = xsltemplate1;
                }

/* 142*/        if(xsltemplate == null)
/* 143*/            compileError("No template exists named " + calledTemplateName);
/* 145*/        return xsltemplate;
            }

            public void process(Context context)
                throws TransformerException
            {
/* 152*/        XSLTemplate xsltemplate = template;
/* 153*/        if(calledTemplateExpression != null)
                {
/* 154*/            String s = calledTemplateExpression.evaluateAsString(context);
/* 155*/            if(!Name.isQName(s))
/* 156*/                throw styleError("Invalid template name: " + s);
                    int i;
/* 160*/            try
                    {
/* 160*/                i = makeNameCode(s, false) & 0xfffff;
                    }
/* 162*/            catch(NamespaceException namespaceexception)
                    {
/* 162*/                throw styleError(namespaceexception.getMessage());
                    }
/* 164*/            xsltemplate = findTemplate(i);
/* 165*/            if(xsltemplate == null)
/* 166*/                throw styleError("Template " + s + " has not been defined");
                }
/* 172*/        ParameterSet parameterset = null;
/* 174*/        if(hasChildNodes())
                {
/* 175*/            NodeImpl nodeimpl = (NodeImpl)getFirstChild();
/* 176*/            parameterset = new ParameterSet();
/* 178*/            for(; nodeimpl != null; nodeimpl = (NodeImpl)nodeimpl.getNextSibling())
/* 178*/                if(nodeimpl instanceof XSLWithParam)
                        {
/* 179*/                    XSLWithParam xslwithparam = (XSLWithParam)nodeimpl;
/* 180*/                    parameterset.put(xslwithparam.getVariableFingerprint(), xslwithparam.getParamValue(context));
                        }

                }
/* 188*/        if(useTailRecursion)
                {
/* 189*/            if(parameterset == null)
/* 190*/                parameterset = new ParameterSet();
/* 192*/            context.setTailRecursion(parameterset);
                } else
                {
/* 197*/            Bindery bindery = context.getBindery();
/* 198*/            bindery.openStackFrame(parameterset);
/* 200*/            if(context.getController().isTracing())
/* 201*/                xsltemplate.traceExpand(context);
/* 203*/            else
/* 203*/                xsltemplate.expand(context);
/* 206*/            bindery.closeStackFrame();
                }
            }
}
