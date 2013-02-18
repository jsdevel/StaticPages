// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StyleSheetFunctionCall.java

package com.icl.saxon.expr;

import com.icl.saxon.*;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.style.SAXONFunction;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.expr:
//            Function, XPathException, Expression, Value

public class StyleSheetFunctionCall extends Function
{

            private SAXONFunction function;
            private Controller boundController;
            private NodeInfo boundContextNode;
            private int boundContextPosition;
            private int boundContextSize;

            public StyleSheetFunctionCall()
            {
/*  18*/        boundController = null;
/*  19*/        boundContextNode = null;
/*  20*/        boundContextPosition = -1;
/*  21*/        boundContextSize = -1;
            }

            public void setFunction(SAXONFunction saxonfunction)
            {
/*  28*/        function = saxonfunction;
            }

            public String getName()
            {
/*  38*/        return function.getAttribute("name");
            }

            public int getDataType()
            {
/*  47*/        return -1;
            }

            public Expression simplify()
                throws XPathException
            {
/*  56*/        for(int i = 0; i < getNumberOfArguments(); i++)
/*  57*/            super.argument[i] = super.argument[i].simplify();

/*  59*/        return this;
            }

            public int getDependencies()
            {
/*  73*/        int i = 0;
/*  74*/        if(boundController == null)
/*  74*/            i |= 0x40;
/*  75*/        if(boundContextNode == null)
/*  75*/            i |= 8;
/*  76*/        if(boundContextPosition == -1)
/*  76*/            i |= 0x10;
/*  77*/        if(boundContextSize == -1)
/*  77*/            i |= 0x20;
/*  79*/        for(int j = 0; j < getNumberOfArguments(); j++)
/*  80*/            i |= super.argument[j].getDependencies();

/*  82*/        return i;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  93*/        StyleSheetFunctionCall stylesheetfunctioncall = new StyleSheetFunctionCall();
/*  94*/        stylesheetfunctioncall.setFunction(function);
/*  95*/        stylesheetfunctioncall.setStaticContext(getStaticContext());
/*  96*/        stylesheetfunctioncall.boundController = boundController;
/*  97*/        stylesheetfunctioncall.boundContextNode = boundContextNode;
/*  98*/        stylesheetfunctioncall.boundContextPosition = boundContextPosition;
/*  99*/        stylesheetfunctioncall.boundContextSize = boundContextSize;
/* 101*/        for(int j = 0; j < getNumberOfArguments(); j++)
/* 102*/            stylesheetfunctioncall.addArgument(super.argument[j].reduce(i, context));

/* 105*/        if(boundController == null && (i & 0x40) != 0)
/* 106*/            stylesheetfunctioncall.boundController = context.getController();
/* 108*/        if(boundContextNode == null && (i & 8) != 0)
/* 109*/            stylesheetfunctioncall.boundContextNode = context.getContextNodeInfo();
/* 111*/        if(boundContextPosition == -1 && (i & 0x10) != 0)
/* 112*/            stylesheetfunctioncall.boundContextPosition = context.getContextPosition();
/* 114*/        if(boundContextSize == -1 && (i & 0x20) != 0)
/* 115*/            stylesheetfunctioncall.boundContextSize = context.getLast();
/* 118*/        return stylesheetfunctioncall.simplify();
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/* 130*/        Context context1 = context.newContext();
/* 131*/        if(boundController != null)
/* 132*/            context1.setController(boundController);
/* 134*/        if(boundContextNode != null)
                {
/* 135*/            context1.setCurrentNode(boundContextNode);
/* 136*/            context1.setContextNode(boundContextNode);
                }
/* 138*/        if(boundContextPosition != -1)
/* 139*/            context1.setPosition(boundContextPosition);
/* 141*/        if(boundContextSize != -1)
/* 142*/            context1.setLast(boundContextSize);
/* 145*/        ParameterSet parameterset = new ParameterSet();
/* 146*/        for(int i = 0; i < getNumberOfArguments(); i++)
                {
/* 147*/            int j = function.getNthParameter(i);
/* 148*/            if(j == -1)
/* 149*/                throw new XPathException("Too many arguments");
/* 151*/            parameterset.put(j, super.argument[i].evaluate(context));
                }

/* 154*/        try
                {
/* 154*/            return function.call(parameterset, context1);
                }
/* 156*/        catch(TransformerException transformerexception)
                {
/* 156*/            throw new XPathException(transformerexception);
                }
            }
}
