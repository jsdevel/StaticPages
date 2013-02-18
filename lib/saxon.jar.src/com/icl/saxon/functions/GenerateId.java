// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   GenerateId.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.*;

public class GenerateId extends Function
{

            public GenerateId()
            {
            }

            public String getName()
            {
/*  15*/        return "generate-id";
            }

            public int getDataType()
            {
/*  24*/        return 3;
            }

            public Expression simplify()
                throws XPathException
            {
/*  32*/        checkArgumentCount(0, 1);
/*  33*/        return this;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  41*/        int i = getNumberOfArguments();
/*  43*/        if(i == 0)
                {
/*  44*/            NodeInfo nodeinfo = context.getContextNodeInfo();
/*  45*/            String s = nodeinfo.generateId();
/*  46*/            return "d" + context.getController().getDocumentPool().getDocumentNumber(nodeinfo.getDocumentRoot()) + s;
                }
/*  52*/        NodeEnumeration nodeenumeration = super.argument[0].enumerate(context, true);
/*  53*/        if(nodeenumeration.hasMoreElements())
                {
/*  54*/            NodeInfo nodeinfo1 = nodeenumeration.nextElement();
/*  55*/            String s1 = nodeinfo1.generateId();
/*  56*/            return "d" + context.getController().getDocumentPool().getDocumentNumber(nodeinfo1.getDocumentRoot()) + s1;
                } else
                {
/*  61*/            return "";
                }
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  70*/        return new StringValue(evaluateAsString(context));
            }

            public int getDependencies()
            {
/*  78*/        if(getNumberOfArguments() == 0)
/*  79*/            return 8;
/*  81*/        else
/*  81*/            return super.argument[0].getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  90*/        GenerateId generateid = new GenerateId();
/*  91*/        if(getNumberOfArguments() == 1)
                {
/*  92*/            generateid.addArgument(super.argument[0].reduce(i, context));
/*  93*/            generateid.setStaticContext(getStaticContext());
/*  94*/            return generateid;
                }
/*  96*/        if((i & 8) != 0)
/*  97*/            return evaluate(context);
/*  99*/        else
/*  99*/            return this;
            }
}
