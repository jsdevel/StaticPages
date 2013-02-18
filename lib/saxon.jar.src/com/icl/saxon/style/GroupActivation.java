// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   GroupActivation.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;

// Referenced classes of package com.icl.saxon.style:
//            SAXONGroup

public class GroupActivation
    implements NodeEnumeration
{

            private SAXONGroup group;
            private NodeEnumeration nodes;
            private Expression groupkey;
            private Context context;
            private NodeInfo next;
            private Value nextValue;
            private NodeInfo current;
            private Value currentValue;
            private int position;

            public GroupActivation(SAXONGroup saxongroup, Expression expression, NodeEnumeration nodeenumeration, Context context1)
                throws XPathException
            {
/*  21*/        next = null;
/*  22*/        nextValue = null;
/*  23*/        current = null;
/*  24*/        currentValue = null;
/*  25*/        position = 0;
/*  30*/        group = saxongroup;
/*  31*/        groupkey = expression;
/*  32*/        nodes = nodeenumeration;
/*  33*/        context = context1;
/*  34*/        position = 0;
/*  35*/        current = null;
/*  36*/        currentValue = null;
/*  37*/        lookAhead();
            }

            private void lookAhead()
                throws XPathException
            {
/*  41*/        if(nodes.hasMoreElements())
                {
/*  42*/            next = nodes.nextElement();
/*  43*/            context.setCurrentNode(next);
/*  44*/            context.setContextNode(next);
/*  45*/            context.setPosition(position + 1);
/*  46*/            nextValue = groupkey.evaluate(context);
                } else
                {
/*  48*/            next = null;
                }
            }

            public boolean hasMoreElements()
            {
/*  53*/        return next != null;
            }

            public NodeInfo nextElement()
                throws XPathException
            {
/*  57*/        current = next;
/*  58*/        currentValue = nextValue;
/*  59*/        position++;
/*  60*/        lookAhead();
/*  61*/        context.setCurrentNode(current);
/*  62*/        context.setContextNode(current);
/*  63*/        context.setPosition(position);
/*  64*/        return current;
            }

            public boolean sameAsNext()
                throws XPathException
            {
/*  68*/        if(next == null)
/*  68*/            return false;
/*  69*/        else
/*  69*/            return currentValue.equals(nextValue);
            }

            public boolean isSorted()
            {
/*  73*/        return nodes.isSorted();
            }

            public boolean isReverseSorted()
            {
/*  77*/        return nodes.isReverseSorted();
            }

            public boolean isPeer()
            {
/*  81*/        return nodes.isPeer();
            }
}
