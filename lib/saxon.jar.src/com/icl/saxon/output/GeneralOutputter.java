// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   GeneralOutputter.java

package com.icl.saxon.output;

import com.icl.saxon.Loader;
import com.icl.saxon.charcode.*;
import com.icl.saxon.om.*;
import com.icl.saxon.tinytree.TinyBuilder;
import com.icl.saxon.tree.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.Properties;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

// Referenced classes of package com.icl.saxon.output:
//            Outputter, DOMEmitter, ContentHandlerProxy, Emitter, 
//            UncommittedEmitter, HTMLEmitter, HTMLIndenter, XMLEmitter, 
//            XMLIndenter, CDATAFilter, TEXTEmitter, XHTMLEmitter, 
//            NamespaceEmitter, ProxyEmitter

public class GeneralOutputter extends Outputter
{

            private NamePool namePool;
            private Properties outputProperties;
            private Writer writer;
            private OutputStream outputStream;
            private boolean closeAfterUse;
            private int pendingStartTag;
            private AttributeCollection pendingAttList;
            private int pendingNSList[];
            private int pendingNSListSize;
            private boolean suppressAttributes;
            char charbuffer[];

            public GeneralOutputter(NamePool namepool)
            {
/*  41*/        closeAfterUse = false;
/*  43*/        pendingStartTag = -1;
/*  46*/        pendingNSList = new int[20];
/*  47*/        pendingNSListSize = 0;
/*  49*/        suppressAttributes = false;
/* 416*/        charbuffer = new char[1024];
/*  52*/        namePool = namepool;
/*  53*/        pendingAttList = new AttributeCollection(namePool, 10);
            }

            public void setOutputDestination(Properties properties, Result result)
                throws TransformerException
            {
/*  65*/        setOutputProperties(properties);
/*  67*/        Emitter emitter = makeEmitter(properties, result);
/*  68*/        emitter.setNamePool(namePool);
/*  69*/        emitter.setOutputProperties(properties);
/*  71*/        setEmitter(emitter);
/*  73*/        open();
            }

            public static String urlToFileName(String s)
            {
/*  85*/        try
                {
/*  85*/            Class class1 = Loader.getClass("java.net.URI");
/*  86*/            Class aclass[] = {
/*  86*/                java.lang.String.class
                    };
/*  87*/            Object aobj[] = {
/*  87*/                s
                    };
/*  88*/            Constructor constructor = class1.getConstructor(aclass);
/*  89*/            Object obj = constructor.newInstance(aobj);
/*  90*/            Class aclass1[] = {
/*  90*/                class1
                    };
/*  91*/            Object aobj1[] = {
/*  91*/                obj
                    };
/*  92*/            constructor = (java.io.File.class).getConstructor(aclass1);
/*  93*/            File file = (File)constructor.newInstance(aobj1);
/*  94*/            return file.toString();
                }
/*  96*/        catch(Exception exception) { }
/*  99*/        if(null != s)
                {
/* 100*/            if(s.startsWith("file:////"))
/* 101*/                s = s.substring(7);
/* 102*/            else
/* 102*/            if(s.startsWith("file:///"))
/* 103*/                s = s.substring(6);
/* 104*/            else
/* 104*/            if(s.startsWith("file://"))
/* 105*/                s = s.substring(5);
/* 106*/            else
/* 106*/            if(s.startsWith("file:/"))
/* 107*/                s = s.substring(5);
/* 108*/            else
/* 108*/            if(s.startsWith("file:"))
/* 109*/                s = s.substring(4);
/* 110*/            if(s.startsWith("file:\\\\\\\\"))
/* 111*/                s = s.substring(7);
/* 112*/            else
/* 112*/            if(s.startsWith("file:\\\\\\"))
/* 113*/                s = s.substring(6);
/* 114*/            else
/* 114*/            if(s.startsWith("file:\\\\"))
/* 115*/                s = s.substring(5);
/* 116*/            else
/* 116*/            if(s.startsWith("file:\\"))
/* 117*/                s = s.substring(5);
                }
/* 120*/        if(File.separatorChar != '/')
/* 121*/            s = s.replace('/', File.separatorChar);
/* 123*/        return s;
            }

