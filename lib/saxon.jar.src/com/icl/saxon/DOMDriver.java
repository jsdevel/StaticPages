// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DOMDriver.java

package com.icl.saxon;

import java.util.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.*;

public class DOMDriver
    implements Locator, XMLReader
{

            protected ContentHandler contentHandler;
            private LexicalHandler lexicalHandler;
            private NamespaceSupport nsSupport;
            private AttributesImpl attlist;
            private String parts[];
            private String elparts[];
            private Hashtable nsDeclarations;
            protected Node root;
            protected String systemId;
            static final String FEATURE = "http://xml.org/sax/features/";
            static final String HANDLER = "http://xml.org/sax/properties/";

            public DOMDriver()
            {
/*  27*/        contentHandler = new DefaultHandler();
/*  28*/        lexicalHandler = null;
/*  29*/        nsSupport = new NamespaceSupport();
/*  30*/        attlist = new AttributesImpl();
/*  31*/        parts = new String[3];
/*  32*/        elparts = new String[3];
/*  33*/        nsDeclarations = new Hashtable();
/*  34*/        root = null;
            }

            public void setContentHandler(ContentHandler contenthandler)
            {
/*  45*/        contentHandler = contenthandler;
/*  46*/        if(contenthandler instanceof LexicalHandler)
/*  47*/            lexicalHandler = (LexicalHandler)contenthandler;
            }

            public ContentHandler getContentHandler()
            {
/*  52*/        return contentHandler;
            }

            public void setLocale(Locale locale)
                throws SAXException
            {
            }

            public EntityResolver getEntityResolver()
            {
/*  72*/        return null;
            }

            public void setEntityResolver(EntityResolver entityresolver)
            {
            }

            public DTDHandler getDTDHandler()
            {
/*  89*/        return null;
            }

            public void setDTDHandler(DTDHandler dtdhandler)
            {
            }

            /**
             * @deprecated Method setDocumentHandler is deprecated
             */

            public void setDocumentHandler(DocumentHandler documenthandler)
            {
            }

            public void setErrorHandler(ErrorHandler errorhandler)
            {
            }

            public ErrorHandler getErrorHandler()
            {
/* 126*/        return null;
            }

            public void setStartNode(Node node)
            {
/* 137*/        root = node;
            }

            public void parse(InputSource inputsource)
                throws SAXException
            {
/* 146*/        parse();
            }

            public void parse(String s)
                throws SAXException
            {
/* 155*/        parse();
            }

            public void parse()
                throws SAXException
            {
/* 165*/        if(root == null)
/* 166*/            throw new SAXException("DOMDriver: no start node defined");
/* 168*/        if(contentHandler == null)
/* 169*/            throw new SAXException("DOMDriver: no content handler defined");
/* 172*/        contentHandler.setDocumentLocator(this);
/* 173*/        contentHandler.startDocument();
/* 174*/        if(root.getNodeType() == 1)
/* 175*/            sendElement((Element)root);
/* 177*/        else
/* 177*/            walkNode(root);
/* 179*/        contentHandler.endDocument();
            }

            private void sendElement(Element element)
                throws SAXException
            {
/* 189*/        Element element1 = element;
/* 190*/        NamedNodeMap namednodemap = gatherNamespaces(element1, false);
/* 192*/        do
                {
/* 192*/            gatherNamespaces(element1, true);
/* 193*/            Node node = element1.getParentNode();
/* 194*/            if(node.getNodeType() == 1)
                    {
/* 195*/                element1 = (Element)node;
                    } else
                    {
/* 200*/                outputElement(element, namednodemap);
/* 201*/                return;
                    }
                } while(true);
            }

            private void walkNode(Node node)
                throws SAXException
            {
/* 212*/        if(node.hasChildNodes())
                {
/* 213*/            NodeList nodelist = node.getChildNodes();
/* 214*/            for(int i = 0; i < nodelist.getLength(); i++)
                    {
/* 215*/                Node node1 = nodelist.item(i);
/* 216*/                switch(node1.getNodeType())
                        {
/* 212*/                case 2: // '\002'
/* 212*/                case 6: // '\006'
/* 212*/                case 9: // '\t'
/* 212*/                default:
                            break;

/* 220*/                case 1: // '\001'
/* 220*/                    Element element = (Element)node1;
/* 221*/                    nsSupport.pushContext();
/* 222*/                    attlist.clear();
/* 223*/                    nsDeclarations.clear();
/* 234*/                    try
                            {
/* 234*/                        String s = element.getPrefix();
/* 235*/                        String s1 = element.getNamespaceURI();
/* 237*/                        if(nsDeclarations.get(s) == null)
                                {
/* 238*/                            nsSupport.declarePrefix(s, s1);
/* 239*/                            contentHandler.startPrefixMapping(s, s1);
/* 240*/                            nsDeclarations.put(s, s1);
                                }
                            }
/* 242*/                    catch(Throwable throwable) { }
/* 247*/                    NamedNodeMap namednodemap = element.getAttributes();
/* 248*/                    for(int j = 0; j < namednodemap.getLength(); j++)
                            {
/* 249*/                        Attr attr = (Attr)namednodemap.item(j);
/* 250*/                        String s2 = attr.getName();
/* 251*/                        if(s2.equals("xmlns"))
                                {
/* 253*/                            if(nsDeclarations.get("") == null)
                                    {
/* 254*/                                String s3 = attr.getValue();
/* 255*/                                nsSupport.declarePrefix("", s3);
/* 256*/                                contentHandler.startPrefixMapping("", s3);
/* 257*/                                nsDeclarations.put("", s3);
                                    }
                                } else
/* 259*/                        if(s2.startsWith("xmlns:"))
                                {
/* 261*/                            String s4 = s2.substring(6);
/* 262*/                            if(nsDeclarations.get(s4) == null)
                                    {
/* 263*/                                String s8 = attr.getValue();
/* 264*/                                nsSupport.declarePrefix(s4, s8);
/* 265*/                                contentHandler.startPrefixMapping(s4, s8);
/* 266*/                                nsDeclarations.put(s4, s8);
                                    }
                                } else
/* 268*/                        if(s2.indexOf(':') >= 0)
/* 270*/                            try
                                    {
/* 270*/                                String s5 = attr.getPrefix();
/* 271*/                                String s9 = attr.getNamespaceURI();
/* 273*/                                if(nsDeclarations.get(s5) == null)
                                        {
/* 274*/                                    nsSupport.declarePrefix(s5, s9);
/* 275*/                                    contentHandler.startPrefixMapping(s5, s9);
/* 276*/                                    nsDeclarations.put(s5, s9);
                                        }
                                    }
/* 278*/                            catch(Throwable throwable1) { }
                            }

/* 283*/                    for(int k = 0; k < namednodemap.getLength(); k++)
                            {
/* 284*/                        Attr attr1 = (Attr)namednodemap.item(k);
/* 285*/                        String s6 = attr1.getName();
/* 286*/                        if(!s6.equals("xmlns") && !s6.startsWith("xmlns:"))
                                {
/* 288*/                            String as1[] = nsSupport.processName(s6, parts, true);
/* 289*/                            if(as1 == null)
/* 290*/                                throw new SAXException("Undeclared namespace in " + s6);
/* 292*/                            attlist.addAttribute(as1[0], as1[1], as1[2], "CDATA", attr1.getValue());
                                }
                            }

/* 296*/                    String as[] = nsSupport.processName(element.getTagName(), elparts, false);
/* 297*/                    if(as == null)
/* 298*/                        throw new SAXException("Undeclared namespace in " + element.getTagName());
/* 300*/                    String s7 = as[0];
/* 301*/                    String s10 = as[1];
/* 302*/                    String s11 = as[2];
/* 304*/                    contentHandler.startElement(s7, s10, s11, attlist);
/* 306*/                    walkNode(((Node) (element)));
/* 308*/                    contentHandler.endElement(s7, s10, s11);
                            String s12;
/* 309*/                    for(Enumeration enumeration = nsSupport.getDeclaredPrefixes(); enumeration.hasMoreElements(); contentHandler.endPrefixMapping(s12))
/* 311*/                        s12 = (String)enumeration.nextElement();

/* 314*/                    nsSupport.popContext();
/* 315*/                    break;

/* 320*/                case 7: // '\007'
/* 320*/                    contentHandler.processingInstruction(((ProcessingInstruction)node1).getTarget(), ((ProcessingInstruction)node1).getData());
/* 323*/                    break;

/* 325*/                case 8: // '\b'
/* 325*/                    if(lexicalHandler == null)
/* 326*/                        break;
/* 326*/                    String s13 = ((Comment)node1).getData();
/* 327*/                    if(s13 != null)
/* 328*/                        lexicalHandler.comment(s13.toCharArray(), 0, s13.length());
/* 328*/                    break;

/* 334*/                case 3: // '\003'
/* 334*/                case 4: // '\004'
/* 334*/                    String s14 = ((CharacterData)node1).getData();
/* 335*/                    if(s14 != null)
/* 336*/                        contentHandler.characters(s14.toCharArray(), 0, s14.length());
                            break;

/* 340*/                case 5: // '\005'
/* 340*/                    walkNode(node1);
                            break;
                        }
                    }

                }
            }

            private void outputElement(Element element, NamedNodeMap namednodemap)
                throws SAXException
            {
/* 351*/        String as[] = nsSupport.processName(element.getTagName(), elparts, false);
/* 352*/        if(as == null)
/* 353*/            throw new SAXException("Undeclared namespace in " + element.getTagName());
/* 355*/        String s = as[0];
/* 356*/        String s1 = as[1];
/* 357*/        String s2 = as[2];
/* 359*/        for(int i = 0; i < namednodemap.getLength(); i++)
                {
/* 360*/            Attr attr = (Attr)namednodemap.item(i);
/* 361*/            String s3 = attr.getName();
/* 362*/            if(!s3.equals("xmlns") && !s3.startsWith("xmlns:"))
                    {
/* 364*/                String as1[] = nsSupport.processName(s3, parts, true);
/* 365*/                if(as1 == null)
/* 366*/                    throw new SAXException("Undeclared namespace in " + s3);
/* 368*/                attlist.addAttribute(as1[0], as1[1], as1[2], "CDATA", attr.getValue());
                    }
                }

/* 373*/        contentHandler.startElement(s, s1, s2, attlist);
/* 375*/        walkNode(element);
/* 377*/        contentHandler.endElement(s, s1, s2);
                String s4;
/* 378*/        for(Enumeration enumeration = nsSupport.getDeclaredPrefixes(); enumeration.hasMoreElements(); contentHandler.endPrefixMapping(s4))
/* 380*/            s4 = (String)enumeration.nextElement();

            }

            private NamedNodeMap gatherNamespaces(Element element, boolean flag)
            {
/* 386*/        if(!flag)
                {
/* 387*/            nsSupport.pushContext();
/* 388*/            attlist.clear();
/* 389*/            nsDeclarations.clear();
                }
/* 398*/        try
                {
/* 398*/            String s = element.getPrefix();
/* 399*/            String s1 = element.getNamespaceURI();
/* 400*/            if(s == null)
/* 400*/                s = "";
/* 401*/            if(s1 == null)
/* 401*/                s1 = "";
/* 403*/            if(nsDeclarations.get(s) == null)
                    {
/* 404*/                nsSupport.declarePrefix(s, s1);
/* 405*/                nsDeclarations.put(s, s1);
                    }
                }
/* 407*/        catch(Throwable throwable) { }
/* 411*/        NamedNodeMap namednodemap = element.getAttributes();
/* 412*/        for(int i = 0; i < namednodemap.getLength(); i++)
                {
/* 413*/            Attr attr = (Attr)namednodemap.item(i);
/* 414*/            String s2 = attr.getName();
/* 415*/            if(s2.equals("xmlns"))
                    {
/* 417*/                if(nsDeclarations.get("") == null)
                        {
/* 418*/                    String s3 = attr.getValue();
/* 419*/                    nsSupport.declarePrefix("", s3);
/* 420*/                    nsDeclarations.put("", s3);
                        }
                    } else
/* 422*/            if(s2.startsWith("xmlns:"))
                    {
/* 424*/                String s4 = s2.substring(6);
/* 425*/                if(nsDeclarations.get(s4) == null)
                        {
/* 426*/                    String s6 = attr.getValue();
/* 427*/                    nsSupport.declarePrefix(s4, s6);
/* 428*/                    nsDeclarations.put(s4, s6);
                        }
                    } else
/* 430*/            if(s2.indexOf(':') >= 0)
/* 432*/                try
                        {
/* 432*/                    String s5 = attr.getPrefix();
/* 433*/                    String s7 = attr.getNamespaceURI();
/* 435*/                    if(nsDeclarations.get(s5) == null)
                            {
/* 436*/                        nsSupport.declarePrefix(s5, s7);
/* 438*/                        nsDeclarations.put(s5, s7);
                            }
                        }
/* 440*/                catch(Throwable throwable1) { }
                }

/* 445*/        return namednodemap;
            }

            public void setSystemId(String s)
            {
/* 453*/        systemId = s;
            }

            public String getPublicId()
            {
/* 458*/        return null;
            }

            public String getSystemId()
            {
/* 463*/        return systemId;
            }

            public int getLineNumber()
            {
/* 468*/        return -1;
            }

            public int getColumnNumber()
            {
/* 473*/        return -1;
            }

            public boolean getFeature(String s)
                throws SAXNotRecognizedException
            {
/* 490*/        if("http://xml.org/sax/features/validation".equals(s))
/* 491*/            return false;
/* 494*/        if("http://xml.org/sax/features/external-general-entities".equals(s) || "http://xml.org/sax/features/external-parameter-entities".equals(s))
/* 496*/            return false;
/* 499*/        if("http://xml.org/sax/features/namespace-prefixes".equals(s))
/* 500*/            return false;
/* 503*/        if("http://xml.org/sax/features/namespaces".equals(s))
/* 504*/            return true;
/* 507*/        if("http://xml.org/sax/features/string-interning".equals(s))
/* 508*/            return false;
/* 510*/        else
/* 510*/            throw new SAXNotRecognizedException(s);
            }

            public Object getProperty(String s)
                throws SAXNotRecognizedException
            {
/* 521*/        if(s.equals("http://xml.org/sax/properties/lexical-handler"))
/* 522*/            return lexicalHandler;
/* 524*/        else
/* 524*/            throw new SAXNotRecognizedException(s);
            }

            public void setFeature(String s, boolean flag)
                throws SAXNotRecognizedException, SAXNotSupportedException
            {
/* 536*/        if("http://xml.org/sax/features/validation".equals(s))
/* 537*/            if(flag)
/* 538*/                throw new SAXNotSupportedException(s + " feature cannot be switched on");
/* 540*/            else
/* 540*/                return;
/* 544*/        if("http://xml.org/sax/features/external-general-entities".equals(s) || "http://xml.org/sax/features/external-parameter-entities".equals(s))
/* 546*/            if(flag)
/* 547*/                throw new SAXNotSupportedException(s + " feature cannot be switched on");
/* 549*/            else
/* 549*/                return;
/* 553*/        if("http://xml.org/sax/features/namespace-prefixes".equals(s))
/* 554*/            if(flag)
/* 555*/                throw new SAXNotSupportedException(s + " feature cannot be switched on");
/* 557*/            else
/* 557*/                return;
/* 561*/        if("http://xml.org/sax/features/namespaces".equals(s))
/* 562*/            if(!flag)
/* 563*/                throw new SAXNotSupportedException(s + " feature cannot be switched off");
/* 565*/            else
/* 565*/                return;
/* 569*/        if("http://xml.org/sax/features/string-interning".equals(s))
                {
/* 570*/            if(flag)
/* 571*/                throw new SAXNotSupportedException(s + " feature cannot be switched on");
/* 573*/            else
/* 573*/                return;
                } else
                {
/* 576*/            throw new SAXNotRecognizedException("Feature not recognized: " + s);
                }
            }

            public void setProperty(String s, Object obj)
                throws SAXNotRecognizedException, SAXNotSupportedException
            {
/* 586*/        if(s.equals("http://xml.org/sax/properties/lexical-handler"))
                {
/* 587*/            if(obj instanceof LexicalHandler)
/* 588*/                lexicalHandler = (LexicalHandler)obj;
/* 590*/            else
/* 590*/                throw new SAXNotSupportedException("Lexical Handler must be instance of org.xml.sax.ext.LexicalHandler");
                } else
                {
/* 594*/            throw new SAXNotRecognizedException(s);
                }
            }
}
