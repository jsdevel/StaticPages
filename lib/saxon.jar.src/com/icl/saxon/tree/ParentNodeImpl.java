// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ParentNodeImpl.java

package com.icl.saxon.tree;

import com.icl.saxon.expr.NodeSetExtent;
import com.icl.saxon.expr.XPathException;
import com.icl.saxon.om.*;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.pattern.AnyNodeTest;
import com.icl.saxon.pattern.NodeTest;
import com.icl.saxon.sort.LocalOrderComparer;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Referenced classes of package com.icl.saxon.tree:
//            NodeImpl, ArrayEnumeration, ChildEnumeration, TextImpl

abstract class ParentNodeImpl extends NodeImpl
{

            private Object children;
            protected int sequence;

            ParentNodeImpl()
            {
/*  26*/        children = null;
            }

            protected final long getSequenceNumber()
            {
/*  40*/        return (long)sequence << 32;
            }

            public final boolean hasChildNodes()
            {
/*  48*/        return children != null;
            }

            public final AxisEnumeration enumerateChildren(NodeTest nodetest)
            {
/*  56*/        if(children == null)
/*  57*/            return EmptyEnumeration.getInstance();
/*  58*/        if(children instanceof NodeImpl)
                {
/*  59*/            NodeImpl nodeimpl = (NodeImpl)children;
/*  60*/            if(nodetest.matches(nodeimpl))
/*  61*/                return new SingletonEnumeration(nodeimpl);
/*  63*/            else
/*  63*/                return EmptyEnumeration.getInstance();
                }
/*  66*/        if(nodetest instanceof AnyNodeTest)
/*  67*/            return new ArrayEnumeration((NodeImpl[])children);
/*  69*/        else
/*  69*/            return new ChildEnumeration(this, nodetest);
            }

            public final Node getFirstChild()
            {
/*  81*/        if(children == null)
/*  81*/            return null;
/*  82*/        if(children instanceof NodeImpl)
/*  82*/            return (NodeImpl)children;
/*  83*/        else
/*  83*/            return ((NodeImpl[])children)[0];
            }

            public final Node getLastChild()
            {
/*  92*/        if(children == null)
/*  92*/            return null;
/*  93*/        if(children instanceof NodeImpl)
                {
/*  93*/            return (NodeImpl)children;
                } else
                {
/*  94*/            NodeImpl anodeimpl[] = (NodeImpl[])children;
/*  95*/            return anodeimpl[anodeimpl.length - 1];
                }
            }

            public final NodeList getChildNodes()
            {
/* 105*/        if(hasChildNodes())
/* 107*/            try
                    {
/* 107*/                return new NodeSetExtent(enumerateChildren(AnyNodeTest.getInstance()), LocalOrderComparer.getInstance());
                    }
/* 111*/            catch(XPathException xpathexception)
                    {
/* 111*/                return super.getChildNodes();
                    }
/* 114*/        else
/* 114*/            return super.getChildNodes();
            }

            protected final NodeImpl getNthChild(int i)
            {
/* 124*/        if(children == null)
/* 124*/            return null;
/* 125*/        if(children instanceof NodeImpl)
/* 126*/            return i != 0 ? null : (NodeImpl)children;
/* 128*/        NodeImpl anodeimpl[] = (NodeImpl[])children;
/* 129*/        if(i < 0 || i >= anodeimpl.length)
/* 129*/            return null;
/* 130*/        else
/* 130*/            return anodeimpl[i];
            }

            public String getStringValue()
            {
/* 141*/        StringBuffer stringbuffer = null;
/* 143*/        for(NodeImpl nodeimpl = (NodeImpl)getFirstChild(); nodeimpl != null; nodeimpl = nodeimpl.getNextInDocument(this))
/* 145*/            if(nodeimpl instanceof TextImpl)
                    {
/* 146*/                if(stringbuffer == null)
/* 147*/                    stringbuffer = new StringBuffer();
/* 149*/                stringbuffer.append(nodeimpl.getStringValue());
                    }

/* 153*/        if(stringbuffer == null)
/* 153*/            return "";
/* 154*/        else
/* 154*/            return stringbuffer.toString();
            }

