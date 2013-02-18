// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DocumentInfo.java

package com.icl.saxon.om;

import com.icl.saxon.KeyManager;
import java.util.Hashtable;

// Referenced classes of package com.icl.saxon.om:
//            NodeInfo, NamePool

public interface DocumentInfo
    extends NodeInfo
{

    public abstract void setNamePool(NamePool namepool);

    public abstract NamePool getNamePool();

    public abstract NodeInfo selectID(String s);

    public abstract Hashtable getKeyIndex(KeyManager keymanager, int i);

    public abstract void setKeyIndex(KeyManager keymanager, int i, Hashtable hashtable);

    public abstract String getUnparsedEntity(String s);
}
