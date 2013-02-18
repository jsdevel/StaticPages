// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Expression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.output.Outputter;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.expr:
//            ExpressionParser, XPathException, ErrorExpression, NodeSetValue, 
//            StaticContext, Value

public abstract class Expression
{

            protected StaticContext staticContext;

            public Expression()
            {
            }

            public static Expression make(String s, StaticContext staticcontext)
                throws XPathException
            {
/*  27*/        try
                {
/*  27*/            Expression expression = (new ExpressionParser()).parse(s, staticcontext).simplify();
/*  28*/            expression.staticContext = staticcontext;
/*  29*/            return expression;
                }
/*  31*/        catch(XPathException xpathexception)
                {
/*  31*/            if(staticcontext.forwardsCompatibleModeIsEnabled())
/*  32*/                return new ErrorExpression(xpathexception);
/*  34*/            else
/*  34*/                throw xpathexception;
                }
            }

            public Expression simplify()
                throws XPathException
            {
/*  45*/        return this;
            }

            public final void setStaticContext(StaticContext staticcontext)
            {
/*  53*/        staticContext = staticcontext;
            }

            public final StaticContext getStaticContext()
            {
/*  61*/        return staticContext;
            }

            public boolean containsReferences()
                throws XPathException
            {
/*  70*/        return (getDependencies() & 1) != 0;
            }

            public abstract Value evaluate(Context context)
                throws XPathException;

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  91*/        return evaluate(context).asBoolean();
            }

            public double evaluateAsNumber(Context context)
                throws XPathException
            {
/* 104*/        return evaluate(context).asNumber();
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/* 117*/        return evaluate(context).asString();
            }

            public void outputStringValue(Outputter outputter, Context context)
                throws TransformerException
            {
/* 128*/        outputter.writeContent(evaluateAsString(context));
            }

            public NodeSetValue evaluateAsNodeSet(Context context)
                throws XPathException
            {
/* 142*/        Value value = evaluate(context);
/* 143*/        if(value instanceof NodeSetValue)
/* 144*/            return (NodeSetValue)value;
/* 145*/        else
/* 145*/            throw new XPathException("The value is not a node-set");
            }

            public NodeEnumeration enumerate(Context context, boolean flag)
                throws XPathException
            {
/* 158*/        Value value = evaluate(context);
/* 159*/        if(value instanceof NodeSetValue)
                {
/* 160*/            if(flag)
/* 161*/                ((NodeSetValue)value).sort();
/* 163*/            NodeEnumeration nodeenumeration = ((NodeSetValue)value).enumerate();
/* 164*/            return nodeenumeration;
                } else
                {
/* 166*/            throw new XPathException("The value is not a node-set");
                }
            }

            public abstract int getDataType();

            public boolean isContextDocumentNodeSet()
            {
/* 184*/        return false;
            }

            public boolean usesCurrent()
            {
/* 193*/        return (getDependencies() & 4) != 0;
            }

            public abstract int getDependencies();

            public abstract Expression reduce(int i, Context context)
                throws XPathException;

            public abstract void display(int i);

            protected static String indent(int i)
            {
/* 225*/        String s = "";
/* 226*/        for(int j = 0; j < i; j++)
/* 227*/            s = s + "  ";

/* 229*/        return s;
            }
}