            public static FileOutputStream makeFileOutputStream(String s, String s1, boolean flag)
                throws TransformerException
            {
/* 136*/        try
                {
/* 136*/            File file = new File(s1);
/* 138*/            if(!file.isAbsolute())
                    {
/* 139*/                String s2 = urlToFileName(s);
/* 140*/                if(null != s2)
                        {
/* 141*/                    File file1 = new File(s2);
/* 142*/                    file = new File(file1.getParent(), s1);
                        }
                    }
/* 146*/            if(flag)
                    {
/* 147*/                String s3 = file.getParent();
/* 148*/                if(null != s3 && s3.length() > 0)
                        {
/* 149*/                    File file2 = new File(s3);
/* 150*/                    file2.mkdirs();
                        }
                    }
/* 154*/            FileOutputStream fileoutputstream = new FileOutputStream(file);
/* 155*/            return fileoutputstream;
                }
/* 157*/        catch(Exception exception)
                {
/* 157*/            throw new TransformerException("Failed to create output file", exception);
                }
            }

            public Emitter makeEmitter(Properties properties, Result result)
                throws TransformerException
            {
                Object obj;
/* 171*/        if(result instanceof DOMResult)
                {
/* 172*/            Node node = ((DOMResult)result).getNode();
/* 173*/            if(node != null)
                    {
/* 174*/                if(node instanceof NodeInfo)
                        {
/* 176*/                    if(node instanceof DocumentInfo)
                            {
/* 177*/                        DocumentInfo documentinfo = (DocumentInfo)node;
/* 178*/                        if(node.getFirstChild() != null)
/* 179*/                            throw new TransformerException("Target document must be empty");
                                Object obj1;
/* 182*/                        if(documentinfo instanceof DocumentImpl)
/* 183*/                            obj1 = new TreeBuilder();
/* 185*/                        else
/* 185*/                            obj1 = new TinyBuilder();
/* 187*/                        ((Builder) (obj1)).setRootNode(documentinfo);
/* 188*/                        ((Emitter) (obj1)).setSystemId(result.getSystemId());
/* 189*/                        ((Emitter) (obj1)).setNamePool(namePool);
/* 190*/                        obj = obj1;
                            } else
                            {
/* 193*/                        throw new TransformerException("Cannot add to an existing Saxon document");
                            }
                        } else
                        {
/* 197*/                    obj = new DOMEmitter();
/* 198*/                    ((DOMEmitter)obj).setNode(node);
                        }
                    } else
                    {
/* 202*/                TinyBuilder tinybuilder = new TinyBuilder();
/* 203*/                tinybuilder.setSystemId(result.getSystemId());
/* 204*/                tinybuilder.setNamePool(namePool);
/* 205*/                tinybuilder.createDocument();
/* 206*/                Document document = (Document)tinybuilder.getCurrentDocument();
/* 207*/                ((DOMResult)result).setNode(document);
/* 208*/                obj = tinybuilder;
                    }
                } else
/* 210*/        if(result instanceof SAXResult)
                {
/* 211*/            SAXResult saxresult = (SAXResult)result;
/* 212*/            ContentHandlerProxy contenthandlerproxy = new ContentHandlerProxy();
/* 213*/            contenthandlerproxy.setUnderlyingContentHandler(saxresult.getHandler());
/* 214*/            if(saxresult.getLexicalHandler() != null)
/* 215*/                contenthandlerproxy.setLexicalHandler(saxresult.getLexicalHandler());
/* 217*/            obj = contenthandlerproxy;
                } else
/* 219*/        if(result instanceof Emitter)
/* 220*/            obj = (Emitter)result;
/* 222*/        else
/* 222*/        if(result instanceof StreamResult)
                {
/* 224*/            String s = properties.getProperty("method");
/* 225*/            if(s == null)
/* 226*/                obj = new UncommittedEmitter();
/* 228*/            else
/* 228*/            if(s.equals("html"))
                    {
/* 229*/                obj = new HTMLEmitter();
/* 230*/                if(!"no".equals(properties.getProperty("indent")))
                        {
/* 231*/                    HTMLIndenter htmlindenter = new HTMLIndenter();
/* 232*/                    htmlindenter.setUnderlyingEmitter(((Emitter) (obj)));
/* 233*/                    obj = htmlindenter;
                        }
                    } else
/* 236*/            if(s.equals("xml"))
                    {
/* 237*/                obj = new XMLEmitter();
/* 238*/                if("yes".equals(properties.getProperty("indent")))
                        {
/* 239*/                    XMLIndenter xmlindenter = new XMLIndenter();
/* 240*/                    xmlindenter.setUnderlyingEmitter(((Emitter) (obj)));
/* 241*/                    obj = xmlindenter;
                        }
/* 243*/                String s1 = properties.getProperty("cdata-section-elements");
/* 244*/                if(s1 != null && s1.length() > 0)
                        {
/* 245*/                    CDATAFilter cdatafilter = new CDATAFilter();
/* 246*/                    cdatafilter.setUnderlyingEmitter(((Emitter) (obj)));
/* 247*/                    obj = cdatafilter;
                        }
                    } else
/* 249*/            if(s.equals("text"))
                    {
/* 250*/                obj = new TEXTEmitter();
                    } else
                    {
/* 254*/                int i = s.indexOf('}');
/* 255*/                String s2 = s.substring(i + 1);
/* 256*/                int j = s2.indexOf(':');
/* 257*/                s2 = s2.substring(j + 1);
/* 259*/                if(s2.equals("fop"))
/* 262*/                    obj = Emitter.makeEmitter("com.icl.saxon.fop.FOPEmitter");
/* 263*/                else
/* 263*/                if(s2.equals("xhtml"))
                        {
/* 264*/                    obj = new XHTMLEmitter();
/* 265*/                    if("yes".equals(properties.getProperty("indent")))
                            {
/* 266*/                        HTMLIndenter htmlindenter1 = new HTMLIndenter();
/* 267*/                        htmlindenter1.setUnderlyingEmitter(((Emitter) (obj)));
/* 268*/                        obj = htmlindenter1;
                            }
/* 270*/                    String s3 = properties.getProperty("cdata-section-elements");
/* 271*/                    if(s3 != null && s3.length() > 0)
                            {
/* 272*/                        CDATAFilter cdatafilter1 = new CDATAFilter();
/* 273*/                        cdatafilter1.setUnderlyingEmitter(((Emitter) (obj)));
/* 274*/                        obj = cdatafilter1;
                            }
                        } else
                        {
/* 277*/                    obj = Emitter.makeEmitter(s2);
                        }
                    }
/* 280*/            if(((Emitter) (obj)).usesWriter())
                    {
/* 281*/                writer = getStreamWriter((StreamResult)result, properties);
/* 282*/                ((Emitter) (obj)).setWriter(writer);
                    } else
                    {
/* 284*/                outputStream = getOutputStream((StreamResult)result, properties);
/* 285*/                ((Emitter) (obj)).setOutputStream(outputStream);
                    }
                } else
                {
/* 289*/            throw new IllegalArgumentException("Unknown type of Result: " + result.getClass());
                }
/* 293*/        NamespaceEmitter namespaceemitter = new NamespaceEmitter();
/* 294*/        namespaceemitter.setUnderlyingEmitter(((Emitter) (obj)));
/* 295*/        obj = namespaceemitter;
/* 296*/        return ((Emitter) (obj));
            }

