// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SingletonNodeSet.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.InternalSaxonError;
import com.icl.saxon.om.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Referenced classes of package com.icl.saxon.expr:
//            NodeSetValue, EmptyNodeSet, BooleanValue, StringValue, 
//            FragmentValue, TextFragmentValue, ObjectValue, XPathException, 
//            NumericValue, Value, Expression

public class SingletonNodeSet extends NodeSetValue
    implements NodeList
{

            protected NodeInfo node;
            protected boolean generalUseAllowed;

            public void allowGeneralUse()
            {
/*  22*/        generalUseAllowed = true;
            }

            public boolean isGeneralUseAllowed()
            {
/*  30*/        return generalUseAllowed;
            }

            public SingletonNodeSet()
            {
/*  13*/        node = null;
/*  14*/        generalUseAllowed = true;
/*  38*/        node = null;
            }

            public SingletonNodeSet(NodeInfo nodeinfo)
            {
/*  13*/        node = null;
/*  14*/        generalUseAllowed = true;
/*  46*/        node = nodeinfo;
            }

            public Expression simplify()
            {
/*  54*/        if(node == null)
/*  55*/            return new EmptyNodeSet();
/*  57*/        else
/*  57*/            return this;
            }

            public Value evaluate(Context context)
            {
/*  67*/        return this;
            }

            public NodeSetValue evaluateAsNodeSet(Context context)
            {
/*  77*/        return this;
            }

            public void setSorted(boolean flag)
            {
            }

            public boolean isSorted()
            {
/*  96*/        return true;
            }

            public String asString()
            {
/* 106*/        if(node == null)
/* 107*/            return "";
/* 109*/        else
/* 109*/            return node.getStringValue();
            }

            public boolean asBoolean()
            {
/* 119*/        return node != null;
            }

            public int getCount()
            {
/* 128*/        return node != null ? 1 : 0;
            }

            public NodeSetValue sort()
            {
/* 139*/        return this;
            }

            public NodeInfo getFirst()
            {
/* 148*/        return node;
            }

            public boolean equals(Value value)
                throws XPathException
            {
/* 158*/        if(node == null)
/* 159*/            if(value instanceof BooleanValue)
/* 160*/                return !value.asBoolean();
/* 162*/            else
/* 162*/                return false;
/* 166*/        if((value instanceof StringValue) || (value instanceof FragmentValue) || (value instanceof TextFragmentValue) || (value instanceof ObjectValue))
/* 170*/            return node.getStringValue().equals(value.asString());
/* 172*/        if(value instanceof NodeSetValue)
/* 177*/            try
                    {
/* 177*/                String s = node.getStringValue();
/* 178*/                for(NodeEnumeration nodeenumeration = ((NodeSetValue)value).enumerate(); nodeenumeration.hasMoreElements();)
/* 180*/                    if(nodeenumeration.nextElement().getStringValue().equals(s))
/* 180*/                        return true;

/* 182*/                return false;
                    }
/* 184*/            catch(XPathException xpathexception)
                    {
/* 184*/                throw new InternalSaxonError(xpathexception.getMessage());
                    }
/* 187*/        if(value instanceof NumericValue)
/* 188*/            return Value.stringToNumber(node.getStringValue()) == value.asNumber();
/* 190*/        if(value instanceof BooleanValue)
/* 191*/            return value.asBoolean();
/* 194*/        else
/* 194*/            throw new InternalSaxonError("Unknown data type in a relational expression");
            }

            public boolean notEquals(Value value)
                throws XPathException
            {
/* 204*/        if(node == null)
/* 205*/            if(value instanceof BooleanValue)
/* 206*/                return value.asBoolean();
/* 208*/            else
/* 208*/                return false;
/* 212*/        if((value instanceof StringValue) || (value instanceof FragmentValue) || (value instanceof TextFragmentValue) || (value instanceof ObjectValue))
/* 216*/            return !node.getStringValue().equals(value.asString());
/* 218*/        if(value instanceof NodeSetValue)
/* 221*/            try
                    {
/* 221*/                String s = node.getStringValue();
/* 223*/                for(NodeEnumeration nodeenumeration = ((NodeSetValue)value).enumerate(); nodeenumeration.hasMoreElements();)
/* 225*/                    if(!nodeenumeration.nextElement().getStringValue().equals(s))
/* 225*/                        return true;

/* 227*/                return false;
                    }
/* 229*/            catch(XPathException xpathexception)
                    {
/* 229*/                throw new InternalSaxonError(xpathexception.getMessage());
                    }
/* 232*/        if(value instanceof NumericValue)
/* 233*/            return Value.stringToNumber(node.getStringValue()) != value.asNumber();
/* 235*/        if(value instanceof BooleanValue)
/* 236*/            return !value.asBoolean();
/* 239*/        else
/* 239*/            throw new InternalSaxonError("Unknown data type in a relational expression");
            }

            public NodeEnumeration enumerate()
                throws XPathException
            {
/* 249*/        return new SingletonEnumeration(node);
            }

            public int getLength()
            {
/* 259*/        return getCount();
            }

            public Node item(int i)
            {
/* 267*/        if(i == 0 && (node instanceof Node))
/* 268*/            return (Node)node;
/* 270*/        else
/* 270*/            return null;
            }
}
