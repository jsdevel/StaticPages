// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DoubleComparer.java

package com.icl.saxon.sort;

import com.icl.saxon.expr.Value;

// Referenced classes of package com.icl.saxon.sort:
//            Comparer

public class DoubleComparer extends Comparer
{

            public DoubleComparer()
            {
            }

            public int compare(Object obj, Object obj1)
            {
/*  20*/        double d = Value.stringToNumber((String)obj);
/*  21*/        double d1 = Value.stringToNumber((String)obj1);
/*  22*/        if(Double.isNaN(d))
/*  23*/            return !Double.isNaN(d1) ? -1 : 0;
/*  29*/        if(Double.isNaN(d1))
/*  30*/            return 1;
/*  32*/        if(d == d1)
/*  32*/            return 0;
/*  33*/        return d >= d1 ? 1 : -1;
            }
}
