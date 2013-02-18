// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   IdentityTransformer.java

package com.icl.saxon;

import com.icl.saxon.output.GeneralOutputter;
import com.icl.saxon.output.Outputter;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.*;
import org.xml.sax.ext.LexicalHandler;

// Referenced classes of package com.icl.saxon:
//            Controller, ContentEmitter, TransformerFactoryImpl

class IdentityTransformer extends Controller
{

            protected IdentityTransformer(TransformerFactoryImpl transformerfactoryimpl)
            {
/*  20*/        super(transformerfactoryimpl);
            }

            public void transform(Source source, Result result)
                throws TransformerException
            {
/*  29*/        SAXSource saxsource = getTransformerFactory().getSAXSource(source, false);
/*  30*/        XMLReader xmlreader = saxsource.getXMLReader();
/*  33*/        try
                {
/*  33*/            xmlreader.setFeature("http://xml.org/sax/features/namespaces", true);
/*  34*/            xmlreader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
                }
/*  36*/        catch(SAXNotSupportedException saxnotsupportedexception)
                {
/*  36*/            throw new TransformerException("The SAX2 parser does not recognize a required namespace feature");
                }
/*  39*/        catch(SAXNotRecognizedException saxnotrecognizedexception)
                {
/*  39*/            throw new TransformerException("The SAX2 parser does not support a required namespace feature");
                }
/*  43*/        if(result instanceof SAXResult)
                {
/*  48*/            org.xml.sax.ContentHandler contenthandler = ((SAXResult)result).getHandler();
/*  49*/            xmlreader.setContentHandler(contenthandler);
/*  50*/            if(contenthandler instanceof LexicalHandler)
/*  53*/                try
                        {
/*  53*/                    xmlreader.setProperty("http://xml.org/sax/properties/lexical-handler", contenthandler);
                        }
/*  54*/                catch(SAXNotSupportedException saxnotsupportedexception1) { }
/*  55*/                catch(SAXNotRecognizedException saxnotrecognizedexception1) { }
/*  59*/            try
                    {
/*  59*/                xmlreader.parse(saxsource.getInputSource());
                    }
/*  61*/            catch(Exception exception)
                    {
/*  61*/                throw new TransformerException(exception);
                    }
                } else
                {
/*  72*/            com.icl.saxon.om.NamePool namepool = getNamePool();
/*  73*/            java.util.Properties properties = getOutputProperties();
/*  74*/            GeneralOutputter generaloutputter = new GeneralOutputter(namepool);
/*  75*/            generaloutputter.setOutputDestination(properties, result);
/*  76*/            com.icl.saxon.output.Emitter emitter = generaloutputter.getEmitter();
/*  77*/            ContentEmitter contentemitter = new ContentEmitter();
/*  78*/            ((ContentEmitter)contentemitter).setNamePool(namepool);
/*  79*/            ((ContentEmitter)contentemitter).setEmitter(emitter);
/*  82*/            try
                    {
/*  82*/                xmlreader.setContentHandler(contentemitter);
/*  85*/                try
                        {
/*  85*/                    xmlreader.setProperty("http://xml.org/sax/properties/lexical-handler", contentemitter);
                        }
/*  86*/                catch(SAXNotSupportedException saxnotsupportedexception2) { }
/*  87*/                catch(SAXNotRecognizedException saxnotrecognizedexception2) { }
/*  90*/                xmlreader.parse(saxsource.getInputSource());
                    }
/*  92*/            catch(Exception exception1)
                    {
/*  92*/                throw new TransformerException(exception1);
                    }
/*  95*/            generaloutputter.close();
                }
            }
}
