// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   HTMLEmitter.java

package com.icl.saxon.output;

import com.icl.saxon.charcode.CharacterSet;
import com.icl.saxon.om.NamePool;
import com.icl.saxon.sort.HashMap;
import com.icl.saxon.tree.AttributeCollection;
import java.io.*;
import java.util.Properties;
import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

// Referenced classes of package com.icl.saxon.output:
//            XMLEmitter, Emitter

public class HTMLEmitter extends XMLEmitter
{

            private static final int REP_NATIVE = 0;
            private static final int REP_ENTITY = 1;
            private static final int REP_DECIMAL = 2;
            private static final int REP_HEX = 3;
            private int nonASCIIRepresentation;
            private int excludedRepresentation;
            private String mediaType;
            private int inScript;
            private boolean started;
            static HashMap emptyTags = new HashMap(101);
            private static HashMap booleanAttributes = new HashMap(101);
            private static HashMap booleanCombinations = new HashMap(203);
            private static HashMap urlAttributes = new HashMap(101);
            private static HashMap urlCombinations = new HashMap(203);
            private static String latin1Entities[] = {
/* 493*/        "nbsp", "iexcl", "cent", "pound", "curren", "yen", "brvbar", "sect", "uml", "copy", 
/* 493*/        "ordf", "laquo", "not", "shy", "reg", "macr", "deg", "plusmn", "sup2", "sup3", 
/* 493*/        "acute", "micro", "para", "middot", "cedil", "sup1", "ordm", "raquo", "frac14", "frac12", 
/* 493*/        "frac34", "iquest", "Agrave", "Aacute", "Acirc", "Atilde", "Auml", "Aring", "AElig", "Ccedil", 
/* 493*/        "Egrave", "Eacute", "Ecirc", "Euml", "Igrave", "Iacute", "Icirc", "Iuml", "ETH", "Ntilde", 
/* 493*/        "Ograve", "Oacute", "Ocirc", "Otilde", "Ouml", "times", "Oslash", "Ugrave", "Uacute", "Ucirc", 
/* 493*/        "Uuml", "Yacute", "THORN", "szlig", "agrave", "aacute", "acirc", "atilde", "auml", "aring", 
/* 493*/        "aelig", "ccedil", "egrave", "eacute", "ecirc", "euml", "igrave", "iacute", "icirc", "iuml", 
/* 493*/        "eth", "ntilde", "ograve", "oacute", "ocirc", "otilde", "ouml", "divide", "oslash", "ugrave", 
/* 493*/        "uacute", "ucirc", "uuml", "yacute", "thorn", "yuml"
            };

            private static int representationCode(String s)
            {
/*  40*/        if(s.equalsIgnoreCase("native"))
/*  40*/            return 0;
/*  41*/        if(s.equalsIgnoreCase("entity"))
/*  41*/            return 1;
/*  42*/        if(s.equalsIgnoreCase("decimal"))
/*  42*/            return 2;
/*  43*/        return !s.equalsIgnoreCase("hex") ? 1 : 3;
            }

            private static void setEmptyTag(String s)
            {
/*  70*/        emptyTags.set(s);
            }

            protected static boolean isEmptyTag(String s)
            {
/*  74*/        return emptyTags.get(s);
            }

            private static void setBooleanAttribute(String s, String s1)
            {
/* 114*/        booleanAttributes.set(s1);
/* 115*/        booleanCombinations.set(s + "+" + s1);
            }

            private static boolean isBooleanAttribute(String s, String s1, String s2)
            {
/* 119*/        if(!s1.equalsIgnoreCase(s2))
/* 119*/            return false;
/* 120*/        if(!booleanAttributes.get(s1))
/* 120*/            return false;
/* 121*/        else
/* 121*/            return booleanCombinations.get(s + "+" + s1);
            }

            private static void setUrlAttribute(String s, String s1)
            {
/* 164*/        urlAttributes.set(s1);
/* 165*/        urlCombinations.set(s + "+" + s1);
            }

            public static boolean isUrlAttribute(String s, String s1)
            {
/* 169*/        if(!urlAttributes.get(s1))
/* 169*/            return false;
/* 170*/        else
/* 170*/            return urlCombinations.get(s + "+" + s1);
            }

            public HTMLEmitter()
            {
/*  29*/        nonASCIIRepresentation = 1;
/*  30*/        excludedRepresentation = 2;
/*  31*/        mediaType = "text/html";
/*  33*/        started = false;
            }

