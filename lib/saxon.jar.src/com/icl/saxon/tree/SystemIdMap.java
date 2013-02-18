// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SystemIdMap.java

package com.icl.saxon.tree;


public class SystemIdMap
{

            private int sequenceNumbers[];
            private String uris[];
            private int allocated;

            public SystemIdMap()
            {
/*  22*/        sequenceNumbers = new int[10];
/*  23*/        uris = new String[10];
/*  24*/        allocated = 0;
            }

            public void setSystemId(int i, String s)
            {
/*  33*/        if(allocated > 0 && s.equals(uris[allocated - 1]))
/*  34*/            return;
/*  36*/        if(sequenceNumbers.length <= allocated + 1)
                {
/*  37*/            int ai[] = new int[allocated * 2];
/*  38*/            String as[] = new String[allocated * 2];
/*  39*/            System.arraycopy(sequenceNumbers, 0, ai, 0, allocated);
/*  40*/            System.arraycopy(uris, 0, as, 0, allocated);
/*  41*/            sequenceNumbers = ai;
/*  42*/            uris = as;
                }
/*  44*/        sequenceNumbers[allocated] = i;
/*  45*/        uris[allocated] = s;
/*  46*/        allocated++;
            }

            public String getSystemId(int i)
            {
/*  55*/        for(int j = 1; j < allocated; j++)
/*  56*/            if(sequenceNumbers[j] > i)
/*  57*/                return uris[j - 1];

/*  60*/        return uris[allocated - 1];
            }
}
