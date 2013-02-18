// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   UnicodeCharacterSet.java

package com.icl.saxon.charcode;


// Referenced classes of package com.icl.saxon.charcode:
//            CharacterSet

public final class UnicodeCharacterSet
    implements CharacterSet
{

            private static UnicodeCharacterSet theInstance = new UnicodeCharacterSet();

            public UnicodeCharacterSet()
            {
            }

            public static UnicodeCharacterSet getInstance()
            {
/*  12*/        return theInstance;
            }

            public boolean inCharset(int i)
            {
/*  16*/        return true;
            }

}
