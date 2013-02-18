// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DTDEmitter.java

package com.icl.saxon.output;

import com.icl.saxon.om.NamePool;
import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

// Referenced classes of package com.icl.saxon.output:
//            ProxyEmitter, Emitter

public class DTDEmitter extends ProxyEmitter
{

            private String current;
            private boolean openSquare;
            private StringBuffer buffer;

            public DTDEmitter()
            {
/*  17*/        current = null;
/*  18*/        openSquare = false;
/*  19*/        buffer = null;
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/*  28*/        String s = super.namePool.getURI(i);
/*  29*/        String s1 = super.namePool.getLocalName(i);
/*  33*/        int k = -1;
/*  34*/        for(int l = 0; l < j; l++)
                {
/*  35*/            if(!super.namePool.getURIFromNamespaceCode(ai[l]).equals("http://icl.com/saxon/dtd"))
/*  36*/                continue;
/*  36*/            k = l;
/*  37*/            break;
                }

/*  41*/        if(k > 0)
                {
/*  43*/            ai[k] = ai[j - 1];
/*  44*/            j--;
                }
/*  47*/        if(s.equals("http://icl.com/saxon/dtd"))
                {
/*  49*/            if("doctype".equals(current) && !openSquare)
                    {
/*  50*/                write(" [");
/*  51*/                openSquare = true;
                    }
/*  54*/            if(s1.equals("doctype"))
                    {
/*  55*/                buffer = new StringBuffer();
/*  56*/                if(current != null)
/*  57*/                    throw new TransformerException("dtd:doctype can only appear at top level of DTD");
/*  59*/                String s2 = attributes.getValue("name");
/*  60*/                String s8 = attributes.getValue("system");
/*  61*/                String s13 = attributes.getValue("public");
/*  62*/                if(s2 == null)
/*  63*/                    throw new TransformerException("dtd:doctype must have a name attribute");
/*  66*/                write("<!DOCTYPE " + s2 + " ");
/*  67*/                if(s8 != null)
/*  68*/                    if(s13 != null)
/*  69*/                        write("PUBLIC \"" + s13 + "\" \"" + s8 + "\"");
/*  71*/                    else
/*  71*/                        write("SYSTEM \"" + s8 + "\"");
                    } else
/*  75*/            if(s1.equals("element"))
                    {
/*  76*/                if(!"doctype".equals(current))
/*  77*/                    throw new TransformerException("dtd:element can only appear as child of dtd:doctype");
/*  79*/                String s3 = attributes.getValue("name");
/*  80*/                String s9 = attributes.getValue("content");
/*  81*/                if(s3 == null)
/*  82*/                    throw new TransformerException("dtd:element must have a name attribute");
/*  84*/                if(s9 == null)
/*  85*/                    throw new TransformerException("dtd:element must have a content attribute");
/*  87*/                write("\n  <!ELEMENT " + s3 + " " + s9 + " ");
                    } else
/*  89*/            if(s1.equals("attlist"))
                    {
/*  90*/                if(!"doctype".equals(current))
/*  91*/                    throw new TransformerException("dtd:attlist can only appear as child of dtd:doctype");
/*  93*/                String s4 = attributes.getValue("element");
/*  94*/                if(s4 == null)
/*  95*/                    throw new TransformerException("dtd:attlist must have an attribute named 'element'");
/*  97*/                write("\n  <!ATTLIST " + s4 + " ");
                    } else
/*  99*/            if(s1.equals("attribute"))
                    {
/* 100*/                if(!"attlist".equals(current))
/* 101*/                    throw new TransformerException("dtd:attribute can only appear as child of dtd:attlist");
/* 103*/                String s5 = attributes.getValue("name");
/* 104*/                String s10 = attributes.getValue("type");
/* 105*/                String s14 = attributes.getValue("value");
/* 106*/                if(s5 == null)
/* 107*/                    throw new TransformerException("dtd:attribute must have a name attribute");
/* 109*/                if(s10 == null)
/* 110*/                    throw new TransformerException("dtd:attribute must have a type attribute");
/* 112*/                if(s14 == null)
/* 113*/                    throw new TransformerException("dtd:attribute must have a value attribute");
/* 115*/                write("\n    " + s5 + " " + s10 + " " + s14);
                    } else
/* 117*/            if(s1.equals("entity"))
                    {
/* 118*/                if(!"doctype".equals(current))
/* 119*/                    throw new TransformerException("dtd:entity can only appear as child of dtd:doctype");
/* 121*/                String s6 = attributes.getValue("name");
/* 122*/                String s11 = attributes.getValue("parameter");
/* 123*/                String s15 = attributes.getValue("system");
/* 124*/                String s17 = attributes.getValue("public");
/* 125*/                String s18 = attributes.getValue("notation");
/* 127*/                if(s6 == null)
/* 128*/                    throw new TransformerException("dtd:entity must have a name attribute");
/* 133*/                write("\n  <!ENTITY ");
/* 134*/                if("yes".equals(s11))
/* 135*/                    write("% ");
/* 137*/                write(s6 + " ");
/* 138*/                if(s15 != null)
/* 139*/                    if(s17 != null)
/* 140*/                        write("PUBLIC \"" + s17 + "\" \"" + s15 + "\" ");
/* 142*/                    else
/* 142*/                        write("SYSTEM \"" + s15 + "\" ");
/* 145*/                if(s18 != null)
/* 146*/                    write("NDATA " + s18 + " ");
                    } else
/* 149*/            if(s1.equals("notation"))
                    {
/* 150*/                if(!"doctype".equals(current))
/* 151*/                    throw new TransformerException("dtd:notation can only appear as a child of dtd:doctype");
/* 153*/                String s7 = attributes.getValue("name");
/* 154*/                String s12 = attributes.getValue("system");
/* 155*/                String s16 = attributes.getValue("public");
/* 156*/                if(s7 == null)
/* 157*/                    throw new TransformerException("dtd:notation must have a name attribute");
/* 159*/                if(s12 == null && s16 == null)
/* 160*/                    throw new TransformerException("dtd:notation must have a system attribute or a public attribute");
/* 162*/                write("\n  <!NOTATION " + s7);
/* 163*/                if(s16 != null)
                        {
/* 164*/                    write(" PUBLIC \"" + s16 + "\" ");
/* 165*/                    if(s12 != null)
/* 166*/                        write("\"" + s12 + "\" ");
                        } else
                        {
/* 169*/                    write(" SYSTEM \"" + s12 + "\" ");
                        }
                    } else
                    {
/* 172*/                throw new TransformerException("Unrecognized element " + s1 + " in DTD output");
                    }
                } else
                {
/* 176*/            if(!current.equals("entity"))
/* 177*/                throw new TransformerException("Unrecognized element " + s1 + " in DTD output");
/* 179*/            super.startElement(i, attributes, ai, j);
                }
/* 181*/        current = s1;
            }

