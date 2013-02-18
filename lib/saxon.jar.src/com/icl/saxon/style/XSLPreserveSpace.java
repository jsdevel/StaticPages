// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLPreserveSpace.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.Mode;
import com.icl.saxon.om.Name;
import com.icl.saxon.om.NamespaceException;
import com.icl.saxon.pattern.*;
import com.icl.saxon.tree.*;
import java.util.StringTokenizer;
import javax.xml.transform.TransformerConfigurationException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames, XSLStyleSheet

public class XSLPreserveSpace extends StyleElement
{

            private String elements;

            public XSLPreserveSpace()
            {
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  20*/        StandardNames standardnames = getStandardNames();
/*  21*/        AttributeCollection attributecollection = getAttributeList();
/*  23*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  24*/            int j = attributecollection.getNameCode(i);
/*  25*/            int k = j & 0xfffff;
/*  26*/            if(k == standardnames.ELEMENTS)
/*  27*/                elements = attributecollection.getValue(i);
/*  29*/            else
/*  29*/                checkUnknownAttribute(j);
                }

/*  32*/        if(elements == null)
                {
/*  33*/            reportAbsence("elements");
/*  34*/            elements = "*";
                }
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  39*/        checkTopLevel();
            }

            public void preprocess()
                throws TransformerConfigurationException
            {
/*  44*/        Boolean boolean1 = new Boolean(getFingerprint() == getStandardNames().XSL_PRESERVE_SPACE);
/*  45*/        Mode mode = getPrincipalStyleSheet().getStripperRules();
/*  49*/        for(StringTokenizer stringtokenizer = new StringTokenizer(elements); stringtokenizer.hasMoreTokens();)
                {
/*  51*/            String s = stringtokenizer.nextToken();
/*  53*/            try
                    {
/*  53*/                if(s.equals("*"))
/*  54*/                    mode.addRule(AnyNodeTest.getInstance(), boolean1, getPrecedence(), -0.5D);
/*  60*/                else
/*  60*/                if(s.endsWith(":*"))
                        {
/*  61*/                    String s1 = s.substring(0, s.length() - 2);
/*  62*/                    mode.addRule(new NamespaceTest(getNamePool(), (short)1, getURICodeForPrefix(s1)), boolean1, getPrecedence(), -0.25D);
                        } else
                        {
/*  71*/                    if(!Name.isQName(s))
/*  72*/                        compileError("Element name " + s + " is not a valid QName");
/*  74*/                    mode.addRule(new NameTest((short)1, makeNameCode(s, false)), boolean1, getPrecedence(), 0.0D);
                        }
                    }
/*  83*/            catch(NamespaceException namespaceexception)
                    {
/*  83*/                compileError(namespaceexception.getMessage());
                    }
                }

            }

            public void process(Context context)
            {
            }
}
