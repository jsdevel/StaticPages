// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   LowercaseFirstComparer.java

package com.icl.saxon.sort;


// Referenced classes of package com.icl.saxon.sort:
//            Comparer

public class LowercaseFirstComparer extends Comparer
{

            public LowercaseFirstComparer()
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
/*  32*/            if(k == i)
/*  32*/                return -1;
/*  33*/            if(l == j)
/*  33*/                return 1;
/*  34*/            int j1 = Character.toLowerCase(ac[k]) - Character.toLowerCase(ac1[l]);
/*  36*/            k++;
/*  37*/            l++;
/*  38*/            if(j1 != 0)
/*  38*/                return j1;
                }

/*  41*/        k = 0;
/*  42*/        int i1 = 0;
                int k1;
/*  44*/        do
                {
/*  44*/            if(k == i)
/*  44*/                return 0;
/*  45*/            k1 = ac[k++] - ac1[i1++];
                } while(k1 == 0);
/*  47*/        return Character.isLowerCase(ac[k - 1]) ? -1 : 1;
            }
}
