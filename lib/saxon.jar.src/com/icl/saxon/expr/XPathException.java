// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XPathException.java

package com.icl.saxon.expr;

import javax.xml.transform.TransformerException;

public class XPathException extends TransformerException
{

            public XPathException(String s)
            {
/*  14*/        super(s);
            }

            public XPathException(Exception exception)
            {
/*  18*/        super(exception);
            }

            public XPathException(String s, Exception exception)
            {
/*  22*/        super(s, exception);
            }
}
