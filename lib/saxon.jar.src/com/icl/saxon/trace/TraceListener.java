// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TraceListener.java

package com.icl.saxon.trace;

import com.icl.saxon.Context;
import com.icl.saxon.NodeHandler;
import com.icl.saxon.om.NodeInfo;
import java.util.EventListener;

public interface TraceListener
    extends EventListener
{

    public abstract void open();

    public abstract void close();

    public abstract void toplevel(NodeInfo nodeinfo);

    public abstract void enterSource(NodeHandler nodehandler, Context context);

    public abstract void leaveSource(NodeHandler nodehandler, Context context);

    public abstract void enter(NodeInfo nodeinfo, Context context);

    public abstract void leave(NodeInfo nodeinfo, Context context);
}
