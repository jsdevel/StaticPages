// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLAttribute.java

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
//            XSLStringConstructor, XSLAttributeSet, StyleElement, StandardNames

public final class XSLAttribute extends XSLStringConstructor
{

            private Expression attributeName;
            private Expression namespace;
            private boolean disable;

            public XSLAttribute()
            {
/*  22*/        namespace = null;
/*  23*/        disable = false;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  27*/        StandardNames standardnames = getStandardNames();
/*  28*/        AttributeCollection attributecollection = getAttributeList();
/*  30*/        String s = null;
/*  31*/        String s1 = null;
/*  32*/        String s2 = null;
/*  34*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  35*/            int j = attributecollection.getNameCode(i);
/*  36*/            int k = j & 0xfffff;
/*  37*/            if(k == standardnames.NAME)
/*  38*/                s = attributecollection.getValue(i);
/*  39*/            else
/*  39*/            if(k == standardnames.NAMESPACE)
/*  40*/                s1 = attributecollection.getValue(i);
/*  41*/            else
/*  41*/            if(k == standardnames.SAXON_DISABLE_OUTPUT_ESCAPING)
/*  42*/                s2 = attributecollection.getValue(i);
/*  44*/            else
/*  44*/                checkUnknownAttribute(j);
                }

/*  48*/        if(s == null)
                {
/*  49*/            reportAbsence("name");
/*  50*/            return;
                }
/*  52*/        attributeName = makeAttributeValueTemplate(s);
/*  53*/        if((attributeName instanceof StringValue) && !Name.isQName(((StringValue)attributeName).asString()))
/*  55*/            compileError("Attribute name is not a valid QName");
/*  60*/        if(s1 != null)
/*  61*/            namespace = makeAttributeValueTemplate(s1);
/*  64*/        disable = s2 != null && s2.equals("yes");
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  69*/        if(!(getParentNode() instanceof XSLAttributeSet))
/*  70*/            checkWithinTemplate();
/*  72*/        optimize();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  77*/        String s = attributeName.evaluateAsString(context);
/*  78*/        Controller controller = context.getController();
/*  79*/        NamePool namepool = controller.getNamePool();
/*  81*/        if(!Name.isQName(s))
                {
/*  82*/            controller.reportRecoverableError("Invalid attribute name: " + s, this);
/*  84*/            return;
                }
/*  87*/        if(s.equals("xmlns") && namespace == null)
                {
/*  89*/            controller.reportRecoverableError("Invalid attribute name: " + s, this);
/*  91*/            return;
                }
/*  94*/        if(s.length() > 6 && s.substring(0, 6).equals("xmlns:"))
                {
/*  95*/            if(namespace == null)
                    {
/*  96*/                controller.reportRecoverableError("Invalid attribute name: " + s, this);
/*  98*/                return;
                    }
/* 101*/            s = s.substring(6);
                }
/* 105*/        String s1 = Name.getPrefix(s);
                short word0;
/* 108*/        if(namespace == null)
                {
/* 113*/            if(s1.equals(""))
/* 114*/                word0 = 0;
/* 117*/            else
/* 117*/                try
                        {
/* 117*/                    word0 = getURICodeForPrefix(s1);
                        }
/* 120*/                catch(NamespaceException namespaceexception)
                        {
/* 120*/                    throw styleError(namespaceexception.getMessage());
                        }
                } else
                {
/* 128*/            String s2 = namespace.evaluateAsString(context);
/* 129*/            if(s2.equals(""))
/* 132*/                s1 = "";
/* 135*/            else
/* 135*/            if(s1.equals(""))
                    {
/* 136*/                s1 = getPrefixForURI(s2);
/* 139*/                if(s1 == null || s1 == "")
/* 140*/                    s1 = "ns0";
                    }
/* 146*/            word0 = namepool.allocateCodeForURI(s2);
                }
/* 151*/        String s3 = Name.getLocalName(s);
/* 152*/        int i = namepool.allocate(s1, word0, s3);
/* 154*/        Outputter outputter = controller.getOutputter();
/* 158*/        if(outputter.thereIsAnOpenStartTag())
                {
/* 159*/            if((i >> 20 & 0xff) != 0)
/* 160*/                i = outputter.checkAttributePrefix(i);
/* 162*/            outputter.writeAttribute(i, expandChildren(context), disable);
                } else
                {
/* 164*/            context.getController().reportRecoverableError("Cannot write an attribute node when no element start tag is open", this);
                }
            }
}
