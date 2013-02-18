// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XMLEmitter.java

package com.icl.saxon.output;

import com.icl.saxon.charcode.*;
import com.icl.saxon.om.NamePool;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

// Referenced classes of package com.icl.saxon.output:
//            Emitter

public class XMLEmitter extends Emitter
{

            protected CharacterSet characterSet;
            protected boolean empty;
            protected boolean escaping;
            protected boolean openStartTag;
            protected boolean declarationIsWritten;
            protected boolean preferHex;
            protected int lastNameCode;
            protected String lastDisplayName;
            protected String lastPrefix;
            protected String lastURI;
            static boolean specialInText[];
            static boolean specialInAtt[];
            boolean docTypeWritten;
            char attbuff1[];
            private char charref[];

            public XMLEmitter()
            {
/*  21*/        characterSet = null;
/*  23*/        empty = true;
/*  24*/        escaping = true;
/*  25*/        openStartTag = false;
/*  26*/        declarationIsWritten = false;
/*  28*/        preferHex = false;
/*  31*/        lastNameCode = -1;
/* 125*/        docTypeWritten = false;
/* 258*/        attbuff1 = new char[256];
/* 458*/        charref = new char[10];
            }

            public void setDocumentLocator(Locator locator)
            {
            }

            public void startDocument()
                throws TransformerException
            {
/*  72*/        if(characterSet == null)
/*  72*/            characterSet = new UnicodeCharacterSet();
/*  73*/        writeDeclaration();
/*  74*/        empty = true;
/*  75*/        String s = super.outputProperties.getProperty("{http://icl.com/saxon}character-representation");
/*  76*/        if(s != null)
/*  77*/            preferHex = s.trim().equalsIgnoreCase("hex");
            }

            public void writeDeclaration()
                throws TransformerException
            {
/*  86*/        if(declarationIsWritten)
/*  86*/            return;
/*  87*/        declarationIsWritten = true;
/*  90*/        try
                {
/*  90*/            String s = super.outputProperties.getProperty("omit-xml-declaration");
/*  91*/            if(s == null)
/*  91*/                s = "no";
/*  93*/            String s1 = super.outputProperties.getProperty("version");
/*  94*/            if(s1 == null)
/*  94*/                s1 = "1.0";
/*  96*/            String s2 = super.outputProperties.getProperty("encoding");
/*  97*/            if(s2 == null || s2.equalsIgnoreCase("utf8"))
/*  98*/                s2 = "utf-8";
/* 101*/            if(!s2.equalsIgnoreCase("utf-8"))
/* 102*/                s = "no";
/* 105*/            String s3 = super.outputProperties.getProperty("standalone");
/* 107*/            if(s.equals("no"))
/* 108*/                super.writer.write("<?xml version=\"" + s1 + "\" " + "encoding=\"" + s2 + "\"" + (s3 == null ? "" : " standalone=\"" + s3 + "\"") + "?>");
                }
/* 116*/        catch(IOException ioexception)
                {
/* 116*/            throw new TransformerException(ioexception);
                }
            }

            protected void writeDocType(String s, String s1, String s2)
                throws TransformerException
            {
/* 127*/        if(docTypeWritten)
/* 127*/            return;
/* 128*/        docTypeWritten = true;
/* 130*/        try
                {
/* 130*/            super.writer.write("\n<!DOCTYPE " + s + "\n");
/* 131*/            if(s1 != null && s2 == null)
/* 132*/                super.writer.write("  SYSTEM \"" + s1 + "\">\n");
/* 133*/            else
/* 133*/            if(s1 == null && s2 != null)
/* 134*/                super.writer.write("  PUBLIC \"" + s2 + "\">\n");
/* 136*/            else
/* 136*/                super.writer.write("  PUBLIC \"" + s2 + "\" \"" + s1 + "\">\n");
                }
/* 139*/        catch(IOException ioexception)
                {
/* 139*/            throw new TransformerException(ioexception);
                }
            }

