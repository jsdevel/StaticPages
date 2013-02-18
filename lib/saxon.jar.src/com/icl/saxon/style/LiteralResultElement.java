// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   LiteralResultElement.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.PreparedStyleSheet;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.expr.StringValue;
import com.icl.saxon.om.*;
import com.icl.saxon.output.Emitter;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.tree.*;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.helpers.AttributesImpl;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, XSLStyleSheet, StandardNames

public class LiteralResultElement extends StyleElement
{

            private int resultNameCode;
            private int attributeNames[];
            private Expression attributeValues[];
            private boolean attributeChecked[];
            private int numberOfAttributes;
            private boolean toplevel;
            private int namespaceCodes[];

            public LiteralResultElement()
            {
            }

            public boolean mayContainTemplateBody()
            {
/*  41*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  60*/        toplevel = getParentNode() instanceof XSLStyleSheet;
/*  62*/        StandardNames standardnames = getStandardNames();
/*  63*/        resultNameCode = getNameCode();
/*  65*/        NamePool namepool = getNamePool();
/*  66*/        short word0 = namepool.getURICode(resultNameCode);
/*  68*/        if(toplevel)
                {
/*  72*/            if(word0 == 0)
/*  73*/                compileError("Top level elements must have a non-null namespace URI");
                } else
                {
/*  92*/            boolean flag = false;
/*  93*/            if((getParent() instanceof LiteralResultElement) && (super.namespaceList == null || super.namespaceList.length == 0) && word0 == namepool.getURICode(getParent().getFingerprint()))
/*  98*/                flag = true;
/* 100*/            if(flag)
                    {
/* 101*/                for(int i = 0; i < super.attributeList.getLength(); i++)
                        {
/* 102*/                    if((super.attributeList.getNameCode(i) >> 20 & 0xff) == 0)
/* 103*/                        continue;
/* 103*/                    flag = false;
/* 104*/                    break;
                        }

                    }
/* 109*/            if(flag)
/* 110*/                namespaceCodes = new int[0];
/* 112*/            else
/* 112*/                namespaceCodes = getNamespaceCodes();
/* 117*/            XSLStyleSheet xslstylesheet = getPrincipalStyleSheet();
/* 119*/            if(xslstylesheet.hasNamespaceAliases())
                    {
/* 120*/                for(int j = 0; j < namespaceCodes.length; j++)
                        {
/* 122*/                    short word1 = (short)(namespaceCodes[j] & 0xffff);
/* 123*/                    short word3 = xslstylesheet.getNamespaceAlias(word1);
/* 124*/                    if(word1 != word3)
                            {
/* 126*/                        int i1 = namespaceCodes[j] & 0xffff0000;
/* 127*/                        namespaceCodes[j] = i1 | word3;
                            }
                        }

/* 134*/                short word2 = xslstylesheet.getNamespaceAlias(word0);
/* 136*/                if(word2 != word0)
                        {
/* 137*/                    word0 = word2;
/* 138*/                    resultNameCode = namepool.allocate(getPrefix(), word2, getLocalName());
                        }
                    }
/* 144*/            int k = super.attributeList.getLength();
/* 145*/            attributeNames = new int[k];
/* 146*/            attributeValues = new Expression[k];
/* 147*/            attributeChecked = new boolean[k];
/* 148*/            short aword0[] = new short[k];
/* 149*/            numberOfAttributes = 0;
/* 151*/            for(int l = 0; l < k; l++)
                    {
/* 153*/                int j1 = super.attributeList.getNameCode(l);
/* 154*/                int l1 = j1;
/* 155*/                int i2 = j1 & 0xfffff;
/* 156*/                short word5 = namepool.getURICode(j1);
/* 158*/                if(i2 == standardnames.XSL_USE_ATTRIBUTE_SETS)
/* 159*/                    findAttributeSets(super.attributeList.getValue(l));
/* 160*/                else
/* 160*/                if(i2 != standardnames.XSL_EXTENSION_ELEMENT_PREFIXES && i2 != standardnames.XSL_EXCLUDE_RESULT_PREFIXES && i2 != standardnames.XSL_VERSION)
                        {
/* 168*/                    if(word5 == 2)
/* 169*/                        compileError("Unknown XSL attribute " + namepool.getDisplayName(j1));
/* 171*/                    if(word5 != 0)
                            {
/* 172*/                        short word6 = xslstylesheet.getNamespaceAlias(word5);
/* 173*/                        if(word6 != word5)
                                {
/* 174*/                            String s = namepool.getDisplayName(j1);
/* 175*/                            String s1 = Name.getPrefix(s);
/* 176*/                            String s3 = Name.getLocalName(s);
/* 178*/                            String s4 = namepool.getURIFromNamespaceCode(word6);
/* 179*/                            l1 = namepool.allocate(s1, s4, s3);
/* 181*/                            word5 = word6;
                                }
                            }
/* 185*/                    attributeNames[numberOfAttributes] = l1;
/* 186*/                    aword0[numberOfAttributes] = word5;
/* 187*/                    Expression expression = makeAttributeValueTemplate(super.attributeList.getValue(l));
/* 188*/                    attributeValues[numberOfAttributes] = expression;
/* 195*/                    attributeChecked[numberOfAttributes] = false;
/* 196*/                    boolean flag2 = false;
/* 197*/                    if(expression instanceof StringValue)
                            {
/* 198*/                        String s2 = ((StringValue)expression).asString();
/* 199*/                        for(int k2 = 0; k2 < s2.length(); k2++)
                                {
/* 200*/                            char c = s2.charAt(k2);
/* 201*/                            if(c >= '!' && c <= '~' && c != '<' && c != '>' && c != '&' && c != '"')
/* 203*/                                continue;
/* 203*/                            flag2 = true;
/* 204*/                            break;
                                }

/* 207*/                        attributeChecked[numberOfAttributes] = !flag2;
                            }
/* 209*/                    numberOfAttributes++;
                        }
                    }

/* 216*/            for(int k1 = 0; k1 < namespaceCodes.length; k1++)
                    {
/* 217*/                short word4 = (short)(namespaceCodes[k1] & 0xffff);
/* 218*/                if(isExcludedNamespace(word4))
                        {
/* 220*/                    boolean flag1 = true;
/* 224*/                    if(word4 == word0)
/* 225*/                        flag1 = false;
/* 230*/                    for(int j2 = 0; j2 < numberOfAttributes; j2++)
                            {
/* 231*/                        if(word4 != aword0[j2])
/* 232*/                            continue;
/* 232*/                        flag1 = false;
/* 233*/                        break;
                            }

/* 239*/                    if(flag1)
/* 240*/                        namespaceCodes[k1] = -1;
                        }
                    }

                }
            }

