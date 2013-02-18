// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NamespaceEmitter.java

package com.icl.saxon.output;

import com.icl.saxon.om.NamePool;
import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

// Referenced classes of package com.icl.saxon.output:
//            ProxyEmitter

public class NamespaceEmitter extends ProxyEmitter
{

            protected NamePool namePool;
            protected int nscodeXML;
            protected int nscodeNull;
            private int namespaces[];
            private int namespacesSize;
            private int namespaceStack[];
            private int nsStackTop;

            public NamespaceEmitter()
            {
/*  29*/        namespaces = new int[30];
/*  30*/        namespacesSize = 0;
/*  31*/        namespaceStack = new int[100];
/*  32*/        nsStackTop = 0;
            }

            public void setNamePool(NamePool namepool)
            {
/*  41*/        namePool = namepool;
/*  42*/        nscodeXML = namepool.getNamespaceCode("xml", "http://www.w3.org/XML/1998/namespace");
/*  43*/        nscodeNull = namepool.getNamespaceCode("", "");
/*  44*/        super.setNamePool(namepool);
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/*  63*/        int ai1[] = new int[ai.length + 1];
/*  64*/        int k = 0;
/*  68*/        int l = namePool.allocateNamespaceCode(i);
/*  69*/        if(isNeeded(l))
                {
/*  70*/            addToStack(l);
/*  71*/            ai1[k++] = l;
                }
/*  76*/        for(int i1 = 0; i1 < j; i1++)
                {
/*  77*/            int j1 = ai[i1];
/*  78*/            if(isNeeded(j1))
                    {
/*  79*/                addToStack(j1);
/*  80*/                ai1[k++] = j1;
                    }
                }

/*  86*/        if(nsStackTop >= namespaceStack.length)
                {
/*  87*/            int ai2[] = new int[nsStackTop * 2];
/*  88*/            System.arraycopy(namespaceStack, 0, ai2, 0, nsStackTop);
/*  89*/            namespaceStack = ai2;
                }
/*  92*/        namespaceStack[nsStackTop++] = k;
/*  95*/        super.startElement(i, attributes, ai1, k);
            }

            private boolean isNeeded(int i)
            {
/* 103*/        if(i == nscodeXML)
/* 105*/            return false;
/* 108*/        for(int j = namespacesSize - 1; j >= 0; j--)
                {
/* 109*/            if(namespaces[j] == i)
/* 111*/                return false;
/* 113*/            if(namespaces[j] >> 16 == i >> 16)
/* 115*/                return true;
                }

/* 118*/        return i != nscodeNull;
            }

            private void addToStack(int i)
            {
/* 127*/        if(namespacesSize + 1 >= namespaces.length)
                {
/* 128*/            int ai[] = new int[namespacesSize * 2];
/* 129*/            System.arraycopy(namespaces, 0, ai, 0, namespacesSize);
/* 130*/            namespaces = ai;
                }
/* 132*/        namespaces[namespacesSize++] = i;
            }

            public void endElement(int i)
                throws TransformerException
            {
/* 143*/        if(nsStackTop-- == 0)
                {
/* 144*/            throw new TransformerException("Attempt to output end tag with no matching start tag");
                } else
                {
/* 147*/            int j = namespaceStack[nsStackTop];
/* 148*/            namespacesSize -= j;
/* 150*/            super.endElement(i);
/* 152*/            return;
                }
            }
}
