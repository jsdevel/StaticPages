// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SAXONDoctype.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.output.*;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement

public class SAXONDoctype extends StyleElement
{

            public SAXONDoctype()
            {
            }

            public boolean isInstruction()
            {
/*  23*/        return true;
            }

            public boolean mayContainTemplateBody()
            {
/*  32*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  37*/        AttributeCollection attributecollection = getAttributeList();
/*  38*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  39*/            int j = attributecollection.getNameCode(i);
/*  40*/            checkUnknownAttribute(j);
                }

            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  46*/        checkWithinTemplate();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  51*/        Controller controller = context.getController();
/*  52*/        Outputter outputter = controller.getOutputter();
/*  53*/        java.util.Properties properties = outputter.getOutputProperties();
/*  55*/        DTDEmitter dtdemitter = new DTDEmitter();
/*  56*/        dtdemitter.setUnderlyingEmitter(outputter.getEmitter());
/*  58*/        controller.changeOutputDestination(properties, dtdemitter);
/*  60*/        processChildren(context);
/*  62*/        controller.resetOutputDestination(outputter);
            }
}