            protected void validateChildren()
                throws TransformerConfigurationException
            {
/* 250*/        if(!toplevel)
/* 251*/            super.validateChildren();
            }

            public void process(Context context)
                throws TransformerException
            {
/* 262*/        if(toplevel)
/* 262*/            return;
/* 265*/        Outputter outputter = context.getOutputter();
/* 266*/        outputter.writeStartTag(resultNameCode);
/* 270*/        for(int i = 0; i < namespaceCodes.length; i++)
/* 271*/            if(namespaceCodes[i] != -1)
/* 272*/                outputter.writeNamespaceDeclaration(namespaceCodes[i]);

/* 278*/        processAttributeSets(context);
/* 282*/        for(int j = 0; j < numberOfAttributes; j++)
                {
/* 283*/            int k = attributeNames[j];
/* 284*/            String s = attributeValues[j].evaluateAsString(context);
/* 285*/            outputter.writeAttribute(k, s, attributeChecked[j]);
                }

/* 290*/        processChildren(context);
/* 294*/        outputter.writeEndTag(resultNameCode);
            }

            public DocumentImpl makeStyleSheet(PreparedStyleSheet preparedstylesheet)
                throws TransformerConfigurationException
            {
/* 308*/        NamePool namepool = getNamePool();
/* 309*/        StandardNames standardnames = getStandardNames();
/* 310*/        String s = getPrefixForURI("http://www.w3.org/1999/XSL/Transform");
/* 311*/        if(s == null)
                {
                    String s1;
/* 313*/            if(getLocalName().equals("stylesheet") || getLocalName().equals("transform"))
                    {
/* 314*/                if(getPrefixForURI("http://www.w3.org/TR/WD-xsl") != null)
/* 315*/                    s1 = "Saxon is not able to process Microsoft's WD-xsl dialect";
/* 317*/                else
/* 317*/                    s1 = "Namespace for stylesheet element should be http://www.w3.org/1999/XSL/Transform";
                    } else
                    {
/* 320*/                s1 = "The supplied file does not appear to be a stylesheet";
                    }
/* 322*/            TransformerConfigurationException transformerconfigurationexception = new TransformerConfigurationException(s1);
/* 324*/            try
                    {
/* 324*/                preparedstylesheet.reportError(transformerconfigurationexception);
                    }
/* 324*/            catch(TransformerException transformerexception1) { }
/* 325*/            throw transformerconfigurationexception;
                }
/* 332*/        String s2 = getAttributeValue(standardnames.XSL_VERSION);
/* 333*/        if(s2 == null)
                {
/* 334*/            TransformerConfigurationException transformerconfigurationexception1 = new TransformerConfigurationException("Literal Result Element As Stylesheet: xsl:version attribute is missing");
/* 337*/            try
                    {
/* 337*/                preparedstylesheet.reportError(transformerconfigurationexception1);
                    }
/* 337*/            catch(TransformerException transformerexception2) { }
/* 338*/            throw transformerconfigurationexception1;
                }
/* 342*/        try
                {
/* 342*/            TreeBuilder treebuilder = new TreeBuilder();
/* 343*/            treebuilder.setDocumentLocator(null);
/* 344*/            treebuilder.setNamePool(namepool);
/* 345*/            treebuilder.setNodeFactory(((DocumentImpl)getParentNode()).getNodeFactory());
/* 346*/            treebuilder.setSystemId(getSystemId());
/* 348*/            treebuilder.startDocument();
/* 349*/            AttributesImpl attributesimpl = new AttributesImpl();
/* 350*/            attributesimpl.addAttribute("", "version", "version", "CDATA", s2);
/* 351*/            int ai[] = new int[1];
/* 352*/            ai[0] = namepool.getNamespaceCode("xsl", "http://www.w3.org/1999/XSL/Transform");
/* 353*/            int i = namepool.allocate("xsl", "http://www.w3.org/1999/XSL/Transform", "stylesheet");
/* 354*/            treebuilder.startElement(i, attributesimpl, ai, 1);
/* 356*/            int j = namepool.allocate("xsl", "http://www.w3.org/1999/XSL/Transform", "template");
/* 357*/            attributesimpl.clear();
/* 358*/            attributesimpl.addAttribute("", "match", "match", "CDATA", "/");
/* 359*/            treebuilder.startElement(j, attributesimpl, ai, 0);
/* 361*/            treebuilder.graftElement(this);
/* 363*/            treebuilder.endElement(j);
/* 364*/            treebuilder.endElement(i);
/* 365*/            treebuilder.endDocument();
/* 367*/            return (DocumentImpl)treebuilder.getCurrentDocument();
                }
/* 369*/        catch(TransformerException transformerexception)
                {
/* 369*/            throw new TransformerConfigurationException(transformerexception);
                }
            }
}
