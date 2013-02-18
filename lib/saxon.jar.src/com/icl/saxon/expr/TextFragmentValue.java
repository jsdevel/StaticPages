// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TextFragmentValue.java

package com.icl.saxon.expr;

import com.icl.saxon.*;
import com.icl.saxon.om.*;
import com.icl.saxon.output.Emitter;
import com.icl.saxon.output.Outputter;
import com.icl.saxon.tinytree.TinyBuilder;
import java.io.PrintStream;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.expr:
//            SingletonNodeSet, XPathException, StringValue, Value, 
//            Expression

public final class TextFragmentValue extends SingletonNodeSet
{

            private String text;
            private String baseURI;
            private Controller controller;

            public TextFragmentValue(String s, String s1, Controller controller1)
            {
/*  26*/        text = s;
/*  27*/        super.node = null;
/*  28*/        baseURI = s1;
/*  29*/        controller = controller1;
/*  30*/        super.generalUseAllowed = false;
            }

            public String asString()
            {
/*  38*/        return text;
            }

            public void outputStringValue(Outputter outputter, Context context)
                throws TransformerException
            {
/*  49*/        outputter.writeContent(text);
            }

            public double asNumber()
            {
/*  57*/        return Value.stringToNumber(text);
            }

            public boolean asBoolean()
            {
/*  65*/        return true;
            }

            public int getCount()
            {
/*  73*/        return 1;
            }

            public Expression simplify()
            {
/*  82*/        return this;
            }

            public NodeInfo getFirst()
            {
/*  91*/        return getRootNode();
            }

            public NodeEnumeration enumerate()
                throws XPathException
            {
/*  99*/        if(!super.generalUseAllowed)
/* 100*/            throw new XPathException("Cannot process a result tree fragment as a node-set under XSLT 1.0");
/* 102*/        else
/* 102*/            return new SingletonEnumeration(getRootNode());
            }

            public boolean equals(Value value)
                throws XPathException
            {
/* 110*/        if(value instanceof StringValue)
/* 111*/            return text.equals(value.asString());
/* 113*/        else
/* 113*/            return (new StringValue(text)).equals(value);
            }

            public boolean notEquals(Value value)
                throws XPathException
            {
/* 121*/        return (new StringValue(text)).notEquals(value);
            }

            public boolean compare(int i, Value value)
                throws XPathException
            {
/* 129*/        return (new StringValue(text)).compare(i, value);
            }

            public int getType()
            {
/* 138*/        return 4;
            }

            public int getDataType()
            {
/* 147*/        return 4;
            }

            public DocumentInfo getRootNode()
            {
/* 155*/        if(super.node != null)
/* 156*/            return (DocumentInfo)super.node;
/* 159*/        try
                {
/* 159*/            int i = text.length();
/* 160*/            char ac[] = new char[i];
/* 161*/            text.getChars(0, i, ac, 0);
/* 162*/            TinyBuilder tinybuilder = new TinyBuilder();
/* 163*/            tinybuilder.setSystemId(baseURI);
/* 164*/            tinybuilder.setNamePool(controller.getNamePool());
/* 165*/            tinybuilder.startDocument();
/* 166*/            tinybuilder.characters(ac, 0, i);
/* 167*/            tinybuilder.endDocument();
/* 168*/            super.node = tinybuilder.getCurrentDocument();
/* 169*/            controller.getDocumentPool().add((DocumentInfo)super.node, null);
/* 170*/            return (DocumentInfo)super.node;
                }
/* 172*/        catch(TransformerException transformerexception)
                {
/* 172*/            throw new InternalSaxonError("Error building temporary tree: " + transformerexception.getMessage());
                }
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/* 181*/        outputter.writeContent(text);
            }

            public void display(int i)
            {
/* 189*/        System.err.println(Expression.indent(i) + "** result tree fragment ** (" + text + ")");
            }
}
