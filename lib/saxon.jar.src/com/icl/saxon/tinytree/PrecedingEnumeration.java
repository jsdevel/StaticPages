// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   PrecedingEnumeration.java

package com.icl.saxon.tinytree;

import com.icl.saxon.om.AxisEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyNodeImpl, TinyDocumentImpl

final class PrecedingEnumeration
    implements AxisEnumeration
{

            TinyDocumentImpl document;
            TinyNodeImpl startNode;
            NodeTest test;
            int nextNodeNr;
            int nextAncestorDepth;
            boolean includeAncestors;
            int last;

            public PrecedingEnumeration(TinyDocumentImpl tinydocumentimpl, TinyNodeImpl tinynodeimpl, NodeTest nodetest, boolean flag)
            {
/*  23*/        last = -1;
/*  28*/        includeAncestors = flag;
/*  29*/        test = nodetest;
/*  30*/        document = tinydocumentimpl;
/*  31*/        startNode = tinynodeimpl;
/*  32*/        nextNodeNr = tinynodeimpl.nodeNr;
/*  33*/        nextAncestorDepth = tinydocumentimpl.depth[nextNodeNr] - 1;
/*  34*/        advance();
            }

            public boolean hasMoreElements()
            {
/*  38*/        return nextNodeNr >= 0;
            }

            public NodeInfo nextElement()
            {
/*  42*/        TinyNodeImpl tinynodeimpl = document.getNode(nextNodeNr);
/*  43*/        advance();
/*  44*/        return tinynodeimpl;
            }

            private void advance()
            {
/*  49*/        do
                {
/*  49*/            nextNodeNr--;
/*  50*/            if(!includeAncestors)
/*  53*/                for(; nextNodeNr >= 0 && document.depth[nextNodeNr] == nextAncestorDepth; nextNodeNr--)
/*  53*/                    nextAncestorDepth--;

                } while(nextNodeNr >= 0 && !test.matches(document.nodeType[nextNodeNr], document.nameCode[nextNodeNr]));
            }

            public boolean isSorted()
            {
/*  63*/        return false;
            }

            public boolean isReverseSorted()
            {
/*  67*/        return true;
            }

            public boolean isPeer()
            {
/*  71*/        return false;
            }

            public int getLastPosition()
            {
/*  79*/        if(last >= 0)
/*  79*/            return last;
/*  80*/        PrecedingEnumeration precedingenumeration = new PrecedingEnumeration(document, startNode, test, includeAncestors);
/*  82*/        for(last = 0; precedingenumeration.hasMoreElements(); last++)
/*  84*/            precedingenumeration.nextElement();

/*  87*/        return last;
            }
}
