// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   CharacterSetFactory.java

package com.icl.saxon.charcode;

import com.icl.saxon.Loader;
import java.io.PrintStream;
import java.util.Properties;

// Referenced classes of package com.icl.saxon.charcode:
//            ASCIICharacterSet, Latin1CharacterSet, Latin2CharacterSet, UnicodeCharacterSet, 
//            KOI8RCharacterSet, CP1251CharacterSet, CP1250CharacterSet, CP852CharacterSet, 
//            PluggableCharacterSet, CharacterSet

public class CharacterSetFactory
{

            public CharacterSetFactory()
            {
            }

            public static CharacterSet getCharacterSet(Properties properties)
            {
/*  19*/        String s = properties.getProperty("encoding");
/*  20*/        if(s == null)
/*  20*/            s = "UTF8";
/*  21*/        if(s.equalsIgnoreCase("utf-8"))
/*  21*/            s = "UTF8";
/*  23*/        Object obj = makeCharacterSet(s);
/*  24*/        if(obj == null)
/*  25*/            obj = new ASCIICharacterSet();
/*  27*/        return ((CharacterSet) (obj));
            }

            public static CharacterSet makeCharacterSet(String s)
            {
/*  32*/        if(s.equalsIgnoreCase("ASCII"))
/*  33*/            return new ASCIICharacterSet();
/*  34*/        if(s.equalsIgnoreCase("US-ASCII"))
/*  35*/            return new ASCIICharacterSet();
/*  36*/        if(s.equalsIgnoreCase("iso-8859-1"))
/*  37*/            return new Latin1CharacterSet();
/*  38*/        if(s.equalsIgnoreCase("ISO8859_1"))
/*  39*/            return new Latin1CharacterSet();
/*  40*/        if(s.equalsIgnoreCase("iso-8859-2"))
/*  41*/            return new Latin2CharacterSet();
/*  42*/        if(s.equalsIgnoreCase("ISO8859_2"))
/*  43*/            return new Latin2CharacterSet();
/*  44*/        if(s.equalsIgnoreCase("utf-8"))
/*  45*/            return new UnicodeCharacterSet();
/*  46*/        if(s.equalsIgnoreCase("UTF8"))
/*  47*/            return new UnicodeCharacterSet();
/*  48*/        if(s.equalsIgnoreCase("utf-16"))
/*  49*/            return new UnicodeCharacterSet();
/*  50*/        if(s.equalsIgnoreCase("utf16"))
/*  51*/            return new UnicodeCharacterSet();
/*  52*/        if(s.equalsIgnoreCase("KOI8-R"))
/*  53*/            return new KOI8RCharacterSet();
/*  54*/        if(s.equalsIgnoreCase("cp1251"))
/*  55*/            return new CP1251CharacterSet();
/*  56*/        if(s.equalsIgnoreCase("windows-1251"))
/*  57*/            return new CP1251CharacterSet();
/*  58*/        if(s.equalsIgnoreCase("cp1250"))
/*  59*/            return new CP1250CharacterSet();
/*  60*/        if(s.equalsIgnoreCase("windows-1250"))
/*  61*/            return new CP1250CharacterSet();
/*  62*/        if(s.equalsIgnoreCase("cp852"))
/*  63*/            return new CP852CharacterSet();
/*  66*/        String s1 = null;
/*  69*/        try
                {
/*  69*/            s1 = System.getProperty("encoding." + s);
/*  70*/            if(s1 == null)
/*  71*/                s1 = s;
/*  73*/            Object obj = Loader.getInstance(s1);
/*  74*/            if(obj instanceof PluggableCharacterSet)
/*  75*/                return (PluggableCharacterSet)obj;
                }
/*  78*/        catch(Exception exception)
                {
/*  78*/            System.err.println("Failed to load " + s1);
                }
/*  82*/        return null;
            }
}
