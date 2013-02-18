// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLStyleSheet.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.om.*;
import com.icl.saxon.trace.SimpleTraceListener;
import com.icl.saxon.trace.TraceListener;
import com.icl.saxon.tree.*;
import java.util.Properties;
import java.util.Vector;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, XSLGeneralIncorporate, XSLNamespaceAlias, XSLOutput, 
//            XSLScript, XSLTemplate, StandardNames, XSLGeneralOutput

public class XSLStyleSheet extends StyleElement
{

            private boolean wasIncluded;
            private int precedence;
            private int minImportPrecedence;
            private XSLStyleSheet importer;
            private PreparedStyleSheet stylesheet;
            private Vector topLevel;
            private Mode stripperRules;
            private RuleManager ruleManager;
            private KeyManager keyManager;
            private DecimalFormatManager decimalFormatManager;
            private PreviewManager previewManager;
            private int numberOfAliases;
            private short aliasSCodes[];
            private short aliasRCodes[];
            private int numberOfVariables;
            private int largestStackFrame;

            public XSLStyleSheet()
            {
/*  26*/        wasIncluded = false;
/*  29*/        precedence = 0;
/*  32*/        minImportPrecedence = 0;
/*  35*/        importer = null;
/*  44*/        stripperRules = null;
/*  50*/        keyManager = new KeyManager();
/*  53*/        decimalFormatManager = new DecimalFormatManager();
/*  56*/        previewManager = null;
/*  62*/        numberOfAliases = 0;
/*  63*/        aliasSCodes = new short[5];
/*  64*/        aliasRCodes = new short[5];
/*  67*/        numberOfVariables = 0;
/*  70*/        largestStackFrame = 0;
            }

            public void setPreparedStyleSheet(PreparedStyleSheet preparedstylesheet)
            {
/*  78*/        stylesheet = preparedstylesheet;
/*  79*/        ruleManager = new RuleManager(preparedstylesheet.getNamePool());
            }

            public PreparedStyleSheet getPreparedStyleSheet()
            {
/*  87*/        if(importer != null)
/*  87*/            return importer.getPreparedStyleSheet();
/*  88*/        else
/*  88*/            return stylesheet;
            }

            public RuleManager getRuleManager()
            {
/*  96*/        return ruleManager;
            }

            protected Mode getStripperRules()
            {
/* 104*/        if(stripperRules == null)
/* 105*/            stripperRules = new Mode();
/* 107*/        return stripperRules;
            }

            public Stripper newStripper()
            {
/* 116*/        return new Stripper(stripperRules);
            }

            public boolean stripsWhitespace()
            {
/* 124*/        StandardNames standardnames = getStandardNames();
/* 125*/        for(int i = 0; i < topLevel.size(); i++)
                {
/* 126*/            NodeInfo nodeinfo = (NodeInfo)topLevel.elementAt(i);
/* 127*/            if(nodeinfo.getFingerprint() == standardnames.XSL_STRIP_SPACE)
/* 128*/                return true;
                }

/* 131*/        return false;
            }

            public KeyManager getKeyManager()
            {
/* 139*/        return keyManager;
            }

            public DecimalFormatManager getDecimalFormatManager()
            {
/* 147*/        return decimalFormatManager;
            }

            public PreviewManager getPreviewManager()
            {
/* 156*/        return previewManager;
            }

            public void setPreviewManager(PreviewManager previewmanager)
            {
/* 164*/        previewManager = previewmanager;
            }

            public void setPrecedence(int i)
            {
/* 172*/        precedence = i;
            }

            public int getPrecedence()
            {
/* 180*/        if(wasIncluded)
/* 180*/            return importer.getPrecedence();
/* 181*/        else
/* 181*/            return precedence;
            }

            public int getMinImportPrecedence()
            {
/* 190*/        return minImportPrecedence;
            }

            public void setMinImportPrecedence(int i)
            {
/* 199*/        minImportPrecedence = i;
            }

