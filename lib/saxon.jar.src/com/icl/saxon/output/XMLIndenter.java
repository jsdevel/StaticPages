// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XMLIndenter.java

package com.icl.saxon.output;

import java.util.Properties;
import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

// Referenced classes of package com.icl.saxon.output:
//            ProxyEmitter

public class XMLIndenter extends ProxyEmitter
{

            private int level;
            private int indentSpaces;
            private String indentChars;
            private boolean sameline;
            private boolean afterTag;
            private boolean allWhite;
            private int suppressedAtLevel;

            public XMLIndenter()
            {
/*  21*/        level = 0;
/*  22*/        indentSpaces = 3;
/*  23*/        indentChars = "                                                          ";
/*  24*/        sameline = false;
/*  25*/        afterTag = true;
/*  26*/        allWhite = true;
/*  27*/        suppressedAtLevel = -1;
            }

            public void startDocument()
                throws TransformerException
            {
/*  35*/        super.startDocument();
/*  37*/        String s = super.outputProperties.getProperty("{http://icl.com/saxon}indent-spaces");
/*  38*/        if(s != null)
/*  40*/            try
                    {
/*  40*/                indentSpaces = Integer.parseInt(s);
                    }
/*  42*/            catch(Exception exception)
                    {
/*  42*/                indentSpaces = 3;
                    }
/*  46*/        String s1 = super.outputProperties.getProperty("omit-xml-declaration");
/*  47*/        afterTag = s1 == null || !s1.equals("yes") || super.outputProperties.getProperty("doctype-system") != null;
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/*  57*/        if(afterTag)
/*  58*/            indent();
/*  60*/        super.startElement(i, attributes, ai, j);
/*  61*/        if("preserve".equals(attributes.getValue("http://www.w3.org/XML/1998/namespace", "space")) && suppressedAtLevel < 0)
/*  62*/            suppressedAtLevel = level;
/*  64*/        level++;
/*  65*/        sameline = true;
/*  66*/        afterTag = true;
/*  67*/        allWhite = true;
            }

            public void endElement(int i)
                throws TransformerException
            {
/*  75*/        level--;
/*  76*/        if(afterTag && !sameline)
/*  76*/            indent();
/*  77*/        super.endElement(i);
/*  78*/        sameline = false;
/*  79*/        afterTag = true;
/*  80*/        allWhite = true;
/*  81*/        if(level == suppressedAtLevel - 1)
/*  82*/            suppressedAtLevel = -1;
            }

            public void processingInstruction(String s, String s1)
                throws TransformerException
            {
/*  92*/        super.processingInstruction(s, s1);
/*  93*/        afterTag = true;
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/* 101*/        for(int k = i; k < i + j; k++)
                {
/* 102*/            if(ac[k] == '\n')
/* 103*/                sameline = false;
/* 105*/            if(!Character.isWhitespace(ac[k]))
/* 106*/                allWhite = false;
                }

/* 109*/        super.characters(ac, i, j);
/* 110*/        if(!allWhite)
/* 111*/            afterTag = false;
            }

            public void ignorableWhitespace(char ac[], int i, int j)
                throws TransformerException
            {
            }

            public void comment(char ac[], int i, int j)
                throws TransformerException
            {
/* 128*/        super.comment(ac, i, j);
/* 129*/        afterTag = true;
            }

            public void endDocument()
                throws TransformerException
            {
/* 137*/        super.endDocument();
            }

            private void indent()
                throws TransformerException
            {
/* 145*/        if(suppressedAtLevel >= 0)
/* 147*/            return;
                int i;
/* 149*/        for(i = level * indentSpaces; i > indentChars.length(); indentChars += indentChars);
/* 153*/        char ac[] = new char[i + 1];
/* 154*/        ac[0] = '\n';
/* 155*/        indentChars.getChars(0, i, ac, 1);
/* 156*/        super.characters(ac, 0, i + 1);
/* 157*/        sameline = false;
            }
}