            public void endDocument()
                throws TransformerException
            {
/* 150*/        try
                {
/* 150*/            super.writer.flush();
                }
/* 152*/        catch(IOException ioexception)
                {
/* 152*/            throw new TransformerException(ioexception);
                }
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
                String s4;
/* 167*/        if(i == lastNameCode)
                {
/* 168*/            String s = lastPrefix;
/* 169*/            String s2 = lastURI;
/* 170*/            s4 = lastDisplayName;
                } else
                {
/* 172*/            String s1 = super.namePool.getPrefix(i);
/* 173*/            String s3 = super.namePool.getURI(i);
/* 174*/            s4 = super.namePool.getDisplayName(i);
/* 176*/            lastNameCode = i;
/* 177*/            lastDisplayName = s4;
/* 178*/            lastPrefix = s1;
/* 179*/            lastURI = s3;
                }
/* 183*/        try
                {
/* 183*/            if(empty)
                    {
/* 184*/                String s5 = super.outputProperties.getProperty("doctype-system");
/* 185*/                String s6 = super.outputProperties.getProperty("doctype-public");
/* 186*/                if(s5 != null)
/* 187*/                    writeDocType(s4, s5, s6);
/* 189*/                empty = false;
                    }
/* 191*/            if(openStartTag)
/* 192*/                closeStartTag(i, false);
/* 194*/            super.writer.write(60);
/* 195*/            testCharacters(s4);
/* 196*/            super.writer.write(s4);
/* 200*/            for(int k = 0; k < j; k++)
                    {
/* 201*/                super.writer.write(32);
/* 202*/                String s7 = super.namePool.getPrefixFromNamespaceCode(ai[k]);
/* 203*/                String s8 = super.namePool.getURIFromNamespaceCode(ai[k]);
/* 205*/                if(s7.equals(""))
/* 206*/                    writeAttribute(i, "xmlns", "CDATA", s8);
/* 208*/                else
/* 208*/                    writeAttribute(i, "xmlns:" + s7, "CDATA", s8);
                    }

/* 215*/            for(int l = 0; l < attributes.getLength(); l++)
                    {
/* 216*/                super.writer.write(32);
/* 217*/                writeAttribute(i, attributes.getQName(l), attributes.getType(l), attributes.getValue(l));
                    }

/* 223*/            openStartTag = true;
                }
/* 226*/        catch(IOException ioexception)
                {
/* 226*/            throw new TransformerException(ioexception);
                }
            }

            protected void closeStartTag(int i, boolean flag)
                throws TransformerException
            {
/* 232*/        try
                {
/* 232*/            if(openStartTag)
                    {
/* 233*/                if(flag)
/* 234*/                    super.writer.write(emptyElementTagCloser(i));
/* 236*/                else
/* 236*/                    super.writer.write(62);
/* 238*/                openStartTag = false;
                    }
                }
/* 241*/        catch(IOException ioexception)
                {
/* 241*/            throw new TransformerException(ioexception);
                }
            }

            protected String emptyElementTagCloser(int i)
            {
/* 250*/        return "/>";
            }

            protected void writeAttribute(int i, String s, String s1, String s2)
                throws TransformerException
            {
/* 261*/        try
                {
/* 261*/            testCharacters(s);
/* 262*/            super.writer.write(s);
/* 263*/            if(s1.equals("NO-ESC"))
                    {
/* 265*/                super.writer.write(61);
/* 266*/                byte byte0 = ((byte)(s2.indexOf('"') < 0 ? 34 : 39));
/* 267*/                super.writer.write(byte0);
/* 268*/                super.writer.write(s2);
/* 269*/                super.writer.write(byte0);
                    } else
                    {
/* 271*/                super.writer.write("=\"");
/* 272*/                int j = s2.length();
/* 273*/                if(j > attbuff1.length)
/* 274*/                    attbuff1 = new char[j];
/* 276*/                s2.getChars(0, j, attbuff1, 0);
/* 277*/                writeEscape(attbuff1, 0, j, true);
/* 278*/                super.writer.write(34);
                    }
                }
/* 281*/        catch(IOException ioexception)
                {
/* 281*/            throw new TransformerException(ioexception);
                }
            }

