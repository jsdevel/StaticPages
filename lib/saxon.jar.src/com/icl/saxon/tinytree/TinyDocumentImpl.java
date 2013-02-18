// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TinyDocumentImpl.java

package com.icl.saxon.tinytree;

import com.icl.saxon.KeyManager;
import com.icl.saxon.expr.NodeSetExtent;
import com.icl.saxon.om.*;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.pattern.AnyNodeTest;
import com.icl.saxon.sort.LocalOrderComparer;
import com.icl.saxon.tree.LineNumberMap;
import com.icl.saxon.tree.SystemIdMap;
import java.io.PrintStream;
import java.util.Hashtable;
import javax.xml.transform.TransformerException;
import org.w3c.dom.*;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyParentNodeImpl, TinyElementImpl, TinyTextImpl, TinyCommentImpl, 
//            TinyProcInstImpl, TinyAttributeImpl, TinyNamespaceImpl, TinyNodeImpl

public final class TinyDocumentImpl extends TinyParentNodeImpl
    implements DocumentInfo, Document
{

            private Hashtable idTable;
            private NamePool namePool;
            private Hashtable elementList;
            private boolean usesNamespaces;
            private Hashtable entityTable;
            protected char charBuffer[];
            protected int charBufferLength;
            protected StringBuffer commentBuffer;
            protected int numberOfNodes;
            protected int lastLevelOneNode;
            protected byte nodeType[];
            protected short depth[];
            protected int next[];
            protected int offset[];
            protected int length[];
            protected int nameCode[];
            protected int prior[];
            protected int numberOfAttributes;
            protected int attParent[];
            protected int attCode[];
            protected String attValue[];
            protected int numberOfNamespaces;
            protected int namespaceParent[];
            protected int namespaceCode[];
            private LineNumberMap lineNumberMap;
            private SystemIdMap systemIdMap;
            private Object index[];
            private int indexEntriesUsed;

            public TinyDocumentImpl()
            {
/*  29*/        idTable = null;
/*  31*/        elementList = null;
/*  32*/        usesNamespaces = false;
/*  33*/        entityTable = null;
/*  37*/        charBuffer = new char[4000];
/*  38*/        charBufferLength = 0;
/*  39*/        commentBuffer = new StringBuffer(500);
/*  41*/        numberOfNodes = 0;
/*  42*/        lastLevelOneNode = -1;
/*  44*/        nodeType = new byte[4000];
/*  45*/        depth = new short[4000];
/*  46*/        next = new int[4000];
/*  47*/        offset = new int[4000];
/*  48*/        length = new int[4000];
/*  49*/        nameCode = new int[4000];
/*  51*/        prior = null;
/*  53*/        numberOfAttributes = 0;
/*  54*/        attParent = new int[100];
/*  55*/        attCode = new int[100];
/*  56*/        attValue = new String[100];
/*  58*/        numberOfNamespaces = 0;
/*  59*/        namespaceParent = new int[20];
/*  60*/        namespaceCode = new int[20];
/*  63*/        systemIdMap = new SystemIdMap();
/*  68*/        index = new Object[30];
/*  69*/        indexEntriesUsed = 0;
/*  73*/        super.nodeNr = 0;
/*  74*/        super.document = this;
            }

            public void setNamePool(NamePool namepool)
            {
/*  82*/        namePool = namepool;
/*  83*/        addNamespace(0, namepool.getNamespaceCode("xml", "http://www.w3.org/XML/1998/namespace"));
            }

            public NamePool getNamePool()
            {
/*  91*/        return namePool;
            }

            protected void ensureNodeCapacity()
            {
/*  95*/        if(nodeType.length < numberOfNodes + 1)
                {
/*  96*/            int i = numberOfNodes * 2;
/*  98*/            byte abyte0[] = new byte[i];
/*  99*/            int ai[] = new int[i];
/* 100*/            short aword0[] = new short[i];
/* 101*/            int ai1[] = new int[i];
/* 102*/            int ai2[] = new int[i];
/* 103*/            int ai3[] = new int[i];
/* 105*/            System.arraycopy(nodeType, 0, abyte0, 0, numberOfNodes);
/* 106*/            System.arraycopy(next, 0, ai, 0, numberOfNodes);
/* 107*/            System.arraycopy(depth, 0, aword0, 0, numberOfNodes);
/* 108*/            System.arraycopy(offset, 0, ai1, 0, numberOfNodes);
/* 109*/            System.arraycopy(length, 0, ai2, 0, numberOfNodes);
/* 110*/            System.arraycopy(nameCode, 0, ai3, 0, numberOfNodes);
/* 112*/            nodeType = abyte0;
/* 113*/            next = ai;
/* 114*/            depth = aword0;
/* 115*/            offset = ai1;
/* 116*/            length = ai2;
/* 117*/            nameCode = ai3;
                }
            }

            protected void ensureAttributeCapacity()
            {
/* 122*/        if(attParent.length < numberOfAttributes + 1)
                {
/* 123*/            int i = numberOfAttributes * 2;
/* 125*/            int ai[] = new int[i];
/* 126*/            int ai1[] = new int[i];
/* 128*/            String as[] = new String[i];
/* 131*/            System.arraycopy(attParent, 0, ai, 0, numberOfAttributes);
/* 132*/            System.arraycopy(attCode, 0, ai1, 0, numberOfAttributes);
/* 134*/            System.arraycopy(attValue, 0, as, 0, numberOfAttributes);
/* 136*/            attParent = ai;
/* 137*/            attCode = ai1;
/* 139*/            attValue = as;
                }
            }

            protected void ensureNamespaceCapacity()
            {
/* 144*/        if(namespaceParent.length < numberOfNamespaces + 1)
                {
/* 145*/            int i = numberOfNamespaces * 2;
/* 147*/            int ai[] = new int[i];
/* 148*/            int ai1[] = new int[i];
/* 150*/            System.arraycopy(namespaceParent, 0, ai, 0, numberOfNamespaces);
/* 151*/            System.arraycopy(namespaceCode, 0, ai1, 0, numberOfNamespaces);
/* 153*/            namespaceParent = ai;
/* 154*/            namespaceCode = ai1;
                }
            }

            protected void addNode(short word0, int i, int j, int k, int l)
            {
/* 159*/        ensureNodeCapacity();
/* 160*/        nodeType[numberOfNodes] = (byte)word0;
/* 161*/        depth[numberOfNodes] = (short)i;
/* 162*/        offset[numberOfNodes] = j;
/* 163*/        length[numberOfNodes] = k;
/* 164*/        nameCode[numberOfNodes] = l;
/* 165*/        next[numberOfNodes] = -1;
/* 167*/        if(i == 1)
/* 167*/            lastLevelOneNode = numberOfNodes;
/* 169*/        numberOfNodes++;
            }

            protected void appendChars(char ac[], int i, int j)
            {
                char ac1[];
/* 174*/        for(; charBuffer.length < charBufferLength + j; charBuffer = ac1)
                {
/* 174*/            ac1 = new char[charBuffer.length * 2];
/* 175*/            System.arraycopy(charBuffer, 0, ac1, 0, charBufferLength);
                }

/* 178*/        System.arraycopy(ac, i, charBuffer, charBufferLength, j);
/* 179*/        charBufferLength += j;
            }

            protected void truncate(int i)
            {
/* 189*/        if(i == numberOfNodes)
/* 189*/            return;
/* 192*/        for(int j = i; j < numberOfNodes; j++)
                {
/* 193*/            if(nodeType[j] != 3)
/* 194*/                continue;
/* 194*/            charBufferLength = offset[j];
/* 195*/            break;
                }

/* 200*/        for(int k = i; k < numberOfNodes; k++)
                {
/* 201*/            if(nodeType[k] != 1 || offset[k] < 0)
/* 202*/                continue;
/* 202*/            numberOfAttributes = offset[k];
/* 203*/            break;
                }

/* 208*/        for(int l = i; l < numberOfNodes; l++)
                {
/* 209*/            if(nodeType[l] != 1 || length[l] < 0)
/* 210*/                continue;
/* 210*/            numberOfNamespaces = length[l];
/* 211*/            break;
                }

/* 218*/        numberOfNodes = i;
/* 221*/        prior = null;
/* 226*/        nodeType[i] = 9;
/* 227*/        depth[i] = 0;
            }

            protected void ensurePriorIndex()
            {
/* 236*/        if(prior == null)
/* 237*/            makePriorIndex();
            }

            private synchronized void makePriorIndex()
            {
/* 242*/        prior = new int[numberOfNodes];
/* 243*/        for(int i = 0; i < numberOfNodes; i++)
/* 244*/            prior[i] = -1;

/* 246*/        for(int j = 0; j < numberOfNodes; j++)
                {
/* 247*/            int k = next[j];
/* 248*/            if(k != -1)
/* 249*/                prior[k] = j;
                }

            }

            protected void addAttribute(int i, int j, String s, String s1)
            {
/* 256*/        ensureAttributeCapacity();
/* 257*/        attParent[numberOfAttributes] = i;
/* 258*/        attCode[numberOfAttributes] = j;
/* 259*/        attValue[numberOfAttributes] = s1;
/* 260*/        numberOfAttributes++;
/* 262*/        if(s.equals("ID"))
                {
/* 263*/            if(idTable == null)
/* 264*/                idTable = new Hashtable();
/* 266*/            TinyNodeImpl tinynodeimpl = getNode(i);
/* 267*/            registerID(tinynodeimpl, s1);
                }
            }

            protected void addNamespace(int i, int j)
            {
/* 272*/        usesNamespaces = true;
/* 273*/        ensureNamespaceCapacity();
/* 274*/        namespaceParent[numberOfNamespaces] = i;
/* 275*/        namespaceCode[numberOfNamespaces] = j;
/* 276*/        numberOfNamespaces++;
            }

            public TinyNodeImpl getNode(int i)
            {
/* 280*/        switch((short)nodeType[i])
                {
/* 282*/        case 9: // '\t'
/* 282*/            return this;

/* 284*/        case 1: // '\001'
/* 284*/            return new TinyElementImpl(this, i);

/* 286*/        case 3: // '\003'
/* 286*/            return new TinyTextImpl(this, i);

/* 288*/        case 8: // '\b'
/* 288*/            return new TinyCommentImpl(this, i);

/* 290*/        case 7: // '\007'
/* 290*/            return new TinyProcInstImpl(this, i);

/* 292*/        case 2: // '\002'
/* 292*/        case 4: // '\004'
/* 292*/        case 5: // '\005'
/* 292*/        case 6: // '\006'
/* 292*/        default:
/* 292*/            return null;
                }
            }

            public long getSequenceNumber()
            {
/* 301*/        return 0L;
            }

            protected TinyAttributeImpl getAttributeNode(int i)
            {
/* 309*/        return new TinyAttributeImpl(this, i);
            }

            protected boolean isUsingNamespaces()
            {
/* 317*/        return usesNamespaces;
            }

            protected TinyNamespaceImpl getNamespaceNode(int i)
            {
/* 325*/        return new TinyNamespaceImpl(this, i);
            }

            public void setSystemId(String s)
            {
/* 336*/        if(s == null)
/* 337*/            s = "";
/* 339*/        systemIdMap.setSystemId(super.nodeNr, s);
            }

            public String getSystemId()
            {
/* 347*/        return systemIdMap.getSystemId(super.nodeNr);
            }

            public String getBaseURI()
            {
/* 356*/        return getSystemId();
            }

            protected void setSystemId(int i, String s)
            {
/* 367*/        if(s == null)
/* 368*/            s = "";
/* 370*/        systemIdMap.setSystemId(i, s);
            }

            protected String getSystemId(int i)
            {
/* 379*/        return systemIdMap.getSystemId(i);
            }

            public void setLineNumbering()
            {
/* 388*/        lineNumberMap = new LineNumberMap();
/* 389*/        lineNumberMap.setLineNumber(0, 0);
            }

            protected void setLineNumber(int i, int j)
            {
/* 397*/        if(lineNumberMap != null)
/* 398*/            lineNumberMap.setLineNumber(i, j);
            }

            protected int getLineNumber(int i)
            {
/* 407*/        if(lineNumberMap != null)
/* 408*/            return lineNumberMap.getLineNumber(i);
/* 410*/        else
/* 410*/            return -1;
            }

            public int getLineNumber()
            {
/* 419*/        return 0;
            }

            public final short getNodeType()
            {
/* 428*/        return 9;
            }

            public NodeInfo getParent()
            {
/* 437*/        return null;
            }

            public DocumentInfo getDocumentRoot()
            {
/* 446*/        return this;
            }

            public String generateId()
            {
/* 455*/        return "";
            }

            protected AxisEnumeration getAllElements(int i)
            {
/* 473*/        Integer integer = new Integer(i);
/* 474*/        if(elementList == null)
/* 475*/            elementList = new Hashtable();
/* 477*/        NodeSetExtent nodesetextent = (NodeSetExtent)elementList.get(integer);
/* 478*/        if(nodesetextent == null)
                {
/* 479*/            nodesetextent = new NodeSetExtent(LocalOrderComparer.getInstance());
/* 480*/            nodesetextent.setSorted(true);
/* 481*/            for(int j = 1; j < numberOfNodes; j++)
/* 482*/                if(nodeType[j] == 1 && (nameCode[j] & 0xfffff) == i)
/* 484*/                    nodesetextent.append(getNode(j));

/* 487*/            elementList.put(integer, nodesetextent);
                }
/* 489*/        return (AxisEnumeration)nodesetextent.enumerate();
            }

            private void registerID(NodeInfo nodeinfo, String s)
            {
/* 500*/        NodeInfo nodeinfo1 = (NodeInfo)idTable.get(s);
/* 501*/        if(nodeinfo1 == null)
/* 502*/            idTable.put(s, nodeinfo);
            }

            public NodeInfo selectID(String s)
            {
/* 515*/        if(idTable == null)
/* 515*/            return null;
/* 516*/        else
/* 516*/            return (NodeInfo)idTable.get(s);
            }

            public synchronized Hashtable getKeyIndex(KeyManager keymanager, int i)
            {
/* 529*/        for(int j = 0; j < indexEntriesUsed; j += 3)
/* 530*/            if((KeyManager)index[j] == keymanager && ((Integer)index[j + 1]).intValue() == i)
                    {
/* 532*/                Object obj = index[j + 2];
/* 533*/                return (Hashtable)index[j + 2];
                    }

/* 536*/        return null;
            }

            public synchronized void setKeyIndex(KeyManager keymanager, int i, Hashtable hashtable)
            {
/* 550*/        for(int j = 0; j < indexEntriesUsed; j += 3)
/* 551*/            if((KeyManager)index[j] == keymanager && ((Integer)index[j + 1]).intValue() == i)
                    {
/* 553*/                index[j + 2] = hashtable;
/* 554*/                return;
                    }

/* 558*/        if(indexEntriesUsed + 3 >= index.length)
                {
/* 559*/            Object aobj[] = new Object[indexEntriesUsed * 2];
/* 560*/            System.arraycopy(((Object) (index)), 0, ((Object) (aobj)), 0, indexEntriesUsed);
/* 561*/            index = aobj;
                }
/* 563*/        index[indexEntriesUsed++] = keymanager;
/* 564*/        index[indexEntriesUsed++] = new Integer(i);
/* 565*/        index[indexEntriesUsed++] = hashtable;
            }

            protected void setUnparsedEntity(String s, String s1)
            {
/* 574*/        if(entityTable == null)
/* 575*/            entityTable = new Hashtable();
/* 577*/        entityTable.put(s, s1);
            }

            public String getUnparsedEntity(String s)
            {
/* 587*/        if(entityTable == null)
                {
/* 588*/            return "";
                } else
                {
/* 590*/            String s1 = (String)entityTable.get(s);
/* 591*/            return s1 != null ? s1 : "";
                }
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/* 606*/        for(AxisEnumeration axisenumeration = getEnumeration((byte)3, AnyNodeTest.getInstance()); axisenumeration.hasMoreElements(); axisenumeration.nextElement().copy(outputter));
            }

            public void diagnosticDump()
            {
/* 619*/        System.err.println("Node\ttype\tdepth\toffset\tlength");
/* 620*/        for(int i = 0; i < numberOfNodes; i++)
/* 621*/            System.err.println(i + "\t" + nodeType[i] + "\t" + depth[i] + "\t" + offset[i] + "\t" + length[i] + "\t" + Navigator.getPath(getNode(i)));

            }

            public Node adoptNode(Node node)
                throws DOMException
            {
/* 639*/        disallowUpdate();
/* 640*/        return null;
            }

            public String getDocumentURI()
            {
/* 658*/        return getSystemId();
            }

            public DOMConfiguration getDomConfig()
            {
/* 668*/        return null;
            }

            public String getInputEncoding()
            {
/* 679*/        return null;
            }

            public boolean getStrictErrorChecking()
            {
/* 694*/        return true;
            }

            public String getXmlEncoding()
            {
/* 705*/        return null;
            }

            public boolean getXmlStandalone()
            {
/* 720*/        return false;
            }

            public String getXmlVersion()
            {
/* 731*/        return "1.0";
            }

            public void normalizeDocument()
            {
/* 740*/        disallowUpdate();
            }

            public Node renameNode(Node node, String s, String s1)
                throws DOMException
            {
/* 774*/        disallowUpdate();
/* 775*/        return null;
            }

            public void setDocumentURI(String s)
            {
/* 793*/        setSystemId(s);
            }

            public void setStrictErrorChecking(boolean flag)
            {
/* 808*/        throw new UnsupportedOperationException("setStrictErrorChecking() is not supported");
            }

            public void setXmlStandalone(boolean flag)
                throws DOMException
            {
/* 825*/        disallowUpdate();
            }

            public void setXmlVersion(String s)
                throws DOMException
            {
/* 857*/        disallowUpdate();
            }
}
