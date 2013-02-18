// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StylesheetStripper.java

package com.icl.saxon;

import com.icl.saxon.om.NamePool;
import com.icl.saxon.om.Stripper;

public class StylesheetStripper extends Stripper
{

            int xsl_text;

            public StylesheetStripper()
            {
            }

            public void setStylesheetRules(NamePool namepool)
            {
/*  31*/        xsl_text = namepool.getFingerprint("http://www.w3.org/1999/XSL/Transform", "text");
            }

            public boolean isSpacePreserving(int i)
            {
/*  42*/        return (i & 0xfffff) == xsl_text;
            }
}
