// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   FragmentValue.java

package com.icl.saxon.expr;

import com.icl.saxon.*;
import com.icl.saxon.om.*;
import com.icl.saxon.output.Emitter;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.TreeBuilder;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

// Referenced classes of package com.icl.saxon.expr:
//            SingletonNodeSet, XPathException, StringValue, Value, 
//            Expression

public final class FragmentValue extends SingletonNodeSet
{
    private class FragmentEmitter extends Emitter
    {

                boolean previousCharacters;

                public void startDocument()
                {
/* 293*/            previousCharacters = false;
                }

                public void endDocument()
                {
/* 301*/            previousCharacters = false;
                }

                public void startElement(int i, Attributes attributes, int ai[], int j)
                {
/* 315*/            events.addElement(FragmentValue.START_ELEMENT);
/* 316*/            events.addElement(new Integer(i));
/* 322*/            int k = attributes.getLength();
                    AttributeCollection attributecollection;
/* 323*/            if(k == 0)
/* 324*/                attributecollection = FragmentValue.emptyAttributeCollection;
/* 326*/            else
/* 326*/                attributecollection = new AttributeCollection((AttributeCollection)attributes);
/* 329*/            events.addElement(attributecollection);
/* 332*/            int ai1[] = new int[j];
/* 333*/            System.arraycopy(ai, 0, ai1, 0, j);
/* 334*/            events.addElement(ai1);
/* 336*/            previousCharacters = false;
                }

                public void endElement(int i)
                {
/* 346*/            events.addElement(FragmentValue.END_ELEMENT);
/* 347*/            events.addElement(new Integer(i));
/* 348*/            previousCharacters = false;
                }

                public void characters(char ac[], int i, int j)
                {
                    char ac1[];
/* 357*/            for(; used + j >= buffer.length; buffer = ac1)
                    {
/* 357*/                ac1 = new char[buffer.length * 2];
/* 358*/                System.arraycopy(buffer, 0, ac1, 0, used);
                    }

/* 361*/            System.arraycopy(ac, i, buffer, used, j);
/* 362*/            if(previousCharacters)
                    {
/* 364*/                int ai[] = (int[])events.elementAt(events.size() - 1);
/* 365*/                ai[1] += j;
                    } else
                    {
/* 367*/                events.addElement(FragmentValue.CHARACTERS);
/* 368*/                int ai1[] = {
/* 368*/                    used, j
                        };
/* 369*/                events.addElement(ai1);
                    }
/* 371*/            used+= = j;
/* 372*/            previousCharacters = true;
                }

                public void processingInstruction(String s, String s1)
                {
/* 380*/            events.addElement(FragmentValue.PROCESSING_INSTRUCTION);
/* 381*/            events.addElement(s);
/* 382*/            events.addElement(s1);
/* 383*/            previousCharacters = false;
                }

                public void comment(char ac[], int i, int j)
                {
/* 391*/            events.addElement(FragmentValue.COMMENT);
/* 392*/            events.addElement(new String(ac, i, j));
/* 393*/            previousCharacters = false;
                }

                public void setEscaping(boolean flag)
                    throws TransformerException
                {
/* 402*/            events.addElement(flag ? ((Object) (FragmentValue.ESCAPING_ON)) : ((Object) (FragmentValue.ESCAPING_OFF)));
/* 403*/            previousCharacters = false;
                }

                private FragmentEmitter()
                {
/* 286*/            previousCharacters = false;
                }

    }


            private char buffer[];
            private int used;
            private Vector events;
            private String baseURI;
            private FragmentEmitter emitter;
            private Controller controller;
            private static AttributeCollection emptyAttributeCollection = new AttributeCollection((NamePool)null);
            private static Integer START_ELEMENT = new Integer(1);
            private static Integer END_ELEMENT = new Integer(2);
            private static Integer CHARACTERS = new Integer(5);
            private static Integer PROCESSING_INSTRUCTION = new Integer(6);
            private static Integer COMMENT = new Integer(7);
            private static Integer ESCAPING_ON = new Integer(8);
            private static Integer ESCAPING_OFF = new Integer(9);

            public FragmentValue(Controller controller1)
            {
/*  25*/        buffer = new char[4096];
/*  26*/        used = 0;
/*  27*/        events = new Vector(20);
/*  28*/        baseURI = null;
/*  29*/        emitter = new FragmentEmitter();
/*  43*/        controller = controller1;
/*  44*/        super.generalUseAllowed = false;
            }

            public void setBaseURI(String s)
            {
/*  53*/        baseURI = s;
            }

            public Emitter getEmitter()
            {
/*  69*/        return emitter;
            }

            public String asString()
            {
/*  77*/        return new String(buffer, 0, used);
            }

