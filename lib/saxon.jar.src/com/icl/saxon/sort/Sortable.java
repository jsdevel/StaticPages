// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Sortable.java

package com.icl.saxon.sort;


public interface Sortable
{

    public abstract int compare(int i, int j);

    public abstract void swap(int i, int j);
}
