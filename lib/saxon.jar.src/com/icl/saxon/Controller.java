// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Controller.java

package com.icl.saxon;

import com.icl.saxon.expr.Expression;
import com.icl.saxon.expr.FunctionProxy;
import com.icl.saxon.expr.LastPositionFinder;
import com.icl.saxon.expr.LookaheadEnumerator;
import com.icl.saxon.expr.SingletonNodeSet;
import com.icl.saxon.expr.StandaloneContext;
import com.icl.saxon.expr.StringValue;
import com.icl.saxon.om.Builder;
import com.icl.saxon.om.DocumentInfo;
import com.icl.saxon.om.DocumentPool;
import com.icl.saxon.om.NamePool;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.om.Stripper;
import com.icl.saxon.output.ContentHandlerProxy;
import com.icl.saxon.output.Emitter;
import com.icl.saxon.output.GeneralOutputter;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.output.ProxyEmitter;
import com.icl.saxon.output.SaxonOutputKeys;
import com.icl.saxon.output.StringOutputter;
import com.icl.saxon.pattern.AnyNodeTest;
import com.icl.saxon.sort.NodeOrderComparer;
import com.icl.saxon.style.TerminationException;
import com.icl.saxon.style.XSLStyleSheet;
import com.icl.saxon.tinytree.TinyBuilder;
import com.icl.saxon.trace.SaxonEventMulticaster;
import com.icl.saxon.trace.TraceListener;
import com.icl.saxon.tree.DocumentImpl;
import com.icl.saxon.tree.TreeBuilder;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.TransformerHandler;
import org.w3c.dom.Node;
import org.xml.sax.SAXParseException;

// Referenced classes of package com.icl.saxon:
//            TransformerFactoryImpl, Bindery, StandardURIResolver, StandardErrorListener, 
//            Context, Mode, ParameterSet, RuleManager, 
//            NodeHandler, PreparedStyleSheet, Loader, DecimalFormatManager, 
//            KeyManager