            public void startDocument()
                throws TransformerException
            {
/* 186*/        if(started)
/* 186*/            return;
/* 187*/        started = true;
/* 189*/        String s = super.outputProperties.getProperty("media-type");
/* 190*/        if(s != null)
/* 191*/            mediaType = s;
/* 194*/        String s1 = super.outputProperties.getProperty("doctype-system");
/* 195*/        String s2 = super.outputProperties.getProperty("doctype-public");
/* 197*/        if(s1 != null || s2 != null)
/* 198*/            writeDocType("html", s1, s2);
/* 201*/        super.empty = false;
/* 202*/        inScript = 0xfff0bdc0;
/* 204*/        String s3 = super.outputProperties.getProperty("{http://icl.com/saxon}character-representation");
/* 206*/        if(s3 != null)
                {
/* 209*/            int i = s3.indexOf(';');
                    String s4;
                    String s5;
/* 210*/            if(i < 0)
                    {
/* 211*/                s4 = s3;
/* 212*/                s5 = s3;
                    } else
                    {
/* 214*/                s4 = s3.substring(0, i).trim();
/* 215*/                s5 = s3.substring(i + 1).trim();
                    }
/* 217*/            nonASCIIRepresentation = representationCode(s4);
/* 218*/            excludedRepresentation = representationCode(s5);
/* 219*/            if(excludedRepresentation == 0)
/* 220*/                excludedRepresentation = 1;
                }
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/* 232*/        String s = super.namePool.getLocalName(i);
/* 233*/        short word0 = super.namePool.getURICode(i);
/* 234*/        if(word0 == 0 && (s.equalsIgnoreCase("script") || s.equalsIgnoreCase("style")))
/* 236*/            inScript = 0;
/* 238*/        inScript++;
/* 239*/        super.startElement(i, attributes, ai, j);
/* 240*/        closeStartTag(-1, false);
/* 243*/        if(word0 == 0 && s.equalsIgnoreCase("head"))
                {
/* 244*/            String s1 = super.outputProperties.getProperty("{http://icl.com/saxon}omit-meta-tag");
/* 246*/            if(!"yes".equals(s1))
                    {
/* 248*/                String s2 = super.outputProperties.getProperty("encoding");
/* 249*/                if(s2 == null)
/* 249*/                    s2 = "utf-8";
/* 251*/                AttributeCollection attributecollection = new AttributeCollection(super.namePool);
/* 252*/                attributecollection.addAttribute("", "", "http-equiv", "CDATA", "Content-Type");
/* 253*/                attributecollection.addAttribute("", "", "content", "CDATA", mediaType + "; charset=" + s2);
/* 255*/                try
                        {
/* 255*/                    super.writer.write("\n      ");
                        }
/* 255*/                catch(IOException ioexception) { }
/* 256*/                int k = super.namePool.allocate("", "", "meta");
/* 257*/                startElement(k, ((Attributes) (attributecollection)), new int[0], 0);
/* 258*/                endElement(k);
/* 259*/                try
                        {
/* 259*/                    super.writer.write("\n   ");
                        }
/* 259*/                catch(IOException ioexception1) { }
                    }
                }
            }

            protected void writeAttribute(int i, String s, String s1, String s2)
                throws TransformerException
            {
/* 272*/        try
                {
/* 272*/            String s3 = super.namePool.getDisplayName(i);
/* 273*/            short word0 = super.namePool.getURICode(i);
/* 274*/            if(word0 == 0 && isBooleanAttribute(s3, s, s2))
                    {
/* 275*/                testCharacters(s);
/* 276*/                super.writer.write(s);
                    } else
/* 277*/            if(word0 == 0 && isUrlAttribute(s3, s) && !s1.equals("NO-ESC"))
                    {
/* 278*/                String s4 = escapeURL(s2);
/* 279*/                super.writeAttribute(i, s, s1, s4);
                    } else
                    {
/* 281*/                super.writeAttribute(i, s, s1, s2);
                    }
                }
/* 284*/        catch(IOException ioexception)
                {
/* 284*/            throw new TransformerException(ioexception);
                }
            }