            public void setImporter(XSLStyleSheet xslstylesheet)
            {
/* 207*/        importer = xslstylesheet;
            }

            public XSLStyleSheet getImporter()
            {
/* 216*/        return importer;
            }

            public void setWasIncluded()
            {
/* 225*/        wasIncluded = true;
            }

            public boolean wasIncluded()
            {
/* 234*/        return wasIncluded;
            }

            public Vector getTopLevel()
            {
/* 242*/        return topLevel;
            }

            public int allocateSlotNumber()
            {
/* 250*/        return numberOfVariables++;
            }

            public void allocateLocalSlots(int i)
            {
/* 258*/        if(i > largestStackFrame)
/* 259*/            largestStackFrame = i;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/* 269*/        StandardNames standardnames = getStandardNames();
/* 270*/        AttributeCollection attributecollection = getAttributeList();
/* 271*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/* 273*/            int j = attributecollection.getNameCode(i);
/* 274*/            int k = j & 0xfffff;
/* 275*/            if(k == standardnames.VERSION)
/* 276*/                super.version = attributecollection.getValueByFingerprint(k);
/* 277*/            else
/* 277*/            if(k != standardnames.ID && k != standardnames.EXTENSION_ELEMENT_PREFIXES && k != standardnames.EXCLUDE_RESULT_PREFIXES)
/* 284*/                checkUnknownAttribute(j);
                }

/* 287*/        if(super.version == null)
/* 288*/            reportAbsence("version");
            }

            protected void processVersionAttribute(int i)
            {
/* 297*/        super.version = getAttributeValue(i & 0xfffff);
            }

            protected short getNamespaceAlias(short word0)
            {
/* 311*/        for(int i = numberOfAliases - 1; i >= 0; i--)
/* 312*/            if(word0 == aliasSCodes[i])
/* 313*/                return aliasRCodes[i];

/* 316*/        return word0;
            }

            public void validate()
                throws TransformerConfigurationException
            {
/* 324*/        if(super.validationError != null)
/* 325*/            compileError(super.validationError);
/* 327*/        if(!(getParentNode() instanceof DocumentInfo))
/* 328*/            throw new TransformerConfigurationException(getDisplayName() + " must be the outermost element");
/* 331*/        else
/* 331*/            return;
            }

            public void preprocess()
                throws TransformerConfigurationException
            {
/* 343*/        spliceIncludes();
/* 348*/        processAllAttributes();
/* 352*/        collectNamespaceAliases();
/* 356*/        validate();
/* 357*/        for(int i = 0; i < topLevel.size(); i++)
                {
/* 358*/            Object obj = topLevel.elementAt(i);
/* 359*/            if(obj instanceof StyleElement)
/* 360*/                ((StyleElement)obj).validateSubtree();
                }

/* 366*/        for(int j = 0; j < topLevel.size(); j++)
                {
/* 367*/            Object obj1 = topLevel.elementAt(j);
/* 368*/            if(obj1 instanceof StyleElement)
/* 370*/                try
                        {
/* 370*/                    ((StyleElement)obj1).preprocess();
                        }
/* 372*/                catch(TransformerConfigurationException transformerconfigurationexception)
                        {
/* 372*/                    ((StyleElement)obj1).compileError(transformerconfigurationexception);
                        }
                }

            }

            public void spliceIncludes()
                throws TransformerConfigurationException
            {
/* 385*/        boolean flag = false;
/* 386*/        topLevel = new Vector();
/* 387*/        minImportPrecedence = precedence;
/* 388*/        Object obj = this;
/* 390*/        for(NodeImpl nodeimpl = (NodeImpl)getFirstChild(); nodeimpl != null; nodeimpl = (NodeImpl)nodeimpl.getNextSibling())
/* 394*/            if(nodeimpl.getNodeType() == 3)
                    {
/* 396*/                if(!Navigator.isWhite(nodeimpl.getStringValue()))
/* 397*/                    ((StyleElement) (obj)).compileError("No character data is allowed between top-level elements");
                    } else
                    {
/* 402*/                obj = (StyleElement)nodeimpl;
/* 403*/                if(nodeimpl instanceof XSLGeneralIncorporate)
                        {
/* 404*/                    XSLGeneralIncorporate xslgeneralincorporate = (XSLGeneralIncorporate)nodeimpl;
/* 405*/                    xslgeneralincorporate.processAttributes();
/* 407*/                    if(xslgeneralincorporate.isImport())
                            {
/* 408*/                        if(flag)
/* 409*/                            xslgeneralincorporate.compileError("xsl:import elements must come first");
                            } else
                            {
/* 412*/                        flag = true;
                            }
/* 418*/                    XSLStyleSheet xslstylesheet = xslgeneralincorporate.getIncludedStyleSheet(this, precedence);
/* 420*/                    if(xslstylesheet == null)
/* 420*/                        return;
/* 425*/                    if(xslgeneralincorporate.isImport())
                            {
/* 426*/                        precedence = xslstylesheet.getPrecedence() + 1;
                            } else
                            {
/* 428*/                        precedence = xslstylesheet.getPrecedence();
/* 429*/                        xslstylesheet.setMinImportPrecedence(minImportPrecedence);
/* 430*/                        xslstylesheet.setWasIncluded();
                            }
/* 441*/                    Vector vector = xslstylesheet.topLevel;
/* 442*/                    for(int i = 0; i < vector.size(); i++)
                            {
/* 443*/                        StyleElement styleelement = (StyleElement)vector.elementAt(i);
/* 444*/                        int j = topLevel.size() - 1;
/* 445*/                        if(j < 0 || styleelement.getPrecedence() >= ((StyleElement)topLevel.elementAt(j)).getPrecedence())
                                {
/* 446*/                            topLevel.addElement(styleelement);
                                } else
                                {
/* 449*/                            for(; j >= 0 && styleelement.getPrecedence() < ((StyleElement)topLevel.elementAt(j)).getPrecedence(); j--);
/* 451*/                            topLevel.insertElementAt(styleelement, j + 1);
                                }
                            }

                        } else
                        {
/* 455*/                    flag = true;
/* 456*/                    topLevel.addElement(nodeimpl);
                        }
                    }

            }

            private void collectNamespaceAliases()
            {
/* 469*/        for(int i = 0; i < topLevel.size(); i++)
                {
/* 470*/            Object obj = topLevel.elementAt(i);
/* 471*/            if(obj instanceof XSLNamespaceAlias)
                    {
/* 472*/                XSLNamespaceAlias xslnamespacealias = (XSLNamespaceAlias)obj;
/* 474*/                if(numberOfAliases == aliasSCodes.length)
                        {
/* 475*/                    short aword0[] = new short[numberOfAliases * 2];
/* 476*/                    short aword1[] = new short[numberOfAliases * 2];
/* 477*/                    System.arraycopy(aliasSCodes, 0, aword0, 0, numberOfAliases);
/* 478*/                    System.arraycopy(aliasRCodes, 0, aword1, 0, numberOfAliases);
/* 479*/                    aliasSCodes = aword0;
/* 480*/                    aliasRCodes = aword1;
                        }
/* 482*/                aliasSCodes[numberOfAliases] = xslnamespacealias.getStylesheetURICode();
/* 483*/                aliasRCodes[numberOfAliases] = xslnamespacealias.getResultURICode();
/* 484*/                numberOfAliases++;
                    }
                }

            }

            protected boolean hasNamespaceAliases()
            {
/* 490*/        return numberOfAliases > 0;
            }

            public void processAllAttributes()
                throws TransformerConfigurationException
            {
/* 498*/        prepareAttributes();
/* 499*/        Vector vector = topLevel;
/* 500*/        for(int i = 0; i < vector.size(); i++)
                {
/* 501*/            Object obj = vector.elementAt(i);
/* 502*/            if(obj instanceof StyleElement)
/* 504*/                try
                        {
/* 504*/                    ((StyleElement)obj).processAllAttributes();
                        }
/* 506*/                catch(TransformerConfigurationException transformerconfigurationexception)
                        {
/* 506*/                    ((StyleElement)obj).compileError(transformerconfigurationexception);
                        }
                }

            }

            public void initialiseBindery(Bindery bindery)
            {
/* 520*/        bindery.allocateGlobals(numberOfVariables);
/* 521*/        bindery.allocateLocals(largestStackFrame);
            }

            public void gatherOutputProperties(Properties properties)
            {
/* 532*/        Vector vector = topLevel;
/* 533*/        for(int i = 0; i < vector.size(); i++)
                {
/* 534*/            Object obj = vector.elementAt(i);
/* 535*/            if(obj instanceof XSLOutput)
/* 536*/                ((XSLOutput)obj).gatherOutputProperties(properties);
                }

            }

            public void updateOutputProperties(Properties properties, Context context)
                throws TransformerException
            {
/* 549*/        Vector vector = topLevel;
/* 550*/        for(int i = 0; i < vector.size(); i++)
                {
/* 551*/            Object obj = vector.elementAt(i);
/* 552*/            if(obj instanceof XSLOutput)
/* 553*/                ((XSLOutput)obj).updateOutputProperties(properties, context);
                }

            }

            public Class getExternalJavaClass(String s)
                throws TransformerException
            {
/* 566*/        Vector vector = topLevel;
/* 567*/        if(!((Boolean)getPreparedStyleSheet().getTransformerFactory().getAttribute("http://icl.com/saxon/feature/allow-external-functions")).booleanValue())
/* 569*/            return null;
/* 571*/        for(int i = vector.size() - 1; i >= 0; i--)
                {
/* 572*/            Object obj = vector.elementAt(i);
/* 573*/            if(obj instanceof XSLScript)
                    {
/* 574*/                XSLScript xslscript = (XSLScript)obj;
/* 575*/                Class class1 = xslscript.getJavaClass(s);
/* 576*/                if(class1 != null)
/* 577*/                    return class1;
                    }
                }

/* 581*/        return null;
            }

            public void process(Context context)
                throws TransformerException
            {
/* 592*/        Controller controller = context.getController();
/* 594*/        String s = getAttributeValue("http://icl.com/saxon", "trace");
/* 595*/        if(s != null && s.equals("yes"))
/* 596*/            controller.setTraceListener(new SimpleTraceListener());
/* 601*/        Vector vector = topLevel;
/* 603*/        boolean flag = controller.isTracing();
/* 604*/        TraceListener tracelistener = null;
/* 606*/        if(flag)
                {
/* 607*/            tracelistener = controller.getTraceListener();
/* 608*/            for(int i = 0; i < vector.size(); i++)
                    {
/* 609*/                Object obj = vector.elementAt(i);
/* 610*/                tracelistener.toplevel((NodeInfo)obj);
                    }

                }
/* 614*/        for(int j = 0; j < vector.size(); j++)
                {
/* 615*/            Object obj1 = vector.elementAt(j);
/* 616*/            if(obj1 instanceof StyleElement)
/* 618*/                try
                        {
/* 618*/                    if(flag && !(obj1 instanceof XSLTemplate))
                            {
/* 619*/                        tracelistener.enter((StyleElement)obj1, context);
/* 620*/                        ((StyleElement)obj1).process(context);
/* 621*/                        tracelistener.leave((StyleElement)obj1, context);
                            } else
                            {
/* 623*/                        ((StyleElement)obj1).process(context);
                            }
                        }
/* 626*/                catch(TransformerException transformerexception)
                        {
/* 626*/                    throw ((StyleElement)obj1).styleError(transformerexception);
                        }
                }

            }
}
