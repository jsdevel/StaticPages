// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLGeneralVariable.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.*;
import com.icl.saxon.tree.*;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, XSLStyleSheet, XSLTemplate, SAXONFunction, 
//            XSLAttributeSet, Procedure, StandardNames

public abstract class XSLGeneralVariable extends StyleElement
{

            protected int variableFingerprint;
            protected Expression select;
            protected String simpleText;
            protected boolean global;
            protected Procedure procedure;
            protected boolean assignable;
            protected boolean redundant;

            public XSLGeneralVariable()
            {
/*  19*/        variableFingerprint = -1;
/*  20*/        select = null;
/*  21*/        simpleText = null;
/*  23*/        procedure = null;
/*  24*/        assignable = false;
/*  25*/        redundant = false;
            }

            public boolean mayContainTemplateBody()
            {
/*  33*/        return true;
            }

            public boolean isGlobal()
            {
/*  37*/        return getParentNode() instanceof XSLStyleSheet;
            }

            public boolean isAssignable()
            {
/*  47*/        return assignable;
            }

            public Procedure getOwningProcedure()
                throws TransformerConfigurationException
            {
/*  55*/        Object obj = this;
/*  57*/        do
                {
/*  57*/            NodeInfo nodeinfo = ((NodeInfo) (obj)).getParent();
/*  58*/            if(nodeinfo instanceof XSLStyleSheet)
                    {
/*  59*/                if(obj instanceof XSLTemplate)
/*  60*/                    return ((XSLTemplate)obj).getProcedure();
/*  61*/                if(obj instanceof XSLGeneralVariable)
/*  62*/                    return ((XSLGeneralVariable)obj).getProcedure();
/*  63*/                if(obj instanceof SAXONFunction)
/*  64*/                    return ((SAXONFunction)obj).getProcedure();
/*  65*/                if(obj instanceof XSLAttributeSet)
                        {
/*  66*/                    return ((XSLAttributeSet)obj).getProcedure();
                        } else
                        {
/*  68*/                    compileError("Local variable must be declared within a template");
/*  69*/                    return new Procedure();
                        }
                    }
/*  72*/            obj = nodeinfo;
                } while(true);
            }

            public void preprocess()
                throws TransformerConfigurationException
            {
/*  83*/        if(global)
/*  84*/            getPrincipalStyleSheet().allocateLocalSlots(procedure.getNumberOfVariables());
            }

            public String getVariableName()
            {
/*  93*/        return getAttributeValue("", "name");
            }

