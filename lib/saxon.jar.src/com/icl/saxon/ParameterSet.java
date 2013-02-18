// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ParameterSet.java

package com.icl.saxon;

import com.icl.saxon.expr.Value;

public class ParameterSet
{

            private int keys[];
            private Value values[];
            private int used;

            public ParameterSet()
            {
/*  12*/        keys = new int[10];
/*  13*/        values = new Value[10];
/*  14*/        used = 0;
            }

            public void put(int i, Value value)
            {
/*  24*/        for(int j = 0; j < used; j++)
/*  25*/            if(keys[j] == i)
                    {
/*  26*/                values[j] = value;
/*  27*/                return;
                    }

/*  30*/        if(used + 1 > keys.length)
                {
/*  31*/            int ai[] = new int[used * 2];
/*  32*/            Value avalue[] = new Value[used * 2];
/*  33*/            System.arraycopy(values, 0, avalue, 0, used);
/*  34*/            System.arraycopy(keys, 0, ai, 0, used);
/*  35*/            values = avalue;
/*  36*/            keys = ai;
                }
/*  38*/        keys[used] = i;
/*  39*/        values[used++] = value;
            }

            public Value get(int i)
            {
/*  49*/        for(int j = 0; j < used; j++)
/*  50*/            if(keys[j] == i)
/*  51*/                return values[j];

/*  54*/        return null;
            }

            public void clear()
            {
/*  62*/        used = 0;
            }
}