            public void endElement(int i)
                throws TransformerException
            {
/* 192*/        String s = super.namePool.getURI(i);
/* 195*/        if(s.equals("http://icl.com/saxon/dtd"))
                {
/* 196*/            String s1 = super.namePool.getLocalName(i);
/* 198*/            if(s1.equals("doctype"))
                    {
/* 199*/                if(openSquare)
                        {
/* 200*/                    write("\n]");
/* 201*/                    openSquare = false;
                        }
/* 203*/                write(">\n");
/* 204*/                current = null;
/* 205*/                flush();
                    } else
/* 207*/            if(s1.equals("element"))
                    {
/* 208*/                write(">");
/* 209*/                current = "doctype";
                    } else
/* 211*/            if(s1.equals("attlist"))
                    {
/* 212*/                write(">");
/* 213*/                current = "doctype";
                    } else
/* 215*/            if(s1.equals("attribute"))
/* 216*/                current = "attlist";
/* 218*/            else
/* 218*/            if(s1.equals("entity"))
                    {
/* 219*/                write(">");
/* 220*/                current = "doctype";
                    } else
/* 222*/            if(s1.equals("notation"))
                    {
/* 223*/                write(">");
/* 224*/                current = "doctype";
                    }
                } else
                {
/* 227*/            super.endElement(i);
                }
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/* 242*/        if(buffer != null)
/* 243*/            buffer.append(ac, i, j);
/* 245*/        else
/* 245*/            super.characters(ac, i, j);
            }

            private void write(String s)
            {
/* 254*/        buffer.append(s);
            }

            private void flush()
                throws TransformerException
            {
/* 262*/        int i = buffer.length();
/* 263*/        char ac[] = new char[i];
/* 264*/        buffer.getChars(0, i, ac, 0);
/* 265*/        buffer = null;
/* 266*/        setEscaping(false);
/* 267*/        characters(ac, 0, i);
/* 268*/        setEscaping(true);
            }
}
