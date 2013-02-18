// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   AttributeEnumeration.java

package com.icl.saxon.tree;

import com.icl.saxon.om.*;
import com.icl.saxon.pattern.NameTest;
import com.icl.saxon.pattern.NodeTest;

// Referenced classes of package com.icl.saxon.tree:
//            ElementImpl, AttributeImpl, AttributeCollection, NodeImpl

final class AttributeEnumeration
    implements AxisEnumeration
{

            private ElementImpl element;
            private NodeTest nodeTest;
            private NodeInfo next;
            private int index;
            private int length;
            private int last;

            public AttributeEnumeration(NodeImpl nodeimpl, NodeTest nodetest)
            {
/*  19*/        last = -1;
/*  32*/        nodeTest = nodetest;
/*  34*/        if(nodeimpl.getNodeType() == 1)
                {
/*  35*/            element = (ElementImpl)nodeimpl;
/*  36*/            AttributeCollection attributecollection = element.getAttributeList();
/*  37*/            index = 0;
/*  39*/            if(nodetest instanceof NameTest)
                    {
/*  40*/                NameTest nametest = (NameTest)nodetest;
/*  41*/                index = attributecollection.getIndexByFingerprint(nametest.getFingerprint());
/*  43*/                if(index < 0)
                        {
/*  44*/                    next = null;
                        } else
                        {
/*  46*/                    next = new AttributeImpl(element, index);
/*  47*/                    index = 0;
/*  48*/                    length = 0;
                        }
                    } else
                    {
/*  52*/                index = 0;
/*  53*/                length = attributecollection.getLength();
/*  54*/                advance();
                    }
                } else
                {
/*  59*/            next = null;
/*  60*/            index = 0;
/*  61*/            length = 0;
                }
            }

            public boolean hasMoreElements()
            {
/*  71*/        return next != null;
            }

            public NodeInfo nextElement()
            {
/*  80*/        NodeInfo nodeinfo = next;
/*  81*/        advance();
/*  82*/        return nodeinfo;
            }

            private void advance()
            {
/*  91*/        do
/*  91*/            if(index < length)
                    {
/*  92*/                next = new AttributeImpl(element, index);
/*  93*/                index++;
                    } else
                    {
/*  95*/                next = null;
/*  96*/                return;
                    }
/*  98*/        while(!nodeTest.matches(next));
            }

            public boolean isSorted()
            {
/* 102*/        return true;
            }

            public boolean isReverseSorted()
            {
/* 106*/        return false;
            }

            public boolean isPeer()
            {
/* 110*/        return true;
            }

            public int getLastPosition()
            {
/* 118*/        if(last >= 0)
/* 118*/            return last;
/* 119*/        AttributeEnumeration attributeenumeration = new AttributeEnumeration(element, nodeTest);
/* 121*/        for(last = 0; attributeenumeration.hasMoreElements(); last++)
/* 123*/            attributeenumeration.nextElement();

/* 126*/        return last;
            }
}
