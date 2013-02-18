// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Numberer_en.java

package com.icl.saxon.number;


// Referenced classes of package com.icl.saxon.number:
//            Numberer

public class Numberer_en
    implements Numberer
{

            protected String westernDigits;
            protected String latinUpper;
            protected String latinLower;
            protected String greekUpper;
            protected String greekLower;
            protected String cyrillicUpper;
            protected String cyrillicLower;
            protected String hebrew;
            protected String hiraganaA;
            protected String katakanaA;
            protected String hiraganaI;
            protected String katakanaI;
            protected String kanjiDigits;
            private static String romanThousands[] = {
/* 338*/        "", "m", "mm", "mmm", "mmmm", "mmmmm", "mmmmmm", "mmmmmmm", "mmmmmmmm", "mmmmmmmmm"
            };
            private static String romanHundreds[] = {
/* 340*/        "", "c", "cc", "ccc", "cd", "d", "dc", "dcc", "dccc", "cm"
            };
            private static String romanTens[] = {
/* 342*/        "", "x", "xx", "xxx", "xl", "l", "lx", "lxx", "lxxx", "xc"
            };
            private static String romanUnits[] = {
/* 344*/        "", "i", "ii", "iii", "iv", "v", "vi", "vii", "viii", "ix"
            };
            protected String englishUnits[] = {
/* 371*/        "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", 
/* 371*/        "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
            };
            protected String englishTens[] = {
/* 376*/        "", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
            };

            public Numberer_en()
            {
/* 157*/        westernDigits = "0123456789";
/* 160*/        latinUpper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
/* 163*/        latinLower = "abcdefghijklmnopqrstuvwxyz";
/* 166*/        greekUpper = "\u0391\u0392\u0393\u0394\u0395\u0396\u0397\u0398\u0399\u039A\u039B\u039C\u039C\u039D\u039E\u039F\u03A0\u03A1\u03A3\u03A4\u03A5\u03A6\u03A7\u03A8\u03A9";
/* 171*/        greekLower = "\u03B1\u03B2\u03B3\u03B4\u03B5\u03B6\u03B7\u03B8\u03B9\u03BA\u03BB\u03BC\u03BC\u03BD\u03BE\u03BF\u03C0\u03C1\u03C3\u03C4\u03C5\u03C6\u03C7\u03C8\u03C9";
/* 179*/        cyrillicUpper = "\u0410\u0411\u0412\u0413\u0414\u0415\u0416\u0417\u0418\u041A\u041B\u041C\u041D\u041E\u041F\u0420\u0421\u0421\u0423\u0424\u0425\u0426\u0427\u0428\u0429\u042B\u042D\u042E\u042F";
/* 184*/        cyrillicLower = "\u0430\u0431\u0432\u0433\u0434\u0435\u0436\u0437\u0438\u043A\u043B\u043C\u043D\u043E\u043F\u0440\u0441\u0441\u0443\u0444\u0445\u0446\u0447\u0448\u0449\u044B\u044D\u044E\u044F";
/* 189*/        hebrew = "\u05D0\u05D1\u05D2\u05D3\u05D4\u05D5\u05D6\u05D7\u05D8\u05D9\u05DB\u05DC\u05DE\u05E0\u05E1\u05E2\u05E4\u05E6\u05E7\u05E8\u05E9\u05EA";
/* 197*/        hiraganaA = "\u3042\u3044\u3046\u3048\u304A\u304B\u304D\u304F\u3051\u3053\u3055\u3057\u3059\u305B\u305D\u305F\u3061\u3064\u3066\u3068\u306A\u306B\u306C\u306D\u306E\u306F\u3072\u3075\u3078\u307B\u307E\u307F\u3080\u3081\u3082\u3084\u3086\u3088\u3089\u308A\u308B\u308C\u308D\u308F\u3092\u3093";
/* 204*/        katakanaA = "\u30A2\u30A4\u30A6\u30A8\u30AA\u30AB\u30AD\u30AF\u30B1\u30B3\u30B5\u30B7\u30B9\u30BB\u30BD\u30BF\u30C1\u30C4\u30C6\u30C8\u30CA\u30CB\u30CC\u30CD\u30CE\u30CF\u30D2\u30D5\u30D8\u30DB\u30DE\u30DF\u30E0\u30E1\u30E2\u30E4\u30E6\u30E8\u30E9\u30EA\u30EB\u30EC\u30ED\u30EF\u30F2\u30F3";
/* 212*/        hiraganaI = "\u3044\u308D\u306F\u306B\u307B\u3078\u3068\u3061\u308A\u306C\u308B\u3092\u308F\u304B\u3088\u305F\u308C\u305D\u3064\u306D\u306A\u3089\u3080\u3046\u3090\u306E\u304A\u304F\u3084\u307E\u3051\u3075\u3053\u3048\u3066\u3042\u3055\u304D\u3086\u3081\u307F\u3057\u3091\u3072\u3082\u305B\u3059";
/* 220*/        katakanaI = "\u30A4\u30ED\u30CF\u30CB\u30DB\u30D8\u30C8\u30C1\u30EA\u30CC\u30EB\u30F2\u30EF\u30AB\u30E8\u30BF\u30EC\u30BD\u30C4\u30CD\u30CA\u30E9\u30E0\u30A6\u30F0\u30CE\u30AA\u30AF\u30E4\u30DE\u30B1\u30D5\u30B3\u30A8\u30C6\u30A2\u30B5\u30AD\u30E6\u30E1\u30DF\u30B7\u30F1\u30D2\u30E2\u30BB\u30B9";
/* 229*/        kanjiDigits = "\u3007\u4E00\u4E8C\u4E09\u56DB\u4E94\u516D\u4E03\u516B\u4E5D";
            }

            public String format(int i, String s, int j, String s1, String s2)
            {
/*  28*/        if(i <= 0)
/*  28*/            return "" + i;
/*  30*/        StringBuffer stringbuffer = new StringBuffer();
/*  31*/        char c = s.charAt(0);
/*  33*/        switch(c)
                {
/*  37*/        case 48: // '0'
/*  37*/        case 49: // '1'
/*  37*/            stringbuffer.append(toRadical(i, westernDigits, s, j, s1));
/*  38*/            break;

/*  41*/        case 65: // 'A'
/*  41*/            stringbuffer.append(toAlphaSequence(i, latinUpper));
/*  42*/            break;

/*  45*/        case 97: // 'a'
/*  45*/            stringbuffer.append(toAlphaSequence(i, latinLower));
/*  46*/            break;

/*  49*/        case 105: // 'i'
/*  49*/            if(s2.equals("alphabetic"))
/*  50*/                alphaDefault(i, c, stringbuffer);
/*  52*/            else
/*  52*/                stringbuffer.append(toRoman(i));
/*  54*/            break;

/*  57*/        case 73: // 'I'
/*  57*/            if(s2.equals("alphabetic"))
/*  58*/                alphaDefault(i, c, stringbuffer);
/*  60*/            else
/*  60*/                stringbuffer.append(toRoman(i).toUpperCase());
/*  62*/            break;

/*  65*/        case 111: // 'o'
/*  65*/            if(s.equals("one"))
/*  66*/                stringbuffer.append(toWords(i));
/*  68*/            else
/*  68*/                alphaDefault(i, c, stringbuffer);
/*  70*/            break;

/*  73*/        case 79: // 'O'
/*  73*/            if(s.equals("ONE"))
/*  74*/                stringbuffer.append(toWords(i).toUpperCase());
/*  76*/            else
/*  76*/                alphaDefault(i, c, stringbuffer);
/*  78*/            break;

/*  81*/        case 913: 
/*  81*/            stringbuffer.append(toAlphaSequence(i, greekUpper));
/*  82*/            break;

/*  85*/        case 945: 
/*  85*/            stringbuffer.append(toAlphaSequence(i, greekLower));
/*  86*/            break;

/*  89*/        case 1040: 
/*  89*/            stringbuffer.append(toAlphaSequence(i, cyrillicUpper));
/*  90*/            break;

/*  93*/        case 1072: 
/*  93*/            stringbuffer.append(toAlphaSequence(i, cyrillicLower));
/*  94*/            break;

/*  97*/        case 1488: 
/*  97*/            stringbuffer.append(toAlphaSequence(i, hebrew));
/*  98*/            break;

/* 101*/        case 12354: 
/* 101*/            stringbuffer.append(toAlphaSequence(i, hiraganaA));
/* 102*/            break;

/* 105*/        case 12450: 
/* 105*/            stringbuffer.append(toAlphaSequence(i, katakanaA));
/* 106*/            break;

/* 109*/        case 12356: 
/* 109*/            stringbuffer.append(toAlphaSequence(i, hiraganaI));
/* 110*/            break;

/* 113*/        case 12452: 
/* 113*/            stringbuffer.append(toAlphaSequence(i, katakanaI));
/* 114*/            break;

/* 117*/        case 19968: 
/* 117*/            stringbuffer.append(toRadical(i, kanjiDigits, s, j, s1));
/* 118*/            break;

/* 122*/        default:
/* 122*/            if(Character.isDigit(c))
                    {
/* 124*/                int k = c - Character.getNumericValue(c);
/* 125*/                String s3 = "" + (char)k + (char)(k + 1) + (char)(k + 2) + (char)(k + 3) + (char)(k + 4) + (char)(k + 5) + (char)(k + 6) + (char)(k + 7) + (char)(k + 8) + (char)(k + 9);
/* 137*/                stringbuffer.append(toRadical(i, s3, s, j, s1));
/* 138*/                break;
                    }
/* 141*/            if(c < '\u1100')
/* 142*/                alphaDefault(i, c, stringbuffer);
/* 145*/            else
/* 145*/                stringbuffer.append(toRadical(i, westernDigits, s, j, s1));
                    break;
                }
/* 153*/        return stringbuffer.toString();
            }

            protected void alphaDefault(int i, char c, StringBuffer stringbuffer)
            {
/* 239*/        char c1 = c;
                int j;
/* 240*/        for(j = c; Character.isLetterOrDigit((char)(j + 1)); j++);
/* 243*/        stringbuffer.append(toAlpha(i, c1, j));
            }

            protected String toAlpha(int i, int j, int k)
            {
/* 252*/        if(i <= 0)
/* 252*/            return "" + i;
/* 253*/        int l = (k - j) + 1;
/* 254*/        char c = (char)((i - 1) % l + j);
/* 255*/        if(i > l)
/* 256*/            return toAlpha((i - 1) / l, j, k) + c;
/* 258*/        else
/* 258*/            return "" + c;
            }

            protected String toAlphaSequence(int i, String s)
            {
/* 268*/        if(i <= 0)
/* 268*/            return "" + i;
/* 269*/        int j = s.length();
/* 270*/        char c = s.charAt((i - 1) % j);
/* 271*/        if(i > j)
/* 272*/            return toAlphaSequence((i - 1) / j, s) + c;
/* 274*/        else
/* 274*/            return "" + c;
            }

            protected String toRadical(int i, String s, String s1, int j, String s2)
            {
/* 293*/        StringBuffer stringbuffer = new StringBuffer();
/* 294*/        StringBuffer stringbuffer1 = new StringBuffer();
/* 295*/        int k = s.length();
/* 297*/        String s3 = "";
/* 298*/        for(int l = i; l > 0; l /= k)
/* 300*/            s3 = s.charAt(l % k) + s3;

/* 304*/        for(int i1 = 0; i1 < s1.length() - s3.length(); i1++)
/* 305*/            stringbuffer1.append(s.charAt(0));

/* 307*/        stringbuffer1.append(s3);
/* 309*/        if(j > 0)
                {
/* 310*/            for(int j1 = 0; j1 < stringbuffer1.length(); j1++)
                    {
/* 311*/                if(j1 != 0 && (stringbuffer1.length() - j1) % j == 0)
/* 312*/                    stringbuffer.append(s2);
/* 314*/                stringbuffer.append(stringbuffer1.charAt(j1));
                    }

                } else
                {
/* 317*/            stringbuffer = stringbuffer1;
                }
/* 320*/        return stringbuffer.toString();
            }

            protected String toRoman(int i)
            {
/* 328*/        if(i <= 0 || i > 9999)
/* 328*/            return "" + i;
/* 329*/        else
/* 329*/            return romanThousands[i / 1000] + romanHundreds[(i / 100) % 10] + romanTens[(i / 10) % 10] + romanUnits[i % 10];
            }

            public String toWords(int i)
            {
/* 353*/        if(i >= 0x3b9aca00)
/* 354*/            return toWords(i / 0x3b9aca00) + " billion " + toWords(i % 0x3b9aca00);
/* 355*/        if(i >= 0xf4240)
/* 356*/            return toWords(i / 0xf4240) + " million " + toWords(i % 0xf4240);
/* 357*/        if(i >= 1000)
/* 358*/            return toWords(i / 1000) + " thousand " + toWords(i % 1000);
/* 359*/        if(i >= 100)
                {
/* 360*/            int j = i % 100;
/* 361*/            return toWords(i / 100) + " hundred" + (j != 0 ? " and " + toWords(j) : "");
                }
/* 364*/        if(i < 20)
                {
/* 364*/            return englishUnits[i];
                } else
                {
/* 365*/            int k = i % 10;
/* 366*/            return englishTens[i / 10] + (k != 0 ? " " + englishUnits[k] : "");
                }
            }

}