            protected void testCharacters(String s)
                throws TransformerException
            {
/* 291*/        for(int i = s.length() - 1; i >= 0; i--)
/* 292*/            if(!characterSet.inCharset(s.charAt(i)))
/* 293*/                throw new TransformerException("Invalid character in output name (" + s + ")");

            }

            protected boolean testCharacters(char ac[], int i, int j)
            {
/* 301*/        for(int k = i; k < j; k++)
/* 302*/            if(!characterSet.inCharset(ac[k]))
/* 304*/                return false;

/* 307*/        return true;
            }

            public void endElement(int i)
                throws TransformerException
            {
/* 317*/        try
                {
/* 317*/            if(openStartTag)
                    {
/* 318*/                closeStartTag(i, true);
                    } else
                    {
                        String s;
/* 321*/                if(i == lastNameCode)
/* 322*/                    s = lastDisplayName;
/* 324*/                else
/* 324*/                    s = super.namePool.getDisplayName(i);
/* 326*/                super.writer.write("</");
/* 327*/                super.writer.write(s);
/* 328*/                super.writer.write(62);
                    }
                }
/* 331*/        catch(IOException ioexception)
                {
/* 331*/            throw new TransformerException(ioexception);
                }
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/* 342*/        try
                {
/* 342*/            if(openStartTag)
/* 343*/                closeStartTag(-1, false);
/* 345*/            if(!escaping)
                    {
/* 346*/                if(testCharacters(ac, i, j))
/* 347*/                    super.writer.write(ac, i, j);
/* 350*/                else
/* 350*/                    writeEscape(ac, i, j, false);
                    } else
                    {
/* 353*/                writeEscape(ac, i, j, false);
                    }
                }
/* 356*/        catch(IOException ioexception)
                {
/* 356*/            throw new TransformerException(ioexception);
                }
            }

            public void processingInstruction(String s, String s1)
                throws TransformerException
            {
/* 369*/        try
                {
/* 369*/            if(openStartTag)
/* 370*/                closeStartTag(-1, false);
/* 372*/            super.writer.write("<?" + s + (s1.length() <= 0 ? "" : ' ' + s1) + "?>");
                }
/* 374*/        catch(IOException ioexception)
                {
/* 374*/            throw new TransformerException(ioexception);
                }
            }

            protected void writeEscape(char ac[], int i, int j, boolean flag)
                throws IOException
            {
/* 389*/        int k = i;
/* 390*/        boolean aflag[] = flag ? specialInAtt : specialInText;
                int l;
/* 393*/        for(; k < i + j; k = ++l)
                {
/* 393*/            for(l = k; l < i + j && (ac[l] >= '\200' ? characterSet.inCharset(ac[l]) : !aflag[ac[l]]); l++);
/* 402*/            super.writer.write(ac, k, l - k);
/* 405*/            if(l >= i + j)
/* 405*/                return;
/* 407*/            if(ac[l] > '\177')
                    {
                        int i1;
/* 420*/                if(ac[l] >= '\uD800' && ac[l] <= '\uDBFF')
                        {
/* 422*/                    i1 = (ac[l] - 55296) * 1024 + (ac[l + 1] - 56320) + 0x10000;
/* 423*/                    l++;
                        } else
                        {
/* 425*/                    i1 = ac[l];
                        }
/* 428*/                outputCharacterReference(i1);
                    } else
/* 434*/            if(ac[l] == '<')
/* 435*/                super.writer.write("&lt;");
/* 436*/            else
/* 436*/            if(ac[l] == '>')
/* 437*/                super.writer.write("&gt;");
/* 438*/            else
/* 438*/            if(ac[l] == '&')
/* 439*/                super.writer.write("&amp;");
/* 440*/            else
/* 440*/            if(ac[l] == '"')
/* 441*/                super.writer.write("&#34;");
/* 442*/            else
/* 442*/            if(ac[l] == '\n')
/* 443*/                super.writer.write("&#xA;");
/* 444*/            else
/* 444*/            if(ac[l] == '\r')
/* 445*/                super.writer.write("&#xD;");
/* 446*/            else
/* 446*/            if(ac[l] == '\t')
/* 447*/                super.writer.write("&#x9;");
                }

            }

