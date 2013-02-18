// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StringComparer.java

package com.icl.saxon.sort;


// Referenced classes of package com.icl.saxon.sort:
//            TextComparer, LowercaseFirstComparer, UppercaseFirstComparer, Comparer

public class StringComparer extends TextComparer
{

            public StringComparer()
            {
            }

            public int compare(Object obj, Object obj1)
            {
/*  21*/        char ac[] = ((String)obj).toCharArray();
/*  22*/        char ac1[] = ((String)obj1).toCharArray();
/*  23*/        int i = ac.length;
/*  24*/        int j = ac1.length;
/*  25*/        int k = 0;
/*  26*/        int l = 0;
                int i1;
/*  28*/        do
                {
/*  28*/            if(k == i && l == j)
/*  28*/                return 0;
/*  29*/            if(k == i)
/*  29*/                return -1;
/*  30*/            if(l == j)
/*  30*/                return 1;
/*  31*/            i1 = ac[k++] - ac1[l++];
                } while(i1 == 0);
/*  32*/        return i1;
            }

            public Comparer setCaseOrder(int i)
            {
/*  46*/        if(i == 1)
/*  47*/            return new LowercaseFirstComparer();
/*  49*/        if(i == 2)
/*  50*/            return new UppercaseFirstComparer();
/*  52*/        else
/*  52*/            return this;
            }
}
