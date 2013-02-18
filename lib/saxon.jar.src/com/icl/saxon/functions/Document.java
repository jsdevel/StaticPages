// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Document.java

package com.icl.saxon.functions;

import com.icl.saxon.*;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;

public class Document extends Function
{

            private Controller boundController;

            public Document()
            {
/*  21*/        boundController = null;
            }

            public String getName()
            {
/*  24*/        return "document";
            }

            public int getDataType()
            {
/*  33*/        return 4;
            }

            public Expression simplify()
                throws XPathException
            {
/*  41*/        int i = checkArgumentCount(1, 2);
/*  42*/        super.argument[0] = super.argument[0].simplify();
/*  43*/        if(i == 2)
/*  44*/            super.argument[1] = super.argument[1].simplify();
/*  51*/        return this;
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  60*/        int i = getNumberOfArguments();
/*  62*/        Value value = super.argument[0].evaluate(context);
/*  63*/        NodeSetValue nodesetvalue = null;
/*  64*/        if(i == 2)
/*  65*/            nodesetvalue = super.argument[1].evaluateAsNodeSet(context);
/*  68*/        String s = getStaticContext().getBaseURI();
/*  70*/        return getDocuments(value, nodesetvalue, s, context);
            }

            public NodeSetValue getDocuments(Value value, NodeSetValue nodesetvalue, String s, Context context)
                throws XPathException
            {
/*  91*/        if((value instanceof NodeSetValue) && !(value instanceof FragmentValue) && !(value instanceof TextFragmentValue))
                {
/*  94*/            NodeEnumeration nodeenumeration = ((NodeSetValue)value).enumerate();
/*  95*/            NodeSetExtent nodesetextent = new NodeSetExtent(context.getController());
/*  98*/            while(nodeenumeration.hasMoreElements()) 
                    {
/*  98*/                NodeInfo nodeinfo2 = nodeenumeration.nextElement();
                        String s1;
/*  99*/                if(nodesetvalue == null)
                        {
/* 100*/                    s1 = nodeinfo2.getBaseURI();
                        } else
                        {
/* 102*/                    NodeInfo nodeinfo3 = nodesetvalue.getFirst();
/* 103*/                    if(nodeinfo3 == null)
/* 105*/                        throw new XPathException("Second argument to document() is empty node-set");
/* 108*/                    s1 = nodeinfo3.getBaseURI();
                        }
/* 111*/                NodeInfo nodeinfo4 = makeDoc(nodeinfo2.getStringValue(), s1, context);
/* 112*/                if(nodeinfo4 != null)
/* 113*/                    nodesetextent.append(nodeinfo4);
                    }
/* 116*/            return nodesetextent;
                }
                String s2;
/* 120*/        if(nodesetvalue == null)
                {
/* 121*/            s2 = s;
                } else
                {
/* 123*/            NodeInfo nodeinfo = nodesetvalue.getFirst();
/* 124*/            if(nodeinfo == null)
/* 127*/                s2 = null;
/* 129*/            else
/* 129*/                s2 = nodeinfo.getBaseURI();
                }
/* 133*/        String s3 = value.asString();
/* 134*/        NodeInfo nodeinfo1 = makeDoc(s3, s2, context);
/* 135*/        return new SingletonNodeSet(nodeinfo1);
            }

