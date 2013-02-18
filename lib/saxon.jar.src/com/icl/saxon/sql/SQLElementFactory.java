// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SQLElementFactory.java

package com.icl.saxon.sql;

import com.icl.saxon.style.ExtensionElementFactory;

public class SQLElementFactory
    implements ExtensionElementFactory
{

            public SQLElementFactory()
            {
            }

            public Class getExtensionClass(String s)
            {
/*  20*/        if(s.equals("connect"))
/*  20*/            return com.icl.saxon.sql.SQLConnect.class;
/*  21*/        if(s.equals("insert"))
/*  21*/            return com.icl.saxon.sql.SQLInsert.class;
/*  22*/        if(s.equals("column"))
/*  22*/            return com.icl.saxon.sql.SQLColumn.class;
/*  23*/        if(s.equals("close"))
/*  23*/            return com.icl.saxon.sql.SQLClose.class;
/*  24*/        else
/*  24*/            return null;
            }
}
