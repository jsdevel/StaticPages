// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DescendingComparer.java

package com.icl.saxon.sort;


// Referenced classes of package com.icl.saxon.sort:
//            Comparer

public class DescendingComparer extends Comparer
{

            private Comparer baseComparer;

            public DescendingComparer(Comparer comparer)
            {
/*  17*/        baseComparer = comparer;
            }

            public int compare(Object obj, Object obj1)
            {
/*  27*/        return 0 - baseComparer.compare(obj, obj1);
            }
}
