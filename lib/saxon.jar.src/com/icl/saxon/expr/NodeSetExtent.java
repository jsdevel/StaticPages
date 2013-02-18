// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NodeSetExtent.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.om.*;
import com.icl.saxon.sort.*;
import java.util.Vector;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Referenced classes of package com.icl.saxon.expr:
//            NodeSetValue, EmptyNodeSet, SingletonNodeSet, XPathException, 
//            Expression, LastPositionFinder

public final class NodeSetExtent extends NodeSetValue
    implements Sortable, NodeList
{
    private class NodeSetValueEnumeration
        implements AxisEnumeration, LastPositionFinder
    {

                int index;

                public boolean hasMoreElements()
                {
/* 333*/            return index < length;
                }

                public NodeInfo nextElement()
                {
/* 338*/            return value[index++];
                }

                public boolean isSorted()
                {
/* 342*/            return sorted;
                }

                public boolean isReverseSorted()
                {
/* 346*/            return reverseSorted;
                }

                public boolean isPeer()
                {
/* 350*/            return false;
                }

                public int getLastPosition()
                {
/* 354*/            return length;
                }

                public NodeSetValueEnumeration()
                {
/* 324*/            index = 0;
/* 327*/            index = 0;
                }
    }


            private NodeInfo value[];
            private int length;
            private boolean sorted;
            private boolean reverseSorted;
            private NodeOrderComparer comparer;

            public NodeSetExtent(NodeOrderComparer nodeordercomparer)
            {
/*  33*/        comparer = nodeordercomparer;
/*  34*/        value = new NodeInfo[0];
/*  35*/        length = 0;
/*  36*/        sorted = true;
/*  37*/        reverseSorted = true;
            }

            public NodeSetExtent(NodeInfo anodeinfo[], NodeOrderComparer nodeordercomparer)
            {
/*  47*/        value = anodeinfo;
/*  48*/        length = anodeinfo.length;
/*  49*/        sorted = length < 2;
/*  50*/        reverseSorted = length < 2;
/*  51*/        comparer = nodeordercomparer;
            }

            public NodeSetExtent(Vector vector, NodeOrderComparer nodeordercomparer)
            {
/*  62*/        value = new NodeInfo[vector.size()];
/*  63*/        for(int i = 0; i < vector.size(); i++)
/*  64*/            value[i] = (NodeInfo)vector.elementAt(i);

/*  66*/        length = vector.size();
/*  67*/        sorted = length < 2;
/*  68*/        reverseSorted = length < 2;
/*  69*/        comparer = nodeordercomparer;
            }

            public NodeSetExtent(NodeEnumeration nodeenumeration, NodeOrderComparer nodeordercomparer)
                throws XPathException
            {
/*  81*/        comparer = nodeordercomparer;
/*  82*/        int i = 20;
/*  91*/        value = new NodeInfo[i];
/*  92*/        int j = 0;
/*  94*/        while(nodeenumeration.hasMoreElements()) 
                {
/*  94*/            if(j >= i)
                    {
/*  95*/                i *= 2;
/*  96*/                NodeInfo anodeinfo[] = new NodeInfo[i];
/*  97*/                System.arraycopy(value, 0, anodeinfo, 0, j);
/*  98*/                value = anodeinfo;
                    }
/* 100*/            value[j++] = nodeenumeration.nextElement();
                }
/* 102*/        sorted = nodeenumeration.isSorted() || j < 2;
/* 103*/        reverseSorted = nodeenumeration.isReverseSorted() || j < 2;
/* 104*/        length = j;
            }

            public void append(NodeInfo nodeinfo)
            {
/* 115*/        reverseSorted = false;
/* 116*/        if(value.length < length + 1)
                {
/* 117*/            NodeInfo anodeinfo[] = new NodeInfo[length != 0 ? length * 2 : 10];
/* 118*/            System.arraycopy(value, 0, anodeinfo, 0, length);
/* 119*/            value = anodeinfo;
                }
/* 121*/        if(length > 0 && value[length - 1].isSameNodeInfo(nodeinfo))
                {
/* 122*/            return;
                } else
                {
/* 124*/            value[length++] = nodeinfo;
/* 126*/            return;
                }
            }

            public Expression simplify()
            {
/* 133*/        if(length == 0)
/* 134*/            return new EmptyNodeSet();
/* 135*/        if(length == 1)
/* 136*/            return new SingletonNodeSet(value[0]);
/* 138*/        else
/* 138*/            return this;
            }

            public void setSorted(boolean flag)
            {
/* 151*/        sorted = flag;
            }

            public boolean isSorted()
            {
/* 161*/        return sorted;
            }

            public String asString()
            {
/* 171*/        return length <= 0 ? "" : getFirst().getStringValue();
            }

            public boolean asBoolean()
                throws XPathException
            {
/* 180*/        return length > 0;
            }

            public int getCount()
            {
/* 189*/        sort();
/* 190*/        return length;
            }

            public NodeSetValue sort()
            {
/* 202*/        if(length < 2)
/* 202*/            sorted = true;
/* 203*/        if(sorted)
/* 203*/            return this;
/* 205*/        if(reverseSorted)
                {
/* 207*/            NodeInfo anodeinfo[] = new NodeInfo[length];
/* 208*/            for(int j = 0; j < length; j++)
/* 209*/                anodeinfo[j] = value[length - j - 1];

/* 211*/            value = anodeinfo;
/* 212*/            sorted = true;
/* 213*/            reverseSorted = false;
                } else
                {
/* 218*/            QuickSort.sort(this, 0, length - 1);
/* 224*/            int i = 1;
/* 225*/            for(int k = 1; k < length; k++)
/* 226*/                if(!value[k].isSameNodeInfo(value[k - 1]))
/* 227*/                    value[i++] = value[k];

/* 230*/            length = i;
/* 232*/            sorted = true;
/* 233*/            reverseSorted = false;
                }
/* 235*/        return this;
            }

            public NodeInfo getFirst()
            {
/* 244*/        if(length == 0)
/* 244*/            return null;
/* 245*/        if(sorted)
/* 245*/            return value[0];
/* 248*/        NodeInfo nodeinfo = value[0];
/* 249*/        for(int i = 1; i < length; i++)
/* 250*/            if(comparer.compare(value[i], nodeinfo) < 0)
/* 251*/                nodeinfo = value[i];

/* 254*/        return nodeinfo;
            }

            public NodeInfo selectFirst(Context context)
            {
/* 265*/        return getFirst();
            }

            public NodeEnumeration enumerate()
            {
/* 273*/        return new NodeSetValueEnumeration();
            }

            public int getLength()
            {
/* 283*/        return getCount();
            }

            public Node item(int i)
            {
/* 291*/        sort();
/* 292*/        if(length > i && (value[i] instanceof Node))
/* 293*/            return (Node)value[i];
/* 295*/        else
/* 295*/            return null;
            }

            public int compare(int i, int j)
            {
/* 305*/        return comparer.compare(value[i], value[j]);
            }

            public void swap(int i, int j)
            {
/* 313*/        NodeInfo nodeinfo = value[i];
/* 314*/        value[i] = value[j];
/* 315*/        value[j] = nodeinfo;
            }




}
