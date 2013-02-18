// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   HTMLIndenter.java

package com.icl.saxon.output;

import com.icl.saxon.om.NamePool;
import com.icl.saxon.sort.HashMap;
import java.util.Properties;
import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

// Referenced classes of package com.icl.saxon.output:
//            ProxyEmitter, Emitter

public class HTMLIndenter extends ProxyEmitter
{

            private int level;
            private int indentSpaces;
            private String indentChars;
            private boolean sameLine;
            private boolean isInlineTag;
            private boolean inFormattedTag;
            private boolean afterInline;
            private boolean afterFormatted;
            private static String inlineTags[] = {
/*  34*/        "tt", "i", "b", "u", "s", "strike", "big", "small", "em", "strong", 
/*  34*/        "dfn", "code", "samp", "kbd", "var", "cite", "abbr", "acronym", "a", "img", 
/*  34*/        "applet", "object", "font", "basefont", "br", "script", "map", "q", "sub", "sup", 
/*  34*/        "span", "bdo", "iframe", "input", "select", "textarea", "label", "button"
            };
            private static HashMap inlineTable;
            private static HashMap formattedTable;

            private static boolean isInline(String s)
            {
/*  49*/        return inlineTable.get(s);
            }

            private static boolean isFormatted(String s)
            {
/*  65*/        return formattedTable.get(s);
            }

            public HTMLIndenter()
            {
/*  21*/        level = 0;
/*  22*/        indentSpaces = 3;
/*  23*/        indentChars = "                                                          ";
/*  24*/        sameLine = false;
/*  25*/        isInlineTag = false;
/*  26*/        inFormattedTag = false;
/*  27*/        afterInline = false;
/*  28*/        afterFormatted = true;
            }

            public void startDocument()
                throws TransformerException
            {
/*  78*/        super.startDocument();
/*  79*/        String s = super.outputProperties.getProperty("{http://icl.com/saxon}indent-spaces");
/*  80*/        if(s == null)
/*  81*/            indentSpaces = 3;
/*  84*/        else
/*  84*/            try
                    {
/*  84*/                indentSpaces = Integer.parseInt(s);
                    }
/*  86*/            catch(Exception exception)
                    {
/*  86*/                indentSpaces = 3;
                    }
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/*  97*/        String s = super.namePool.getDisplayName(i);
/*  98*/        isInlineTag = isInline(s);
/*  99*/        inFormattedTag = inFormattedTag || isFormatted(s);
/* 100*/        if(!isInlineTag && !inFormattedTag && !afterInline && !afterFormatted)
/* 102*/            indent();
/* 106*/        super.startElement(i, attributes, ai, j);
/* 107*/        level++;
/* 108*/        sameLine = true;
/* 109*/        afterInline = false;
/* 110*/        afterFormatted = false;
            }

            public void endElement(int i)
                throws TransformerException
            {
/* 118*/        level--;
/* 119*/        String s = super.namePool.getDisplayName(i);
/* 120*/        boolean flag = isInline(s);
/* 121*/        boolean flag1 = isFormatted(s);
/* 122*/        if(!flag && !flag1 && !afterInline && !sameLine && !afterFormatted && !inFormattedTag)
                {
/* 124*/            indent();
/* 125*/            afterInline = false;
/* 126*/            afterFormatted = false;
                } else
                {
/* 128*/            afterInline = flag;
/* 129*/            afterFormatted = flag1;
                }
/* 131*/        super.endElement(i);
/* 132*/        inFormattedTag = inFormattedTag && !flag1;
/* 133*/        sameLine = false;
            }

            public void processingInstruction(String s, String s1)
                throws TransformerException
            {
/* 141*/        super.processingInstruction(s, s1);
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/* 149*/        if(inFormattedTag)
                {
/* 150*/            super.characters(ac, i, j);
                } else
                {
/* 152*/            int k = i;
/* 154*/            for(int l = i; l < i + j; l++)
/* 155*/                if(ac[l] == '\n' || l - k > 120 && ac[l] == ' ')
                        {
/* 156*/                    sameLine = false;
/* 157*/                    super.characters(ac, k, l - k);
/* 158*/                    indent();
/* 159*/                    for(k = l + 1; k < j && ac[k] == ' '; k++);
                        }

/* 163*/            if(k < i + j)
/* 164*/                super.characters(ac, k, (i + j) - k);
                }
/* 167*/        afterInline = false;
            }

            public void ignorableWhitespace(char ac[], int i, int j)
                throws TransformerException
            {
            }

            public void comment(char ac[], int i, int j)
                throws TransformerException
            {
/* 183*/        indent();
/* 184*/        super.comment(ac, i, j);
            }

            public void endDocument()
                throws TransformerException
            {
/* 192*/        super.endDocument();
            }

            private void indent()
                throws TransformerException
            {
                int i;
/* 200*/        for(i = level * indentSpaces; i > indentChars.length(); indentChars += indentChars);
/* 204*/        char ac[] = new char[i + 1];
/* 205*/        ac[0] = '\n';
/* 206*/        indentChars.getChars(0, i, ac, 1);
/* 207*/        super.characters(ac, 0, i + 1);
/* 208*/        sameLine = false;
            }

            static 
            {
/*  40*/        inlineTable = new HashMap(203);
/*  43*/        for(int i = 0; i < inlineTags.length; i++)
/*  44*/            inlineTable.set(inlineTags[i]);

/*  54*/        formattedTable = new HashMap(51);
/*  57*/        formattedTable.set("pre");
/*  58*/        formattedTable.set("script");
/*  59*/        formattedTable.set("style");
/*  60*/        formattedTable.set("textarea");
/*  61*/        formattedTable.set("xmp");
            }
}
