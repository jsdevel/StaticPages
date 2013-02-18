// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   CP1251CharacterSet.java

package com.icl.saxon.charcode;


// Referenced classes of package com.icl.saxon.charcode:
//            CharacterSet

public class CP1251CharacterSet
    implements CharacterSet
{

            public CP1251CharacterSet()
            {
            }

            public final boolean inCharset(int i)
            {
/*  11*/        return i <= 127 || i >= 1025 && i <= 1103 || i >= 1105 && i <= 1119 || i == 1168 || i == 1169 || i == 8211 || i == 8212 || i == 8216 || i == 8217 || i == 8218 || i == 8220 || i == 8221 || i == 8222 || i == 8224 || i == 8225 || i == 8226 || i == 8230 || i == 8240 || i == 8249 || i == 8250 || i == 8364 || i == 8470 || i == 8482;
            }
}
