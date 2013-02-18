// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLScript.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.Loader;
import com.icl.saxon.om.NamePool;
import com.icl.saxon.om.NamespaceException;
import com.icl.saxon.tree.*;
import java.net.*;
import java.util.StringTokenizer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames

public class XSLScript extends StyleElement
{

            private Class javaClass;
            private String implementsURI;
            private String language;

            public XSLScript()
            {
/*  20*/        javaClass = null;
/*  21*/        implementsURI = null;
/*  22*/        language = null;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  26*/        String s = null;
/*  27*/        String s1 = null;
/*  28*/        String s2 = null;
/*  29*/        String s3 = null;
/*  31*/        StandardNames standardnames = getStandardNames();
/*  32*/        AttributeCollection attributecollection = getAttributeList();
/*  34*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  35*/            int j = attributecollection.getNameCode(i);
/*  36*/            int k = j & 0xfffff;
/*  37*/            if(k == standardnames.LANGUAGE)
/*  38*/                s = attributecollection.getValue(i);
/*  39*/            else
/*  39*/            if(k == standardnames.IMPLEMENTS_PREFIX)
/*  40*/                s1 = attributecollection.getValue(i);
/*  41*/            else
/*  41*/            if(k == standardnames.SRC)
/*  42*/                s2 = attributecollection.getValue(i);
/*  43*/            else
/*  43*/            if(k == standardnames.ARCHIVE)
/*  44*/                s3 = attributecollection.getValue(i);
/*  46*/            else
/*  46*/                checkUnknownAttribute(j);
                }

/*  49*/        if(s1 == null)
                {
/*  50*/            reportAbsence("implements-prefix");
/*  51*/            return;
                }
/*  54*/        try
                {
/*  54*/            short word0 = getURICodeForPrefix(s1);
/*  55*/            implementsURI = getNamePool().getURIFromURICode(word0);
                }
/*  57*/        catch(NamespaceException namespaceexception)
                {
/*  57*/            compileError(namespaceexception.getMessage());
                }
/*  61*/        if(s == null)
                {
/*  62*/            reportAbsence("language");
/*  63*/            return;
                }
/*  65*/        language = s;
/*  70*/        if(language.equals("java"))
                {
/*  71*/            if(s2 == null)
                    {
/*  72*/                compileError("For java, the src attribute is mandatory");
/*  73*/                return;
                    }
/*  75*/            if(!s2.startsWith("java:"))
                    {
/*  76*/                compileError("The src attribute must be a URI of the form java:full.class.Name");
/*  77*/                return;
                    }
/*  79*/            String s4 = s2.substring(5);
/*  81*/            if(s3 == null)
                    {
/*  83*/                try
                        {
/*  83*/                    javaClass = Loader.getClass(s4);
                        }
/*  85*/                catch(TransformerException transformerexception)
                        {
/*  85*/                    compileError(transformerexception);
/*  86*/                    return;
                        }
                    } else
                    {
                        URL url;
/*  91*/                try
                        {
/*  91*/                    url = new URL(getBaseURI());
                        }
/*  93*/                catch(MalformedURLException malformedurlexception)
                        {
/*  93*/                    compileError("Invalid base URI " + getBaseURI());
/*  94*/                    return;
                        }
/*  96*/                StringTokenizer stringtokenizer = new StringTokenizer(s3);
/*  97*/                int l = 0;
/*  99*/                for(; stringtokenizer.hasMoreTokens(); stringtokenizer.nextToken())
/*  99*/                    l++;

/* 102*/                URL aurl[] = new URL[l];
/* 103*/                l = 0;
/* 104*/                for(StringTokenizer stringtokenizer1 = new StringTokenizer(s3); stringtokenizer1.hasMoreTokens();)
                        {
/* 106*/                    String s5 = stringtokenizer1.nextToken();
/* 108*/                    try
                            {
/* 108*/                        aurl[l++] = new URL(url, s5);
                            }
/* 110*/                    catch(MalformedURLException malformedurlexception1)
                            {
/* 110*/                        compileError("Invalid URL " + s5);
/* 111*/                        return;
                            }
                        }

/* 115*/                try
                        {
/* 115*/                    javaClass = (new URLClassLoader(aurl)).loadClass(s4);
                        }
/* 117*/                catch(ClassNotFoundException classnotfoundexception)
                        {
/* 117*/                    compileError("Cannot find class " + s4 + " in the specified archive" + (l <= 1 ? "" : "s"));
                        }
/* 120*/                catch(NoClassDefFoundError noclassdeffounderror)
                        {
/* 120*/                    compileError("Cannot use the archive attribute with this Java VM");
                        }
                    }
                }
            }

            public void validate()
                throws TransformerConfigurationException
            {
/* 127*/        if(getURI().equals("http://www.w3.org/1999/XSL/Transform") && !forwardsCompatibleModeIsEnabled())
/* 130*/            compileError("To use xsl:script, set xsl:stylesheet version='1.1'");
/* 133*/        checkTopLevel();
            }

            public void preprocess()
                throws TransformerConfigurationException
            {
            }

            public void process(Context context)
            {
            }

            public Class getJavaClass(String s)
                throws TransformerException
            {
/* 146*/        if(language == null)
/* 148*/            prepareAttributes();
/* 150*/        if(language.equals("java") && implementsURI.equals(s))
/* 151*/            return javaClass;
/* 153*/        else
/* 153*/            return null;
            }
}
