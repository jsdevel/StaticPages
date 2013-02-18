// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLText.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.om.AbstractNode;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.tree.*;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames

public class XSLText extends StyleElement
{

            private boolean disable;
            private String value;

            public XSLText()
            {
/*  18*/        disable = false;
/*  19*/        value = null;
            }

            public boolean isInstruction()
            {
/*  28*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  33*/        String s = null;
/*  35*/        StandardNames standardnames = getStandardNames();
/*  36*/        AttributeCollection attributecollection = getAttributeList();
/*  38*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  39*/            int j = attributecollection.getNameCode(i);
/*  40*/            int k = j & 0xfffff;
/*  41*/            if(k == standardnames.DISABLE_OUTPUT_ESCAPING)
/*  42*/                s = attributecollection.getValue(i);
/*  44*/            else
/*  44*/                checkUnknownAttribute(j);
                }

/*  48*/        if(s != null)
/*  49*/            if(s.equals("yes"))
/*  50*/                disable = true;
/*  51*/            else
/*  51*/            if(s.equals("no"))
/*  52*/                disable = false;
/*  54*/            else
/*  54*/                compileError("disable-output-escaping attribute must be either yes or no");
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  60*/        checkWithinTemplate();
/*  61*/        NodeImpl nodeimpl = (NodeImpl)getFirstChild();
/*  62*/        if(nodeimpl == null)
                {
/*  63*/            value = "";
                } else
                {
/*  65*/            value = nodeimpl.getStringValue();
/*  67*/            for(; nodeimpl != null; nodeimpl = (NodeImpl)nodeimpl.getNextSibling())
/*  67*/                if(nodeimpl.getNodeType() == 1)
/*  68*/                    compileError("xsl:text must not have any child elements");

                }
            }

            public void process(Context context)
                throws TransformerException
            {
/*  76*/        if(!value.equals(""))
                {
/*  77*/            Outputter outputter = context.getOutputter();
/*  78*/            if(disable)
                    {
/*  79*/                outputter.setEscaping(false);
/*  80*/                outputter.writeContent(value);
/*  81*/                outputter.setEscaping(true);
                    } else
                    {
/*  83*/                outputter.writeContent(value);
                    }
                }
            }
}
