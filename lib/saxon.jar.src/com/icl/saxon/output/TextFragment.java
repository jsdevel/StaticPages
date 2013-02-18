// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   TextFragment.java

package com.icl.saxon.output;

import java.util.Hashtable;
import java.util.Properties;

public class TextFragment
{

            private static Properties props;

            public TextFragment()
            {
            }

            public static Properties getProperties()
            {
/*  23*/        return props;
            }

            static 
            {
/*  12*/        props = new Properties();
/*  14*/        props.put("method", "text");
/*  15*/        props.put("indent", "no");
            }
}
