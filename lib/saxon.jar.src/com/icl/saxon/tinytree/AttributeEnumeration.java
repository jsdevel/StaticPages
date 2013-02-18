// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   AttributeEnumeration.java

package com.icl.saxon.tinytree;

import com.icl.saxon.om.AxisEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.pattern.NameTest;
import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyDocumentImpl

final class AttributeEnumeration
    implements AxisEnumeration
{

            private TinyDocumentImpl doc;
            private int element;
            private NodeTest nodeTest;
            private int index;
            private int last;

            protected AttributeEnumeration(TinyDocumentImpl tinydocumentimpl, int i, NodeTest nodetest)
            {
/*  18*/        last = -1;
/*  33*/        nodeTest = nodetest;
/*  34*/        doc = tinydocumentimpl;
/*  35*/        element = i;
/*  37*/        index = tinydocumentimpl.offset[i];
/*  38*/        advance();
            }

            public boolean hasMoreElements()
            {
/*  47*/        return index >= 0;
            }

            public NodeInfo nextElement()
            {
/*  56*/        int i = index++;
/*  57*/        if(nodeTest instanceof NameTest)
/*  59*/            index = -1;
/*  61*/        else
/*  61*/            advance();
/*  63*/        return doc.getAttributeNode(i);
            }

            private void advance()
            {
/*  72*/        do
                {
/*  72*/            if(index >= doc.numberOfAttributes || doc.attParent[index] != element)
                    {
/*  73*/                index = -1;
/*  74*/                return;
                    }
/*  76*/            if(nodeTest.matches((short)2, doc.attCode[index]))
/*  77*/                return;
/*  79*/            index++;
                } while(true);
            }

            public boolean isSorted()
            {
/*  84*/        return true;
            }

            public boolean isReverseSorted()
            {
/*  88*/        return false;
            }

            public boolean isPeer()
            {
/*  92*/        return true;
            }

            public int getLastPosition()
            {
/* 100*/        if(last >= 0)
/* 100*/            return last;
/* 101*/        AttributeEnumeration attributeenumeration = new AttributeEnumeration(doc, element, nodeTest);
/* 103*/        for(last = 0; attributeenumeration.hasMoreElements(); last++)
/* 105*/            attributeenumeration.nextElement();

/* 108*/        return last;
            }
}
