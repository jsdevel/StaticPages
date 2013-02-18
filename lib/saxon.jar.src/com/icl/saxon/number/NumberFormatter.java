// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NumberFormatter.java

package com.icl.saxon.number;

import java.util.Vector;

// Referenced classes of package com.icl.saxon.number:
//            Numberer

public class NumberFormatter
{

            private Vector formatTokens;
            private Vector separators;
            private boolean startsWithSeparator;

            public NumberFormatter()
            {
            }

            public void prepare(String s)
            {
/*  34*/        if(s.length() == 0)
/*  34*/            s = "1";
/*  36*/        formatTokens = new Vector();
/*  37*/        separators = new Vector();
/*  39*/        int i = s.length();
/*  40*/        int j = 0;
/*  41*/        boolean flag = false;
/*  42*/        boolean flag1 = true;
/*  43*/        startsWithSeparator = true;
/*  46*/        while(j < i) 
                {
/*  46*/            char c = s.charAt(j);
/*  47*/            int k = j;
/*  49*/            for(; Character.isLetterOrDigit(c); c = s.charAt(j))
/*  49*/                if(++j == i)
/*  50*/                    break;

/*  53*/            if(j > k)
                    {
/*  54*/                String s1 = s.substring(k, j);
/*  55*/                formatTokens.addElement(s1);
/*  56*/                if(flag1)
                        {
/*  57*/                    separators.addElement(".");
/*  58*/                    startsWithSeparator = false;
/*  59*/                    flag1 = false;
                        }
                    }
/*  62*/            if(j == i)
/*  62*/                break;
/*  63*/            k = j;
/*  64*/            for(char c1 = s.charAt(j); !Character.isLetterOrDigit(c1); c1 = s.charAt(j))
                    {
/*  66*/                flag1 = false;
/*  67*/                if(++j == i)
/*  68*/                    break;
                    }

/*  71*/            if(j > k)
                    {
/*  72*/                String s2 = s.substring(k, j);
/*  73*/                separators.addElement(s2);
                    }
                }
/*  76*/        if(formatTokens.size() == 0)
                {
/*  77*/            formatTokens.add("1");
/*  78*/            if(separators.size() == 1)
/*  79*/                separators.add(separators.get(0));
                }
            }

            public String format(Vector vector, int i, String s, String s1, Numberer numberer)
            {
/*  93*/        StringBuffer stringbuffer = new StringBuffer();
/*  94*/        int j = 0;
/*  95*/        int k = 0;
/*  97*/        if(startsWithSeparator)
/*  98*/            stringbuffer.append((String)separators.elementAt(k));
/* 102*/        while(j < vector.size()) 
                {
/* 102*/            if(j > 0)
/* 103*/                if(k == 0 && startsWithSeparator)
/* 106*/                    stringbuffer.append(".");
/* 108*/                else
/* 108*/                    stringbuffer.append((String)separators.get(k));
/* 111*/            int l = ((Integer)vector.elementAt(j++)).intValue();
/* 112*/            String s2 = numberer.format(l, (String)formatTokens.elementAt(k), i, s, s1);
/* 114*/            stringbuffer.append(s2);
/* 115*/            if(++k == formatTokens.size())
/* 116*/                k--;
                }
/* 119*/        if(separators.size() > formatTokens.size())
/* 120*/            stringbuffer.append((String)separators.elementAt(separators.size() - 1));
/* 122*/        return stringbuffer.toString();
            }

            public String format(int i, int j, String s, String s1, Numberer numberer)
            {
/* 132*/        Vector vector = new Vector();
/* 133*/        vector.addElement(new Integer(i));
/* 134*/        return format(vector, j, s, s1, numberer);
            }
}
