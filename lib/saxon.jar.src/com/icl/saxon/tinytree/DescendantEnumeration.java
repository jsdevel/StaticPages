// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DescendantEnumeration.java

package com.icl.saxon.tinytree;

import com.icl.saxon.om.AxisEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyNodeImpl, TinyDocumentImpl

final class DescendantEnumeration
    implements AxisEnumeration
{

            TinyDocumentImpl document;
            TinyNodeImpl startNode;
            boolean includeSelf;
            int nextNodeNr;
            int startDepth;
            NodeTest test;
            int last;
            TinyNodeImpl parentNode;

            protected DescendantEnumeration(TinyDocumentImpl tinydocumentimpl, TinyNodeImpl tinynodeimpl, NodeTest nodetest, boolean flag)
            {
/*  21*/        last = -1;
/*  26*/        document = tinydocumentimpl;
/*  27*/        startNode = tinynodeimpl;
/*  28*/        includeSelf = flag;
/*  29*/        test = nodetest;
/*  30*/        nextNodeNr = tinynodeimpl.nodeNr;
/*  31*/        startDepth = tinydocumentimpl.depth[nextNodeNr];
/*  32*/        if(!flag)
                {
/*  35*/            nextNodeNr++;
/*  36*/            if(tinydocumentimpl.depth[nextNodeNr] <= startDepth)
/*  37*/                nextNodeNr = -1;
                }
/*  42*/        if(nextNodeNr >= 0 && nextNodeNr < tinydocumentimpl.numberOfNodes && !nodetest.matches(document.nodeType[nextNodeNr], document.nameCode[nextNodeNr]))
/*  46*/            advance();
            }

            public boolean hasMoreElements()
            {
/*  51*/        return nextNodeNr >= 0;
            }

            public NodeInfo nextElement()
            {
/*  55*/        TinyNodeImpl tinynodeimpl = document.getNode(nextNodeNr);
/*  56*/        advance();
/*  57*/        return tinynodeimpl;
            }

            private void advance()
            {
/*  62*/        do
                {
/*  62*/            nextNodeNr++;
/*  63*/            if(nextNodeNr >= document.numberOfNodes || document.depth[nextNodeNr] <= startDepth)
                    {
/*  65*/                nextNodeNr = -1;
/*  66*/                return;
                    }
                } while(!test.matches(document.nodeType[nextNodeNr], document.nameCode[nextNodeNr]));
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
/*  81*/        return false;
            }

            public int getLastPosition()
            {
/*  89*/        if(last >= 0)
/*  89*/            return last;
/*  90*/        DescendantEnumeration descendantenumeration = new DescendantEnumeration(document, startNode, test, includeSelf);
/*  92*/        for(last = 0; descendantenumeration.hasMoreElements(); last++)
/*  94*/            descendantenumeration.nextElement();

/*  97*/        return last;
            }
}
