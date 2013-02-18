// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TransformerHandlerImpl.java

package com.icl.saxon;

import com.icl.saxon.om.Builder;
import com.icl.saxon.om.DocumentPool;
import com.icl.saxon.output.Emitter;
import javax.xml.transform.*;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.SAXException;

// Referenced classes of package com.icl.saxon:
//            ContentEmitter, Controller

public class TransformerHandlerImpl extends ContentEmitter
    implements TransformerHandler
{

            Controller controller;
            Builder builder;
            Result result;
            String systemId;

            protected TransformerHandlerImpl(Controller controller1)
            {
/*  34*/        controller = controller1;
/*  35*/        setNamePool(controller1.getNamePool());
/*  36*/        builder = controller1.makeBuilder();
/*  37*/        builder.setNamePool(controller1.getNamePool());
/*  38*/        setEmitter(controller1.makeStripper(builder));
            }

            public Transformer getTransformer()
            {
/*  46*/        return controller;
            }

            public void setSystemId(String s)
            {
/*  54*/        systemId = s;
/*  55*/        builder.setSystemId(s);
            }

            public String getSystemId()
            {
/*  63*/        return systemId;
            }

            public void setResult(Result result1)
            {
/*  71*/        if(result1 == null)
                {
/*  72*/            throw new IllegalArgumentException("Result must not be null");
                } else
                {
/*  74*/            result = result1;
/*  75*/            return;
                }
            }

            public Result getResult()
            {
/*  82*/        return result;
            }

            public void endDocument()
                throws SAXException
            {
/*  92*/        super.endDocument();
/*  93*/        com.icl.saxon.om.DocumentInfo documentinfo = builder.getCurrentDocument();
/*  94*/        if(documentinfo == null)
/*  95*/            throw new SAXException("No source document has been built");
/*  97*/        controller.getDocumentPool().add(documentinfo, null);
/*  99*/        try
                {
/*  99*/            controller.transformDocument(documentinfo, result);
                }
/* 102*/        catch(TransformerException transformerexception)
                {
/* 102*/            throw new SAXException(transformerexception);
                }
            }
}
