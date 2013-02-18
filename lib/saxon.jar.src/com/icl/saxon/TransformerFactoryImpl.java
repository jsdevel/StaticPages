// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TransformerFactoryImpl.java

package com.icl.saxon;

import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.trace.TraceListener;
import java.io.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.*;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.*;
import org.xml.sax.helpers.ParserAdapter;

// Referenced classes of package com.icl.saxon:
//            StandardURIResolver, StandardErrorListener, IdentityTransformer, PreparedStyleSheet, 
//            TreeDriver, DOMDriver, PIGrabber, Controller, 
//            TransformerHandlerImpl, IdentityTransformerHandler, TemplatesHandlerImpl, Filter, 
//            Loader

public class TransformerFactoryImpl extends SAXTransformerFactory
{

            private URIResolver resolver;
            private ErrorListener listener;
            private int treeModel;
            private boolean lineNumbering;
            private TraceListener traceListener;
            private int recoveryPolicy;
            private String messageEmitterClass;
            private String sourceParserClass;
            private String styleParserClass;
            private boolean timing;
            private boolean allowExternalFunctions;

            public TransformerFactoryImpl()
            {
/*  38*/        resolver = new StandardURIResolver(this);
/*  39*/        listener = new StandardErrorListener();
/*  40*/        treeModel = 1;
/*  41*/        lineNumbering = false;
/*  42*/        traceListener = null;
/*  43*/        recoveryPolicy = 1;
/*  44*/        messageEmitterClass = "com.icl.saxon.output.MessageEmitter";
/*  47*/        timing = false;
/*  48*/        allowExternalFunctions = true;
            }

            public Transformer newTransformer(Source source)
                throws TransformerConfigurationException
            {
/*  72*/        Templates templates = newTemplates(source);
/*  73*/        Transformer transformer = templates.newTransformer();
/*  76*/        return transformer;
            }

            public Transformer newTransformer()
                throws TransformerConfigurationException
            {
/*  94*/        return new IdentityTransformer(this);
            }

            public Templates newTemplates(Source source)
                throws TransformerConfigurationException
            {
/* 118*/        PreparedStyleSheet preparedstylesheet = new PreparedStyleSheet(this);
/* 119*/        SAXSource saxsource = getSAXSource(source, true);
/* 120*/        preparedstylesheet.prepare(saxsource);
/* 121*/        return preparedstylesheet;
            }

            public SAXSource getSAXSource(Source source, boolean flag)
            {
/* 133*/        if(source instanceof SAXSource)
/* 134*/            if(((SAXSource)source).getXMLReader() == null)
                    {
/* 135*/                SAXSource saxsource = new SAXSource();
/* 136*/                saxsource.setInputSource(((SAXSource)source).getInputSource());
/* 137*/                saxsource.setSystemId(source.getSystemId());
/* 138*/                saxsource.setXMLReader(flag ? getStyleParser() : getSourceParser());
/* 139*/                return saxsource;
                    } else
                    {
/* 141*/                return (SAXSource)source;
                    }
/* 152*/        if(source instanceof DOMSource)
                {
/* 153*/            InputSource inputsource = new InputSource("dummy");
/* 154*/            Node node = ((DOMSource)source).getNode();
                    Document document;
/* 156*/            if(node instanceof Document)
/* 157*/                document = (Document)node;
/* 159*/            else
/* 159*/                document = node.getOwnerDocument();
                    Object obj;
/* 162*/            if(node instanceof NodeInfo)
/* 163*/                obj = new TreeDriver();
/* 165*/            else
/* 165*/                obj = new DOMDriver();
/* 167*/            ((DOMDriver) (obj)).setStartNode(document);
/* 168*/            inputsource.setSystemId(source.getSystemId());
/* 169*/            ((DOMDriver) (obj)).setSystemId(source.getSystemId());
/* 170*/            return new SAXSource(((XMLReader) (obj)), inputsource);
                }
/* 171*/        if(source instanceof StreamSource)
                {
/* 172*/            StreamSource streamsource = (StreamSource)source;
/* 181*/            String s = source.getSystemId();
/* 186*/            InputSource inputsource1 = new InputSource(s);
/* 187*/            inputsource1.setCharacterStream(streamsource.getReader());
/* 188*/            inputsource1.setByteStream(streamsource.getInputStream());
/* 189*/            return new SAXSource(flag ? getStyleParser() : getSourceParser(), inputsource1);
                } else
                {
/* 193*/            throw new IllegalArgumentException("Unknown type of source");
                }
            }

