// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Loader.java

package com.icl.saxon;

import java.io.PrintStream;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon:
//            Version

public class Loader
{

            private static boolean tracing = false;

            public Loader()
            {
            }

            public static synchronized void setTracing(boolean flag)
            {
/*  29*/        tracing = flag;
            }

            public static Class getClass(String s)
                throws TransformerException
            {
/*  45*/        if(tracing)
/*  46*/            System.err.println("Loading " + s);
/*  48*/        if(Version.isPreJDK12())
/*  50*/            try
                    {
/*  50*/                return Class.forName(s);
                    }
/*  53*/            catch(Exception exception)
                    {
/*  53*/                throw new TransformerException("Failed to load " + s, exception);
                    }
/*  58*/        try
                {
/*  58*/            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
/*  59*/            if(classloader != null)
/*  61*/                try
                        {
/*  61*/                    return classloader.loadClass(s);
                        }
/*  63*/                catch(Exception exception2)
                        {
/*  63*/                    return Class.forName(s);
                        }
/*  66*/            else
/*  66*/                return Class.forName(s);
                }
/*  70*/        catch(Exception exception1)
                {
/*  70*/            throw new TransformerException("Failed to load " + s, exception1);
                }
            }

            public static Object getInstance(String s)
                throws TransformerException
            {
/*  89*/        Class class1 = getClass(s);
/*  91*/        try
                {
/*  91*/            return class1.newInstance();
                }
/*  93*/        catch(Exception exception)
                {
/*  93*/            throw new TransformerException("Failed to instantiate class " + s, exception);
                }
            }

}