            private Writer getStreamWriter(StreamResult streamresult, Properties properties)
                throws TransformerException
            {
/* 306*/        closeAfterUse = false;
/* 307*/        Object obj = streamresult.getWriter();
/* 308*/        if(obj == null)
                {
/* 309*/            Object obj1 = streamresult.getOutputStream();
/* 310*/            if(obj1 == null)
                    {
/* 311*/                String s1 = streamresult.getSystemId();
/* 312*/                if(s1 == null)
                        {
/* 313*/                    obj1 = System.out;
                        } else
                        {
/* 315*/                    obj1 = makeFileOutputStream("", urlToFileName(s1), true);
/* 316*/                    closeAfterUse = true;
                        }
                    }
/* 320*/            com.icl.saxon.charcode.CharacterSet characterset = CharacterSetFactory.getCharacterSet(properties);
/* 322*/            String s2 = properties.getProperty("encoding");
/* 323*/            if(s2 == null)
/* 323*/                s2 = "UTF8";
/* 324*/            if(s2.equalsIgnoreCase("utf-8"))
/* 324*/                s2 = "UTF8";
/* 327*/            if(characterset instanceof PluggableCharacterSet)
/* 328*/                s2 = ((PluggableCharacterSet)characterset).getEncodingName();
/* 333*/            do
/* 333*/                try
                        {
/* 333*/                    obj = new BufferedWriter(new OutputStreamWriter(((OutputStream) (obj1)), s2));
/* 336*/                    break;
                        }
/* 338*/                catch(Exception exception)
                        {
/* 338*/                    if(s2.equalsIgnoreCase("UTF8"))
/* 339*/                        throw new TransformerException("Failed to create a UTF8 output writer");
/* 341*/                    System.err.println("Encoding " + s2 + " is not supported: using UTF8");
/* 342*/                    s2 = "UTF8";
/* 343*/                    UnicodeCharacterSet unicodecharacterset = new UnicodeCharacterSet();
/* 344*/                    properties.put("encoding", "utf-8");
                        }
/* 344*/            while(true);
                } else
/* 352*/        if(obj instanceof OutputStreamWriter)
                {
/* 353*/            String s = ((OutputStreamWriter)obj).getEncoding();
/* 355*/            properties.put("encoding", s);
                }
/* 358*/        return ((Writer) (obj));
            }

