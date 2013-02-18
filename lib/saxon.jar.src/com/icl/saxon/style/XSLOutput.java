// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLOutput.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import java.util.Hashtable;
import java.util.Properties;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            XSLGeneralOutput, StyleElement, StandardNames

public class XSLOutput extends XSLGeneralOutput
{

            public XSLOutput()
            {
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  18*/        super.prepareAttributes();
/*  19*/        if(super.href != null)
/*  20*/            compileError("The href attribute is not allowed on this element");
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  25*/        checkTopLevel();
/*  26*/        checkEmpty();
/*  30*/        if(!forwardsCompatibleModeIsEnabled())
                {
/*  31*/            AttributeCollection attributecollection = getAttributeList();
/*  32*/            for(int i = 0; i < attributecollection.getLength(); i++)
                    {
/*  33*/                if(attributecollection.getValue(i).indexOf('{') < 0)
/*  34*/                    continue;
/*  34*/                compileError("To use attribute value templates in xsl:output, set xsl:stylesheet version='1.1'");
/*  35*/                break;
                    }

                }
            }

            public void process(Context context)
                throws TransformerException
            {
            }

            protected Properties gatherOutputProperties(Properties properties)
            {
/*  48*/        StandardNames standardnames = getStandardNames();
/*  49*/        AttributeCollection attributecollection = getAttributeList();
/*  50*/        if(super.method != null)
/*  51*/            properties.put("method", attributecollection.getValueByFingerprint(standardnames.METHOD));
/*  55*/        if(super.version != null)
/*  56*/            properties.put("version", attributecollection.getValueByFingerprint(standardnames.VERSION));
/*  60*/        if(super.indent != null)
/*  61*/            properties.put("indent", attributecollection.getValueByFingerprint(standardnames.INDENT));
/*  65*/        if(super.indentSpaces != null)
/*  66*/            properties.put("{http://icl.com/saxon}indent-spaces", attributecollection.getValueByFingerprint(standardnames.SAXON_INDENT_SPACES));
/*  70*/        if(super.encoding != null)
/*  71*/            properties.put("encoding", attributecollection.getValueByFingerprint(standardnames.ENCODING));
/*  75*/        if(super.mediaType != null)
/*  76*/            properties.put("media-type", attributecollection.getValueByFingerprint(standardnames.MEDIA_TYPE));
/*  80*/        if(super.doctypeSystem != null)
/*  81*/            properties.put("doctype-system", attributecollection.getValueByFingerprint(standardnames.DOCTYPE_SYSTEM));
/*  85*/        if(super.doctypePublic != null)
/*  86*/            properties.put("doctype-public", attributecollection.getValueByFingerprint(standardnames.DOCTYPE_PUBLIC));
/*  90*/        if(super.omitDeclaration != null)
/*  91*/            properties.put("omit-xml-declaration", attributecollection.getValueByFingerprint(standardnames.OMIT_XML_DECLARATION));
/*  95*/        if(super.standalone != null)
/*  96*/            properties.put("standalone", attributecollection.getValueByFingerprint(standardnames.STANDALONE));
/* 100*/        if(super.cdataElements != null)
                {
/* 101*/            String s = properties.getProperty("cdata-section-elements");
/* 102*/            String s1 = s + " " + attributecollection.getValueByFingerprint(standardnames.CDATA_SECTION_ELEMENTS);
/* 103*/            properties.put("cdata-section-elements", s1);
                }
/* 106*/        if(super.nextInChain == null);
/* 110*/        if(super.requireWellFormed != null)
/* 111*/            properties.put("{http://saxon.sf.net/}require-well-formed", attributecollection.getValueByFingerprint(standardnames.SAXON_REQUIRE_WELL_FORMED));
/* 115*/        return properties;
            }
}
