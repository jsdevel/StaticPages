// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLKey.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.expr.XPathException;
import com.icl.saxon.om.Name;
import com.icl.saxon.om.NamespaceException;
import com.icl.saxon.pattern.Pattern;
import com.icl.saxon.tree.*;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames, XSLStyleSheet

public class XSLKey extends StyleElement
{

            private int fingerprint;
            private Pattern match;
            private Expression use;

            public XSLKey()
            {
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  28*/        String s = null;
/*  29*/        String s1 = null;
/*  30*/        String s2 = null;
/*  32*/        StandardNames standardnames = getStandardNames();
/*  33*/        AttributeCollection attributecollection = getAttributeList();
/*  35*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  36*/            int j = attributecollection.getNameCode(i);
/*  37*/            int k = j & 0xfffff;
/*  38*/            if(k == standardnames.NAME)
/*  39*/                s = attributecollection.getValue(i);
/*  40*/            else
/*  40*/            if(k == standardnames.USE)
/*  41*/                s2 = attributecollection.getValue(i);
/*  42*/            else
/*  42*/            if(k == standardnames.MATCH)
/*  43*/                s1 = attributecollection.getValue(i);
/*  45*/            else
/*  45*/                checkUnknownAttribute(j);
                }

/*  49*/        if(s == null)
                {
/*  50*/            reportAbsence("name");
/*  51*/            return;
                }
/*  53*/        if(!Name.isQName(s))
                {
/*  54*/            compileError("Name of key must be a valid QName");
/*  55*/            return;
                }
/*  58*/        try
                {
/*  58*/            fingerprint = makeNameCode(s, false) & 0xfffff;
                }
/*  60*/        catch(NamespaceException namespaceexception)
                {
/*  60*/            compileError(namespaceexception.getMessage());
                }
/*  63*/        if(s1 == null)
/*  64*/            reportAbsence("match");
/*  66*/        else
/*  66*/            match = makePattern(s1);
/*  69*/        if(s2 == null)
/*  70*/            reportAbsence("use");
/*  72*/        else
/*  72*/            use = makeExpression(s2);
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  77*/        checkTopLevel();
/*  78*/        checkEmpty();
            }

            public void preprocess()
                throws TransformerConfigurationException
            {
/*  83*/        KeyManager keymanager = getPrincipalStyleSheet().getKeyManager();
/*  84*/        keymanager.setKeyDefinition(new KeyDefinition(fingerprint, match, use));
            }

            public void process(Context context)
                throws TransformerException
            {
            }

            public Binding bindVariable(int i)
                throws XPathException
            {
/*  95*/        throw new XPathException("The expressions in xsl:key may not contain references to variables");
            }
}
