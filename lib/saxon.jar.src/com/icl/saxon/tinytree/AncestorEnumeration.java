// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   AncestorEnumeration.java

package com.icl.saxon.tinytree;

import com.icl.saxon.om.AxisEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyNodeImpl, TinyDocumentImpl

final class AncestorEnumeration
    implements AxisEnumeration
{

            private int nextNodeNr;
            private TinyDocumentImpl document;
            private TinyNodeImpl node;
            private NodeTest test;
            private TinyNodeImpl first;
            private boolean includeSelf;
            private int last;

            public AncestorEnumeration(TinyDocumentImpl tinydocumentimpl, TinyNodeImpl tinynodeimpl, NodeTest nodetest, boolean flag)
            {
/*  17*/        first = null;
/*  19*/        last = -1;
/*  23*/        document = tinydocumentimpl;
/*  24*/        test = nodetest;
/*  25*/        node = tinynodeimpl;
/*  26*/        includeSelf = flag;
/*  27*/        if(flag && nodetest.matches(tinynodeimpl))
/*  28*/            first = tinynodeimpl;
/*  34*/        TinyNodeImpl tinynodeimpl1 = (TinyNodeImpl)tinynodeimpl.getParent();
/*  35*/        nextNodeNr = tinynodeimpl1.nodeNr;
/*  36*/        if(!nodetest.matches(tinynodeimpl1))
/*  37*/            advance();
            }

            public boolean hasMoreElements()
            {
/*  42*/        return first != null || nextNodeNr >= 0;
            }

            public NodeInfo nextElement()
            {
/*  46*/        if(first != null)
                {
/*  47*/            TinyNodeImpl tinynodeimpl = first;
/*  48*/            first = null;
/*  49*/            return tinynodeimpl;
                } else
                {
/*  51*/            TinyNodeImpl tinynodeimpl1 = document.getNode(nextNodeNr);
/*  52*/            advance();
/*  53*/            return tinynodeimpl1;
                }
            }

            private void advance()
            {
/*  58*/        int i = document.depth[nextNodeNr] - 1;
/*  61*/        do
                {
/*  61*/            do
                    {
/*  61*/                nextNodeNr--;
/*  62*/                if(nextNodeNr < 0)
/*  62*/                    return;
                    } while(document.depth[nextNodeNr] > i);
/*  64*/            if(test.matches(document.nodeType[nextNodeNr], document.nameCode[nextNodeNr]))
/*  66*/                return;
/*  68*/            i--;
                } while(nextNodeNr >= 0);
            }

            public boolean isSorted()
            {
/*  73*/        return false;
            }

            public boolean isReverseSorted()
            {
/*  77*/        return true;
            }

            public boolean isPeer()
            {
/*  81*/        return false;
            }

            public int getLastPosition()
            {
/*  89*/        if(last >= 0)
/*  89*/            return last;
/*  90*/        AncestorEnumeration ancestorenumeration = new AncestorEnumeration(document, node, test, includeSelf);
/*  92*/        for(last = 0; ancestorenumeration.hasMoreElements(); last++)
/*  94*/            ancestorenumeration.nextElement();

/*  97*/        return last;
            }
}
