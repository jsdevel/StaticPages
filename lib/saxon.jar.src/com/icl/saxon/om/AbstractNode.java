// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   AbstractNode.java

package com.icl.saxon.om;

import com.icl.saxon.expr.NodeSetExtent;
import com.icl.saxon.expr.XPathException;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.pattern.*;
import com.icl.saxon.sort.LocalOrderComparer;
import com.icl.saxon.tree.DOMExceptionImpl;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMLocator;
import org.w3c.dom.*;

// Referenced classes of package com.icl.saxon.om:
//            DocumentInfo, NodeInfo, AxisEnumeration, NamePool

public abstract class AbstractNode
    implements Node, NodeInfo, SourceLocator, DOMLocator
{
    private class AttributeMap
        implements NamedNodeMap
    {

                public Node getNamedItem(String s)
                {
/*1954*/            for(AxisEnumeration axisenumeration = getEnumeration((byte)2, AnyNodeTest.getInstance()); axisenumeration.hasMoreElements();)
                    {
/*1957*/                NodeInfo nodeinfo = axisenumeration.nextElement();
/*1958*/                if(s.equals(nodeinfo.getDisplayName()))
/*1959*/                    return (Node)nodeinfo;
                    }

/*1962*/            return null;
                }

                public Node item(int i)
                {
/*1971*/            if(i < 0)
/*1972*/                return null;
/*1974*/            int j = 0;
/*1975*/            for(AxisEnumeration axisenumeration = getEnumeration((byte)2, AnyNodeTest.getInstance()); axisenumeration.hasMoreElements();)
                    {
/*1978*/                NodeInfo nodeinfo = axisenumeration.nextElement();
/*1979*/                if(j == i)
/*1980*/                    return (Node)nodeinfo;
/*1982*/                j++;
                    }

/*1984*/            return null;
                }

                public int getLength()
                {
/*1992*/            int i = 0;
/*1993*/            for(AxisEnumeration axisenumeration = getEnumeration((byte)2, AnyNodeTest.getInstance()); axisenumeration.hasMoreElements();)
                    {
/*1996*/                axisenumeration.nextElement();
/*1997*/                i++;
                    }

/*1999*/            return i;
                }

                public Node getNamedItemNS(String s, String s1)
                {
/*2007*/            if(s == null)
/*2007*/                s = "";
/*2008*/            for(AxisEnumeration axisenumeration = getEnumeration((byte)2, AnyNodeTest.getInstance()); axisenumeration.hasMoreElements();)
                    {
/*2011*/                NodeInfo nodeinfo = axisenumeration.nextElement();
/*2012*/                if(s.equals(nodeinfo.getURI()) && s1.equals(nodeinfo.getLocalName()))
/*2013*/                    return (Node)nodeinfo;
                    }

/*2016*/            return null;
                }

                public Node setNamedItem(Node node)
                    throws DOMException
                {
/*2024*/            disallowUpdate();
/*2025*/            return null;
                }

                public Node removeNamedItem(String s)
                    throws DOMException
                {
/*2033*/            disallowUpdate();
/*2034*/            return null;
                }

                public Node setNamedItemNS(Node node)
                    throws DOMException
                {
/*2041*/            disallowUpdate();
/*2042*/            return null;
                }

                public Node removeNamedItemNS(String s, String s1)
                    throws DOMException
                {
/*2050*/            disallowUpdate();
/*2051*/            return null;
                }

                private AttributeMap()
                {
                }

    }

    private class DOMImplementationImpl
        implements DOMImplementation
    {

                public boolean hasFeature(String s, String s1)
                {
/*1861*/            return false;
                }

                public DocumentType createDocumentType(String s, String s1, String s2)
                    throws DOMException
                {
/*1886*/            disallowUpdate();
/*1887*/            return null;
                }

                public Document createDocument(String s, String s1, DocumentType documenttype)
                    throws DOMException
                {
/*1907*/            disallowUpdate();
/*1908*/            return null;
                }

                public Object getFeature(String s, String s1)
                {
/*1938*/            return null;
                }

                private DOMImplementationImpl()
                {
                }

    }


            public static final char NODE_LETTER[] = {
/*  38*/        'x', 'e', 'a', 't', 'x', 'x', 'x', 'p', 'c', 'r', 
/*  38*/        'x', 'x', 'x', 'n'
            };

            public AbstractNode()
            {
            }

            public abstract boolean isSameNodeInfo(NodeInfo nodeinfo);

            public abstract String generateId();

            public abstract String getSystemId();

            public abstract String getBaseURI();

            public Node getOriginatingNode()
            {
/*  74*/        return this;
            }

            public abstract int compareOrder(NodeInfo nodeinfo);

            public abstract int getNameCode();

            public abstract int getFingerprint();

            public String getNodeName()
            {
/* 108*/        switch(getNodeType())
                {
/* 110*/        case 9: // '\t'
/* 110*/            return "#document";

/* 112*/        case 1: // '\001'
/* 112*/            return getDisplayName();

/* 114*/        case 2: // '\002'
/* 114*/            return getDisplayName();

/* 116*/        case 3: // '\003'
/* 116*/            return "#text";

/* 118*/        case 8: // '\b'
/* 118*/            return "#comment";

/* 120*/        case 7: // '\007'
/* 120*/            return getLocalName();

/* 122*/        case 13: // '\r'
/* 122*/            return getLocalName();

/* 124*/        case 4: // '\004'
/* 124*/        case 5: // '\005'
/* 124*/        case 6: // '\006'
/* 124*/        case 10: // '\n'
/* 124*/        case 11: // '\013'
/* 124*/        case 12: // '\f'
/* 124*/        default:
/* 124*/            return "#unknown";
                }
            }

            public abstract String getPrefix();

            public abstract String getURI();

            public String getDisplayName()
            {
/* 152*/        String s = getLocalName();
/* 153*/        if("".equals(s))
/* 154*/            return "";
/* 156*/        String s1 = getPrefix();
/* 157*/        if("".equals(s1))
/* 158*/            return s;
/* 160*/        else
/* 160*/            return s1 + ":" + s;
            }

            public abstract String getLocalName();

            public abstract boolean hasChildNodes();

            public abstract boolean hasAttributes();

            public abstract String getAttributeValue(String s, String s1);

            public abstract String getAttributeValue(int i);

            public int getLineNumber()
            {
/* 213*/        return -1;
            }

            public int getColumnNumber()
            {
/* 222*/        return -1;
            }

            public String getPublicId()
            {
/* 231*/        return null;
            }

            public abstract AxisEnumeration getEnumeration(byte byte0, NodeTest nodetest);

            public abstract NodeInfo getParent();

            public DocumentInfo getDocumentRoot()
            {
                Object obj;
/* 258*/        for(obj = this; ((NodeInfo) (obj)).getNodeType() != 9; obj = ((NodeInfo) (obj)).getParent());
/* 262*/        return (DocumentInfo)obj;
            }

            public Node getParentNode()
            {
/* 271*/        return (Node)getParent();
            }

            public Node getPreviousSibling()
            {
/* 281*/        AxisEnumeration axisenumeration = getEnumeration((byte)11, AnyNodeTest.getInstance());
/* 283*/        if(axisenumeration.hasMoreElements())
/* 284*/            return (Node)axisenumeration.nextElement();
/* 286*/        else
/* 286*/            return null;
            }

            public Node getNextSibling()
            {
/* 297*/        AxisEnumeration axisenumeration = getEnumeration((byte)7, AnyNodeTest.getInstance());
/* 299*/        if(axisenumeration.hasMoreElements())
/* 300*/            return (Node)axisenumeration.nextElement();
/* 302*/        else
/* 302*/            return null;
            }

            public Node getFirstChild()
            {
/* 312*/        AxisEnumeration axisenumeration = getEnumeration((byte)3, AnyNodeTest.getInstance());
/* 314*/        if(axisenumeration.hasMoreElements())
/* 315*/            return (Node)axisenumeration.nextElement();
/* 317*/        else
/* 317*/            return null;
            }

            public Node getLastChild()
            {
/* 327*/        AxisEnumeration axisenumeration = getEnumeration((byte)3, AnyNodeTest.getInstance());
                NodeInfo nodeinfo;
/* 329*/        for(nodeinfo = null; axisenumeration.hasMoreElements(); nodeinfo = axisenumeration.nextElement());
/* 333*/        return (Node)nodeinfo;
            }

            public Element getDocumentElement()
            {
/* 345*/        DocumentInfo documentinfo = getDocumentRoot();
/* 346*/        AxisEnumeration axisenumeration = documentinfo.getEnumeration((byte)3, new NodeTypeTest((short)1));
/* 348*/        if(axisenumeration.hasMoreElements())
/* 349*/            return (Element)axisenumeration.nextElement();
/* 351*/        else
/* 351*/            return null;
            }

            public void copyStringValue(Outputter outputter)
                throws TransformerException
            {
/* 363*/        outputter.writeContent(getStringValue());
            }

            public void outputNamespaceNodes(Outputter outputter, boolean flag)
                throws TransformerException
            {
            }

            public String getNodeValue()
            {
/* 385*/        switch(getNodeType())
                {
/* 388*/        case 1: // '\001'
/* 388*/        case 9: // '\t'
/* 388*/            return null;

/* 394*/        case 2: // '\002'
/* 394*/        case 3: // '\003'
/* 394*/        case 7: // '\007'
/* 394*/        case 8: // '\b'
/* 394*/        case 13: // '\r'
/* 394*/            return getStringValue();

/* 396*/        case 4: // '\004'
/* 396*/        case 5: // '\005'
/* 396*/        case 6: // '\006'
/* 396*/        case 10: // '\n'
/* 396*/        case 11: // '\013'
/* 396*/        case 12: // '\f'
/* 396*/        default:
/* 396*/            return null;
                }
            }

            public void setNodeValue(String s)
                throws DOMException
            {
/* 405*/        disallowUpdate();
            }

            public NodeList getChildNodes()
            {
/* 416*/        try
                {
/* 416*/            return new NodeSetExtent(getEnumeration((byte)3, AnyNodeTest.getInstance()), LocalOrderComparer.getInstance());
                }
/* 420*/        catch(XPathException xpathexception)
                {
/* 420*/            return null;
                }
            }

            public NamedNodeMap getAttributes()
            {
/* 431*/        if(getNodeType() == 1)
/* 432*/            return new AttributeMap();
/* 434*/        else
/* 434*/            return null;
            }

            public Document getOwnerDocument()
            {
/* 443*/        return (Document)getDocumentRoot();
            }

            public Node insertBefore(Node node, Node node1)
                throws DOMException
            {
/* 460*/        disallowUpdate();
/* 461*/        return null;
            }

            public Node replaceChild(Node node, Node node1)
                throws DOMException
            {
/* 478*/        disallowUpdate();
/* 479*/        return null;
            }

            public Node removeChild(Node node)
                throws DOMException
            {
/* 492*/        disallowUpdate();
/* 493*/        return null;
            }

            public Node appendChild(Node node)
                throws DOMException
            {
/* 506*/        disallowUpdate();
/* 507*/        return null;
            }

            public Node cloneNode(boolean flag)
            {
/* 523*/        return null;
            }

            public void normalize()
            {
            }

            public boolean isSupported(String s, String s1)
            {
/* 557*/        return s.equalsIgnoreCase("xml");
            }

            public boolean supports(String s, String s1)
            {
/* 566*/        return isSupported(s, s1);
            }

            public String getNamespaceURI()
            {
/* 587*/        String s = getURI();
/* 588*/        return s.equals("") ? null : s;
            }

            public void setPrefix(String s)
                throws DOMException
            {
/* 597*/        disallowUpdate();
            }

            protected void disallowUpdate()
                throws DOMException
            {
/* 605*/        throw new UnsupportedOperationException("The Saxon DOM cannot be updated");
            }

            public DocumentType getDoctype()
            {
/* 622*/        return null;
            }

            public DOMImplementation getImplementation()
            {
/* 632*/        return new DOMImplementationImpl();
            }

            public Element createElement(String s)
                throws DOMException
            {
/* 641*/        disallowUpdate();
/* 642*/        return null;
            }

            public DocumentFragment createDocumentFragment()
            {
/* 652*/        return null;
            }

            public Text createTextNode(String s)
            {
/* 663*/        return null;
            }

            public Comment createComment(String s)
            {
/* 673*/        return null;
            }

            public CDATASection createCDATASection(String s)
                throws DOMException
            {
/* 687*/        disallowUpdate();
/* 688*/        return null;
            }

            public ProcessingInstruction createProcessingInstruction(String s, String s1)
                throws DOMException
            {
/* 707*/        disallowUpdate();
/* 708*/        return null;
            }

            public Attr createAttribute(String s)
                throws DOMException
            {
/* 725*/        disallowUpdate();
/* 726*/        return null;
            }

            public EntityReference createEntityReference(String s)
                throws DOMException
            {
/* 741*/        disallowUpdate();
/* 742*/        return null;
            }

            public NodeList getElementsByTagName(String s)
            {
/* 759*/        AxisEnumeration axisenumeration = getEnumeration((byte)4, AnyNodeTest.getInstance());
/* 761*/        NodeSetExtent nodesetextent = new NodeSetExtent(LocalOrderComparer.getInstance());
/* 763*/        while(axisenumeration.hasMoreElements()) 
                {
/* 763*/            NodeInfo nodeinfo = axisenumeration.nextElement();
/* 764*/            if(nodeinfo.getNodeType() == 1 && (s.equals("*") || s.equals(nodeinfo.getDisplayName())))
/* 766*/                nodesetextent.append(nodeinfo);
                }
/* 770*/        return nodesetextent;
            }

            public Node importNode(Node node, boolean flag)
                throws DOMException
            {
/* 782*/        disallowUpdate();
/* 783*/        return null;
            }

            public Element createElementNS(String s, String s1)
                throws DOMException
            {
/* 801*/        disallowUpdate();
/* 802*/        return null;
            }

            public Attr createAttributeNS(String s, String s1)
                throws DOMException
            {
/* 819*/        disallowUpdate();
/* 820*/        return null;
            }

            public NodeList getElementsByTagNameNS(String s, String s1)
            {
/* 841*/        AxisEnumeration axisenumeration = getEnumeration((byte)4, AnyNodeTest.getInstance());
/* 843*/        NodeSetExtent nodesetextent = new NodeSetExtent(LocalOrderComparer.getInstance());
/* 845*/        while(axisenumeration.hasMoreElements()) 
                {
/* 845*/            NodeInfo nodeinfo = axisenumeration.nextElement();
/* 846*/            if(nodeinfo.getNodeType() == 1 && (s.equals("*") || s.equals(nodeinfo.getURI())) && (s1.equals("*") || s1.equals(nodeinfo.getLocalName())))
/* 849*/                nodesetextent.append(nodeinfo);
                }
/* 853*/        return nodesetextent;
            }

            public Element getElementById(String s)
            {
/* 872*/        return (Element)getDocumentRoot().selectID(s);
            }

            public String getTagName()
            {
/* 884*/        return getDisplayName();
            }

            public String getAttribute(String s)
            {
/* 896*/        for(AxisEnumeration axisenumeration = getEnumeration((byte)2, AnyNodeTest.getInstance()); axisenumeration.hasMoreElements();)
                {
/* 898*/            NodeInfo nodeinfo = axisenumeration.nextElement();
/* 899*/            if(nodeinfo.getDisplayName().equals(s))
                    {
/* 900*/                String s1 = nodeinfo.getStringValue();
/* 901*/                if(s1 == null)
/* 901*/                    return "";
/* 902*/                else
/* 902*/                    return s1;
                    }
                }

/* 905*/        return "";
            }

            public Attr getAttributeNode(String s)
            {
/* 921*/        for(AxisEnumeration axisenumeration = getEnumeration((byte)2, AnyNodeTest.getInstance()); axisenumeration.hasMoreElements();)
                {
/* 923*/            NodeInfo nodeinfo = axisenumeration.nextElement();
/* 924*/            if(nodeinfo.getDisplayName().equals(s))
/* 925*/                return (Attr)nodeinfo;
                }

/* 928*/        return null;
            }

            public Attr setAttributeNode(Attr attr)
                throws DOMException
            {
/* 938*/        disallowUpdate();
/* 939*/        return null;
            }

            public void removeAttribute(String s)
                throws DOMException
            {
/* 949*/        disallowUpdate();
            }

            public Attr removeAttributeNode(Attr attr)
                throws DOMException
            {
/* 959*/        disallowUpdate();
/* 960*/        return null;
            }

            public String getAttributeNS(String s, String s1)
            {
/* 975*/        String s2 = getAttributeValue(s, s1);
/* 976*/        if(s2 == null)
/* 976*/            return "";
/* 977*/        else
/* 977*/            return s2;
            }

            public void setAttributeNS(String s, String s1, String s2)
                throws DOMException
            {
/* 995*/        disallowUpdate();
            }

            public void removeAttributeNS(String s, String s1)
                throws DOMException
            {
/*1008*/        disallowUpdate();
            }

            public Attr getAttributeNodeNS(String s, String s1)
            {
/*1023*/        int i = getDocumentRoot().getNamePool().getFingerprint(s, s1);
/*1024*/        if(i == -1)
/*1024*/            return null;
/*1025*/        NameTest nametest = new NameTest((short)2, i);
/*1026*/        AxisEnumeration axisenumeration = getEnumeration((byte)2, nametest);
/*1027*/        if(axisenumeration.hasMoreElements())
/*1027*/            return (Attr)axisenumeration.nextElement();
/*1028*/        else
/*1028*/            return null;
            }

            public Attr setAttributeNodeNS(Attr attr)
                throws DOMException
            {
/*1045*/        disallowUpdate();
/*1046*/        return null;
            }

            public boolean hasAttribute(String s)
            {
/*1062*/        for(AxisEnumeration axisenumeration = getEnumeration((byte)2, AnyNodeTest.getInstance()); axisenumeration.hasMoreElements();)
                {
/*1064*/            NodeInfo nodeinfo = axisenumeration.nextElement();
/*1065*/            if(nodeinfo.getDisplayName().equals(s))
/*1066*/                return true;
                }

/*1069*/        return false;
            }

            public boolean hasAttributeNS(String s, String s1)
            {
/*1086*/        return getAttributeValue(s, s1) != null;
            }

            public String getData()
            {
/*1103*/        return getStringValue();
            }

            public void setData(String s)
                throws DOMException
            {
/*1112*/        disallowUpdate();
            }

            public int getLength()
            {
/*1121*/        return getStringValue().length();
            }

            public String substringData(int i, int j)
                throws DOMException
            {
/*1140*/        try
                {
/*1140*/            return getStringValue().substring(i, i + j);
                }
/*1142*/        catch(IndexOutOfBoundsException indexoutofboundsexception)
                {
/*1142*/            throw new DOMExceptionImpl((short)1, "substringData: index out of bounds");
                }
            }

            public void appendData(String s)
                throws DOMException
            {
/*1156*/        disallowUpdate();
            }

            public void insertData(int i, String s)
                throws DOMException
            {
/*1168*/        disallowUpdate();
            }

            public void deleteData(int i, int j)
                throws DOMException
            {
/*1180*/        disallowUpdate();
            }

            public void replaceData(int i, int j, String s)
                throws DOMException
            {
/*1197*/        disallowUpdate();
            }

            public Text splitText(int i)
                throws DOMException
            {
/*1211*/        disallowUpdate();
/*1212*/        return null;
            }

            public String getName()
            {
/*1224*/        return getDisplayName();
            }

            public String getValue()
            {
/*1233*/        return getStringValue();
            }

            public boolean getSpecified()
            {
/*1244*/        return true;
            }

            public void setValue(String s)
                throws DOMException
            {
/*1253*/        disallowUpdate();
            }

            public Element getOwnerElement()
            {
/*1263*/        if(getNodeType() != 2)
/*1264*/            throw new UnsupportedOperationException("This method is defined only on attribute nodes");
/*1267*/        else
/*1267*/            return (Element)getParent();
            }

            public TypeInfo getSchemaTypeInfo()
            {
/*1282*/        return null;
            }

            public boolean isId()
            {
/*1293*/        return false;
            }

            public short compareDocumentPosition(Node node)
                throws DOMException
            {
/*1315*/        if(node instanceof NodeInfo)
                {
/*1316*/            int i = compareOrder((NodeInfo)node);
/*1317*/            if(i == 0)
/*1318*/                return 0;
/*1319*/            if(i < 0)
                    {
/*1320*/                for(NodeInfo nodeinfo = ((NodeInfo)node).getParent(); nodeinfo != null; nodeinfo = nodeinfo.getParent())
/*1322*/                    if(nodeinfo.isSameNodeInfo(this))
/*1323*/                        return 16;

/*1327*/                return 4;
                    }
/*1329*/            for(NodeInfo nodeinfo1 = getParent(); nodeinfo1 != null; nodeinfo1 = nodeinfo1.getParent())
/*1331*/                if(nodeinfo1.isSameNodeInfo(this))
/*1332*/                    return 8;

/*1336*/            return 2;
                } else
                {
/*1339*/            return 32;
                }
            }

            public Object getFeature(String s, String s1)
            {
/*1369*/        return null;
            }

            public String getTextContent()
                throws DOMException
            {
/*1422*/        return getStringValue();
            }

            public Object getUserData(String s)
            {
/*1438*/        return null;
            }

            public boolean isDefaultNamespace(String s)
            {
                Object obj;
/*1452*/        for(obj = this; obj != null && ((NodeInfo) (obj)).getNodeType() != 1; obj = ((NodeInfo) (obj)).getParent());
/*1456*/        if(obj == null)
/*1457*/            return false;
/*1459*/        for(AxisEnumeration axisenumeration = getEnumeration((byte)8, AnyNodeTest.getInstance()); axisenumeration.hasMoreElements();)
                {
/*1461*/            NodeInfo nodeinfo = axisenumeration.nextElement();
/*1462*/            if(nodeinfo.getStringValue().equals(s))
/*1463*/                return nodeinfo.getLocalName().equals("");
                }

/*1466*/        return false;
            }

            public boolean isEqualNode(Node node)
            {
/*1534*/        throw new UnsupportedOperationException("isEqualNode() is not supported");
            }

            public boolean isSameNode(Node node)
            {
/*1553*/        if(node instanceof NodeInfo)
/*1554*/            return isSameNodeInfo((NodeInfo)node);
/*1556*/        else
/*1556*/            return false;
            }

            public String lookupNamespaceURI(String s)
            {
/*1573*/        if(s == null)
/*1574*/            s = "";
                Object obj;
/*1576*/        for(obj = this; obj != null && ((NodeInfo) (obj)).getNodeType() != 1; obj = ((NodeInfo) (obj)).getParent());
/*1580*/        if(obj == null)
/*1581*/            return null;
/*1583*/        for(AxisEnumeration axisenumeration = getEnumeration((byte)8, AnyNodeTest.getInstance()); axisenumeration.hasMoreElements();)
                {
/*1585*/            NodeInfo nodeinfo = axisenumeration.nextElement();
/*1586*/            if(nodeinfo.getLocalName().equals(s))
/*1587*/                return nodeinfo.getStringValue();
                }

/*1590*/        return null;
            }

            public String lookupPrefix(String s)
            {
                Object obj;
/*1607*/        for(obj = this; obj != null && ((NodeInfo) (obj)).getNodeType() != 1; obj = ((NodeInfo) (obj)).getParent());
/*1611*/        if(obj == null)
/*1612*/            return null;
/*1614*/        for(AxisEnumeration axisenumeration = getEnumeration((byte)8, AnyNodeTest.getInstance()); axisenumeration.hasMoreElements();)
                {
/*1616*/            NodeInfo nodeinfo = axisenumeration.nextElement();
/*1617*/            if(nodeinfo.getStringValue().equals(s))
/*1618*/                return nodeinfo.getLocalName();
                }

/*1621*/        return null;
            }

            public void setTextContent(String s)
                throws DOMException
            {
/*1670*/        disallowUpdate();
            }

            public Object setUserData(String s, Object obj, UserDataHandler userdatahandler)
            {
/*1688*/        disallowUpdate();
/*1689*/        return null;
            }

            public String getWholeText()
            {
/*1706*/        return getStringValue();
            }

            public boolean isElementContentWhitespace()
            {
/*1721*/        return false;
            }

            public Text replaceWholeText(String s)
                throws DOMException
            {
/*1767*/        disallowUpdate();
/*1768*/        return null;
            }

            public void setIdAttribute(String s, boolean flag)
                throws DOMException
            {
/*1793*/        disallowUpdate();
            }

            public void setIdAttributeNode(Attr attr, boolean flag)
                throws DOMException
            {
/*1815*/        disallowUpdate();
            }

            public void setIdAttributeNS(String s, String s1, boolean flag)
                throws DOMException
            {
/*1838*/        disallowUpdate();
            }

            public abstract short getNodeType();

            public abstract void copy(Outputter outputter)
                throws TransformerException;

            public abstract String getStringValue();

            public abstract void setSystemId(String s);

}
