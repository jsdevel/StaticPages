// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ExtendedInputSource.java

package com.icl.saxon;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import org.xml.sax.InputSource;

public class ExtendedInputSource extends InputSource
{

            private int estimatedLength;

            public ExtendedInputSource()
            {
/*  30*/        estimatedLength = -1;
            }

            public ExtendedInputSource(String s)
            {
/*  45*/        super(s);
/*  30*/        estimatedLength = -1;
            }

            public ExtendedInputSource(Reader reader)
            {
/*  53*/        super(reader);
/*  30*/        estimatedLength = -1;
            }

            public ExtendedInputSource(InputStream inputstream)
            {
/*  61*/        super(inputstream);
/*  30*/        estimatedLength = -1;
            }

            public ExtendedInputSource(File file)
            {
/*  30*/        estimatedLength = -1;
/*  76*/        setFile(file);
            }

            public ExtendedInputSource(InputSource inputsource)
            {
/*  30*/        estimatedLength = -1;
/*  84*/        setSystemId(inputsource.getSystemId());
/*  85*/        setPublicId(inputsource.getPublicId());
/*  86*/        setByteStream(inputsource.getByteStream());
/*  87*/        setEncoding(inputsource.getEncoding());
/*  88*/        setCharacterStream(inputsource.getCharacterStream());
            }

            public void setFile(File file)
            {
/*  96*/        super.setSystemId(createURL(file));
            }

            public void setEstimatedLength(int i)
            {
/* 104*/        estimatedLength = i;
            }

            public int getEstimatedLength()
            {
/* 112*/        return estimatedLength;
            }

            public static String createURL(File file)
            {
/* 121*/        String s = file.getAbsolutePath();
/* 123*/        do
                {
/* 123*/            int i = s.indexOf('#');
/* 124*/            if(i < 0)
/* 125*/                break;
/* 125*/            s = s.substring(0, i) + "%23" + s.substring(i + 1);
                } while(true);
/* 133*/        URL url = null;
/* 136*/        try
                {
/* 136*/            url = new URL(s);
                }
/* 140*/        catch(MalformedURLException malformedurlexception)
                {
/* 145*/            try
                    {
/* 145*/                String s1 = System.getProperty("file.separator");
/* 146*/                if(s1.length() == 1)
                        {
/* 148*/                    char c = s1.charAt(0);
/* 149*/                    if(c != '/')
/* 150*/                        s = s.replace(c, '/');
/* 151*/                    if(s.charAt(0) != '/')
/* 152*/                        s = '/' + s;
                        }
/* 154*/                s = "file://" + s;
/* 155*/                url = new URL(s);
                    }
/* 159*/            catch(MalformedURLException malformedurlexception1)
                    {
/* 159*/                return null;
                    }
                }
/* 162*/        return url.toString();
            }
}
