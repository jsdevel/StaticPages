// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLDocument.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.output.*;
import com.icl.saxon.tree.ElementImpl;
import com.icl.saxon.tree.NodeImpl;
import java.io.*;
import java.util.Properties;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

// Referenced classes of package com.icl.saxon.style:
//            XSLGeneralOutput, StyleElement

public class XSLDocument extends XSLGeneralOutput
{

            public XSLDocument()
            {
            }

            public boolean isInstruction()
            {
/*  43*/        return true;
            }

            public boolean mayContainTemplateBody()
            {
/*  52*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  56*/        super.prepareAttributes();
/*  57*/        if(super.href == null)
/*  58*/            reportAbsence("href");
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  63*/        if(getURI().equals("http://www.w3.org/1999/XSL/Transform") && !forwardsCompatibleModeIsEnabled())
/*  66*/            compileError("To use xsl:document, set xsl:stylesheet version='1.1'");
/*  69*/        checkWithinTemplate();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  74*/        Controller controller = context.getController();
/*  75*/        Outputter outputter = controller.getOutputter();
/*  76*/        Properties properties = outputter.getOutputProperties();
/*  77*/        Properties properties1 = new Properties(properties);
/*  78*/        updateOutputProperties(properties1, context);
/*  79*/        Object obj = null;
/*  86*/        String s = super.href.evaluateAsString(context);
                FileOutputStream fileoutputstream;
/*  88*/        try
                {
/*  88*/            File file = new File(s);
/*  89*/            if(!file.exists())
                    {
/*  90*/                String s2 = file.getParent();
/*  91*/                if(s2 != null && !Version.isPreJDK12())
                        {
/*  92*/                    File file1 = new File(s2);
/*  93*/                    if(file1 != null && !file1.exists())
/*  94*/                        file1.mkdirs();
/*  96*/                    file.createNewFile();
                        }
                    }
/*  99*/            fileoutputstream = new FileOutputStream(file);
/* 100*/            obj = new StreamResult(fileoutputstream);
                }
/* 102*/        catch(IOException ioexception)
                {
/* 102*/            throw new TransformerException("Failed to create output file " + s, ioexception);
                }
/* 105*/        if(super.nextInChain != null)
                {
/* 106*/            String s1 = super.nextInChain.evaluateAsString(context);
/* 107*/            TransformerHandler transformerhandler = prepareNextStylesheet(s1, context);
/* 108*/            ContentHandlerProxy contenthandlerproxy = new ContentHandlerProxy();
/* 109*/            contenthandlerproxy.setSystemId(getSystemId());
/* 110*/            contenthandlerproxy.setUnderlyingContentHandler(transformerhandler);
/* 111*/            contenthandlerproxy.setRequireWellFormed(false);
/* 112*/            transformerhandler.setResult(((javax.xml.transform.Result) (obj)));
/* 113*/            obj = contenthandlerproxy;
                }
/* 116*/        controller.changeOutputDestination(properties1, ((javax.xml.transform.Result) (obj)));
/* 117*/        processChildren(context);
/* 118*/        controller.resetOutputDestination(outputter);
/* 120*/        try
                {
/* 120*/            fileoutputstream.close();
                }
/* 122*/        catch(IOException ioexception1)
                {
/* 122*/            throw new TransformerException("Failed to close output file", ioexception1);
                }
            }
}
