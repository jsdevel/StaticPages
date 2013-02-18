// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   PathExpression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.om.*;
import com.icl.saxon.pattern.*;
import com.icl.saxon.sort.LocalOrderComparer;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            NodeSetExpression, EmptyNodeSet, RootExpression, ContextNodeExpression, 
//            AttributeReference, AxisExpression, Step, NodeSetValue, 
//            NodeSetIntent, SingletonNodeSet, NodeSetExtent, XPathException, 
//            Expression, SingletonExpression

public class PathExpression extends NodeSetExpression
{
    private class PathEnumeration
        implements NodeEnumeration
    {

                private Expression thisStart;
                private NodeEnumeration base;
                private NodeEnumeration thisStep;
                private NodeInfo next;
                private Context context;

                public boolean hasMoreElements()
                {
/* 277*/            return next != null;
                }

                public NodeInfo nextElement()
                    throws XPathException
                {
/* 281*/            NodeInfo nodeinfo = next;
/* 282*/            next = getNextNode();
/* 283*/            return nodeinfo;
                }

                private NodeInfo getNextNode()
                    throws XPathException
                {
/* 291*/            if(thisStep != null && thisStep.hasMoreElements())
/* 292*/                return thisStep.nextElement();
/* 299*/            while(base.hasMoreElements()) 
                    {
/* 299*/                NodeInfo nodeinfo = base.nextElement();
/* 301*/                thisStep = step.enumerate(nodeinfo, context);
/* 302*/                if(thisStep.hasMoreElements())
/* 303*/                    return thisStep.nextElement();
                    }
/* 310*/            return null;
                }

                public boolean isSorted()
                {
/* 321*/            byte byte0 = step.getAxis();
/* 322*/            return Axis.isForwards[byte0] && ((thisStart instanceof SingletonExpression) || base.isSorted() && base.isPeer() && Axis.isSubtreeAxis[byte0] || base.isSorted() && (byte0 == 2 || byte0 == 8));
                }

                public boolean isReverseSorted()
                {
/* 335*/            return (thisStart instanceof SingletonExpression) && Axis.isReverse[step.getAxis()];
                }

                public boolean isPeer()
                {
/* 344*/            return base.isPeer() && Axis.isPeerAxis[step.getAxis()];
                }

                public PathEnumeration(Expression expression, Context context1)
                    throws XPathException
                {
/* 259*/            base = null;
/* 260*/            thisStep = null;
/* 261*/            next = null;
/* 265*/            if((expression instanceof SingletonNodeSet) && !((SingletonNodeSet)expression).isGeneralUseAllowed())
                    {
/* 267*/                throw new XPathException("To use a result tree fragment in a path expression, either use exsl:node-set() or specify version='1.1'");
                    } else
                    {
/* 270*/                thisStart = expression;
/* 271*/                context = context1.newContext();
/* 272*/                base = expression.enumerate(context, false);
/* 273*/                next = getNextNode();
/* 274*/                return;
                    }
                }
    }


            private Expression start;
            private Step step;
            int dependencies;

            public PathExpression(Expression expression, Step step1)
            {
/*  22*/        dependencies = -1;
/*  33*/        start = expression;
/*  34*/        step = step1;
            }

            public Expression simplify()
                throws XPathException
            {
/*  44*/        start = start.simplify();
/*  45*/        step = step.simplify();
/*  48*/        if(start instanceof EmptyNodeSet)
/*  49*/            return start;
/*  53*/        if(step == null)
/*  54*/            return new EmptyNodeSet();
/*  57*/        byte byte0 = step.getAxis();
/*  61*/        if((start instanceof RootExpression) && byte0 == 9)
/*  62*/            return new EmptyNodeSet();
/*  67*/        if((start instanceof ContextNodeExpression) && byte0 == 2 && (step.getNodeTest() instanceof NameTest) && step.getNumberOfFilters() == 0)
/*  72*/            return new AttributeReference(step.getNodeTest().getFingerprint());
/*  78*/        if((start instanceof ContextNodeExpression) && step.getNumberOfFilters() == 0)
/*  80*/            return new AxisExpression(byte0, step.getNodeTest());
/*  88*/        if(byte0 == 3 && step.getNumberOfFilters() == 0 && (start instanceof PathExpression) && ((PathExpression)start).step.getAxis() == 5 && ((PathExpression)start).step.getNumberOfFilters() == 0 && (((PathExpression)start).step.getNodeTest() instanceof AnyNodeTest))
/* 108*/            return new PathExpression(((PathExpression)start).start, new Step((byte)4, step.getNodeTest()));
/* 114*/        else
/* 114*/            return this;
            }

