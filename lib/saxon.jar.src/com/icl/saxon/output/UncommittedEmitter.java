// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   UncommittedEmitter.java

package com.icl.saxon.output;

import com.icl.saxon.om.NamePool;
import java.util.Properties;
import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

// Referenced classes of package com.icl.saxon.output:
//            ProxyEmitter, XMLEmitter, XMLIndenter, CDATAFilter, 
//            HTMLEmitter, HTMLIndenter, Emitter

public class UncommittedEmitter extends ProxyEmitter
{

            boolean committed;
            boolean initialNewline;
            boolean initialEscaping;
            StringBuffer pendingCharacters;

            public UncommittedEmitter()
            {
/*  18*/        committed = false;
/*  19*/        initialNewline = false;
/*  20*/        initialEscaping = true;
            }

            public void startDocument()
                throws TransformerException
            {
/*  24*/        committed = false;
            }

            public void endDocument()
                throws TransformerException
            {
/*  33*/        if(!committed)
/*  34*/            switchToXML();
/*  36*/        super.endDocument();
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/*  46*/        if(!committed)
                {
/*  47*/            boolean flag = true;
/*  48*/            if(pendingCharacters == null)
/*  49*/                pendingCharacters = new StringBuffer();
/*  51*/            for(int k = i; k < i + j; k++)
                    {
/*  52*/                char c = ac[k];
/*  53*/                if(!Character.isWhitespace(c))
/*  54*/                    flag = false;
/*  56*/                if(initialEscaping)
                        {
/*  57*/                    if(c == '<')
/*  58*/                        pendingCharacters.append("&lt;");
/*  59*/                    else
/*  59*/                    if(c == '>')
/*  60*/                        pendingCharacters.append("&gt;");
/*  61*/                    else
/*  61*/                    if(c == '&')
/*  62*/                        pendingCharacters.append("&amp;");
/*  64*/                    else
/*  64*/                        pendingCharacters.append(c);
                        } else
                        {
/*  67*/                    pendingCharacters.append(c);
                        }
                    }

/*  70*/            if(!flag)
/*  71*/                switchToXML();
                } else
                {
/*  74*/            super.characters(ac, i, j);
                }
            }

            public void processingInstruction(String s, String s1)
                throws TransformerException
            {
/*  83*/        if(!committed)
                {
/*  84*/            if(pendingCharacters == null)
/*  85*/                pendingCharacters = new StringBuffer();
/*  87*/            pendingCharacters.append("<?" + s + " " + s1 + "?>");
                } else
                {
/*  89*/            super.processingInstruction(s, s1);
                }
            }

            public void comment(char ac[], int i, int j)
                throws TransformerException
            {
/*  98*/        if(!committed)
                {
/*  99*/            if(pendingCharacters == null)
/* 100*/                pendingCharacters = new StringBuffer();
/* 102*/            pendingCharacters.append("<!--" + new String(ac, i, j) + "-->");
                } else
                {
/* 104*/            super.comment(ac, i, j);
                }
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/* 117*/        if(!committed)
                {
/* 118*/            String s = super.namePool.getLocalName(i);
/* 119*/            short word0 = super.namePool.getURICode(i);
/* 120*/            if(s.equalsIgnoreCase("html") && word0 == 0)
/* 121*/                switchToHTML();
/* 123*/            else
/* 123*/                switchToXML();
                }
/* 126*/        super.startElement(i, attributes, ai, j);
            }

            private void switchToXML()
                throws TransformerException
            {
/* 134*/        Object obj = new XMLEmitter();
/* 135*/        String s = super.outputProperties.getProperty("indent");
/* 136*/        if(s != null && s.equals("yes"))
                {
/* 137*/            XMLIndenter xmlindenter = new XMLIndenter();
/* 138*/            xmlindenter.setUnderlyingEmitter(((Emitter) (obj)));
/* 139*/            obj = xmlindenter;
                }
/* 141*/        String s1 = super.outputProperties.getProperty("cdata-section-elements");
/* 142*/        if(s1 != null && s1.length() > 0)
                {
/* 143*/            CDATAFilter cdatafilter = new CDATAFilter();
/* 144*/            cdatafilter.setUnderlyingEmitter(((Emitter) (obj)));
/* 145*/            obj = cdatafilter;
                }
/* 147*/        switchTo(((Emitter) (obj)));
            }

            private void switchToHTML()
                throws TransformerException
            {
/* 155*/        Object obj = new HTMLEmitter();
/* 156*/        String s = super.outputProperties.getProperty("indent");
/* 157*/        if(s == null || s.equals("yes"))
                {
/* 158*/            HTMLIndenter htmlindenter = new HTMLIndenter();
/* 159*/            htmlindenter.setUnderlyingEmitter(((Emitter) (obj)));
/* 160*/            obj = htmlindenter;
                }
/* 162*/        switchTo(((Emitter) (obj)));
            }

            public void setEscaping(boolean flag)
                throws TransformerException
            {
/* 172*/        if(!committed)
/* 173*/            initialEscaping = flag;
/* 175*/        super.setEscaping(flag);
            }

            private void switchTo(Emitter emitter)
                throws TransformerException
            {
/* 183*/        setUnderlyingEmitter(emitter);
/* 184*/        committed = true;
/* 185*/        emitter.setWriter(super.writer);
/* 186*/        emitter.setOutputProperties(super.outputProperties);
/* 187*/        emitter.startDocument();
/* 188*/        if(pendingCharacters != null)
                {
/* 189*/            emitter.setEscaping(false);
/* 190*/            int i = pendingCharacters.length();
/* 191*/            char ac[] = new char[i];
/* 192*/            pendingCharacters.getChars(0, i, ac, 0);
/* 193*/            emitter.characters(ac, 0, i);
                }
/* 195*/        emitter.setEscaping(initialEscaping);
            }
}
