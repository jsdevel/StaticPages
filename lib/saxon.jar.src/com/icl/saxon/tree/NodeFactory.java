// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NodeFactory.java

package com.icl.saxon.tree;

import com.icl.saxon.om.NodeInfo;
import org.xml.sax.Locator;

// Referenced classes of package com.icl.saxon.tree:
//            AttributeCollection, ElementImpl

public interface NodeFactory
{

    public abstract ElementImpl makeElementNode(NodeInfo nodeinfo, int i, AttributeCollection attributecollection, int ai[], int j, Locator locator, int k);
}
