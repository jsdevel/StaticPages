// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Latin1CharacterSet.java

package com.icl.saxon.charcode;


// Referenced classes of package com.icl.saxon.charcode:
//            CharacterSet

public class Latin1CharacterSet
    implements CharacterSet
{

            public Latin1CharacterSet()
            {
            }

            public final boolean inCharset(int i)
            {
/*  10*/        return i <= 255;
            }
}
