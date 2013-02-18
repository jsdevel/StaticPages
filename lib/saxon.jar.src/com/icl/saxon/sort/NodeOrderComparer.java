// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NodeOrderComparer.java

package com.icl.saxon.sort;

import com.icl.saxon.om.NodeInfo;

public interface NodeOrderComparer
{

    public abstract int compare(NodeInfo nodeinfo, NodeInfo nodeinfo1);
}
