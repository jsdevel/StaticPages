// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TinyNodeImpl.java

package com.icl.saxon.tinytree;

import com.icl.saxon.om.*;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.pattern.*;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;

// Referenced classes of package com.icl.saxon.tinytree:
//            AncestorEnumeration, AttributeEnumeration, SiblingEnumeration, TinyDocumentImpl, 
//            DescendantEnumeration, FollowingEnumeration, NamespaceEnumeration, TinyElementImpl, 
//            PrecedingEnumeration, PrecedingSiblingEnumeration

abstract class TinyNodeImpl extends AbstractNode
{

            protected TinyDocumentImpl document;
            protected int nodeNr;
            protected TinyNodeImpl parent;

            TinyNodeImpl()
            {
/*  23*/        parent = null;
            }

            public void setSystemId(String s)
            {
/*  33*/        short word0 = document.nodeType[nodeNr];
/*  34*/        if(word0 == 2 || word0 == 13)
/*  35*/            ((TinyNodeImpl)getParent()).setSystemId(s);
/*  37*/        else
/*  37*/            document.setSystemId(nodeNr, s);
            }

            protected void setParentNode(TinyNodeImpl tinynodeimpl)
            {
/*  48*/        parent = tinynodeimpl;
            }

            public boolean isSameNodeInfo(NodeInfo nodeinfo)
            {
/*  58*/        if(this == nodeinfo)
/*  58*/            return true;
/*  59*/        if(!(nodeinfo instanceof TinyNodeImpl))
/*  59*/            return false;
/*  60*/        if(getNodeType() != nodeinfo.getNodeType())
/*  60*/            return false;
/*  61*/        else
/*  61*/            return document == ((TinyNodeImpl)nodeinfo).document && nodeNr == ((TinyNodeImpl)nodeinfo).nodeNr;
            }

            public String getSystemId()
            {
/*  70*/        return document.getSystemId(nodeNr);
            }

            public String getBaseURI()
            {
/*  79*/        return getParent().getBaseURI();
            }

            public Node getOriginatingNode()
            {
/*  87*/        return this;
            }

            public void setLineNumber(int i)
            {
/*  96*/        document.setLineNumber(nodeNr, i);
            }

            public int getLineNumber()
            {
/* 105*/        return document.getLineNumber(nodeNr);
            }

            protected long getSequenceNumber()
            {
/* 115*/        return (long)nodeNr << 32;
            }

            public final int compareOrder(NodeInfo nodeinfo)
            {
/* 128*/        long l = getSequenceNumber();
/* 129*/        long l1 = ((TinyNodeImpl)nodeinfo).getSequenceNumber();
/* 130*/        if(l < l1)
/* 130*/            return -1;
/* 131*/        return l <= l1 ? 0 : 1;
            }

            public int getFingerprint()
            {
/* 140*/        int i = getNameCode();
/* 141*/        if(i == -1)
/* 141*/            return -1;
/* 142*/        else
/* 142*/            return i & 0xfffff;
            }

            public int getNameCode()
            {
/* 151*/        return document.nameCode[nodeNr];
            }

            public String getPrefix()
            {
/* 160*/        int i = document.nameCode[nodeNr];
/* 161*/        if(i < 0)
/* 161*/            return "";
/* 162*/        if((i >> 20 & 0xff) == 0)
/* 162*/            return "";
/* 163*/        else
/* 163*/            return document.getNamePool().getPrefix(i);
            }

            public String getURI()
            {
/* 174*/        int i = document.nameCode[nodeNr];
/* 175*/        if(i < 0)
/* 175*/            return "";
/* 176*/        else
/* 176*/            return document.getNamePool().getURI(i);
            }

            public String getDisplayName()
            {
/* 187*/        int i = document.nameCode[nodeNr];
/* 188*/        if(i < 0)
/* 188*/            return "";
/* 189*/        else
/* 189*/            return document.getNamePool().getDisplayName(i);
            }

            public String getLocalName()
            {
/* 199*/        int i = document.nameCode[nodeNr];
/* 200*/        if(i < 0)
/* 200*/            return "";
/* 201*/        else
/* 201*/            return document.getNamePool().getLocalName(i);
            }

