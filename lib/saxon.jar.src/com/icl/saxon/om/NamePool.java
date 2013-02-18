// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NamePool.java

package com.icl.saxon.om;

import com.icl.saxon.style.StandardNames;
import java.io.PrintStream;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.transform.TransformerException;

public class NamePool
{
    private class NameEntry
    {

                String localName;
                short uriCode;
                NameEntry nextEntry;

                public NameEntry(short word0, String s)
                {
/*  74*/            uriCode = word0;
/*  75*/            localName = s;
/*  76*/            nextEntry = null;
                }
    }


            private static NamePool defaultNamePool;
            private StandardNames standardNames;
            NameEntry hashslots[];
            String prefixes[];
            short prefixesUsed;
            String uris[];
            String prefixesForUri[];
            short urisUsed;
            Vector signatures;
            boolean sealed;

            public static NamePool getDefaultNamePool()
            {
/*  64*/        return defaultNamePool;
            }

            public NamePool()
            {
/*  57*/        standardNames = null;
/*  81*/        hashslots = new NameEntry[1024];
/*  83*/        prefixes = new String[100];
/*  84*/        prefixesUsed = 0;
/*  85*/        uris = new String[100];
/*  86*/        prefixesForUri = new String[100];
/*  87*/        urisUsed = 0;
/*  88*/        signatures = new Vector();
/*  89*/        sealed = false;
/*  96*/        prefixes[0] = "";
/*  97*/        uris[0] = "";
/*  98*/        prefixesForUri[0] = "";
/* 100*/        prefixes[1] = "xml";
/* 101*/        uris[1] = "http://www.w3.org/XML/1998/namespace";
/* 102*/        prefixesForUri[1] = "xml ";
/* 104*/        prefixes[2] = "xsl";
/* 105*/        uris[2] = "http://www.w3.org/1999/XSL/Transform";
/* 106*/        prefixesForUri[2] = "xsl ";
/* 108*/        prefixes[3] = "saxon";
/* 109*/        uris[3] = "http://icl.com/saxon";
/* 110*/        prefixesForUri[3] = "saxon ";
/* 112*/        prefixes[4] = "func";
/* 113*/        uris[4] = "http://exslt.org/functions";
/* 114*/        prefixesForUri[4] = "func ";
/* 116*/        prefixesUsed = 5;
/* 117*/        urisUsed = 5;
            }

            public synchronized void loadStandardNames()
            {
/* 126*/        if(standardNames == null)
                {
/* 127*/            standardNames = new StandardNames(this);
/* 128*/            standardNames.allocateNames();
                }
            }

            public StandardNames getStandardNames()
            {
/* 137*/        return standardNames;
            }

            public synchronized void setStylesheetSignature(Object obj)
            {
/* 147*/        signatures.addElement(new Integer(obj.hashCode()));
            }

            public boolean hasSignature(Object obj)
            {
/* 160*/        return signatures.contains(new Integer(obj.hashCode()));
            }

            public synchronized void importPool(NamePool namepool)
                throws TransformerException
            {
/* 174*/        if(signatures.size() > 0)
/* 175*/            throw new TransformerException("Cannot merge names into a non-empty namepool");
/* 178*/        for(int i = 0; i < namepool.signatures.size(); i++)
/* 179*/            signatures.addElement(namepool.signatures.elementAt(i));

/* 182*/        for(int j = 0; j < 1024; j++)
                {
/* 183*/            NameEntry nameentry = namepool.hashslots[j];
/* 184*/            NameEntry nameentry1 = null;
/* 186*/            for(; nameentry != null; nameentry = nameentry.nextEntry)
                    {
/* 186*/                NameEntry nameentry2 = new NameEntry(nameentry.uriCode, nameentry.localName);
/* 187*/                if(nameentry1 == null)
/* 188*/                    hashslots[j] = nameentry2;
/* 190*/                else
/* 190*/                    nameentry1.nextEntry = nameentry2;
/* 192*/                nameentry1 = nameentry2;
                    }

                }

/* 197*/        prefixesUsed = namepool.prefixesUsed;
/* 198*/        urisUsed = namepool.urisUsed;
/* 199*/        if(prefixesUsed > 60)
/* 200*/            prefixes = new String[prefixesUsed * 2];
/* 202*/        if(urisUsed > 60)
                {
/* 203*/            uris = new String[urisUsed * 2];
/* 204*/            prefixesForUri = new String[urisUsed * 2];
                }
/* 206*/        System.arraycopy(namepool.prefixes, 0, prefixes, 0, prefixesUsed);
/* 207*/        System.arraycopy(namepool.uris, 0, uris, 0, urisUsed);
/* 208*/        System.arraycopy(namepool.prefixesForUri, 0, prefixesForUri, 0, urisUsed);
/* 210*/        namepool.sealed = true;
            }

