// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TinyParentNodeImpl.java

package com.icl.saxon.tinytree;

import com.icl.saxon.output.Outputter;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.tinytree:
//            TinyNodeImpl, TinyDocumentImpl

abstract class TinyParentNodeImpl extends TinyNodeImpl
{

            TinyParentNodeImpl()
            {
            }

            public boolean hasChildNodes()
            {
/*  21*/        return super.nodeNr + 1 < super.document.numberOfNodes && super.document.depth[super.nodeNr + 1] > super.document.depth[super.nodeNr];
            }

            public String getStringValue()
            {
/*  32*/        short word0 = super.document.depth[super.nodeNr];
/*  33*/        StringBuffer stringbuffer = null;
/*  38*/        for(int i = super.nodeNr + 1; i < super.document.numberOfNodes && super.document.depth[i] > word0; i++)
/*  40*/            if(super.document.nodeType[i] == 3)
                    {
/*  41*/                if(stringbuffer == null)
/*  42*/                    stringbuffer = new StringBuffer();
/*  44*/                int j = super.document.length[i];
/*  45*/                int k = super.document.offset[i];
/*  46*/                stringbuffer.append(super.document.charBuffer, k, j);
                    }

/*  50*/        if(stringbuffer == null)
/*  50*/            return "";
/*  51*/        else
/*  51*/            return stringbuffer.toString();
            }

            public void copyStringValue(Outputter outputter)
                throws TransformerException
            {
/*  59*/        short word0 = super.document.depth[super.nodeNr];
/*  64*/        for(int i = super.nodeNr + 1; i < super.document.numberOfNodes && super.document.depth[i] > word0; i++)
/*  66*/            if(super.document.nodeType[i] == 3)
/*  67*/                outputter.writeContent(super.document.charBuffer, super.document.offset[i], super.document.length[i]);

            }
}
