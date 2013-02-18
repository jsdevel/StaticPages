// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TemplatesHandlerImpl.java

package com.icl.saxon;

import com.icl.saxon.om.Builder;
import com.icl.saxon.om.NamePool;
import com.icl.saxon.output.Emitter;
import com.icl.saxon.output.ProxyEmitter;
import com.icl.saxon.style.StyleNodeFactory;
import com.icl.saxon.tree.DocumentImpl;
import com.icl.saxon.tree.TreeBuilder;
import java.io.PrintStream;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.TemplatesHandler;

// Referenced classes of package com.icl.saxon:
//            ContentEmitter, StylesheetStripper, PreparedStyleSheet, TransformerFactoryImpl

public class TemplatesHandlerImpl extends ContentEmitter
    implements TemplatesHandler
{

            TransformerFactoryImpl factory;
            TreeBuilder builder;
            Templates templates;
            String systemId;

            protected TemplatesHandlerImpl(TransformerFactoryImpl transformerfactoryimpl)
            {
/*  35*/        NamePool namepool = NamePool.getDefaultNamePool();
/*  36*/        setNamePool(namepool);
/*  37*/        factory = transformerfactoryimpl;
/*  38*/        builder = new TreeBuilder();
/*  39*/        builder.setNamePool(namepool);
/*  40*/        StyleNodeFactory stylenodefactory = new StyleNodeFactory(namepool);
/*  43*/        StylesheetStripper stylesheetstripper = new StylesheetStripper();
/*  44*/        stylesheetstripper.setStylesheetRules(namepool);
/*  46*/        builder = new TreeBuilder();
/*  47*/        builder.setNamePool(namepool);
/*  48*/        builder.setStripper(stylesheetstripper);
/*  49*/        builder.setNodeFactory(stylenodefactory);
/*  50*/        builder.setDiscardCommentsAndPIs(true);
/*  51*/        builder.setLineNumbering(true);
/*  53*/        setEmitter(stylesheetstripper);
/*  54*/        stylesheetstripper.setUnderlyingEmitter(builder);
            }

            public Templates getTemplates()
            {
/*  63*/        if(templates == null)
                {
/*  64*/            DocumentImpl documentimpl = (DocumentImpl)builder.getCurrentDocument();
/*  65*/            if(documentimpl == null)
/*  66*/                return null;
/*  68*/            PreparedStyleSheet preparedstylesheet = new PreparedStyleSheet(factory);
/*  70*/            try
                    {
/*  70*/                preparedstylesheet.setStyleSheetDocument(documentimpl);
/*  71*/                templates = preparedstylesheet;
                    }
/*  74*/            catch(TransformerConfigurationException transformerconfigurationexception)
                    {
/*  74*/                System.err.println(transformerconfigurationexception.getMessage());
/*  75*/                return null;
                    }
                }
/*  79*/        return templates;
            }

            public void setSystemId(String s)
            {
/*  87*/        systemId = s;
/*  88*/        builder.setSystemId(s);
            }

            public String getSystemId()
            {
/*  96*/        return systemId;
            }
}
