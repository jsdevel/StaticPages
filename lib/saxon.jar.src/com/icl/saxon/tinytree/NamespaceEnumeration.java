// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NamespaceEnumeration.java

package com.icl.saxon.tinytree;

import com.icl.saxon.om.*;
import com.icl.saxon.pattern.*;
import java.util.Vector;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyDocumentImpl, TinyElementImpl, TinyNodeImpl, TinyNamespaceImpl

final class NamespaceEnumeration
    implements AxisEnumeration
{

            private TinyDocumentImpl document;
            private TinyElementImpl element;
            private NamePool pool;
            private int owner;
            private int currentElement;
            private int index;
            private Vector list;
            private NodeTest nodeTest;
            private int last;
            private int xmlNamespace;

            protected NamespaceEnumeration(TinyElementImpl tinyelementimpl, NodeTest nodetest)
            {
/*  22*/        list = new Vector();
/*  24*/        last = -1;
/*  34*/        element = tinyelementimpl;
/*  35*/        owner = ((TinyNodeImpl) (tinyelementimpl)).nodeNr;
/*  36*/        document = (TinyDocumentImpl)tinyelementimpl.getDocumentRoot();
/*  37*/        pool = document.getNamePool();
/*  38*/        currentElement = owner;
/*  39*/        index = document.length[currentElement];
/*  40*/        nodeTest = nodetest;
/*  41*/        xmlNamespace = pool.allocate("", "", "xml");
/*  42*/        advance();
            }

            private void advance()
            {
/*  47*/        if(index == 0)
                {
/*  48*/            index = -1;
/*  49*/            return;
                }
/*  50*/        if(index > 0)
/*  54*/            for(; index < document.numberOfNamespaces && document.namespaceParent[index] == currentElement; index++)
                    {
/*  54*/                int i = document.namespaceCode[index];
/*  59*/                if(i == 0)
/*  60*/                    list.addElement(new Short((short)0));
/*  62*/                else
/*  62*/                if(matches(i))
                        {
/*  63*/                    short word0 = (short)(i >> 16);
/*  65*/                    int j = list.size();
/*  66*/                    boolean flag = false;
/*  69*/                    for(int k = 0; k < j;)
                            {
/*  70*/                        short word1 = ((Short)list.elementAt(k++)).shortValue();
/*  71*/                        if(word1 == word0)
                                {
/*  72*/                            flag = true;
/*  73*/                            break;
                                }
                            }

/*  76*/                    if(!flag)
                            {
/*  77*/                        list.addElement(new Short(word0));
/*  78*/                        return;
                            }
                        }
                    }

/*  87*/        NodeInfo nodeinfo = document.getNode(currentElement).getParent();
/*  88*/        if(nodeinfo.getNodeType() == 9)
                {
/*  89*/            if(nodeTest.matches((short)13, xmlNamespace))
/*  90*/                index = 0;
/*  92*/            else
/*  92*/                index = -1;
                } else
                {
/*  95*/            currentElement = ((TinyNodeImpl) ((TinyElementImpl)nodeinfo)).nodeNr;
/*  96*/            index = document.length[currentElement];
/*  97*/            advance();
                }
            }

            private boolean matches(int i)
            {
/* 103*/        if((nodeTest instanceof NodeTypeTest) && nodeTest.getNodeType() == 13)
                {
/* 105*/            return true;
                } else
                {
/* 107*/            int j = pool.allocate("", "", pool.getPrefixFromNamespaceCode(i));
/* 108*/            return nodeTest.matches((short)13, j);
                }
            }

            public boolean hasMoreElements()
            {
/* 113*/        return index >= 0;
            }

            public NodeInfo nextElement()
            {
/* 118*/        TinyNamespaceImpl tinynamespaceimpl = document.getNamespaceNode(index);
/* 119*/        tinynamespaceimpl.setParentNode(owner);
/* 120*/        advance();
/* 121*/        return tinynamespaceimpl;
            }

            public boolean isSorted()
            {
/* 125*/        return false;
            }

            public boolean isReverseSorted()
            {
/* 129*/        return false;
            }

            public boolean isPeer()
            {
/* 133*/        return true;
            }

            public int getLastPosition()
            {
/* 141*/        if(last >= 0)
/* 141*/            return last;
/* 142*/        NamespaceEnumeration namespaceenumeration = new NamespaceEnumeration(element, nodeTest);
/* 144*/        for(last = 0; namespaceenumeration.hasMoreElements(); last++)
/* 146*/            namespaceenumeration.nextElement();

/* 149*/        return last;
            }
}
