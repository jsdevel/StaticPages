// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLApplyTemplates.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.*;
import com.icl.saxon.pattern.AnyNodeTest;
import com.icl.saxon.tree.*;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, XSLSort, XSLWithParam, StandardNames, 
//            XSLStyleSheet, XSLGeneralVariable

public class XSLApplyTemplates extends StyleElement
{

            private Expression select;
            private boolean usesParams;
            private int modeNameCode;
            private Mode mode;
            private String modeAttribute;

            public XSLApplyTemplates()
            {
/*  25*/        modeNameCode = -1;
            }

            public boolean isInstruction()
            {
/*  35*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  41*/        StandardNames standardnames = getStandardNames();
/*  42*/        AttributeCollection attributecollection = getAttributeList();
/*  44*/        String s = null;
/*  46*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  47*/            int j = attributecollection.getNameCode(i);
/*  48*/            int k = j & 0xfffff;
/*  49*/            if(k == standardnames.MODE)
/*  50*/                modeAttribute = attributecollection.getValue(i);
/*  51*/            else
/*  51*/            if(k == standardnames.SELECT)
/*  52*/                s = attributecollection.getValue(i);
/*  54*/            else
/*  54*/                checkUnknownAttribute(j);
                }

/*  58*/        if(modeAttribute != null)
/*  59*/            if(Name.isQName(modeAttribute))
/*  61*/                try
                        {
/*  61*/                    modeNameCode = makeNameCode(modeAttribute, false);
                        }
/*  63*/                catch(NamespaceException namespaceexception)
                        {
/*  63*/                    compileError(namespaceexception.getMessage());
                        }
/*  66*/            else
/*  66*/            if(forwardsCompatibleModeIsEnabled())
/*  67*/                modeAttribute = null;
/*  69*/            else
/*  69*/                compileError("Mode name is not a valid QName");
/*  74*/        if(s != null)
/*  75*/            select = makeExpression(s);
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  81*/        checkWithinTemplate();
/*  84*/        mode = getPrincipalStyleSheet().getRuleManager().getMode(modeNameCode);
/*  88*/        boolean flag = false;
/*  89*/        for(NodeImpl nodeimpl = (NodeImpl)getFirstChild(); nodeimpl != null; nodeimpl = (NodeImpl)nodeimpl.getNextSibling())
/*  91*/            if(nodeimpl instanceof XSLSort)
/*  92*/                flag = true;
/*  93*/            else
/*  93*/            if(nodeimpl instanceof XSLWithParam)
/*  94*/                usesParams = true;
/*  96*/            else
/*  96*/            if(nodeimpl.getNodeType() == 3)
                    {
/*  98*/                if(!Navigator.isWhite(nodeimpl.getStringValue()))
/*  99*/                    compileError("No character data allowed within xsl:apply-templates");
                    } else
                    {
/* 103*/                compileError("Invalid element within xsl:apply-templates: ");
                    }

/* 109*/        if(select == null && flag)
/* 110*/            select = new PathExpression(new ContextNodeExpression(), new Step((byte)3, AnyNodeTest.getInstance()));
/* 114*/        if(select != null)
/* 115*/            select = handleSortKeys(select);
            }

            public void process(Context context)
                throws TransformerException
            {
/* 123*/        ParameterSet parameterset = null;
/* 124*/        if(usesParams)
                {
/* 125*/            parameterset = new ParameterSet();
/* 126*/            for(Node node = getFirstChild(); node != null; node = node.getNextSibling())
/* 128*/                if(node instanceof XSLWithParam)
                        {
/* 129*/                    XSLWithParam xslwithparam = (XSLWithParam)node;
/* 130*/                    parameterset.put(xslwithparam.getVariableFingerprint(), xslwithparam.getParamValue(context));
                        }

                }
/* 139*/        try
                {
/* 139*/            context.getController().applyTemplates(context, select, mode, parameterset);
                }
/* 141*/        catch(StackOverflowError stackoverflowerror)
                {
/* 141*/            throw new XPathException("Too many nested apply-templates calls");
                }
            }
}
