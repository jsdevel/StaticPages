// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Name.java

package com.icl.saxon.om;


// Referenced classes of package com.icl.saxon.om:
//            XMLChar

public abstract class Name
{

            public Name()
            {
            }

            public static boolean isNCName(String s)
            {
/*  18*/        return XMLChar.isValidNCName(s);
            }

            public static boolean isQName(String s)
            {
/*  26*/        int i = s.indexOf(':');
/*  27*/        if(i < 0)
/*  27*/            return isNCName(s);
/*  28*/        if(i == 0 || i == s.length() - 1)
/*  28*/            return false;
/*  29*/        if(!isNCName(s.substring(0, i)))
/*  29*/            return false;
/*  30*/        return isNCName(s.substring(i + 1));
            }

            public static final String getPrefix(String s)
            {
/*  39*/        int i = s.indexOf(':');
/*  40*/        if(i < 0)
/*  41*/            return "";
/*  43*/        else
/*  43*/            return s.substring(0, i);
            }

            public static final String getLocalName(String s)
            {
/*  51*/        int i = s.indexOf(':');
/*  52*/        if(i < 0)
/*  53*/            return s;
/*  55*/        else
/*  55*/            return s.substring(i + 1);
            }
}
