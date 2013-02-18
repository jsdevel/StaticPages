// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   IDFilter.java

package com.icl.saxon;

import java.util.Stack;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

public class IDFilter extends XMLFilterImpl
{

            private String id;
            private int activeDepth;
            private Stack namespacePrefixes;
            private Stack namespaceURIs;

            public IDFilter(String s)
            {
/*  19*/        activeDepth = 0;
/*  20*/        namespacePrefixes = new Stack();
/*  21*/        namespaceURIs = new Stack();
/*  25*/        id = s;
            }

            public void startPrefixMapping(String s, String s1)
                throws SAXException
            {
/*  31*/        if(activeDepth > 0)
                {
/*  32*/            super.startPrefixMapping(s, s1);
                } else
                {
/*  34*/            namespacePrefixes.push(s);
/*  35*/            namespaceURIs.push(s1);
                }
            }

            public void endPrefixMapping(String s)
                throws SAXException
            {
/*  42*/        if(activeDepth > 0)
                {
/*  43*/            super.endPrefixMapping(s);
                } else
                {
/*  45*/            namespacePrefixes.pop();
/*  46*/            namespaceURIs.pop();
                }
            }

            public void startElement(String s, String s1, String s2, Attributes attributes)
                throws SAXException
            {
/*  54*/        if(activeDepth == 0)
                {
/*  55*/            for(int i = 0; i < attributes.getLength(); i++)
                    {
/*  56*/                if(!attributes.getType(i).equals("ID") || !attributes.getValue(i).equals(id))
/*  57*/                    continue;
/*  57*/                activeDepth = 1;
/*  58*/                break;
                    }

/*  61*/            if(activeDepth == 1)
                    {
/*  62*/                for(int j = 0; j < namespacePrefixes.size(); j++)
/*  63*/                    super.startPrefixMapping((String)namespacePrefixes.elementAt(j), (String)namespaceURIs.elementAt(j));

                    }
                } else
                {
/*  69*/            activeDepth++;
                }
/*  71*/        if(activeDepth > 0)
/*  72*/            super.startElement(s, s1, s2, attributes);
            }

            public void endElement(String s, String s1, String s2)
                throws SAXException
            {
/*  79*/        if(activeDepth > 0)
                {
/*  80*/            super.endElement(s, s1, s2);
/*  81*/            activeDepth--;
/*  82*/            if(activeDepth == 0)
                    {
/*  83*/                for(int i = namespacePrefixes.size() - 1; i >= 0; i--)
/*  84*/                    super.endPrefixMapping((String)namespacePrefixes.elementAt(i));

                    }
                }
            }

            public void characters(char ac[], int i, int j)
                throws SAXException
            {
/*  93*/        if(activeDepth > 0)
/*  94*/            super.characters(ac, i, j);
            }

            public void ignorableWhitespace(char ac[], int i, int j)
                throws SAXException
            {
/* 100*/        if(activeDepth > 0)
/* 101*/            super.ignorableWhitespace(ac, i, j);
            }

            public void processingInstruction(String s, String s1)
                throws SAXException
            {
/* 107*/        if(activeDepth > 0)
/* 108*/            super.processingInstruction(s, s1);
            }
}
