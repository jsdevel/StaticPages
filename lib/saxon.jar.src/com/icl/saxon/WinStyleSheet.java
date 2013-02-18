// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   WinStyleSheet.java

package com.icl.saxon;

import java.util.Hashtable;
import java.util.Properties;

// Referenced classes of package com.icl.saxon:
//            StyleSheet

public class WinStyleSheet extends StyleSheet
{

            public WinStyleSheet()
            {
            }

            public static void main(String args[])
                throws Exception
            {
/*  33*/        Properties properties = System.getProperties();
/*  34*/        Properties properties1 = new Properties(properties);
/*  35*/        properties1.put("javax.xml.parsers.SAXParserFactory", "com.icl.saxon.aelfred.SAXParserFactoryImpl");
/*  37*/        properties1.put("javax.xml.transform.TransformerFactory", "com.icl.saxon.TransformerFactoryImpl");
/*  39*/        System.setProperties(properties1);
/*  40*/        WinStyleSheet winstylesheet = new WinStyleSheet();
/*  41*/        winstylesheet.doMain(args, winstylesheet, "saxon");
            }
}
