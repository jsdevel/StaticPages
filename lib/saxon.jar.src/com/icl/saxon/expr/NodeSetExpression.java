// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NodeSetExpression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.output.Outputter;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.expr:
//            Expression, NodeSetValue, Value, NodeSetIntent, 
//            XPathException

public abstract class NodeSetExpression extends Expression
{

            public NodeSetExpression()
            {
            }

            public abstract NodeEnumeration enumerate(Context context, boolean flag)
                throws XPathException;

            public Value evaluate(Context context)
                throws XPathException
            {
/*  39*/        Expression expression = reduce(255, context);
/*  40*/        if(expression instanceof NodeSetValue)
/*  41*/            return (Value)expression;
/*  42*/        if(expression instanceof NodeSetExpression)
/*  43*/            return new NodeSetIntent((NodeSetExpression)expression, context.getController());
/*  45*/        Value value = expression.evaluate(context);
/*  46*/        if(value instanceof NodeSetValue)
/*  47*/            return value;
/*  49*/        else
/*  49*/            throw new XPathException("Value must be a node-set: it is a " + expression.getClass());
            }

            public NodeInfo selectFirst(Context context)
                throws XPathException
            {
/*  63*/        NodeEnumeration nodeenumeration = enumerate(context, false);
/*  64*/        if(nodeenumeration.isSorted())
/*  65*/            if(nodeenumeration.hasMoreElements())
/*  66*/                return nodeenumeration.nextElement();
/*  68*/            else
/*  68*/                return null;
/*  73*/        Controller controller = context.getController();
/*  74*/        NodeInfo nodeinfo = null;
/*  76*/        while(nodeenumeration.hasMoreElements()) 
                {
/*  76*/            NodeInfo nodeinfo1 = nodeenumeration.nextElement();
/*  77*/            if(nodeinfo == null || controller.compare(nodeinfo1, nodeinfo) < 0)
/*  78*/                nodeinfo = nodeinfo1;
                }
/*  81*/        return nodeinfo;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  93*/        NodeInfo nodeinfo = selectFirst(context);
/*  94*/        if(nodeinfo == null)
/*  94*/            return "";
/*  95*/        else
/*  95*/            return nodeinfo.getStringValue();
            }

            public void outputStringValue(Outputter outputter, Context context)
                throws TransformerException
            {
/* 106*/        NodeInfo nodeinfo = selectFirst(context);
/* 107*/        if(nodeinfo != null)
/* 108*/            nodeinfo.copyStringValue(outputter);
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/* 120*/        return enumerate(context, false).hasMoreElements();
            }

            public NodeSetValue evaluateAsNodeSet(Context context)
                throws XPathException
            {
/* 130*/        return (NodeSetValue)evaluate(context);
            }

            public int getDataType()
            {
/* 139*/        return 4;
            }
}
