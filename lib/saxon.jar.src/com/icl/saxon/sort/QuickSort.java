// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   QuickSort.java

package com.icl.saxon.sort;


// Referenced classes of package com.icl.saxon.sort:
//            Sortable

public abstract class QuickSort
{

            public QuickSort()
            {
            }

            public static void sort(Sortable sortable, int i, int j)
            {
/*  37*/        int k = i;
/*  38*/        int l = j;
/*  40*/        if(j > i)
                {
/*  44*/            int i1 = (i + j) / 2;
/*  52*/            while(k <= l) 
                    {
/*  52*/                while(k < j && sortable.compare(k, i1) < 0) 
/*  52*/                    k++;
/*  58*/                for(; l > i && sortable.compare(l, i1) > 0; l--);
/*  61*/                if(k <= l)
                        {
/*  62*/                    if(k != l)
                            {
/*  63*/                        sortable.swap(k, l);
/*  66*/                        if(k == i1)
/*  67*/                            i1 = l;
/*  68*/                        else
/*  68*/                        if(l == i1)
/*  69*/                            i1 = k;
                            }
/*  72*/                    k++;
/*  73*/                    l--;
                        }
                    }
/*  80*/            if(i < l)
/*  81*/                sort(sortable, i, l);
/*  86*/            if(k < j)
/*  87*/                sort(sortable, k, j);
                }
            }
}
