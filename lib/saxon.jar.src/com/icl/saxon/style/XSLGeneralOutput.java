// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLGeneralOutput.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.om.*;
import com.icl.saxon.output.Emitter;
import com.icl.saxon.tree.*;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.sax.TransformerHandler;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames

abstract class XSLGeneralOutput extends StyleElement
{

            Expression href;
            Expression userData;
            Expression method;
            Expression version;
            Expression indent;
            Expression encoding;
            Expression mediaType;
            Expression doctypeSystem;
            Expression doctypePublic;
            Expression omitDeclaration;
            Expression standalone;
            Expression cdataElements;
            Expression omitMetaTag;
            Expression nextInChain;
            Expression representation;
            Expression indentSpaces;
            Expression requireWellFormed;
            Hashtable userAttributes;
            Emitter handler;

            XSLGeneralOutput()
            {
/*  28*/        href = null;
/*  29*/        userData = null;
/*  30*/        method = null;
/*  31*/        version = null;
/*  32*/        indent = null;
/*  33*/        encoding = null;
/*  34*/        mediaType = null;
/*  35*/        doctypeSystem = null;
/*  36*/        doctypePublic = null;
/*  37*/        omitDeclaration = null;
/*  38*/        standalone = null;
/*  39*/        cdataElements = null;
/*  40*/        omitMetaTag = null;
/*  41*/        nextInChain = null;
/*  42*/        representation = null;
/*  43*/        indentSpaces = null;
/*  44*/        requireWellFormed = null;
/*  45*/        userAttributes = null;
/*  47*/        handler = null;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  51*/        StandardNames standardnames = getStandardNames();
/*  52*/        AttributeCollection attributecollection = getAttributeList();
/*  54*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  55*/            int j = attributecollection.getNameCode(i);
/*  56*/            int k = j & 0xfffff;
/*  57*/            if(k == standardnames.HREF)
/*  58*/                href = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  59*/            else
/*  59*/            if(k == standardnames.METHOD)
/*  60*/                method = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  61*/            else
/*  61*/            if(k == standardnames.VERSION)
/*  62*/                version = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  63*/            else
/*  63*/            if(k == standardnames.ENCODING)
/*  64*/                encoding = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  65*/            else
/*  65*/            if(k == standardnames.OMIT_XML_DECLARATION)
/*  66*/                omitDeclaration = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  67*/            else
/*  67*/            if(k == standardnames.STANDALONE)
/*  68*/                standalone = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  69*/            else
/*  69*/            if(k == standardnames.DOCTYPE_PUBLIC)
/*  70*/                doctypePublic = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  71*/            else
/*  71*/            if(k == standardnames.DOCTYPE_SYSTEM)
/*  72*/                doctypeSystem = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  73*/            else
/*  73*/            if(k == standardnames.CDATA_SECTION_ELEMENTS)
/*  74*/                cdataElements = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  75*/            else
/*  75*/            if(k == standardnames.INDENT)
/*  76*/                indent = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  77*/            else
/*  77*/            if(k == standardnames.MEDIA_TYPE)
/*  78*/                mediaType = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  79*/            else
/*  79*/            if(k == standardnames.SAXON_OMIT_META_TAG)
/*  80*/                omitMetaTag = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  81*/            else
/*  81*/            if(k == standardnames.SAXON_CHARACTER_REPRESENTATION)
/*  82*/                representation = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  83*/            else
/*  83*/            if(k == standardnames.SAXON_INDENT_SPACES)
/*  84*/                indentSpaces = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  85*/            else
/*  85*/            if(k == standardnames.SAXON_NEXT_IN_CHAIN)
/*  86*/                nextInChain = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  87*/            else
/*  87*/            if(k == standardnames.SAXON_REQUIRE_WELL_FORMED)
                    {
/*  88*/                requireWellFormed = makeAttributeValueTemplate(attributecollection.getValue(i));
                    } else
                    {
/*  90*/                String s = getNamePool().getURI(j);
/*  91*/                if("".equals(s) || "http://www.w3.org/1999/XSL/Transform".equals(s) || "http://icl.com/saxon".equals(s))
                        {
/*  94*/                    checkUnknownAttribute(j);
                        } else
                        {
/*  96*/                    String s1 = "{" + s + "}" + attributecollection.getLocalName(i);
/*  97*/                    Expression expression = makeAttributeValueTemplate(attributecollection.getValue(i));
/*  98*/                    if(userAttributes == null)
/*  99*/                        userAttributes = new Hashtable(5);
/* 101*/                    userAttributes.put(s1, expression);
                        }
                    }
                }

            }

            protected Properties updateOutputProperties(Properties properties, Context context)
                throws TransformerException
            {
/* 114*/        if(method != null)
                {
/* 115*/            String s = method.evaluateAsString(context);
/* 116*/            if(s.equals("xml") || s.equals("html") || s.equals("text"))
                    {
/* 117*/                properties.put("method", s);
                    } else
                    {
/* 120*/                NamePool namepool = getNamePool();
                        int i;
/* 122*/                try
                        {
/* 122*/                    i = makeNameCode(s, false);
                        }
/* 124*/                catch(NamespaceException namespaceexception)
                        {
/* 124*/                    throw styleError(namespaceexception.getMessage());
                        }
/* 126*/                if(namepool.getURICode(i) == 0)
/* 127*/                    throw styleError("method must be xml, html, or text, or a prefixed name");
/* 129*/                properties.put("method", "{" + namepool.getURI(i) + "}" + namepool.getLocalName(i));
                    }
                }
/* 134*/        if(version != null)
                {
/* 135*/            String s1 = version.evaluateAsString(context);
/* 136*/            properties.put("version", s1);
                }
/* 139*/        if(indent != null)
                {
/* 140*/            String s2 = indent.evaluateAsString(context);
/* 141*/            if(s2 == null || s2.equals("yes") || s2.equals("no"))
/* 142*/                properties.put("indent", s2);
/* 144*/            else
/* 144*/                throw styleError("indent must be yes or no or an integer");
                }
/* 148*/        if(indentSpaces != null)
                {
/* 149*/            String s3 = indentSpaces.evaluateAsString(context);
/* 151*/            try
                    {
/* 151*/                int j = Integer.parseInt(s3);
/* 152*/                properties.put("indent", "yes");
/* 153*/                properties.put("{http://icl.com/saxon}indent-spaces", s3);
                    }
/* 155*/            catch(NumberFormatException numberformatexception)
                    {
/* 155*/                throw styleError("indent-spaces must be an integer");
                    }
                }
/* 159*/        if(encoding != null)
                {
/* 160*/            String s4 = encoding.evaluateAsString(context);
/* 161*/            properties.put("encoding", s4);
                }
/* 164*/        if(mediaType != null)
                {
/* 165*/            String s5 = mediaType.evaluateAsString(context);
/* 166*/            properties.put("media-type", s5);
                }
/* 169*/        if(doctypeSystem != null)
                {
/* 170*/            String s6 = doctypeSystem.evaluateAsString(context);
/* 171*/            properties.put("doctype-system", s6);
                }
/* 174*/        if(doctypePublic != null)
                {
/* 175*/            String s7 = doctypePublic.evaluateAsString(context);
/* 176*/            properties.put("doctype-public", s7);
                }
/* 179*/        if(omitDeclaration != null)
                {
/* 180*/            String s8 = omitDeclaration.evaluateAsString(context);
/* 181*/            if(s8.equals("yes") || s8.equals("no"))
/* 182*/                properties.put("omit-xml-declaration", s8);
/* 184*/            else
/* 184*/                throw styleError("omit-xml-declaration attribute must be yes or no");
                }
/* 188*/        if(standalone != null)
                {
/* 189*/            String s9 = standalone.evaluateAsString(context);
/* 190*/            if(s9.equals("yes") || s9.equals("no"))
/* 191*/                properties.put("standalone", s9);
/* 193*/            else
/* 193*/                throw styleError("standalone attribute must be yes or no");
                }
/* 197*/        if(cdataElements != null)
                {
/* 198*/            String s10 = cdataElements.evaluateAsString(context);
/* 199*/            String s15 = properties.getProperty("cdata-section-elements");
/* 200*/            String s17 = " ";
/* 201*/            StringTokenizer stringtokenizer = new StringTokenizer(s10);
/* 202*/            NamePool namepool1 = context.getController().getNamePool();
/* 204*/            for(; stringtokenizer.hasMoreTokens(); properties.put("cdata-section-elements", s15 + s17))
                    {
/* 204*/                String s19 = stringtokenizer.nextToken();
/* 205*/                if(!Name.isQName(s19))
/* 206*/                    throw styleError("CDATA element " + s19 + " is not a valid QName");
                        int k;
/* 210*/                try
                        {
/* 210*/                    k = makeNameCode(s19, true);
                        }
/* 212*/                catch(NamespaceException namespaceexception1)
                        {
/* 212*/                    throw styleError(namespaceexception1.getMessage());
                        }
/* 214*/                s17 = s17 + " {" + namepool1.getURI(k) + '}' + namepool1.getLocalName(k);
                    }

                }
/* 219*/        if(representation != null)
                {
/* 220*/            String s11 = representation.evaluateAsString(context);
/* 221*/            properties.put("{http://icl.com/saxon}character-representation", s11);
                }
/* 224*/        if(omitMetaTag != null)
                {
/* 225*/            String s12 = omitMetaTag.evaluateAsString(context);
/* 226*/            if(s12.equals("yes") || s12.equals("no"))
/* 227*/                properties.put("{http://icl.com/saxon}omit-meta-tag", s12);
/* 229*/            else
/* 229*/                throw styleError("saxon:omit-meta-tag attribute must be yes or no");
                }
/* 233*/        if(requireWellFormed != null)
                {
/* 234*/            String s13 = requireWellFormed.evaluateAsString(context);
/* 235*/            if(s13.equals("yes") || s13.equals("no"))
/* 236*/                properties.put("{http://saxon.sf.net/}require-well-formed", s13);
/* 238*/            else
/* 238*/                throw styleError("saxon:require-well-formed attribute must be yes or no");
                }
/* 242*/        if(nextInChain != null)
                {
/* 243*/            String s14 = nextInChain.evaluateAsString(context);
/* 244*/            properties.put("{http://icl.com/saxon}next-in-chain", s14);
/* 245*/            properties.put("{http://icl.com/saxon}next-in-chain-base-uri", getSystemId());
                }
/* 250*/        if(userAttributes != null)
                {
                    String s16;
                    String s18;
/* 251*/            for(Enumeration enumeration = userAttributes.keys(); enumeration.hasMoreElements(); properties.put(s16, s18))
                    {
/* 253*/                s16 = (String)enumeration.nextElement();
/* 254*/                Expression expression = (Expression)userAttributes.get(s16);
/* 255*/                s18 = expression.evaluateAsString(context);
                    }

                }
/* 260*/        return properties;
            }

            protected TransformerHandler prepareNextStylesheet(String s, Context context)
                throws TransformerException
            {
/* 273*/        TransformerFactoryImpl transformerfactoryimpl = getPreparedStyleSheet().getTransformerFactory();
/* 275*/        URIResolver uriresolver = context.getController().getURIResolver();
/* 276*/        javax.xml.transform.Source source = uriresolver.resolve(s, getSystemId());
/* 277*/        javax.xml.transform.sax.SAXSource saxsource = transformerfactoryimpl.getSAXSource(source, true);
/* 279*/        javax.xml.transform.Templates templates = transformerfactoryimpl.newTemplates(source);
/* 280*/        TransformerHandler transformerhandler = transformerfactoryimpl.newTransformerHandler(templates);
/* 281*/        return transformerhandler;
            }
}
