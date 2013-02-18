// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TextComparer.java

package com.icl.saxon.sort;


// Referenced classes of package com.icl.saxon.sort:
//            Comparer

public abstract class TextComparer extends Comparer
{

            public static final int DEFAULT_CASE_ORDER = 0;
            public static final int LOWERCASE_FIRST = 1;
            public static final int UPPERCASE_FIRST = 2;

            public TextComparer()
            {
            }

            public Comparer setCaseOrder(int i)
            {
/*  29*/        return this;
            }
}
