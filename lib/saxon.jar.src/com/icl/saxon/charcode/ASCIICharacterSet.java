// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ASCIICharacterSet.java

package com.icl.saxon.charcode;


// Referenced classes of package com.icl.saxon.charcode:
//            CharacterSet

public class ASCIICharacterSet
    implements CharacterSet
{

            public ASCIICharacterSet()
            {
            }

            public final boolean inCharset(int i)
            {
/*  10*/        return i <= 127;
            }
}
