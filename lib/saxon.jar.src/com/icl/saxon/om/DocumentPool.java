// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   DocumentPool.java

package com.icl.saxon.om;

import java.util.Hashtable;

// Referenced classes of package com.icl.saxon.om:
//            DocumentInfo

public final class DocumentPool
{

            private Hashtable documentNameMap;
            private Hashtable documentNumberMap;
            private int numberOfDocuments;

            public DocumentPool()
            {
/*  21*/        documentNameMap = new Hashtable(10);
/*  22*/        documentNumberMap = new Hashtable(10);
/*  23*/        numberOfDocuments = 0;
            }

            public int add(DocumentInfo documentinfo, String s)
            {
/*  36*/        Integer integer = new Integer(documentinfo.hashCode());
/*  37*/        Integer integer1 = (Integer)documentNumberMap.get(integer);
/*  38*/        if(integer1 != null)
/*  39*/            return integer1.intValue();
/*  41*/        if(s != null)
/*  42*/            documentNameMap.put(s, documentinfo);
/*  44*/        int i = numberOfDocuments++;
/*  45*/        documentNumberMap.put(integer, new Integer(i));
/*  46*/        return i;
            }

            public int getDocumentNumber(DocumentInfo documentinfo)
            {
/*  61*/        Integer integer = new Integer(documentinfo.hashCode());
/*  62*/        Integer integer1 = (Integer)documentNumberMap.get(integer);
/*  63*/        if(integer1 == null)
                {
/*  64*/            int i = numberOfDocuments++;
/*  65*/            integer1 = new Integer(i);
/*  66*/            documentNumberMap.put(integer, integer1);
                }
/*  68*/        return integer1.intValue();
            }

            public DocumentInfo find(String s)
            {
/*  78*/        return (DocumentInfo)documentNameMap.get(s);
            }

            public int getNumberOfDocuments()
            {
/*  86*/        return numberOfDocuments;
            }
}
