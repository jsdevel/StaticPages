// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Filter.java

package com.icl.saxon;

import com.icl.saxon.output.ContentHandlerProxy;
import java.io.IOException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;

// Referenced classes of package com.icl.saxon:
//            Controller

public class Filter
    implements XMLFilter
{

            Controller controller;
            XMLReader parser;
            ContentHandler contentHandler;
            LexicalHandler lexicalHandler;

            protected Filter(Controller controller1)
            {
/*  42*/        controller = controller1;
            }

            public void setParent(XMLReader xmlreader)
            {
/*  61*/        parser = xmlreader;
            }

            public XMLReader getParent()
            {
/*  76*/        return parser;
            }

            public boolean getFeature(String s)
                throws SAXNotRecognizedException, SAXNotSupportedException
            {
/* 109*/        if(s.equals("http://xml.org/sax/features/namespaces"))
/* 110*/            return true;
/* 111*/        if(s.equals("http://xml.org/sax/features/namespace-prefixes"))
/* 112*/            return false;
/* 114*/        else
/* 114*/            throw new SAXNotRecognizedException(s);
            }

            public void setFeature(String s, boolean flag)
                throws SAXNotRecognizedException, SAXNotSupportedException
            {
/* 149*/        if(s.equals("http://xml.org/sax/features/namespaces"))
                {
/* 150*/            if(!flag)
/* 151*/                throw new SAXNotSupportedException(s);
                } else
/* 153*/        if(s.equals("http://xml.org/sax/features/namespace-prefixes"))
                {
/* 154*/            if(flag)
/* 155*/                throw new SAXNotSupportedException(s);
                } else
                {
/* 158*/            throw new SAXNotRecognizedException(s);
                }
            }

            public Object getProperty(String s)
                throws SAXNotRecognizedException, SAXNotSupportedException
            {
/* 193*/        if(s.equals("http://xml.org/sax/properties/lexical-handler"))
/* 194*/            return lexicalHandler;
/* 196*/        else
/* 196*/            throw new SAXNotRecognizedException(s);
            }

            public void setProperty(String s, Object obj)
                throws SAXNotRecognizedException, SAXNotSupportedException
            {
/* 232*/        if(s.equals("http://xml.org/sax/properties/lexical-handler"))
                {
/* 233*/            if(obj instanceof LexicalHandler)
/* 234*/                lexicalHandler = (LexicalHandler)obj;
/* 236*/            else
/* 236*/                throw new SAXNotSupportedException("Lexical Handler must be instance of org.xml.sax.ext.LexicalHandler");
                } else
                {
/* 240*/            throw new SAXNotRecognizedException(s);
                }
            }

            public void setContentHandler(ContentHandler contenthandler)
            {
/* 251*/        contentHandler = contenthandler;
/* 252*/        if((contenthandler instanceof LexicalHandler) && lexicalHandler == null)
/* 253*/            lexicalHandler = (LexicalHandler)contenthandler;
            }

            public ContentHandler getContentHandler()
            {
/* 262*/        return contentHandler;
            }

            public void setEntityResolver(EntityResolver entityresolver)
            {
            }

            public EntityResolver getEntityResolver()
            {
/* 296*/        return null;
            }

            public void setDTDHandler(DTDHandler dtdhandler)
            {
            }

            public DTDHandler getDTDHandler()
            {
/* 330*/        return null;
            }

            public void setErrorHandler(ErrorHandler errorhandler)
            {
            }

            public ErrorHandler getErrorHandler()
            {
/* 366*/        return null;
            }

            public void parse(InputSource inputsource)
                throws IOException, SAXException
            {
/* 388*/        if(parser == null)
/* 390*/            try
                    {
/* 390*/                parser = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
                    }
/* 392*/            catch(Exception exception)
                    {
/* 392*/                throw new SAXException(exception);
                    }
/* 395*/        SAXSource saxsource = new SAXSource();
/* 396*/        saxsource.setInputSource(inputsource);
/* 397*/        saxsource.setXMLReader(parser);
/* 398*/        ContentHandlerProxy contenthandlerproxy = new ContentHandlerProxy();
/* 399*/        contenthandlerproxy.setUnderlyingContentHandler(contentHandler);
/* 400*/        if(lexicalHandler != null)
/* 401*/            contenthandlerproxy.setLexicalHandler(lexicalHandler);
/* 404*/        try
                {
/* 404*/            controller.transform(saxsource, contenthandlerproxy);
                }
/* 406*/        catch(TransformerException transformerexception)
                {
/* 406*/            Throwable throwable = transformerexception.getException();
/* 407*/            if(throwable != null && (throwable instanceof SAXException))
/* 408*/                throw (SAXException)throwable;
/* 409*/            if(throwable != null && (throwable instanceof IOException))
/* 410*/                throw (IOException)throwable;
/* 412*/            else
/* 412*/                throw new SAXException(transformerexception);
                }
            }

            public void parse(String s)
                throws IOException, SAXException
            {
/* 443*/        InputSource inputsource = new InputSource(s);
/* 444*/        parse(inputsource);
            }
}
