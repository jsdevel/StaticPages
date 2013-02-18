// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   KOI8RCharacterSet.java

package com.icl.saxon.charcode;


// Referenced classes of package com.icl.saxon.charcode:
//            CharacterSet

public class KOI8RCharacterSet
    implements CharacterSet
{

            public KOI8RCharacterSet()
            {
            }

            public final boolean inCharset(int i)
            {
/*  10*/        return i <= 127 || 1040 <= i && i <= 1103 || i == 1105 || i == 1025;
            }
}
