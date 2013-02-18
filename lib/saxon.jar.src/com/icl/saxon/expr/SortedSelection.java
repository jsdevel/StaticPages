// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SortedSelection.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.sort.SortKeyDefinition;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            NodeSetExpression, XPathException, SortKeyEnumeration, Expression

public class SortedSelection extends NodeSetExpression
{

            private Expression selection;
            private SortKeyDefinition sortkeys[];
            private int numberOfSortKeys;

            public SortedSelection(Expression expression, int i)
            {
/*  29*/        selection = expression;
/*  30*/        sortkeys = new SortKeyDefinition[i];
/*  31*/        numberOfSortKeys = i;
            }

            public void setSortKey(SortKeyDefinition sortkeydefinition, int i)
            {
/*  45*/        sortkeys[i] = sortkeydefinition;
            }

            public Expression simplify()
                throws XPathException
            {
/*  56*/        selection = selection.simplify();
/*  57*/        for(int i = 0; i < numberOfSortKeys; i++)
                {
/*  58*/            SortKeyDefinition sortkeydefinition = sortkeys[i];
/*  59*/            sortkeydefinition.setSortKey(sortkeydefinition.getSortKey().simplify());
/*  60*/            sortkeydefinition.setOrder(sortkeydefinition.getOrder().simplify());
/*  61*/            sortkeydefinition.setDataType(sortkeydefinition.getDataType().simplify());
/*  62*/            sortkeydefinition.setCaseOrder(sortkeydefinition.getCaseOrder().simplify());
/*  63*/            sortkeydefinition.setLanguage(sortkeydefinition.getLanguage().simplify());
                }

/*  65*/        return this;
            }

            public int getDependencies()
            {
/*  75*/        int i = selection.getDependencies();
/*  76*/        for(int j = 0; j < sortkeys.length; j++)
                {
/*  77*/            SortKeyDefinition sortkeydefinition = sortkeys[j];
/*  80*/            i |= sortkeydefinition.getSortKey().getDependencies() & 0x41;
/*  82*/            i |= sortkeydefinition.getOrder().getDependencies();
/*  83*/            i |= sortkeydefinition.getDataType().getDependencies();
/*  84*/            i |= sortkeydefinition.getCaseOrder().getDependencies();
/*  85*/            i |= sortkeydefinition.getLanguage().getDependencies();
                }

/*  87*/        return i;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/* 100*/        if((i & getDependencies()) != 0)
                {
/* 101*/            Expression expression = selection.reduce(i, context);
/* 102*/            SortedSelection sortedselection = new SortedSelection(expression, numberOfSortKeys);
/* 103*/            sortedselection.setStaticContext(getStaticContext());
/* 104*/            for(int j = 0; j < numberOfSortKeys; j++)
                    {
/* 105*/                SortKeyDefinition sortkeydefinition = sortkeys[j];
/* 106*/                SortKeyDefinition sortkeydefinition1 = new SortKeyDefinition();
/* 107*/                sortkeydefinition1.setStaticContext(getStaticContext());
/* 108*/                sortkeydefinition1.setSortKey(sortkeydefinition.getSortKey().reduce(i & 0x41, context));
/* 112*/                sortkeydefinition1.setOrder(sortkeydefinition.getOrder().reduce(i, context));
/* 113*/                sortkeydefinition1.setDataType(sortkeydefinition.getDataType().reduce(i, context));
/* 114*/                sortkeydefinition1.setCaseOrder(sortkeydefinition.getCaseOrder().reduce(i, context));
/* 115*/                sortkeydefinition1.setLanguage(sortkeydefinition.getLanguage().reduce(i, context));
/* 116*/                sortedselection.setSortKey(sortkeydefinition1, j);
                    }

/* 118*/            return sortedselection.simplify();
                } else
                {
/* 120*/            return this;
                }
            }

            public NodeEnumeration enumerate(Context context, boolean flag)
                throws XPathException
            {
/* 133*/        if(flag)
                {
/* 134*/            throw new XPathException("SortedSelection doesn't provide nodes in document order");
                } else
                {
/* 136*/            NodeEnumeration nodeenumeration = selection.enumerate(context, true);
/* 137*/            return new SortKeyEnumeration(context, nodeenumeration, sortkeys);
                }
            }

            public void display(int i)
            {
/* 145*/        System.err.println(Expression.indent(i) + "sorted");
/* 146*/        selection.display(i + 1);
            }
}
