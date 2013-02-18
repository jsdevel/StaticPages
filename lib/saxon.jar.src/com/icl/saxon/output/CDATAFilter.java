// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   CDATAFilter.java

package com.icl.saxon.output;

import com.icl.saxon.charcode.CharacterSet;
import com.icl.saxon.charcode.CharacterSetFactory;
import com.icl.saxon.om.NamePool;
import java.util.*;
import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

// Referenced classes of package com.icl.saxon.output:
//            ProxyEmitter, Emitter

public class CDATAFilter extends ProxyEmitter
{

            private StringBuffer buffer;
            private Stack stack;
            private int nameList[];
            private CharacterSet characterSet;
            private boolean disableEscaping;

            public CDATAFilter()
            {
/*  22*/        buffer = new StringBuffer(100);
/*  23*/        stack = new Stack();
/*  26*/        disableEscaping = false;
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/*  35*/        flush(buffer);
/*  36*/        stack.push(new Integer(i & 0xfffff));
/*  37*/        super.startElement(i, attributes, ai, j);
            }

            public void endElement(int i)
                throws TransformerException
            {
/*  46*/        flush(buffer);
/*  47*/        stack.pop();
/*  48*/        super.endElement(i);
            }

            public void processingInstruction(String s, String s1)
                throws TransformerException
            {
/*  56*/        flush(buffer);
/*  57*/        super.processingInstruction(s, s1);
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/*  66*/        buffer.append(ac, i, j);
            }

            public void ignorableWhitespace(char ac[], int i, int j)
                throws TransformerException
            {
/*  74*/        buffer.append(ac, i, j);
            }

            public void comment(char ac[], int i, int j)
                throws TransformerException
            {
/*  82*/        flush(buffer);
/*  83*/        super.comment(ac, i, j);
            }

            public void setEscaping(boolean flag)
                throws TransformerException
            {
                boolean flag1;
/*  93*/        if(stack.isEmpty())
                {
/*  94*/            flag1 = false;
                } else
                {
/*  96*/            int i = ((Integer)stack.peek()).intValue();
/*  97*/            flag1 = isCDATA(i);
                }
/* 100*/        if(!flag1)
                {
/* 101*/            flush(buffer);
/* 102*/            disableEscaping = !flag;
/* 103*/            super.setEscaping(flag);
                } else
/* 105*/        if(!flag)
                {
/* 106*/            flush(buffer);
/* 107*/            disableEscaping = true;
/* 108*/            super.setEscaping(false);
                } else
                {
/* 110*/            flush(buffer);
/* 111*/            disableEscaping = false;
/* 112*/            super.setEscaping(true);
                }
            }

            public void flush(StringBuffer stringbuffer)
                throws TransformerException
            {
/* 124*/        int i = stringbuffer.length();
/* 125*/        if(i == 0)
/* 125*/            return;
                boolean flag;
/* 127*/        if(stack.isEmpty())
                {
/* 128*/            flag = false;
                } else
                {
/* 130*/            int j = ((Integer)stack.peek()).intValue();
/* 131*/            flag = isCDATA(j);
                }
/* 134*/        if(flag & (!disableEscaping))
                {
/* 139*/            int k = 0;
/* 140*/            for(int l = 0; l < i;)
                    {
/* 142*/                int i1 = stringbuffer.charAt(l);
/* 143*/                byte byte0 = 1;
/* 144*/                if(isHighSurrogate((char)i1))
                        {
/* 145*/                    i1 = supplemental((char)i1, stringbuffer.charAt(l + 1));
/* 146*/                    byte0 = 2;
                        }
/* 148*/                if(characterSet.inCharset(i1))
                        {
/* 149*/                    l++;
                        } else
                        {
/* 154*/                    char ac2[] = new char[l - k];
/* 155*/                    stringbuffer.getChars(k, l, ac2, 0);
/* 156*/                    flushCDATA(ac2, l - k);
/* 161*/                    while(l < i) 
                            {
/* 161*/                        char ac3[] = new char[byte0];
/* 162*/                        stringbuffer.getChars(l, l + byte0, ac3, 0);
/* 163*/                        super.characters(ac3, 0, byte0);
/* 164*/                        l += byte0;
/* 165*/                        int j1 = stringbuffer.charAt(l);
/* 166*/                        byte0 = 1;
/* 167*/                        if(isHighSurrogate((char)j1))
                                {
/* 168*/                            j1 = supplemental((char)j1, stringbuffer.charAt(l + 1));
/* 169*/                            byte0 = 2;
                                }
/* 171*/                        if(characterSet.inCharset(j1))
/* 172*/                            break;
                            }
/* 175*/                    k = l;
                        }
                    }

/* 178*/            char ac1[] = new char[i - k];
/* 179*/            stringbuffer.getChars(k, i, ac1, 0);
/* 180*/            flushCDATA(ac1, i - k);
                } else
                {
/* 183*/            char ac[] = new char[i];
/* 184*/            stringbuffer.getChars(0, i, ac, 0);
/* 185*/            super.characters(ac, 0, i);
                }
/* 188*/        stringbuffer.setLength(0);
            }

