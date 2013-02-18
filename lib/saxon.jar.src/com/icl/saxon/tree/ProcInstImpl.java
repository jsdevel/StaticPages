// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ProcInstImpl.java

package com.icl.saxon.tree;

import com.icl.saxon.om.AbstractNode;
import com.icl.saxon.output.Outputter;
import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;
import org.w3c.dom.ProcessingInstruction;

// Referenced classes of package com.icl.saxon.tree:
//            NodeImpl

class ProcInstImpl extends NodeImpl
    implements ProcessingInstruction
{

            String content;
            int nameCode;
            String systemId;
            int lineNumber;

            public ProcInstImpl(int i, String s)
            {
/*  20*/        lineNumber = -1;
/*  23*/        nameCode = i;
/*  24*/        content = s;
            }

            public int getNameCode()
            {
/*  32*/        return nameCode;
            }

            public String getStringValue()
            {
/*  36*/        return content;
            }

            public final short getNodeType()
            {
/*  40*/        return 7;
            }

            public void setLocation(String s, int i)
            {
/*  48*/        systemId = s;
/*  49*/        lineNumber = i;
            }

            public String getSystemId()
            {
/*  57*/        return systemId;
            }

            public int getLineNumber()
            {
/*  65*/        return lineNumber;
            }

            public void copy(Outputter outputter)
                throws TransformerException
            {
/*  73*/        outputter.writePI(getLocalName(), content);
            }

            public String getTarget()
            {
/*  85*/        return getLocalName();
            }

            public String getData()
            {
/*  95*/        return content;
            }

            public void setData(String s)
                throws DOMException
            {
/* 105*/        disallowUpdate();
            }
}
