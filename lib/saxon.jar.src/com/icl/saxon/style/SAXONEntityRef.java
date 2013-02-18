// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SAXONEntityRef.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames

public class SAXONEntityRef extends StyleElement
{

            String nameAttribute;

            public SAXONEntityRef()
            {
            }

            public boolean isInstruction()
            {
/*  26*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  32*/        StandardNames standardnames = getStandardNames();
/*  33*/        AttributeCollection attributecollection = getAttributeList();
/*  35*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  36*/            int j = attributecollection.getNameCode(i);
/*  37*/            int k = j & 0xfffff;
/*  38*/            if(k == standardnames.NAME)
/*  39*/                nameAttribute = attributecollection.getValue(i);
/*  41*/            else
/*  41*/                checkUnknownAttribute(j);
                }

/*  45*/        if(nameAttribute == null)
/*  46*/            reportAbsence("name");
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  51*/        checkWithinTemplate();
/*  52*/        checkEmpty();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  56*/        Outputter outputter = context.getOutputter();
/*  57*/        outputter.setEscaping(false);
/*  58*/        outputter.writeContent('&' + nameAttribute + ';');
/*  59*/        outputter.setEscaping(true);
            }
}
