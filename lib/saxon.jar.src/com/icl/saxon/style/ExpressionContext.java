// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ExpressionContext.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.*;
import com.icl.saxon.pattern.*;
import com.icl.saxon.tree.*;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            XSLKey, StyleElement, XSLStyleSheet, StyleNodeFactory

public class ExpressionContext
    implements StaticContext
{

            private StyleElement element;
            private NamePool namePool;

            public ExpressionContext(StyleElement styleelement)
            {
/*  30*/        element = styleelement;
/*  31*/        namePool = styleelement.getNamePool();
            }

            public StaticContext makeRuntimeContext(NamePool namepool)
            {
/*  40*/        ExpressionContext expressioncontext = new ExpressionContext(element);
/*  41*/        expressioncontext.namePool = namepool;
/*  42*/        return expressioncontext;
            }

            public String getSystemId()
            {
/*  50*/        return element.getSystemId();
            }

            public int getLineNumber()
            {
/*  59*/        return element.getLineNumber();
            }

            public String getBaseURI()
            {
/*  69*/        return element.getBaseURI();
            }

            public String getURIForPrefix(String s)
                throws XPathException
            {
/*  80*/        try
                {
/*  80*/            short word0 = element.getURICodeForPrefix(s);
/*  81*/            return namePool.getURIFromURICode(word0);
                }
/*  83*/        catch(NamespaceException namespaceexception)
                {
/*  83*/            throw new XPathException(namespaceexception.getMessage());
                }
            }

            public final int makeNameCode(String s, boolean flag)
                throws XPathException
            {
/*  97*/        String s1 = Name.getPrefix(s);
/*  99*/        try
                {
/*  99*/            if(s1.equals(""))
                    {
/* 100*/                short word0 = 0;
/* 102*/                if(!Name.isNCName(s))
/* 103*/                    throw new XPathException("Name " + s + " contains invalid characters");
/* 106*/                if(flag)
/* 107*/                    word0 = element.getURICodeForPrefix(s1);
/* 110*/                return namePool.allocate(s1, word0, s);
                    } else
                    {
/* 113*/                String s2 = Name.getLocalName(s);
/* 114*/                short word1 = element.getURICodeForPrefix(s1);
/* 115*/                return namePool.allocate(s1, word1, s2);
                    }
                }
/* 118*/        catch(NamespaceException namespaceexception)
                {
/* 118*/            throw new XPathException("Namespace prefix " + s1 + " has not been declared");
                }
            }

            public int getFingerprint(String s, boolean flag)
                throws XPathException
            {
/* 134*/        String s1 = Name.getPrefix(s);
/* 135*/        if(s1.equals(""))
                {
/* 136*/            String s2 = "";
/* 138*/            if(flag)
/* 139*/                s2 = getURIForPrefix(s1);
/* 142*/            return namePool.getFingerprint(s2, s);
                } else
                {
/* 145*/            String s3 = Name.getLocalName(s);
/* 146*/            String s4 = getURIForPrefix(s1);
/* 147*/            return namePool.getFingerprint(s4, s3);
                }
            }

            public NameTest makeNameTest(short word0, String s, boolean flag)
                throws XPathException
            {
/* 157*/        int i = makeNameCode(s, flag);
/* 158*/        NameTest nametest = new NameTest(word0, i);
/* 159*/        nametest.setOriginalText(s);
/* 160*/        return nametest;
            }

            public NamespaceTest makeNamespaceTest(short word0, String s)
                throws XPathException
            {
/* 170*/        try
                {
/* 170*/            short word1 = element.getURICodeForPrefix(s);
/* 171*/            NamespaceTest namespacetest = new NamespaceTest(namePool, word0, word1);
/* 172*/            namespacetest.setOriginalText(s + ":*");
/* 173*/            return namespacetest;
                }
/* 175*/        catch(NamespaceException namespaceexception)
                {
/* 175*/            throw new XPathException(namespaceexception.getMessage());
                }
            }

            public Binding bindVariable(int i)
                throws XPathException
            {
/* 187*/        return element.bindVariable(i);
            }

            public boolean isExtensionNamespace(short word0)
                throws XPathException
            {
/* 195*/        return element.isExtensionNamespace(word0);
            }

            public boolean forwardsCompatibleModeIsEnabled()
                throws XPathException
            {
/* 203*/        return element.forwardsCompatibleModeIsEnabled();
            }

            public Function getStyleSheetFunction(int i)
                throws XPathException
            {
/* 213*/        return element.getStyleSheetFunction(i);
            }

            public Class getExternalJavaClass(String s)
                throws TransformerException
            {
/* 227*/        XSLStyleSheet xslstylesheet = element.getPrincipalStyleSheet();
/* 228*/        Class class1 = xslstylesheet.getExternalJavaClass(s);
/* 229*/        if(class1 != null)
/* 230*/            return class1;
/* 235*/        if(s.equals("http://icl.com/saxon"))
/* 236*/            return com.icl.saxon.functions.Extensions.class;
/* 238*/        if(s.equals("http://exslt.org/common"))
/* 239*/            return com.icl.saxon.exslt.Common.class;
/* 240*/        if(s.equals("http://exslt.org/sets"))
/* 241*/            return com.icl.saxon.exslt.Sets.class;
/* 242*/        if(s.equals("http://exslt.org/math"))
/* 243*/            return com.icl.saxon.exslt.Math.class;
/* 244*/        if(s.equals("http://exslt.org/dates-and-times"))
/* 245*/            return com.icl.saxon.exslt.Date.class;
/* 250*/        if(!((Boolean)xslstylesheet.getPreparedStyleSheet().getTransformerFactory().getAttribute("http://icl.com/saxon/feature/allow-external-functions")).booleanValue())
/* 252*/            throw new TransformerException("Calls to external functions have been disabled");
/* 258*/        try
                {
/* 258*/            if(s.startsWith("java:"))
/* 259*/                return Loader.getClass(s.substring(5));
/* 265*/            int i = s.lastIndexOf('/');
/* 266*/            if(i < 0)
/* 267*/                return Loader.getClass(s);
/* 268*/            if(i == s.length() - 1)
/* 269*/                return null;
/* 271*/            else
/* 271*/                return Loader.getClass(s.substring(i + 1));
                }
/* 274*/        catch(TransformerException transformerexception)
                {
/* 274*/            return null;
                }
            }

            public boolean isElementAvailable(String s)
                throws XPathException
            {
/* 285*/        if(!Name.isQName(s))
                {
/* 286*/            throw new XPathException("Invalid QName: " + s);
                } else
                {
/* 289*/            String s1 = Name.getPrefix(s);
/* 290*/            String s2 = Name.getLocalName(s);
/* 291*/            String s3 = getURIForPrefix(s1);
/* 293*/            return element.getPreparedStyleSheet().getStyleNodeFactory().isElementAvailable(s3, s2);
                }
            }

            public boolean isFunctionAvailable(String s)
                throws XPathException
            {
/* 302*/        if(!Name.isQName(s))
/* 303*/            throw new XPathException("Invalid QName: " + s);
/* 305*/        String s1 = Name.getPrefix(s);
/* 306*/        String s2 = getURIForPrefix(s1);
/* 308*/        try
                {
/* 308*/            if(s1.equals(""))
/* 309*/                return ExpressionParser.makeSystemFunction(s) != null;
/* 312*/            int i = getFingerprint(s, false);
/* 313*/            if(i >= 0)
                    {
/* 314*/                Function function = getStyleSheetFunction(i);
/* 315*/                if(function != null)
/* 315*/                    return true;
                    }
/* 318*/            Class class1 = getExternalJavaClass(s2);
/* 319*/            if(class1 == null)
                    {
/* 320*/                return false;
                    } else
                    {
/* 323*/                String s3 = Name.getLocalName(s);
/* 325*/                FunctionProxy functionproxy = new FunctionProxy();
/* 326*/                return functionproxy.setFunctionName(class1, s3);
                    }
                }
/* 328*/        catch(Exception exception)
                {
/* 328*/            return false;
                }
            }

            public boolean allowsKeyFunction()
            {
/* 337*/        return !(element instanceof XSLKey);
            }

            public String getVersion()
            {
/* 345*/        return element.getVersion();
            }

            public String toString()
            {
/* 353*/        return "Expression Context at " + element.toString();
            }
}
