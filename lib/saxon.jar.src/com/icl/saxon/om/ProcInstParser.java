// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ProcInstParser.java

package com.icl.saxon.om;


public class ProcInstParser
{

            public ProcInstParser()
            {
            }

            public static String getPseudoAttribute(String s, String s1)
            {
/*  22*/        boolean flag = false;
                int l;
/*  23*/        for(int i = 0; i <= s.length() - 4; i = l + 1)
                {
/*  25*/            int j = -1;
/*  26*/            for(int k = i; k < s.length(); k++)
                    {
/*  27*/                if(s.charAt(k) != '"' && s.charAt(k) != '\'')
/*  28*/                    continue;
/*  28*/                j = k;
/*  29*/                break;
                    }

/*  32*/            if(j < 0)
/*  32*/                return null;
/*  35*/            l = s.indexOf(s.charAt(j), j + 1);
/*  36*/            if(l < 0)
/*  36*/                return null;
/*  37*/            int i1 = s.indexOf(s1, i);
/*  38*/            if(i1 < 0)
/*  38*/                return null;
/*  39*/            if(i1 < j)
                    {
/*  41*/                boolean flag1 = true;
/*  42*/                for(int j1 = i1 + s1.length(); j1 < j; j1++)
                        {
/*  43*/                    char c = s.charAt(j1);
/*  44*/                    if(Character.isWhitespace(c) || c == '=')
/*  45*/                        continue;
/*  45*/                    flag1 = false;
/*  46*/                    break;
                        }

/*  49*/                if(flag1)
                        {
/*  50*/                    String s2 = s.substring(j + 1, l);
/*  51*/                    return unescape(s2);
                        }
                    }
                }

/*  56*/        return null;
            }

            private static String unescape(String s)
            {
/*  64*/        if(s.indexOf('&') < 0)
/*  64*/            return s;
/*  65*/        StringBuffer stringbuffer = new StringBuffer();
/*  66*/        for(int i = 0; i < s.length(); i++)
                {
/*  67*/            char c = s.charAt(i);
/*  68*/            if(c == '&')
                    {
/*  69*/                if(i + 2 < s.length() && s.charAt(i + 1) == '#')
                        {
/*  70*/                    if(s.charAt(i + 2) == 'x')
                            {
/*  71*/                        int j = i + 3;
/*  72*/                        int l = 0;
/*  74*/                        for(; j < s.length() && s.charAt(j) != ';'; j++)
                                {
/*  74*/                            int j1 = "0123456789abcdef".indexOf(s.charAt(j));
/*  75*/                            if(j1 < 0)
/*  76*/                                j1 = "0123456789ABCDEF".indexOf(s.charAt(j));
/*  78*/                            if(j1 < 0)
/*  79*/                                return null;
/*  81*/                            l = l * 16 + j1;
                                }

/*  84*/                        char c1 = (char)l;
/*  85*/                        stringbuffer.append(c1);
/*  86*/                        i = j;
                            } else
                            {
/*  88*/                        int k = i + 2;
/*  89*/                        int i1 = 0;
/*  91*/                        for(; k < s.length() && s.charAt(k) != ';'; k++)
                                {
/*  91*/                            int k1 = "0123456789".indexOf(s.charAt(k));
/*  92*/                            if(k1 < 0)
/*  93*/                                return null;
/*  95*/                            i1 = i1 * 10 + k1;
                                }

/*  98*/                        char c2 = (char)i1;
/*  99*/                        stringbuffer.append(c2);
/* 100*/                        i = k;
                            }
                        } else
/* 102*/                if(s.substring(i + 1).startsWith("lt;"))
                        {
/* 103*/                    stringbuffer.append('<');
/* 104*/                    i += 3;
                        } else
/* 105*/                if(s.substring(i + 1).startsWith("gt;"))
                        {
/* 106*/                    stringbuffer.append('>');
/* 107*/                    i += 3;
                        } else
/* 108*/                if(s.substring(i + 1).startsWith("amp;"))
                        {
/* 109*/                    stringbuffer.append('&');
/* 110*/                    i += 4;
                        } else
/* 111*/                if(s.substring(i + 1).startsWith("quot;"))
                        {
/* 112*/                    stringbuffer.append('"');
/* 113*/                    i += 5;
                        } else
/* 114*/                if(s.substring(i + 1).startsWith("apos;"))
                        {
/* 115*/                    stringbuffer.append('\'');
/* 116*/                    i += 5;
                        } else
                        {
/* 118*/                    return null;
                        }
                    } else
                    {
/* 122*/                stringbuffer.append(c);
                    }
                }

/* 125*/        return stringbuffer.toString();
            }
}
