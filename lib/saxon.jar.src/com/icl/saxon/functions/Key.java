// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Key.java

package com.icl.saxon.functions;

import com.icl.saxon.*;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.*;
import com.icl.saxon.sort.LocalOrderComparer;

public class Key extends Function
{

            private DocumentInfo boundDocument;
            private Controller boundController;

            public Key()
            {
/*  22*/        boundDocument = null;
/*  23*/        boundController = null;
            }

            public String getName()
            {
/*  26*/        return "key";
            }

            public int getDataType()
            {
/*  35*/        return 4;
            }

            public boolean isContextDocumentNodeSet()
            {
/*  45*/        return true;
            }

            public Expression simplify()
                throws XPathException
            {
/*  54*/        if(!getStaticContext().allowsKeyFunction())
                {
/*  55*/            throw new XPathException("key() function cannot be used here");
                } else
                {
/*  57*/            checkArgumentCount(2, 2);
/*  58*/            super.argument[0] = super.argument[0].simplify();
/*  59*/            super.argument[1] = super.argument[1].simplify();
/*  60*/            return this;
                }
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  68*/        NodeSetExtent nodesetextent = new NodeSetExtent(enumerate(context, true), LocalOrderComparer.getInstance());
/*  70*/        nodesetextent.setSorted(true);
/*  71*/        return nodesetextent;
            }

            public NodeEnumeration enumerate(Context context, boolean flag)
                throws XPathException
            {
/*  79*/        String s = super.argument[0].evaluateAsString(context);
/*  80*/        Value value = super.argument[1].evaluate(context);
/*  82*/        Controller controller = boundController;
/*  83*/        if(controller == null)
/*  83*/            controller = context.getController();
/*  85*/        DocumentInfo documentinfo = boundDocument;
/*  86*/        if(documentinfo == null)
/*  86*/            documentinfo = context.getContextNodeInfo().getDocumentRoot();
/*  88*/        int i = getStaticContext().getFingerprint(s, false);
/*  89*/        if(i == -1)
/*  90*/            throw new XPathException("Key '" + s + "' has not been defined");
/*  92*/        else
/*  92*/            return findKey(controller, documentinfo, i, value);
            }

            private NodeEnumeration findKey(Controller controller, DocumentInfo documentinfo, int i, Value value)
                throws XPathException
            {
/* 109*/        KeyManager keymanager = controller.getKeyManager();
/* 113*/        if((value instanceof NodeSetValue) && !(value instanceof FragmentValue) && !(value instanceof TextFragmentValue))
                {
/* 116*/            NodeSetValue nodesetvalue = (NodeSetValue)value;
/* 118*/            NodeEnumeration nodeenumeration = nodesetvalue.enumerate();
/* 119*/            Object obj = null;
/* 121*/            int j = 0;
/* 123*/            while(nodeenumeration.hasMoreElements()) 
                    {
/* 123*/                j++;
/* 124*/                NodeEnumeration nodeenumeration1 = keymanager.selectByKey(i, documentinfo, nodeenumeration.nextElement().getStringValue(), controller);
/* 129*/                if(j == 1)
/* 130*/                    obj = nodeenumeration1;
/* 132*/                else
/* 132*/                    obj = new UnionEnumeration(((NodeEnumeration) (obj)), nodeenumeration1, controller);
                    }
/* 136*/            if(j == 0)
/* 137*/                return EmptyEnumeration.getInstance();
/* 139*/            else
/* 139*/                return ((NodeEnumeration) (obj));
                } else
                {
/* 143*/            return keymanager.selectByKey(i, documentinfo, value.asString(), controller);
                }
            }

            public int getDependencies()
            {
/* 154*/        int i = super.argument[0].getDependencies();
/* 155*/        i |= super.argument[1].getDependencies();
/* 156*/        if(boundDocument == null)
/* 157*/            i |= 0x88;
/* 159*/        if(boundController == null)
/* 160*/            i |= 0x40;
/* 162*/        return i;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/* 171*/        Key key = new Key();
/* 172*/        key.addArgument(super.argument[0].reduce(i, context));
/* 173*/        key.addArgument(super.argument[1].reduce(i, context));
/* 174*/        if(boundDocument == null && (i & 0x88) != 0)
/* 176*/            key.boundDocument = context.getContextNodeInfo().getDocumentRoot();
/* 178*/        else
/* 178*/            key.boundDocument = boundDocument;
/* 180*/        if(boundController == null && (i & 0x40) != 0)
/* 181*/            key.boundController = context.getController();
/* 183*/        else
/* 183*/            key.boundController = boundController;
/* 185*/        key.setStaticContext(getStaticContext());
/* 186*/        return key.simplify();
            }
}
