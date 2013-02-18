// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   AxisEnumeration.java

package com.icl.saxon.om;

import com.icl.saxon.expr.LastPositionFinder;

// Referenced classes of package com.icl.saxon.om:
//            NodeEnumeration, NodeInfo

public interface AxisEnumeration
    extends NodeEnumeration, LastPositionFinder
{

    public abstract boolean hasMoreElements();

    public abstract NodeInfo nextElement();

    public abstract int getLastPosition();
}
