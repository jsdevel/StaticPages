// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NodeImpl.java

package com.icl.saxon.tree;

import com.icl.saxon.expr.NodeSetExtent;
import com.icl.saxon.expr.XPathException;
import com.icl.saxon.om.*;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.pattern.*;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import org.w3c.dom.*;

// Referenced classes of package com.icl.saxon.tree:
//            ParentNodeImpl, AncestorEnumeration, AttributeEnumeration, DocumentImpl, 
//            DescendantEnumeration, FollowingEnumeration, FollowingSiblingEnumeration, NamespaceEnumeration, 
//            PrecedingEnumeration, PrecedingSiblingEnumeration, PrecedingOrAncestorEnumeration

public abstract class NodeImpl extends AbstractNode
{

            protected static NodeInfo emptyArray[] = new NodeInfo[0];
            protected ParentNodeImpl parent;
            protected int index;

            public NodeImpl()
            {
            }

            public void setSystemId(String s)
            {
/*  44*/        getParent().setSystemId(s);
            }

            public boolean isSameNodeInfo(NodeInfo nodeinfo)
            {
/*  55*/        return this == nodeinfo;
            }

            public int getNameCode()
            {
/*  64*/        return -1;
            }

            public int getFingerprint()
            {
/*  73*/        int i = getNameCode();
/*  74*/        if(i == -1)
/*  74*/            return -1;
/*  75*/        else
/*  75*/            return i & 0xfffff;
            }

            public String generateId()
            {
/*  85*/        return "" + AbstractNode.NODE_LETTER[getNodeType()] + getSequenceNumber();
            }

            public Node getOriginatingNode()
            {
/*  94*/        return this;
            }

            public String getSystemId()
            {
/* 102*/        return parent.getSystemId();
            }

            public String getBaseURI()
            {
/* 110*/        return parent.getBaseURI();
            }

            protected long getSequenceNumber()
            {
/* 122*/        NodeImpl nodeimpl = this;
/* 123*/        int i = 0;
/* 124*/        do
                {
/* 124*/            if(nodeimpl instanceof ParentNodeImpl)
/* 125*/                return nodeimpl.getSequenceNumber() + 0x10000L + (long)i;
/* 128*/            nodeimpl = nodeimpl.getPreviousInDocument();
/* 123*/            i++;
                } while(true);
            }

            public final int compareOrder(NodeInfo nodeinfo)
            {
/* 143*/        long l = getSequenceNumber();
/* 144*/        long l1 = ((NodeImpl)nodeinfo).getSequenceNumber();
/* 145*/        if(l < l1)
/* 145*/            return -1;
/* 146*/        return l <= l1 ? 0 : 1;
            }

            public NamePool getNamePool()
            {
/* 155*/        return getDocumentRoot().getNamePool();
            }

            public String getPrefix()
            {
/* 164*/        int i = getNameCode();
/* 165*/        if(i == -1)
/* 165*/            return "";
/* 166*/        if((i >> 20 & 0xff) == 0)
/* 166*/            return "";
/* 167*/        else
/* 167*/            return getNamePool().getPrefix(i);
            }

            public String getURI()
            {
/* 178*/        int i = getNameCode();
/* 179*/        if(i == -1)
/* 179*/            return null;
/* 180*/        else
/* 180*/            return getNamePool().getURI(i);
            }

            public String getDisplayName()
            {
/* 204*/        int i = getNameCode();
/* 205*/        if(i == -1)
/* 205*/            return "";
/* 206*/        else
/* 206*/            return getNamePool().getDisplayName(i);
            }

            public String getLocalName()
            {
/* 216*/        int i = getNameCode();
/* 217*/        if(i == -1)
/* 217*/            return "";
/* 218*/        else
/* 218*/            return getNamePool().getLocalName(i);
            }

            public int getLineNumber()
            {
/* 226*/        return parent.getLineNumber();
            }

            public final NodeInfo getParent()
            {
/* 254*/        return parent;
            }

            public Node getPreviousSibling()
            {
/* 273*/        return parent.getNthChild(index - 1);
            }

            public Node getNextSibling()
            {
/* 284*/        return parent.getNthChild(index + 1);
            }

            public Node getFirstChild()
            {
/* 293*/        return null;
            }

            public Node getLastChild()
            {
/* 302*/        return null;
            }

