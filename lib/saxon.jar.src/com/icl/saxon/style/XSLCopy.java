// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLCopy.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames

public class XSLCopy extends StyleElement
{

            private String use;

            public XSLCopy()
            {
            }

            public boolean isInstruction()
            {
/*  26*/        return true;
            }

            public boolean mayContainTemplateBody()
            {
/*  35*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  40*/        StandardNames standardnames = getStandardNames();
/*  41*/        AttributeCollection attributecollection = getAttributeList();
/*  43*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  44*/            int j = attributecollection.getNameCode(i);
/*  45*/            int k = j & 0xfffff;
/*  46*/            if(k == standardnames.USE_ATTRIBUTE_SETS)
/*  47*/                use = attributecollection.getValue(i);
/*  49*/            else
/*  49*/                checkUnknownAttribute(j);
                }

            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  55*/        checkWithinTemplate();
/*  56*/        if(use != null)
/*  57*/            findAttributeSets(use);
            }

            public void process(Context context)
                throws TransformerException
            {
/*  63*/        NodeInfo nodeinfo = context.getCurrentNodeInfo();
/*  64*/        Outputter outputter = context.getOutputter();
/*  68*/        switch(nodeinfo.getNodeType())
                {
/*  71*/        case 1: // '\001'
/*  71*/            outputter.writeStartTag(nodeinfo.getNameCode());
/*  73*/            nodeinfo.outputNamespaceNodes(outputter, true);
/*  75*/            processAttributeSets(context);
/*  76*/            processChildren(context);
/*  77*/            outputter.writeEndTag(nodeinfo.getNameCode());
/*  78*/            break;

/*  81*/        case 2: // '\002'
/*  81*/            int i = nodeinfo.getNameCode();
/*  82*/            if((i >> 20 & 0xff) != 0)
/*  83*/                i = outputter.checkAttributePrefix(i);
/*  85*/            outputter.writeAttribute(i, nodeinfo.getStringValue());
                    break;

/*  89*/        case 3: // '\003'
/*  89*/            outputter.writeContent(nodeinfo.getStringValue());
                    break;

/*  93*/        case 7: // '\007'
/*  93*/            outputter.writePI(nodeinfo.getDisplayName(), nodeinfo.getStringValue());
                    break;

/*  97*/        case 8: // '\b'
/*  97*/            outputter.writeComment(nodeinfo.getStringValue());
                    break;

/* 101*/        case 13: // '\r'
/* 101*/            nodeinfo.copy(outputter);
                    break;

/* 105*/        case 9: // '\t'
/* 105*/            processChildren(context);
                    break;

/* 109*/        case 4: // '\004'
/* 109*/        case 5: // '\005'
/* 109*/        case 6: // '\006'
/* 109*/        case 10: // '\n'
/* 109*/        case 11: // '\013'
/* 109*/        case 12: // '\f'
/* 109*/        default:
/* 109*/            throw new IllegalArgumentException("Unknown node type " + nodeinfo.getNodeType());
                }
            }
}