            public boolean isSealed()
            {
/* 218*/        return sealed;
            }

            private NameEntry getNameEntry(int i)
            {
/* 227*/        int j = i & 0x3ff;
/* 228*/        int k = i >> 10 & 0x3ff;
/* 229*/        NameEntry nameentry = hashslots[j];
/* 231*/        for(int l = 0; l < k; l++)
                {
/* 232*/            if(nameentry == null)
/* 232*/                return null;
/* 233*/            nameentry = nameentry.nextEntry;
                }

/* 235*/        return nameentry;
            }

            public synchronized int allocateNamespaceCode(String s, String s1)
            {
/* 246*/        short word0 = allocateCodeForPrefix(s);
/* 247*/        short word1 = allocateCodeForURI(s1);
/* 249*/        if(word0 != 0)
                {
/* 251*/            String s2 = s + " ";
/* 252*/            if(prefixesForUri[word1].indexOf(s2) < 0)
/* 253*/                prefixesForUri[word1] += s2;
                }
/* 257*/        return (word0 << 16) + word1;
            }

            public int getNamespaceCode(String s, String s1)
            {
/* 267*/        short word0 = getCodeForPrefix(s);
/* 268*/        if(word0 < 0)
/* 268*/            return -1;
/* 269*/        short word1 = getCodeForURI(s1);
/* 270*/        if(word1 < 0)
/* 270*/            return -1;
/* 272*/        if(word0 != 0)
                {
/* 274*/            String s2 = s + " ";
/* 275*/            if(prefixesForUri[word1].indexOf(s2) < 0)
/* 276*/                return -1;
                }
/* 280*/        return (word0 << 16) + word1;
            }

            public synchronized short allocateCodeForURI(String s)
            {
/* 290*/        for(short word0 = 0; word0 < urisUsed; word0++)
/* 291*/            if(uris[word0].equals(s))
/* 292*/                return word0;

/* 295*/        if(sealed)
/* 296*/            throw new IllegalArgumentException("Namepool has been sealed");
/* 298*/        if(urisUsed >= uris.length)
                {
/* 299*/            if(urisUsed > 32000)
/* 300*/                throw new IllegalArgumentException("Too many namespace URIs");
/* 302*/            String as[] = new String[urisUsed * 2];
/* 303*/            String as1[] = new String[urisUsed * 2];
/* 304*/            System.arraycopy(prefixesForUri, 0, as, 0, urisUsed);
/* 305*/            System.arraycopy(uris, 0, as1, 0, urisUsed);
/* 306*/            prefixesForUri = as;
/* 307*/            uris = as1;
                }
/* 309*/        uris[urisUsed] = s;
/* 310*/        prefixesForUri[urisUsed] = "";
/* 311*/        return urisUsed++;
            }

            public short getCodeForURI(String s)
            {
/* 322*/        for(short word0 = 0; word0 < urisUsed; word0++)
/* 323*/            if(uris[word0].equals(s))
/* 324*/                return word0;

/* 327*/        return -1;
            }

            public synchronized short allocateCodeForPrefix(String s)
            {
/* 335*/        for(short word0 = 0; word0 < prefixesUsed; word0++)
/* 336*/            if(prefixes[word0].equals(s))
/* 337*/                return word0;

/* 340*/        if(sealed)
/* 341*/            throw new IllegalArgumentException("Namepool has been sealed");
/* 343*/        if(prefixesUsed >= prefixes.length)
                {
/* 344*/            if(prefixesUsed > 32000)
/* 345*/                throw new IllegalArgumentException("Too many namespace prefixes");
/* 347*/            String as[] = new String[prefixesUsed * 2];
/* 348*/            System.arraycopy(prefixes, 0, as, 0, prefixesUsed);
/* 349*/            prefixes = as;
                }
/* 351*/        prefixes[prefixesUsed] = s;
/* 352*/        return prefixesUsed++;
            }

            public short getCodeForPrefix(String s)
            {
/* 362*/        for(short word0 = 0; word0 < prefixesUsed; word0++)
/* 363*/            if(prefixes[word0].equals(s))
/* 364*/                return word0;

/* 367*/        return -1;
            }

            public int getPrefixIndex(short word0, String s)
            {
/* 378*/        if(s.equals(""))
/* 378*/            return 0;
/* 379*/        if(prefixesForUri[word0].equals(s + " "))
/* 379*/            return 1;
/* 382*/        int i = 1;
/* 383*/        for(StringTokenizer stringtokenizer = new StringTokenizer(prefixesForUri[word0]); stringtokenizer.hasMoreElements();)
                {
/* 385*/            if(s.equals(stringtokenizer.nextElement()))
/* 386*/                return i;
/* 388*/            if(i++ == 255)
/* 389*/                throw new IllegalArgumentException("Too many prefixes for one namespace URI");
                }

/* 392*/        return -1;
            }

