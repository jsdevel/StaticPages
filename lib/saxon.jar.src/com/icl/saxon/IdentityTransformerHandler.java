// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   IdentityTransformerHandler.java

package com.icl.saxon;

import com.icl.saxon.output.GeneralOutputter;
import com.icl.saxon.output.Outputter;
import javax.xml.transform.*;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

// Referenced classes of package com.icl.saxon:
//            ContentEmitter, Controller

public class IdentityTransformerHandler extends ContentEmitter
    implements TransformerHandler
{

            Result result;
            String systemId;
            Controller controller;
            GeneralOutputter outputter;

            protected IdentityTransformerHandler(Controller controller1)
            {
/*  37*/        controller = controller1;
/*  38*/        setNamePool(controller1.getNamePool());
            }

            public Transformer getTransformer()
            {
/*  47*/        return controller;
            }

            public void setSystemId(String s)
            {
/*  55*/        systemId = s;
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

            public void startDocument()
                throws SAXException
            {
/*  90*/        if(result == null)
/*  91*/            result = new StreamResult(System.out);
/*  94*/        try
                {
/*  94*/            com.icl.saxon.om.NamePool namepool = controller.getNamePool();
/*  95*/            java.util.Properties properties = controller.getOutputProperties();
/*  96*/            outputter = new GeneralOutputter(namepool);
/*  97*/            outputter.setOutputDestination(properties, result);
/*  98*/            com.icl.saxon.output.Emitter emitter = outputter.getEmitter();
/*  99*/            setNamePool(namepool);
/* 100*/            setEmitter(emitter);
                }
/* 102*/        catch(TransformerException transformerexception)
                {
/* 102*/            throw new SAXException(transformerexception);
                }
/* 104*/        super.startDocument();
            }

            public void endDocument()
                throws SAXException
            {
/* 113*/        try
                {
/* 113*/            outputter.close();
                }
/* 115*/        catch(TransformerException transformerexception)
                {
/* 115*/            throw new SAXException(transformerexception);
                }
            }
}