            public void outputStringValue(Outputter outputter, Context context)
                throws TransformerException
            {
/*  88*/        outputter.writeContent(buffer, 0, used);
            }

            public double asNumber()
            {
/*  96*/        return Value.stringToNumber(asString());
            }

            public boolean asBoolean()
            {
/* 104*/        return true;
            }

            public int getCount()
            {
/* 112*/        return 1;
            }

            public Expression simplify()
            {
/* 121*/        return this;
            }

            public NodeInfo getFirst()
            {
/* 130*/        return getRootNode();
            }

            public NodeEnumeration enumerate()
                throws XPathException
            {
/* 138*/        if(!super.generalUseAllowed)
/* 139*/            throw new XPathException("Cannot process a result tree fragment as a node-set under XSLT 1.0");
/* 141*/        else
/* 141*/            return new SingletonEnumeration(getRootNode());
            }

            public boolean equals(Value value)
                throws XPathException
            {
/* 149*/        if(value instanceof StringValue)
/* 150*/            return asString().equals(value.asString());
/* 152*/        else
/* 152*/            return (new StringValue(asString())).equals(value);
            }

            public boolean notEquals(Value value)
                throws XPathException
            {
/* 160*/        return (new StringValue(asString())).notEquals(value);
            }

            public boolean compare(int i, Value value)
                throws XPathException
            {
/* 168*/        return (new StringValue(asString())).compare(i, value);
            }

            public int getType()
            {
/* 177*/        return 4;
            }

            public int getDataType()
            {
/* 186*/        return 4;
            }

            public DocumentInfo getRootNode()
            {
/* 194*/        if(super.node != null)
/* 195*/            return (DocumentInfo)super.node;
/* 198*/        try
                {
/* 198*/            TreeBuilder treebuilder = new TreeBuilder();
/* 199*/            treebuilder.setSystemId(baseURI);
/* 200*/            treebuilder.setNamePool(controller.getNamePool());
/* 201*/            treebuilder.startDocument();
/* 202*/            replay(treebuilder);
/* 203*/            treebuilder.endDocument();
/* 204*/            super.node = treebuilder.getCurrentDocument();
/* 205*/            controller.getDocumentPool().add((DocumentInfo)super.node, null);
/* 206*/            return (DocumentInfo)super.node;
                }
/* 208*/        catch(TransformerException transformerexception)
                {
/* 208*/            throw new InternalSaxonError("Error building temporary tree: " + transformerexception.getMessage());
                }
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/* 217*/        Emitter emitter1 = outputter.getEmitter();
/* 218*/        replay(emitter1);
            }

            public void replay(Emitter emitter1)
                throws TransformerException
            {
/* 226*/        for(Enumeration enumeration = events.elements(); enumeration.hasMoreElements();)
                {
/* 229*/            Object obj = enumeration.nextElement();
/* 233*/            if(obj == START_ELEMENT)
                    {
/* 234*/                Object obj1 = enumeration.nextElement();
/* 235*/                Object obj6 = enumeration.nextElement();
/* 236*/                Object obj8 = enumeration.nextElement();
/* 237*/                int ai[] = (int[])obj8;
/* 238*/                emitter1.startElement(((Integer)obj1).intValue(), (AttributeCollection)obj6, ai, ai.length);
                    } else
/* 242*/            if(obj == END_ELEMENT)
                    {
/* 243*/                Object obj2 = enumeration.nextElement();
/* 244*/                emitter1.endElement(((Integer)obj2).intValue());
                    } else
/* 246*/            if(obj == CHARACTERS)
                    {
/* 247*/                Object obj3 = enumeration.nextElement();
/* 248*/                emitter1.characters(buffer, ((int[])obj3)[0], ((int[])obj3)[1]);
                    } else
/* 250*/            if(obj == PROCESSING_INSTRUCTION)
                    {
/* 251*/                Object obj4 = enumeration.nextElement();
/* 252*/                Object obj7 = enumeration.nextElement();
/* 253*/                emitter1.processingInstruction((String)obj4, (String)obj7);
                    } else
/* 255*/            if(obj == COMMENT)
                    {
/* 256*/                Object obj5 = enumeration.nextElement();
/* 257*/                emitter1.comment(((String)obj5).toCharArray(), 0, ((String)obj5).length());
                    } else
/* 259*/            if(obj == ESCAPING_ON)
/* 260*/                emitter1.setEscaping(true);
/* 262*/            else
/* 262*/            if(obj == ESCAPING_OFF)
/* 263*/                emitter1.setEscaping(false);
/* 266*/            else
/* 266*/                throw new InternalSaxonError("Corrupt data in temporary tree: " + obj);
                }

            }

            public void display(int i)
            {
/* 277*/        System.err.println(Expression.indent(i) + "** result tree fragment **");
            }














}
