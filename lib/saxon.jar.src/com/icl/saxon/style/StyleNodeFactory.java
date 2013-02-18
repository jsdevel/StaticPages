// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StyleNodeFactory.java

package com.icl.saxon.style;

import com.icl.saxon.Loader;
import com.icl.saxon.om.NamePool;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.tree.*;
import java.util.Hashtable;
import javax.xml.transform.*;
import org.xml.sax.Locator;

// Referenced classes of package com.icl.saxon.style:
//            XSLStyleSheet, LiteralResultElement, StyleElement, XSLApplyImports, 
//            XSLApplyTemplates, XSLAttribute, XSLAttributeSet, XSLCallTemplate, 
//            XSLChoose, XSLComment, XSLCopy, XSLCopyOf, 
//            XSLDecimalFormat, XSLDocument, XSLElement, XSLFallback, 
//            XSLForEach, XSLIf, XSLImport, XSLInclude, 
//            XSLKey, XSLMessage, XSLNumber, XSLNamespaceAlias, 
//            XSLOtherwise, XSLOutput, XSLParam, XSLPreserveSpace, 
//            XSLProcessingInstruction, XSLScript, XSLSort, XSLTemplate, 
//            XSLText, XSLValueOf, XSLVariable, XSLWithParam, 
//            XSLWhen, SAXONAssign, SAXONEntityRef, SAXONDoctype, 
//            SAXONFunction, SAXONGroup, SAXONHandler, SAXONItem, 
//            SAXONPreview, SAXONReturn, SAXONWhile, ExtensionElementFactory, 
//            StandardNames