            public AxisEnumeration getEnumeration(byte byte0, NodeTest nodetest)
            {
/* 217*/        short word0 = getNodeType();
/* 218*/        switch(byte0)
                {
/* 220*/        case 0: // '\0'
/* 220*/            if(word0 == 9)
/* 221*/                return EmptyEnumeration.getInstance();
/* 223*/            else
/* 223*/                return new AncestorEnumeration(document, this, nodetest, false);

/* 227*/        case 1: // '\001'
/* 227*/            if(word0 == 9)
                    {
/* 228*/                if(nodetest.matches(this))
/* 229*/                    return new SingletonEnumeration(this);
/* 231*/                else
/* 231*/                    return EmptyEnumeration.getInstance();
                    } else
                    {
/* 234*/                return new AncestorEnumeration(document, this, nodetest, true);
                    }

/* 238*/        case 2: // '\002'
/* 238*/            if(word0 != 1)
/* 238*/                return EmptyEnumeration.getInstance();
/* 239*/            if(document.offset[nodeNr] < 0)
/* 239*/                return EmptyEnumeration.getInstance();
/* 240*/            else
/* 240*/                return new AttributeEnumeration(document, nodeNr, nodetest);

/* 243*/        case 3: // '\003'
/* 243*/            if(hasChildNodes())
/* 244*/                return new SiblingEnumeration(document, this, nodetest, true);
/* 246*/            else
/* 246*/                return EmptyEnumeration.getInstance();

/* 250*/        case 4: // '\004'
/* 250*/            if(word0 == 9 && (nodetest instanceof NameTest) && nodetest.getNodeType() == 1)
/* 253*/                return ((TinyDocumentImpl)this).getAllElements(((NameTest)nodetest).getFingerprint());
/* 255*/            if(hasChildNodes())
/* 256*/                return new DescendantEnumeration(document, this, nodetest, false);
/* 258*/            else
/* 258*/                return EmptyEnumeration.getInstance();

/* 262*/        case 5: // '\005'
/* 262*/            if(hasChildNodes())
/* 263*/                return new DescendantEnumeration(document, this, nodetest, true);
/* 265*/            if(nodetest.matches(this))
/* 266*/                return new SingletonEnumeration(this);
/* 268*/            else
/* 268*/                return EmptyEnumeration.getInstance();

/* 272*/        case 6: // '\006'
/* 272*/            if(word0 == 9)
/* 273*/                return EmptyEnumeration.getInstance();
/* 274*/            if(word0 == 2 || word0 == 13)
/* 275*/                return new FollowingEnumeration(document, (TinyNodeImpl)getParent(), nodetest, true);
/* 278*/            else
/* 278*/                return new FollowingEnumeration(document, this, nodetest, false);

/* 283*/        case 7: // '\007'
/* 283*/            if(word0 == 9 || word0 == 2 || word0 == 13)
/* 284*/                return EmptyEnumeration.getInstance();
/* 286*/            else
/* 286*/                return new SiblingEnumeration(document, this, nodetest, false);

/* 291*/        case 8: // '\b'
/* 291*/            if(word0 != 1)
/* 291*/                return EmptyEnumeration.getInstance();
/* 292*/            else
/* 292*/                return new NamespaceEnumeration((TinyElementImpl)this, nodetest);

/* 295*/        case 9: // '\t'
/* 295*/            NodeInfo nodeinfo = getParent();
/* 296*/            if(nodeinfo == null)
/* 296*/                return EmptyEnumeration.getInstance();
/* 297*/            if(nodetest.matches(nodeinfo))
/* 297*/                return new SingletonEnumeration(nodeinfo);
/* 298*/            else
/* 298*/                return EmptyEnumeration.getInstance();

/* 301*/        case 10: // '\n'
/* 301*/            if(word0 == 9)
/* 302*/                return EmptyEnumeration.getInstance();
/* 303*/            if(word0 == 2 || word0 == 13)
/* 304*/                return new PrecedingEnumeration(document, (TinyNodeImpl)getParent(), nodetest, false);
/* 307*/            else
/* 307*/                return new PrecedingEnumeration(document, this, nodetest, false);

/* 312*/        case 11: // '\013'
/* 312*/            if(word0 == 9 || word0 == 2 || word0 == 13)
/* 313*/                return EmptyEnumeration.getInstance();
/* 315*/            else
/* 315*/                return new PrecedingSiblingEnumeration(document, this, nodetest);

/* 320*/        case 12: // '\f'
/* 320*/            if(nodetest.matches(this))
/* 320*/                return new SingletonEnumeration(this);
/* 321*/            else
/* 321*/                return EmptyEnumeration.getInstance();

/* 324*/        case 13: // '\r'
/* 324*/            if(word0 == 9)
/* 325*/                return EmptyEnumeration.getInstance();
/* 326*/            if(word0 == 2 || word0 == 13)
                    {
/* 328*/                TinyNodeImpl tinynodeimpl = (TinyNodeImpl)getParent();
/* 329*/                return new PrependIterator(tinynodeimpl, new PrecedingEnumeration(document, tinynodeimpl, nodetest, true));
                    } else
                    {
/* 333*/                return new PrecedingEnumeration(document, this, nodetest, true);
                    }
                }
/* 338*/        throw new IllegalArgumentException("Unknown axis number " + byte0);
            }

            public NodeInfo getParent()
            {
/* 348*/        if(parent != null)
/* 348*/            return parent;
/* 351*/        for(int i = nodeNr - 1; i >= 0; i--)
/* 352*/            if(document.depth[i] < document.depth[nodeNr])
                    {
/* 353*/                parent = document.getNode(i);
/* 354*/                return parent;
                    }

/* 357*/        parent = document;
/* 358*/        return parent;
            }

            public boolean hasChildNodes()
            {
/* 369*/        return false;
            }

            public boolean hasAttributes()
            {
/* 381*/        return false;
            }

            public String getAttributeValue(String s, String s1)
            {
/* 394*/        return null;
            }

            public String getAttributeValue(int i)
            {
/* 421*/        return null;
            }

            public DocumentInfo getDocumentRoot()
            {
/* 430*/        return document;
            }

            public void outputNamespaceNodes(Outputter outputter, boolean flag)
                throws TransformerException
            {
            }

            public String generateId()
            {
/* 451*/        return document.generateId() + AbstractNode.NODE_LETTER[getNodeType()] + nodeNr;
            }
}
