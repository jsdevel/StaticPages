// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   KeyDefinition.java

package com.icl.saxon;

import com.icl.saxon.expr.Expression;
import com.icl.saxon.pattern.Pattern;

public class KeyDefinition
{

            private int fingerprint;
            private Pattern match;
            private Expression use;

            public KeyDefinition(int i, Pattern pattern, Expression expression)
            {
/*  21*/        fingerprint = i;
/*  22*/        match = pattern;
/*  23*/        use = expression;
            }

            public int getFingerprint()
            {
/*  31*/        return fingerprint;
            }

            public Pattern getMatch()
            {
/*  39*/        return match;
            }

            public Expression getUse()
            {
/*  47*/        return use;
            }
}
