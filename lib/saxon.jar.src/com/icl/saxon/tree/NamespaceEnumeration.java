// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NamespaceEnumeration.java

package com.icl.saxon.tree;

import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.pattern.NodeTest;
import java.util.Vector;

// Referenced classes of package com.icl.saxon.tree:
//            TreeEnumeration, ElementImpl, NamespaceImpl, NodeImpl

final class NamespaceEnumeration extends TreeEnumeration
{

            private ElementImpl element;
            private Vector nslist;
            private int index;
            private int length;

            public NamespaceEnumeration(NodeImpl nodeimpl, NodeTest nodetest)
            {
/*  15*/        super(nodeimpl, nodetest);
/*  17*/        if(nodeimpl instanceof ElementImpl)
                {
/*  18*/            element = (ElementImpl)nodeimpl;
/*  19*/            nslist = new Vector(10);
/*  20*/            element.addNamespaceNodes(element, nslist, true);
/*  21*/            index = -1;
/*  22*/            length = nslist.size();
/*  23*/            advance();
                } else
                {
/*  25*/            super.next = null;
                }
            }

            public void step()
            {
/*  31*/        index++;
/*  32*/        if(index < length)
/*  33*/            super.next = (NamespaceImpl)nslist.elementAt(index);
/*  35*/        else
/*  35*/            super.next = null;
            }

            protected boolean conforms(NodeInfo nodeinfo)
            {
/*  45*/        if(nodeinfo == null)
/*  45*/            return true;
/*  46*/        NamespaceImpl namespaceimpl = (NamespaceImpl)nodeinfo;
/*  47*/        if(namespaceimpl.getLocalName().equals("") && namespaceimpl.getStringValue().equals(""))
/*  48*/            return false;
/*  50*/        else
/*  50*/            return super.nodeTest.matches(nodeinfo);
            }

            public boolean isSorted()
            {
/*  54*/        return false;
            }

            public boolean isPeer()
            {
/*  58*/        return true;
            }

            public int getLastPosition()
            {
/*  66*/        if(super.last >= 0)
                {
/*  66*/            return super.last;
                } else
                {
/*  67*/            NamespaceEnumeration namespaceenumeration = new NamespaceEnumeration(super.start, super.nodeTest);
/*  69*/            return namespaceenumeration.count();
                }
            }
}
