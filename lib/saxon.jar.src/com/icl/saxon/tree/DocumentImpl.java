// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DocumentImpl.java

package com.icl.saxon.tree;

import com.icl.saxon.KeyManager;
import com.icl.saxon.expr.NodeSetExtent;
import com.icl.saxon.om.*;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.sort.LocalOrderComparer;
import java.util.Hashtable;
import javax.xml.transform.TransformerException;
import org.w3c.dom.*;
import org.xml.sax.Attributes;

// Referenced classes of package com.icl.saxon.tree:
//            ParentNodeImpl, SystemIdMap, LineNumberMap, ElementImpl, 
//            NodeImpl, NodeFactory

public final class DocumentImpl extends ParentNodeImpl
    implements DocumentInfo, Document
{

            private ElementImpl documentElement;
            private Hashtable idTable;
            private Hashtable entityTable;
            private Hashtable elementList;
            private StringBuffer characterBuffer;
            private NamePool namePool;
            private NodeFactory nodeFactory;
            private LineNumberMap lineNumberMap;
            private SystemIdMap systemIdMap;
            private Object index[];
            private int indexEntriesUsed;

            public DocumentImpl()
            {
/*  29*/        idTable = null;
/*  31*/        entityTable = null;
/*  32*/        elementList = null;
/*  37*/        systemIdMap = new SystemIdMap();
/*  42*/        index = new Object[30];
/*  43*/        indexEntriesUsed = 0;
/*  46*/        super.parent = null;
            }

            protected void setCharacterBuffer(StringBuffer stringbuffer)
            {
/*  54*/        characterBuffer = stringbuffer;
            }

            public final StringBuffer getCharacterBuffer()
            {
/*  62*/        return characterBuffer;
            }

            public void setNamePool(NamePool namepool)
            {
/*  70*/        namePool = namepool;
            }

            public NamePool getNamePool()
            {
/*  78*/        return namePool;
            }

            public void setNodeFactory(NodeFactory nodefactory)
            {
/*  86*/        nodeFactory = nodefactory;
            }

            public NodeFactory getNodeFactory()
            {
/*  94*/        return nodeFactory;
            }

            protected void setDocumentElement(ElementImpl elementimpl)
            {
/* 105*/        documentElement = elementimpl;
            }

            public void setSystemId(String s)
            {
/* 116*/        if(s == null)
/* 117*/            s = "";
/* 119*/        systemIdMap.setSystemId(super.sequence, s);
            }

            public String getSystemId()
            {
/* 127*/        return systemIdMap.getSystemId(super.sequence);
            }

            public String getBaseURI()
            {
/* 136*/        return getSystemId();
            }

            protected void setSystemId(int i, String s)
            {
/* 144*/        if(s == null)
/* 145*/            s = "";
/* 149*/        systemIdMap.setSystemId(i, s);
            }

            protected String getSystemId(int i)
            {
/* 158*/        return systemIdMap.getSystemId(i);
            }

            public void setLineNumbering()
            {
/* 167*/        lineNumberMap = new LineNumberMap();
/* 168*/        lineNumberMap.setLineNumber(super.sequence, 0);
            }

            protected void setLineNumber(int i, int j)
            {
/* 176*/        if(lineNumberMap != null)
/* 177*/            lineNumberMap.setLineNumber(i, j);
            }

            protected int getLineNumber(int i)
            {
/* 186*/        if(lineNumberMap != null)
/* 187*/            return lineNumberMap.getLineNumber(i);
/* 189*/        else
/* 189*/            return -1;
            }

            public int getLineNumber()
            {
/* 198*/        return 0;
            }

            public final short getNodeType()
            {
/* 207*/        return 9;
            }

            public final Node getNextSibling()
            {
/* 216*/        return null;
            }

            public final Node getPreviousSibling()
            {
/* 225*/        return null;
            }

            public Element getDocumentElement()
            {
/* 234*/        return documentElement;
            }

            public DocumentInfo getDocumentRoot()
            {
/* 243*/        return this;
            }

            public String generateId()
            {
/* 252*/        return "";
            }

            protected AxisEnumeration getAllElements(int i)
            {
/* 260*/        Integer integer = new Integer(i);
/* 261*/        if(elementList == null)
/* 262*/            elementList = new Hashtable();
/* 264*/        NodeSetExtent nodesetextent = (NodeSetExtent)elementList.get(integer);
/* 265*/        if(nodesetextent == null)
                {
/* 266*/            nodesetextent = new NodeSetExtent(LocalOrderComparer.getInstance());
/* 267*/            nodesetextent.setSorted(true);
/* 268*/            for(NodeImpl nodeimpl = getNextInDocument(this); nodeimpl != null; nodeimpl = nodeimpl.getNextInDocument(this))
/* 270*/                if(nodeimpl.getNodeType() == 1 && nodeimpl.getFingerprint() == i)
/* 272*/                    nodesetextent.append(nodeimpl);

/* 276*/            elementList.put(integer, nodesetextent);
                }
/* 278*/        return (AxisEnumeration)nodesetextent.enumerate();
            }

            private void indexIDs()
            {
/* 287*/        if(idTable != null)
/* 287*/            return;
/* 288*/        idTable = new Hashtable();
/* 290*/        Object obj = this;
/* 291*/        Object obj1 = obj;
/* 293*/        for(; obj != null; obj = ((NodeImpl) (obj)).getNextInDocument(((NodeImpl) (obj1))))
/* 293*/            if(((AbstractNode) (obj)).getNodeType() == 1)
                    {
/* 294*/                ElementImpl elementimpl = (ElementImpl)obj;
/* 295*/                AttributeCollection attributecollection = elementimpl.getAttributeList();
/* 296*/                for(int i = 0; i < attributecollection.getLength(); i++)
/* 297*/                    if("ID".equals(attributecollection.getType(i)))
/* 298*/                        registerID(elementimpl, attributecollection.getValue(i));

                    }

            }

            private void registerID(NodeInfo nodeinfo, String s)
            {
/* 314*/        Object obj = idTable.get(s);
/* 315*/        if(obj == null)
/* 316*/            idTable.put(s, nodeinfo);
            }

            public NodeInfo selectID(String s)
            {
/* 328*/        if(idTable == null)
/* 328*/            indexIDs();
/* 329*/        return (NodeInfo)idTable.get(s);
            }

            public synchronized Hashtable getKeyIndex(KeyManager keymanager, int i)
            {
/* 342*/        for(int j = 0; j < indexEntriesUsed; j += 3)
/* 343*/            if((KeyManager)index[j] == keymanager && ((Integer)index[j + 1]).intValue() == i)
                    {
/* 345*/                Object obj = index[j + 2];
/* 346*/                return (Hashtable)index[j + 2];
                    }

/* 351*/        return null;
            }

            public synchronized void setKeyIndex(KeyManager keymanager, int i, Hashtable hashtable)
            {
/* 365*/        for(int j = 0; j < indexEntriesUsed; j += 3)
/* 366*/            if((KeyManager)index[j] == keymanager && ((Integer)index[j + 1]).intValue() == i)
                    {
/* 368*/                index[j + 2] = hashtable;
/* 369*/                return;
                    }

/* 373*/        if(indexEntriesUsed + 3 >= index.length)
                {
/* 374*/            Object aobj[] = new Object[indexEntriesUsed * 2];
/* 375*/            System.arraycopy(((Object) (index)), 0, ((Object) (aobj)), 0, indexEntriesUsed);
/* 376*/            index = aobj;
                }
/* 378*/        index[indexEntriesUsed++] = keymanager;
/* 379*/        index[indexEntriesUsed++] = new Integer(i);
/* 380*/        index[indexEntriesUsed++] = hashtable;
            }

            protected void setUnparsedEntity(String s, String s1)
            {
/* 389*/        if(entityTable == null)
/* 390*/            entityTable = new Hashtable();
/* 392*/        entityTable.put(s, s1);
            }

            public String getUnparsedEntity(String s)
            {
/* 402*/        if(entityTable == null)
                {
/* 403*/            return "";
                } else
                {
/* 405*/            String s1 = (String)entityTable.get(s);
/* 406*/            return s1 != null ? s1 : "";
                }
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/* 414*/        for(NodeImpl nodeimpl = (NodeImpl)getFirstChild(); nodeimpl != null; nodeimpl = (NodeImpl)nodeimpl.getNextSibling())
/* 416*/            nodeimpl.copy(outputter);

            }

            public Node adoptNode(Node node)
                throws DOMException
            {
/* 434*/        disallowUpdate();
/* 435*/        return null;
            }

            public String getDocumentURI()
            {
/* 453*/        return getSystemId();
            }

            public DOMConfiguration getDomConfig()
            {
/* 463*/        return null;
            }

            public String getInputEncoding()
            {
/* 474*/        return null;
            }

            public boolean getStrictErrorChecking()
            {
/* 489*/        return true;
            }

            public String getXmlEncoding()
            {
/* 500*/        return null;
            }

            public boolean getXmlStandalone()
            {
/* 515*/        return false;
            }

            public String getXmlVersion()
            {
/* 526*/        return "1.0";
            }

            public void normalizeDocument()
            {
/* 535*/        disallowUpdate();
            }

            public Node renameNode(Node node, String s, String s1)
                throws DOMException
            {
/* 569*/        disallowUpdate();
/* 570*/        return null;
            }

            public void setDocumentURI(String s)
            {
/* 588*/        setSystemId(s);
            }

            public void setStrictErrorChecking(boolean flag)
            {
/* 603*/        throw new UnsupportedOperationException("setStrictErrorChecking() is not supported");
            }

            public void setXmlStandalone(boolean flag)
                throws DOMException
            {
/* 620*/        disallowUpdate();
            }

            public void setXmlVersion(String s)
                throws DOMException
            {
/* 652*/        disallowUpdate();
            }
}
