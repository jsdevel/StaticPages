// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StyleElement.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.*;
import com.icl.saxon.pattern.NoNodeTest;
import com.icl.saxon.pattern.Pattern;
import com.icl.saxon.trace.TraceListener;
import com.icl.saxon.tree.*;
import java.util.*;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;
import org.xml.sax.Locator;

// Referenced classes of package com.icl.saxon.style:
//            XSLStyleSheet, StyleNodeFactory, ExpressionContext, XSLFallback, 
//            XSLSort, XSLApplyTemplates, XSLWithParam, XSLAttributeSet, 
//            StyleException, TerminationException, SAXONFunction, StandardNames

public abstract class StyleElement extends ElementWithAttributes
    implements Locator
{

            protected Vector attributeSets;
            protected short extensionNamespaces[];
            private short excludedNamespaces[];
            protected String version;
            protected StaticContext staticContext;
            protected TransformerConfigurationException validationError;
            protected int reportingCircumstances;
            public static final int REPORT_ALWAYS = 1;
            public static final int REPORT_UNLESS_FORWARDS_COMPATIBLE = 2;
            public static final int REPORT_IF_INSTANTIATED = 3;

            public StyleElement()
            {
/*  37*/        attributeSets = null;
/*  38*/        extensionNamespaces = null;
/*  39*/        excludedNamespaces = null;
/*  40*/        version = null;
/*  41*/        staticContext = null;
/*  42*/        validationError = null;
/*  43*/        reportingCircumstances = 1;
            }

            public void substituteFor(StyleElement styleelement)
            {
/*  65*/        super.parent = ((NodeImpl) (styleelement)).parent;
/*  66*/        super.attributeList = ((ElementWithAttributes) (styleelement)).attributeList;
/*  67*/        super.namespaceList = ((ElementWithAttributes) (styleelement)).namespaceList;
/*  68*/        super.nameCode = ((ElementImpl) (styleelement)).nameCode;
/*  69*/        sequence = styleelement.sequence;
/*  70*/        attributeSets = styleelement.attributeSets;
/*  71*/        extensionNamespaces = styleelement.extensionNamespaces;
/*  72*/        excludedNamespaces = styleelement.excludedNamespaces;
/*  73*/        version = styleelement.version;
/*  74*/        super.root = ((ElementImpl) (styleelement)).root;
/*  75*/        staticContext = styleelement.staticContext;
/*  76*/        validationError = styleelement.validationError;
/*  77*/        reportingCircumstances = styleelement.reportingCircumstances;
            }

            protected void setValidationError(TransformerException transformerexception, int i)
            {
/*  86*/        if(transformerexception instanceof TransformerConfigurationException)
/*  87*/            validationError = (TransformerConfigurationException)transformerexception;
/*  89*/        else
/*  89*/            validationError = new TransformerConfigurationException(transformerexception);
/*  91*/        reportingCircumstances = i;
            }

            public boolean isInstruction()
            {
/*  99*/        return false;
            }

            public boolean doesPostProcessing()
            {
/* 110*/        return true;
            }

            public boolean mayContainTemplateBody()
            {
/* 118*/        return false;
            }

            public XSLStyleSheet getContainingStyleSheet()
            {
                Object obj;
/* 126*/        for(obj = this; !(obj instanceof XSLStyleSheet); obj = (NodeImpl)((NodeImpl) (obj)).getParent());
/* 130*/        return (XSLStyleSheet)obj;
            }

            public int getPrecedence()
            {
/* 138*/        return getContainingStyleSheet().getPrecedence();
            }

            public final StandardNames getStandardNames()
            {
/* 146*/        DocumentImpl documentimpl = (DocumentImpl)getDocumentRoot();
/* 147*/        return ((StyleNodeFactory)documentimpl.getNodeFactory()).getStandardNames();
            }

            public void processAllAttributes()
                throws TransformerConfigurationException
            {
/* 155*/        staticContext = new ExpressionContext(this);
/* 156*/        processAttributes();
/* 157*/        for(NodeImpl nodeimpl = (NodeImpl)getFirstChild(); nodeimpl != null; nodeimpl = (NodeImpl)nodeimpl.getNextSibling())
/* 159*/            if(nodeimpl instanceof XSLStyleSheet)
/* 160*/                ((XSLStyleSheet)nodeimpl).compileError(nodeimpl.getDisplayName() + " cannot appear as a child of another element");
/* 162*/            else
/* 162*/            if(nodeimpl instanceof StyleElement)
/* 163*/                ((StyleElement)nodeimpl).processAllAttributes();

            }

            public final void processAttributes()
                throws TransformerConfigurationException
            {
/* 176*/        try
                {
/* 176*/            prepareAttributes();
                }
/* 178*/        catch(TransformerConfigurationException transformerconfigurationexception)
                {
/* 178*/            if(forwardsCompatibleModeIsEnabled())
/* 180*/                setValidationError(transformerconfigurationexception, 2);
/* 182*/            else
/* 182*/                compileError(transformerconfigurationexception);
                }
            }

            protected void checkUnknownAttribute(int i)
                throws TransformerConfigurationException
            {
/* 193*/        if(forwardsCompatibleModeIsEnabled())
/* 195*/            return;
/* 197*/        String s = getNamePool().getURI(i);
/* 198*/        String s1 = getURI();
/* 199*/        int j = i & 0xfffff;
/* 200*/        StandardNames standardnames = getStandardNames();
/* 204*/        if(isInstruction() && s.equals("http://www.w3.org/1999/XSL/Transform") && !s1.equals("http://www.w3.org/1999/XSL/Transform") && (j == standardnames.XSL_EXTENSION_ELEMENT_PREFIXES || j == standardnames.XSL_EXCLUDE_RESULT_PREFIXES || j == standardnames.XSL_VERSION))
/* 210*/            return;
/* 213*/        if(s.equals("") || s.equals("http://www.w3.org/1999/XSL/Transform"))
/* 214*/            compileError("Attribute " + getNamePool().getDisplayName(i) + " is not allowed on this element");
            }

            public abstract void prepareAttributes()
                throws TransformerConfigurationException;

            public Expression makeExpression(String s)
                throws TransformerConfigurationException
            {
/* 235*/        try
                {
/* 235*/            return Expression.make(s, staticContext);
                }
/* 237*/        catch(XPathException xpathexception)
                {
/* 237*/            compileError(xpathexception);
/* 238*/            return new ErrorExpression(xpathexception);
                }
            }

            public Pattern makePattern(String s)
                throws TransformerConfigurationException
            {
/* 249*/        try
                {
/* 249*/            return Pattern.make(s, staticContext);
                }
/* 251*/        catch(XPathException xpathexception)
                {
/* 251*/            if(forwardsCompatibleModeIsEnabled())
                    {
/* 252*/                return NoNodeTest.getInstance();
                    } else
                    {
/* 254*/                compileError(xpathexception);
/* 255*/                return NoNodeTest.getInstance();
                    }
                }
            }

            public Expression makeAttributeValueTemplate(String s)
                throws TransformerConfigurationException
            {
/* 267*/        try
                {
/* 267*/            return AttributeValueTemplate.make(s, staticContext);
                }
/* 269*/        catch(XPathException xpathexception)
                {
/* 269*/            compileError(xpathexception);
                }
/* 270*/        return new StringValue(s);
            }

            protected void processExtensionElementAttribute(int i)
                throws TransformerConfigurationException
            {
/* 281*/        String s = getAttributeValue(i & 0xfffff);
/* 282*/        if(s != null)
                {
/* 284*/            int j = 0;
/* 285*/            for(StringTokenizer stringtokenizer = new StringTokenizer(s); stringtokenizer.hasMoreTokens();)
                    {
/* 287*/                stringtokenizer.nextToken();
/* 288*/                j++;
                    }

/* 290*/            extensionNamespaces = new short[j];
/* 291*/            j = 0;
/* 292*/            for(StringTokenizer stringtokenizer1 = new StringTokenizer(s); stringtokenizer1.hasMoreTokens();)
                    {
/* 294*/                String s1 = stringtokenizer1.nextToken();
/* 295*/                if(s1.equals("#default"))
/* 296*/                    s1 = "";
/* 299*/                try
                        {
/* 299*/                    short word0 = getURICodeForPrefix(s1);
/* 300*/                    extensionNamespaces[j++] = word0;
                        }
/* 302*/                catch(NamespaceException namespaceexception)
                        {
/* 302*/                    extensionNamespaces = null;
/* 303*/                    compileError(namespaceexception.getMessage());
                        }
                    }

                }
            }

            protected void processExcludedNamespaces(int i)
                throws TransformerConfigurationException
            {
/* 316*/        String s = getAttributeValue(i & 0xfffff);
/* 317*/        if(s != null)
                {
/* 319*/            int j = 0;
/* 320*/            for(StringTokenizer stringtokenizer = new StringTokenizer(s); stringtokenizer.hasMoreTokens();)
                    {
/* 322*/                stringtokenizer.nextToken();
/* 323*/                j++;
                    }

/* 325*/            excludedNamespaces = new short[j];
/* 326*/            j = 0;
/* 327*/            for(StringTokenizer stringtokenizer1 = new StringTokenizer(s); stringtokenizer1.hasMoreTokens();)
                    {
/* 329*/                String s1 = stringtokenizer1.nextToken();
/* 330*/                if(s1.equals("#default"))
/* 331*/                    s1 = "";
/* 334*/                try
                        {
/* 334*/                    short word0 = getURICodeForPrefix(s1);
/* 335*/                    excludedNamespaces[j++] = word0;
                        }
/* 337*/                catch(NamespaceException namespaceexception)
                        {
/* 337*/                    excludedNamespaces = null;
/* 338*/                    compileError(namespaceexception.getMessage());
                        }
                    }

                }
            }

            protected void processVersionAttribute(int i)
            {
/* 350*/        version = getAttributeValue(i & 0xfffff);
            }

            public String getVersion()
            {
/* 358*/        if(version == null)
                {
/* 359*/            NodeInfo nodeinfo = (NodeInfo)getParentNode();
/* 360*/            if(nodeinfo instanceof StyleElement)
/* 361*/                version = ((StyleElement)nodeinfo).getVersion();
/* 363*/            else
/* 363*/                version = "1.0";
                }
/* 366*/        return version;
            }

            public boolean forwardsCompatibleModeIsEnabled()
            {
/* 374*/        return !getVersion().equals("1.0");
            }

            protected boolean definesExtensionElement(short word0)
            {
/* 386*/        if(extensionNamespaces == null)
/* 387*/            return false;
/* 389*/        for(int i = 0; i < extensionNamespaces.length; i++)
/* 390*/            if(extensionNamespaces[i] == word0)
/* 391*/                return true;

/* 394*/        return false;
            }

            public boolean isExtensionNamespace(short word0)
            {
/* 404*/        for(Object obj = this; obj instanceof StyleElement; obj = (NodeImpl)((NodeImpl) (obj)).getParent())
/* 406*/            if(((StyleElement)obj).definesExtensionElement(word0))
/* 407*/                return true;

/* 411*/        return false;
            }

            protected boolean definesExcludedNamespace(short word0)
            {
/* 421*/        if(excludedNamespaces == null)
/* 422*/            return false;
/* 424*/        for(int i = 0; i < excludedNamespaces.length; i++)
/* 425*/            if(excludedNamespaces[i] == word0)
/* 426*/                return true;

/* 429*/        return false;
            }

            public boolean isExcludedNamespace(short word0)
            {
/* 440*/        if(word0 == 2)
/* 440*/            return true;
/* 441*/        if(isExtensionNamespace(word0))
/* 441*/            return true;
/* 442*/        for(Object obj = this; obj instanceof StyleElement; obj = (NodeImpl)((NodeImpl) (obj)).getParent())
/* 444*/            if(((StyleElement)obj).definesExcludedNamespace(word0))
/* 445*/                return true;

/* 449*/        return false;
            }

            public void validate()
                throws TransformerConfigurationException
            {
            }

            public void preprocess()
                throws TransformerConfigurationException
            {
            }

            public void validateSubtree()
                throws TransformerConfigurationException
            {
/* 474*/        if(validationError != null)
/* 475*/            if(reportingCircumstances == 1)
/* 476*/                compileError(validationError);
/* 477*/            else
/* 477*/            if(reportingCircumstances == 2 && !forwardsCompatibleModeIsEnabled())
/* 479*/                compileError(validationError);
/* 483*/        try
                {
/* 483*/            validate();
                }
/* 485*/        catch(TransformerConfigurationException transformerconfigurationexception)
                {
/* 485*/            if(forwardsCompatibleModeIsEnabled())
/* 486*/                setValidationError(transformerconfigurationexception, 3);
/* 488*/            else
/* 488*/                compileError(transformerconfigurationexception);
                }
/* 492*/        validateChildren();
            }

            protected void validateChildren()
                throws TransformerConfigurationException
            {
/* 497*/        for(NodeImpl nodeimpl = (NodeImpl)getFirstChild(); nodeimpl != null; nodeimpl = (NodeImpl)nodeimpl.getNextSibling())
/* 499*/            if(nodeimpl instanceof StyleElement)
/* 500*/                ((StyleElement)nodeimpl).validateSubtree();

            }

            protected XSLStyleSheet getPrincipalStyleSheet()
            {
/* 512*/        XSLStyleSheet xslstylesheet = getContainingStyleSheet();
/* 514*/        do
                {
/* 514*/            XSLStyleSheet xslstylesheet1 = xslstylesheet.getImporter();
/* 515*/            if(xslstylesheet1 == null)
/* 515*/                return xslstylesheet;
/* 516*/            xslstylesheet = xslstylesheet1;
                } while(true);
            }

            public PreparedStyleSheet getPreparedStyleSheet()
            {
/* 526*/        return getPrincipalStyleSheet().getPreparedStyleSheet();
            }

            public void checkWithinTemplate()
                throws TransformerConfigurationException
            {
/* 535*/        StyleElement styleelement = (StyleElement)getParentNode();
/* 536*/        if(!styleelement.mayContainTemplateBody())
/* 537*/            compileError("Element must only be used within a template body");
            }

            public void checkTopLevel()
                throws TransformerConfigurationException
            {
/* 547*/        if(!(getParentNode() instanceof XSLStyleSheet))
/* 548*/            compileError("Element must only be used at top level of stylesheet");
            }

            public void checkNotTopLevel()
                throws TransformerConfigurationException
            {
/* 558*/        if(getParentNode() instanceof XSLStyleSheet)
/* 559*/            compileError("Element must not be used at top level of stylesheet");
            }

            public void checkEmpty()
                throws TransformerConfigurationException
            {
/* 569*/        if(getFirstChild() != null)
/* 570*/            compileError("Element must be empty");
            }

            public void reportAbsence(String s)
                throws TransformerConfigurationException
            {
/* 581*/        compileError("Element must have a \"" + s + "\" attribute");
            }

            public abstract void process(Context context)
                throws TransformerException;

            public void processChildren(Context context)
                throws TransformerException
            {
/* 601*/        if(context.getController().isTracing())
                {
/* 602*/            TraceListener tracelistener = context.getController().getTraceListener();
/* 604*/            for(NodeImpl nodeimpl1 = (NodeImpl)getFirstChild(); nodeimpl1 != null; nodeimpl1 = (NodeImpl)nodeimpl1.getNextSibling())
                    {
/* 607*/                tracelistener.enter(nodeimpl1, context);
/* 609*/                if(nodeimpl1.getNodeType() == 3)
/* 610*/                    nodeimpl1.copy(context.getOutputter());
/* 611*/                else
/* 611*/                if(nodeimpl1 instanceof StyleElement)
                        {
/* 612*/                    StyleElement styleelement1 = (StyleElement)nodeimpl1;
/* 613*/                    if(styleelement1.validationError != null)
/* 614*/                        fallbackProcessing(styleelement1, context);
/* 617*/                    else
/* 617*/                        try
                                {
/* 617*/                            context.setStaticContext(styleelement1.staticContext);
/* 618*/                            styleelement1.process(context);
                                }
/* 620*/                        catch(TransformerException transformerexception1)
                                {
/* 620*/                            throw styleelement1.styleError(transformerexception1);
                                }
                        }
/* 625*/                tracelistener.leave(nodeimpl1, context);
                    }

                } else
                {
/* 631*/            for(NodeImpl nodeimpl = (NodeImpl)getFirstChild(); nodeimpl != null; nodeimpl = (NodeImpl)nodeimpl.getNextSibling())
/* 634*/                if(nodeimpl.getNodeType() == 3)
/* 635*/                    nodeimpl.copy(context.getOutputter());
/* 636*/                else
/* 636*/                if(nodeimpl instanceof StyleElement)
                        {
/* 637*/                    StyleElement styleelement = (StyleElement)nodeimpl;
/* 638*/                    if(styleelement.validationError != null)
/* 639*/                        fallbackProcessing(styleelement, context);
/* 642*/                    else
/* 642*/                        try
                                {
/* 642*/                            context.setStaticContext(styleelement.staticContext);
/* 643*/                            styleelement.process(context);
                                }
/* 645*/                        catch(TransformerException transformerexception)
                                {
/* 645*/                            throw styleelement.styleError(transformerexception);
                                }
                        }

                }
            }

            protected void fallbackProcessing(StyleElement styleelement, Context context)
                throws TransformerException
            {
/* 661*/        XSLFallback xslfallback = null;
                Node node;
/* 662*/        for(node = styleelement.getFirstChild(); node != null; node = node.getNextSibling())
                {
/* 664*/            if(!(node instanceof XSLFallback))
/* 665*/                continue;
/* 665*/            xslfallback = (XSLFallback)node;
/* 666*/            break;
                }

/* 671*/        if(xslfallback == null)
/* 672*/            throw styleelement.styleError(styleelement.validationError);
/* 675*/        boolean flag = context.getController().isTracing();
/* 678*/        for(; node != null; node = node.getNextSibling())
/* 678*/            if(node instanceof XSLFallback)
                    {
/* 679*/                XSLFallback xslfallback1 = (XSLFallback)node;
/* 681*/                if(flag)
                        {
/* 682*/                    TraceListener tracelistener = context.getController().getTraceListener();
/* 683*/                    tracelistener.enter(xslfallback1, context);
/* 684*/                    xslfallback1.process(context);
/* 685*/                    tracelistener.leave(xslfallback1, context);
                        } else
                        {
/* 687*/                    xslfallback1.process(context);
                        }
                    }

            }

            protected Expression handleSortKeys(Expression expression)
                throws TransformerConfigurationException
            {
/* 704*/        int i = 0;
/* 705*/        boolean flag = true;
/* 706*/        for(Node node = getFirstChild(); node != null; node = node.getNextSibling())
/* 709*/            if(node instanceof XSLSort)
                    {
/* 710*/                if(!flag)
/* 711*/                    compileError("An xsl:sort element is not allowed here");
/* 713*/                i++;
                    } else
/* 715*/            if(!(this instanceof XSLApplyTemplates) || !(node instanceof XSLWithParam))
/* 716*/                flag = false;

/* 722*/        if(i > 0)
                {
/* 723*/            SortedSelection sortedselection = new SortedSelection(expression, i);
/* 724*/            Node node1 = getFirstChild();
/* 726*/            int j = 0;
/* 728*/            for(; node1 != null; node1 = node1.getNextSibling())
/* 728*/                if(node1 instanceof XSLSort)
/* 729*/                    sortedselection.setSortKey(((XSLSort)node1).getSortKeyDefinition(), j++);

/* 735*/            return sortedselection;
                } else
                {
/* 738*/            return new NodeListExpression(expression);
                }
            }

            protected void findAttributeSets(String s)
                throws TransformerConfigurationException
            {
/* 751*/        attributeSets = new Vector(5);
/* 753*/        XSLStyleSheet xslstylesheet = getPrincipalStyleSheet();
/* 754*/        Vector vector = xslstylesheet.getTopLevel();
/* 756*/        for(StringTokenizer stringtokenizer = new StringTokenizer(s); stringtokenizer.hasMoreTokens();)
                {
/* 758*/            String s1 = stringtokenizer.nextToken();
                    int i;
/* 761*/            try
                    {
/* 761*/                i = makeNameCode(s1, false) & 0xfffff;
                    }
/* 763*/            catch(NamespaceException namespaceexception)
                    {
/* 763*/                compileError(namespaceexception.getMessage());
/* 764*/                i = -1;
                    }
/* 766*/            boolean flag = false;
/* 771*/            for(int j = 0; j < vector.size(); j++)
/* 772*/                if(vector.elementAt(j) instanceof XSLAttributeSet)
                        {
/* 773*/                    XSLAttributeSet xslattributeset = (XSLAttributeSet)vector.elementAt(j);
/* 774*/                    if(xslattributeset.getAttributeSetFingerprint() == i)
                            {
/* 775*/                        attributeSets.addElement(xslattributeset);
/* 776*/                        flag = true;
                            }
                        }

/* 781*/            if(!flag)
/* 782*/                compileError("No attribute-set exists named " + s1);
                }

            }

            protected void processAttributeSets(Context context)
                throws TransformerException
            {
/* 793*/        if(attributeSets == null)
/* 793*/            return;
/* 794*/        Controller controller = context.getController();
/* 795*/        for(int i = 0; i < attributeSets.size(); i++)
                {
/* 796*/            XSLAttributeSet xslattributeset = (XSLAttributeSet)attributeSets.elementAt(i);
/* 800*/            Object obj = controller.getUserData(xslattributeset, "is-being-expanded");
/* 801*/            if(obj != null)
/* 802*/                throw styleError("Circular reference to attribute set");
/* 804*/            controller.setUserData(xslattributeset, "is-being-expanded", "is-being-expanded");
/* 805*/            xslattributeset.expand(context);
/* 806*/            controller.setUserData(xslattributeset, "is-being-expanded", null);
                }

            }

            protected TransformerException styleError(TransformerException transformerexception)
            {
/* 815*/        if(transformerexception instanceof StyleException)
/* 815*/            return transformerexception;
/* 816*/        if(transformerexception instanceof TerminationException)
/* 816*/            return transformerexception;
/* 817*/        if(transformerexception.getLocator() == null)
/* 818*/            return new TransformerException(transformerexception.getMessage(), this, transformerexception.getException());
/* 822*/        else
/* 822*/            return transformerexception;
            }

            protected TransformerException styleError(String s)
            {
/* 826*/        return new TransformerException(s, this);
            }

            protected void compileError(TransformerException transformerexception)
                throws TransformerConfigurationException
            {
/* 835*/        if(transformerexception.getLocator() == null)
/* 836*/            transformerexception.setLocator(this);
/* 838*/        PreparedStyleSheet preparedstylesheet = getPreparedStyleSheet();
/* 840*/        try
                {
/* 840*/            if(preparedstylesheet == null)
/* 842*/                throw transformerexception;
/* 844*/            preparedstylesheet.reportError(transformerexception);
                }
/* 847*/        catch(TransformerException transformerexception1)
                {
/* 847*/            if(transformerexception1 instanceof TransformerConfigurationException)
/* 848*/                throw (TransformerConfigurationException)transformerexception1;
/* 850*/            if(transformerexception1.getException() instanceof TransformerConfigurationException)
                    {
/* 851*/                throw (TransformerConfigurationException)transformerexception1.getException();
                    } else
                    {
/* 853*/                TransformerConfigurationException transformerconfigurationexception = new TransformerConfigurationException(transformerexception);
/* 854*/                transformerconfigurationexception.setLocator(this);
/* 855*/                throw transformerconfigurationexception;
                    }
                }
            }

            protected void compileError(String s)
                throws TransformerConfigurationException
            {
/* 861*/        TransformerConfigurationException transformerconfigurationexception = new TransformerConfigurationException(s);
/* 863*/        transformerconfigurationexception.setLocator(this);
/* 864*/        compileError(((TransformerException) (transformerconfigurationexception)));
            }

            public boolean isTopLevel()
            {
/* 872*/        return getParentNode() instanceof XSLStyleSheet;
            }

            public Binding bindVariable(int i)
                throws XPathException
            {
/* 883*/        Binding binding = getVariableBinding(i);
/* 884*/        if(binding == null)
/* 885*/            throw new XPathException("Variable " + getNamePool().getDisplayName(i) + " has not been declared");
/* 887*/        else
/* 887*/            return binding;
            }

            public Binding getVariableBinding(int i)
            {
/* 897*/        Object obj = this;
/* 898*/        Object obj1 = this;
                int j;
/* 901*/        if(!isTopLevel())
/* 903*/            do
                    {
/* 903*/                for(obj = (NodeImpl)((NodeImpl) (obj)).getPreviousSibling(); obj == null; obj = (NodeImpl)((NodeImpl) (obj)).getPreviousSibling())
                        {
/* 905*/                    obj = (NodeImpl)((NodeImpl) (obj1)).getParent();
/* 906*/                    obj1 = obj;
/* 907*/                    if(((NodeImpl) (obj)).getParent() instanceof XSLStyleSheet)
/* 907*/                        break;
                        }

/* 910*/                if(((NodeImpl) (obj)).getParent() instanceof XSLStyleSheet)
/* 910*/                    break;
/* 911*/                if(obj instanceof Binding)
                        {
/* 912*/                    j = ((Binding)obj).getVariableFingerprint();
/* 913*/                    if(j == i)
/* 914*/                        return (Binding)obj;
                        }
                    } while(true);
/* 923*/        XSLStyleSheet xslstylesheet = getPrincipalStyleSheet();
/* 924*/        Vector vector = xslstylesheet.getTopLevel();
/* 925*/        for(int k = vector.size() - 1; k >= 0; k--)
                {
/* 926*/            Object obj2 = vector.elementAt(k);
/* 927*/            if((obj2 instanceof Binding) && obj2 != this)
                    {
/* 928*/                int l = ((Binding)obj2).getVariableFingerprint();
/* 929*/                if(l == i)
/* 930*/                    return (Binding)obj2;
                    }
                }

/* 935*/        return null;
            }

            public Enumeration[] getVariableNames()
            {
/* 945*/        Hashtable hashtable = new Hashtable();
/* 946*/        Hashtable hashtable1 = new Hashtable();
/* 948*/        Object obj = this;
/* 949*/        Object obj1 = this;
/* 950*/        NamePool namepool = getNamePool();
/* 954*/        if(!isTopLevel())
/* 956*/            do
                    {
/* 956*/                for(obj = (NodeImpl)((NodeImpl) (obj)).getPreviousSibling(); obj == null; obj = (NodeImpl)((NodeImpl) (obj)).getPreviousSibling())
                        {
/* 958*/                    obj = (NodeImpl)((NodeImpl) (obj1)).getParent();
/* 959*/                    obj1 = obj;
/* 960*/                    if(((NodeImpl) (obj)).getParent() instanceof XSLStyleSheet)
/* 960*/                        break;
                        }

/* 963*/                if(((AbstractNode) (obj)).getParentNode() instanceof XSLStyleSheet)
/* 963*/                    break;
/* 964*/                if(obj instanceof Binding)
                        {
/* 965*/                    int i = ((Binding)obj).getVariableFingerprint();
/* 966*/                    String s = namepool.getURI(i);
/* 967*/                    String s1 = namepool.getLocalName(i);
/* 968*/                    String s2 = s + '^' + s1;
/* 969*/                    if(hashtable.get(s2) == null)
/* 970*/                        hashtable.put(s2, s2);
                        }
                    } while(true);
/* 979*/        XSLStyleSheet xslstylesheet = getPrincipalStyleSheet();
/* 980*/        Vector vector = xslstylesheet.getTopLevel();
/* 981*/        for(int j = 0; j < vector.size(); j++)
                {
/* 982*/            Object obj2 = vector.elementAt(j);
/* 983*/            if((obj2 instanceof Binding) && obj2 != this)
                    {
/* 984*/                int k = ((Binding)obj2).getVariableFingerprint();
/* 985*/                String s3 = namepool.getURI(k);
/* 986*/                String s4 = namepool.getLocalName(k);
/* 987*/                String s5 = s3 + '^' + s4;
/* 988*/                if(hashtable.get(s5) == null)
/* 989*/                    hashtable1.put(s5, s5);
                    }
                }

/* 994*/        Enumeration aenumeration[] = new Enumeration[2];
/* 995*/        aenumeration[0] = hashtable1.keys();
/* 996*/        aenumeration[1] = hashtable.keys();
/* 997*/        return aenumeration;
            }

            public Function getStyleSheetFunction(int i)
            {
/*1011*/        XSLStyleSheet xslstylesheet = getPrincipalStyleSheet();
/*1012*/        Vector vector = xslstylesheet.getTopLevel();
/*1013*/        for(int j = vector.size() - 1; j >= 0; j--)
                {
/*1014*/            Object obj = vector.elementAt(j);
/*1015*/            if((obj instanceof SAXONFunction) && ((SAXONFunction)obj).getFunctionFingerprint() == i)
                    {
/*1017*/                StyleSheetFunctionCall stylesheetfunctioncall = new StyleSheetFunctionCall();
/*1018*/                stylesheetfunctioncall.setFunction((SAXONFunction)obj);
/*1019*/                return stylesheetfunctioncall;
                    }
                }

/*1022*/        return null;
            }
}
