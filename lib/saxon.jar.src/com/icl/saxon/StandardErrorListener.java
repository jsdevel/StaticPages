// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StandardErrorListener.java

package com.icl.saxon;

import java.io.PrintStream;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMLocator;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class StandardErrorListener
    implements ErrorListener
{

            int recoveryPolicy;
            int warningCount;
            PrintStream errorOutput;

            public StandardErrorListener()
            {
/*  16*/        recoveryPolicy = 1;
/*  17*/        warningCount = 0;
/*  18*/        errorOutput = System.err;
            }

            public void setErrorOutput(PrintStream printstream)
            {
/*  26*/        errorOutput = printstream;
            }

            public void setRecoveryPolicy(int i)
            {
/*  34*/        recoveryPolicy = i;
            }

            public void warning(TransformerException transformerexception)
                throws TransformerException
            {
/*  60*/        if(recoveryPolicy == 0)
/*  62*/            return;
/*  65*/        String s = "";
/*  66*/        if(transformerexception.getLocator() != null)
/*  67*/            s = getLocationMessage(transformerexception) + "\n  ";
/*  69*/        s = s + getExpandedMessage(transformerexception);
/*  71*/        if(recoveryPolicy == 1)
                {
/*  72*/            errorOutput.println("Recoverable error");
/*  73*/            errorOutput.println(s);
/*  74*/            warningCount++;
/*  75*/            if(warningCount > 25)
                    {
/*  76*/                System.err.println("No more warnings will be displayed");
/*  77*/                recoveryPolicy = 0;
/*  78*/                warningCount = 0;
                    }
                } else
                {
/*  81*/            errorOutput.println("Recoverable error");
/*  82*/            errorOutput.println(s);
/*  83*/            errorOutput.println("Processing terminated because error recovery is disabled");
/*  84*/            throw new TransformerException(transformerexception);
                }
            }

            public void error(TransformerException transformerexception)
                throws TransformerException
            {
/* 110*/        String s = "Error " + getLocationMessage(transformerexception) + "\n  " + getExpandedMessage(transformerexception);
/* 114*/        errorOutput.println(s);
            }

            public void fatalError(TransformerException transformerexception)
                throws TransformerException
            {
/* 136*/        error(transformerexception);
/* 137*/        throw transformerexception;
            }

            public static String getLocationMessage(TransformerException transformerexception)
            {
/* 145*/        SourceLocator sourcelocator = transformerexception.getLocator();
/* 146*/        if(sourcelocator == null)
/* 147*/            return "";
/* 149*/        String s = "";
/* 150*/        if(sourcelocator instanceof DOMLocator)
/* 151*/            s = s + "at " + ((DOMLocator)sourcelocator).getOriginatingNode().getNodeName() + " ";
/* 153*/        int i = sourcelocator.getLineNumber();
/* 154*/        int j = sourcelocator.getColumnNumber();
/* 155*/        if(i < 0 && j > 0)
                {
/* 156*/            s = s + "at byte " + j + " ";
                } else
                {
/* 158*/            s = s + "on line " + i + " ";
/* 159*/            if(sourcelocator.getColumnNumber() != -1)
/* 160*/                s = s + "column " + j + " ";
                }
/* 163*/        s = s + "of " + sourcelocator.getSystemId() + ":";
/* 164*/        return s;
            }

            public static String getExpandedMessage(TransformerException transformerexception)
            {
/* 173*/        String s = "";
/* 174*/        Object obj = transformerexception;
/* 176*/        do
                {
/* 176*/            if(obj == null)
/* 177*/                break;
/* 179*/            String s1 = ((Throwable) (obj)).getMessage();
/* 180*/            if(s1 == null)
/* 180*/                s1 = "";
/* 181*/            if(!s1.equals("TRaX Transform Exception") && !s.endsWith(s1))
                    {
/* 182*/                if(!s.equals(""))
/* 183*/                    s = s + ": ";
/* 185*/                s = s + ((Throwable) (obj)).getMessage();
                    }
/* 187*/            if(obj instanceof TransformerException)
                    {
/* 188*/                obj = ((TransformerException)obj).getException();
/* 188*/                continue;
                    }
/* 189*/            if(!(obj instanceof SAXException))
/* 190*/                break;
/* 190*/            obj = ((SAXException)obj).getException();
                } while(true);
/* 196*/        return s;
            }
}
