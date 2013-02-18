// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TEXTEmitter.java

package com.icl.saxon.output;

import com.icl.saxon.charcode.CharacterSet;
import com.icl.saxon.charcode.UnicodeCharacterSet;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import javax.xml.transform.TransformerException;
import org.xml.sax.Attributes;

// Referenced classes of package com.icl.saxon.output:
//            XMLEmitter, Emitter

public class TEXTEmitter extends XMLEmitter
{

            private String mediaType;

            public TEXTEmitter()
            {
/*  19*/        mediaType = "text/plain";
            }

            public void startDocument()
                throws TransformerException
            {
/*  27*/        String s = super.outputProperties.getProperty("media-type");
/*  28*/        if(s != null)
/*  29*/            mediaType = s;
/*  32*/        if(super.characterSet == null)
/*  33*/            super.characterSet = UnicodeCharacterSet.getInstance();
/*  35*/        super.empty = true;
            }

            public void characters(char ac[], int i, int j)
                throws TransformerException
            {
/*  48*/        for(int k = i; k < i + j; k++)
/*  49*/            if(!super.characterSet.inCharset(ac[k]))
/*  50*/                throw new TransformerException("Output character not available in this encoding (decimal " + (int)ac[k] + ")");

/*  54*/        try
                {
/*  54*/            super.writer.write(ac, i, j);
                }
/*  56*/        catch(IOException ioexception)
                {
/*  56*/            throw new TransformerException(ioexception);
                }
            }

            public void startElement(int i, Attributes attributes, int ai[], int j)
                throws TransformerException
            {
            }

            public void endElement(int i)
                throws TransformerException
            {
            }

            public void processingInstruction(String s, String s1)
                throws TransformerException
            {
            }

            public void comment(char ac[], int i, int j)
                throws TransformerException
            {
            }
}
