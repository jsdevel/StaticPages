// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLSort.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.AbstractNode;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.sort.SortKeyDefinition;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import java.util.Locale;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, ExpressionContext, XSLApplyTemplates, XSLForEach, 
//            SAXONGroup, StandardNames

public class XSLSort extends StyleElement
{

            private SortKeyDefinition sortKeyDefinition;

            public XSLSort()
            {
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  27*/        StandardNames standardnames = getStandardNames();
/*  28*/        AttributeCollection attributecollection = getAttributeList();
/*  30*/        String s = null;
/*  31*/        String s1 = null;
/*  32*/        String s2 = null;
/*  33*/        String s3 = null;
/*  34*/        String s4 = null;
/*  36*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  37*/            int j = attributecollection.getNameCode(i);
/*  38*/            int k = j & 0xfffff;
/*  39*/            if(k == standardnames.SELECT)
/*  40*/                s = attributecollection.getValue(i);
/*  41*/            else
/*  41*/            if(k == standardnames.ORDER)
/*  42*/                s1 = attributecollection.getValue(i);
/*  43*/            else
/*  43*/            if(k == standardnames.DATA_TYPE)
/*  44*/                s2 = attributecollection.getValue(i);
/*  45*/            else
/*  45*/            if(k == standardnames.CASE_ORDER)
/*  46*/                s3 = attributecollection.getValue(i);
/*  47*/            else
/*  47*/            if(k == standardnames.LANG)
/*  48*/                s4 = attributecollection.getValue(i);
/*  50*/            else
/*  50*/                checkUnknownAttribute(j);
                }

                Object obj;
/*  54*/        if(s == null)
/*  55*/            obj = new ContextNodeExpression();
/*  57*/        else
/*  57*/            obj = makeExpression(s);
                Object obj1;
/*  60*/        if(s1 == null)
/*  61*/            obj1 = new StringValue("ascending");
/*  63*/        else
/*  63*/            obj1 = makeAttributeValueTemplate(s1);
                Object obj2;
/*  66*/        if(s2 == null)
/*  67*/            obj2 = new StringValue("text");
/*  69*/        else
/*  69*/            obj2 = makeAttributeValueTemplate(s2);
                Object obj3;
/*  72*/        if(s3 == null)
/*  73*/            obj3 = new StringValue("#default");
/*  75*/        else
/*  75*/            obj3 = makeAttributeValueTemplate(s3);
                Object obj4;
/*  78*/        if(s4 == null)
/*  79*/            obj4 = new StringValue(Locale.getDefault().getLanguage());
/*  81*/        else
/*  81*/            obj4 = makeAttributeValueTemplate(s4);
/*  85*/        try
                {
/*  85*/            sortKeyDefinition = new SortKeyDefinition();
/*  86*/            sortKeyDefinition.setSortKey(((com.icl.saxon.expr.Expression) (obj)));
/*  87*/            sortKeyDefinition.setOrder(((com.icl.saxon.expr.Expression) (obj1)));
/*  88*/            sortKeyDefinition.setDataType(((com.icl.saxon.expr.Expression) (obj2)));
/*  89*/            sortKeyDefinition.setCaseOrder(((com.icl.saxon.expr.Expression) (obj3)));
/*  90*/            sortKeyDefinition.setLanguage(((com.icl.saxon.expr.Expression) (obj4)));
/*  91*/            sortKeyDefinition.setStaticContext(new ExpressionContext(this));
/*  92*/            sortKeyDefinition.bindComparer();
                }
/*  94*/        catch(XPathException xpathexception)
                {
/*  94*/            compileError(xpathexception);
                }
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  99*/        NodeInfo nodeinfo = (NodeInfo)getParentNode();
/* 100*/        if(!(nodeinfo instanceof XSLApplyTemplates) && !(nodeinfo instanceof XSLForEach) && !(nodeinfo instanceof SAXONGroup))
/* 104*/            compileError("xsl:sort must be child of xsl:apply-templates or xsl:for-each");
            }

            public void process(Context context)
                throws TransformerException
            {
            }

            public SortKeyDefinition getSortKeyDefinition()
            {
/* 112*/        return sortKeyDefinition;
            }
}
