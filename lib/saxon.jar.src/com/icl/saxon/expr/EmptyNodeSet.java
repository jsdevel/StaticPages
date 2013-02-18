// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   EmptyNodeSet.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.om.*;

// Referenced classes of package com.icl.saxon.expr:
//            NodeSetValue, BooleanValue, Value

public final class EmptyNodeSet extends NodeSetValue
{

            private static NodeInfo emptyArray[] = new NodeInfo[0];

            public EmptyNodeSet()
            {
            }

            public Value evaluate(Context context)
            {
/*  19*/        return this;
            }

            public NodeSetValue evaluateAsNodeSet(Context context)
            {
/*  29*/        return this;
            }

            public void setSorted(boolean flag)
            {
            }

            public boolean isSorted()
            {
/*  48*/        return true;
            }

            public boolean isContextDocumentNodeSet()
            {
/*  58*/        return true;
            }

            public String asString()
            {
/*  67*/        return "";
            }

            public boolean asBoolean()
            {
/*  76*/        return false;
            }

            public int getCount()
            {
/*  85*/        return 0;
            }

            public NodeSetValue sort()
            {
/*  97*/        return this;
            }

            public NodeInfo getFirst()
            {
/* 106*/        return null;
            }

            public boolean equals(Value value)
            {
/* 115*/        if(value instanceof BooleanValue)
/* 116*/            return !((BooleanValue)value).asBoolean();
/* 118*/        else
/* 118*/            return false;
            }

            public boolean notEquals(Value value)
            {
/* 127*/        if(value instanceof BooleanValue)
/* 128*/            return ((BooleanValue)value).asBoolean();
/* 130*/        else
/* 130*/            return false;
            }

            public NodeEnumeration enumerate()
            {
/* 139*/        return EmptyEnumeration.getInstance();
            }

}
