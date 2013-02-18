// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Comparer.java

package com.icl.saxon.sort;


// Referenced classes of package com.icl.saxon.sort:
//            DescendingComparer

public abstract class Comparer
{

            public Comparer()
            {
            }

            public abstract int compare(Object obj, Object obj1);

            public Comparer setDataType(String s, String s1)
            {
/*  27*/        return this;
            }

            public Comparer setOrder(boolean flag)
            {
/*  35*/        return ((Comparer) (flag ? this : new DescendingComparer(this)));
            }
}
