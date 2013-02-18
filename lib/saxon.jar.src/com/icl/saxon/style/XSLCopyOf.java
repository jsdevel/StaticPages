// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLCopyOf.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames

public class XSLCopyOf extends StyleElement
{

            Expression select;

            public XSLCopyOf()
            {
            }

            public boolean isInstruction()
            {
/*  27*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  33*/        StandardNames standardnames = getStandardNames();
/*  34*/        AttributeCollection attributecollection = getAttributeList();
/*  35*/        String s = null;
/*  37*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  38*/            int j = attributecollection.getNameCode(i);
/*  39*/            int k = j & 0xfffff;
/*  40*/            if(k == standardnames.SELECT)
/*  41*/                s = attributecollection.getValue(i);
/*  43*/            else
/*  43*/                checkUnknownAttribute(j);
                }

/*  47*/        if(s != null)
/*  48*/            select = makeExpression(s);
/*  50*/        else
/*  50*/            reportAbsence("select");
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  55*/        checkWithinTemplate();
/*  56*/        checkEmpty();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  62*/        if(select instanceof NodeSetExpression)
                {
/*  63*/            copyNodeSet(select, context);
                } else
                {
/*  65*/            Value value = select.evaluate(context);
/*  66*/            if(value instanceof FragmentValue)
/*  67*/                ((FragmentValue)value).copy(context.getOutputter());
/*  69*/            else
/*  69*/            if(value instanceof TextFragmentValue)
/*  70*/                ((TextFragmentValue)value).copy(context.getOutputter());
/*  72*/            else
/*  72*/            if(value instanceof NodeSetValue)
/*  73*/                copyNodeSet((NodeSetValue)value, context);
/*  76*/            else
/*  76*/                context.getOutputter().writeContent(value.asString());
                }
            }

            private void copyNodeSet(Expression expression, Context context)
                throws TransformerException
            {
/*  82*/        Outputter outputter = context.getOutputter();
                NodeInfo nodeinfo;
/*  83*/        for(NodeEnumeration nodeenumeration = expression.enumerate(context, true); nodeenumeration.hasMoreElements(); nodeinfo.copy(outputter))
/*  85*/            nodeinfo = nodeenumeration.nextElement();

            }
}
