// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NamespaceException.java

package com.icl.saxon.om;


public class NamespaceException extends Exception
{

            String prefix;

            public NamespaceException(String s)
            {
/*  13*/        prefix = s;
            }

            public String getMessage()
            {
/*  17*/        return "Namespace prefix " + prefix + " has not been declared";
            }
}