            private OutputStream getOutputStream(StreamResult streamresult, Properties properties)
                throws TransformerException
            {
/* 368*/        closeAfterUse = false;
/* 370*/        Object obj = streamresult.getOutputStream();
/* 371*/        if(obj == null)
                {
/* 372*/            String s = streamresult.getSystemId();
/* 373*/            if(s == null)
                    {
/* 374*/                obj = System.out;
                    } else
                    {
/* 376*/                obj = makeFileOutputStream("", urlToFileName(s), true);
/* 377*/                closeAfterUse = true;
                    }
                }
/* 381*/        if(obj == null)
/* 382*/            throw new TransformerException("This output method requires a binary output destination");
/* 385*/        else
/* 385*/            return ((OutputStream) (obj));
            }

            private void setEmitter(Emitter emitter)
            {
/* 394*/        super.emitter = emitter;
            }

            public void reset()
                throws TransformerException
            {
/* 398*/        if(pendingStartTag != -1)
/* 398*/            flushStartTag();
            }

            private void setOutputProperties(Properties properties)
                throws TransformerException
            {
/* 402*/        outputProperties = properties;
            }

            public Properties getOutputProperties()
            {
/* 406*/        return outputProperties;
            }

            public void write(String s)
                throws TransformerException
            {
/* 418*/        if(pendingStartTag != -1)
/* 418*/            flushStartTag();
/* 419*/        super.emitter.setEscaping(false);
/* 420*/        int i = s.length();
/* 421*/        if(i > charbuffer.length)
/* 422*/            charbuffer = new char[i];
/* 424*/        s.getChars(0, i, charbuffer, 0);
/* 425*/        super.emitter.characters(charbuffer, 0, i);
/* 426*/        super.emitter.setEscaping(true);
            }

            public void writeContent(String s)
                throws TransformerException
            {
/* 438*/        if(s == null)
/* 438*/            return;
/* 439*/        int i = s.length();
/* 440*/        if(i > charbuffer.length)
/* 441*/            charbuffer = new char[i];
/* 443*/        s.getChars(0, i, charbuffer, 0);
/* 444*/        writeContent(charbuffer, 0, i);
            }

            public void writeContent(char ac[], int i, int j)
                throws TransformerException
            {
/* 459*/        if(j == 0)
/* 459*/            return;
/* 460*/        if(pendingStartTag != -1)
/* 461*/            flushStartTag();
/* 463*/        super.emitter.characters(ac, i, j);
            }

            public void writeContent(StringBuffer stringbuffer, int i, int j)
                throws TransformerException
            {
/* 478*/        if(j == 0)
/* 478*/            return;
/* 479*/        if(pendingStartTag != -1)
/* 480*/            flushStartTag();
/* 482*/        if(j > charbuffer.length)
/* 483*/            charbuffer = new char[j];
/* 485*/        stringbuffer.getChars(i, i + j, charbuffer, 0);
/* 486*/        super.emitter.characters(charbuffer, 0, j);
            }

            public void writeStartTag(int i)
                throws TransformerException
            {
/* 499*/        if(i == -1)
                {
/* 500*/            suppressAttributes = true;
/* 501*/            return;
                }
/* 503*/        suppressAttributes = false;
/* 506*/        if(pendingStartTag != -1)
/* 506*/            flushStartTag();
/* 507*/        pendingAttList.clear();
/* 508*/        pendingNSListSize = 0;
/* 509*/        pendingStartTag = i;
            }

            public int checkAttributePrefix(int i)
                throws TransformerException
            {
/* 525*/        int j = namePool.allocateNamespaceCode(i);
/* 526*/        for(int k = 0; k < pendingNSListSize; k++)
/* 527*/            if(j >> 16 == pendingNSList[k] >> 16)
/* 529*/                if((j & 0xffff) == (pendingNSList[k] & 0xffff))
                        {
/* 531*/                    return i;
                        } else
                        {
/* 533*/                    String s = getSubstitutePrefix(j);
/* 534*/                    int l = namePool.allocate(s, namePool.getURI(i), namePool.getLocalName(i));
/* 538*/                    writeNamespaceDeclaration(namePool.allocateNamespaceCode(l));
/* 539*/                    return l;
                        }

/* 544*/        writeNamespaceDeclaration(j);
/* 545*/        return i;
            }

