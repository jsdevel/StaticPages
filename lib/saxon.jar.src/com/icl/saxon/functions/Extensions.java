// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Extensions.java

package com.icl.saxon.functions;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.expr.*;
import com.icl.saxon.exslt.Math;
import com.icl.saxon.om.*;
import com.icl.saxon.output.Emitter;
import com.icl.saxon.pattern.AnyNodeTest;
import com.icl.saxon.tinytree.TinyBuilder;
import com.icl.saxon.tree.AttributeCollection;
import java.io.PrintStream;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.functions:
//            Not, Round

public class Extensions
{

            private static final String NODE_TYPE_NAMES[] = {
/* 618*/        "Node", "Element", "Attribute", "Text", "?", "?", "?", "Processing Instruction", "Comment", "Root", 
/* 618*/        "?", "?", "?", "Namespace"
            };

            public Extensions()
            {
            }

            public static void pauseTracing(Context context)
            {
/*  30*/        context.getController().pauseTracing(true);
            }

            public static void resumeTracing(Context context)
            {
/*  33*/        context.getController().pauseTracing(false);
            }

            public static NodeSetValue nodeSet(Context context, Value value)
                throws XPathException
            {
/*  42*/        if(value instanceof SingletonNodeSet)
/*  43*/            ((SingletonNodeSet)value).allowGeneralUse();
/*  45*/        if(value instanceof NodeSetValue)
                {
/*  46*/            return (NodeSetValue)value;
                } else
                {
/*  48*/            TextFragmentValue textfragmentvalue = new TextFragmentValue(value.asString(), "", context.getController());
/*  50*/            textfragmentvalue.allowGeneralUse();
/*  51*/            return textfragmentvalue;
                }
            }

            public static NodeSetValue nodeset(Context context, Value value)
                throws XPathException
            {
/*  60*/        return nodeSet(context, value);
            }

            public static String systemId(Context context)
                throws XPathException
            {
/*  68*/        return context.getContextNodeInfo().getSystemId();
            }

            public static double lineNumber(Context context)
                throws XPathException
            {
/*  77*/        return (double)context.getContextNodeInfo().getLineNumber();
            }

            public static String baseUri(Context context)
                throws XPathException
            {
/*  85*/        return context.getContextNodeInfo().getBaseURI();
            }

            public static NodeEnumeration intersection(Context context, NodeEnumeration nodeenumeration, NodeEnumeration nodeenumeration1)
                throws XPathException
            {
/*  96*/        return new IntersectionEnumeration(nodeenumeration, nodeenumeration1, context.getController());
            }

            public static NodeEnumeration difference(Context context, NodeEnumeration nodeenumeration, NodeEnumeration nodeenumeration1)
                throws XPathException
            {
/* 107*/        return new DifferenceEnumeration(nodeenumeration, nodeenumeration1, context.getController());
            }

            public static boolean hasSameNodes(Context context, NodeEnumeration nodeenumeration, NodeEnumeration nodeenumeration1)
                throws XPathException
            {
/* 118*/        NodeEnumeration nodeenumeration2 = nodeenumeration;
/* 119*/        NodeEnumeration nodeenumeration3 = nodeenumeration1;
/* 120*/        Controller controller = context.getController();
/* 121*/        if(!nodeenumeration2.isSorted())
/* 122*/            nodeenumeration2 = (new NodeSetExtent(nodeenumeration2, controller)).sort().enumerate();
/* 124*/        if(!nodeenumeration3.isSorted())
/* 125*/            nodeenumeration3 = (new NodeSetExtent(nodeenumeration3, controller)).sort().enumerate();
/* 128*/        while(nodeenumeration2.hasMoreElements()) 
                {
/* 128*/            if(!nodeenumeration3.hasMoreElements())
/* 129*/                return false;
/* 131*/            if(!nodeenumeration2.nextElement().isSameNodeInfo(nodeenumeration3.nextElement()))
/* 132*/                return false;
                }
/* 135*/        return !nodeenumeration3.hasMoreElements();
            }

            public static Value IF(Value value, Value value1, Value value2)
                throws XPathException
            {
/* 152*/        return value.asBoolean() ? value1 : value2;
            }

            public static Value evaluate(Context context, String s)
                throws XPathException
            {
/* 160*/        StaticContext staticcontext = context.getStaticContext().makeRuntimeContext(context.getController().getNamePool());
/* 162*/        Expression expression1 = Expression.make(s, staticcontext);
/* 163*/        return expression1.evaluate(context);
            }