public class StyleNodeFactory
    implements NodeFactory
{

            Hashtable userStyles;
            NamePool namePool;
            StandardNames sn;

            public StyleNodeFactory(NamePool namepool)
            {
/*  25*/        userStyles = new Hashtable();
/*  31*/        namePool = namepool;
/*  32*/        sn = namepool.getStandardNames();
            }

            public StandardNames getStandardNames()
            {
/*  36*/        return namePool.getStandardNames();
            }

            public ElementImpl makeElementNode(NodeInfo nodeinfo, int i, AttributeCollection attributecollection, int ai[], int j, Locator locator, int k)
            {
/*  63*/        boolean flag = nodeinfo instanceof XSLStyleSheet;
/*  65*/        String s = null;
/*  66*/        int l = -1;
/*  68*/        if(locator != null)
                {
/*  69*/            s = locator.getSystemId();
/*  70*/            l = locator.getLineNumber();
                }
/*  73*/        int i1 = i & 0xfffff;
/*  74*/        StyleElement styleelement = makeXSLElement(i1);
/*  76*/        if(styleelement != null)
                {
/*  78*/            try
                    {
/*  78*/                styleelement.setNamespaceDeclarations(ai, j);
/*  79*/                styleelement.initialise(i, attributecollection, nodeinfo, s, l, k);
/*  80*/                if(styleelement instanceof XSLStyleSheet)
                        {
/*  81*/                    styleelement.processVersionAttribute(sn.VERSION);
/*  82*/                    styleelement.processExtensionElementAttribute(sn.EXTENSION_ELEMENT_PREFIXES);
/*  83*/                    styleelement.processExcludedNamespaces(sn.EXCLUDE_RESULT_PREFIXES);
                        }
                    }
/*  86*/            catch(TransformerException transformerexception)
                    {
/*  86*/                styleelement.setValidationError(transformerexception, 2);
                    }
/*  88*/            return styleelement;
                }
/*  92*/        short word0 = namePool.getURICode(i);
/*  93*/        String s1 = namePool.getLocalName(i);
/*  95*/        Class class1 = com.icl.saxon.style.LiteralResultElement.class;
/* 101*/        Object obj = null;
/* 102*/        boolean flag1 = false;
/* 106*/        if(word0 == 4)
                {
/* 107*/            obj = makeExsltFunctionsElement(i1);
/* 108*/            if(obj != null)
                    {
/* 109*/                class1 = obj.getClass();
/* 110*/                flag1 = true;
                    }
                } else
/* 113*/        if(word0 == 3)
                {
/* 114*/            obj = makeSaxonElement(i1);
/* 115*/            if(obj != null)
                    {
/* 116*/                class1 = obj.getClass();
/* 117*/                flag1 = true;
                    }
                }
/* 120*/        if(obj == null)
/* 121*/            obj = new LiteralResultElement();
/* 124*/        ((ElementWithAttributes) (obj)).setNamespaceDeclarations(ai, j);
/* 127*/        try
                {
/* 127*/            ((ElementWithAttributes) (obj)).initialise(i, attributecollection, nodeinfo, s, l, k);
/* 128*/            ((StyleElement) (obj)).processExtensionElementAttribute(sn.XSL_EXTENSION_ELEMENT_PREFIXES);
/* 129*/            ((StyleElement) (obj)).processExcludedNamespaces(sn.XSL_EXCLUDE_RESULT_PREFIXES);
/* 130*/            ((StyleElement) (obj)).processVersionAttribute(sn.XSL_VERSION);
                }
/* 132*/        catch(TransformerException transformerexception1)
                {
/* 132*/            ((StyleElement) (obj)).setValidationError(transformerexception1, 2);
                }
/* 137*/        Object obj1 = null;
/* 138*/        Class class2 = com.icl.saxon.style.LiteralResultElement.class;
/* 140*/        if(word0 == 2)
                {
/* 141*/            TransformerConfigurationException transformerconfigurationexception = new TransformerConfigurationException("Unknown XSLT element: " + s1);
/* 142*/            class2 = com.icl.saxon.style.AbsentExtensionElement.class;
/* 143*/            ((StyleElement) (obj)).setValidationError(transformerconfigurationexception, 2);
                } else
/* 144*/        if(word0 == 3 || word0 == 4)
                {
/* 145*/            if(flag || ((StyleElement) (obj)).isExtensionNamespace(word0))
                    {
/* 146*/                if(flag1)
                        {
/* 148*/                    class2 = class1;
                        } else
                        {
/* 150*/                    class2 = com.icl.saxon.style.AbsentExtensionElement.class;
/* 151*/                    TransformerConfigurationException transformerconfigurationexception1 = new TransformerConfigurationException("Unknown Saxon extension element: " + s1);
/* 153*/                    ((StyleElement) (obj)).setValidationError(transformerconfigurationexception1, 3);
                        }
                    } else
                    {
/* 156*/                class2 = com.icl.saxon.style.LiteralResultElement.class;
                    }
                } else
/* 158*/        if(((StyleElement) (obj)).isExtensionNamespace(word0) && !flag)
                {
/* 159*/            Integer integer = new Integer(i & 0xfffff);
/* 161*/            class2 = (Class)userStyles.get(integer);
/* 162*/            if(class2 == null)
                    {
/* 163*/                ExtensionElementFactory extensionelementfactory = getFactory(word0);
/* 164*/                if(extensionelementfactory != null)
                        {
/* 165*/                    class2 = extensionelementfactory.getExtensionClass(s1);
/* 166*/                    if(class2 != null)
/* 167*/                        userStyles.put(integer, class2);
                        }
/* 170*/                if(class2 == null)
                        {
/* 177*/                    class2 = com.icl.saxon.style.AbsentExtensionElement.class;
/* 178*/                    TransformerConfigurationException transformerconfigurationexception2 = new TransformerConfigurationException("Unknown extension element");
/* 179*/                    ((StyleElement) (obj)).setValidationError(transformerconfigurationexception2, 3);
                        }
                    }
                } else
                {
/* 183*/            class2 = com.icl.saxon.style.LiteralResultElement.class;
                }
                Object obj2;
/* 187*/        if(!class2.equals(class1))
                {
/* 189*/            try
                    {
/* 189*/                obj2 = (StyleElement)class2.newInstance();
                    }
/* 194*/            catch(InstantiationException instantiationexception)
                    {
/* 194*/                throw new TransformerFactoryConfigurationError(instantiationexception, "Failed to create instance of " + class2.getName());
                    }
/* 196*/            catch(IllegalAccessException illegalaccessexception)
                    {
/* 196*/                throw new TransformerFactoryConfigurationError(illegalaccessexception, "Failed to access class " + class2.getName());
                    }
/* 198*/            ((StyleElement) (obj2)).substituteFor(((StyleElement) (obj)));
                } else
                {
/* 200*/            obj2 = obj;
                }
/* 202*/        return ((ElementImpl) (obj2));
            }

            private StyleElement makeXSLElement(int i)
            {
/* 212*/        Object obj = null;
/* 214*/        if(i == sn.XSL_APPLY_IMPORTS)
/* 214*/            obj = new XSLApplyImports();
/* 215*/        else
/* 215*/        if(i == sn.XSL_APPLY_TEMPLATES)
/* 215*/            obj = new XSLApplyTemplates();
/* 216*/        else
/* 216*/        if(i == sn.XSL_ATTRIBUTE)
/* 216*/            obj = new XSLAttribute();
/* 217*/        else
/* 217*/        if(i == sn.XSL_ATTRIBUTE_SET)
/* 217*/            obj = new XSLAttributeSet();
/* 218*/        else
/* 218*/        if(i == sn.XSL_CALL_TEMPLATE)
/* 218*/            obj = new XSLCallTemplate();
/* 219*/        else
/* 219*/        if(i == sn.XSL_CHOOSE)
/* 219*/            obj = new XSLChoose();
/* 220*/        else
/* 220*/        if(i == sn.XSL_COMMENT)
/* 220*/            obj = new XSLComment();
/* 221*/        else
/* 221*/        if(i == sn.XSL_COPY)
/* 221*/            obj = new XSLCopy();
/* 222*/        else
/* 222*/        if(i == sn.XSL_COPY_OF)
/* 222*/            obj = new XSLCopyOf();
/* 223*/        else
/* 223*/        if(i == sn.XSL_DECIMAL_FORMAT)
/* 223*/            obj = new XSLDecimalFormat();
/* 224*/        else
/* 224*/        if(i == sn.XSL_DOCUMENT)
/* 224*/            obj = new XSLDocument();
/* 225*/        else
/* 225*/        if(i == sn.XSL_ELEMENT)
/* 225*/            obj = new XSLElement();
/* 226*/        else
/* 226*/        if(i == sn.XSL_FALLBACK)
/* 226*/            obj = new XSLFallback();
/* 227*/        else
/* 227*/        if(i == sn.XSL_FOR_EACH)
/* 227*/            obj = new XSLForEach();
/* 228*/        else
/* 228*/        if(i == sn.XSL_IF)
/* 228*/            obj = new XSLIf();
/* 229*/        else
/* 229*/        if(i == sn.XSL_IMPORT)
/* 229*/            obj = new XSLImport();
/* 230*/        else
/* 230*/        if(i == sn.XSL_INCLUDE)
/* 230*/            obj = new XSLInclude();
/* 231*/        else
/* 231*/        if(i == sn.XSL_KEY)
/* 231*/            obj = new XSLKey();
/* 232*/        else
/* 232*/        if(i == sn.XSL_MESSAGE)
/* 232*/            obj = new XSLMessage();
/* 233*/        else
/* 233*/        if(i == sn.XSL_NUMBER)
/* 233*/            obj = new XSLNumber();
/* 234*/        else
/* 234*/        if(i == sn.XSL_NAMESPACE_ALIAS)
/* 234*/            obj = new XSLNamespaceAlias();
/* 235*/        else
/* 235*/        if(i == sn.XSL_OTHERWISE)
/* 235*/            obj = new XSLOtherwise();
/* 236*/        else
/* 236*/        if(i == sn.XSL_OUTPUT)
/* 236*/            obj = new XSLOutput();
/* 237*/        else
/* 237*/        if(i == sn.XSL_PARAM)
/* 237*/            obj = new XSLParam();
/* 238*/        else
/* 238*/        if(i == sn.XSL_PRESERVE_SPACE)
/* 238*/            obj = new XSLPreserveSpace();
/* 239*/        else
/* 239*/        if(i == sn.XSL_PROCESSING_INSTRUCTION)
/* 239*/            obj = new XSLProcessingInstruction();
/* 240*/        else
/* 240*/        if(i == sn.XSL_SCRIPT)
/* 240*/            obj = new XSLScript();
/* 241*/        else
/* 241*/        if(i == sn.XSL_SORT)
/* 241*/            obj = new XSLSort();
/* 242*/        else
/* 242*/        if(i == sn.XSL_STRIP_SPACE)
/* 242*/            obj = new XSLPreserveSpace();
/* 243*/        else
/* 243*/        if(i == sn.XSL_STYLESHEET)
/* 243*/            obj = new XSLStyleSheet();
/* 244*/        else
/* 244*/        if(i == sn.XSL_TEMPLATE)
/* 244*/            obj = new XSLTemplate();
/* 245*/        else
/* 245*/        if(i == sn.XSL_TEXT)
/* 245*/            obj = new XSLText();
/* 246*/        else
/* 246*/        if(i == sn.XSL_TRANSFORM)
/* 246*/            obj = new XSLStyleSheet();
/* 247*/        else
/* 247*/        if(i == sn.XSL_VALUE_OF)
/* 247*/            obj = new XSLValueOf();
/* 248*/        else
/* 248*/        if(i == sn.XSL_VARIABLE)
/* 248*/            obj = new XSLVariable();
/* 249*/        else
/* 249*/        if(i == sn.XSL_WITH_PARAM)
/* 249*/            obj = new XSLWithParam();
/* 250*/        else
/* 250*/        if(i == sn.XSL_WHEN)
/* 250*/            obj = new XSLWhen();
/* 252*/        return ((StyleElement) (obj));
            }

            private StyleElement makeSaxonElement(int i)
            {
/* 261*/        Object obj = null;
/* 263*/        if(i == sn.SAXON_ASSIGN)
/* 263*/            obj = new SAXONAssign();
/* 264*/        else
/* 264*/        if(i == sn.SAXON_ENTITY_REF)
/* 264*/            obj = new SAXONEntityRef();
/* 265*/        else
/* 265*/        if(i == sn.SAXON_DOCTYPE)
/* 265*/            obj = new SAXONDoctype();
/* 266*/        else
/* 266*/        if(i == sn.SAXON_FUNCTION)
/* 266*/            obj = new SAXONFunction();
/* 267*/        else
/* 267*/        if(i == sn.SAXON_GROUP)
/* 267*/            obj = new SAXONGroup();
/* 268*/        else
/* 268*/        if(i == sn.SAXON_HANDLER)
/* 268*/            obj = new SAXONHandler();
/* 269*/        else
/* 269*/        if(i == sn.SAXON_ITEM)
/* 269*/            obj = new SAXONItem();
/* 270*/        else
/* 270*/        if(i == sn.SAXON_OUTPUT)
/* 270*/            obj = new XSLDocument();
/* 271*/        else
/* 271*/        if(i == sn.SAXON_PREVIEW)
/* 271*/            obj = new SAXONPreview();
/* 272*/        else
/* 272*/        if(i == sn.SAXON_RETURN)
/* 272*/            obj = new SAXONReturn();
/* 273*/        else
/* 273*/        if(i == sn.SAXON_SCRIPT)
/* 273*/            obj = new XSLScript();
/* 274*/        else
/* 274*/        if(i == sn.SAXON_WHILE)
/* 274*/            obj = new SAXONWhile();
/* 275*/        return ((StyleElement) (obj));
            }

            private StyleElement makeExsltFunctionsElement(int i)
            {
/* 284*/        if(i == sn.EXSLT_FUNC_FUNCTION)
/* 284*/            return new SAXONFunction();
/* 285*/        if(i == sn.EXSLT_FUNC_RESULT)
/* 285*/            return new SAXONReturn();
/* 287*/        else
/* 287*/            return null;
            }

            private ExtensionElementFactory getFactory(short word0)
            {
/* 296*/        String s = namePool.getURIFromNamespaceCode(word0);
/* 297*/        int i = s.lastIndexOf('/');
/* 298*/        if(i < 0 || i == s.length() - 1)
/* 299*/            return null;
/* 301*/        String s1 = s.substring(i + 1);
                ExtensionElementFactory extensionelementfactory;
/* 305*/        try
                {
/* 305*/            extensionelementfactory = (ExtensionElementFactory)Loader.getInstance(s1);
                }
/* 307*/        catch(Exception exception)
                {
/* 307*/            return null;
                }
/* 309*/        return extensionelementfactory;
            }

            public boolean isElementAvailable(String s, String s1)
            {
/* 317*/        int i = namePool.getFingerprint(s, s1);
/* 318*/        if(s.equals("http://www.w3.org/1999/XSL/Transform"))
                {
/* 319*/            if(i == -1)
/* 319*/                return false;
/* 320*/            StyleElement styleelement = makeXSLElement(i);
/* 321*/            if(styleelement != null)
/* 321*/                return styleelement.isInstruction();
                }
/* 324*/        if(s.equals("http://icl.com/saxon"))
                {
/* 325*/            if(i == -1)
/* 325*/                return false;
/* 326*/            StyleElement styleelement1 = makeSaxonElement(i);
/* 327*/            if(styleelement1 != null)
/* 327*/                return styleelement1.isInstruction();
                }
/* 330*/        short word0 = namePool.getCodeForURI(s);
/* 331*/        ExtensionElementFactory extensionelementfactory = getFactory(word0);
/* 332*/        if(extensionelementfactory == null)
                {
/* 332*/            return false;
                } else
                {
/* 333*/            Class class1 = extensionelementfactory.getExtensionClass(s1);
/* 334*/            return class1 != null;
                }
            }
}