            public void copyStringValue(Outputter outputter)
                throws TransformerException
            {
/* 162*/        for(NodeImpl nodeimpl = (NodeImpl)getFirstChild(); nodeimpl != null; nodeimpl = nodeimpl.getNextInDocument(this))
/* 164*/            if(nodeimpl.getNodeType() == 3)
/* 165*/                nodeimpl.copyStringValue(outputter);

            }

            public void useChildrenArray(NodeImpl anodeimpl[])
            {
/* 176*/        children = anodeimpl;
            }

            public void addChild(NodeImpl nodeimpl, int i)
            {
                NodeImpl anodeimpl[];
/* 186*/        if(children == null)
/* 187*/            anodeimpl = new NodeImpl[10];
/* 188*/        else
/* 188*/        if(children instanceof NodeImpl)
                {
/* 189*/            anodeimpl = new NodeImpl[10];
/* 190*/            anodeimpl[0] = (NodeImpl)children;
                } else
                {
/* 192*/            anodeimpl = (NodeImpl[])children;
                }
/* 194*/        if(i >= anodeimpl.length)
                {
/* 195*/            NodeImpl anodeimpl1[] = new NodeImpl[anodeimpl.length * 2];
/* 196*/            System.arraycopy(anodeimpl, 0, anodeimpl1, 0, anodeimpl.length);
/* 197*/            anodeimpl = anodeimpl1;
                }
/* 199*/        anodeimpl[i] = nodeimpl;
/* 200*/        nodeimpl.parent = this;
/* 201*/        nodeimpl.index = i;
/* 202*/        children = anodeimpl;
            }

            public void removeChild(int i)
            {
/* 210*/        if(children instanceof NodeImpl)
/* 211*/            children = null;
/* 213*/        else
/* 213*/            ((NodeImpl[])children)[i] = null;
            }

            public void renumberChildren()
            {
/* 222*/        int i = 0;
/* 223*/        if(children == null)
/* 224*/            return;
/* 225*/        if(children instanceof NodeImpl)
                {
/* 226*/            ((NodeImpl)children).parent = this;
/* 227*/            ((NodeImpl)children).index = 0;
                } else
                {
/* 229*/            NodeImpl anodeimpl[] = (NodeImpl[])children;
/* 230*/            for(int j = 0; j < anodeimpl.length; j++)
/* 231*/                if(anodeimpl[j] != null)
                        {
/* 232*/                    anodeimpl[j].parent = this;
/* 233*/                    anodeimpl[j].index = i;
/* 234*/                    anodeimpl[i] = anodeimpl[j];
/* 235*/                    i++;
                        }

/* 238*/            compact(i);
                }
            }

            public void dropChildren()
            {
/* 250*/        for(NodeImpl nodeimpl = getNextInDocument(this); nodeimpl != null; nodeimpl = nodeimpl.getNextInDocument(this))
                {
/* 252*/            if(!(nodeimpl instanceof TextImpl))
/* 253*/                continue;
/* 253*/            ((TextImpl)nodeimpl).truncateToStart();
/* 254*/            break;
                }

/* 259*/        children = null;
            }

            public void compact(int i)
            {
/* 267*/        if(i == 0)
/* 268*/            children = null;
/* 269*/        else
/* 269*/        if(i == 1)
                {
/* 270*/            if(children instanceof NodeImpl[])
/* 271*/                children = ((NodeImpl[])children)[0];
                } else
                {
/* 274*/            NodeImpl anodeimpl[] = new NodeImpl[i];
/* 275*/            System.arraycopy(children, 0, anodeimpl, 0, i);
/* 276*/            children = anodeimpl;
                }
            }

            public String getNodeValue()
            {
/* 285*/        return null;
            }
}