            public static Value eval(Context context, Expression expression1)
                throws XPathException
            {
/* 171*/        return expression1.evaluate(context);
            }

            public static Value expression(Context context, String s)
                throws XPathException
            {
/* 180*/        StaticContext staticcontext = context.getStaticContext().makeRuntimeContext(context.getController().getNamePool());
/* 182*/        Expression expression1 = Expression.make(s, staticcontext);
/* 184*/        Expression expression2 = expression1.reduce(1, context).simplify();
/* 185*/        return new ObjectValue(expression2);
            }

            public static double sum(Context context, NodeEnumeration nodeenumeration, Expression expression1)
                throws XPathException
            {
/* 195*/        double d = 0.0D;
/* 196*/        Context context1 = context.newContext();
                Object obj;
/* 198*/        if(nodeenumeration instanceof LastPositionFinder)
/* 199*/            obj = nodeenumeration;
/* 201*/        else
/* 201*/            obj = new LookaheadEnumerator(nodeenumeration);
/* 203*/        context1.setLastPositionFinder((LastPositionFinder)obj);
/* 204*/        int i = 1;
/* 206*/        while(((NodeEnumeration) (obj)).hasMoreElements()) 
                {
/* 206*/            context1.setContextNode(((NodeEnumeration) (obj)).nextElement());
/* 207*/            context1.setPosition(i++);
/* 208*/            double d1 = expression1.evaluateAsNumber(context1);
/* 209*/            d += d1;
                }
/* 211*/        return d;
            }

            public static double max(NodeEnumeration nodeenumeration)
                throws XPathException
            {
/* 219*/        return Math.max(nodeenumeration);
            }

            public static double max(Context context, NodeEnumeration nodeenumeration, Expression expression1)
                throws XPathException
            {
/* 230*/        double d = (-1.0D / 0.0D);
/* 231*/        Context context1 = context.newContext();
                Object obj;
/* 233*/        if(nodeenumeration instanceof LastPositionFinder)
/* 234*/            obj = nodeenumeration;
/* 236*/        else
/* 236*/            obj = new LookaheadEnumerator(nodeenumeration);
/* 238*/        context1.setLastPositionFinder((LastPositionFinder)obj);
/* 239*/        int i = 1;
/* 241*/        while(((NodeEnumeration) (obj)).hasMoreElements()) 
                {
/* 241*/            context1.setContextNode(((NodeEnumeration) (obj)).nextElement());
/* 242*/            context1.setPosition(i++);
/* 243*/            double d1 = expression1.evaluateAsNumber(context1);
/* 244*/            if(d1 > d)
/* 244*/                d = d1;
                }
/* 246*/        return d;
            }

            public static double min(NodeEnumeration nodeenumeration)
                throws XPathException
            {
/* 254*/        return Math.min(nodeenumeration);
            }

            public static double min(Context context, NodeEnumeration nodeenumeration, Expression expression1)
                throws XPathException
            {
/* 264*/        double d = (1.0D / 0.0D);
/* 265*/        Context context1 = context.newContext();
                Object obj;
/* 267*/        if(nodeenumeration instanceof LastPositionFinder)
/* 268*/            obj = nodeenumeration;
/* 270*/        else
/* 270*/            obj = new LookaheadEnumerator(nodeenumeration);
/* 272*/        context1.setLastPositionFinder((LastPositionFinder)obj);
/* 273*/        int i = 1;
/* 275*/        while(((NodeEnumeration) (obj)).hasMoreElements()) 
                {
/* 275*/            context1.setContextNode(((NodeEnumeration) (obj)).nextElement());
/* 276*/            context1.setPosition(i++);
/* 277*/            double d1 = expression1.evaluateAsNumber(context1);
/* 278*/            if(d1 < d)
/* 278*/                d = d1;
                }
/* 280*/        return d;
            }

            public static NodeSetValue highest(Context context, NodeEnumeration nodeenumeration)
                throws XPathException
            {
/* 288*/        return Math.highest(context, nodeenumeration);
            }

