// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Id.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.*;
import com.icl.saxon.sort.LocalOrderComparer;
import java.util.StringTokenizer;
import java.util.Vector;

public class Id extends Function
{

            private DocumentInfo boundDocument;

            public Id()
            {
/*  16*/        boundDocument = null;
            }

            public String getName()
            {
/*  19*/        return "id";
            }

            public int getDataType()
            {
/*  28*/        return 4;
            }

            public boolean isContextDocumentNodeSet()
            {
/*  38*/        return true;
            }

            public Expression simplify()
                throws XPathException
            {
/*  46*/        checkArgumentCount(1, 1);
/*  47*/        super.argument[0] = super.argument[0].simplify();
/*  48*/        return this;
            }

            public NodeSetValue evaluateAsNodeSet(Context context)
                throws XPathException
            {
/*  56*/        return findId(super.argument[0].evaluate(context), context);
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  64*/        return evaluateAsNodeSet(context);
            }

            public int getDependencies()
            {
/*  74*/        int i = super.argument[0].getDependencies();
/*  75*/        if(boundDocument != null)
/*  76*/            return i;
/*  78*/        else
/*  78*/            return i | 8 | 0x80;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  89*/        Id id = new Id();
/*  90*/        id.addArgument(super.argument[0].reduce(i, context));
/*  91*/        id.setStaticContext(getStaticContext());
/*  92*/        id.boundDocument = boundDocument;
/*  94*/        if(boundDocument == null && (i & 0x88) != 0)
/*  96*/            id.boundDocument = context.getContextNodeInfo().getDocumentRoot();
/*  98*/        return id;
            }

            private NodeSetValue findId(Value value, Context context)
                throws XPathException
            {
/* 106*/        Vector vector = null;
                DocumentInfo documentinfo;
/* 108*/        if(boundDocument == null)
/* 109*/            documentinfo = context.getContextNodeInfo().getDocumentRoot();
/* 111*/        else
/* 111*/            documentinfo = boundDocument;
/* 114*/        if((value instanceof NodeSetValue) && !(value instanceof FragmentValue) && !(value instanceof FragmentValue))
                {
/* 117*/            for(NodeEnumeration nodeenumeration = ((NodeSetValue)value).enumerate(); nodeenumeration.hasMoreElements();)
                    {
/* 119*/                NodeInfo nodeinfo = nodeenumeration.nextElement();
/* 120*/                String s1 = nodeinfo.getStringValue();
/* 121*/                for(StringTokenizer stringtokenizer1 = new StringTokenizer(s1); stringtokenizer1.hasMoreTokens();)
                        {
/* 123*/                    NodeInfo nodeinfo2 = documentinfo.selectID(stringtokenizer1.nextToken());
/* 124*/                    if(nodeinfo2 != null)
                            {
/* 125*/                        if(vector == null)
/* 126*/                            vector = new Vector(2);
/* 128*/                        vector.addElement(nodeinfo2);
                            }
                        }

                    }

                } else
                {
/* 135*/            String s = value.asString();
/* 136*/            for(StringTokenizer stringtokenizer = new StringTokenizer(s); stringtokenizer.hasMoreTokens();)
                    {
/* 138*/                NodeInfo nodeinfo1 = documentinfo.selectID(stringtokenizer.nextToken());
/* 139*/                if(nodeinfo1 != null)
                        {
/* 140*/                    if(vector == null)
/* 141*/                        vector = new Vector(2);
/* 143*/                    vector.addElement(nodeinfo1);
                        }
                    }

                }
/* 148*/        if(vector == null)
/* 149*/            return new EmptyNodeSet();
/* 151*/        if(vector.size() == 1)
/* 152*/            return new SingletonNodeSet((NodeInfo)vector.elementAt(0));
/* 154*/        else
/* 154*/            return new NodeSetExtent(vector, LocalOrderComparer.getInstance());
            }
}
