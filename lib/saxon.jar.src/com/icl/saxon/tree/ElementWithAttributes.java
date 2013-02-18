// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ElementWithAttributes.java

package com.icl.saxon.tree;

import com.icl.saxon.om.*;
import com.icl.saxon.output.Outputter;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.w3c.dom.*;

// Referenced classes of package com.icl.saxon.tree:
//            ElementImpl, ParentNodeImpl, DocumentImpl, NodeImpl, 
//            NamespaceImpl, AttributeImpl, AttributeCollection

public class ElementWithAttributes extends ElementImpl
    implements Element, NamedNodeMap
{

            protected AttributeCollection attributeList;
            protected int namespaceList[];

            public ElementWithAttributes()
            {
/*  33*/        namespaceList = null;
            }

            public void initialise(int i, AttributeCollection attributecollection, NodeInfo nodeinfo, String s, int j, int k)
            {
/*  47*/        super.nameCode = i;
/*  48*/        attributeList = attributecollection;
/*  49*/        super.parent = (ParentNodeImpl)nodeinfo;
/*  50*/        super.sequence = k;
/*  51*/        super.root = (DocumentImpl)nodeinfo.getDocumentRoot();
/*  52*/        super.root.setLineNumber(k, j);
/*  53*/        super.root.setSystemId(k, s);
            }

            public void setNamespaceDeclarations(int ai[], int i)
            {
/*  61*/        namespaceList = new int[i];
/*  62*/        System.arraycopy(ai, 0, namespaceList, 0, i);
            }

            public short getURICodeForPrefix(String s)
                throws NamespaceException
            {
/*  75*/        if(s.equals("xml"))
/*  75*/            return 1;
/*  77*/        NamePool namepool = getNamePool();
/*  78*/        short word0 = namepool.getCodeForPrefix(s);
/*  79*/        if(word0 == -1)
/*  80*/            throw new NamespaceException(s);
/*  82*/        else
/*  82*/            return getURICodeForPrefixCode(word0);
            }

            private short getURICodeForPrefixCode(int i)
                throws NamespaceException
            {
/*  86*/        if(namespaceList != null)
                {
/*  87*/            for(int j = 0; j < namespaceList.length; j++)
/*  88*/                if(namespaceList[j] >> 16 == i)
/*  89*/                    return (short)(namespaceList[j] & 0xffff);

                }
/*  93*/        Object obj = super.parent;
/*  95*/        do
                {
/*  95*/            if(((AbstractNode) (obj)).getNodeType() == 9)
/*  97*/                if(i == 0)
/*  97*/                    return 0;
/*  98*/                else
/*  98*/                    throw new NamespaceException(getNamePool().getPrefixFromNamespaceCode(i << 16));
/*  99*/            if(obj instanceof ElementWithAttributes)
/* 100*/                return ((ElementWithAttributes)obj).getURICodeForPrefixCode(i);
/* 102*/            obj = (NodeImpl)((AbstractNode) (obj)).getParentNode();
                } while(true);
            }

            public String getPrefixForURI(String s)
            {
/* 116*/        if(s.equals("http://www.w3.org/XML/1998/namespace"))
/* 116*/            return "xml";
/* 118*/        NamePool namepool = getNamePool();
/* 119*/        short word0 = namepool.getCodeForURI(s);
/* 120*/        if(word0 < 0)
/* 120*/            return null;
/* 121*/        else
/* 121*/            return getPrefixForURICode(word0);
            }

            private String getPrefixForURICode(int i)
            {
/* 125*/        if(namespaceList != null)
                {
/* 126*/            for(int j = 0; j < namespaceList.length; j++)
/* 127*/                if((namespaceList[j] & 0xffff) == i)
/* 128*/                    return getNamePool().getPrefixFromNamespaceCode(namespaceList[j]);

                }
/* 132*/        Object obj = super.parent;
/* 134*/        do
                {
/* 134*/            if(obj instanceof DocumentInfo)
/* 135*/                return null;
/* 136*/            if(obj instanceof ElementWithAttributes)
/* 137*/                return ((ElementWithAttributes)obj).getPrefixForURICode(i);
/* 139*/            obj = (NodeImpl)((AbstractNode) (obj)).getParentNode();
                } while(true);
            }

            public void addNamespaceNodes(ElementImpl elementimpl, Vector vector, boolean flag)
            {
/* 157*/        if(namespaceList != null)
                {
/* 158*/            int i = vector.size();
/* 159*/            for(int k = 0; k < namespaceList.length; k++)
                    {
/* 160*/                int l = namespaceList[k];
/* 161*/                int i1 = l >> 16;
/* 163*/                boolean flag1 = false;
/* 166*/                for(int j1 = 0; j1 < i;)
                        {
/* 167*/                    NamespaceImpl namespaceimpl = (NamespaceImpl)vector.elementAt(j1++);
/* 168*/                    if(namespaceimpl.getNamespaceCode() == i1)
                            {
/* 169*/                        flag1 = true;
/* 170*/                        break;
                            }
                        }

/* 173*/                if(!flag1)
/* 174*/                    vector.addElement(new NamespaceImpl(elementimpl, l, vector.size() + 1));
                    }

                }
/* 183*/        if(super.parent.getNodeType() != 9)
/* 184*/            ((ElementImpl)super.parent).addNamespaceNodes(elementimpl, vector, false);
/* 187*/        if(flag)
                {
/* 188*/            int j = 0x10001;
/* 189*/            vector.addElement(new NamespaceImpl(this, j, vector.size() + 1));
                }
            }

            public void outputNamespaceNodes(Outputter outputter, boolean flag)
                throws TransformerException
            {
/* 202*/        if(namespaceList != null)
                {
/* 203*/            for(int i = 0; i < namespaceList.length; i++)
/* 204*/                outputter.writeNamespaceDeclaration(namespaceList[i]);

                }
/* 211*/        if(flag && super.parent.getNodeType() != 9)
/* 213*/            ((ElementImpl)super.parent).outputNamespaceNodes(outputter, true);
            }

            protected int[] getNamespaceCodes()
            {
/* 224*/        Vector vector = new Vector();
/* 225*/        addNamespaceNodes(this, vector, true);
/* 228*/        int ai[] = new int[vector.size()];
/* 229*/        for(int i = 0; i < vector.size(); i++)
                {
/* 230*/            NamespaceImpl namespaceimpl = (NamespaceImpl)vector.elementAt(i);
/* 231*/            ai[i] = namespaceimpl.getNamespaceCode();
                }

/* 233*/        return ai;
            }

            public AttributeCollection getAttributeList()
            {
/* 244*/        return attributeList;
            }

            public boolean hasAttributes()
            {
/* 255*/        return attributeList.getLength() > 0;
            }

            public String getAttributeValue(String s, String s1)
            {
/* 268*/        return attributeList.getValue(s, s1);
            }

            public String getAttributeValue(String s)
            {
/* 282*/        return attributeList.getValue(s);
            }

            public String getAttributeValue(int i)
            {
/* 292*/        return attributeList.getValueByFingerprint(i);
            }

            public void setAttribute(String s, String s1)
                throws DOMException
            {
/* 305*/        disallowUpdate();
            }

            public void copy(Outputter outputter, boolean flag)
                throws TransformerException
            {
/* 315*/        int i = getNameCode();
/* 316*/        outputter.writeStartTag(i);
/* 320*/        outputNamespaceNodes(outputter, flag);
/* 324*/        for(int j = 0; j < attributeList.getLength(); j++)
/* 325*/            outputter.writeAttribute(attributeList.getNameCode(j), attributeList.getValue(j));

/* 331*/        for(NodeImpl nodeimpl = (NodeImpl)getFirstChild(); nodeimpl != null; nodeimpl = (NodeImpl)nodeimpl.getNextSibling())
/* 333*/            if(nodeimpl instanceof ElementImpl)
/* 334*/                ((ElementImpl)nodeimpl).copy(outputter, false);
/* 336*/            else
/* 336*/                nodeimpl.copy(outputter);

/* 341*/        outputter.writeEndTag(i);
            }

            public String getAttribute(String s)
            {
/* 360*/        int i = attributeList.getIndex(s);
/* 361*/        if(i < 0)
/* 361*/            return "";
/* 362*/        else
/* 362*/            return attributeList.getValue(i);
            }

            public NamedNodeMap getAttributes()
            {
/* 371*/        return this;
            }

            public void removeAttribute(String s)
            {
/* 380*/        setAttribute(s, null);
            }

            public Attr getAttributeNode(String s)
            {
/* 396*/        int i = getAttributeList().getIndex(s);
/* 397*/        if(i < 0)
/* 398*/            return null;
/* 400*/        else
/* 400*/            return new AttributeImpl(this, i);
            }

            public Attr setAttributeNode(Attr attr)
                throws DOMException
            {
/* 410*/        disallowUpdate();
/* 411*/        return null;
            }

            public Attr removeAttributeNode(Attr attr)
                throws DOMException
            {
/* 421*/        disallowUpdate();
/* 422*/        return null;
            }

            public String getAttributeNS(String s, String s1)
            {
/* 436*/        String s2 = getAttributeValue(s, s1);
/* 437*/        return s2 != null ? s2 : "";
            }

            public void setAttributeNS(String s, String s1, String s2)
                throws DOMException
            {
/* 455*/        disallowUpdate();
            }

            public void removeAttributeNS(String s, String s1)
                throws DOMException
            {
/* 468*/        disallowUpdate();
            }

            public Attr getAttributeNodeNS(String s, String s1)
            {
/* 483*/        int i = attributeList.getIndex(s, s1);
/* 484*/        if(i < 0)
/* 484*/            return null;
/* 485*/        else
/* 485*/            return new AttributeImpl(this, i);
            }

            public Attr setAttributeNodeNS(Attr attr)
                throws DOMException
            {
/* 502*/        disallowUpdate();
/* 503*/        return null;
            }

            public boolean hasAttribute(String s)
            {
/* 518*/        return attributeList.getIndex(s) >= 0;
            }

            public boolean hasAttributeNS(String s, String s1)
            {
/* 535*/        return getAttributeValue(s, s1) != null;
            }

            public Node getNamedItem(String s)
            {
/* 548*/        return getAttributeNode(s);
            }

            public Node setNamedItem(Node node)
                throws DOMException
            {
/* 556*/        disallowUpdate();
/* 557*/        return null;
            }

            public Node removeNamedItem(String s)
                throws DOMException
            {
/* 565*/        disallowUpdate();
/* 566*/        return null;
            }

            public Node item(int i)
            {
/* 575*/        if(i < 0 || i >= attributeList.getLength())
/* 576*/            return null;
/* 578*/        else
/* 578*/            return new AttributeImpl(this, i);
            }

            public int getLength()
            {
/* 587*/        return attributeList.getLength();
            }

            public Node getNamedItemNS(String s, String s1)
            {
/* 596*/        return getAttributeNodeNS(s, s1);
            }

            public Node setNamedItemNS(Node node)
                throws DOMException
            {
/* 604*/        disallowUpdate();
/* 605*/        return null;
            }

            public Node removeNamedItemNS(String s, String s1)
                throws DOMException
            {
/* 613*/        disallowUpdate();
/* 614*/        return null;
            }
}
