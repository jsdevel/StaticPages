// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NodeSetValue.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.InternalSaxonError;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.sort.LocalOrderComparer;
import java.io.PrintStream;
import java.util.Hashtable;
import org.w3c.dom.NodeList;

// Referenced classes of package com.icl.saxon.expr:
//            Value, StringValue, ObjectValue, SingletonNodeSet, 
//            NumericValue, BooleanValue, NodeSetExtent, XPathException, 
//            Expression

public abstract class NodeSetValue extends Value
{

            private Hashtable stringValues;

            public NodeSetValue()
            {
/*  18*/        stringValues = null;
            }

            public int getDataType()
            {
/*  26*/        return 4;
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  35*/        sort();
/*  36*/        return this;
            }

            public NodeSetValue evaluateAsNodeSet(Context context)
                throws XPathException
            {
/*  46*/        sort();
/*  47*/        return this;
            }

            public abstract NodeEnumeration enumerate()
                throws XPathException;

            public NodeEnumeration enumerate(Context context, boolean flag)
                throws XPathException
            {
/*  65*/        if(flag)
/*  65*/            sort();
/*  66*/        return enumerate();
            }

            public abstract void setSorted(boolean flag);

            public abstract boolean isSorted()
                throws XPathException;

            public abstract String asString()
                throws XPathException;

            public double asNumber()
                throws XPathException
            {
/* 100*/        return (new StringValue(asString())).asNumber();
            }

            public abstract boolean asBoolean()
                throws XPathException;

            public abstract int getCount()
                throws XPathException;

            public abstract NodeSetValue sort()
                throws XPathException;

            public abstract NodeInfo getFirst()
                throws XPathException;

            private Hashtable getStringValues()
                throws XPathException
            {
/* 140*/        if(stringValues == null)
                {
/* 141*/            stringValues = new Hashtable();
/* 142*/            for(NodeEnumeration nodeenumeration = enumerate(); nodeenumeration.hasMoreElements(); stringValues.put(nodeenumeration.nextElement().getStringValue(), "x"));
                }
/* 147*/        return stringValues;
            }

            public boolean equals(Value value)
                throws XPathException
            {
/* 156*/        if(value instanceof ObjectValue)
/* 157*/            return false;
/* 159*/        if(value instanceof SingletonNodeSet)
/* 160*/            if(value.asBoolean())
/* 161*/                return equals(((Value) (new StringValue(value.asString()))));
/* 163*/            else
/* 163*/                return false;
/* 166*/        if(value instanceof NodeSetValue)
                {
/* 170*/            Hashtable hashtable = getStringValues();
/* 172*/            for(NodeEnumeration nodeenumeration2 = ((NodeSetValue)value).enumerate(); nodeenumeration2.hasMoreElements();)
/* 174*/                if(hashtable.get(nodeenumeration2.nextElement().getStringValue()) != null)
/* 174*/                    return true;

/* 176*/            return false;
                }
/* 178*/        if(value instanceof NumericValue)
                {
/* 179*/            for(NodeEnumeration nodeenumeration = enumerate(); nodeenumeration.hasMoreElements();)
                    {
/* 181*/                NodeInfo nodeinfo = nodeenumeration.nextElement();
/* 182*/                if(Value.stringToNumber(nodeinfo.getStringValue()) == value.asNumber())
/* 182*/                    return true;
                    }

/* 184*/            return false;
                }
/* 186*/        if(value instanceof StringValue)
/* 187*/            if(stringValues == null)
                    {
/* 188*/                for(NodeEnumeration nodeenumeration1 = enumerate(); nodeenumeration1.hasMoreElements();)
                        {
/* 190*/                    NodeInfo nodeinfo1 = nodeenumeration1.nextElement();
/* 191*/                    if(nodeinfo1.getStringValue().equals(value.asString()))
/* 191*/                        return true;
                        }

/* 193*/                return false;
                    } else
                    {
/* 195*/                return stringValues.get(value.asString()) != null;
                    }
/* 198*/        if(value instanceof BooleanValue)
/* 200*/            return asBoolean() == value.asBoolean();
/* 203*/        else
/* 203*/            throw new InternalSaxonError("Unknown data type in a relational expression");
            }