            protected void writeEscape(char ac[], int i, int j, boolean flag)
                throws IOException
            {
/* 296*/        int k = i;
/* 297*/        boolean aflag[] = flag ? XMLEmitter.specialInAtt : XMLEmitter.specialInText;
                int l;
/* 300*/        for(; k < i + j; k = ++l)
                {
/* 300*/            for(l = k; l < i + j && (ac[l] >= '\200' ? super.characterSet.inCharset(ac[l]) ? nonASCIIRepresentation == 0 && ac[l] != '\240' : false : !aflag[ac[l]]); l++);
/* 317*/            super.writer.write(ac, k, l - k);
/* 321*/            if(l == i + j)
/* 321*/                return;
/* 323*/            if(ac[l] < '\177')
                    {
/* 327*/                if(flag)
                        {
/* 328*/                    if(ac[l] == '<')
/* 329*/                        super.writer.write(60);
/* 330*/                    else
/* 330*/                    if(ac[l] == '>')
/* 331*/                        super.writer.write("&gt;");
/* 332*/                    else
/* 332*/                    if(ac[l] == '&')
                            {
/* 333*/                        if(l + 1 < i + j && ac[l + 1] == '{')
/* 334*/                            super.writer.write(38);
/* 336*/                        else
/* 336*/                            super.writer.write("&amp;");
                            } else
/* 338*/                    if(ac[l] == '"')
/* 339*/                        super.writer.write("&#34;");
/* 340*/                    else
/* 340*/                    if(ac[l] == '\n')
/* 341*/                        super.writer.write("&#xA;");
                        } else
/* 344*/                if(ac[l] == '<')
/* 345*/                    super.writer.write("&lt;");
/* 346*/                else
/* 346*/                if(ac[l] == '>')
/* 347*/                    super.writer.write("&gt;");
/* 348*/                else
/* 348*/                if(ac[l] == '&')
/* 349*/                    super.writer.write("&amp;");
/* 349*/                continue;
                    }
/* 353*/            if(ac[l] == '\240')
                    {
/* 355*/                super.writer.write("&nbsp;");
/* 355*/                continue;
                    }
/* 357*/            if(ac[l] >= '\uD800' && ac[l] <= '\uDBFF')
                    {
/* 366*/                int i1 = (ac[l] - 55296) * 1024 + (ac[l + 1] - 56320) + 0x10000;
/* 367*/                outputCharacterReference(i1);
/* 368*/                l++;
/* 368*/                continue;
                    }
/* 371*/            if(super.characterSet.inCharset(ac[l]))
                    {
/* 372*/                switch(nonASCIIRepresentation)
                        {
/* 374*/                case 0: // '\0'
/* 374*/                    super.writer.write(ac[l]);
/* 375*/                    continue;

/* 377*/                case 1: // '\001'
/* 377*/                    if(ac[l] > '\240' && ac[l] <= '\377')
                            {
/* 381*/                        super.writer.write(38);
/* 382*/                        super.writer.write(latin1Entities[ac[l] - 160]);
/* 383*/                        super.writer.write(59);
/* 384*/                        continue;
                            }
                            // fall through

/* 388*/                case 2: // '\002'
/* 388*/                    super.preferHex = false;
/* 389*/                    outputCharacterReference(ac[l]);
                            break;

/* 392*/                case 3: // '\003'
/* 392*/                    super.preferHex = true;
                            // fall through

/* 395*/                default:
/* 395*/                    outputCharacterReference(ac[l]);
                            break;
                        }
                    } else
                    {
/* 400*/                super.preferHex = excludedRepresentation == 3;
/* 401*/                outputCharacterReference(ac[l]);
                    }
                }

            }

            public void endElement(int i)
                throws TransformerException
            {
/* 415*/        String s = super.namePool.getLocalName(i);
/* 416*/        short word0 = super.namePool.getURICode(i);
/* 417*/        inScript--;
/* 418*/        if(inScript == 0)
/* 419*/            inScript = 0xfff0bdc0;
/* 422*/        if(word0 != 0 || !isEmptyTag(s))
/* 423*/            super.endElement(i);
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/* 434*/        if(inScript > 0 && super.escaping)
                {
/* 435*/            setEscaping(false);
/* 436*/            super.characters(ac, i, j);
/* 437*/            setEscaping(true);
                } else
                {
/* 439*/            super.characters(ac, i, j);
                }
            }

            public void processingInstruction(String s, String s1)
                throws TransformerException
            {
/* 451*/        try
                {
/* 451*/            super.writer.write("<?");
/* 452*/            super.writer.write(s);
/* 453*/            super.writer.write(32);
/* 454*/            super.writer.write(s1);
/* 455*/            super.writer.write(62);
                }
/* 457*/        catch(IOException ioexception)
                {
/* 457*/            throw new TransformerException(ioexception);
                }
            }