            protected void outputCharacterReference(int i)
                throws IOException
            {
/* 460*/        if(preferHex)
                {
/* 461*/            int j = 0;
/* 462*/            charref[j++] = '&';
/* 463*/            charref[j++] = '#';
/* 464*/            charref[j++] = 'x';
/* 465*/            String s = Integer.toHexString(i);
/* 466*/            int l = s.length();
/* 467*/            for(int j1 = 0; j1 < l; j1++)
/* 468*/                charref[j++] = s.charAt(j1);

/* 470*/            charref[j++] = ';';
/* 471*/            super.writer.write(charref, 0, j);
                } else
                {
/* 473*/            int k = 0;
/* 474*/            charref[k++] = '&';
/* 475*/            charref[k++] = '#';
/* 476*/            String s1 = Integer.toString(i);
/* 477*/            int i1 = s1.length();
/* 478*/            for(int k1 = 0; k1 < i1; k1++)
/* 479*/                charref[k++] = s1.charAt(k1);

/* 481*/            charref[k++] = ';';
/* 482*/            super.writer.write(charref, 0, k);
                }
            }

            public void setEscaping(boolean flag)
            {
/* 491*/        escaping = flag;
            }

            public void comment(char ac[], int i, int j)
                throws TransformerException
            {
/* 501*/        try
                {
/* 501*/            if(openStartTag)
/* 502*/                closeStartTag(-1, false);
/* 504*/            super.writer.write("<!--");
/* 505*/            super.writer.write(ac, i, j);
/* 506*/            super.writer.write("-->");
                }
/* 508*/        catch(IOException ioexception)
                {
/* 508*/            throw new TransformerException(ioexception);
                }
            }

            public void setResult(Result result)
            {
/* 517*/        if(!(result instanceof StreamResult))
/* 518*/            throw new IllegalArgumentException("Destination for XMLEmitter must be a StreamResult");
/* 520*/        super.writer = ((StreamResult)result).getWriter();
/* 521*/        if(super.writer == null)
/* 522*/            throw new IllegalArgumentException("No writer supplied");
/* 525*/        else
/* 525*/            return;
            }

            public void setOutputProperties(Properties properties)
            {
/* 532*/        characterSet = CharacterSetFactory.getCharacterSet(properties);
/* 533*/        super.setOutputProperties(properties);
            }

            public void setUnparsedEntity(String s, String s1)
                throws TransformerException
            {
            }

            static 
            {
/*  41*/        specialInText = new boolean[128];
/*  42*/        for(int i = 0; i <= 127; i++)
/*  42*/            specialInText[i] = false;

/*  43*/        specialInText[13] = true;
/*  44*/        specialInText[60] = true;
/*  45*/        specialInText[62] = true;
/*  46*/        specialInText[38] = true;
/*  48*/        specialInAtt = new boolean[128];
/*  49*/        for(int j = 0; j <= 127; j++)
/*  49*/            specialInAtt[j] = false;

/*  50*/        specialInAtt[13] = true;
/*  51*/        specialInAtt[10] = true;
/*  52*/        specialInAtt[9] = true;
/*  53*/        specialInAtt[60] = true;
/*  54*/        specialInAtt[62] = true;
/*  55*/        specialInAtt[38] = true;
/*  56*/        specialInAtt[34] = true;
            }
}