            public boolean notEquals(Value value)
                throws XPathException
            {
/* 215*/        if(value instanceof ObjectValue)
/* 216*/            return false;
/* 218*/        if(value instanceof SingletonNodeSet)
/* 219*/            if(value.asBoolean())
/* 220*/                return notEquals(((Value) (new StringValue(value.asString()))));
/* 222*/            else
/* 222*/                return false;
/* 225*/        if(value instanceof NodeSetValue)
                {
/* 230*/            for(NodeEnumeration nodeenumeration = enumerate(); nodeenumeration.hasMoreElements();)
                    {
/* 232*/                String s = nodeenumeration.nextElement().getStringValue();
/* 233*/                for(NodeEnumeration nodeenumeration3 = ((NodeSetValue)value).enumerate(); nodeenumeration3.hasMoreElements();)
                        {
/* 235*/                    String s1 = nodeenumeration3.nextElement().getStringValue();
/* 236*/                    if(!s.equals(s1))
/* 236*/                        return true;
                        }

                    }

/* 239*/            return false;
                }
/* 241*/        if(value instanceof NumericValue)
                {
/* 242*/            for(NodeEnumeration nodeenumeration1 = enumerate(); nodeenumeration1.hasMoreElements();)
                    {
/* 244*/                NodeInfo nodeinfo = nodeenumeration1.nextElement();
/* 245*/                if(Value.stringToNumber(nodeinfo.getStringValue()) != value.asNumber())
/* 245*/                    return true;
                    }

/* 247*/            return false;
                }
/* 249*/        if(value instanceof StringValue)
                {
/* 250*/            for(NodeEnumeration nodeenumeration2 = enumerate(); nodeenumeration2.hasMoreElements();)
                    {
/* 252*/                NodeInfo nodeinfo1 = nodeenumeration2.nextElement();
/* 253*/                if(!nodeinfo1.getStringValue().equals(value.asString()))
/* 253*/                    return true;
                    }

/* 255*/            return false;
                }
/* 257*/        if(value instanceof BooleanValue)
/* 259*/            return asBoolean() != value.asBoolean();
/* 262*/        else
/* 262*/            throw new InternalSaxonError("Unknown data type in a relational expression");
            }

            public boolean compare(int i, Value value)
                throws XPathException
            {
/* 274*/        if(value instanceof ObjectValue)
/* 275*/            return false;
/* 278*/        if(value instanceof SingletonNodeSet)
/* 279*/            if(value.asBoolean())
/* 280*/                value = new StringValue(value.asString());
/* 282*/            else
/* 282*/                return false;
/* 286*/        if(i == 11)
/* 286*/            return equals(value);
/* 287*/        if(i == 34)
/* 287*/            return notEquals(value);
/* 289*/        if(value instanceof NodeSetValue)
                {
/* 293*/            double d = (-1.0D / 0.0D);
/* 294*/            double d1 = (1.0D / 0.0D);
/* 295*/            boolean flag = true;
/* 297*/            for(NodeEnumeration nodeenumeration1 = enumerate(); nodeenumeration1.hasMoreElements();)
                    {
/* 299*/                double d2 = Value.stringToNumber(nodeenumeration1.nextElement().getStringValue());
/* 300*/                if(d2 < d1)
/* 300*/                    d1 = d2;
/* 301*/                if(d2 > d)
/* 301*/                    d = d2;
/* 302*/                flag = false;
                    }

/* 305*/            if(flag)
/* 305*/                return false;
/* 309*/            double d3 = (-1.0D / 0.0D);
/* 310*/            double d4 = (1.0D / 0.0D);
/* 311*/            boolean flag1 = true;
/* 313*/            for(NodeEnumeration nodeenumeration2 = ((NodeSetValue)value).enumerate(); nodeenumeration2.hasMoreElements();)
                    {
/* 315*/                double d5 = Value.stringToNumber(nodeenumeration2.nextElement().getStringValue());
/* 316*/                if(d5 < d4)
/* 316*/                    d4 = d5;
/* 317*/                if(d5 > d3)
/* 317*/                    d3 = d5;
/* 318*/                flag1 = false;
                    }

/* 321*/            if(flag1)
/* 321*/                return false;
/* 323*/            switch(i)
                    {
/* 325*/            case 22: // '\026'
/* 325*/                return d1 < d3;

/* 327*/            case 24: // '\030'
/* 327*/                return d1 <= d3;

/* 329*/            case 21: // '\025'
/* 329*/                return d > d4;

/* 331*/            case 23: // '\027'
/* 331*/                return d >= d4;
                    }
/* 333*/            return false;
                }
/* 337*/        if((value instanceof NumericValue) || (value instanceof StringValue))
                {
/* 338*/            for(NodeEnumeration nodeenumeration = enumerate(); nodeenumeration.hasMoreElements();)
                    {
/* 340*/                NodeInfo nodeinfo = nodeenumeration.nextElement();
/* 341*/                if(numericCompare(i, Value.stringToNumber(nodeinfo.getStringValue()), value.asNumber()))
/* 344*/                    return true;
                    }

/* 346*/            return false;
                }
/* 347*/        if(value instanceof BooleanValue)
/* 348*/            return numericCompare(i, (new BooleanValue(asBoolean())).asNumber(), (new BooleanValue(value.asBoolean())).asNumber());
/* 352*/        else
/* 352*/            throw new InternalSaxonError("Unknown data type in a relational expression");
            }

