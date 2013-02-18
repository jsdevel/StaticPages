// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   FollowingEnumeration.java

package com.icl.saxon.tinytree;

import com.icl.saxon.om.AxisEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyNodeImpl, TinyDocumentImpl

final class FollowingEnumeration
    implements AxisEnumeration
{

            private TinyDocumentImpl document;
            private TinyNodeImpl startNode;
            private int nextNodeNr;
            private NodeTest test;
            int last;
            boolean includeDescendants;

            public FollowingEnumeration(TinyDocumentImpl tinydocumentimpl, TinyNodeImpl tinynodeimpl, NodeTest nodetest, boolean flag)
            {
/*  17*/        last = -1;
/*  22*/        document = tinydocumentimpl;
/*  23*/        test = nodetest;
/*  24*/        startNode = tinynodeimpl;
/*  25*/        nextNodeNr = tinynodeimpl.nodeNr;
/*  26*/        includeDescendants = flag;
/*  27*/        short word0 = tinydocumentimpl.depth[nextNodeNr];
/*  30*/        if(flag)
/*  31*/            nextNodeNr++;
/*  34*/        else
/*  34*/            do
                    {
/*  34*/                nextNodeNr++;
/*  35*/                if(nextNodeNr >= tinydocumentimpl.numberOfNodes)
                        {
/*  36*/                    nextNodeNr = -1;
/*  37*/                    return;
                        }
                    } while(tinydocumentimpl.depth[nextNodeNr] > word0);
/*  42*/        if(!test.matches(tinydocumentimpl.nodeType[nextNodeNr], tinydocumentimpl.nameCode[nextNodeNr]))
/*  43*/            advance();
            }

            private void advance()
            {
/*  49*/        do
                {
/*  49*/            nextNodeNr++;
/*  50*/            if(nextNodeNr >= document.numberOfNodes)
                    {
/*  51*/                nextNodeNr = -1;
/*  52*/                return;
                    }
                } while(!test.matches(document.nodeType[nextNodeNr], document.nameCode[nextNodeNr]));
            }

            public boolean hasMoreElements()
            {
/*  58*/        return nextNodeNr >= 0;
            }

            public NodeInfo nextElement()
            {
/*  62*/        TinyNodeImpl tinynodeimpl = document.getNode(nextNodeNr);
/*  63*/        advance();
/*  64*/        return tinynodeimpl;
            }

            public boolean isSorted()
            {
/*  68*/        return true;
            }

            public boolean isReverseSorted()
            {
/*  72*/        return false;
            }

            public boolean isPeer()
            {
/*  76*/        return false;
            }

            public int getLastPosition()
            {
/*  84*/        if(last >= 0)
/*  84*/            return last;
/*  85*/        FollowingEnumeration followingenumeration = new FollowingEnumeration(document, startNode, test, includeDescendants);
/*  87*/        for(last = 0; followingenumeration.hasMoreElements(); last++)
/*  89*/            followingenumeration.nextElement();

/*  92*/        return last;
            }
}