            private static String escapeURL(String s)
                throws TransformerException
            {
/* 463*/        StringBuffer stringbuffer = new StringBuffer();
/* 464*/        String s1 = "0123456789ABCDEF";
/* 465*/        for(int i = 0; i < s.length(); i++)
                {
/* 466*/            char c = s.charAt(i);
/* 467*/            if(c < ' ' || c > '~')
                    {
/* 468*/                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
/* 470*/                try
                        {
/* 470*/                    OutputStreamWriter outputstreamwriter = new OutputStreamWriter(bytearrayoutputstream, "UTF8");
/* 471*/                    outputstreamwriter.write(c);
/* 472*/                    outputstreamwriter.close();
                        }
/* 474*/                catch(UnsupportedEncodingException unsupportedencodingexception)
                        {
/* 474*/                    throw new TransformerException(unsupportedencodingexception);
                        }
/* 476*/                catch(IOException ioexception)
                        {
/* 476*/                    throw new TransformerException(ioexception);
                        }
/* 478*/                byte abyte0[] = bytearrayoutputstream.toByteArray();
/* 479*/                for(int j = 0; j < abyte0.length; j++)
                        {
/* 480*/                    int k = abyte0[j] < 0 ? 256 + abyte0[j] : ((int) (abyte0[j]));
/* 481*/                    stringbuffer.append('%');
/* 482*/                    stringbuffer.append(s1.charAt(k / 16));
/* 483*/                    stringbuffer.append(s1.charAt(k % 16));
                        }

                    } else
                    {
/* 487*/                stringbuffer.append(c);
                    }
                }

/* 490*/        return stringbuffer.toString();
            }

            static 
            {
/*  54*/        setEmptyTag("area");
/*  55*/        setEmptyTag("base");
/*  56*/        setEmptyTag("basefont");
/*  57*/        setEmptyTag("br");
/*  58*/        setEmptyTag("col");
/*  59*/        setEmptyTag("frame");
/*  60*/        setEmptyTag("hr");
/*  61*/        setEmptyTag("img");
/*  62*/        setEmptyTag("input");
/*  63*/        setEmptyTag("isindex");
/*  64*/        setEmptyTag("link");
/*  65*/        setEmptyTag("meta");
/*  66*/        setEmptyTag("param");
/*  87*/        setBooleanAttribute("area", "nohref");
/*  88*/        setBooleanAttribute("button", "disabled");
/*  89*/        setBooleanAttribute("dir", "compact");
/*  90*/        setBooleanAttribute("dl", "compact");
/*  91*/        setBooleanAttribute("frame", "noresize");
/*  92*/        setBooleanAttribute("hr", "noshade");
/*  93*/        setBooleanAttribute("img", "ismap");
/*  94*/        setBooleanAttribute("input", "checked");
/*  95*/        setBooleanAttribute("input", "disabled");
/*  96*/        setBooleanAttribute("input", "readonly");
/*  97*/        setBooleanAttribute("menu", "compact");
/*  98*/        setBooleanAttribute("object", "declare");
/*  99*/        setBooleanAttribute("ol", "compact");
/* 100*/        setBooleanAttribute("optgroup", "disabled");
/* 101*/        setBooleanAttribute("option", "selected");
/* 102*/        setBooleanAttribute("option", "disabled");
/* 103*/        setBooleanAttribute("script", "defer");
/* 104*/        setBooleanAttribute("select", "multiple");
/* 105*/        setBooleanAttribute("select", "disabled");
/* 106*/        setBooleanAttribute("td", "nowrap");
/* 107*/        setBooleanAttribute("textarea", "disabled");
/* 108*/        setBooleanAttribute("textarea", "readonly");
/* 109*/        setBooleanAttribute("th", "nowrap");
/* 110*/        setBooleanAttribute("ul", "compact");
/* 134*/        setUrlAttribute("form", "action");
/* 135*/        setUrlAttribute("body", "background");
/* 136*/        setUrlAttribute("q", "cite");
/* 137*/        setUrlAttribute("blockquote", "cite");
/* 138*/        setUrlAttribute("del", "cite");
/* 139*/        setUrlAttribute("ins", "cite");
/* 140*/        setUrlAttribute("object", "classid");
/* 141*/        setUrlAttribute("object", "codebase");
/* 142*/        setUrlAttribute("applet", "codebase");
/* 143*/        setUrlAttribute("object", "data");
/* 144*/        setUrlAttribute("a", "href");
/* 145*/        setUrlAttribute("a", "name");
/* 146*/        setUrlAttribute("area", "href");
/* 147*/        setUrlAttribute("link", "href");
/* 148*/        setUrlAttribute("base", "href");
/* 149*/        setUrlAttribute("img", "longdesc");
/* 150*/        setUrlAttribute("frame", "longdesc");
/* 151*/        setUrlAttribute("iframe", "longdesc");
/* 152*/        setUrlAttribute("head", "profile");
/* 153*/        setUrlAttribute("script", "src");
/* 154*/        setUrlAttribute("input", "src");
/* 155*/        setUrlAttribute("frame", "src");
/* 156*/        setUrlAttribute("iframe", "src");
/* 157*/        setUrlAttribute("img", "src");
/* 158*/        setUrlAttribute("img", "usemap");
/* 159*/        setUrlAttribute("input", "usemap");
/* 160*/        setUrlAttribute("object", "usemap");
            }
}