            public void display(int i)
            {
/* 363*/        System.err.println(Expression.indent(i) + "** node set value (class " + getClass() + ") **");
            }

            public int conversionPreference(Class class1)
            {
/* 373*/        if(class1.isAssignableFrom(com.icl.saxon.expr.NodeSetValue.class))
/* 373*/            return 0;
/* 375*/        if(class1 == (org.w3c.dom.NodeList.class))
/* 375*/            return 0;
/* 376*/        if(class1 == Boolean.TYPE)
/* 376*/            return 8;
/* 377*/        if(class1 == (java.lang.Boolean.class))
/* 377*/            return 9;
/* 378*/        if(class1 == Byte.TYPE)
/* 378*/            return 6;
/* 379*/        if(class1 == (java.lang.Byte.class))
/* 379*/            return 7;
/* 380*/        if(class1 == Character.TYPE)
/* 380*/            return 4;
/* 381*/        if(class1 == (java.lang.Character.class))
/* 381*/            return 5;
/* 382*/        if(class1 == Double.TYPE)
/* 382*/            return 6;
/* 383*/        if(class1 == (java.lang.Double.class))
/* 383*/            return 7;
/* 384*/        if(class1 == Float.TYPE)
/* 384*/            return 6;
/* 385*/        if(class1 == (java.lang.Float.class))
/* 385*/            return 7;
/* 386*/        if(class1 == Integer.TYPE)
/* 386*/            return 6;
/* 387*/        if(class1 == (java.lang.Integer.class))
/* 387*/            return 7;
/* 388*/        if(class1 == Long.TYPE)
/* 388*/            return 6;
/* 389*/        if(class1 == (java.lang.Long.class))
/* 389*/            return 7;
/* 390*/        if(class1 == Short.TYPE)
/* 390*/            return 6;
/* 391*/        if(class1 == (java.lang.Short.class))
/* 391*/            return 7;
/* 392*/        if(class1 == (java.lang.String.class))
/* 392*/            return 2;
/* 393*/        if(class1 == (java.lang.Object.class))
/* 393*/            return 3;
/* 394*/        if(class1 == (org.w3c.dom.Node.class))
/* 394*/            return 1;
/* 395*/        if(class1 == (org.w3c.dom.Element.class))
/* 395*/            return 1;
/* 396*/        if(class1 == (org.w3c.dom.Document.class))
/* 396*/            return 1;
/* 397*/        if(class1 == (org.w3c.dom.DocumentFragment.class))
/* 397*/            return 1;
/* 398*/        if(class1 == (org.w3c.dom.Attr.class))
/* 398*/            return 1;
/* 399*/        if(class1 == (org.w3c.dom.Comment.class))
/* 399*/            return 1;
/* 400*/        if(class1 == (org.w3c.dom.Text.class))
/* 400*/            return 1;
/* 401*/        if(class1 == (org.w3c.dom.CharacterData.class))
/* 401*/            return 1;
/* 402*/        return class1 != (org.w3c.dom.ProcessingInstruction.class) ? 0x7fffffff : 1;
            }