            private NodeInfo makeDoc(String s, String s1, Context context)
                throws XPathException
            {
/* 147*/        int i = s.indexOf('#');
/* 149*/        String s2 = null;
/* 150*/        if(i >= 0)
/* 151*/            if(i == s.length() - 1)
                    {
/* 153*/                s = s.substring(0, i);
                    } else
                    {
/* 155*/                s2 = s.substring(i + 1);
/* 156*/                s = s.substring(0, i);
                    }
                String s3;
/* 164*/        if(s1 == null)
/* 167*/            try
                    {
/* 167*/                s3 = (new URL(s)).toString();
                    }
/* 170*/            catch(MalformedURLException malformedurlexception)
                    {
/* 170*/                s3 = s1 + "/" + s;
/* 171*/                s1 = "";
                    }
/* 175*/        else
/* 175*/            try
                    {
/* 175*/                URL url = new URL(new URL(s1), s);
/* 176*/                s3 = url.toString();
                    }
/* 178*/            catch(MalformedURLException malformedurlexception1)
                    {
/* 178*/                s3 = s1 + "/../" + s;
                    }
/* 182*/        Controller controller = boundController;
/* 183*/        if(controller == null)
/* 184*/            controller = context.getController();
/* 187*/        if(controller == null)
/* 188*/            throw new XPathException("Internal error: no controller available for document() function");
/* 194*/        DocumentInfo documentinfo = controller.getDocumentPool().find(s3);
/* 195*/        if(documentinfo != null)
/* 195*/            return getFragment(documentinfo, s2);
/* 200*/        try
                {
/* 200*/            URIResolver uriresolver = controller.getURIResolver();
/* 201*/            javax.xml.transform.Source source = uriresolver.resolve(s, s1);
/* 205*/            if(source == null)
                    {
/* 206*/                URIResolver uriresolver1 = controller.getStandardURIResolver();
/* 207*/                source = uriresolver1.resolve(s, s1);
                    }
/* 210*/            DocumentInfo documentinfo1 = null;
/* 211*/            if(source instanceof DocumentInfo)
                    {
/* 212*/                documentinfo1 = (DocumentInfo)source;
                    } else
                    {
/* 214*/                if(source instanceof DOMSource)
                        {
/* 215*/                    DOMSource domsource = (DOMSource)source;
/* 216*/                    if(domsource.getNode() instanceof DocumentInfo)
/* 219*/                        documentinfo1 = (DocumentInfo)domsource.getNode();
                        }
/* 222*/                if(documentinfo1 == null)
                        {
/* 224*/                    javax.xml.transform.sax.SAXSource saxsource = controller.getTransformerFactory().getSAXSource(source, false);
/* 227*/                    Builder builder = controller.makeBuilder();
/* 228*/                    documentinfo1 = builder.build(saxsource);
                        }
                    }
/* 233*/            controller.getDocumentPool().add(documentinfo1, s3);
/* 235*/            return getFragment(documentinfo1, s2);
                }
/* 238*/        catch(TransformerException transformerexception)
                {
/* 239*/            try
                    {
/* 239*/                controller.reportRecoverableError(transformerexception);
                    }
/* 241*/            catch(TransformerException transformerexception1)
                    {
/* 241*/                throw new XPathException(transformerexception);
                    }
                }
/* 243*/        return null;
            }

            private NodeInfo getFragment(DocumentInfo documentinfo, String s)
            {
/* 256*/        if(s == null)
/* 257*/            return documentinfo;
/* 259*/        else
/* 259*/            return documentinfo.selectID(s);
            }

            public int getDependencies()
            {
/* 269*/        int i = super.argument[0].getDependencies();
/* 270*/        if(getNumberOfArguments() == 2)
/* 271*/            i |= super.argument[1].getDependencies();
/* 273*/        if(boundController == null)
/* 274*/            return i | 0x40;
/* 276*/        else
/* 276*/            return i;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/* 287*/        Document document = new Document();
/* 288*/        document.addArgument(super.argument[0].reduce(i, context));
/* 289*/        if(getNumberOfArguments() == 2)
/* 290*/            document.addArgument(super.argument[1].reduce(i, context));
/* 292*/        document.setStaticContext(getStaticContext());
/* 294*/        if(boundController == null && (i & 0x40) != 0)
/* 295*/            document.boundController = context.getController();
/* 298*/        if(document.getDependencies() == 0)
/* 299*/            return document.evaluate(context);
/* 302*/        else
/* 302*/            return document;
            }
}
