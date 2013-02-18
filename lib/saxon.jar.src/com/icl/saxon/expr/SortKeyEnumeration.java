// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SortKeyEnumeration.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.sort.*;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            NodeSetExtent, LastPositionFinder, LookaheadEnumerator, XPathException, 
//            Expression

public final class SortKeyEnumeration
    implements NodeEnumeration, LastPositionFinder, Sortable
{

            protected NodeEnumeration base;
            private SortKeyDefinition sortkeys[];
            private int recordSize;
            private Object nodeKeys[];
            private int count;
            private int index;
            private Context context;
            private Controller controller;
            private Comparer keyComparers[];

            public SortKeyEnumeration(Context context1, NodeEnumeration nodeenumeration, SortKeyDefinition asortkeydefinition[])
                throws XPathException
            {
/*  30*/        count = -1;
/*  33*/        index = 0;
/*  43*/        context = context1.newContext();
/*  44*/        controller = context1.getController();
/*  45*/        base = nodeenumeration;
/*  46*/        sortkeys = asortkeydefinition;
/*  47*/        recordSize = asortkeydefinition.length + 1;
/*  49*/        keyComparers = new Comparer[asortkeydefinition.length];
/*  50*/        for(int i = 0; i < asortkeydefinition.length; i++)
/*  51*/            keyComparers[i] = asortkeydefinition[i].getComparer(context1);

/*  57*/        if(!base.isSorted())
                {
/*  58*/            boolean flag = false;
/*  59*/            for(int j = 0; j < asortkeydefinition.length; j++)
                    {
/*  60*/                SortKeyDefinition sortkeydefinition = asortkeydefinition[j];
/*  61*/                Expression expression = sortkeydefinition.getSortKey();
/*  62*/                if((expression.getDependencies() & 0x30) == 0)
/*  63*/                    continue;
/*  63*/                flag = true;
/*  64*/                break;
                    }

/*  67*/            if(flag)
                    {
/*  68*/                NodeSetExtent nodesetextent = new NodeSetExtent(base, controller);
/*  69*/                nodesetextent.sort();
/*  70*/                base = nodesetextent.enumerate();
                    }
                }
            }

            public boolean hasMoreElements()
            {
/*  80*/        if(count < 0)
/*  81*/            return base.hasMoreElements();
/*  83*/        else
/*  83*/            return index < count;
            }

            public NodeInfo nextElement()
                throws XPathException
            {
/*  92*/        if(count < 0)
/*  93*/            doSort();
/*  95*/        return (NodeInfo)nodeKeys[index++ * recordSize];
            }

            public boolean isSorted()
            {
/*  99*/        return true;
            }

            public boolean isReverseSorted()
            {
/* 103*/        return false;
            }

            public boolean isPeer()
            {
/* 107*/        return base.isPeer();
            }

            public int getLastPosition()
                throws XPathException
            {
/* 111*/        if((base instanceof LastPositionFinder) && !(base instanceof LookaheadEnumerator))
/* 112*/            return ((LastPositionFinder)base).getLastPosition();
/* 114*/        if(count < 0)
/* 114*/            doSort();
/* 115*/        return count;
            }

            private void buildArray()
                throws XPathException
            {
                int i;
/* 120*/        if((base instanceof LastPositionFinder) && !(base instanceof LookaheadEnumerator))
                {
/* 121*/            i = ((LastPositionFinder)base).getLastPosition();
/* 122*/            context.setLast(i);
                } else
                {
/* 124*/            i = 100;
                }
/* 126*/        nodeKeys = new Object[i * recordSize];
/* 127*/        for(count = 0; base.hasMoreElements(); count++)
                {
/* 132*/            NodeInfo nodeinfo = base.nextElement();
/* 133*/            if(count == i)
                    {
/* 134*/                i *= 2;
/* 135*/                Object aobj[] = new Object[i * recordSize];
/* 136*/                System.arraycopy(((Object) (nodeKeys)), 0, ((Object) (aobj)), 0, count * recordSize);
/* 137*/                nodeKeys = aobj;
                    }
/* 139*/            context.setCurrentNode(nodeinfo);
/* 140*/            context.setContextNode(nodeinfo);
/* 141*/            context.setPosition(count + 1);
/* 143*/            int j = count * recordSize;
/* 144*/            nodeKeys[j] = nodeinfo;
/* 145*/            for(int k = 0; k < sortkeys.length; k++)
/* 146*/                nodeKeys[j + k + 1] = sortkeys[k].getSortKey().evaluateAsString(context);

                }

            }

            private void diag()
            {
/* 154*/        System.err.println("Diagnostic print of keys");
/* 155*/        for(int i = 0; i < count * recordSize; i++)
/* 156*/            System.err.println(i + " : " + nodeKeys[i]);

            }

            private void doSort()
                throws XPathException
            {
/* 162*/        buildArray();
/* 163*/        if(count < 2)
                {
/* 163*/            return;
                } else
                {
/* 167*/            QuickSort.sort(this, 0, count - 1);
/* 168*/            return;
                }
            }

            public int compare(int i, int j)
            {
/* 176*/        int k = i * recordSize + 1;
/* 177*/        int l = j * recordSize + 1;
/* 178*/        for(int i1 = 0; i1 < sortkeys.length; i1++)
                {
/* 179*/            Comparer comparer = keyComparers[i1];
/* 180*/            int j1 = comparer.compare(nodeKeys[k + i1], nodeKeys[l + i1]);
/* 181*/            if(j1 != 0)
/* 181*/                return j1;
                }

/* 184*/        return controller.compare((NodeInfo)nodeKeys[k - 1], (NodeInfo)nodeKeys[l - 1]);
            }

            public void swap(int i, int j)
            {
/* 194*/        int k = i * recordSize;
/* 195*/        int l = j * recordSize;
/* 196*/        for(int i1 = 0; i1 < recordSize; i1++)
                {
/* 197*/            Object obj = nodeKeys[k + i1];
/* 198*/            nodeKeys[k + i1] = nodeKeys[l + i1];
/* 199*/            nodeKeys[l + i1] = obj;
                }

            }
}