            private boolean isHighSurrogate(char c)
            {
/* 193*/        return (c & 0xfc00) == 55296;
            }

            private int supplemental(char c, char c1)
            {
/* 197*/        return (c - 55296) * 1024 + (c1 - 56320) + 0x10000;
            }

            private void flushCDATA(char ac[], int i)
                throws TransformerException
            {
/* 206*/        super.setEscaping(false);
/* 207*/        super.characters("<![CDATA[".toCharArray(), 0, 9);
/* 211*/        int j = 0;
/* 212*/        int k = 0;
/* 214*/        for(; j < i - 2; j++)
/* 214*/            if(ac[j] == ']' && ac[j + 1] == ']' && ac[j + 2] == '>')
                    {
/* 215*/                super.characters(ac, k, (j + 2) - k);
/* 216*/                super.characters("]]><![CDATA[".toCharArray(), 0, 12);
/* 217*/                k = j + 2;
                    }

/* 221*/        super.characters(ac, k, i - k);
/* 222*/        super.characters("]]>".toCharArray(), 0, 3);
/* 223*/        super.setEscaping(true);
            }

            public void setOutputProperties(Properties properties)
            {
/* 231*/        nameList = getCdataElements(properties);
/* 232*/        characterSet = CharacterSetFactory.getCharacterSet(properties);
/* 233*/        super.setOutputProperties(properties);
            }

            public boolean isCDATA(int i)
            {
/* 241*/        for(int j = 0; j < nameList.length; j++)
/* 242*/            if(nameList[j] == i)
/* 242*/                return true;

/* 244*/        return false;
            }

            private int[] getCdataElements(Properties properties)
            {
/* 252*/        String s = properties.getProperty("cdata-section-elements");
/* 253*/        if(s == null)
/* 254*/            return new int[0];
/* 257*/        int i = 0;
/* 258*/        for(StringTokenizer stringtokenizer = new StringTokenizer(s); stringtokenizer.hasMoreTokens();)
                {
/* 260*/            stringtokenizer.nextToken();
/* 261*/            i++;
                }

/* 263*/        int ai[] = new int[i];
/* 264*/        i = 0;
/* 265*/        for(StringTokenizer stringtokenizer1 = new StringTokenizer(s); stringtokenizer1.hasMoreTokens();)
                {
/* 267*/            String s1 = stringtokenizer1.nextToken();
/* 268*/            ai[i++] = getFingerprintForExpandedName(s1);
                }

/* 270*/        return ai;
            }

            private int getFingerprintForExpandedName(String s)
            {
                String s1;
                String s2;
/* 282*/        if(s.charAt(0) == '{')
                {
/* 283*/            int i = s.indexOf('}');
/* 284*/            if(i < 0)
/* 285*/                throw new IllegalArgumentException("No closing '}' in parameter name");
/* 287*/            s2 = s.substring(1, i);
/* 288*/            if(i == s.length())
/* 289*/                throw new IllegalArgumentException("Missing local part in parameter name");
/* 291*/            s1 = s.substring(i + 1);
                } else
                {
/* 293*/            s2 = "";
/* 294*/            s1 = s;
                }
/* 297*/        return super.namePool.allocate("", s2, s1);
            }
}
