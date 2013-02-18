// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SAXDriver.java

package com.icl.saxon.aelfred;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import org.xml.sax.*;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.NamespaceSupport;

// Referenced classes of package com.icl.saxon.aelfred:
//            DefaultHandler, XmlParser

public class SAXDriver
    implements Locator, Attributes, XMLReader, Parser, AttributeList
{
    private static class Adapter
        implements ContentHandler
    {

                private DocumentHandler docHandler;

                public void setDocumentLocator(Locator locator)
                {
/*1268*/            docHandler.setDocumentLocator(locator);
                }

                public void startDocument()
                    throws SAXException
                {
/*1271*/            docHandler.startDocument();
                }

                public void processingInstruction(String s, String s1)
                    throws SAXException
                {
/*1275*/            docHandler.processingInstruction(s, s1);
                }

                public void startPrefixMapping(String s, String s1)
                {
                }

                public void startElement(String s, String s1, String s2, Attributes attributes)
                    throws SAXException
                {
/*1286*/            docHandler.startElement(s2, (AttributeList)attributes);
                }

                public void characters(char ac[], int i, int j)
                    throws SAXException
                {
/*1292*/            docHandler.characters(ac, i, j);
                }

                public void ignorableWhitespace(char ac[], int i, int j)
                    throws SAXException
                {
/*1298*/            docHandler.ignorableWhitespace(ac, i, j);
                }

                public void skippedEntity(String s)
                {
                }

                public void endElement(String s, String s1, String s2)
                    throws SAXException
                {
/*1306*/            docHandler.endElement(s2);
                }

                public void endPrefixMapping(String s)
                {
                }

                public void endDocument()
                    throws SAXException
                {
/*1312*/            docHandler.endDocument();
                }

                Adapter(DocumentHandler documenthandler)
                {
/*1264*/            docHandler = documenthandler;
                }
    }


            private final DefaultHandler base = new DefaultHandler();
            private XmlParser parser;
            private EntityResolver entityResolver;
            private ContentHandler contentHandler;
            private DTDHandler dtdHandler;
            private ErrorHandler errorHandler;
            private DeclHandler declHandler;
            private LexicalHandler lexicalHandler;
            private String elementName;
            private Stack entityStack;
            private Vector attributeNames;
            private Vector attributeNamespaces;
            private Vector attributeLocalNames;
            private Vector attributeValues;
            private boolean namespaces;
            private boolean xmlNames;
            private boolean nspending;
            private int attributeCount;
            private String nsTemp[];
            private NamespaceSupport prefixStack;
            private Hashtable features;
            private Hashtable properties;
            static final String FEATURE = "http://xml.org/sax/features/";
            static final String HANDLER = "http://xml.org/sax/properties/";

            public SAXDriver()
            {
/* 122*/        entityResolver = base;
/* 123*/        contentHandler = base;
/* 124*/        dtdHandler = base;
/* 125*/        errorHandler = base;
/* 126*/        declHandler = base;
/* 127*/        lexicalHandler = base;
/* 129*/        elementName = null;
/* 130*/        entityStack = new Stack();
/* 132*/        attributeNames = new Vector();
/* 133*/        attributeNamespaces = new Vector();
/* 134*/        attributeLocalNames = new Vector();
/* 135*/        attributeValues = new Vector();
/* 137*/        namespaces = true;
/* 138*/        xmlNames = false;
/* 139*/        nspending = false;
/* 142*/        attributeCount = 0;
/* 143*/        nsTemp = new String[3];
/* 144*/        prefixStack = new NamespaceSupport();
            }

            public void setLocale(Locale locale)
                throws SAXException
            {
/* 170*/        if("en".equals(locale.getLanguage()))
/* 171*/            return;
/* 173*/        else
/* 173*/            throw new SAXException("AElfred only supports English locales.");
            }

            public EntityResolver getEntityResolver()
            {
/* 183*/        return entityResolver;
            }

            public void setEntityResolver(EntityResolver entityresolver)
            {
/* 192*/        if(entityresolver == null)
/* 193*/            entityresolver = base;
/* 194*/        entityResolver = entityresolver;
            }

            public DTDHandler getDTDHandler()
            {
/* 204*/        return dtdHandler;
            }

            public void setDTDHandler(DTDHandler dtdhandler)
            {
/* 213*/        if(dtdhandler == null)
/* 214*/            dtdhandler = base;
/* 215*/        dtdHandler = dtdhandler;
            }

            /**
             * @deprecated Method setDocumentHandler is deprecated
             */

            public void setDocumentHandler(DocumentHandler documenthandler)
            {
/* 233*/        contentHandler = new Adapter(documenthandler);
/* 234*/        xmlNames = true;
            }

            public ContentHandler getContentHandler()
            {
/* 243*/        return contentHandler;
            }

            public void setContentHandler(ContentHandler contenthandler)
            {
/* 254*/        if(contenthandler == null)
/* 255*/            contenthandler = base;
/* 256*/        contentHandler = contenthandler;
            }

            public void setErrorHandler(ErrorHandler errorhandler)
            {
/* 265*/        if(errorhandler == null)
/* 266*/            errorhandler = base;
/* 267*/        errorHandler = errorhandler;
            }

            public ErrorHandler getErrorHandler()
            {
/* 276*/        return errorHandler;
            }

            public void parse(InputSource inputsource)
                throws SAXException, IOException
            {
/* 299*/        synchronized(base)
                {
/* 300*/            parser = new XmlParser();
/* 301*/            parser.setHandler(this);
/* 304*/            try
                    {
/* 304*/                String s = inputsource.getSystemId();
/* 309*/                s = tryToExpand(s);
/* 315*/                if(s != null)
/* 316*/                    entityStack.push(s);
/* 318*/                else
/* 318*/                    entityStack.push("illegal:unknown system ID");
/* 320*/                parser.doParse(s, inputsource.getPublicId(), inputsource.getCharacterStream(), inputsource.getByteStream(), inputsource.getEncoding());
                    }
/* 326*/            catch(SAXException saxexception)
                    {
/* 326*/                throw saxexception;
                    }
/* 328*/            catch(IOException ioexception)
                    {
/* 328*/                throw ioexception;
                    }
/* 330*/            catch(RuntimeException runtimeexception)
                    {
/* 330*/                throw runtimeexception;
                    }
/* 332*/            catch(Exception exception)
                    {
/* 332*/                throw new SAXException(exception.getMessage(), exception);
                    }
/* 334*/            finally
                    {
/* 334*/                contentHandler.endDocument();
/* 335*/                entityStack.removeAllElements();
                    }
                }
            }

            public void parse(String s)
                throws SAXException, IOException
            {
/* 348*/        parse(new InputSource(s));
            }

            public boolean getFeature(String s)
                throws SAXNotRecognizedException
            {
/* 366*/        if("http://xml.org/sax/features/validation".equals(s))
/* 367*/            return false;
/* 370*/        if("http://xml.org/sax/features/external-general-entities".equals(s) || "http://xml.org/sax/features/external-parameter-entities".equals(s))
/* 373*/            return true;
/* 376*/        if("http://xml.org/sax/features/namespace-prefixes".equals(s))
/* 377*/            return xmlNames;
/* 380*/        if("http://xml.org/sax/features/namespaces".equals(s))
/* 381*/            return namespaces;
/* 386*/        if("http://xml.org/sax/features/string-interning".equals(s))
/* 387*/            return true;
/* 389*/        if(features != null && features.containsKey(s))
/* 390*/            return ((Boolean)features.get(s)).booleanValue();
/* 392*/        else
/* 392*/            throw new SAXNotRecognizedException(s);
            }

            public Object getProperty(String s)
                throws SAXNotRecognizedException
            {
/* 404*/        if("http://xml.org/sax/properties/declaration-handler".equals(s))
/* 405*/            return declHandler;
/* 407*/        if("http://xml.org/sax/properties/lexical-handler".equals(s))
/* 408*/            return lexicalHandler;
/* 410*/        if(properties != null && properties.containsKey(s))
/* 411*/            return properties.get(s);
/* 414*/        else
/* 414*/            throw new SAXNotRecognizedException(s);
            }

            public void setFeature(String s, boolean flag)
                throws SAXNotRecognizedException, SAXNotSupportedException
            {
/* 429*/        try
                {
/* 429*/            boolean flag1 = getFeature(s);
/* 431*/            if(flag == flag1)
/* 432*/                return;
/* 434*/            if("http://xml.org/sax/features/namespace-prefixes".equals(s))
                    {
/* 436*/                xmlNames = flag;
/* 437*/                return;
                    }
/* 440*/            if("http://xml.org/sax/features/namespaces".equals(s))
                    {
/* 443*/                namespaces = flag;
/* 444*/                return;
                    }
/* 450*/            if(features == null || !features.containsKey(s))
/* 451*/                throw new SAXNotSupportedException(s);
                }
/* 455*/        catch(SAXNotRecognizedException saxnotrecognizedexception)
                {
/* 455*/            if(features == null)
/* 456*/                features = new Hashtable(5);
                }
/* 460*/        features.put(s, flag ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)));
            }

            public void setProperty(String s, Object obj)
                throws SAXNotRecognizedException, SAXNotSupportedException
            {
/* 477*/        try
                {
/* 477*/            Object obj1 = getProperty(s);
/* 479*/            if("http://xml.org/sax/properties/declaration-handler".equals(s))
                    {
/* 480*/                if(obj == null)
                        {
/* 481*/                    declHandler = base;
                        } else
                        {
/* 482*/                    if(!(obj instanceof DeclHandler))
/* 483*/                        throw new SAXNotSupportedException(s);
/* 485*/                    declHandler = (DeclHandler)obj;
                        }
/* 486*/                return;
                    }
/* 489*/            if("http://xml.org/sax/properties/lexical-handler".equals(s) || "http://xml.org/sax/handlers/LexicalHandler".equals(s))
                    {
/* 492*/                if(obj == null)
                        {
/* 493*/                    lexicalHandler = base;
                        } else
                        {
/* 494*/                    if(!(obj instanceof LexicalHandler))
/* 495*/                        throw new SAXNotSupportedException(s);
/* 497*/                    lexicalHandler = (LexicalHandler)obj;
                        }
/* 498*/                return;
                    }
/* 502*/            if(properties == null || !properties.containsKey(s))
/* 503*/                throw new SAXNotSupportedException(s);
                }
/* 507*/        catch(SAXNotRecognizedException saxnotrecognizedexception)
                {
/* 507*/            if(properties == null)
/* 508*/                properties = new Hashtable(5);
                }
/* 512*/        properties.put(s, obj);
            }

            void startDocument()
                throws SAXException
            {
/* 528*/        contentHandler.setDocumentLocator(this);
/* 529*/        contentHandler.startDocument();
/* 530*/        attributeNames.removeAllElements();
/* 531*/        attributeValues.removeAllElements();
            }

            void endDocument()
                throws SAXException
            {
            }

            Object resolveEntity(String s, String s1)
                throws SAXException, IOException
            {
/* 543*/        InputSource inputsource = entityResolver.resolveEntity(s, s1);
/* 546*/        if(inputsource == null)
/* 547*/            return null;
/* 548*/        if(inputsource.getCharacterStream() != null)
/* 549*/            return inputsource.getCharacterStream();
/* 550*/        if(inputsource.getByteStream() != null)
                {
/* 551*/            if(inputsource.getEncoding() == null)
/* 552*/                return inputsource.getByteStream();
/* 554*/            try
                    {
/* 554*/                return new InputStreamReader(inputsource.getByteStream(), inputsource.getEncoding());
                    }
/* 559*/            catch(IOException ioexception)
                    {
/* 559*/                return inputsource.getByteStream();
                    }
                } else
                {
/* 562*/            String s2 = inputsource.getSystemId();
/* 563*/            return tryToExpand(s2);
                }
            }

            private String tryToExpand(String s)
            {
/* 575*/        if(s == null)
/* 576*/            s = "";
                String s2;
/* 579*/        try
                {
/* 579*/            URL url = new URL(s);
/* 580*/            return s;
                }
/* 582*/        catch(MalformedURLException malformedurlexception)
                {
/* 582*/            String s1 = System.getProperty("user.dir");
/* 583*/            if(s1.startsWith("/"))
/* 584*/                s1 = "file://" + s1;
/* 586*/            else
/* 586*/                s1 = "file:///" + s1;
/* 588*/            if(!s1.endsWith("/") && !s.startsWith("/"))
/* 589*/                s1 = s1 + "/";
/* 591*/            s2 = s1 + s;
                }
/* 593*/        try
                {
/* 593*/            URL url1 = new URL(s2);
/* 595*/            return s2;
                }
/* 598*/        catch(MalformedURLException malformedurlexception1)
                {
/* 598*/            return s;
                }
            }

            void startExternalEntity(String s)
                throws SAXException
            {
/* 606*/        entityStack.push(s);
            }

            void endExternalEntity(String s)
                throws SAXException
            {
/* 612*/        entityStack.pop();
            }

            void doctypeDecl(String s, String s1, String s2)
                throws SAXException
            {
/* 618*/        lexicalHandler.startDTD(s, s1, s2);
            }

            void endDoctype()
                throws SAXException
            {
/* 635*/        deliverDTDEvents();
/* 636*/        lexicalHandler.endDTD();
            }

            void attribute(String s, String s1, boolean flag)
                throws SAXException
            {
/* 649*/        if(attributeCount++ == 0 && namespaces)
/* 651*/            prefixStack.pushContext();
/* 657*/        if(s1 == null)
/* 661*/            return;
/* 664*/        if(namespaces && s.startsWith("xmlns"))
                {
/* 665*/            if(s.length() == 5)
                    {
/* 666*/                prefixStack.declarePrefix("", s1);
/* 668*/                contentHandler.startPrefixMapping("", s1);
                    } else
/* 670*/            if(s.charAt(5) == ':' && !s.equals("xmlns:xml"))
                    {
/* 672*/                if(s.length() == 6)
                        {
/* 673*/                    errorHandler.error(new SAXParseException("Missing namespace prefix in namespace declaration: " + s, this));
/* 676*/                    return;
                        }
/* 678*/                String s2 = s.substring(6);
/* 679*/                if(s1.length() == 0)
                        {
/* 680*/                    errorHandler.error(new SAXParseException("Missing URI in namespace declaration: " + s, this));
/* 683*/                    return;
                        }
/* 685*/                prefixStack.declarePrefix(s2, s1);
/* 687*/                contentHandler.startPrefixMapping(s2, s1);
                    }
/* 690*/            if(!xmlNames)
/* 693*/                return;
                }
/* 697*/        attributeNames.addElement(s);
/* 698*/        attributeValues.addElement(s1);
            }

            void startElement(String s)
                throws SAXException
            {
/* 704*/        ContentHandler contenthandler = contentHandler;
/* 706*/        if(attributeCount == 0)
/* 707*/            prefixStack.pushContext();
/* 710*/        elementName = s;
/* 711*/        if(namespaces)
                {
/* 714*/            if(attributeCount > 0)
                    {
/* 715*/                for(int i = 0; i < attributeNames.size(); i++)
                        {
/* 716*/                    String s1 = (String)attributeNames.elementAt(i);
/* 717*/                    if(s1.indexOf(':') > 0)
                            {
/* 718*/                        if(xmlNames && s1.startsWith("xmlns:"))
                                {
/* 719*/                            attributeNamespaces.addElement("");
/* 720*/                            attributeLocalNames.addElement(s1);
                                } else
/* 722*/                        if(prefixStack.processName(s1, nsTemp, true) == null)
                                {
/* 723*/                            errorHandler.error(new SAXParseException("undeclared name prefix in: " + s1, this));
/* 727*/                            attributeNamespaces.addElement("");
/* 728*/                            attributeLocalNames.addElement(s1.substring(s1.indexOf(':')));
                                } else
                                {
/* 730*/                            attributeNamespaces.addElement(nsTemp[0]);
/* 731*/                            attributeLocalNames.addElement(nsTemp[1]);
                                }
                            } else
                            {
/* 734*/                        attributeNamespaces.addElement("");
/* 735*/                        attributeLocalNames.addElement(s1);
                            }
/* 738*/                    for(int j = 0; j < i; j++)
/* 739*/                        if(attributeNamespaces.elementAt(i) == attributeNamespaces.elementAt(j) && attributeLocalNames.elementAt(i) == attributeLocalNames.elementAt(j))
/* 741*/                            errorHandler.error(new SAXParseException("duplicate attribute name: " + attributeLocalNames.elementAt(j), this));

                        }

                    }
/* 749*/            if(prefixStack.processName(s, nsTemp, false) == null)
                    {
/* 750*/                errorHandler.error(new SAXParseException("undeclared name prefix in: " + s, this));
/* 753*/                nsTemp[0] = nsTemp[1] = "";
/* 755*/                s = s.substring(s.indexOf(':'));
                    }
/* 757*/            contenthandler.startElement(nsTemp[0], nsTemp[1], s, this);
                } else
                {
/* 759*/            contenthandler.startElement("", "", s, this);
                }
/* 763*/        if(attributeCount != 0)
                {
/* 764*/            attributeNames.removeAllElements();
/* 765*/            attributeNamespaces.removeAllElements();
/* 766*/            attributeLocalNames.removeAllElements();
/* 767*/            attributeValues.removeAllElements();
/* 768*/            attributeCount = 0;
                }
/* 770*/        nspending = false;
            }

            void endElement(String s)
                throws SAXException
            {
/* 776*/        ContentHandler contenthandler = contentHandler;
/* 778*/        if(!namespaces)
                {
/* 779*/            contenthandler.endElement("", "", s);
/* 780*/            return;
                }
/* 784*/        if(prefixStack.processName(s, nsTemp, false) == null)
                {
/* 786*/            errorHandler.error(new SAXParseException("undeclared name prefix in: " + s, this));
/* 788*/            nsTemp[0] = nsTemp[1] = "";
/* 789*/            s = s.substring(s.indexOf(':'));
                }
/* 792*/        contenthandler.endElement(nsTemp[0], nsTemp[1], s);
/* 799*/        for(Enumeration enumeration = prefixStack.getDeclaredPrefixes(); enumeration.hasMoreElements(); contenthandler.endPrefixMapping((String)enumeration.nextElement()));
/* 803*/        prefixStack.popContext();
            }

            void startCDATA()
                throws SAXException
            {
/* 809*/        lexicalHandler.startCDATA();
            }

            void charData(char ac[], int i, int j)
                throws SAXException
            {
/* 815*/        contentHandler.characters(ac, i, j);
            }

            void endCDATA()
                throws SAXException
            {
/* 821*/        lexicalHandler.endCDATA();
            }

            void ignorableWhitespace(char ac[], int i, int j)
                throws SAXException
            {
/* 827*/        contentHandler.ignorableWhitespace(ac, i, j);
            }

            void processingInstruction(String s, String s1)
                throws SAXException
            {
/* 837*/        contentHandler.processingInstruction(s, s1);
            }

            void comment(char ac[], int i, int j)
                throws SAXException
            {
/* 847*/        if(lexicalHandler != base)
/* 848*/            lexicalHandler.comment(ac, i, j);
            }

            void error(String s, String s1, int i, int j)
                throws SAXException
            {
/* 857*/        SAXParseException saxparseexception = new SAXParseException(s, null, s1, i, j);
/* 858*/        errorHandler.fatalError(saxparseexception);
/* 861*/        throw saxparseexception;
            }

            private void deliverDTDEvents()
                throws SAXException
            {
/* 877*/        if(dtdHandler != base)
                {
                    String s2;
                    String s4;
                    String s7;
/* 878*/            for(Enumeration enumeration = parser.declaredNotations(); enumeration.hasMoreElements(); dtdHandler.notationDecl(s2, s4, s7))
                    {
/* 881*/                s2 = (String)enumeration.nextElement();
/* 882*/                s4 = parser.getNotationPublicId(s2);
/* 883*/                s7 = parser.getNotationSystemId(s2);
                    }

                }
/* 889*/        if(dtdHandler != base || declHandler != base)
                {
/* 890*/            for(Enumeration enumeration1 = parser.declaredEntities(); enumeration1.hasMoreElements();)
                    {
/* 894*/                String s = (String)enumeration1.nextElement();
/* 895*/                int i = parser.getEntityType(s);
/* 897*/                if(s.charAt(0) != '%')
/* 901*/                    if(i == 2)
                            {
/* 902*/                        String s5 = parser.getEntityPublicId(s);
/* 903*/                        String s8 = parser.getEntitySystemId(s);
/* 904*/                        String s3 = parser.getEntityNotationName(s);
/* 905*/                        dtdHandler.unparsedEntityDecl(s, s5, s8, s3);
                            } else
/* 910*/                    if(i == 3)
                            {
/* 911*/                        String s6 = parser.getEntityPublicId(s);
/* 912*/                        String s9 = parser.getEntitySystemId(s);
/* 913*/                        declHandler.externalEntityDecl(s, s6, s9);
                            } else
/* 918*/                    if(i == 1 && !"lt".equals(s) && !"gt".equals(s) && !"quot".equals(s) && !"apos".equals(s) && !"amp".equals(s))
/* 926*/                        declHandler.internalEntityDecl(s, parser.getEntityValue(s));
                    }

                }
/* 933*/        if(declHandler != base)
                {
/* 934*/            for(Enumeration enumeration2 = parser.declaredElements(); enumeration2.hasMoreElements();)
                    {
/* 938*/                String s10 = null;
/* 940*/                String s1 = (String)enumeration2.nextElement();
/* 941*/                switch(parser.getElementContentType(s1))
                        {
/* 943*/                case 1: // '\001'
/* 943*/                    s10 = "ANY";
                            break;

/* 946*/                case 2: // '\002'
/* 946*/                    s10 = "EMPTY";
                            break;

/* 950*/                case 3: // '\003'
/* 950*/                case 4: // '\004'
/* 950*/                    s10 = parser.getElementContentModel(s1);
                            break;

/* 954*/                case 0: // '\0'
/* 954*/                default:
/* 954*/                    s10 = null;
                            break;
                        }
/* 957*/                if(s10 != null)
/* 958*/                    declHandler.elementDecl(s1, s10);
                        String s11;
                        String s12;
                        String s13;
                        String s14;
/* 960*/                for(Enumeration enumeration3 = parser.declaredAttributes(s1); enumeration3 != null && enumeration3.hasMoreElements(); declHandler.attributeDecl(s1, s11, s12, s13, s14))
                        {
/* 962*/                    s11 = (String)enumeration3.nextElement();
/* 967*/                    switch(parser.getAttributeType(s1, s11))
                            {
/* 969*/                    case 1: // '\001'
/* 969*/                        s12 = "CDATA";
                                break;

/* 972*/                    case 5: // '\005'
/* 972*/                        s12 = "ENTITY";
                                break;

/* 975*/                    case 6: // '\006'
/* 975*/                        s12 = "ENTITIES";
                                break;

/* 978*/                    case 9: // '\t'
/* 978*/                        s12 = parser.getAttributeEnumeration(s1, s11);
                                break;

/* 981*/                    case 2: // '\002'
/* 981*/                        s12 = "ID";
                                break;

/* 984*/                    case 3: // '\003'
/* 984*/                        s12 = "IDREF";
                                break;

/* 987*/                    case 4: // '\004'
/* 987*/                        s12 = "IDREFS";
                                break;

/* 990*/                    case 7: // '\007'
/* 990*/                        s12 = "NMTOKEN";
                                break;

/* 993*/                    case 8: // '\b'
/* 993*/                        s12 = "NMTOKENS";
                                break;

/*1001*/                    case 10: // '\n'
/*1001*/                        s12 = "NOTATION";
                                break;

/*1005*/                    default:
/*1005*/                        errorHandler.fatalError(new SAXParseException("internal error, att type", this));
/*1007*/                        s12 = null;
                                break;
                            }
/*1010*/                    switch(parser.getAttributeDefaultValueType(s1, s11))
                            {
/*1013*/                    case 32: // ' '
/*1013*/                        s13 = "#IMPLIED";
                                break;

/*1016*/                    case 33: // '!'
/*1016*/                        s13 = "#REQUIRED";
                                break;

/*1019*/                    case 34: // '"'
/*1019*/                        s13 = "#FIXED";
                                break;

/*1022*/                    case 31: // '\037'
/*1022*/                        s13 = null;
                                break;

/*1026*/                    default:
/*1026*/                        errorHandler.fatalError(new SAXParseException("internal error, att default", this));
/*1028*/                        s13 = null;
                                break;
                            }
/*1031*/                    s14 = parser.getAttributeDefaultValue(s1, s11);
                        }

                    }

                }
            }

            public int getLength()
            {
/*1051*/        return attributeNames.size();
            }

            public String getURI(int i)
            {
/*1059*/        return (String)attributeNamespaces.elementAt(i);
            }

            public String getLocalName(int i)
            {
/*1067*/        return (String)attributeLocalNames.elementAt(i);
            }

            public String getQName(int i)
            {
/*1075*/        return (String)attributeNames.elementAt(i);
            }

            public String getName(int i)
            {
/*1083*/        return (String)attributeNames.elementAt(i);
            }

            public String getType(int i)
            {
/*1092*/        switch(parser.getAttributeType(elementName, getQName(i)))
                {
/*1096*/        case 0: // '\0'
/*1096*/        case 1: // '\001'
/*1096*/            return "CDATA";

/*1098*/        case 2: // '\002'
/*1098*/            return "ID";

/*1100*/        case 3: // '\003'
/*1100*/            return "IDREF";

/*1102*/        case 4: // '\004'
/*1102*/            return "IDREFS";

/*1104*/        case 5: // '\005'
/*1104*/            return "ENTITY";

/*1106*/        case 6: // '\006'
/*1106*/            return "ENTITIES";

/*1111*/        case 7: // '\007'
/*1111*/        case 9: // '\t'
/*1111*/            return "NMTOKEN";

/*1113*/        case 8: // '\b'
/*1113*/            return "NMTOKENS";

/*1117*/        case 10: // '\n'
/*1117*/            return "NOTATION";
                }
/*1121*/        return null;
            }

            public String getValue(int i)
            {
/*1131*/        return (String)attributeValues.elementAt(i);
            }

            public int getIndex(String s, String s1)
            {
/*1140*/        int i = getLength();
/*1142*/        for(int j = 0; j < i; j++)
/*1143*/            if(getURI(j).equals(s) && getLocalName(j).equals(s1))
/*1146*/                return j;

/*1148*/        return -1;
            }

            public int getIndex(String s)
            {
/*1157*/        int i = getLength();
/*1159*/        for(int j = 0; j < i; j++)
/*1160*/            if(getQName(j).equals(s))
/*1161*/                return j;

/*1163*/        return -1;
            }

            public String getType(String s, String s1)
            {
/*1172*/        int i = getIndex(s, s1);
/*1174*/        if(i < 0)
/*1175*/            return null;
/*1176*/        else
/*1176*/            return getType(i);
            }

            public String getType(String s)
            {
/*1186*/        int i = getIndex(s);
/*1188*/        if(i < 0)
/*1189*/            return null;
/*1190*/        else
/*1190*/            return getType(i);
            }

            public String getValue(String s, String s1)
            {
/*1199*/        int i = getIndex(s, s1);
/*1201*/        if(i < 0)
/*1202*/            return null;
/*1203*/        else
/*1203*/            return getValue(i);
            }

            public String getValue(String s)
            {
/*1213*/        int i = getIndex(s);
/*1215*/        if(i < 0)
/*1216*/            return null;
/*1217*/        else
/*1217*/            return getValue(i);
            }

            public String getPublicId()
            {
/*1230*/        return null;
            }

            public String getSystemId()
            {
/*1238*/        return (String)entityStack.peek();
            }

            public int getLineNumber()
            {
/*1246*/        return parser.getLineNumber();
            }

            public int getColumnNumber()
            {
/*1254*/        return parser.getColumnNumber();
            }
}