            public int getDependencies()
            {
/* 124*/        if(dependencies == -1)
                {
/* 125*/            dependencies = start.getDependencies();
/* 126*/            Expression aexpression[] = step.getFilters();
/* 127*/            for(int i = 0; i < step.getNumberOfFilters(); i++)
                    {
/* 128*/                Expression expression = aexpression[i];
/* 131*/                dependencies |= expression.getDependencies() & 0x45;
                    }

                }
/* 135*/        return dependencies;
            }

            public boolean isContextDocumentNodeSet()
            {
/* 145*/        return start.isContextDocumentNodeSet();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/* 158*/        Object obj = this;
/* 159*/        if((i & getDependencies()) != 0)
                {
/* 160*/            Expression expression = start.reduce(i, context);
/* 161*/            Step step1 = new Step(step.getAxis(), step.getNodeTest());
/* 162*/            Expression aexpression[] = step.getFilters();
/* 164*/            int j = i & 0x45;
/* 165*/            if(start.isContextDocumentNodeSet() && (i & 0x80) != 0)
/* 167*/                j |= 0x80;
/* 170*/            for(int k = 0; k < step.getNumberOfFilters(); k++)
                    {
/* 171*/                Expression expression1 = aexpression[k];
/* 174*/                Expression expression2 = expression1.reduce(j, context);
/* 175*/                step1.addFilter(expression2);
                    }

/* 177*/            obj = new PathExpression(expression, step1);
/* 178*/            ((Expression) (obj)).setStaticContext(getStaticContext());
/* 179*/            obj = ((Expression) (obj)).simplify();
                }
/* 186*/        if((obj instanceof PathExpression) && (((PathExpression)obj).start instanceof NodeSetValue))
/* 188*/            return new NodeSetIntent((PathExpression)obj, context.getController());
/* 191*/        else
/* 191*/            return ((Expression) (obj));
            }

            public NodeEnumeration enumerate(Context context, boolean flag)
                throws XPathException
            {
/* 208*/        int i = getDependencies();
/* 209*/        int j = 0;
/* 211*/        if((i & 0x45) != 0)
/* 212*/            j |= 0x45;
/* 215*/        if(start.isContextDocumentNodeSet() && (i & 0x80) != 0)
/* 217*/            j |= 0x80;
/* 220*/        if((j & 0xc5) != 0)
                {
/* 221*/            Expression expression = reduce(j, context);
/* 222*/            return expression.enumerate(context, flag);
                }
/* 225*/        PathEnumeration pathenumeration = new PathEnumeration(start, context);
/* 226*/        if(flag && !pathenumeration.isSorted())
                {
                    Object obj;
/* 228*/            if((start instanceof SingletonNodeSet) || start.isContextDocumentNodeSet())
/* 230*/                obj = LocalOrderComparer.getInstance();
/* 232*/            else
/* 232*/                obj = context.getController();
/* 234*/            NodeSetExtent nodesetextent = new NodeSetExtent(pathenumeration, ((com.icl.saxon.sort.NodeOrderComparer) (obj)));
/* 235*/            nodesetextent.sort();
/* 236*/            return nodesetextent.enumerate();
                } else
                {
/* 238*/            return pathenumeration;
                }
            }

            public void display(int i)
            {
/* 246*/        System.err.println(Expression.indent(i) + "path");
/* 247*/        start.display(i + 1);
/* 248*/        step.display(i + 1);
            }

}
