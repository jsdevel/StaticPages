// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   MessageEmitter.java

package com.icl.saxon.output;

import java.io.IOException;
import java.io.Writer;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.output:
//            XMLEmitter, Emitter

public class MessageEmitter extends XMLEmitter
{

            public MessageEmitter()
            {
            }

            public void endDocument()
                throws TransformerException
            {
/*  16*/        try
                {
/*  16*/            super.writer.write(10);
                }
/*  18*/        catch(IOException ioexception)
                {
/*  18*/            throw new TransformerException(ioexception);
                }
/*  20*/        super.endDocument();
            }
}
