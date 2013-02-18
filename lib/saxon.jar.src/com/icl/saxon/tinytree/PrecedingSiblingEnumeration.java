// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   PrecedingSiblingEnumeration.java

package com.icl.saxon.tinytree;

import com.icl.saxon.om.AxisEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyDocumentImpl, TinyNodeImpl

final class PrecedingSiblingEnumeration
    implements AxisEnumeration
{

            TinyDocumentImpl document;
            TinyNodeImpl startNode;
            int nextNodeNr;
            int depth;
            NodeTest test;
            TinyNodeImpl parentNode;
            int last;

            protected PrecedingSiblingEnumeration(TinyDocumentImpl tinydocumentimpl, TinyNodeImpl tinynodeimpl, NodeTest nodetest)
            {
/*  20*/        last = -1;
/*  24*/        document = tinydocumentimpl;
/*  25*/        document.ensurePriorIndex();
/*  26*/        test = nodetest;
/*  27*/        startNode = tinynodeimpl;
/*  28*/        nextNodeNr = tinynodeimpl.nodeNr;
/*  29*/        depth = tinydocumentimpl.depth[nextNodeNr];
/*  30*/        parentNode = tinynodeimpl.parent;
/*  31*/        advance();
            }

            public boolean hasMoreElements()
            {
/*  35*/        return nextNodeNr >= 0;
            }

            public NodeInfo nextElement()
            {
/*  39*/        TinyNodeImpl tinynodeimpl = document.getNode(nextNodeNr);
/*  40*/        tinynodeimpl.setParentNode(parentNode);
/*  41*/        advance();
/*  42*/        return tinynodeimpl;
            }

            private void advance()
            {
/*  47*/        do
/*  47*/            nextNodeNr = document.prior[nextNodeNr];
/*  48*/        while(nextNodeNr >= 0 && !test.matches(document.nodeType[nextNodeNr], document.nameCode[nextNodeNr]));
            }

            public boolean isSorted()
            {
/*  70*/        return false;
            }

            public boolean isReverseSorted()
            {
/*  74*/        return true;
            }

            public boolean isPeer()
            {
/*  78*/        return true;
            }

            public int getLastPosition()
            {
/*  86*/        if(last >= 0)
/*  86*/            return last;
/*  87*/        PrecedingSiblingEnumeration precedingsiblingenumeration = new PrecedingSiblingEnumeration(document, startNode, test);
/*  89*/        for(last = 0; precedingsiblingenumeration.hasMoreElements(); last++)
/*  91*/            precedingsiblingenumeration.nextElement();

/*  94*/        return last;
            }
}
