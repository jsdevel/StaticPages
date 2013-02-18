// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   AttributeCollection.java

package com.icl.saxon.tree;

import com.icl.saxon.om.Name;
import com.icl.saxon.om.NamePool;
import org.xml.sax.Attributes;

public final class AttributeCollection
    implements Attributes
{

            private NamePool namePool;
            private Object list[];
            private int used;
            private static int RECSIZE = 3;
            private static int NAMECODE = 0;
            private static int TYPE = 1;
            private static int VALUE = 2;

            public AttributeCollection(NamePool namepool)
            {
/*  23*/        list = null;
/*  24*/        used = 0;
/*  36*/        namePool = namepool;
/*  37*/        list = null;
/*  38*/        used = 0;
            }

            public AttributeCollection(NamePool namepool, int i)
            {
/*  23*/        list = null;
/*  24*/        used = 0;
/*  46*/        namePool = namepool;
/*  47*/        list = new Object[i * RECSIZE];
/*  48*/        used = 0;
            }

            public AttributeCollection(AttributeCollection attributecollection)
            {
/*  23*/        list = null;
/*  24*/        used = 0;
/*  56*/        namePool = attributecollection.namePool;
/*  57*/        list = new Object[attributecollection.used];
/*  58*/        if(attributecollection.used > 0)
/*  59*/            System.arraycopy(((Object) (attributecollection.list)), 0, ((Object) (list)), 0, attributecollection.used);
/*  61*/        used = attributecollection.used;
            }

            public AttributeCollection(NamePool namepool, Attributes attributes)
            {
/*  23*/        list = null;
/*  24*/        used = 0;
/*  69*/        namePool = namepool;
/*  70*/        int i = attributes.getLength();
/*  71*/        used = i * RECSIZE;
/*  72*/        list = new Object[used];
/*  74*/        for(int j = 0; j < i; j++)
                {
/*  75*/            int k = j * RECSIZE;
/*  76*/            String s = attributes.getQName(j);
/*  77*/            String s1 = Name.getPrefix(s);
/*  78*/            String s2 = attributes.getURI(j);
/*  79*/            String s3 = attributes.getLocalName(j);
/*  80*/            int l = namePool.allocate(s1, s2, s3);
/*  81*/            list[k + NAMECODE] = new Integer(l);
/*  82*/            list[k + TYPE] = attributes.getType(j);
/*  83*/            list[k + VALUE] = attributes.getValue(j);
                }

            }

            public void addAttribute(int i, String s, String s1)
            {
/*  97*/        if(list == null)
                {
/*  98*/            list = new Object[5 * RECSIZE];
/*  99*/            used = 0;
                }
/* 101*/        if(list.length == used)
                {
/* 102*/            int j = used != 0 ? used * 2 : 5 * RECSIZE;
/* 103*/            Object aobj[] = new Object[j];
/* 104*/            System.arraycopy(((Object) (list)), 0, ((Object) (aobj)), 0, used);
/* 105*/            list = aobj;
                }
/* 107*/        list[used++] = new Integer(i);
/* 108*/        list[used++] = s;
/* 109*/        list[used++] = s1;
            }

            public void addAttribute(String s, String s1, String s2, String s3, String s4)
            {
/* 124*/        addAttribute(namePool.allocate(s, s1, s2), s3, s4);
            }

            public void setAttribute(String s, String s1, String s2, String s3, String s4)
            {
/* 136*/        int i = namePool.allocate(s, s1, s2);
/* 137*/        int j = findByFingerprint(i & 0xfffff);
/* 138*/        if(j < 0)
                {
/* 139*/            addAttribute(s, s1, s2, s3, s4);
                } else
                {
/* 141*/            list[j + NAMECODE] = new Integer(i);
/* 142*/            list[j + TYPE] = s3;
/* 143*/            list[j + VALUE] = s4;
                }
            }

            public void setAttribute(int i, String s, String s1)
            {
/* 156*/        int j = findByFingerprint(i & 0xfffff);
/* 157*/        if(j < 0)
                {
/* 158*/            addAttribute(i, s, s1);
                } else
                {
/* 160*/            list[j + NAMECODE] = new Integer(i);
/* 161*/            list[j + TYPE] = s;
/* 162*/            list[j + VALUE] = s1;
                }
            }

            public void clear()
            {
/* 172*/        used = 0;
            }

            public void compact()
            {
/* 180*/        if(used == 0)
/* 181*/            list = null;
/* 182*/        else
/* 182*/        if(list.length > used)
                {
/* 183*/            Object aobj[] = new Object[used];
/* 184*/            System.arraycopy(((Object) (list)), 0, ((Object) (aobj)), 0, used);
/* 185*/            list = aobj;
                }
            }

            public int getLength()
            {
/* 202*/        return list != null ? used / RECSIZE : 0;
            }

            public int getNameCode(int i)
            {
/* 215*/        int j = i * RECSIZE;
/* 216*/        if(list == null)
/* 216*/            return -1;
/* 217*/        if(j >= used)
/* 217*/            return -1;
/* 219*/        else
/* 219*/            return ((Integer)list[j + NAMECODE]).intValue();
            }

            public String getQName(int i)
            {
/* 232*/        int j = i * RECSIZE;
/* 233*/        if(list == null)
/* 233*/            return null;
/* 234*/        if(j >= used)
/* 234*/            return null;
/* 235*/        else
/* 235*/            return namePool.getDisplayName(getNameCode(i));
            }

            public String getLocalName(int i)
            {
/* 248*/        if(list == null)
/* 248*/            return null;
/* 249*/        if(i * RECSIZE >= used)
/* 249*/            return null;
/* 250*/        else
/* 250*/            return namePool.getLocalName(getNameCode(i));
            }

            public String getURI(int i)
            {
/* 263*/        if(list == null)
/* 263*/            return null;
/* 264*/        if(i * RECSIZE >= used)
/* 264*/            return null;
/* 265*/        else
/* 265*/            return namePool.getURI(getNameCode(i));
            }

            public String getType(int i)
            {
/* 281*/        int j = i * RECSIZE;
/* 282*/        if(list == null)
/* 282*/            return null;
/* 283*/        if(j >= used)
/* 283*/            return null;
/* 284*/        else
/* 284*/            return (String)list[j + TYPE];
            }

            public String getType(String s, String s1)
            {
/* 297*/        int i = findByName(s, s1);
/* 298*/        return i >= 0 ? (String)list[i + TYPE] : null;
            }

            public String getValue(int i)
            {
/* 310*/        int j = i * RECSIZE;
/* 311*/        if(list == null)
/* 311*/            return null;
/* 312*/        if(j >= used)
/* 312*/            return null;
/* 313*/        else
/* 313*/            return (String)list[j + VALUE];
            }

            public String getValue(String s, String s1)
            {
/* 326*/        int i = findByName(s, s1);
/* 327*/        return i >= 0 ? (String)list[i + VALUE] : null;
            }

            public String getValueByFingerprint(int i)
            {
/* 335*/        int j = findByFingerprint(i);
/* 336*/        return j >= 0 ? (String)list[j + VALUE] : null;
            }

            public int getIndex(String s)
            {
/* 348*/        int i = findByDisplayName(s);
/* 349*/        return i >= 0 ? i / RECSIZE : -1;
            }

            public int getIndex(String s, String s1)
            {
/* 362*/        int i = findByName(s, s1);
/* 363*/        return i >= 0 ? i / RECSIZE : -1;
            }

            public int getIndexByFingerprint(int i)
            {
/* 371*/        int j = findByFingerprint(i);
/* 372*/        return j >= 0 ? j / RECSIZE : -1;
            }

            public String getType(String s)
            {
/* 386*/        int i = findByDisplayName(s);
/* 387*/        return i >= 0 ? (String)list[i + TYPE] : null;
            }

            public String getValue(String s)
            {
/* 399*/        int i = findByDisplayName(s);
/* 400*/        return i >= 0 ? (String)list[i + VALUE] : null;
            }

            private int findByName(String s, String s1)
            {
/* 413*/        if(namePool == null)
/* 413*/            return -1;
/* 414*/        int i = namePool.getFingerprint(s, s1);
/* 415*/        if(i == -1)
/* 415*/            return -1;
/* 416*/        else
/* 416*/            return findByFingerprint(i);
            }

            private int findByFingerprint(int i)
            {
/* 425*/        if(list == null)
/* 425*/            return -1;
/* 426*/        for(int j = 0; j < used; j += RECSIZE)
/* 427*/            if(i == (((Integer)list[j + NAMECODE]).intValue() & 0xfffff))
/* 428*/                return j;

/* 431*/        return -1;
            }

            private int findByDisplayName(String s)
            {
/* 440*/        if(list == null)
/* 440*/            return -1;
/* 441*/        String s1 = Name.getPrefix(s);
/* 442*/        if(s1.equals(""))
/* 443*/            return findByName("", s);
/* 445*/        String s2 = Name.getLocalName(s);
/* 446*/        for(int i = 0; i < getLength(); i++)
                {
/* 447*/            String s3 = namePool.getLocalName(getNameCode(i));
/* 448*/            String s4 = namePool.getPrefix(getNameCode(i));
/* 449*/            if(s2.equals(s3) && s1.equals(s4))
/* 450*/                return i;
                }

/* 453*/        return -1;
            }

}
