// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   EmptyEnumeration.java

package com.icl.saxon.om;


// Referenced classes of package com.icl.saxon.om:
//            AxisEnumeration, NodeInfo

public class EmptyEnumeration
    implements AxisEnumeration
{

            private static EmptyEnumeration instance = new EmptyEnumeration();

            public EmptyEnumeration()
            {
            }

            public static EmptyEnumeration getInstance()
            {
/*  16*/        return instance;
            }

            public boolean hasMoreElements()
            {
/*  20*/        return false;
            }

            public NodeInfo nextElement()
            {
/*  24*/        return null;
            }

            public boolean isSorted()
            {
/*  28*/        return true;
            }

            public boolean isReverseSorted()
            {
/*  32*/        return true;
            }

            public boolean isPeer()
            {
/*  36*/        return true;
            }

            public int getLastPosition()
            {
/*  40*/        return 0;
            }

}
