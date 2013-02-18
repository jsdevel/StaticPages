// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   UppercaseFirstComparer.java

package com.icl.saxon.sort;


// Referenced classes of package com.icl.saxon.sort:
//            Comparer

public class UppercaseFirstComparer extends Comparer
{

            public UppercaseFirstComparer()
            {
            }

            public int compare(Object obj, Object obj1)
            {
/*  23*/        char ac[] = ((String)obj).toCharArray();
/*  24*/        char ac1[] = ((String)obj1).toCharArray();
/*  25*/        int i = ac.length;
/*  26*/        int j = ac1.length;
/*  27*/        int k = 0;
/*  28*/        for(int l = 0; k != i || l != j;)
                {
/*  31*/            if(k == i)
/*  31*/                return -1;
/*  32*/            if(l == j)
/*  32*/                return 1;
/*  33*/            int j1 = Character.toLowerCase(ac[k++]) - Character.toLowerCase(ac1[l++]);
/*  34*/            if(j1 != 0)
/*  34*/                return j1;
                }

/*  36*/        k = 0;
/*  37*/        int i1 = 0;
                int k1;
/*  39*/        do
                {
/*  39*/            if(k == i)
/*  39*/                return 0;
/*  40*/            k1 = ac[k++] - ac1[i1++];
                } while(k1 == 0);
/*  42*/        return Character.isUpperCase(ac[k - 1]) ? -1 : 1;
            }
}