            public Source getAssociatedStylesheet(Source source, String s, String s1, String s2)
                throws TransformerConfigurationException
            {
/* 223*/        PIGrabber pigrabber = new PIGrabber();
/* 224*/        pigrabber.setCriteria(s, s1, s2);
/* 225*/        pigrabber.setBaseURI(source.getSystemId());
/* 226*/        pigrabber.setURIResolver(resolver);
/* 228*/        SAXSource saxsource = getSAXSource(source, false);
/* 229*/        XMLReader xmlreader = saxsource.getXMLReader();
/* 231*/        xmlreader.setContentHandler(pigrabber);
/* 233*/        try
                {
/* 233*/            xmlreader.parse(saxsource.getInputSource());
                }
/* 235*/        catch(SAXException saxexception)
                {
/* 235*/            if(!saxexception.getMessage().equals("#start#"))
                    {
/* 239*/                System.err.println("Failed while looking for xml-stylesheet PI");
/* 240*/                System.err.println(saxexception.getMessage());
/* 241*/                if(saxexception.getException() != null)
/* 242*/                    saxexception.getException().printStackTrace();
/* 244*/                if(saxexception instanceof SAXParseException)
                        {
/* 245*/                    SAXParseException saxparseexception = (SAXParseException)saxexception;
/* 246*/                    System.err.println("At line " + saxparseexception.getLineNumber() + " in " + saxparseexception.getSystemId());
                        }
/* 248*/                throw new TransformerConfigurationException(saxexception);
                    }
                }
/* 251*/        catch(IOException ioexception)
                {
/* 251*/            System.err.println(ioexception.getMessage());
/* 252*/            throw new TransformerConfigurationException("XML parsing failure while looking for <?xml-stylesheet?>");
                }
/* 256*/        try
                {
/* 256*/            SAXSource asaxsource[] = pigrabber.getAssociatedStylesheets();
/* 257*/            if(asaxsource == null)
/* 258*/                throw new TransformerConfigurationException("No matching <?xml-stylesheet?> processing instruction found");
/* 261*/            else
/* 261*/                return compositeStylesheet(asaxsource);
                }
/* 263*/        catch(TransformerException transformerexception)
                {
/* 263*/            if(transformerexception instanceof TransformerConfigurationException)
/* 264*/                throw (TransformerConfigurationException)transformerexception;
/* 266*/            else
/* 266*/                throw new TransformerConfigurationException(transformerexception);
                }
            }

            public Source compositeStylesheet(SAXSource asaxsource[])
                throws TransformerConfigurationException
            {
/* 285*/        if(asaxsource.length == 1)
/* 286*/            return asaxsource[0];
/* 287*/        if(asaxsource.length == 0)
/* 288*/            throw new TransformerConfigurationException("No stylesheets were supplied");
/* 294*/        StringBuffer stringbuffer = new StringBuffer();
/* 295*/        stringbuffer.append("<xsl:stylesheet version='1.0' ");
/* 296*/        stringbuffer.append(" xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>");
/* 297*/        for(int i = 0; i < asaxsource.length; i++)
/* 298*/            stringbuffer.append("<xsl:import href='" + asaxsource[i].getInputSource().getSystemId() + "'/>");

/* 300*/        stringbuffer.append("</xsl:stylesheet>");
/* 301*/        InputSource inputsource = new InputSource();
/* 302*/        inputsource.setCharacterStream(new StringReader(stringbuffer.toString()));
/* 303*/        return new SAXSource(getSourceParser(), inputsource);
            }

            public void setURIResolver(URIResolver uriresolver)
            {
/* 315*/        resolver = uriresolver;
            }

            public URIResolver getURIResolver()
            {
/* 326*/        return resolver;
            }

            public void setFeature(String s, boolean flag)
                throws TransformerConfigurationException
            {
/* 370*/        if(s.equals("http://javax.xml.XMLConstants/feature/secure-processing") && !flag)
/* 371*/            return;
/* 373*/        else
/* 373*/            throw new TransformerConfigurationException("Unsupported feature: " + s);
            }