            public void writeNamespaceDeclaration(int i)
                throws TransformerException
            {
/* 562*/        if(suppressAttributes)
/* 562*/            return;
/* 565*/        if(pendingStartTag == -1)
/* 566*/            throw new TransformerException("Cannot write a namespace declaration when there is no open start tag");
/* 574*/        for(int j = 0; j < pendingNSListSize; j++)
/* 575*/            if(i >> 16 == pendingNSList[j] >> 16)
/* 577*/                return;

/* 583*/        if(pendingNSListSize + 1 > pendingNSList.length)
                {
/* 584*/            int ai[] = new int[pendingNSListSize * 2];
/* 585*/            System.arraycopy(pendingNSList, 0, ai, 0, pendingNSListSize);
/* 586*/            pendingNSList = ai;
                }
/* 588*/        pendingNSList[pendingNSListSize++] = i;
            }

            public void copyNamespaceNode(int i)
                throws TransformerException
            {
/* 597*/        if(pendingStartTag == -1)
/* 598*/            throw new TransformerException("Cannot copy a namespace node when there is no containing element node");
/* 600*/        if(pendingAttList.getLength() > 0)
/* 601*/            throw new TransformerException("Cannot copy a namespace node to an element after attributes have been added");
/* 605*/        for(int j = 0; j < pendingNSListSize; j++)
/* 606*/            if(i >> 16 == pendingNSList[j] >> 16)
/* 608*/                if(i == pendingNSList[j])
/* 609*/                    return;
/* 612*/                else
/* 612*/                    throw new TransformerException("Cannot create two namespace nodes with the same name");

/* 616*/        writeNamespaceDeclaration(i);
            }

            private String getSubstitutePrefix(int i)
            {
/* 627*/        String s = namePool.getPrefixFromNamespaceCode(i);
/* 628*/        return s + "." + (i & 0xffff);
            }

            public boolean thereIsAnOpenStartTag()
            {
/* 637*/        return pendingStartTag != -1;
            }

            public void writeAttribute(int i, String s)
                throws TransformerException
            {
/* 652*/        writeAttribute(i, s, false);
            }

            public void writeAttribute(int i, String s, boolean flag)
                throws TransformerException
            {
/* 672*/        if(suppressAttributes)
/* 672*/            return;
/* 675*/        if(pendingStartTag == -1)
                {
/* 676*/            throw new TransformerException("Cannot write an attribute when there is no open start tag");
                } else
                {
/* 679*/            pendingAttList.setAttribute(i, flag ? "NO-ESC" : "CDATA", s);
/* 683*/            return;
                }
            }

            public void writeEndTag(int i)
                throws TransformerException
            {
/* 693*/        if(pendingStartTag != -1)
/* 694*/            flushStartTag();
/* 698*/        super.emitter.endElement(i);
            }

            public void writeComment(String s)
                throws TransformerException
            {
/* 706*/        if(pendingStartTag != -1)
/* 706*/            flushStartTag();
/* 707*/        super.emitter.comment(s.toCharArray(), 0, s.length());
            }

            public void writePI(String s, String s1)
                throws TransformerException
            {
/* 715*/        if(pendingStartTag != -1)
/* 715*/            flushStartTag();
/* 716*/        super.emitter.processingInstruction(s, s1);
            }

            public void close()
                throws TransformerException
            {
/* 725*/        super.emitter.endDocument();
/* 726*/        if(closeAfterUse)
/* 728*/            try
                    {
/* 728*/                if(writer != null)
/* 729*/                    writer.close();
/* 731*/                if(outputStream != null)
/* 732*/                    outputStream.close();
                    }
/* 735*/            catch(IOException ioexception)
                    {
/* 735*/                throw new TransformerException(ioexception);
                    }
            }

            protected void flushStartTag()
                throws TransformerException
            {
/* 745*/        super.emitter.startElement(pendingStartTag, pendingAttList, pendingNSList, pendingNSListSize);
/* 747*/        pendingNSListSize = 0;
/* 748*/        pendingStartTag = -1;
            }
}