            public static NodeEnumeration highest(Context context, NodeEnumeration nodeenumeration, Expression expression1)
                throws XPathException
            {
/* 299*/        double d = (-1.0D / 0.0D);
/* 300*/        Context context1 = context.newContext();
/* 301*/        NodeInfo nodeinfo = null;
                Object obj;
/* 303*/        if(nodeenumeration instanceof LastPositionFinder)
/* 304*/            obj = nodeenumeration;
/* 306*/        else
/* 306*/            obj = new LookaheadEnumerator(nodeenumeration);
/* 308*/        context1.setLastPositionFinder((LastPositionFinder)obj);
/* 309*/        int i = 1;
/* 311*/        while(((NodeEnumeration) (obj)).hasMoreElements()) 
                {
/* 311*/            context1.setContextNode(((NodeEnumeration) (obj)).nextElement());
/* 312*/            context1.setPosition(i++);
/* 313*/            double d1 = expression1.evaluateAsNumber(context1);
/* 314*/            if(d1 > d)
                    {
/* 315*/                d = d1;
/* 316*/                nodeinfo = context1.getContextNodeInfo();
                    }
                }
/* 319*/        return new SingletonEnumeration(nodeinfo);
            }

            public static NodeSetValue lowest(Context context, NodeEnumeration nodeenumeration)
                throws XPathException
            {
/* 327*/        return Math.lowest(context, nodeenumeration);
            }

            public static NodeEnumeration lowest(Context context, NodeEnumeration nodeenumeration, Expression expression1)
                throws XPathException
            {
/* 337*/        double d = (1.0D / 0.0D);
/* 338*/        Context context1 = context.newContext();
/* 339*/        NodeInfo nodeinfo = null;
                Object obj;
/* 341*/        if(nodeenumeration instanceof LastPositionFinder)
/* 342*/            obj = nodeenumeration;
/* 344*/        else
/* 344*/            obj = new LookaheadEnumerator(nodeenumeration);
/* 346*/        context1.setLastPositionFinder((LastPositionFinder)obj);
/* 347*/        int i = 1;
/* 349*/        while(((NodeEnumeration) (obj)).hasMoreElements()) 
                {
/* 349*/            context1.setContextNode(((NodeEnumeration) (obj)).nextElement());
/* 350*/            context1.setPosition(i++);
/* 351*/            double d1 = expression1.evaluateAsNumber(context1);
/* 352*/            if(d1 < d)
                    {
/* 353*/                d = d1;
/* 354*/                nodeinfo = context1.getContextNodeInfo();
                    }
                }
/* 357*/        return new SingletonEnumeration(nodeinfo);
            }

            public static NodeEnumeration distinct(Context context, NodeEnumeration nodeenumeration)
                throws XPathException
            {
/* 365*/        return new DistinctEnumeration(nodeenumeration, context.getController());
            }

            public static NodeEnumeration distinct(Context context, NodeEnumeration nodeenumeration, Expression expression1)
                throws XPathException
            {
/* 376*/        return new DistinctEnumeration(context, nodeenumeration, expression1);
            }

            public static NodeEnumeration closure(Context context, NodeEnumeration nodeenumeration, Expression expression1)
                throws XPathException
            {
/* 386*/        Object obj = EmptyEnumeration.getInstance();
/* 387*/        Controller controller = context.getController();
/* 389*/        while(nodeenumeration.hasMoreElements()) 
                {
/* 389*/            NodeInfo nodeinfo = nodeenumeration.nextElement();
/* 390*/            Context context1 = context.newContext();
/* 391*/            context1.setContextNode(nodeinfo);
/* 392*/            context1.setCurrentNode(nodeinfo);
/* 393*/            context1.setPosition(1);
/* 394*/            context1.setLast(1);
/* 395*/            UnionEnumeration unionenumeration = new UnionEnumeration(new SingletonEnumeration(nodeinfo), closure(context1, expression1.enumerate(context1, false), expression1), controller);
/* 400*/            obj = new UnionEnumeration(((NodeEnumeration) (obj)), unionenumeration, controller);
                }
/* 402*/        return ((NodeEnumeration) (obj));
            }

            public static NodeEnumeration leading(Context context, NodeEnumeration nodeenumeration, Expression expression1)
                throws XPathException
            {
/* 412*/        return new FilterEnumerator(nodeenumeration, expression1, context.newContext(), true);
            }

            public static NodeSetValue before(Context context, NodeSetValue nodesetvalue, NodeSetValue nodesetvalue1)
                throws XPathException
            {
/* 425*/        NodeInfo nodeinfo = null;
/* 426*/        for(NodeEnumeration nodeenumeration = nodesetvalue1.enumerate(); nodeenumeration.hasMoreElements();)
/* 428*/            nodeinfo = nodeenumeration.nextElement();

/* 430*/        if(nodeinfo == null)
/* 431*/            return new EmptyNodeSet();
/* 433*/        Controller controller = context.getController();
/* 435*/        Vector vector = new Vector();
                NodeInfo nodeinfo1;
/* 436*/        for(NodeEnumeration nodeenumeration1 = nodesetvalue.enumerate(); nodeenumeration1.hasMoreElements(); vector.addElement(nodeinfo1))
                {
/* 438*/            nodeinfo1 = nodeenumeration1.nextElement();
/* 439*/            if(controller.compare(nodeinfo1, nodeinfo) >= 0)
/* 440*/                break;
                }

/* 445*/        return new NodeSetExtent(vector, controller);
            }