            public boolean getFeature(String s)
            {
/* 385*/        if(s.equals("http://javax.xml.transform.sax.SAXSource/feature"))
/* 385*/            return true;
/* 386*/        if(s.equals("http://javax.xml.transform.sax.SAXResult/feature"))
/* 386*/            return true;
/* 387*/        if(s.equals("http://javax.xml.transform.dom.DOMSource/feature"))
/* 387*/            return true;
/* 388*/        if(s.equals("http://javax.xml.transform.dom.DOMResult/feature"))
/* 388*/            return true;
/* 389*/        if(s.equals("http://javax.xml.transform.stream.StreamSource/feature"))
/* 389*/            return true;
/* 390*/        if(s.equals("http://javax.xml.transform.stream.StreamResult/feature"))
/* 390*/            return true;
/* 391*/        if(s.equals("http://javax.xml.transform.sax.SAXTransformerFactory/feature"))
/* 391*/            return true;
/* 392*/        if(s.equals("http://javax.xml.transform.sax.SAXTransformerFactory/feature/xmlfilter"))
/* 392*/            return true;
/* 393*/        else
/* 393*/            throw new IllegalArgumentException("Unknown feature " + s);
            }

            public void setAttribute(String s, Object obj)
                throws IllegalArgumentException
            {
/* 410*/        if(s.equals("http://icl.com/saxon/feature/treeModel"))
                {
/* 411*/            if(!(obj instanceof Integer))
/* 412*/                throw new IllegalArgumentException("Tree model must be an Integer");
/* 414*/            treeModel = ((Integer)obj).intValue();
                } else
/* 416*/        if(s.equals("http://icl.com/saxon/feature/allow-external-functions"))
                {
/* 417*/            if(!(obj instanceof Boolean))
/* 418*/                throw new IllegalArgumentException("allow-external-functions must be a boolean");
/* 420*/            allowExternalFunctions = ((Boolean)obj).booleanValue();
                } else
/* 422*/        if(s.equals("http://icl.com/saxon/feature/timing"))
                {
/* 423*/            if(!(obj instanceof Boolean))
/* 424*/                throw new IllegalArgumentException("Timing must be a boolean");
/* 426*/            timing = ((Boolean)obj).booleanValue();
                } else
/* 428*/        if(s.equals("http://icl.com/saxon/feature/traceListener"))
                {
/* 429*/            if(!(obj instanceof TraceListener))
/* 430*/                throw new IllegalArgumentException("Trace listener is of wrong class");
/* 432*/            traceListener = (TraceListener)obj;
                } else
/* 434*/        if(s.equals("http://icl.com/saxon/feature/linenumbering"))
                {
/* 435*/            if(!(obj instanceof Boolean))
/* 436*/                throw new IllegalArgumentException("Line Numbering value must be Boolean");
/* 438*/            lineNumbering = ((Boolean)obj).booleanValue();
                } else
/* 440*/        if(s.equals("http://icl.com/saxon/feature/recoveryPolicy"))
                {
/* 441*/            if(!(obj instanceof Integer))
/* 442*/                throw new IllegalArgumentException("Recovery Policy value must be Integer");
/* 444*/            recoveryPolicy = ((Integer)obj).intValue();
                } else
/* 446*/        if(s.equals("http://icl.com/saxon/feature/messageEmitterClass"))
                {
/* 447*/            if(!(obj instanceof String))
/* 448*/                throw new IllegalArgumentException("Message Emitter class must be a String");
/* 450*/            messageEmitterClass = (String)obj;
                } else
/* 452*/        if(s.equals("http://icl.com/saxon/feature/sourceParserClass"))
                {
/* 453*/            if(!(obj instanceof String))
/* 454*/                throw new IllegalArgumentException("Source Parser class must be a String");
/* 456*/            sourceParserClass = (String)obj;
                } else
/* 458*/        if(s.equals("http://icl.com/saxon/feature/styleParserClass"))
                {
/* 459*/            if(!(obj instanceof String))
/* 460*/                throw new IllegalArgumentException("Style Parser class must be a String");
/* 462*/            styleParserClass = (String)obj;
                } else
                {
/* 465*/            throw new IllegalArgumentException("Unknown attribute " + s);
                }
            }

            public Object getAttribute(String s)
                throws IllegalArgumentException
            {
/* 479*/        if(s.equals("http://icl.com/saxon/feature/treeModel"))
/* 480*/            return new Integer(treeModel);
/* 482*/        if(s.equals("http://icl.com/saxon/feature/timing"))
/* 483*/            return new Boolean(timing);
/* 485*/        if(s.equals("http://icl.com/saxon/feature/allow-external-functions"))
/* 486*/            return new Boolean(allowExternalFunctions);
/* 488*/        if(s.equals("http://icl.com/saxon/feature/traceListener"))
/* 489*/            return traceListener;
/* 491*/        if(s.equals("http://icl.com/saxon/feature/linenumbering"))
/* 492*/            return new Boolean(lineNumbering);
/* 494*/        if(s.equals("http://icl.com/saxon/feature/recoveryPolicy"))
/* 495*/            return new Integer(recoveryPolicy);
/* 497*/        if(s.equals("http://icl.com/saxon/feature/messageEmitterClass"))
/* 498*/            return messageEmitterClass;
/* 500*/        if(s.equals("http://icl.com/saxon/feature/sourceParserClass"))
/* 501*/            return sourceParserClass;
/* 503*/        if(s.equals("http://icl.com/saxon/feature/styleParserClass"))
/* 504*/            return styleParserClass;
/* 507*/        else
/* 507*/            throw new IllegalArgumentException("Unknown attribute " + s);
            }

