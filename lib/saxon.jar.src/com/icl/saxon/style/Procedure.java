// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Procedure.java

package com.icl.saxon.style;


public class Procedure
{

            protected int numberOfVariables;

            public Procedure()
            {
/*  13*/        numberOfVariables = 0;
            }

            public int getNumberOfVariables()
            {
/*  20*/        return numberOfVariables;
            }

            public int allocateSlotNumber()
            {
/*  28*/        return numberOfVariables++;
            }
}
