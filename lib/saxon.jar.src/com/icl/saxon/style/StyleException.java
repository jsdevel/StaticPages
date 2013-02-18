// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StyleException.java

package com.icl.saxon.style;

import javax.xml.transform.TransformerException;

public class StyleException extends TransformerException
{

            public StyleException(String s)
            {
/*  13*/        super(s);
            }

            public StyleException(Exception exception, String s)
            {
/*  17*/        super(s, exception);
            }
}