            public int getVariableFingerprint()
            {
/* 107*/        if(variableFingerprint == -1)
                {
/* 108*/            StandardNames standardnames = getStandardNames();
/* 109*/            String s = getAttributeValue(standardnames.NAME & 0xfffff);
/* 110*/            if(s == null)
/* 111*/                return -1;
/* 114*/            try
                    {
/* 114*/                variableFingerprint = makeNameCode(s, false) & 0xfffff;
                    }
/* 116*/            catch(NamespaceException namespaceexception)
                    {
/* 116*/                variableFingerprint = -1;
                    }
                }
/* 119*/        return variableFingerprint;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/* 124*/        getVariableFingerprint();
/* 126*/        StandardNames standardnames = getStandardNames();
/* 127*/        AttributeCollection attributecollection = getAttributeList();
/* 129*/        String s = null;
/* 130*/        String s1 = null;
/* 131*/        String s2 = null;
/* 133*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/* 134*/            int j = attributecollection.getNameCode(i);
/* 135*/            int k = j & 0xfffff;
/* 136*/            if(k == standardnames.NAME)
/* 137*/                s2 = attributecollection.getValue(i);
/* 138*/            else
/* 138*/            if(k == standardnames.SELECT)
/* 139*/                s = attributecollection.getValue(i);
/* 140*/            else
/* 140*/            if(k == standardnames.SAXON_ASSIGNABLE)
/* 141*/                s1 = attributecollection.getValue(i);
/* 143*/            else
/* 143*/                checkUnknownAttribute(j);
                }

/* 147*/        if(s2 == null)
/* 148*/            reportAbsence("name");
/* 149*/        else
/* 149*/        if(!Name.isQName(s2))
/* 150*/            compileError("Variable name must be a valid QName");
/* 154*/        if(s != null)
/* 155*/            select = makeExpression(s);
/* 158*/        if(s1 != null && s1.equals("yes"))
/* 159*/            assignable = true;
            }

            public void validate()
                throws TransformerConfigurationException
            {
/* 164*/        global = getParentNode() instanceof XSLStyleSheet;
/* 165*/        if(global)
/* 166*/            procedure = new Procedure();
/* 168*/        if(select != null && getFirstChild() != null)
/* 169*/            compileError("An " + getDisplayName() + " element with a select attribute must be empty");
/* 172*/        if(select == null)
                {
/* 173*/            NodeImpl nodeimpl = (NodeImpl)getFirstChild();
/* 174*/            if(nodeimpl == null)
                    {
/* 175*/                select = new StringValue("");
                    } else
                    {
/* 177*/                NodeImpl nodeimpl1 = (NodeImpl)nodeimpl.getNextSibling();
/* 178*/                if(nodeimpl1 == null && nodeimpl.getNodeType() == 3)
/* 182*/                    simpleText = nodeimpl.getStringValue();
                    }
                }
            }

            public void checkDuplicateDeclaration()
                throws TransformerConfigurationException
            {
/* 194*/        Binding binding = getVariableBinding(getVariableFingerprint());
/* 195*/        int i = getPrecedence();
/* 196*/        if(binding != null)
/* 197*/            if(global)
                    {
/* 198*/                int j = ((XSLGeneralVariable)binding).getPrecedence();
/* 199*/                if(i == j)
/* 200*/                    compileError("Duplicate global variable declaration");
/* 201*/                else
/* 201*/                if(i < j)
/* 202*/                    redundant = true;
/* 204*/                else
/* 204*/                    ((XSLGeneralVariable)binding).redundant = true;
                    } else
/* 207*/            if(!binding.isGlobal())
/* 209*/                compileError("Variable is already declared in this template");
            }

            protected Value getSelectValue(Context context)
                throws TransformerException
            {
/* 221*/        if(select == null)
                {
                    Object obj;
/* 223*/            if(simpleText != null)
                    {
/* 224*/                obj = new TextFragmentValue(simpleText, getSystemId(), context.getController());
                    } else
                    {
/* 228*/                Controller controller = context.getController();
/* 229*/                FragmentValue fragmentvalue = new FragmentValue(controller);
/* 230*/                com.icl.saxon.output.Outputter outputter = controller.getOutputter();
/* 231*/                controller.changeOutputDestination(null, fragmentvalue.getEmitter());
/* 232*/                if(global && procedure.getNumberOfVariables() > 0)
                        {
/* 233*/                    Bindery bindery = context.getBindery();
/* 234*/                    bindery.openStackFrame(new ParameterSet());
/* 235*/                    processChildren(context);
/* 236*/                    bindery.closeStackFrame();
                        } else
                        {
/* 238*/                    processChildren(context);
                        }
/* 240*/                controller.resetOutputDestination(outputter);
/* 241*/                fragmentvalue.setBaseURI(getSystemId());
/* 242*/                obj = fragmentvalue;
                    }
/* 244*/            if(forwardsCompatibleModeIsEnabled())
/* 245*/                ((SingletonNodeSet) (obj)).allowGeneralUse();
/* 247*/            return ((Value) (obj));
                }
/* 250*/        context.setStaticContext(super.staticContext);
/* 251*/        Object obj1 = select.evaluate(context);
/* 252*/        if(assignable && (obj1 instanceof NodeSetIntent))
/* 253*/            obj1 = new NodeSetExtent(((NodeSetIntent)obj1).enumerate(), context.getController());
/* 256*/        return ((Value) (obj1));
            }

            public Procedure getProcedure()
            {
/* 266*/        return procedure;
            }
}