            public static NodeSetValue after(Context context, NodeSetValue nodesetvalue, NodeSetValue nodesetvalue1)
                throws XPathException
            {
/* 458*/        NodeInfo nodeinfo = nodesetvalue1.getFirst();
/* 459*/        if(nodeinfo == null)
/* 460*/            return new EmptyNodeSet();
/* 462*/        Controller controller = context.getController();
/* 464*/        Vector vector = new Vector();
/* 465*/        NodeEnumeration nodeenumeration = nodesetvalue.enumerate();
/* 466*/        boolean flag = false;
/* 468*/        while(nodeenumeration.hasMoreElements()) 
                {
/* 468*/            NodeInfo nodeinfo1 = nodeenumeration.nextElement();
/* 469*/            if(flag)
/* 470*/                vector.addElement(nodeinfo1);
/* 471*/            else
/* 471*/            if(controller.compare(nodeinfo1, nodeinfo) > 0)
                    {
/* 472*/                flag = true;
/* 473*/                vector.addElement(nodeinfo1);
                    }
                }
/* 476*/        return new NodeSetExtent(vector, controller);
            }

            public static boolean exists(Context context, NodeEnumeration nodeenumeration, Expression expression1)
                throws XPathException
            {
/* 487*/        return (new FilterEnumerator(nodeenumeration, expression1, context.newContext(), false)).hasMoreElements();
            }

            public static boolean forAll(Context context, NodeEnumeration nodeenumeration, Expression expression1)
                throws XPathException
            {
/* 498*/        Not not = new Not();
/* 499*/        not.addArgument(expression1);
/* 500*/        return !(new FilterEnumerator(nodeenumeration, not, context.newContext(), false)).hasMoreElements();
            }

            public static NodeEnumeration range(Context context, double d, double d1)
                throws XPathException
            {
/* 510*/        int i = (int)Round.round(d);
/* 511*/        int j = (int)Round.round(d1);
/* 514*/        try
                {
/* 514*/            TinyBuilder tinybuilder = new TinyBuilder();
/* 515*/            NamePool namepool = context.getController().getNamePool();
/* 516*/            int ai[] = new int[1];
/* 517*/            ai[0] = namepool.getNamespaceCode("saxon", "http://icl.com/saxon");
/* 518*/            int k = namepool.allocate("saxon", "http://icl.com/saxon", "range");
/* 519*/            tinybuilder.setNamePool(namepool);
/* 520*/            tinybuilder.startDocument();
/* 521*/            AttributeCollection attributecollection = new AttributeCollection(namepool);
/* 523*/            for(int l = i; l <= j; l++)
                    {
/* 524*/                tinybuilder.startElement(k, attributecollection, ai, 1);
/* 525*/                String s = l + "";
/* 526*/                tinybuilder.characters(s.toCharArray(), 0, s.length());
/* 527*/                tinybuilder.endElement(k);
                    }

/* 530*/            tinybuilder.endDocument();
/* 531*/            com.icl.saxon.om.DocumentInfo documentinfo = tinybuilder.getCurrentDocument();
/* 532*/            return documentinfo.getEnumeration((byte)3, AnyNodeTest.getInstance());
                }
/* 535*/        catch(TransformerException transformerexception)
                {
/* 535*/            throw new XPathException(transformerexception);
                }
            }

            public static NodeEnumeration tokenize(Context context, String s)
                throws XPathException
            {
/* 548*/        try
                {
/* 548*/            Builder builder = context.getController().makeBuilder();
/* 549*/            NamePool namepool = context.getController().getNamePool();
/* 550*/            builder.startDocument();
/* 551*/            int ai[] = new int[1];
/* 552*/            ai[0] = namepool.getNamespaceCode("saxon", "http://icl.com/saxon");
/* 553*/            int i = namepool.allocate("saxon", "http://icl.com/saxon", "token");
/* 554*/            AttributeCollection attributecollection = new AttributeCollection(namepool);
/* 556*/            for(StringTokenizer stringtokenizer = new StringTokenizer(s); stringtokenizer.hasMoreTokens(); builder.endElement(i))
                    {
/* 558*/                builder.startElement(i, attributecollection, ai, 1);
/* 559*/                String s1 = stringtokenizer.nextToken();
/* 560*/                builder.characters(s1.toCharArray(), 0, s1.length());
                    }

/* 564*/            builder.endDocument();
/* 565*/            com.icl.saxon.om.DocumentInfo documentinfo = builder.getCurrentDocument();
/* 566*/            return documentinfo.getEnumeration((byte)3, AnyNodeTest.getInstance());
                }
/* 568*/        catch(TransformerException transformerexception)
                {
/* 568*/            throw new XPathException(transformerexception);
                }
            }

