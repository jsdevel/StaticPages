// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Stripper.java

package com.icl.saxon.om;

import com.icl.saxon.*;
import com.icl.saxon.output.ProxyEmitter;
import com.icl.saxon.tree.ElementImpl;
import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

// Referenced classes of package com.icl.saxon.om:
//            NamePool

public class Stripper extends ProxyEmitter
{
    private class DummyElement extends ElementImpl
    {

                public short getURICode()
                {
/* 223*/            return namePool.getURICode(getNameCode());
                }

                private DummyElement()
                {
                }

    }


            private boolean preserveAll;
            private boolean stripAll;
            private byte stripStack[];
            private int top;
            private Mode stripperMode;
            private DummyElement element;
            private Context context;
            private NamePool namePool;

            protected Stripper()
            {
/*  34*/        stripStack = new byte[100];
/*  35*/        top = 0;
/*  43*/        element = new DummyElement();
            }

            public Stripper(Mode mode)
            {
/*  34*/        stripStack = new byte[100];
/*  35*/        top = 0;
/*  43*/        element = new DummyElement();
/*  65*/        stripperMode = mode;
/*  66*/        preserveAll = mode == null;
/*  67*/        stripAll = false;
            }

            public void setPreserveAll()
            {
/*  75*/        preserveAll = true;
/*  76*/        stripAll = false;
            }

            public boolean getPreserveAll()
            {
/*  85*/        return preserveAll;
            }

            public void setStripAll()
            {
/*  93*/        preserveAll = false;
/*  94*/        stripAll = true;
            }

            public boolean getStripAll()
            {
/* 103*/        return stripAll;
            }

            public void setController(Controller controller)
            {
/* 112*/        context = controller.makeContext(element);
/* 113*/        namePool = controller.getNamePool();
            }

            public boolean isSpacePreserving(int i)
            {
/* 125*/        try
                {
/* 125*/            if(preserveAll)
/* 125*/                return true;
/* 126*/            if(stripAll)
/* 126*/                return false;
/* 127*/            element.setNameCode(i);
/* 128*/            Object obj = stripperMode.getRule(element, context);
/* 129*/            if(obj == null)
/* 129*/                return true;
/* 130*/            else
/* 130*/                return ((Boolean)obj).booleanValue();
                }
/* 132*/        catch(TransformerException transformerexception)
                {
/* 132*/            return true;
                }
            }

            public void startDocument()
                throws TransformerException
            {
/* 143*/        top = 0;
/* 144*/        stripStack[top] = 1;
/* 145*/        super.startDocument();
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
/* 156*/        super.startElement(i, attributes, ai, j);
/* 158*/        byte byte0 = stripStack[top];
/* 160*/        String s = attributes.getValue("http://www.w3.org/XML/1998/namespace", "space");
/* 161*/        byte byte1 = (byte)(byte0 & 2);
/* 162*/        if(s != null)
                {
/* 163*/            if(s.equals("preserve"))
/* 163*/                byte1 = 2;
/* 164*/            if(s.equals("default"))
/* 164*/                byte1 = 0;
                }
/* 166*/        if(isSpacePreserving(i))
/* 167*/            byte1 |= 1;
/* 172*/        top++;
/* 173*/        if(top >= stripStack.length)
                {
/* 174*/            byte abyte0[] = new byte[top * 2];
/* 175*/            System.arraycopy(stripStack, 0, abyte0, 0, top);
/* 176*/            stripStack = abyte0;
                }
/* 178*/        stripStack[top] = byte1;
            }

            public void endElement(int i)
                throws TransformerException
            {
/* 187*/        super.endElement(i);
/* 188*/        top--;
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/* 199*/        if(j > 0 && (stripStack[top] != 0 || !isWhite(ac, i, j)))
/* 201*/            super.characters(ac, i, j);
            }

            private boolean isWhite(char ac[], int i, int j)
            {
/* 211*/        for(int k = i; k < i + j; k++)
/* 212*/            if(ac[k] > ' ')
/* 213*/                return false;

/* 216*/        return true;
            }

}
