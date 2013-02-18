// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   UnparsedEntityURI.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.DocumentInfo;
import com.icl.saxon.om.NodeInfo;

public class UnparsedEntityURI extends Function
{

            DocumentInfo boundDocument;

            public UnparsedEntityURI()
            {
/*  13*/        boundDocument = null;
            }

            public String getName()
            {
/*  16*/        return "unparsed-entity-uri";
            }

            public int getDataType()
            {
/*  25*/        return 3;
            }

            public Expression simplify()
                throws XPathException
            {
/*  33*/        checkArgumentCount(1, 1);
/*  34*/        return this;
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/*  42*/        String s = super.argument[0].evaluateAsString(context);
/*  43*/        DocumentInfo documentinfo = boundDocument;
/*  44*/        if(documentinfo == null)
/*  44*/            documentinfo = context.getContextNodeInfo().getDocumentRoot();
/*  45*/        return documentinfo.getUnparsedEntity(s);
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  53*/        return new StringValue(evaluateAsString(context));
            }

            public int getDependencies()
            {
/*  63*/        int i = super.argument[0].getDependencies();
/*  64*/        if(boundDocument == null)
/*  65*/            i |= 8;
/*  67*/        return i;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  75*/        UnparsedEntityURI unparsedentityuri = new UnparsedEntityURI();
/*  76*/        unparsedentityuri.addArgument(super.argument[0].reduce(i, context));
/*  77*/        unparsedentityuri.setStaticContext(getStaticContext());
/*  79*/        if(boundDocument == null && (i & 8) != 0)
/*  80*/            unparsedentityuri.boundDocument = context.getContextNodeInfo().getDocumentRoot();
/*  82*/        else
/*  82*/            unparsedentityuri.boundDocument = boundDocument;
/*  84*/        return unparsedentityuri;
            }
}
