// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   PluggableCharacterSet.java

package com.icl.saxon.charcode;


// Referenced classes of package com.icl.saxon.charcode:
//            CharacterSet

public interface PluggableCharacterSet
    extends CharacterSet
{

    public abstract String getEncodingName();
}
