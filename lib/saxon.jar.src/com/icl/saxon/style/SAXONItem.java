// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SAXONItem.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.om.AbstractNode;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import java.util.Stack;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, SAXONGroup, GroupActivation

public class SAXONItem extends StyleElement
{

            private SAXONGroup group;

            public SAXONItem()
            {
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  19*/        AttributeCollection attributecollection = getAttributeList();
/*  20*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  21*/            int j = attributecollection.getNameCode(i);
/*  22*/            checkUnknownAttribute(j);
                }

            }

            public boolean mayContainTemplateBody()
            {
/*  32*/        return true;
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  36*/        checkWithinTemplate();
/*  37*/        for(Node node = getParentNode(); node != null; node = node.getParentNode())
                {
/*  39*/            if(!(node instanceof SAXONGroup))
/*  40*/                continue;
/*  40*/            group = (SAXONGroup)node;
/*  41*/            break;
                }

/*  46*/        if(group == null)
/*  47*/            compileError("saxon:item must be within a saxon:group");
            }

            public void process(Context context)
                throws TransformerException
            {
/*  54*/        GroupActivation groupactivation = (GroupActivation)context.getGroupActivationStack().peek();
/*  57*/        do
                {
/*  57*/            processChildren(context);
/*  58*/            if(groupactivation.sameAsNext())
/*  59*/                groupactivation.nextElement();
/*  61*/            else
/*  61*/                return;
                } while(true);
            }
}