            public void setErrorListener(ErrorListener errorlistener)
                throws IllegalArgumentException
            {
/* 522*/        listener = errorlistener;
            }

            public ErrorListener getErrorListener()
            {
/* 531*/        return listener;
            }

            public XMLReader getSourceParser()
                throws TransformerFactoryConfigurationError
            {
/* 539*/        if(sourceParserClass != null)
/* 540*/            return makeParser(sourceParserClass);
/* 543*/        try
                {
/* 543*/            return SAXParserFactory.newInstance().newSAXParser().getXMLReader();
                }
/* 545*/        catch(Exception exception)
                {
/* 545*/            throw new TransformerFactoryConfigurationError(exception);
                }
            }

            public XMLReader getStyleParser()
                throws TransformerFactoryConfigurationError
            {
/* 555*/        if(styleParserClass != null)
/* 556*/            return makeParser(styleParserClass);
/* 559*/        try
                {
/* 559*/            return SAXParserFactory.newInstance().newSAXParser().getXMLReader();
                }
/* 561*/        catch(Exception exception)
                {
/* 561*/            throw new TransformerFactoryConfigurationError(exception);
                }
            }

            public static XMLReader makeParser(String s)
                throws TransformerFactoryConfigurationError
            {
                Object obj;
/* 585*/        try
                {
/* 585*/            obj = Loader.getInstance(s);
                }
/* 587*/        catch(TransformerException transformerexception)
                {
/* 587*/            throw new TransformerFactoryConfigurationError(transformerexception);
                }
/* 589*/        if(obj instanceof XMLReader)
/* 590*/            return (XMLReader)obj;
/* 592*/        if(obj instanceof Parser)
/* 593*/            return new ParserAdapter((Parser)obj);
/* 595*/        else
/* 595*/            throw new TransformerFactoryConfigurationError("Class " + s + " is neither a SAX1 Parser nor a SAX2 XMLReader");
            }

            public TransformerHandler newTransformerHandler(Source source)
                throws TransformerConfigurationException
            {
/* 618*/        Templates templates = newTemplates(source);
/* 619*/        return newTransformerHandler(templates);
            }

            public TransformerHandler newTransformerHandler(Templates templates)
                throws TransformerConfigurationException
            {
/* 636*/        if(!(templates instanceof PreparedStyleSheet))
/* 637*/            throw new TransformerConfigurationException("Templates object was not created by Saxon");
/* 639*/        Controller controller = (Controller)templates.newTransformer();
/* 640*/        if(controller.usesPreviewMode())
                {
/* 641*/            throw new TransformerConfigurationException("Preview mode is not available with a TransformerHandler");
                } else
                {
/* 643*/            TransformerHandlerImpl transformerhandlerimpl = new TransformerHandlerImpl(controller);
/* 644*/            return transformerhandlerimpl;
                }
            }

            public TransformerHandler newTransformerHandler()
                throws TransformerConfigurationException
            {
/* 662*/        IdentityTransformer identitytransformer = new IdentityTransformer(this);
/* 663*/        return new IdentityTransformerHandler(identitytransformer);
            }

            public TemplatesHandler newTemplatesHandler()
                throws TransformerConfigurationException
            {
/* 679*/        return new TemplatesHandlerImpl(this);
            }

            public XMLFilter newXMLFilter(Source source)
                throws TransformerConfigurationException
            {
/* 696*/        Templates templates = newTemplates(source);
/* 697*/        return newXMLFilter(templates);
            }

            public XMLFilter newXMLFilter(Templates templates)
                throws TransformerConfigurationException
            {
/* 713*/        if(!(templates instanceof PreparedStyleSheet))
                {
/* 714*/            throw new TransformerConfigurationException("Supplied Templates object was not created using Saxon");
                } else
                {
/* 716*/            Controller controller = (Controller)templates.newTransformer();
/* 717*/            return new Filter(controller);
                }
            }
}
