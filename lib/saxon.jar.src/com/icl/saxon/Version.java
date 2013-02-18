// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Version.java

package com.icl.saxon;


public class Version
{

            public static String javaVersion;
            public static boolean preJDK12;

            public Version()
            {
            }

            public static final boolean isPreJDK12()
            {
/*  16*/        return preJDK12;
            }

            public static final String getVersion()
            {
/*  20*/        return "6.5.5";
            }

            public static final double getXSLVersion()
            {
/*  24*/        return 1.0D;
            }

            public static final String getXSLVersionString()
            {
/*  28*/        return "1.0";
            }

            public static String getProductName()
            {
/*  32*/        return "SAXON " + getVersion() + " from Michael Kay";
            }

            public static String getWebSiteAddress()
            {
/*  36*/        return "http://saxon.sf.net/";
            }

            static 
            {
/*   9*/        javaVersion = System.getProperty("java.version");
/*  10*/        preJDK12 = javaVersion.startsWith("1.1") || javaVersion.startsWith("1.0") || javaVersion.startsWith("3.1.1 (Sun 1.1");
            }
}
