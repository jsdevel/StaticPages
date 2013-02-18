// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ElementHandler.java

package com.icl.saxon.handlers;

import com.icl.saxon.Context;
import com.icl.saxon.NodeHandler;
import com.icl.saxon.om.NodeInfo;
import javax.xml.transform.TransformerException;

public abstract class ElementHandler
    implements NodeHandler
{

            public ElementHandler()
            {
            }

            public abstract void start(NodeInfo nodeinfo, Context context)
                throws TransformerException;

            public boolean needsStackFrame()
            {
/*  28*/        return false;
            }
}