            public String getPrefixWithIndex(short word0, int i)
            {
/* 401*/        if(i == 0)
/* 401*/            return "";
/* 402*/        StringTokenizer stringtokenizer = new StringTokenizer(prefixesForUri[word0]);
/* 403*/        int j = 1;
/* 405*/        while(stringtokenizer.hasMoreElements()) 
                {
/* 405*/            String s = (String)stringtokenizer.nextElement();
/* 406*/            if(j++ == i)
/* 407*/                return s;
                }
/* 410*/        return null;
            }

            public synchronized int allocate(String s, String s1, String s2)
            {
/* 423*/        short word0 = allocateCodeForURI(s1);
/* 424*/        return allocate(s, word0, s2);
            }

            public synchronized int allocate(String s, short word0, String s1)
            {
/* 437*/        int i = (s1.hashCode() & 0x7fffffff) % 1023;
/* 438*/        int j = 0;
/* 439*/        int k = getPrefixIndex(word0, s);
/* 441*/        if(k < 0)
                {
/* 442*/            prefixesForUri[word0] += s + " ";
/* 443*/            k = getPrefixIndex(word0, s);
                }
/* 445*/        Object obj = null;
/* 447*/        if(hashslots[i] == null)
                {
/* 448*/            if(sealed)
/* 449*/                throw new IllegalArgumentException("Namepool has been sealed");
/* 451*/            NameEntry nameentry = new NameEntry(word0, s1);
/* 452*/            hashslots[i] = nameentry;
                } else
                {
/* 454*/            NameEntry nameentry1 = hashslots[i];
/* 456*/            do
                    {
/* 456*/                boolean flag = nameentry1.localName.equals(s1);
/* 457*/                boolean flag1 = nameentry1.uriCode == word0;
/* 459*/                if(flag && flag1)
/* 461*/                    break;
/* 463*/                NameEntry nameentry2 = nameentry1.nextEntry;
/* 464*/                if(++j >= 1024)
/* 466*/                    throw new IllegalArgumentException("Saxon name pool is full");
/* 468*/                if(nameentry2 == null)
                        {
/* 469*/                    if(sealed)
/* 470*/                        throw new IllegalArgumentException("Namepool has been sealed");
/* 472*/                    NameEntry nameentry3 = new NameEntry(word0, s1);
/* 473*/                    nameentry1.nextEntry = nameentry3;
/* 474*/                    break;
                        }
/* 476*/                nameentry1 = nameentry2;
                    } while(true);
                }
/* 482*/        return (k << 20) + (j << 10) + i;
            }

            public synchronized int allocateNamespaceCode(int i)
            {
/* 490*/        String s = getPrefix(i);
/* 491*/        short word0 = getURICode(i);
/* 492*/        short word1 = allocateCodeForPrefix(s);
/* 493*/        return (word1 << 16) + word0;
            }

            public int getNamespaceCode(int i)
            {
/* 502*/        String s = getPrefix(i);
/* 503*/        short word0 = getURICode(i);
/* 504*/        short word1 = getCodeForPrefix(s);
/* 505*/        return (word1 << 16) + word0;
            }

            public String getURI(int i)
            {
/* 513*/        NameEntry nameentry = getNameEntry(i);
/* 514*/        if(nameentry == null)
/* 515*/            unknownNameCode(i);
/* 517*/        return uris[nameentry.uriCode];
            }

            public short getURICode(int i)
            {
/* 525*/        NameEntry nameentry = getNameEntry(i);
/* 526*/        if(nameentry == null)
/* 527*/            unknownNameCode(i);
/* 529*/        return nameentry.uriCode;
            }

            public String getLocalName(int i)
            {
/* 537*/        NameEntry nameentry = getNameEntry(i);
/* 538*/        if(nameentry == null)
/* 539*/            unknownNameCode(i);
/* 541*/        return nameentry.localName;
            }

            public String getPrefix(int i)
            {
/* 549*/        short word0 = getURICode(i);
/* 550*/        int j = i >> 20 & 0xff;
/* 551*/        return getPrefixWithIndex(word0, j);
            }

            public String getDisplayName(int i)
            {
/* 559*/        NameEntry nameentry = getNameEntry(i);
/* 560*/        if(nameentry == null)
/* 561*/            unknownNameCode(i);
/* 563*/        int j = i >> 20 & 0xff;
/* 564*/        if(j == 0)
/* 564*/            return nameentry.localName;
/* 565*/        else
/* 565*/            return getPrefixWithIndex(nameentry.uriCode, j) + ':' + nameentry.localName;
            }