public class Controller extends Transformer
    implements NodeOrderComparer
{

            public static final int RECOVER_SILENTLY = 0;
            public static final int RECOVER_WITH_WARNINGS = 1;
            public static final int DO_NOT_RECOVER = 2;
            private TransformerFactoryImpl factory;
            private Bindery bindery;
            private NamePool namePool;
            private DecimalFormatManager decimalFormatManager;
            private Emitter messageEmitter;
            private RuleManager ruleManager;
            private Properties outputProperties;
            private Outputter currentOutputter;
            private ParameterSet parameters;
            private PreparedStyleSheet preparedStyleSheet;
            private TraceListener traceListener;
            private boolean tracingIsSuspended;
            private URIResolver standardURIResolver;
            private URIResolver userURIResolver;
            private ErrorListener errorListener;
            private XSLStyleSheet styleSheetElement;
            private int recoveryPolicy;
            private int treeModel;
            private boolean disableStripping;
            private DocumentPool sourceDocumentPool;
            private Hashtable userDataTable;
            private boolean lineNumbering;
            private boolean preview;
            private String diagnosticName;

            public Controller()
            {
/*  81*/        this(new TransformerFactoryImpl());
/*  82*/        bindery = new Bindery();
            }

            protected Controller(TransformerFactoryImpl transformerfactoryimpl)
            {
/*  59*/        tracingIsSuspended = false;
/*  64*/        recoveryPolicy = 1;
/*  65*/        treeModel = 1;
/*  66*/        disableStripping = false;
/*  72*/        diagnosticName = null;
/*  92*/        factory = transformerfactoryimpl;
/*  93*/        namePool = NamePool.getDefaultNamePool();
/*  94*/        standardURIResolver = new StandardURIResolver(transformerfactoryimpl);
/*  95*/        userURIResolver = transformerfactoryimpl.getURIResolver();
/*  97*/        errorListener = transformerfactoryimpl.getErrorListener();
/*  98*/        if(errorListener instanceof StandardErrorListener)
/*  99*/            ((StandardErrorListener)errorListener).setRecoveryPolicy(((Integer)transformerfactoryimpl.getAttribute("http://icl.com/saxon/feature/recoveryPolicy")).intValue());
/* 103*/        sourceDocumentPool = new DocumentPool();
/* 104*/        userDataTable = new Hashtable();
/* 106*/        TraceListener tracelistener = (TraceListener)transformerfactoryimpl.getAttribute("http://icl.com/saxon/feature/traceListener");
/* 107*/        if(tracelistener != null)
/* 108*/            addTraceListener(tracelistener);
/* 111*/        Boolean boolean1 = (Boolean)transformerfactoryimpl.getAttribute("http://icl.com/saxon/feature/linenumbering");
/* 112*/        if(boolean1 != null && boolean1.booleanValue())
/* 113*/            setLineNumbering(true);
/* 116*/        Integer integer = (Integer)transformerfactoryimpl.getAttribute("http://icl.com/saxon/feature/treeModel");
/* 117*/        if(integer != null)
/* 118*/            setTreeModel(integer.intValue());
            }

            public void reset()
            {
/* 147*/        clearParameters();
/* 148*/        namePool = NamePool.getDefaultNamePool();
/* 149*/        standardURIResolver = new StandardURIResolver(factory);
/* 150*/        userURIResolver = factory.getURIResolver();
/* 151*/        currentOutputter = null;
/* 152*/        messageEmitter = null;
/* 153*/        outputProperties = null;
/* 155*/        errorListener = factory.getErrorListener();
/* 156*/        if(errorListener instanceof StandardErrorListener)
/* 157*/            ((StandardErrorListener)errorListener).setRecoveryPolicy(((Integer)factory.getAttribute("http://icl.com/saxon/feature/recoveryPolicy")).intValue());
/* 161*/        userDataTable = new Hashtable();
/* 163*/        TraceListener tracelistener = (TraceListener)factory.getAttribute("http://icl.com/saxon/feature/traceListener");
/* 164*/        if(tracelistener != null)
/* 165*/            addTraceListener(tracelistener);
/* 168*/        Boolean boolean1 = (Boolean)factory.getAttribute("http://icl.com/saxon/feature/linenumbering");
/* 169*/        if(boolean1 != null && boolean1.booleanValue())
/* 170*/            setLineNumbering(true);
/* 173*/        Integer integer = (Integer)factory.getAttribute("http://icl.com/saxon/feature/treeModel");
/* 174*/        if(integer != null)
/* 175*/            setTreeModel(integer.intValue());
            }

            public TransformerFactoryImpl getTransformerFactory()
            {
/* 181*/        return factory;
            }

            public void setDiagnosticName(String s)
            {
/* 189*/        diagnosticName = s;
            }

            public String toString()
            {
/* 193*/        if(diagnosticName == null)
/* 194*/            return super.toString();
/* 196*/        else
/* 196*/            return diagnosticName;
            }

            public void run(NodeInfo nodeinfo)
                throws TransformerException
            {
/* 219*/        Context context = makeContext(nodeinfo);
/* 220*/        applyTemplates(context, new SingletonNodeSet(nodeinfo), getRuleManager().getMode(-1), null);
            }

            public void applyTemplates(Context context, Expression expression, Mode mode, ParameterSet parameterset)
                throws TransformerException
            {
                Object obj;
/* 245*/        if(expression == null)
/* 246*/            obj = context.getCurrentNodeInfo().getEnumeration((byte)3, AnyNodeTest.getInstance());
/* 248*/        else
/* 248*/            obj = expression.enumerate(context, false);
/* 253*/        if(!(obj instanceof LastPositionFinder))
/* 254*/            obj = new LookaheadEnumerator(((NodeEnumeration) (obj)));
/* 257*/        int i = 1;
/* 258*/        Context context1 = context.newContext();
/* 259*/        context1.setLastPositionFinder((LastPositionFinder)obj);
/* 260*/        context1.setMode(mode);
/* 262*/        while(((NodeEnumeration) (obj)).hasMoreElements()) 
                {
/* 262*/            NodeInfo nodeinfo = ((NodeEnumeration) (obj)).nextElement();
/* 268*/            context1.setCurrentNode(nodeinfo);
/* 269*/            context1.setContextNode(nodeinfo);
/* 270*/            context1.setPosition(i++);
/* 274*/            NodeHandler nodehandler = ruleManager.getHandler(nodeinfo, mode, context1);
/* 276*/            if(nodehandler == null)
/* 278*/                defaultAction(nodeinfo, context1);
/* 281*/            else
/* 281*/            if(nodehandler.needsStackFrame())
                    {
/* 282*/                bindery.openStackFrame(parameterset);
/* 283*/                if(isTracing())
                        {
/* 284*/                    traceListener.enterSource(nodehandler, context1);
/* 285*/                    nodehandler.start(nodeinfo, context1);
/* 286*/                    traceListener.leaveSource(nodehandler, context1);
                        } else
                        {
/* 288*/                    nodehandler.start(nodeinfo, context1);
                        }
/* 290*/                bindery.closeStackFrame();
                    } else
/* 292*/            if(isTracing())
                    {
/* 293*/                traceListener.enterSource(nodehandler, context1);
/* 294*/                nodehandler.start(nodeinfo, context1);
/* 295*/                traceListener.leaveSource(nodehandler, context1);
                    } else
                    {
/* 297*/                nodehandler.start(nodeinfo, context1);
                    }
                }
            }

            private void defaultAction(NodeInfo nodeinfo, Context context)
                throws TransformerException
            {
/* 310*/        switch(nodeinfo.getNodeType())
                {
/* 313*/        case 1: // '\001'
/* 313*/        case 9: // '\t'
/* 313*/            applyTemplates(context, null, context.getMode(), null);
/* 314*/            return;

/* 317*/        case 2: // '\002'
/* 317*/        case 3: // '\003'
/* 317*/            nodeinfo.copyStringValue(getOutputter());
/* 318*/            return;

/* 323*/        case 7: // '\007'
/* 323*/        case 8: // '\b'
/* 323*/        case 13: // '\r'
/* 323*/            return;

/* 325*/        case 4: // '\004'
/* 325*/        case 5: // '\005'
/* 325*/        case 6: // '\006'
/* 325*/        case 10: // '\n'
/* 325*/        case 11: // '\013'
/* 325*/        case 12: // '\f'
/* 325*/        default:
/* 325*/            return;
                }
            }

            public void applyImports(Context context, Mode mode, int i, int j, ParameterSet parameterset)
                throws TransformerException
            {
/* 333*/        NodeInfo nodeinfo = context.getCurrentNodeInfo();
/* 334*/        NodeHandler nodehandler = ruleManager.getHandler(nodeinfo, mode, i, j, context);
/* 336*/        if(nodehandler == null)
                {
/* 337*/            defaultAction(nodeinfo, context);
                } else
                {
/* 339*/            bindery.openStackFrame(parameterset);
/* 340*/            nodehandler.start(nodeinfo, context);
/* 341*/            bindery.closeStackFrame();
                }
            }

            public int compare(NodeInfo nodeinfo, NodeInfo nodeinfo1)
            {
/* 355*/        if(sourceDocumentPool.getNumberOfDocuments() == 1)
/* 356*/            return nodeinfo.compareOrder(nodeinfo1);
/* 358*/        int i = sourceDocumentPool.getDocumentNumber(nodeinfo.getDocumentRoot());
/* 359*/        int j = sourceDocumentPool.getDocumentNumber(nodeinfo1.getDocumentRoot());
/* 360*/        if(i == j)
/* 361*/            return nodeinfo.compareOrder(nodeinfo1);
/* 363*/        else
/* 363*/            return i - j;
            }

            public void setOutputProperties(Properties properties)
            {
                String s;
/* 379*/        for(Enumeration enumeration = properties.keys(); enumeration.hasMoreElements(); setOutputProperty(s, (String)properties.get(s)))
/* 381*/            s = (String)enumeration.nextElement();

            }

            public Properties getOutputProperties()
            {
/* 391*/        if(outputProperties == null)
                {
/* 392*/            if(preparedStyleSheet == null)
/* 393*/                return new Properties();
/* 395*/            outputProperties = preparedStyleSheet.getOutputProperties();
                }
/* 401*/        Properties properties = new Properties();
                String s;
/* 402*/        for(Enumeration enumeration = outputProperties.keys(); enumeration.hasMoreElements(); properties.put(s, (String)outputProperties.get(s)))
/* 404*/            s = (String)enumeration.nextElement();

/* 407*/        return properties;
            }

            public void setOutputProperty(String s, String s1)
            {
/* 415*/        if(outputProperties == null)
/* 416*/            outputProperties = getOutputProperties();
/* 418*/        if(!SaxonOutputKeys.isValidOutputKey(s))
                {
/* 419*/            throw new IllegalArgumentException(s);
                } else
                {
/* 421*/            outputProperties.put(s, s1);
/* 422*/            return;
                }
            }

            public String getOutputProperty(String s)
            {
/* 429*/        if(outputProperties == null)
/* 430*/            outputProperties = getOutputProperties();
/* 432*/        return outputProperties.getProperty(s);
            }

            public void changeOutputDestination(Properties properties, Result result)
                throws TransformerException
            {
/* 446*/        GeneralOutputter generaloutputter = new GeneralOutputter(namePool);
/* 447*/        generaloutputter.setOutputDestination(properties, result);
/* 448*/        currentOutputter = generaloutputter;
            }

            public void changeToTextOutputDestination(StringBuffer stringbuffer)
            {
/* 457*/        StringOutputter stringoutputter = new StringOutputter(stringbuffer);
/* 458*/        stringoutputter.setErrorListener(errorListener);
/* 459*/        currentOutputter = stringoutputter;
            }

            public Outputter getOutputter()
            {
/* 467*/        return currentOutputter;
            }

            public void resetOutputDestination(Outputter outputter)
                throws TransformerException
            {
/* 477*/        if(currentOutputter == null)
                {
/* 478*/            throw new TransformerException("No outputter has been allocated");
                } else
                {
/* 480*/            currentOutputter.close();
/* 481*/            currentOutputter = outputter;
/* 482*/            return;
                }
            }

            public Emitter makeMessageEmitter()
                throws TransformerException
            {
/* 492*/        String s = (String)factory.getAttribute("http://icl.com/saxon/feature/messageEmitterClass");
/* 494*/        Object obj = Loader.getInstance(s);
/* 495*/        if(!(obj instanceof Emitter))
                {
/* 496*/            throw new TransformerException(s + " is not an Emitter");
                } else
                {
/* 498*/            messageEmitter = (Emitter)obj;
/* 499*/            return messageEmitter;
                }
            }

            public void setMessageEmitter(Emitter emitter)
            {
/* 507*/        messageEmitter = emitter;
            }

            public Emitter getMessageEmitter()
            {
/* 515*/        return messageEmitter;
            }

            public void setRecoveryPolicy(int i)
            {
/* 523*/        recoveryPolicy = i;
/* 524*/        if(errorListener instanceof StandardErrorListener)
/* 525*/            ((StandardErrorListener)errorListener).setRecoveryPolicy(i);
            }

            public int getRecoveryPolicy()
            {
/* 534*/        return recoveryPolicy;
            }

            public void setErrorListener(ErrorListener errorlistener)
            {
/* 542*/        errorListener = errorlistener;
            }

            public ErrorListener getErrorListener()
            {
/* 550*/        return errorListener;
            }

            public void reportRecoverableError(String s, SourceLocator sourcelocator)
                throws TransformerException
            {
/* 560*/        if(sourcelocator == null)
                {
/* 561*/            errorListener.warning(new TransformerException(s));
                } else
                {
/* 563*/            TransformerException transformerexception = new TransformerException(s, sourcelocator);
/* 564*/            errorListener.warning(transformerexception);
                }
            }

            public void reportRecoverableError(TransformerException transformerexception)
                throws TransformerException
            {
/* 575*/        errorListener.warning(transformerexception);
            }

            public DocumentPool getDocumentPool()
            {
/* 588*/        return sourceDocumentPool;
            }

            public void clearDocumentPool()
            {
/* 598*/        sourceDocumentPool = new DocumentPool();
            }

            public void setLineNumbering(boolean flag)
            {
/* 606*/        lineNumbering = flag;
            }

            public boolean isLineNumbering()
            {
/* 614*/        return lineNumbering;
            }

            public Context makeContext(NodeInfo nodeinfo)
            {
/* 623*/        Context context = new Context(this);
/* 624*/        context.setCurrentNode(nodeinfo);
/* 625*/        context.setContextNode(nodeinfo);
/* 626*/        context.setPosition(1);
/* 627*/        context.setLast(1);
/* 628*/        return context;
            }

            public Bindery getBindery()
            {
/* 636*/        return bindery;
            }

            public URIResolver getURIResolver()
            {
/* 646*/        return userURIResolver != null ? userURIResolver : standardURIResolver;
            }

            public URIResolver getStandardURIResolver()
            {
/* 655*/        return standardURIResolver;
            }

            public KeyManager getKeyManager()
            {
/* 664*/        return styleSheetElement.getKeyManager();
            }

            public void setNamePool(NamePool namepool)
            {
/* 672*/        namePool = namepool;
            }

            public NamePool getNamePool()
            {
/* 680*/        return namePool;
            }

            public void setTreeModel(int i)
            {
/* 688*/        treeModel = i;
            }

            public int getTreeModel()
            {
/* 696*/        return treeModel;
            }

            public void disableWhitespaceStripping(boolean flag)
            {
/* 704*/        disableStripping = flag;
            }

            public boolean isWhitespaceStrippingDisabled()
            {
/* 712*/        return disableStripping;
            }

            public Builder makeBuilder()
            {
                Object obj;
/* 721*/        if(treeModel == 1)
/* 722*/            obj = new TinyBuilder();
/* 724*/        else
/* 724*/            obj = new TreeBuilder();
/* 726*/        Boolean boolean1 = (Boolean)factory.getAttribute("http://icl.com/saxon/feature/timing");
/* 727*/        ((Builder) (obj)).setTiming(boolean1 != null ? boolean1.booleanValue() : false);
/* 728*/        ((Emitter) (obj)).setNamePool(namePool);
/* 729*/        ((Builder) (obj)).setLineNumbering(lineNumbering);
/* 730*/        ((Builder) (obj)).setErrorListener(errorListener);
/* 732*/        Stripper stripper = makeStripper(((Builder) (obj)));
/* 733*/        return ((Builder) (obj));
            }

            public Stripper makeStripper(Builder builder)
            {
                Stripper stripper;
/* 738*/        if(styleSheetElement == null)
/* 739*/            stripper = new Stripper(new Mode());
/* 741*/        else
/* 741*/            stripper = styleSheetElement.newStripper();
/* 743*/        stripper.setController(this);
/* 744*/        stripper.setUnderlyingEmitter(builder);
/* 745*/        builder.setStripper(stripper);
/* 746*/        return stripper;
            }

            public void setDecimalFormatManager(DecimalFormatManager decimalformatmanager)
            {
/* 755*/        decimalFormatManager = decimalformatmanager;
            }

            public DecimalFormatManager getDecimalFormatManager()
            {
/* 759*/        return decimalFormatManager;
            }

            public void setRuleManager(RuleManager rulemanager)
            {
/* 767*/        ruleManager = rulemanager;
            }

            public RuleManager getRuleManager()
            {
/* 771*/        return ruleManager;
            }

            public void setTraceListener(TraceListener tracelistener)
            {
/* 779*/        traceListener = tracelistener;
            }

            public TraceListener getTraceListener()
            {
/* 783*/        return traceListener;
            }

            public final boolean isTracing()
            {
/* 787*/        return traceListener != null && !tracingIsSuspended;
            }

            public void pauseTracing(boolean flag)
            {
/* 791*/        tracingIsSuspended = flag;
            }

            public void setPreparedStyleSheet(PreparedStyleSheet preparedstylesheet)
            {
/* 799*/        preparedStyleSheet = preparedstylesheet;
/* 800*/        styleSheetElement = (XSLStyleSheet)preparedstylesheet.getStyleSheetDocument().getDocumentElement();
/* 801*/        preview = styleSheetElement.getPreviewManager() != null;
            }

            protected boolean usesPreviewMode()
            {
/* 811*/        return preview;
            }

            private void initializeController()
            {
/* 819*/        setRuleManager(styleSheetElement.getRuleManager());
/* 820*/        setDecimalFormatManager(styleSheetElement.getDecimalFormatManager());
/* 822*/        if(traceListener != null)
/* 823*/            traceListener.open();
/* 828*/        bindery = new Bindery();
/* 829*/        styleSheetElement.initialiseBindery(bindery);
/* 833*/        bindery.defineGlobalParameters(parameters);
            }

            public void addTraceListener(TraceListener tracelistener)
            {
/* 844*/        traceListener = SaxonEventMulticaster.add(traceListener, tracelistener);
            }

            public void removeTraceListener(TraceListener tracelistener)
            {
/* 854*/        traceListener = SaxonEventMulticaster.remove(traceListener, tracelistener);
            }

            public Object getUserData(NodeInfo nodeinfo, String s)
            {
/* 870*/        String s1 = s + ' ' + getDocumentPool().getDocumentNumber(nodeinfo.getDocumentRoot()) + nodeinfo.generateId();
/* 872*/        return userDataTable.get(s1);
            }

            public void setUserData(NodeInfo nodeinfo, String s, Object obj)
            {
/* 884*/        String s1 = s + ' ' + getDocumentPool().getDocumentNumber(nodeinfo.getDocumentRoot()) + nodeinfo.generateId();
/* 886*/        if(obj == null)
/* 887*/            userDataTable.remove(s1);
/* 889*/        else
/* 889*/            userDataTable.put(s1, obj);
            }

            public void transform(Source source, Result result)
                throws TransformerException
            {
/* 908*/        if(preparedStyleSheet == null)
/* 909*/            throw new TransformerException("Stylesheet has not been prepared");
/* 912*/        PreviewManager previewmanager = styleSheetElement.getPreviewManager();
/* 913*/        preview = previewmanager != null;
/* 915*/        String s = "/";
/* 918*/        try
                {
/* 918*/            if(source instanceof NodeInfo)
/* 920*/                if(preview)
                        {
/* 921*/                    throw new TransformerException("Preview mode requires serial input");
                        } else
                        {
/* 923*/                    transformDocument((NodeInfo)source, result);
/* 924*/                    return;
                        }
/* 926*/            if(source instanceof DOMSource)
                    {
/* 927*/                DOMSource domsource = (DOMSource)source;
/* 929*/                if(preview)
/* 930*/                    throw new TransformerException("Preview mode requires serial input");
/* 932*/                if((disableStripping || !styleSheetElement.stripsWhitespace()) && (domsource.getNode() instanceof NodeInfo))
                        {
/* 938*/                    transformDocument((NodeInfo)domsource.getNode(), result);
/* 939*/                    return;
                        }
/* 942*/                s = getPathToNode(domsource.getNode());
                    }
/* 946*/            SAXSource saxsource = factory.getSAXSource(source, false);
/* 950*/            if(preview)
                    {
/* 952*/                initializeController();
/* 959*/                if(outputProperties == null)
/* 960*/                    outputProperties = new Properties();
/* 962*/                changeOutputDestination(outputProperties, result);
/* 964*/                Builder builder = makeBuilder();
/* 965*/                builder.setController(this);
/* 966*/                builder.setPreviewManager(previewmanager);
/* 967*/                builder.setNamePool(namePool);
/* 968*/                DocumentInfo documentinfo = builder.build(saxsource);
/* 969*/                sourceDocumentPool.add(documentinfo, null);
/* 970*/                builder = null;
/* 972*/                transformDocument(documentinfo, result);
/* 973*/                resetOutputDestination(null);
                    } else
                    {
/* 976*/                Builder builder1 = makeBuilder();
/* 977*/                DocumentInfo documentinfo1 = builder1.build(saxsource);
/* 979*/                sourceDocumentPool.add(documentinfo1, null);
/* 980*/                builder1 = null;
/* 982*/                Object obj = documentinfo1;
/* 983*/                if(!s.equals("/"))
                        {
/* 984*/                    Expression expression = Expression.make(s, new StandaloneContext(namePool));
/* 985*/                    Context context = makeContext(documentinfo1);
/* 986*/                    NodeEnumeration nodeenumeration = expression.enumerate(context, false);
/* 987*/                    if(nodeenumeration.hasMoreElements())
/* 988*/                        obj = nodeenumeration.nextElement();
/* 990*/                    else
/* 990*/                        throw new TransformerException("Problem finding the start node after converting DOM to Saxon tree");
                        }
/* 994*/                transformDocument(((NodeInfo) (obj)), result);
                    }
                }
/* 998*/        catch(TerminationException terminationexception)
                {
/* 998*/            throw terminationexception;
                }
/*1000*/        catch(TransformerException transformerexception)
                {
/*1000*/            Throwable throwable = transformerexception.getException();
/*1001*/            if(throwable == null || !(throwable instanceof SAXParseException))
/*1004*/                errorListener.fatalError(transformerexception);
/*1006*/            throw transformerexception;
                }
            }

            private String getPathToNode(Node node)
                throws TransformerException
            {
/*1015*/        short word0 = node.getNodeType();
                String s;
/*1017*/        if(word0 == 9)
/*1018*/            s = "/";
/*1019*/        else
/*1019*/        if(word0 == 1)
                {
/*1020*/            s = "";
/*1021*/            Node node1 = node;
/*1023*/            while(word0 == 1) 
                    {
/*1023*/                int i = 1;
/*1024*/                for(Node node2 = node1.getPreviousSibling(); node2 != null; node2 = node2.getPreviousSibling())
                        {
/*1026*/                    short word1 = node2.getNodeType();
/*1027*/                    if(word1 == 1)
/*1028*/                        i++;
/*1029*/                    else
/*1029*/                    if(word1 == 4 || word1 == 5)
/*1031*/                        throw new TransformerException("Document contains CDATA or Entity nodes: can only transform starting at root");
                        }

/*1036*/                if(!s.equals(""))
/*1037*/                    s = '/' + s;
/*1039*/                s = "*[" + i + ']' + s;
/*1041*/                node1 = node1.getParentNode();
/*1042*/                if(node1 == null)
/*1043*/                    throw new TransformerException("Supplied element is not within a Document");
/*1045*/                word0 = node1.getNodeType();
/*1046*/                if(word0 == 9)
/*1047*/                    s = '/' + s;
/*1048*/                else
/*1048*/                if(word0 == 4 || word0 == 5)
/*1050*/                    throw new TransformerException("Document contains CDATA or Entity nodes: can only transform starting at root");
                    }
                } else
                {
/*1055*/            throw new TransformerException("Start node must be either the root or an element");
                }
/*1057*/        return s;
            }

            public void transformDocument(NodeInfo nodeinfo, Result result)
                throws TransformerException
            {
                DocumentInfo documentinfo;
/*1073*/        if(nodeinfo instanceof DocumentInfo)
/*1074*/            documentinfo = (DocumentInfo)nodeinfo;
/*1076*/        else
/*1076*/            documentinfo = nodeinfo.getDocumentRoot();
/*1079*/        if(styleSheetElement == null)
/*1080*/            throw new TransformerException("Stylesheet has not been prepared");
/*1083*/        if(documentinfo.getNamePool() == null)
/*1085*/            documentinfo.setNamePool(preparedStyleSheet.getNamePool());
/*1088*/        if(documentinfo.getNamePool() != preparedStyleSheet.getNamePool())
/*1089*/            throw new TransformerException("Source document and stylesheet must use the same name pool");
/*1092*/        Context context = makeContext(documentinfo);
/*1094*/        if(!preview)
                {
/*1095*/            initializeController();
/*1096*/            Properties properties = new Properties();
/*1097*/            styleSheetElement.updateOutputProperties(properties, context);
/*1099*/            if(outputProperties != null)
                    {
                        String s1;
                        String s3;
/*1100*/                for(Enumeration enumeration = outputProperties.propertyNames(); enumeration.hasMoreElements(); properties.put(s1, s3))
                        {
/*1102*/                    s1 = (String)enumeration.nextElement();
/*1103*/                    s3 = outputProperties.getProperty(s1);
                        }

                    }
/*1109*/            String s = properties.getProperty("{http://icl.com/saxon}next-in-chain");
/*1110*/            if(s != null)
                    {
/*1111*/                String s2 = properties.getProperty("{http://icl.com/saxon}next-in-chain-base-uri");
/*1112*/                result = prepareNextStylesheet(s, s2, result);
                    }
/*1115*/            changeOutputDestination(properties, result);
                }
/*1121*/        styleSheetElement.process(context);
/*1125*/        run(nodeinfo);
/*1127*/        if(traceListener != null)
/*1128*/            traceListener.close();
/*1131*/        if(!preview)
/*1132*/            resetOutputDestination(null);
            }

            private Result prepareNextStylesheet(String s, String s1, Result result)
                throws TransformerException
            {
/*1149*/        Source source = getURIResolver().resolve(s, s1);
/*1150*/        SAXSource saxsource = factory.getSAXSource(source, true);
/*1152*/        javax.xml.transform.Templates templates = factory.newTemplates(source);
/*1153*/        TransformerHandler transformerhandler = factory.newTransformerHandler(templates);
/*1155*/        ContentHandlerProxy contenthandlerproxy = new ContentHandlerProxy();
/*1156*/        contenthandlerproxy.setUnderlyingContentHandler(transformerhandler);
/*1157*/        contenthandlerproxy.setSystemId(saxsource.getSystemId());
/*1158*/        contenthandlerproxy.setRequireWellFormed(false);
/*1159*/        transformerhandler.setResult(result);
/*1161*/        return contenthandlerproxy;
            }

            public void setParameter(String s, Object obj)
            {
/*1177*/        if(parameters == null)
/*1178*/            parameters = new ParameterSet();
                Object obj1;
/*1183*/        try
                {
/*1183*/            obj1 = FunctionProxy.convertJavaObjectToXPath(obj, this);
                }
/*1185*/        catch(TransformerException transformerexception)
                {
/*1185*/            obj1 = new StringValue(obj.toString());
                }
/*1187*/        int i = getFingerprintForExpandedName(s);
/*1188*/        parameters.put(i, ((com.icl.saxon.expr.Value) (obj1)));
            }

            public void setParams(ParameterSet parameterset)
            {
/*1199*/        parameters = parameterset;
            }

            private int getFingerprintForExpandedName(String s)
            {
                String s1;
                String s2;
/*1210*/        if(s.charAt(0) == '{')
                {
/*1211*/            int i = s.indexOf('}');
/*1212*/            if(i < 0)
/*1213*/                throw new IllegalArgumentException("No closing '}' in parameter name");
/*1215*/            s2 = s.substring(1, i);
/*1216*/            if(i == s.length())
/*1217*/                throw new IllegalArgumentException("Missing local part in parameter name");
/*1219*/            s1 = s.substring(i + 1);
                } else
                {
/*1221*/            s2 = "";
/*1222*/            s1 = s;
                }
/*1225*/        return namePool.allocate("", s2, s1);
            }

            public void clearParameters()
            {
/*1233*/        parameters = null;
            }

            public Object getParameter(String s)
            {
/*1241*/        if(parameters == null)
                {
/*1241*/            return null;
                } else
                {
/*1242*/            int i = getFingerprintForExpandedName(s);
/*1243*/            return parameters.get(i);
                }
            }

            public void setURIResolver(URIResolver uriresolver)
            {
/*1254*/        userURIResolver = uriresolver;
            }
}
