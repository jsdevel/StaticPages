// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   HashMap.java

package com.icl.saxon.sort;


public class HashMap
{

            String strings[];
            int size;

            public HashMap(int i)
            {
/*  19*/        strings = new String[i];
/*  20*/        size = i;
            }

            public void set(String s)
            {
/*  24*/        int i = (hashCode(s) & 0x7fffffff) % size;
/*  26*/        do
                {
/*  26*/            if(strings[i] == null)
                    {
/*  27*/                strings[i] = s;
/*  28*/                return;
                    }
/*  30*/            if(strings[i].equalsIgnoreCase(s))
/*  31*/                return;
/*  33*/            i = (i + 1) % size;
                } while(true);
            }

            public boolean get(String s)
            {
/*  38*/        int i = (hashCode(s) & 0x7fffffff) % size;
/*  40*/        do
                {
/*  40*/            if(strings[i] == null)
/*  41*/                return false;
/*  43*/            if(strings[i].equalsIgnoreCase(s))
/*  44*/                return true;
/*  46*/            i = (i + 1) % size;
                } while(true);
            }

            private int hashCode(String s)
            {
/*  53*/        int i = 0;
/*  54*/        int j = s.length();
/*  55*/        if(j > 24)
/*  55*/            j = 24;
/*  56*/        for(int k = 0; k < j; k++)
/*  57*/            i = (i << 1) + (s.charAt(k) & 0xdf);

/*  59*/        return i;
            }
}
