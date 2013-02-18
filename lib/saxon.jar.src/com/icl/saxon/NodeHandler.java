// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NodeHandler.java

package com.icl.saxon;

import com.icl.saxon.om.NodeInfo;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon:
//            Context

public interface NodeHandler
{

    public abstract void start(NodeInfo nodeinfo, Context context)
        throws TransformerException;

    public abstract boolean needsStackFrame();
}
