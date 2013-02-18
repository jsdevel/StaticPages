// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLMessage.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.output.Emitter;
import com.icl.saxon.tree.*;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.Properties;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, TerminationException, StandardNames

public class XSLMessage extends StyleElement
{

            boolean terminate;

            public XSLMessage()
            {
/*  20*/        terminate = false;
            }

            public boolean isInstruction()
            {
/*  28*/        return true;
            }

            public boolean mayContainTemplateBody()
            {
/*  37*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  42*/        String s = null;
/*  43*/        StandardNames standardnames = getStandardNames();
/*  44*/        AttributeCollection attributecollection = getAttributeList();
/*  46*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  47*/            int j = attributecollection.getNameCode(i);
/*  48*/            int k = j & 0xfffff;
/*  49*/            if(k == standardnames.TERMINATE)
/*  50*/                s = attributecollection.getValue(i);
/*  52*/            else
/*  52*/                checkUnknownAttribute(j);
                }

/*  56*/        if(s != null)
/*  57*/            if(s.equals("yes"))
/*  58*/                terminate = true;
/*  59*/            else
/*  59*/            if(s.equals("no"))
/*  60*/                terminate = false;
/*  62*/            else
/*  62*/                styleError("terminate must be \"yes\" or \"no\"");
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  68*/        checkWithinTemplate();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  73*/        Controller controller = context.getController();
/*  74*/        Emitter emitter = controller.getMessageEmitter();
/*  75*/        if(emitter == null)
/*  76*/            emitter = controller.makeMessageEmitter();
/*  78*/        if(emitter.getWriter() == null)
/*  79*/            emitter.setWriter(new OutputStreamWriter(System.err));
/*  82*/        com.icl.saxon.output.Outputter outputter = controller.getOutputter();
/*  83*/        Properties properties = new Properties();
/*  84*/        properties.put("omit-xml-declaration", "yes");
/*  85*/        controller.changeOutputDestination(properties, emitter);
/*  87*/        processChildren(context);
/*  89*/        controller.resetOutputDestination(outputter);
/*  91*/        if(terminate)
/*  92*/            throw new TerminationException("Processing terminated by xsl:message at line " + getLineNumber());
/*  94*/        else
/*  94*/            return;
            }
}
