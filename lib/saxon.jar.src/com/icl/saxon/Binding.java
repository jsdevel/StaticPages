// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Binding.java

package com.icl.saxon;

import com.icl.saxon.expr.Value;

public interface Binding
{

    public abstract boolean isGlobal();

    public abstract String getVariableName();

    public abstract int getVariableFingerprint();

    public abstract int getSlotNumber();

    public abstract int getDataType();

    public abstract Value constantValue();

    public abstract boolean isAssignable();
}