            public AxisEnumeration getEnumeration(byte byte0, NodeTest nodetest)
            {
/* 318*/        switch(byte0)
                {
/* 320*/        case 0: // '\0'
/* 320*/            return new AncestorEnumeration(this, nodetest, false);

/* 323*/        case 1: // '\001'
/* 323*/            return new AncestorEnumeration(this, nodetest, true);

/* 326*/        case 2: // '\002'
/* 326*/            if(getNodeType() != 1)
/* 326*/                return EmptyEnumeration.getInstance();
/* 327*/            else
/* 327*/                return new AttributeEnumeration(this, nodetest);

/* 330*/        case 3: // '\003'
/* 330*/            if(this instanceof ParentNodeImpl)
/* 331*/                return ((ParentNodeImpl)this).enumerateChildren(nodetest);
/* 333*/            else
/* 333*/                return EmptyEnumeration.getInstance();

/* 337*/        case 4: // '\004'
/* 337*/            if(getNodeType() == 9 && (nodetest instanceof NameTest) && nodetest.getNodeType() == 1)
/* 340*/                return ((DocumentImpl)this).getAllElements(((NameTest)nodetest).getFingerprint());
/* 342*/            if(hasChildNodes())
/* 343*/                return new DescendantEnumeration(this, nodetest, false);
/* 345*/            else
/* 345*/                return EmptyEnumeration.getInstance();

/* 349*/        case 5: // '\005'
/* 349*/            return new DescendantEnumeration(this, nodetest, true);

/* 352*/        case 6: // '\006'
/* 352*/            return new FollowingEnumeration(this, nodetest);

/* 355*/        case 7: // '\007'
/* 355*/            return new FollowingSiblingEnumeration(this, nodetest);

/* 358*/        case 8: // '\b'
/* 358*/            if(getNodeType() != 1)
/* 358*/                return EmptyEnumeration.getInstance();
/* 359*/            else
/* 359*/                return new NamespaceEnumeration(this, nodetest);

/* 362*/        case 9: // '\t'
/* 362*/            NodeInfo nodeinfo = (NodeInfo)getParentNode();
/* 363*/            if(nodeinfo == null)
/* 363*/                return EmptyEnumeration.getInstance();
/* 364*/            if(nodetest.matches(nodeinfo))
/* 364*/                return new SingletonEnumeration(nodeinfo);
/* 365*/            else
/* 365*/                return EmptyEnumeration.getInstance();

/* 368*/        case 10: // '\n'
/* 368*/            return new PrecedingEnumeration(this, nodetest);

/* 371*/        case 11: // '\013'
/* 371*/            return new PrecedingSiblingEnumeration(this, nodetest);

/* 374*/        case 12: // '\f'
/* 374*/            if(nodetest.matches(this))
/* 374*/                return new SingletonEnumeration(this);
/* 375*/            else
/* 375*/                return EmptyEnumeration.getInstance();

/* 378*/        case 13: // '\r'
/* 378*/            return new PrecedingOrAncestorEnumeration(this, nodetest);
                }
/* 381*/        throw new IllegalArgumentException("Unknown axis number " + byte0);
            }

            public boolean hasAttributes()
            {
/* 393*/        return false;
            }

            public String getAttributeValue(String s, String s1)
            {
/* 406*/        return null;
            }

            public String getAttributeValue(int i)
            {
/* 429*/        return null;
            }

            public Element getDocumentElement()
            {
/* 440*/        return ((DocumentImpl)getDocumentRoot()).getDocumentElement();
            }

            public DocumentInfo getDocumentRoot()
            {
/* 450*/        return getParent().getDocumentRoot();
            }

            public NodeImpl getNextInDocument(NodeImpl nodeimpl)
            {
/* 465*/        NodeImpl nodeimpl1 = (NodeImpl)getFirstChild();
/* 466*/        if(nodeimpl1 != null)
/* 466*/            return nodeimpl1;
/* 467*/        if(this == nodeimpl)
/* 467*/            return null;
/* 468*/        nodeimpl1 = (NodeImpl)getNextSibling();
/* 469*/        if(nodeimpl1 != null)
/* 469*/            return nodeimpl1;
/* 470*/        NodeImpl nodeimpl2 = this;
/* 472*/        do
                {
/* 472*/            nodeimpl2 = (NodeImpl)nodeimpl2.getParent();
/* 473*/            if(nodeimpl2 == null)
/* 473*/                return null;
/* 474*/            if(nodeimpl2 == nodeimpl)
/* 474*/                return null;
/* 475*/            nodeimpl1 = (NodeImpl)nodeimpl2.getNextSibling();
                } while(nodeimpl1 == null);
/* 476*/        return nodeimpl1;
            }

            public NodeImpl getPreviousInDocument()
            {
/* 493*/        NodeImpl nodeimpl = (NodeImpl)getPreviousSibling();
/* 494*/        if(nodeimpl != null)
/* 495*/            return nodeimpl.getLastDescendantOrSelf();
/* 497*/        else
/* 497*/            return (NodeImpl)getParentNode();
            }

            private NodeImpl getLastDescendantOrSelf()
            {
/* 501*/        NodeImpl nodeimpl = (NodeImpl)getLastChild();
/* 502*/        if(nodeimpl == null)
/* 502*/            return this;
/* 503*/        else
/* 503*/            return nodeimpl.getLastDescendantOrSelf();
            }

            public void outputNamespaceNodes(Outputter outputter, boolean flag)
                throws TransformerException
            {
            }

            public void removeNode()
            {
/* 526*/        parent.removeChild(index);
            }

            public NodeList getChildNodes()
            {
/* 549*/        try
                {
/* 549*/            return new NodeSetExtent(EmptyEnumeration.getInstance(), null);
                }
/* 551*/        catch(XPathException xpathexception)
                {
/* 551*/            return null;
                }
            }

            public NamedNodeMap getAttributes()
            {
/* 562*/        return null;
            }

            public boolean hasChildNodes()
            {
/* 574*/        return getFirstChild() != null;
            }

}
