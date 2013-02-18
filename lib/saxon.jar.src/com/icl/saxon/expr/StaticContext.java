// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StaticContext.java

package com.icl.saxon.expr;

import com.icl.saxon.Binding;
import com.icl.saxon.om.NamePool;
import com.icl.saxon.pattern.NameTest;
import com.icl.saxon.pattern.NamespaceTest;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.expr:
//            XPathException, Function

public interface StaticContext
{

    public abstract StaticContext makeRuntimeContext(NamePool namepool);

    public abstract String getSystemId();

    public abstract int getLineNumber();

    public abstract String getBaseURI();

    public abstract String getURIForPrefix(String s)
        throws XPathException;

    public abstract int makeNameCode(String s, boolean flag)
        throws XPathException;

    public abstract int getFingerprint(String s, boolean flag)
        throws XPathException;

    public abstract NameTest makeNameTest(short word0, String s, boolean flag)
        throws XPathException;

    public abstract NamespaceTest makeNamespaceTest(short word0, String s)
        throws XPathException;

    public abstract Binding bindVariable(int i)
        throws XPathException;

    public abstract boolean isExtensionNamespace(short word0)
        throws XPathException;

    public abstract boolean forwardsCompatibleModeIsEnabled()
        throws XPathException;

    public abstract Function getStyleSheetFunction(int i)
        throws XPathException;

    public abstract Class getExternalJavaClass(String s)
        throws TransformerException;

    public abstract boolean isElementAvailable(String s)
        throws XPathException;

    public abstract boolean isFunctionAvailable(String s)
        throws XPathException;

    public abstract boolean allowsKeyFunction();

    public abstract String getVersion();
}
