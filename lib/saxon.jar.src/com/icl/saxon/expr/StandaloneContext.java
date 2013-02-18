// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StandaloneContext.java

package com.icl.saxon.expr;

import com.icl.saxon.Binding;
import com.icl.saxon.om.Name;
import com.icl.saxon.om.NamePool;
import com.icl.saxon.pattern.NameTest;
import com.icl.saxon.pattern.NamespaceTest;
import java.util.Hashtable;

// Referenced classes of package com.icl.saxon.expr:
//            XPathException, StaticContext, ExpressionParser, Function

public class StandaloneContext
    implements StaticContext
{

            private NamePool namePool;
            private Hashtable namespaces;

            public StandaloneContext()
            {
/*  24*/        this(NamePool.getDefaultNamePool());
            }

            public StandaloneContext(NamePool namepool)
            {
/*  17*/        namespaces = new Hashtable();
/*  32*/        namePool = namepool;
/*  33*/        declareNamespace("xml", "http://www.w3.org/XML/1998/namespace");
/*  34*/        declareNamespace("xsl", "http://www.w3.org/1999/XSL/Transform");
/*  35*/        declareNamespace("saxon", "http://icl.com/saxon");
/*  36*/        declareNamespace("", "");
            }

            public void declareNamespace(String s, String s1)
            {
/*  44*/        namespaces.put(s, s1);
/*  45*/        namePool.allocateNamespaceCode(s, s1);
            }

            public StaticContext makeRuntimeContext(NamePool namepool)
            {
/*  54*/        return null;
            }

            public String getSystemId()
            {
/*  63*/        return "";
            }

            public String getBaseURI()
            {
/*  74*/        return "";
            }

            public int getLineNumber()
            {
/*  83*/        return -1;
            }

            public String getURIForPrefix(String s)
                throws XPathException
            {
/*  93*/        String s1 = (String)namespaces.get(s);
/*  94*/        if(s1 == null)
/*  95*/            throw new XPathException("Prefix " + s + " has not been declared");
/*  97*/        else
/*  97*/            return s1;
            }

            public final int makeNameCode(String s, boolean flag)
                throws XPathException
            {
/* 109*/        String s1 = Name.getPrefix(s);
/* 110*/        String s2 = Name.getLocalName(s);
                String s3;
/* 112*/        if(s1.equals("") && flag)
/* 113*/            s3 = "";
/* 115*/        else
/* 115*/            s3 = getURIForPrefix(s1);
/* 117*/        return namePool.allocate(s1, s3, s2);
            }

            public final int getFingerprint(String s, boolean flag)
                throws XPathException
            {
/* 130*/        String s1 = Name.getPrefix(s);
/* 131*/        String s2 = Name.getLocalName(s);
                String s3;
/* 133*/        if(s1.equals("") && flag)
/* 134*/            s3 = "";
/* 136*/        else
/* 136*/            s3 = getURIForPrefix(s1);
/* 138*/        return namePool.getFingerprint(s3, s2);
            }

            public NameTest makeNameTest(short word0, String s, boolean flag)
                throws XPathException
            {
/* 148*/        return new NameTest(word0, makeNameCode(s, flag));
            }

            public NamespaceTest makeNamespaceTest(short word0, String s)
                throws XPathException
            {
/* 157*/        return new NamespaceTest(namePool, word0, getURICodeForPrefix(s));
            }

            private short getURICodeForPrefix(String s)
                throws XPathException
            {
/* 170*/        String s1 = getURIForPrefix(s);
/* 171*/        return namePool.getCodeForURI(s1);
            }

            public Binding bindVariable(int i)
                throws XPathException
            {
/* 179*/        throw new XPathException("Variables are not allowed in a standalone expression");
            }

            public boolean isExtensionNamespace(short word0)
            {
/* 187*/        return false;
            }

            public boolean forwardsCompatibleModeIsEnabled()
            {
/* 195*/        return false;
            }

            public Function getStyleSheetFunction(int i)
                throws XPathException
            {
/* 205*/        return null;
            }

            public Class getExternalJavaClass(String s)
            {
/* 217*/        return null;
            }

            public boolean isElementAvailable(String s)
                throws XPathException
            {
/* 225*/        return false;
            }

            public boolean isFunctionAvailable(String s)
                throws XPathException
            {
/* 234*/        String s1 = Name.getPrefix(s);
/* 235*/        if(s1.equals(""))
/* 236*/            return ExpressionParser.makeSystemFunction(s) != null;
/* 239*/        else
/* 239*/            return false;
            }

            public boolean allowsKeyFunction()
            {
/* 254*/        return false;
            }

            public String getVersion()
            {
/* 262*/        return "1.1";
            }
}
