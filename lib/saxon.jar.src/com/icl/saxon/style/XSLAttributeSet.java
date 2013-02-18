// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLAttributeSet.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.om.Name;
import com.icl.saxon.om.NamespaceException;
import com.icl.saxon.tree.*;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, Procedure, XSLAttribute, StandardNames, 
//            XSLStyleSheet

public class XSLAttributeSet extends StyleElement
{

            int fingerprint;
            String use;
            Procedure procedure;

            public XSLAttributeSet()
            {
/*  22*/        procedure = new Procedure();
            }

            public int getAttributeSetFingerprint()
            {
/*  25*/        return fingerprint;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  30*/        String s = null;
/*  31*/        use = null;
/*  33*/        StandardNames standardnames = getStandardNames();
/*  34*/        AttributeCollection attributecollection = getAttributeList();
/*  36*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  37*/            int j = attributecollection.getNameCode(i);
/*  38*/            int k = j & 0xfffff;
/*  39*/            if(k == standardnames.NAME)
/*  40*/                s = attributecollection.getValue(i);
/*  41*/            else
/*  41*/            if(k == standardnames.USE_ATTRIBUTE_SETS)
/*  42*/                use = attributecollection.getValue(i);
/*  44*/            else
/*  44*/                checkUnknownAttribute(j);
                }

/*  48*/        if(s == null)
                {
/*  49*/            reportAbsence("name");
/*  50*/            return;
                }
/*  53*/        if(!Name.isQName(s))
/*  54*/            compileError("Attribute set name must be a valid QName");
/*  58*/        try
                {
/*  58*/            fingerprint = makeNameCode(s, false) & 0xfffff;
                }
/*  60*/        catch(NamespaceException namespaceexception)
                {
/*  60*/            compileError(namespaceexception.getMessage());
                }
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  66*/        checkTopLevel();
/*  68*/        for(Node node = getFirstChild(); node != null; node = node.getNextSibling())
/*  70*/            if(!(node instanceof XSLAttribute))
/*  71*/                compileError("Only xsl:attribute is allowed within xsl:attribute-set");

/*  76*/        if(use != null)
/*  77*/            findAttributeSets(use);
            }

            public Procedure getProcedure()
            {
/*  86*/        return procedure;
            }

            public void preprocess()
                throws TransformerConfigurationException
            {
/*  90*/        getPrincipalStyleSheet().allocateLocalSlots(procedure.getNumberOfVariables());
            }

            public void process(Context context)
                throws TransformerException
            {
            }

            public void expand(Context context)
                throws TransformerException
            {
/*  99*/        processAttributeSets(context);
/* 100*/        if(procedure.getNumberOfVariables() == 0)
                {
/* 101*/            processChildren(context);
                } else
                {
/* 103*/            Bindery bindery = context.getController().getBindery();
/* 104*/            bindery.openStackFrame(null);
/* 105*/            processChildren(context);
/* 106*/            bindery.closeStackFrame();
                }
            }
}