            public Object convertToJava(Class class1)
                throws XPathException
            {
/* 412*/        if(class1.isAssignableFrom(getClass()))
/* 413*/            return this;
/* 414*/        if(class1 == (com.icl.saxon.om.NodeEnumeration.class))
/* 415*/            return enumerate();
/* 417*/        if(class1 == Boolean.TYPE)
/* 418*/            return new Boolean(asBoolean());
/* 419*/        if(class1 == (java.lang.Boolean.class))
/* 420*/            return new Boolean(asBoolean());
/* 422*/        if(class1 == (java.lang.Object.class) || class1 == (org.w3c.dom.NodeList.class))
/* 423*/            if(this instanceof NodeList)
/* 424*/                return this;
/* 427*/            else
/* 427*/                return new NodeSetExtent(enumerate(), new LocalOrderComparer());
/* 430*/        if(class1 == (org.w3c.dom.Node.class))
                {
/* 431*/            NodeInfo nodeinfo = getFirst();
/* 432*/            return nodeinfo;
                }
/* 434*/        if(class1 == (org.w3c.dom.Attr.class))
                {
/* 435*/            NodeInfo nodeinfo1 = getFirst();
/* 436*/            if(nodeinfo1 == null)
/* 436*/                return null;
/* 437*/            if(nodeinfo1.getNodeType() == 2)
/* 437*/                return nodeinfo1;
/* 438*/            else
/* 438*/                throw new XPathException("Node is of wrong type");
                }
/* 440*/        if(class1 == (org.w3c.dom.CharacterData.class) || class1 == (org.w3c.dom.Text.class))
                {
/* 441*/            NodeInfo nodeinfo2 = getFirst();
/* 442*/            if(nodeinfo2 == null)
/* 442*/                return null;
/* 443*/            if(nodeinfo2.getNodeType() == 3)
/* 443*/                return nodeinfo2;
/* 444*/            else
/* 444*/                throw new XPathException("Node is of wrong type");
                }
/* 446*/        if(class1 == (org.w3c.dom.Comment.class))
                {
/* 447*/            NodeInfo nodeinfo3 = getFirst();
/* 448*/            if(nodeinfo3 == null)
/* 448*/                return null;
/* 449*/            if(nodeinfo3.getNodeType() == 8)
/* 449*/                return nodeinfo3;
/* 450*/            else
/* 450*/                throw new XPathException("Node is of wrong type");
                }
/* 452*/        if(class1 == (org.w3c.dom.Document.class))
                {
/* 453*/            NodeInfo nodeinfo4 = getFirst();
/* 454*/            if(nodeinfo4 == null)
/* 454*/                return null;
/* 455*/            if(nodeinfo4.getNodeType() == 9)
/* 455*/                return nodeinfo4;
/* 456*/            else
/* 456*/                throw new XPathException("Node is of wrong type");
                }
/* 458*/        if(class1 == (org.w3c.dom.Element.class))
                {
/* 459*/            NodeInfo nodeinfo5 = getFirst();
/* 460*/            if(nodeinfo5 == null)
/* 460*/                return null;
/* 461*/            if(nodeinfo5.getNodeType() == 1)
/* 461*/                return nodeinfo5;
/* 462*/            else
/* 462*/                throw new XPathException("Node is of wrong type");
                }
/* 464*/        if(class1 == (org.w3c.dom.ProcessingInstruction.class))
                {
/* 465*/            NodeInfo nodeinfo6 = getFirst();
/* 466*/            if(nodeinfo6 == null)
/* 466*/                return null;
/* 467*/            if(nodeinfo6.getNodeType() == 7)
/* 467*/                return nodeinfo6;
/* 468*/            else
/* 468*/                throw new XPathException("Node is of wrong type");
                }
/* 470*/        if(class1 == (java.lang.String.class))
/* 471*/            return asString();
/* 472*/        if(class1 == Double.TYPE)
/* 473*/            return new Double(asNumber());
/* 474*/        if(class1 == (java.lang.Double.class))
/* 475*/            return new Double(asNumber());
/* 476*/        if(class1 == Float.TYPE)
/* 477*/            return new Float(asNumber());
/* 478*/        if(class1 == (java.lang.Float.class))
/* 479*/            return new Float(asNumber());
/* 480*/        if(class1 == Long.TYPE)
/* 481*/            return new Long((long)asNumber());
/* 482*/        if(class1 == (java.lang.Long.class))
/* 483*/            return new Long((long)asNumber());
/* 484*/        if(class1 == Integer.TYPE)
/* 485*/            return new Integer((int)asNumber());
/* 486*/        if(class1 == (java.lang.Integer.class))
/* 487*/            return new Integer((int)asNumber());
/* 488*/        if(class1 == Short.TYPE)
/* 489*/            return new Short((short)(int)asNumber());
/* 490*/        if(class1 == (java.lang.Short.class))
/* 491*/            return new Short((short)(int)asNumber());
/* 492*/        if(class1 == Byte.TYPE)
/* 493*/            return new Byte((byte)(int)asNumber());
/* 494*/        if(class1 == (java.lang.Byte.class))
/* 495*/            return new Byte((byte)(int)asNumber());
/* 496*/        if(class1 == Character.TYPE || class1 == (java.lang.Character.class))
                {
/* 497*/            String s = asString();
/* 498*/            if(s.length() == 1)
/* 499*/                return new Character(s.charAt(0));
/* 501*/            else
/* 501*/                throw new XPathException("Cannot convert string to Java char unless length is 1");
                } else
                {
/* 504*/            throw new XPathException("Conversion of node-set to " + class1.getName() + " is not supported");
                }
            }
}
