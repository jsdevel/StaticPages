// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DefaultHandler.java

package com.icl.saxon.aelfred;

import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

public class DefaultHandler extends org.xml.sax.helpers.DefaultHandler
    implements LexicalHandler, DeclHandler
{

            public DefaultHandler()
            {
            }

            public void startCDATA()
                throws SAXException
            {
            }

            public void endCDATA()
                throws SAXException
            {
            }

            public void startDTD(String s, String s1, String s2)
                throws SAXException
            {
            }

            public void endDTD()
                throws SAXException
            {
            }

            public void startEntity(String s)
                throws SAXException
            {
            }

            public void endEntity(String s)
                throws SAXException
            {
            }

            public void comment(char ac[], int i, int j)
                throws SAXException
            {
            }

            public void attributeDecl(String s, String s1, String s2, String s3, String s4)
                throws SAXException
            {
            }

            public void elementDecl(String s, String s1)
                throws SAXException
            {
            }

            public void externalEntityDecl(String s, String s1, String s2)
                throws SAXException
            {
            }

            public void internalEntityDecl(String s, String s1)
                throws SAXException
            {
            }
}
