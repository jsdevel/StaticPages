// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SAXONGroup.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.tree.*;
import java.util.Stack;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            XSLForEach, SAXONItem, GroupActivation, StyleElement, 
//            StandardNames

public class SAXONGroup extends XSLForEach
{

            Expression groupBy;

            public SAXONGroup()
            {
/*  18*/        groupBy = null;
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
/*  35*/        String s = null;
/*  36*/        String s1 = null;
/*  38*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  39*/            int j = attributecollection.getNameCode(i);
/*  40*/            int k = j & 0xfffff;
/*  41*/            if(k == standardnames.SELECT)
/*  42*/                s = attributecollection.getValue(i);
/*  43*/            else
/*  43*/            if(k == standardnames.GROUP_BY)
/*  44*/                s1 = attributecollection.getValue(i);
/*  46*/            else
/*  46*/                checkUnknownAttribute(j);
                }

/*  50*/        if(s == null)
/*  51*/            reportAbsence("select");
/*  53*/        else
/*  53*/            super.select = makeExpression(s);
/*  56*/        if(s1 == null)
/*  57*/            reportAbsence("group-by");
/*  59*/        else
/*  59*/            groupBy = makeExpression(s1);
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  64*/        checkWithinTemplate();
/*  65*/        super.select = handleSortKeys(super.select);
/*  68*/        Object obj = this;
/*  69*/        SAXONItem saxonitem = null;
/*  71*/        for(; obj != null; obj = ((NodeImpl) (obj)).getNextInDocument(this))
                {
/*  71*/            if(!(obj instanceof SAXONItem))
/*  72*/                continue;
/*  72*/            saxonitem = (SAXONItem)obj;
/*  73*/            break;
                }

/*  77*/        if(saxonitem == null)
/*  78*/            compileError("saxon:group must have a nested saxon:item element");
            }

            public void process(Context context)
                throws TransformerException
            {
/*  84*/        Object obj = super.select.enumerate(context, false);
/*  85*/        if(!(obj instanceof LastPositionFinder))
/*  86*/            obj = new LookaheadEnumerator(((com.icl.saxon.om.NodeEnumeration) (obj)));
/*  89*/        Context context1 = context.newContext();
/*  90*/        context1.setLastPositionFinder((LastPositionFinder)obj);
/*  94*/        GroupActivation groupactivation = new GroupActivation(this, groupBy, ((com.icl.saxon.om.NodeEnumeration) (obj)), context1);
/*  95*/        Stack stack = context1.getGroupActivationStack();
/*  96*/        stack.push(groupactivation);
/*  99*/        for(; groupactivation.hasMoreElements(); context.setReturnValue(context1.getReturnValue()))
                {
/*  99*/            groupactivation.nextElement();
/* 100*/            processChildren(context1);
                }

/* 104*/        stack.pop();
            }
}
