// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   LineNumberMap.java

package com.icl.saxon.tree;


public class LineNumberMap
{

            private int sequenceNumbers[];
            private int lineNumbers[];
            private int allocated;

            public LineNumberMap()
            {
/*  18*/        sequenceNumbers = new int[1000];
/*  19*/        lineNumbers = new int[1000];
/*  20*/        allocated = 0;
            }

            public void setLineNumber(int i, int j)
            {
/*  28*/        if(sequenceNumbers.length <= allocated + 1)
                {
/*  29*/            int ai[] = new int[allocated * 2];
/*  30*/            int ai1[] = new int[allocated * 2];
/*  31*/            System.arraycopy(sequenceNumbers, 0, ai, 0, allocated);
/*  32*/            System.arraycopy(lineNumbers, 0, ai1, 0, allocated);
/*  33*/            sequenceNumbers = ai;
/*  34*/            lineNumbers = ai1;
                }
/*  36*/        sequenceNumbers[allocated] = i;
/*  37*/        lineNumbers[allocated] = j;
/*  38*/        allocated++;
            }

            public int getLineNumber(int i)
            {
/*  47*/        for(int j = 1; j < allocated; j++)
/*  48*/            if(sequenceNumbers[j] > i)
/*  49*/                return lineNumbers[j - 1];

/*  52*/        return lineNumbers[allocated - 1];
            }
}