            private void unknownNameCode(int i)
            {
/* 575*/        System.err.println("Unknown name code " + i);
/* 576*/        diagnosticDump();
/* 577*/        (new IllegalArgumentException("Unknown name")).printStackTrace();
/* 578*/        throw new IllegalArgumentException("Unknown name code " + i);
            }

            public int getFingerprint(int i)
            {
/* 588*/        return i & 0xfffff;
            }

            public int getFingerprint(String s, String s1)
            {
/* 602*/        int i = -1;
/* 603*/        for(short word0 = 0; word0 < urisUsed; word0++)
                {
/* 604*/            if(!uris[word0].equals(s))
/* 605*/                continue;
/* 605*/            i = word0;
/* 606*/            break;
                }

/* 610*/        if(i == -1)
/* 610*/            return -1;
/* 612*/        int j = (s1.hashCode() & 0x7fffffff) % 1023;
/* 613*/        int k = 0;
/* 615*/        NameEntry nameentry = null;
/* 617*/        if(hashslots[j] == null)
/* 618*/            return -1;
/* 620*/        nameentry = hashslots[j];
/* 622*/        do
                {
/* 622*/            boolean flag = nameentry.localName.equals(s1);
/* 623*/            boolean flag1 = nameentry.uriCode == i;
/* 625*/            if(!flag || !flag1)
                    {
/* 628*/                NameEntry nameentry1 = nameentry.nextEntry;
/* 629*/                k++;
/* 630*/                if(nameentry1 == null)
/* 631*/                    return -1;
/* 633*/                nameentry = nameentry1;
                    } else
                    {
/* 638*/                return (k << 10) + j;
                    }
                } while(true);
            }

            public String getURIFromNamespaceCode(int i)
            {
/* 646*/        return uris[i & 0xffff];
            }

            public String getURIFromURICode(short word0)
            {
/* 654*/        return uris[word0];
            }

            public String getPrefixFromNamespaceCode(int i)
            {
/* 663*/        return prefixes[i >> 16];
            }

            public synchronized void diagnosticDump()
            {
/* 673*/        System.err.println("Contents of NamePool " + this);
/* 674*/        for(int i = 0; i < 1024; i++)
                {
/* 675*/            NameEntry nameentry = hashslots[i];
/* 676*/            for(int k = 0; nameentry != null; k++)
                    {
/* 678*/                System.err.println("Fingerprint " + k + "/" + i);
/* 679*/                System.err.println("  local name = " + nameentry.localName + " uri code = " + nameentry.uriCode);
/* 681*/                nameentry = nameentry.nextEntry;
                    }

                }

/* 686*/        for(int j = 0; j < prefixesUsed; j++)
/* 687*/            System.err.println("Prefix " + j + " = " + prefixes[j]);

/* 689*/        for(int l = 0; l < urisUsed; l++)
                {
/* 690*/            System.err.println("URI " + l + " = " + uris[l]);
/* 691*/            System.err.println("Prefixes for URI " + l + " = " + prefixesForUri[l]);
                }

            }

            public void generateJavaConstants()
            {
/* 703*/        System.out.println("// Declarations generated from NamePool");
/* 704*/        for(int i = 0; i < 1024; i++)
                {
/* 705*/            NameEntry nameentry = hashslots[i];
/* 706*/            for(int j = 0; nameentry != null; j++)
                    {
/* 708*/                int k = (j << 10) + i;
/* 709*/                String s = "";
/* 710*/                if(nameentry.uriCode == 0)
/* 710*/                    s = "";
/* 711*/                else
/* 711*/                if(nameentry.uriCode == 2)
/* 711*/                    s = "XSL_";
/* 712*/                else
/* 712*/                if(nameentry.uriCode == 1)
/* 712*/                    s = "XML_";
/* 713*/                else
/* 713*/                if(nameentry.uriCode == 3)
/* 713*/                    s = "SAXON_";
/* 714*/                String s1 = nameentry.localName.toUpperCase();
/* 716*/                do
                        {
/* 716*/                    int l = s1.indexOf('-');
/* 717*/                    if(l < 0)
/* 717*/                        break;
/* 718*/                    s1 = s1.substring(0, l) + '_' + s1.substring(l + 1);
                        } while(true);
/* 721*/                System.out.println("public final static int " + s + s1 + " = " + k + ";");
/* 723*/                nameentry = nameentry.nextEntry;
                    }

                }

            }

            static 
            {
/*  52*/        defaultNamePool = new NamePool();
/*  54*/        defaultNamePool.loadStandardNames();
            }
}
