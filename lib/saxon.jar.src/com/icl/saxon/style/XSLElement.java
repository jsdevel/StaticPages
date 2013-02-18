// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLElement.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.expr.StringValue;
import com.icl.saxon.om.*;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames

public class XSLElement extends StyleElement
{

            private Expression elementName;
            private Expression namespace;
            private String use;
            private boolean declared;

            public XSLElement()
            {
/*  19*/        namespace = null;
/*  21*/        declared = false;
            }

            public boolean isInstruction()
            {
/*  29*/        return true;
            }

            public boolean mayContainTemplateBody()
            {
/*  38*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  43*/        StandardNames standardnames = getStandardNames();
/*  44*/        AttributeCollection attributecollection = getAttributeList();
/*  46*/        String s = null;
/*  47*/        String s1 = null;
/*  49*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  50*/            int j = attributecollection.getNameCode(i);
/*  51*/            int k = j & 0xfffff;
/*  52*/            if(k == standardnames.NAME)
/*  53*/                s = attributecollection.getValue(i);
/*  54*/            else
/*  54*/            if(k == standardnames.NAMESPACE)
/*  55*/                s1 = attributecollection.getValue(i);
/*  56*/            else
/*  56*/            if(k == standardnames.USE_ATTRIBUTE_SETS)
/*  57*/                use = attributecollection.getValue(i);
/*  59*/            else
/*  59*/                checkUnknownAttribute(j);
                }

/*  63*/        if(s == null)
                {
/*  64*/            reportAbsence("name");
                } else
                {
/*  66*/            elementName = makeAttributeValueTemplate(s);
/*  67*/            if((elementName instanceof StringValue) && !Name.isQName(((StringValue)elementName).asString()))
/*  69*/                compileError("Element name is not a valid QName");
                }
/*  74*/        if(s1 != null)
/*  75*/            namespace = makeAttributeValueTemplate(s1);
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  81*/        checkWithinTemplate();
/*  82*/        if(use != null)
/*  83*/            findAttributeSets(use);
            }

            public void process(Context context)
                throws TransformerException
            {
/*  89*/        Controller controller = context.getController();
/*  90*/        NamePool namepool = controller.getNamePool();
/*  94*/        String s = elementName.evaluateAsString(context);
/*  96*/        if(!Name.isQName(s))
                {
/*  97*/            controller.reportRecoverableError("Invalid element name: " + s, this);
/* 101*/            context.getOutputter().writeStartTag(-1);
/* 102*/            processChildren(context);
/* 103*/            return;
                }
/* 106*/        String s1 = Name.getPrefix(s);
                short word0;
/* 109*/        if(namespace == null)
                {
/* 114*/            try
                    {
/* 114*/                word0 = getURICodeForPrefix(s1);
                    }
/* 117*/            catch(NamespaceException namespaceexception)
                    {
/* 117*/                throw styleError(namespaceexception.getMessage());
                    }
                } else
                {
/* 122*/            String s2 = namespace.evaluateAsString(context);
/* 123*/            if(s2.equals(""))
/* 126*/                s1 = "";
/* 128*/            word0 = namepool.allocateCodeForURI(s2);
                }
/* 131*/        String s3 = Name.getLocalName(s);
/* 132*/        int i = namepool.allocate(s1, word0, s3);
/* 134*/        Outputter outputter = context.getOutputter();
/* 135*/        outputter.writeStartTag(i);
/* 136*/        outputter.writeNamespaceDeclaration(namepool.allocateNamespaceCode(i));
/* 139*/        processAttributeSets(context);
/* 142*/        processChildren(context);
/* 145*/        outputter.writeEndTag(i);
            }
}
