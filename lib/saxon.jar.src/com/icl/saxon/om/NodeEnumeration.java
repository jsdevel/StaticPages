// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NodeEnumeration.java

package com.icl.saxon.om;

import com.icl.saxon.expr.XPathException;

// Referenced classes of package com.icl.saxon.om:
//            NodeInfo

public interface NodeEnumeration
{

    public abstract boolean hasMoreElements();

    public abstract NodeInfo nextElement()
        throws XPathException;

    public abstract boolean isSorted();

    public abstract boolean isReverseSorted();

    public abstract boolean isPeer();
}