            public static NodeEnumeration tokenize(Context context, String s, String s1)
                throws XPathException
            {
/* 580*/        try
                {
/* 580*/            Builder builder = context.getController().makeBuilder();
/* 581*/            NamePool namepool = context.getController().getNamePool();
/* 582*/            builder.setNamePool(namepool);
/* 583*/            builder.startDocument();
/* 584*/            int ai[] = new int[1];
/* 585*/            ai[0] = namepool.getNamespaceCode("saxon", "http://icl.com/saxon");
/* 586*/            int i = namepool.allocate("saxon", "http://icl.com/saxon", "token");
/* 587*/            AttributeCollection attributecollection = new AttributeCollection(namepool);
/* 589*/            for(StringTokenizer stringtokenizer = new StringTokenizer(s, s1); stringtokenizer.hasMoreTokens(); builder.endElement(i))
                    {
/* 591*/                builder.startElement(i, attributecollection, ai, 1);
/* 592*/                String s2 = stringtokenizer.nextToken();
/* 593*/                builder.characters(s2.toCharArray(), 0, s2.length());
                    }

/* 597*/            builder.endDocument();
/* 598*/            com.icl.saxon.om.DocumentInfo documentinfo = builder.getCurrentDocument();
/* 599*/            return documentinfo.getEnumeration((byte)3, AnyNodeTest.getInstance());
                }
/* 601*/        catch(TransformerException transformerexception)
                {
/* 601*/            throw new XPathException(transformerexception);
                }
            }

            public static String path(Context context)
                throws XPathException
            {
/* 611*/        return Navigator.getPath(context.getContextNodeInfo());
            }

            public static String showNodeset(Context context, NodeSetValue nodesetvalue)
                throws XPathException
            {
/* 627*/        System.err.println("Node-set contents at line " + context.getStaticContext().getLineNumber() + " [");
/* 628*/        NodeEnumeration nodeenumeration = nodesetvalue.enumerate(context, true);
/* 629*/        int i = 0;
                NodeInfo nodeinfo;
                String s;
/* 631*/        for(; nodeenumeration.hasMoreElements(); System.err.println("  " + s + " " + nodeinfo.getDisplayName() + " " + Navigator.getPath(nodeinfo) + " " + nodeinfo.generateId()))
                {
/* 631*/            i++;
/* 632*/            nodeinfo = nodeenumeration.nextElement();
/* 633*/            s = NODE_TYPE_NAMES[nodeinfo.getNodeType()];
                }

/* 639*/        System.err.println("] (Total number of nodes: " + i + ")");
/* 640*/        return "";
            }

            public static boolean isNull(Object obj)
                throws XPathException
            {
/* 649*/        return obj == null;
            }

            public static void setUserData(Context context, String s, Value value)
                throws XPathException
            {
/* 659*/        context.getController().setUserData(context.getContextNodeInfo(), s, value);
            }

            public static Value getUserData(Context context, String s)
                throws XPathException
            {
/* 668*/        Object obj = context.getController().getUserData(context.getContextNodeInfo(), s);
/* 671*/        if(obj == null)
/* 671*/            return new StringValue("");
/* 672*/        if(obj instanceof Value)
/* 672*/            return (Value)obj;
/* 673*/        else
/* 673*/            return new ObjectValue(obj);
            }

            public static Context getContext(Context context)
            {
/* 681*/        return context;
            }

            public static String getPseudoAttribute(Context context, String s)
            {
/* 691*/        NodeInfo nodeinfo = context.getContextNodeInfo();
/* 692*/        if(nodeinfo.getNodeType() != 7)
/* 692*/            return "";
/* 693*/        String s1 = ProcInstParser.getPseudoAttribute(nodeinfo.getStringValue(), s);
/* 694*/        if(s1 == null)
/* 694*/            return "";
/* 695*/        else
/* 695*/            return s1;
            }

}
