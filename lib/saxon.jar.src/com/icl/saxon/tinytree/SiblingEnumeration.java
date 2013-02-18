// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SiblingEnumeration.java

package com.icl.saxon.tinytree;

import com.icl.saxon.om.AxisEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyNodeImpl, TinyDocumentImpl

final class SiblingEnumeration
    implements AxisEnumeration
{

            TinyDocumentImpl document;
            int nextNodeNr;
            NodeTest test;
            TinyNodeImpl startNode;
            TinyNodeImpl parentNode;
            boolean getChildren;
            int last;

            protected SiblingEnumeration(TinyDocumentImpl tinydocumentimpl, TinyNodeImpl tinynodeimpl, NodeTest nodetest, boolean flag)
            {
/*  23*/        last = -1;
/*  27*/        document = tinydocumentimpl;
/*  28*/        test = nodetest;
/*  29*/        startNode = tinynodeimpl;
/*  30*/        getChildren = flag;
/*  31*/        if(flag)
                {
/*  32*/            parentNode = tinynodeimpl;
/*  35*/            nextNodeNr = tinynodeimpl.nodeNr + 1;
                } else
                {
/*  38*/            parentNode = (TinyNodeImpl)tinynodeimpl.getParent();
/*  41*/            nextNodeNr = tinydocumentimpl.next[tinynodeimpl.nodeNr];
                }
/*  45*/        if(nextNodeNr >= 0 && !nodetest.matches(document.nodeType[nextNodeNr], document.nameCode[nextNodeNr]))
/*  48*/            advance();
            }

            public boolean hasMoreElements()
            {
/*  54*/        return nextNodeNr >= 0;
            }

            public NodeInfo nextElement()
            {
/*  58*/        TinyNodeImpl tinynodeimpl = document.getNode(nextNodeNr);
/*  59*/        tinynodeimpl.setParentNode(parentNode);
/*  60*/        advance();
/*  61*/        return tinynodeimpl;
            }

            private void advance()
            {
/*  66*/        do
/*  66*/            nextNodeNr = document.next[nextNodeNr];
/*  67*/        while(nextNodeNr >= 0 && !test.matches(document.nodeType[nextNodeNr], document.nameCode[nextNodeNr]));
            }

            public boolean isSorted()
            {
/*  73*/        return true;
            }

            public boolean isReverseSorted()
            {
/*  77*/        return false;
            }

            public boolean isPeer()
            {
/*  81*/        return true;
            }

            public int getLastPosition()
            {
/*  89*/        if(last >= 0)
/*  89*/            return last;
/*  90*/        SiblingEnumeration siblingenumeration = new SiblingEnumeration(document, startNode, test, getChildren);
/*  92*/        for(last = 0; siblingenumeration.hasMoreElements(); last++)
/*  94*/            siblingenumeration.nextElement();

/*  97*/        return last;
            }
}
