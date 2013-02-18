// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SaxonOutputKeys.java

package com.icl.saxon.output;


public class SaxonOutputKeys
{

            public static final String INDENT_SPACES = "{http://icl.com/saxon}indent-spaces";
            public static final String OMIT_META_TAG = "{http://icl.com/saxon}omit-meta-tag";
            public static final String CHARACTER_REPRESENTATION = "{http://icl.com/saxon}character-representation";
            public static final String NEXT_IN_CHAIN = "{http://icl.com/saxon}next-in-chain";
            public static final String NEXT_IN_CHAIN_BASE_URI = "{http://icl.com/saxon}next-in-chain-base-uri";
            public static final String REQUIRE_WELL_FORMED = "{http://saxon.sf.net/}require-well-formed";

            public SaxonOutputKeys()
            {
            }

            public static final boolean isValidOutputKey(String s)
            {
/*  65*/        if(s.startsWith("{"))
                {
/*  66*/            if(s.startsWith("{http://icl.com/saxon}"))
/*  67*/                return s.equals("{http://icl.com/saxon}indent-spaces") || s.equals("{http://icl.com/saxon}omit-meta-tag") || s.equals("{http://icl.com/saxon}character-representation") || s.equals("{http://icl.com/saxon}next-in-chain") || s.equals("{http://icl.com/saxon}next-in-chain-base-uri") || s.equals("{http://saxon.sf.net/}require-well-formed");
/*  75*/            else
/*  75*/                return true;
                } else
                {
/*  78*/            return s.equals("cdata-section-elements") || s.equals("doctype-public") || s.equals("doctype-system") || s.equals("encoding") || s.equals("indent") || s.equals("media-type") || s.equals("method") || s.equals("omit-xml-declaration") || s.equals("standalone") || s.equals("version");
                }
            }
}
