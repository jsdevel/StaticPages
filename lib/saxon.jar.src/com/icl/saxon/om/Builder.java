// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Builder.java

package com.icl.saxon.om;

import com.icl.saxon.*;
import com.icl.saxon.output.Emitter;
import com.icl.saxon.output.ProxyEmitter;
import java.io.*;
import java.util.Date;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import org.xml.sax.*;

// Referenced classes of package com.icl.saxon.om:
//            Stripper, DocumentInfo

public abstract class Builder extends Emitter
    implements ErrorHandler, Locator, SourceLocator
{

            public static final int STANDARD_TREE = 0;
            public static final int TINY_TREE = 1;
            protected int estimatedLength;
            protected Writer errorOutput;
            protected Stripper stripper;
            protected PreviewManager previewManager;
            protected boolean discardComments;
            protected DocumentInfo currentDocument;
            protected ErrorHandler errorHandler;
            protected ErrorListener errorListener;
            protected boolean failed;
            protected boolean started;
            protected boolean timing;
            protected boolean inDTD;
            protected boolean lineNumbering;
            protected int lineNumber;
            protected int columnNumber;
            private long startTime;
            protected Controller controller;

            public void setController(Controller controller1)
            {
/*  62*/        controller = controller1;
            }

            public Builder()
            {
/*  38*/        errorOutput = new PrintWriter(System.err);
/*  41*/        previewManager = null;
/*  45*/        errorHandler = this;
/*  46*/        errorListener = null;
/*  48*/        failed = false;
/*  49*/        started = false;
/*  50*/        timing = false;
/*  52*/        inDTD = false;
/*  53*/        lineNumbering = false;
/*  54*/        lineNumber = -1;
/*  55*/        columnNumber = -1;
            }

            public void setRootNode(DocumentInfo documentinfo)
            {
/*  83*/        currentDocument = documentinfo;
            }

            public void setTiming(boolean flag)
            {
/*  92*/        timing = flag;
            }

            public boolean isTiming()
            {
/* 100*/        return timing;
            }

            public void setLineNumbering(boolean flag)
            {
/* 108*/        lineNumbering = flag;
            }

            public void setStripper(Stripper stripper1)
            {
/* 116*/        stripper = stripper1;
            }

            public Stripper getStripper()
            {
/* 124*/        return stripper;
            }

            public void setPreviewManager(PreviewManager previewmanager)
            {
/* 132*/        previewManager = previewmanager;
            }

            public void setDiscardCommentsAndPIs(boolean flag)
            {
/* 143*/        discardComments = flag;
            }

            public void setErrorHandler(ErrorHandler errorhandler)
            {
/* 154*/        errorHandler = errorhandler;
            }

            public void setErrorListener(ErrorListener errorlistener)
            {
/* 164*/        errorListener = errorlistener;
            }

            public void setErrorOutput(Writer writer)
            {
/* 179*/        errorOutput = writer;
            }

            public DocumentInfo build(SAXSource saxsource)
                throws TransformerException
            {
/* 195*/        InputSource inputsource = saxsource.getInputSource();
/* 196*/        XMLReader xmlreader = saxsource.getXMLReader();
/* 199*/        if(timing)
                {
/* 200*/            System.err.println("Building tree for " + inputsource.getSystemId() + " using " + getClass());
/* 201*/            startTime = (new Date()).getTime();
                }
/* 204*/        failed = true;
/* 205*/        started = false;
/* 206*/        if(saxsource.getSystemId() != null)
/* 207*/            setSystemId(saxsource.getSystemId());
/* 209*/        else
/* 209*/            setSystemId(inputsource.getSystemId());
/* 212*/        if(inputsource instanceof ExtendedInputSource)
                {
/* 213*/            estimatedLength = ((ExtendedInputSource)inputsource).getEstimatedLength();
/* 214*/            if(estimatedLength < 1)
/* 214*/                estimatedLength = 4096;
/* 215*/            if(estimatedLength > 0xf4240)
/* 215*/                estimatedLength = 0xf4240;
                } else
                {
/* 217*/            estimatedLength = 4096;
                }
/* 222*/        if(xmlreader == null)
/* 224*/            try
                    {
/* 224*/                xmlreader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
                    }
/* 227*/            catch(Exception exception)
                    {
/* 227*/                throw new TransformerException(exception);
                    }
/* 231*/        ContentEmitter contentemitter = new ContentEmitter();
/* 232*/        contentemitter.setNamePool(super.namePool);
/* 233*/        xmlreader.setContentHandler(contentemitter);
/* 234*/        xmlreader.setDTDHandler(contentemitter);
/* 235*/        xmlreader.setErrorHandler(errorHandler);
/* 237*/        if(!discardComments)
/* 239*/            try
                    {
/* 239*/                xmlreader.setProperty("http://xml.org/sax/properties/lexical-handler", contentemitter);
                    }
/* 240*/            catch(SAXNotSupportedException saxnotsupportedexception) { }
/* 241*/            catch(SAXNotRecognizedException saxnotrecognizedexception) { }
/* 245*/        if(stripper != null)
                {
/* 246*/            contentemitter.setEmitter(stripper);
/* 247*/            stripper.setUnderlyingEmitter(this);
                } else
                {
/* 249*/            contentemitter.setEmitter(this);
                }
/* 253*/        try
                {
/* 253*/            xmlreader.setFeature("http://xml.org/sax/features/namespaces", true);
/* 254*/            xmlreader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
                }
/* 256*/        catch(SAXNotSupportedException saxnotsupportedexception1)
                {
/* 256*/            throw new TransformerException("The SAX2 parser does not recognize a required namespace feature");
                }
/* 259*/        catch(SAXNotRecognizedException saxnotrecognizedexception1)
                {
/* 259*/            throw new TransformerException("The SAX2 parser does not support a required namespace feature");
                }
/* 265*/        try
                {
/* 265*/            xmlreader.parse(inputsource);
                }
/* 267*/        catch(IOException ioexception)
                {
/* 267*/            throw new TransformerException("Failure reading " + inputsource.getSystemId(), ioexception);
                }
/* 269*/        catch(SAXException saxexception)
                {
/* 269*/            Exception exception1 = saxexception.getException();
/* 270*/            if(exception1 != null && (exception1 instanceof TransformerException))
/* 271*/                throw (TransformerException)exception1;
/* 273*/            else
/* 273*/                throw new TransformerException(saxexception);
                }
/* 277*/        if(!started)
/* 279*/            throw new TransformerException("Source document not supplied");
/* 282*/        if(failed)
/* 284*/            throw new TransformerException("XML Parsing failed");
/* 287*/        if(timing)
                {
/* 288*/            long l = (new Date()).getTime();
/* 289*/            System.err.println("Tree built in " + (l - startTime) + " milliseconds");
/* 290*/            startTime = l;
                }
/* 293*/        return currentDocument;
            }

            public DocumentInfo getCurrentDocument()
            {
/* 302*/        return currentDocument;
            }

            public void warning(SAXParseException saxparseexception)
            {
/* 315*/        if(errorListener != null)
/* 317*/            try
                    {
/* 317*/                errorListener.warning(new TransformerException(saxparseexception));
                    }
/* 318*/            catch(Exception exception) { }
            }

            public void error(SAXParseException saxparseexception)
                throws SAXException
            {
/* 327*/        reportError(saxparseexception, false);
/* 328*/        failed = true;
            }

            public void fatalError(SAXParseException saxparseexception)
                throws SAXException
            {
/* 336*/        reportError(saxparseexception, true);
/* 337*/        failed = true;
/* 338*/        throw saxparseexception;
            }

            protected void reportError(SAXParseException saxparseexception, boolean flag)
            {
/* 347*/        if(errorListener != null)
/* 349*/            try
                    {
/* 349*/                super.systemId = saxparseexception.getSystemId();
/* 350*/                lineNumber = saxparseexception.getLineNumber();
/* 351*/                columnNumber = saxparseexception.getColumnNumber();
/* 352*/                TransformerException transformerexception = new TransformerException("Error reported by XML parser", this, saxparseexception);
/* 354*/                if(flag)
/* 355*/                    errorListener.fatalError(transformerexception);
/* 357*/                else
/* 357*/                    errorListener.error(transformerexception);
                    }
/* 359*/            catch(Exception exception) { }
/* 363*/        else
/* 363*/            try
                    {
/* 363*/                String s = flag ? "Fatal error" : "Error";
/* 364*/                errorOutput.write(s + " reported by XML parser: " + saxparseexception.getMessage() + "\n");
/* 365*/                errorOutput.write("  URL:    " + saxparseexception.getSystemId() + "\n");
/* 366*/                errorOutput.write("  Line:   " + saxparseexception.getLineNumber() + "\n");
/* 367*/                errorOutput.write("  Column: " + saxparseexception.getColumnNumber() + "\n");
/* 368*/                errorOutput.flush();
                    }
/* 370*/            catch(Exception exception1)
                    {
/* 370*/                System.err.println(saxparseexception);
/* 371*/                System.err.println(exception1);
/* 372*/                exception1.printStackTrace();
                    }
            }

            public abstract void setUnparsedEntity(String s, String s1);

            public String getPublicId()
            {
/* 400*/        return null;
            }

            public int getLineNumber()
            {
/* 404*/        return lineNumber;
            }

            public int getColumnNumber()
            {
/* 408*/        return columnNumber;
            }
}
