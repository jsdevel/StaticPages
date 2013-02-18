// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Compare_en.java

package com.icl.saxon.sort;


// Referenced classes of package com.icl.saxon.sort:
//            TextComparer, Comparer

public class Compare_en extends TextComparer
{

            private static String supp = "AAAAAAACEEEEIIII[NOOOOO*OUUUUY]Saaaaaaaceeeeiiii{nooooo*ouuuuy}y";
            int caseOrder;

            public Compare_en()
            {
/*  21*/        caseOrder = 2;
            }

            public int compare(Object obj, Object obj1)
            {
/*  33*/        char ac[] = ((String)obj).toCharArray();
/*  34*/        char ac1[] = ((String)obj1).toCharArray();
/*  35*/        int i = ac.length;
/*  36*/        int j = ac1.length;
/*  39*/        for(int k = 0; k < i; k++)
                {
/*  40*/            char c = ac[k];
/*  41*/            if(c >= '\300' && c <= '\377')
/*  42*/                ac[k] = supp.charAt(c - 192);
                }

/*  45*/        for(int l = 0; l < j; l++)
                {
/*  46*/            char c1 = ac1[l];
/*  47*/            if(c1 >= '\300' && c1 <= '\377')
/*  48*/                ac1[l] = supp.charAt(c1 - 192);
                }

/*  51*/        int i1 = 0;
/*  52*/        for(int j1 = 0; i1 != i || j1 != j;)
                {
/*  57*/            if(i1 == i)
/*  57*/                return -1;
/*  58*/            if(j1 == j)
/*  58*/                return 1;
/*  59*/            int i2 = Character.toLowerCase(ac[i1]) - Character.toLowerCase(ac1[j1]);
/*  61*/            i1++;
/*  62*/            j1++;
/*  63*/            if(i2 != 0)
/*  63*/                return i2;
                }

/*  68*/        ac = ((String)obj).toCharArray();
/*  69*/        ac1 = ((String)obj1).toCharArray();
/*  70*/        i1 = 0;
/*  71*/        for(int k1 = 0; i1 != i || k1 != j;)
                {
/*  74*/            if(i1 == i)
/*  74*/                return -1;
/*  75*/            if(k1 == j)
/*  75*/                return 1;
/*  76*/            int j2 = Character.toLowerCase(ac[i1]) - Character.toLowerCase(ac1[k1]);
/*  78*/            i1++;
/*  79*/            k1++;
/*  80*/            if(j2 != 0)
/*  80*/                return j2;
                }

/*  85*/        i1 = 0;
/*  86*/        int l1 = 0;
                int k2;
/*  88*/        do
                {
/*  88*/            if(i1 == i)
/*  88*/                return 0;
/*  89*/            k2 = ac[i1++] - ac1[l1++];
                } while(k2 == 0);
/*  91*/        if(caseOrder == 1)
/*  92*/            return Character.isLowerCase(ac[i1 - 1]) ? -1 : 1;
/*  94*/        else
/*  94*/            return Character.isUpperCase(ac[i1 - 1]) ? -1 : 1;
            }

            public Comparer setCaseOrder(int i)
            {
/* 102*/        caseOrder = i;
/* 103*/        return this;
            }

}
