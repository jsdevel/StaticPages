// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLNamespaceAlias.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.om.NamespaceException;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames

public class XSLNamespaceAlias extends StyleElement
{

            private short stylesheetURICode;
            private short resultURICode;

            public XSLNamespaceAlias()
            {
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  21*/        String s = null;
/*  22*/        String s1 = null;
/*  24*/        StandardNames standardnames = getStandardNames();
/*  25*/        AttributeCollection attributecollection = getAttributeList();
/*  27*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  28*/            int j = attributecollection.getNameCode(i);
/*  29*/            int k = j & 0xfffff;
/*  30*/            if(k == standardnames.STYLESHEET_PREFIX)
/*  31*/                s = attributecollection.getValue(i);
/*  32*/            else
/*  32*/            if(k == standardnames.RESULT_PREFIX)
/*  33*/                s1 = attributecollection.getValue(i);
/*  35*/            else
/*  35*/                checkUnknownAttribute(j);
                }

/*  38*/        if(s == null)
                {
/*  39*/            reportAbsence("stylesheet-prefix");
/*  40*/            return;
                }
/*  42*/        if(s.equals("#default"))
/*  43*/            s = "";
/*  45*/        if(s1 == null)
                {
/*  46*/            reportAbsence("result-prefix");
/*  47*/            return;
                }
/*  49*/        if(s1.equals("#default"))
/*  50*/            s1 = "";
/*  53*/        try
                {
/*  53*/            stylesheetURICode = getURICodeForPrefix(s);
/*  54*/            resultURICode = getURICodeForPrefix(s1);
                }
/*  56*/        catch(NamespaceException namespaceexception)
                {
/*  56*/            compileError(namespaceexception.getMessage());
                }
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  61*/        checkTopLevel();
            }

            public void preprocess()
                throws TransformerConfigurationException
            {
            }

            public void process(Context context)
            {
            }

            public short getStylesheetURICode()
            {
/*  69*/        return stylesheetURICode;
            }

            public short getResultURICode()
            {
/*  73*/        return resultURICode;
            }
}
