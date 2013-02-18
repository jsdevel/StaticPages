// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Context.java

package com.icl.saxon;

import com.icl.saxon.expr.BooleanValue;
import com.icl.saxon.expr.LastPositionFinder;
import com.icl.saxon.expr.NumericValue;
import com.icl.saxon.expr.StaticContext;
import com.icl.saxon.expr.StringValue;
import com.icl.saxon.expr.Value;
import com.icl.saxon.expr.XPathException;
import com.icl.saxon.functions.SystemProperty;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.style.XSLTemplate;
import java.util.Stack;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.xsl.XSLTContext;

// Referenced classes of package com.icl.saxon:
//            Controller, Mode, ParameterSet, Bindery

public final class Context
    implements XSLTContext, LastPositionFinder
{

            public static final int VARIABLES = 1;
            public static final int CURRENT_NODE = 4;
            public static final int CONTEXT_NODE = 8;
            public static final int POSITION = 16;
            public static final int LAST = 32;
            public static final int CONTROLLER = 64;
            public static final int CONTEXT_DOCUMENT = 128;
            public static final int NO_DEPENDENCIES = 0;
            public static final int ALL_DEPENDENCIES = 255;
            public static final int XSLT_CONTEXT = 69;
            private NodeInfo contextNode;
            private NodeInfo currentNode;
            private int position;
            private int last;
            private LastPositionFinder lastPositionFinder;
            private Controller controller;
            private Mode currentMode;
            private XSLTemplate currentTemplate;
            private Stack groupActivationStack;
            private StaticContext staticContext;
            private ParameterSet tailRecursion;
            private NodeInfo lastRememberedNode;
            private int lastRememberedNumber;
            private Value returnValue;
            private XPathException exception;
            private static Controller defaultController = null;

            public Context()
            {
/*  40*/        position = -1;
/*  41*/        last = -1;
/*  50*/        lastRememberedNode = null;
/*  51*/        lastRememberedNumber = -1;
/*  52*/        returnValue = null;
/*  53*/        exception = null;
/*  67*/        if(defaultController == null)
/*  68*/            defaultController = new Controller();
/*  70*/        controller = defaultController;
/*  71*/        lastPositionFinder = this;
            }

            public Context(Controller controller1)
            {
/*  40*/        position = -1;
/*  41*/        last = -1;
/*  50*/        lastRememberedNode = null;
/*  51*/        lastRememberedNumber = -1;
/*  52*/        returnValue = null;
/*  53*/        exception = null;
/*  80*/        controller = controller1;
/*  81*/        lastPositionFinder = this;
            }

            public Context newContext()
            {
/*  89*/        Context context = new Context(controller);
/*  90*/        context.staticContext = staticContext;
/*  91*/        context.currentNode = currentNode;
/*  92*/        context.contextNode = contextNode;
/*  93*/        context.position = position;
/*  94*/        context.last = last;
/*  95*/        context.lastPositionFinder = lastPositionFinder;
/*  96*/        context.currentMode = currentMode;
/*  97*/        context.currentTemplate = currentTemplate;
/*  99*/        context.groupActivationStack = groupActivationStack;
/* 100*/        context.lastRememberedNode = lastRememberedNode;
/* 101*/        context.lastRememberedNumber = lastRememberedNumber;
/* 102*/        context.returnValue = null;
/* 103*/        return context;
            }

            public void setController(Controller controller1)
            {
/* 111*/        controller = controller1;
            }

            public Controller getController()
            {
/* 119*/        return controller;
            }

            public Bindery getBindery()
            {
/* 127*/        return controller.getBindery();
            }

            public Outputter getOutputter()
            {
/* 137*/        return controller.getOutputter();
            }

            public void setMode(Mode mode)
            {
/* 145*/        currentMode = mode;
            }

            public Mode getMode()
            {
/* 153*/        return currentMode;
            }

            public void setContextNode(NodeInfo nodeinfo)
            {
/* 163*/        contextNode = nodeinfo;
            }

            public NodeInfo getContextNodeInfo()
            {
/* 172*/        return contextNode;
            }

            public Node getContextNode()
            {
/* 181*/        if(contextNode instanceof Node)
/* 182*/            return (Node)contextNode;
/* 184*/        else
/* 184*/            return null;
            }

            public void setPosition(int i)
            {
/* 193*/        position = i;
            }

            public int getContextPosition()
            {
/* 202*/        return position;
            }

            public void setLast(int i)
            {
/* 211*/        last = i;
/* 212*/        lastPositionFinder = this;
            }

            public void setLastPositionFinder(LastPositionFinder lastpositionfinder)
            {
/* 221*/        lastPositionFinder = lastpositionfinder;
            }

            public int getLast()
                throws XPathException
            {
/* 230*/        if(lastPositionFinder == null)
/* 230*/            return 1;
/* 231*/        else
/* 231*/            return lastPositionFinder.getLastPosition();
            }

            public boolean isAtLast()
                throws XPathException
            {
/* 242*/        if(lastPositionFinder != null && (lastPositionFinder instanceof NodeEnumeration))
/* 243*/            return !((NodeEnumeration)lastPositionFinder).hasMoreElements();
/* 245*/        else
/* 245*/            return getContextPosition() == getLast();
            }

            public int getContextSize()
            {
/* 260*/        try
                {
/* 260*/            return getLast();
                }
/* 264*/        catch(XPathException xpathexception)
                {
/* 264*/            setException(xpathexception);
                }
/* 265*/        return getContextPosition();
            }

            public int getLastPosition()
            {
/* 275*/        return last;
            }

            public void setCurrentNode(NodeInfo nodeinfo)
            {
/* 284*/        currentNode = nodeinfo;
            }

            public NodeInfo getCurrentNodeInfo()
            {
/* 294*/        return currentNode;
            }

            public Node getCurrentNode()
            {
/* 306*/        if(currentNode instanceof Node)
/* 307*/            return (Node)currentNode;
/* 309*/        else
/* 309*/            return null;
            }

            public void setCurrentTemplate(XSLTemplate xsltemplate)
            {
/* 318*/        currentTemplate = xsltemplate;
            }

            public XSLTemplate getCurrentTemplate()
            {
/* 326*/        return currentTemplate;
            }

            public Document getOwnerDocument()
            {
/* 334*/        return (Document)(Node)contextNode.getDocumentRoot();
            }

            public Object systemProperty(String s, String s1)
            {
/* 343*/        try
                {
/* 343*/            Value value = SystemProperty.getProperty(s, s1);
/* 344*/            if(value == null)
/* 345*/                return null;
/* 346*/            if(value instanceof StringValue)
/* 347*/                return value.asString();
/* 348*/            if(value instanceof NumericValue)
/* 349*/                return new Double(value.asNumber());
/* 350*/            if(value instanceof BooleanValue)
/* 351*/                return new Boolean(value.asBoolean());
/* 353*/            else
/* 353*/                return value;
                }
/* 356*/        catch(Exception exception1)
                {
/* 356*/            return null;
                }
            }

            public String stringValue(Node node)
            {
/* 366*/        if(node instanceof NodeInfo)
/* 367*/            return ((NodeInfo)node).getStringValue();
/* 369*/        else
/* 369*/            throw new IllegalArgumentException("Node is not a Saxon node");
            }

            public void setStaticContext(StaticContext staticcontext)
            {
/* 378*/        staticContext = staticcontext;
            }

            public StaticContext getStaticContext()
            {
/* 387*/        return staticContext;
            }

            public void setException(XPathException xpathexception)
            {
/* 397*/        exception = xpathexception;
            }

            public XPathException getException()
            {
/* 405*/        return exception;
            }

            public Stack getGroupActivationStack()
            {
/* 413*/        if(groupActivationStack == null)
/* 414*/            groupActivationStack = new Stack();
/* 416*/        return groupActivationStack;
            }

            public void setRememberedNumber(NodeInfo nodeinfo, int i)
            {
/* 424*/        lastRememberedNode = nodeinfo;
/* 425*/        lastRememberedNumber = i;
            }

            public int getRememberedNumber(NodeInfo nodeinfo)
            {
/* 434*/        if(lastRememberedNode == nodeinfo)
/* 434*/            return lastRememberedNumber;
/* 435*/        else
/* 435*/            return -1;
            }

            public void setTailRecursion(ParameterSet parameterset)
            {
/* 443*/        tailRecursion = parameterset;
            }

            public ParameterSet getTailRecursion()
            {
/* 451*/        return tailRecursion;
            }

            public void setReturnValue(Value value)
                throws TransformerException
            {
/* 459*/        if(value != null && returnValue != null)
                {
/* 460*/            throw new TransformerException("A function can only return one result");
                } else
                {
/* 462*/            returnValue = value;
/* 463*/            return;
                }
            }

            public Value getReturnValue()
            {
/* 470*/        return returnValue;
            }

}
