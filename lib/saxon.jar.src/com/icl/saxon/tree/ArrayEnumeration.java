// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ArrayEnumeration.java

package com.icl.saxon.tree;

import com.icl.saxon.om.AxisEnumeration;
import com.icl.saxon.om.NodeInfo;
import java.io.PrintStream;

final class ArrayEnumeration
    implements AxisEnumeration
{

            NodeInfo nodes[];
            int index;

            public ArrayEnumeration(NodeInfo anodeinfo[])
            {
/*  18*/        index = 0;
/*  21*/        nodes = anodeinfo;
/*  22*/        index = 0;
/*  23*/        for(int i = 0; i < anodeinfo.length; i++)
/*  24*/            if(anodeinfo[i] == null)
/*  25*/                System.err.println("  node " + i + " is null");

            }

            public boolean hasMoreElements()
            {
/*  31*/        return index < nodes.length;
            }

            public NodeInfo nextElement()
            {
/*  35*/        return nodes[index++];
            }

            public boolean isSorted()
            {
/*  39*/        return true;
            }

            public boolean isReverseSorted()
            {
/*  43*/        return false;
            }

            public boolean isPeer()
            {
/*  47*/        return true;
            }

            public int getLastPosition()
            {
/*  51*/        return nodes.length;
            }
}
