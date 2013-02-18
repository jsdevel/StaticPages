// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XMLChar.java

package com.icl.saxon.om;

import java.util.Arrays;

public class XMLChar
{

            private static final byte CHARS[];
            public static final int MASK_VALID = 1;
            public static final int MASK_SPACE = 2;
            public static final int MASK_NAME_START = 4;
            public static final int MASK_NAME = 8;
            public static final int MASK_PUBID = 16;
            public static final int MASK_CONTENT = 32;
            public static final int MASK_NCNAME_START = 64;
            public static final int MASK_NCNAME = 128;

            private XMLChar()
            {
            }

            public static boolean isSupplemental(int i)
            {
/* 735*/        return i >= 0x10000 && i <= 0x10ffff;
            }

            public static int supplemental(char c, char c1)
            {
/* 746*/        return (c - 55296) * 1024 + (c1 - 56320) + 0x10000;
            }

            public static char highSurrogate(int i)
            {
/* 755*/        return (char)((i - 0x10000 >> 10) + 55296);
            }

            public static boolean isSurrogate(int i)
            {
/* 764*/        return (i & 0xf800) == 55296;
            }

            public static char lowSurrogate(int i)
            {
/* 773*/        return (char)((i - 0x10000 & 0x3ff) + 56320);
            }

            public static boolean isHighSurrogate(int i)
            {
/* 782*/        return 55296 <= i && i <= 56319;
            }

            public static boolean isLowSurrogate(int i)
            {
/* 791*/        return 56320 <= i && i <= 57343;
            }

            public static boolean isValid(int i)
            {
/* 806*/        return i < 0x10000 && (CHARS[i] & 1) != 0 || 0x10000 <= i && i <= 0x10ffff;
            }

            public static boolean isInvalid(int i)
            {
/* 816*/        return !isValid(i);
            }

            public static boolean isContent(int i)
            {
/* 825*/        return i < 0x10000 && (CHARS[i] & 0x20) != 0 || 0x10000 <= i && i <= 0x10ffff;
            }

            public static boolean isMarkup(int i)
            {
/* 836*/        return i == 60 || i == 38 || i == 37;
            }

            public static boolean isSpace(int i)
            {
/* 846*/        return i <= 32 && (CHARS[i] & 2) != 0;
            }

            public static boolean isNameStart(int i)
            {
/* 857*/        return i < 0x10000 && (CHARS[i] & 4) != 0;
            }

            public static boolean isName(int i)
            {
/* 868*/        return i < 0x10000 && (CHARS[i] & 8) != 0;
            }

            public static boolean isNCNameStart(int i)
            {
/* 879*/        return i < 0x10000 && (CHARS[i] & 0x40) != 0;
            }

            public static boolean isNCName(int i)
            {
/* 890*/        return i < 0x10000 && (CHARS[i] & 0x80) != 0;
            }

            public static boolean isPubid(int i)
            {
/* 901*/        return i < 0x10000 && (CHARS[i] & 0x10) != 0;
            }

            public static boolean isValidName(String s)
            {
/* 915*/        if(s.length() == 0)
/* 916*/            return false;
/* 917*/        char c = s.charAt(0);
/* 918*/        if(!isNameStart(c))
/* 919*/            return false;
/* 920*/        for(int i = 1; i < s.length(); i++)
                {
/* 921*/            char c1 = s.charAt(i);
/* 922*/            if(!isName(c1))
/* 923*/                return false;
                }

/* 926*/        return true;
            }

            public static boolean isValidNCName(String s)
            {
/* 942*/        if(s.length() == 0)
/* 943*/            return false;
/* 944*/        char c = s.charAt(0);
/* 945*/        if(!isNCNameStart(c))
/* 946*/            return false;
/* 947*/        for(int i = 1; i < s.length(); i++)
                {
/* 948*/            char c1 = s.charAt(i);
/* 949*/            if(!isNCName(c1))
/* 950*/                return false;
                }

/* 953*/        return true;
            }

            public static boolean isValidNmtoken(String s)
            {
/* 967*/        if(s.length() == 0)
/* 968*/            return false;
/* 969*/        for(int i = 0; i < s.length(); i++)
                {
/* 970*/            char c = s.charAt(i);
/* 971*/            if(!isName(c))
/* 972*/                return false;
                }

/* 975*/        return true;
            }

            public static boolean isValidIANAEncoding(String s)
            {
/* 993*/        if(s != null)
                {
/* 994*/            int i = s.length();
/* 995*/            if(i > 0)
                    {
/* 996*/                char c = s.charAt(0);
/* 997*/                if(c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z')
                        {
/* 998*/                    for(int j = 1; j < i; j++)
                            {
/* 999*/                        char c1 = s.charAt(j);
/*1000*/                        if((c1 < 'A' || c1 > 'Z') && (c1 < 'a' || c1 > 'z') && (c1 < '0' || c1 > '9') && c1 != '.' && c1 != '_' && c1 != '-')
/*1003*/                            return false;
                            }

/*1006*/                    return true;
                        }
                    }
                }
/*1010*/        return false;
            }

            public static boolean isValidJavaEncoding(String s)
            {
/*1022*/        if(s != null)
                {
/*1023*/            int i = s.length();
/*1024*/            if(i > 0)
                    {
/*1025*/                for(int j = 1; j < i; j++)
                        {
/*1026*/                    char c = s.charAt(j);
/*1027*/                    if((c < 'A' || c > 'Z') && (c < 'a' || c > 'z') && (c < '0' || c > '9') && c != '.' && c != '_' && c != '-')
/*1030*/                        return false;
                        }

/*1033*/                return true;
                    }
                }
/*1036*/        return false;
            }

            static 
            {
/*  58*/        CHARS = new byte[0x10000];
/* 103*/        CHARS[9] = 35;
/* 104*/        CHARS[10] = 19;
/* 105*/        CHARS[13] = 19;
/* 106*/        CHARS[32] = 51;
/* 107*/        CHARS[33] = 49;
/* 108*/        CHARS[34] = 33;
/* 109*/        Arrays.fill(CHARS, 35, 38, (byte)49);
/* 110*/        CHARS[38] = 1;
/* 111*/        Arrays.fill(CHARS, 39, 45, (byte)49);
/* 112*/        Arrays.fill(CHARS, 45, 47, (byte)-71);
/* 113*/        CHARS[47] = 49;
/* 114*/        Arrays.fill(CHARS, 48, 58, (byte)-71);
/* 115*/        CHARS[58] = 61;
/* 116*/        CHARS[59] = 49;
/* 117*/        CHARS[60] = 1;
/* 118*/        CHARS[61] = 49;
/* 119*/        CHARS[62] = 33;
/* 120*/        Arrays.fill(CHARS, 63, 65, (byte)49);
/* 121*/        Arrays.fill(CHARS, 65, 91, (byte)-3);
/* 122*/        Arrays.fill(CHARS, 91, 93, (byte)33);
/* 123*/        CHARS[93] = 1;
/* 124*/        CHARS[94] = 33;
/* 125*/        CHARS[95] = -3;
/* 126*/        CHARS[96] = 33;
/* 127*/        Arrays.fill(CHARS, 97, 123, (byte)-3);
/* 128*/        Arrays.fill(CHARS, 123, 183, (byte)33);
/* 129*/        CHARS[183] = -87;
/* 130*/        Arrays.fill(CHARS, 184, 192, (byte)33);
/* 131*/        Arrays.fill(CHARS, 192, 215, (byte)-19);
/* 132*/        CHARS[215] = 33;
/* 133*/        Arrays.fill(CHARS, 216, 247, (byte)-19);
/* 134*/        CHARS[247] = 33;
/* 135*/        Arrays.fill(CHARS, 248, 306, (byte)-19);
/* 136*/        Arrays.fill(CHARS, 306, 308, (byte)33);
/* 137*/        Arrays.fill(CHARS, 308, 319, (byte)-19);
/* 138*/        Arrays.fill(CHARS, 319, 321, (byte)33);
/* 139*/        Arrays.fill(CHARS, 321, 329, (byte)-19);
/* 140*/        CHARS[329] = 33;
/* 141*/        Arrays.fill(CHARS, 330, 383, (byte)-19);
/* 142*/        CHARS[383] = 33;
/* 143*/        Arrays.fill(CHARS, 384, 452, (byte)-19);
/* 144*/        Arrays.fill(CHARS, 452, 461, (byte)33);
/* 145*/        Arrays.fill(CHARS, 461, 497, (byte)-19);
/* 146*/        Arrays.fill(CHARS, 497, 500, (byte)33);
/* 147*/        Arrays.fill(CHARS, 500, 502, (byte)-19);
/* 148*/        Arrays.fill(CHARS, 502, 506, (byte)33);
/* 149*/        Arrays.fill(CHARS, 506, 536, (byte)-19);
/* 150*/        Arrays.fill(CHARS, 536, 592, (byte)33);
/* 151*/        Arrays.fill(CHARS, 592, 681, (byte)-19);
/* 152*/        Arrays.fill(CHARS, 681, 699, (byte)33);
/* 153*/        Arrays.fill(CHARS, 699, 706, (byte)-19);
/* 154*/        Arrays.fill(CHARS, 706, 720, (byte)33);
/* 155*/        Arrays.fill(CHARS, 720, 722, (byte)-87);
/* 156*/        Arrays.fill(CHARS, 722, 768, (byte)33);
/* 157*/        Arrays.fill(CHARS, 768, 838, (byte)-87);
/* 158*/        Arrays.fill(CHARS, 838, 864, (byte)33);
/* 159*/        Arrays.fill(CHARS, 864, 866, (byte)-87);
/* 160*/        Arrays.fill(CHARS, 866, 902, (byte)33);
/* 161*/        CHARS[902] = -19;
/* 162*/        CHARS[903] = -87;
/* 163*/        Arrays.fill(CHARS, 904, 907, (byte)-19);
/* 164*/        CHARS[907] = 33;
/* 165*/        CHARS[908] = -19;
/* 166*/        CHARS[909] = 33;
/* 167*/        Arrays.fill(CHARS, 910, 930, (byte)-19);
/* 168*/        CHARS[930] = 33;
/* 169*/        Arrays.fill(CHARS, 931, 975, (byte)-19);
/* 170*/        CHARS[975] = 33;
/* 171*/        Arrays.fill(CHARS, 976, 983, (byte)-19);
/* 172*/        Arrays.fill(CHARS, 983, 986, (byte)33);
/* 173*/        CHARS[986] = -19;
/* 174*/        CHARS[987] = 33;
/* 175*/        CHARS[988] = -19;
/* 176*/        CHARS[989] = 33;
/* 177*/        CHARS[990] = -19;
/* 178*/        CHARS[991] = 33;
/* 179*/        CHARS[992] = -19;
/* 180*/        CHARS[993] = 33;
/* 181*/        Arrays.fill(CHARS, 994, 1012, (byte)-19);
/* 182*/        Arrays.fill(CHARS, 1012, 1025, (byte)33);
/* 183*/        Arrays.fill(CHARS, 1025, 1037, (byte)-19);
/* 184*/        CHARS[1037] = 33;
/* 185*/        Arrays.fill(CHARS, 1038, 1104, (byte)-19);
/* 186*/        CHARS[1104] = 33;
/* 187*/        Arrays.fill(CHARS, 1105, 1117, (byte)-19);
/* 188*/        CHARS[1117] = 33;
/* 189*/        Arrays.fill(CHARS, 1118, 1154, (byte)-19);
/* 190*/        CHARS[1154] = 33;
/* 191*/        Arrays.fill(CHARS, 1155, 1159, (byte)-87);
/* 192*/        Arrays.fill(CHARS, 1159, 1168, (byte)33);
/* 193*/        Arrays.fill(CHARS, 1168, 1221, (byte)-19);
/* 194*/        Arrays.fill(CHARS, 1221, 1223, (byte)33);
/* 195*/        Arrays.fill(CHARS, 1223, 1225, (byte)-19);
/* 196*/        Arrays.fill(CHARS, 1225, 1227, (byte)33);
/* 197*/        Arrays.fill(CHARS, 1227, 1229, (byte)-19);
/* 198*/        Arrays.fill(CHARS, 1229, 1232, (byte)33);
/* 199*/        Arrays.fill(CHARS, 1232, 1260, (byte)-19);
/* 200*/        Arrays.fill(CHARS, 1260, 1262, (byte)33);
/* 201*/        Arrays.fill(CHARS, 1262, 1270, (byte)-19);
/* 202*/        Arrays.fill(CHARS, 1270, 1272, (byte)33);
/* 203*/        Arrays.fill(CHARS, 1272, 1274, (byte)-19);
/* 204*/        Arrays.fill(CHARS, 1274, 1329, (byte)33);
/* 205*/        Arrays.fill(CHARS, 1329, 1367, (byte)-19);
/* 206*/        Arrays.fill(CHARS, 1367, 1369, (byte)33);
/* 207*/        CHARS[1369] = -19;
/* 208*/        Arrays.fill(CHARS, 1370, 1377, (byte)33);
/* 209*/        Arrays.fill(CHARS, 1377, 1415, (byte)-19);
/* 210*/        Arrays.fill(CHARS, 1415, 1425, (byte)33);
/* 211*/        Arrays.fill(CHARS, 1425, 1442, (byte)-87);
/* 212*/        CHARS[1442] = 33;
/* 213*/        Arrays.fill(CHARS, 1443, 1466, (byte)-87);
/* 214*/        CHARS[1466] = 33;
/* 215*/        Arrays.fill(CHARS, 1467, 1470, (byte)-87);
/* 216*/        CHARS[1470] = 33;
/* 217*/        CHARS[1471] = -87;
/* 218*/        CHARS[1472] = 33;
/* 219*/        Arrays.fill(CHARS, 1473, 1475, (byte)-87);
/* 220*/        CHARS[1475] = 33;
/* 221*/        CHARS[1476] = -87;
/* 222*/        Arrays.fill(CHARS, 1477, 1488, (byte)33);
/* 223*/        Arrays.fill(CHARS, 1488, 1515, (byte)-19);
/* 224*/        Arrays.fill(CHARS, 1515, 1520, (byte)33);
/* 225*/        Arrays.fill(CHARS, 1520, 1523, (byte)-19);
/* 226*/        Arrays.fill(CHARS, 1523, 1569, (byte)33);
/* 227*/        Arrays.fill(CHARS, 1569, 1595, (byte)-19);
/* 228*/        Arrays.fill(CHARS, 1595, 1600, (byte)33);
/* 229*/        CHARS[1600] = -87;
/* 230*/        Arrays.fill(CHARS, 1601, 1611, (byte)-19);
/* 231*/        Arrays.fill(CHARS, 1611, 1619, (byte)-87);
/* 232*/        Arrays.fill(CHARS, 1619, 1632, (byte)33);
/* 233*/        Arrays.fill(CHARS, 1632, 1642, (byte)-87);
/* 234*/        Arrays.fill(CHARS, 1642, 1648, (byte)33);
/* 235*/        CHARS[1648] = -87;
/* 236*/        Arrays.fill(CHARS, 1649, 1720, (byte)-19);
/* 237*/        Arrays.fill(CHARS, 1720, 1722, (byte)33);
/* 238*/        Arrays.fill(CHARS, 1722, 1727, (byte)-19);
/* 239*/        CHARS[1727] = 33;
/* 240*/        Arrays.fill(CHARS, 1728, 1743, (byte)-19);
/* 241*/        CHARS[1743] = 33;
/* 242*/        Arrays.fill(CHARS, 1744, 1748, (byte)-19);
/* 243*/        CHARS[1748] = 33;
/* 244*/        CHARS[1749] = -19;
/* 245*/        Arrays.fill(CHARS, 1750, 1765, (byte)-87);
/* 246*/        Arrays.fill(CHARS, 1765, 1767, (byte)-19);
/* 247*/        Arrays.fill(CHARS, 1767, 1769, (byte)-87);
/* 248*/        CHARS[1769] = 33;
/* 249*/        Arrays.fill(CHARS, 1770, 1774, (byte)-87);
/* 250*/        Arrays.fill(CHARS, 1774, 1776, (byte)33);
/* 251*/        Arrays.fill(CHARS, 1776, 1786, (byte)-87);
/* 252*/        Arrays.fill(CHARS, 1786, 2305, (byte)33);
/* 253*/        Arrays.fill(CHARS, 2305, 2308, (byte)-87);
/* 254*/        CHARS[2308] = 33;
/* 255*/        Arrays.fill(CHARS, 2309, 2362, (byte)-19);
/* 256*/        Arrays.fill(CHARS, 2362, 2364, (byte)33);
/* 257*/        CHARS[2364] = -87;
/* 258*/        CHARS[2365] = -19;
/* 259*/        Arrays.fill(CHARS, 2366, 2382, (byte)-87);
/* 260*/        Arrays.fill(CHARS, 2382, 2385, (byte)33);
/* 261*/        Arrays.fill(CHARS, 2385, 2389, (byte)-87);
/* 262*/        Arrays.fill(CHARS, 2389, 2392, (byte)33);
/* 263*/        Arrays.fill(CHARS, 2392, 2402, (byte)-19);
/* 264*/        Arrays.fill(CHARS, 2402, 2404, (byte)-87);
/* 265*/        Arrays.fill(CHARS, 2404, 2406, (byte)33);
/* 266*/        Arrays.fill(CHARS, 2406, 2416, (byte)-87);
/* 267*/        Arrays.fill(CHARS, 2416, 2433, (byte)33);
/* 268*/        Arrays.fill(CHARS, 2433, 2436, (byte)-87);
/* 269*/        CHARS[2436] = 33;
/* 270*/        Arrays.fill(CHARS, 2437, 2445, (byte)-19);
/* 271*/        Arrays.fill(CHARS, 2445, 2447, (byte)33);
/* 272*/        Arrays.fill(CHARS, 2447, 2449, (byte)-19);
/* 273*/        Arrays.fill(CHARS, 2449, 2451, (byte)33);
/* 274*/        Arrays.fill(CHARS, 2451, 2473, (byte)-19);
/* 275*/        CHARS[2473] = 33;
/* 276*/        Arrays.fill(CHARS, 2474, 2481, (byte)-19);
/* 277*/        CHARS[2481] = 33;
/* 278*/        CHARS[2482] = -19;
/* 279*/        Arrays.fill(CHARS, 2483, 2486, (byte)33);
/* 280*/        Arrays.fill(CHARS, 2486, 2490, (byte)-19);
/* 281*/        Arrays.fill(CHARS, 2490, 2492, (byte)33);
/* 282*/        CHARS[2492] = -87;
/* 283*/        CHARS[2493] = 33;
/* 284*/        Arrays.fill(CHARS, 2494, 2501, (byte)-87);
/* 285*/        Arrays.fill(CHARS, 2501, 2503, (byte)33);
/* 286*/        Arrays.fill(CHARS, 2503, 2505, (byte)-87);
/* 287*/        Arrays.fill(CHARS, 2505, 2507, (byte)33);
/* 288*/        Arrays.fill(CHARS, 2507, 2510, (byte)-87);
/* 289*/        Arrays.fill(CHARS, 2510, 2519, (byte)33);
/* 290*/        CHARS[2519] = -87;
/* 291*/        Arrays.fill(CHARS, 2520, 2524, (byte)33);
/* 292*/        Arrays.fill(CHARS, 2524, 2526, (byte)-19);
/* 293*/        CHARS[2526] = 33;
/* 294*/        Arrays.fill(CHARS, 2527, 2530, (byte)-19);
/* 295*/        Arrays.fill(CHARS, 2530, 2532, (byte)-87);
/* 296*/        Arrays.fill(CHARS, 2532, 2534, (byte)33);
/* 297*/        Arrays.fill(CHARS, 2534, 2544, (byte)-87);
/* 298*/        Arrays.fill(CHARS, 2544, 2546, (byte)-19);
/* 299*/        Arrays.fill(CHARS, 2546, 2562, (byte)33);
/* 300*/        CHARS[2562] = -87;
/* 301*/        Arrays.fill(CHARS, 2563, 2565, (byte)33);
/* 302*/        Arrays.fill(CHARS, 2565, 2571, (byte)-19);
/* 303*/        Arrays.fill(CHARS, 2571, 2575, (byte)33);
/* 304*/        Arrays.fill(CHARS, 2575, 2577, (byte)-19);
/* 305*/        Arrays.fill(CHARS, 2577, 2579, (byte)33);
/* 306*/        Arrays.fill(CHARS, 2579, 2601, (byte)-19);
/* 307*/        CHARS[2601] = 33;
/* 308*/        Arrays.fill(CHARS, 2602, 2609, (byte)-19);
/* 309*/        CHARS[2609] = 33;
/* 310*/        Arrays.fill(CHARS, 2610, 2612, (byte)-19);
/* 311*/        CHARS[2612] = 33;
/* 312*/        Arrays.fill(CHARS, 2613, 2615, (byte)-19);
/* 313*/        CHARS[2615] = 33;
/* 314*/        Arrays.fill(CHARS, 2616, 2618, (byte)-19);
/* 315*/        Arrays.fill(CHARS, 2618, 2620, (byte)33);
/* 316*/        CHARS[2620] = -87;
/* 317*/        CHARS[2621] = 33;
/* 318*/        Arrays.fill(CHARS, 2622, 2627, (byte)-87);
/* 319*/        Arrays.fill(CHARS, 2627, 2631, (byte)33);
/* 320*/        Arrays.fill(CHARS, 2631, 2633, (byte)-87);
/* 321*/        Arrays.fill(CHARS, 2633, 2635, (byte)33);
/* 322*/        Arrays.fill(CHARS, 2635, 2638, (byte)-87);
/* 323*/        Arrays.fill(CHARS, 2638, 2649, (byte)33);
/* 324*/        Arrays.fill(CHARS, 2649, 2653, (byte)-19);
/* 325*/        CHARS[2653] = 33;
/* 326*/        CHARS[2654] = -19;
/* 327*/        Arrays.fill(CHARS, 2655, 2662, (byte)33);
/* 328*/        Arrays.fill(CHARS, 2662, 2674, (byte)-87);
/* 329*/        Arrays.fill(CHARS, 2674, 2677, (byte)-19);
/* 330*/        Arrays.fill(CHARS, 2677, 2689, (byte)33);
/* 331*/        Arrays.fill(CHARS, 2689, 2692, (byte)-87);
/* 332*/        CHARS[2692] = 33;
/* 333*/        Arrays.fill(CHARS, 2693, 2700, (byte)-19);
/* 334*/        CHARS[2700] = 33;
/* 335*/        CHARS[2701] = -19;
/* 336*/        CHARS[2702] = 33;
/* 337*/        Arrays.fill(CHARS, 2703, 2706, (byte)-19);
/* 338*/        CHARS[2706] = 33;
/* 339*/        Arrays.fill(CHARS, 2707, 2729, (byte)-19);
/* 340*/        CHARS[2729] = 33;
/* 341*/        Arrays.fill(CHARS, 2730, 2737, (byte)-19);
/* 342*/        CHARS[2737] = 33;
/* 343*/        Arrays.fill(CHARS, 2738, 2740, (byte)-19);
/* 344*/        CHARS[2740] = 33;
/* 345*/        Arrays.fill(CHARS, 2741, 2746, (byte)-19);
/* 346*/        Arrays.fill(CHARS, 2746, 2748, (byte)33);
/* 347*/        CHARS[2748] = -87;
/* 348*/        CHARS[2749] = -19;
/* 349*/        Arrays.fill(CHARS, 2750, 2758, (byte)-87);
/* 350*/        CHARS[2758] = 33;
/* 351*/        Arrays.fill(CHARS, 2759, 2762, (byte)-87);
/* 352*/        CHARS[2762] = 33;
/* 353*/        Arrays.fill(CHARS, 2763, 2766, (byte)-87);
/* 354*/        Arrays.fill(CHARS, 2766, 2784, (byte)33);
/* 355*/        CHARS[2784] = -19;
/* 356*/        Arrays.fill(CHARS, 2785, 2790, (byte)33);
/* 357*/        Arrays.fill(CHARS, 2790, 2800, (byte)-87);
/* 358*/        Arrays.fill(CHARS, 2800, 2817, (byte)33);
/* 359*/        Arrays.fill(CHARS, 2817, 2820, (byte)-87);
/* 360*/        CHARS[2820] = 33;
/* 361*/        Arrays.fill(CHARS, 2821, 2829, (byte)-19);
/* 362*/        Arrays.fill(CHARS, 2829, 2831, (byte)33);
/* 363*/        Arrays.fill(CHARS, 2831, 2833, (byte)-19);
/* 364*/        Arrays.fill(CHARS, 2833, 2835, (byte)33);
/* 365*/        Arrays.fill(CHARS, 2835, 2857, (byte)-19);
/* 366*/        CHARS[2857] = 33;
/* 367*/        Arrays.fill(CHARS, 2858, 2865, (byte)-19);
/* 368*/        CHARS[2865] = 33;
/* 369*/        Arrays.fill(CHARS, 2866, 2868, (byte)-19);
/* 370*/        Arrays.fill(CHARS, 2868, 2870, (byte)33);
/* 371*/        Arrays.fill(CHARS, 2870, 2874, (byte)-19);
/* 372*/        Arrays.fill(CHARS, 2874, 2876, (byte)33);
/* 373*/        CHARS[2876] = -87;
/* 374*/        CHARS[2877] = -19;
/* 375*/        Arrays.fill(CHARS, 2878, 2884, (byte)-87);
/* 376*/        Arrays.fill(CHARS, 2884, 2887, (byte)33);
/* 377*/        Arrays.fill(CHARS, 2887, 2889, (byte)-87);
/* 378*/        Arrays.fill(CHARS, 2889, 2891, (byte)33);
/* 379*/        Arrays.fill(CHARS, 2891, 2894, (byte)-87);
/* 380*/        Arrays.fill(CHARS, 2894, 2902, (byte)33);
/* 381*/        Arrays.fill(CHARS, 2902, 2904, (byte)-87);
/* 382*/        Arrays.fill(CHARS, 2904, 2908, (byte)33);
/* 383*/        Arrays.fill(CHARS, 2908, 2910, (byte)-19);
/* 384*/        CHARS[2910] = 33;
/* 385*/        Arrays.fill(CHARS, 2911, 2914, (byte)-19);
/* 386*/        Arrays.fill(CHARS, 2914, 2918, (byte)33);
/* 387*/        Arrays.fill(CHARS, 2918, 2928, (byte)-87);
/* 388*/        Arrays.fill(CHARS, 2928, 2946, (byte)33);
/* 389*/        Arrays.fill(CHARS, 2946, 2948, (byte)-87);
/* 390*/        CHARS[2948] = 33;
/* 391*/        Arrays.fill(CHARS, 2949, 2955, (byte)-19);
/* 392*/        Arrays.fill(CHARS, 2955, 2958, (byte)33);
/* 393*/        Arrays.fill(CHARS, 2958, 2961, (byte)-19);
/* 394*/        CHARS[2961] = 33;
/* 395*/        Arrays.fill(CHARS, 2962, 2966, (byte)-19);
/* 396*/        Arrays.fill(CHARS, 2966, 2969, (byte)33);
/* 397*/        Arrays.fill(CHARS, 2969, 2971, (byte)-19);
/* 398*/        CHARS[2971] = 33;
/* 399*/        CHARS[2972] = -19;
/* 400*/        CHARS[2973] = 33;
/* 401*/        Arrays.fill(CHARS, 2974, 2976, (byte)-19);
/* 402*/        Arrays.fill(CHARS, 2976, 2979, (byte)33);
/* 403*/        Arrays.fill(CHARS, 2979, 2981, (byte)-19);
/* 404*/        Arrays.fill(CHARS, 2981, 2984, (byte)33);
/* 405*/        Arrays.fill(CHARS, 2984, 2987, (byte)-19);
/* 406*/        Arrays.fill(CHARS, 2987, 2990, (byte)33);
/* 407*/        Arrays.fill(CHARS, 2990, 2998, (byte)-19);
/* 408*/        CHARS[2998] = 33;
/* 409*/        Arrays.fill(CHARS, 2999, 3002, (byte)-19);
/* 410*/        Arrays.fill(CHARS, 3002, 3006, (byte)33);
/* 411*/        Arrays.fill(CHARS, 3006, 3011, (byte)-87);
/* 412*/        Arrays.fill(CHARS, 3011, 3014, (byte)33);
/* 413*/        Arrays.fill(CHARS, 3014, 3017, (byte)-87);
/* 414*/        CHARS[3017] = 33;
/* 415*/        Arrays.fill(CHARS, 3018, 3022, (byte)-87);
/* 416*/        Arrays.fill(CHARS, 3022, 3031, (byte)33);
/* 417*/        CHARS[3031] = -87;
/* 418*/        Arrays.fill(CHARS, 3032, 3047, (byte)33);
/* 419*/        Arrays.fill(CHARS, 3047, 3056, (byte)-87);
/* 420*/        Arrays.fill(CHARS, 3056, 3073, (byte)33);
/* 421*/        Arrays.fill(CHARS, 3073, 3076, (byte)-87);
/* 422*/        CHARS[3076] = 33;
/* 423*/        Arrays.fill(CHARS, 3077, 3085, (byte)-19);
/* 424*/        CHARS[3085] = 33;
/* 425*/        Arrays.fill(CHARS, 3086, 3089, (byte)-19);
/* 426*/        CHARS[3089] = 33;
/* 427*/        Arrays.fill(CHARS, 3090, 3113, (byte)-19);
/* 428*/        CHARS[3113] = 33;
/* 429*/        Arrays.fill(CHARS, 3114, 3124, (byte)-19);
/* 430*/        CHARS[3124] = 33;
/* 431*/        Arrays.fill(CHARS, 3125, 3130, (byte)-19);
/* 432*/        Arrays.fill(CHARS, 3130, 3134, (byte)33);
/* 433*/        Arrays.fill(CHARS, 3134, 3141, (byte)-87);
/* 434*/        CHARS[3141] = 33;
/* 435*/        Arrays.fill(CHARS, 3142, 3145, (byte)-87);
/* 436*/        CHARS[3145] = 33;
/* 437*/        Arrays.fill(CHARS, 3146, 3150, (byte)-87);
/* 438*/        Arrays.fill(CHARS, 3150, 3157, (byte)33);
/* 439*/        Arrays.fill(CHARS, 3157, 3159, (byte)-87);
/* 440*/        Arrays.fill(CHARS, 3159, 3168, (byte)33);
/* 441*/        Arrays.fill(CHARS, 3168, 3170, (byte)-19);
/* 442*/        Arrays.fill(CHARS, 3170, 3174, (byte)33);
/* 443*/        Arrays.fill(CHARS, 3174, 3184, (byte)-87);
/* 444*/        Arrays.fill(CHARS, 3184, 3202, (byte)33);
/* 445*/        Arrays.fill(CHARS, 3202, 3204, (byte)-87);
/* 446*/        CHARS[3204] = 33;
/* 447*/        Arrays.fill(CHARS, 3205, 3213, (byte)-19);
/* 448*/        CHARS[3213] = 33;
/* 449*/        Arrays.fill(CHARS, 3214, 3217, (byte)-19);
/* 450*/        CHARS[3217] = 33;
/* 451*/        Arrays.fill(CHARS, 3218, 3241, (byte)-19);
/* 452*/        CHARS[3241] = 33;
/* 453*/        Arrays.fill(CHARS, 3242, 3252, (byte)-19);
/* 454*/        CHARS[3252] = 33;
/* 455*/        Arrays.fill(CHARS, 3253, 3258, (byte)-19);
/* 456*/        Arrays.fill(CHARS, 3258, 3262, (byte)33);
/* 457*/        Arrays.fill(CHARS, 3262, 3269, (byte)-87);
/* 458*/        CHARS[3269] = 33;
/* 459*/        Arrays.fill(CHARS, 3270, 3273, (byte)-87);
/* 460*/        CHARS[3273] = 33;
/* 461*/        Arrays.fill(CHARS, 3274, 3278, (byte)-87);
/* 462*/        Arrays.fill(CHARS, 3278, 3285, (byte)33);
/* 463*/        Arrays.fill(CHARS, 3285, 3287, (byte)-87);
/* 464*/        Arrays.fill(CHARS, 3287, 3294, (byte)33);
/* 465*/        CHARS[3294] = -19;
/* 466*/        CHARS[3295] = 33;
/* 467*/        Arrays.fill(CHARS, 3296, 3298, (byte)-19);
/* 468*/        Arrays.fill(CHARS, 3298, 3302, (byte)33);
/* 469*/        Arrays.fill(CHARS, 3302, 3312, (byte)-87);
/* 470*/        Arrays.fill(CHARS, 3312, 3330, (byte)33);
/* 471*/        Arrays.fill(CHARS, 3330, 3332, (byte)-87);
/* 472*/        CHARS[3332] = 33;
/* 473*/        Arrays.fill(CHARS, 3333, 3341, (byte)-19);
/* 474*/        CHARS[3341] = 33;
/* 475*/        Arrays.fill(CHARS, 3342, 3345, (byte)-19);
/* 476*/        CHARS[3345] = 33;
/* 477*/        Arrays.fill(CHARS, 3346, 3369, (byte)-19);
/* 478*/        CHARS[3369] = 33;
/* 479*/        Arrays.fill(CHARS, 3370, 3386, (byte)-19);
/* 480*/        Arrays.fill(CHARS, 3386, 3390, (byte)33);
/* 481*/        Arrays.fill(CHARS, 3390, 3396, (byte)-87);
/* 482*/        Arrays.fill(CHARS, 3396, 3398, (byte)33);
/* 483*/        Arrays.fill(CHARS, 3398, 3401, (byte)-87);
/* 484*/        CHARS[3401] = 33;
/* 485*/        Arrays.fill(CHARS, 3402, 3406, (byte)-87);
/* 486*/        Arrays.fill(CHARS, 3406, 3415, (byte)33);
/* 487*/        CHARS[3415] = -87;
/* 488*/        Arrays.fill(CHARS, 3416, 3424, (byte)33);
/* 489*/        Arrays.fill(CHARS, 3424, 3426, (byte)-19);
/* 490*/        Arrays.fill(CHARS, 3426, 3430, (byte)33);
/* 491*/        Arrays.fill(CHARS, 3430, 3440, (byte)-87);
/* 492*/        Arrays.fill(CHARS, 3440, 3585, (byte)33);
/* 493*/        Arrays.fill(CHARS, 3585, 3631, (byte)-19);
/* 494*/        CHARS[3631] = 33;
/* 495*/        CHARS[3632] = -19;
/* 496*/        CHARS[3633] = -87;
/* 497*/        Arrays.fill(CHARS, 3634, 3636, (byte)-19);
/* 498*/        Arrays.fill(CHARS, 3636, 3643, (byte)-87);
/* 499*/        Arrays.fill(CHARS, 3643, 3648, (byte)33);
/* 500*/        Arrays.fill(CHARS, 3648, 3654, (byte)-19);
/* 501*/        Arrays.fill(CHARS, 3654, 3663, (byte)-87);
/* 502*/        CHARS[3663] = 33;
/* 503*/        Arrays.fill(CHARS, 3664, 3674, (byte)-87);
/* 504*/        Arrays.fill(CHARS, 3674, 3713, (byte)33);
/* 505*/        Arrays.fill(CHARS, 3713, 3715, (byte)-19);
/* 506*/        CHARS[3715] = 33;
/* 507*/        CHARS[3716] = -19;
/* 508*/        Arrays.fill(CHARS, 3717, 3719, (byte)33);
/* 509*/        Arrays.fill(CHARS, 3719, 3721, (byte)-19);
/* 510*/        CHARS[3721] = 33;
/* 511*/        CHARS[3722] = -19;
/* 512*/        Arrays.fill(CHARS, 3723, 3725, (byte)33);
/* 513*/        CHARS[3725] = -19;
/* 514*/        Arrays.fill(CHARS, 3726, 3732, (byte)33);
/* 515*/        Arrays.fill(CHARS, 3732, 3736, (byte)-19);
/* 516*/        CHARS[3736] = 33;
/* 517*/        Arrays.fill(CHARS, 3737, 3744, (byte)-19);
/* 518*/        CHARS[3744] = 33;
/* 519*/        Arrays.fill(CHARS, 3745, 3748, (byte)-19);
/* 520*/        CHARS[3748] = 33;
/* 521*/        CHARS[3749] = -19;
/* 522*/        CHARS[3750] = 33;
/* 523*/        CHARS[3751] = -19;
/* 524*/        Arrays.fill(CHARS, 3752, 3754, (byte)33);
/* 525*/        Arrays.fill(CHARS, 3754, 3756, (byte)-19);
/* 526*/        CHARS[3756] = 33;
/* 527*/        Arrays.fill(CHARS, 3757, 3759, (byte)-19);
/* 528*/        CHARS[3759] = 33;
/* 529*/        CHARS[3760] = -19;
/* 530*/        CHARS[3761] = -87;
/* 531*/        Arrays.fill(CHARS, 3762, 3764, (byte)-19);
/* 532*/        Arrays.fill(CHARS, 3764, 3770, (byte)-87);
/* 533*/        CHARS[3770] = 33;
/* 534*/        Arrays.fill(CHARS, 3771, 3773, (byte)-87);
/* 535*/        CHARS[3773] = -19;
/* 536*/        Arrays.fill(CHARS, 3774, 3776, (byte)33);
/* 537*/        Arrays.fill(CHARS, 3776, 3781, (byte)-19);
/* 538*/        CHARS[3781] = 33;
/* 539*/        CHARS[3782] = -87;
/* 540*/        CHARS[3783] = 33;
/* 541*/        Arrays.fill(CHARS, 3784, 3790, (byte)-87);
/* 542*/        Arrays.fill(CHARS, 3790, 3792, (byte)33);
/* 543*/        Arrays.fill(CHARS, 3792, 3802, (byte)-87);
/* 544*/        Arrays.fill(CHARS, 3802, 3864, (byte)33);
/* 545*/        Arrays.fill(CHARS, 3864, 3866, (byte)-87);
/* 546*/        Arrays.fill(CHARS, 3866, 3872, (byte)33);
/* 547*/        Arrays.fill(CHARS, 3872, 3882, (byte)-87);
/* 548*/        Arrays.fill(CHARS, 3882, 3893, (byte)33);
/* 549*/        CHARS[3893] = -87;
/* 550*/        CHARS[3894] = 33;
/* 551*/        CHARS[3895] = -87;
/* 552*/        CHARS[3896] = 33;
/* 553*/        CHARS[3897] = -87;
/* 554*/        Arrays.fill(CHARS, 3898, 3902, (byte)33);
/* 555*/        Arrays.fill(CHARS, 3902, 3904, (byte)-87);
/* 556*/        Arrays.fill(CHARS, 3904, 3912, (byte)-19);
/* 557*/        CHARS[3912] = 33;
/* 558*/        Arrays.fill(CHARS, 3913, 3946, (byte)-19);
/* 559*/        Arrays.fill(CHARS, 3946, 3953, (byte)33);
/* 560*/        Arrays.fill(CHARS, 3953, 3973, (byte)-87);
/* 561*/        CHARS[3973] = 33;
/* 562*/        Arrays.fill(CHARS, 3974, 3980, (byte)-87);
/* 563*/        Arrays.fill(CHARS, 3980, 3984, (byte)33);
/* 564*/        Arrays.fill(CHARS, 3984, 3990, (byte)-87);
/* 565*/        CHARS[3990] = 33;
/* 566*/        CHARS[3991] = -87;
/* 567*/        CHARS[3992] = 33;
/* 568*/        Arrays.fill(CHARS, 3993, 4014, (byte)-87);
/* 569*/        Arrays.fill(CHARS, 4014, 4017, (byte)33);
/* 570*/        Arrays.fill(CHARS, 4017, 4024, (byte)-87);
/* 571*/        CHARS[4024] = 33;
/* 572*/        CHARS[4025] = -87;
/* 573*/        Arrays.fill(CHARS, 4026, 4256, (byte)33);
/* 574*/        Arrays.fill(CHARS, 4256, 4294, (byte)-19);
/* 575*/        Arrays.fill(CHARS, 4294, 4304, (byte)33);
/* 576*/        Arrays.fill(CHARS, 4304, 4343, (byte)-19);
/* 577*/        Arrays.fill(CHARS, 4343, 4352, (byte)33);
/* 578*/        CHARS[4352] = -19;
/* 579*/        CHARS[4353] = 33;
/* 580*/        Arrays.fill(CHARS, 4354, 4356, (byte)-19);
/* 581*/        CHARS[4356] = 33;
/* 582*/        Arrays.fill(CHARS, 4357, 4360, (byte)-19);
/* 583*/        CHARS[4360] = 33;
/* 584*/        CHARS[4361] = -19;
/* 585*/        CHARS[4362] = 33;
/* 586*/        Arrays.fill(CHARS, 4363, 4365, (byte)-19);
/* 587*/        CHARS[4365] = 33;
/* 588*/        Arrays.fill(CHARS, 4366, 4371, (byte)-19);
/* 589*/        Arrays.fill(CHARS, 4371, 4412, (byte)33);
/* 590*/        CHARS[4412] = -19;
/* 591*/        CHARS[4413] = 33;
/* 592*/        CHARS[4414] = -19;
/* 593*/        CHARS[4415] = 33;
/* 594*/        CHARS[4416] = -19;
/* 595*/        Arrays.fill(CHARS, 4417, 4428, (byte)33);
/* 596*/        CHARS[4428] = -19;
/* 597*/        CHARS[4429] = 33;
/* 598*/        CHARS[4430] = -19;
/* 599*/        CHARS[4431] = 33;
/* 600*/        CHARS[4432] = -19;
/* 601*/        Arrays.fill(CHARS, 4433, 4436, (byte)33);
/* 602*/        Arrays.fill(CHARS, 4436, 4438, (byte)-19);
/* 603*/        Arrays.fill(CHARS, 4438, 4441, (byte)33);
/* 604*/        CHARS[4441] = -19;
/* 605*/        Arrays.fill(CHARS, 4442, 4447, (byte)33);
/* 606*/        Arrays.fill(CHARS, 4447, 4450, (byte)-19);
/* 607*/        CHARS[4450] = 33;
/* 608*/        CHARS[4451] = -19;
/* 609*/        CHARS[4452] = 33;
/* 610*/        CHARS[4453] = -19;
/* 611*/        CHARS[4454] = 33;
/* 612*/        CHARS[4455] = -19;
/* 613*/        CHARS[4456] = 33;
/* 614*/        CHARS[4457] = -19;
/* 615*/        Arrays.fill(CHARS, 4458, 4461, (byte)33);
/* 616*/        Arrays.fill(CHARS, 4461, 4463, (byte)-19);
/* 617*/        Arrays.fill(CHARS, 4463, 4466, (byte)33);
/* 618*/        Arrays.fill(CHARS, 4466, 4468, (byte)-19);
/* 619*/        CHARS[4468] = 33;
/* 620*/        CHARS[4469] = -19;
/* 621*/        Arrays.fill(CHARS, 4470, 4510, (byte)33);
/* 622*/        CHARS[4510] = -19;
/* 623*/        Arrays.fill(CHARS, 4511, 4520, (byte)33);
/* 624*/        CHARS[4520] = -19;
/* 625*/        Arrays.fill(CHARS, 4521, 4523, (byte)33);
/* 626*/        CHARS[4523] = -19;
/* 627*/        Arrays.fill(CHARS, 4524, 4526, (byte)33);
/* 628*/        Arrays.fill(CHARS, 4526, 4528, (byte)-19);
/* 629*/        Arrays.fill(CHARS, 4528, 4535, (byte)33);
/* 630*/        Arrays.fill(CHARS, 4535, 4537, (byte)-19);
/* 631*/        CHARS[4537] = 33;
/* 632*/        CHARS[4538] = -19;
/* 633*/        CHARS[4539] = 33;
/* 634*/        Arrays.fill(CHARS, 4540, 4547, (byte)-19);
/* 635*/        Arrays.fill(CHARS, 4547, 4587, (byte)33);
/* 636*/        CHARS[4587] = -19;
/* 637*/        Arrays.fill(CHARS, 4588, 4592, (byte)33);
/* 638*/        CHARS[4592] = -19;
/* 639*/        Arrays.fill(CHARS, 4593, 4601, (byte)33);
/* 640*/        CHARS[4601] = -19;
/* 641*/        Arrays.fill(CHARS, 4602, 7680, (byte)33);
/* 642*/        Arrays.fill(CHARS, 7680, 7836, (byte)-19);
/* 643*/        Arrays.fill(CHARS, 7836, 7840, (byte)33);
/* 644*/        Arrays.fill(CHARS, 7840, 7930, (byte)-19);
/* 645*/        Arrays.fill(CHARS, 7930, 7936, (byte)33);
/* 646*/        Arrays.fill(CHARS, 7936, 7958, (byte)-19);
/* 647*/        Arrays.fill(CHARS, 7958, 7960, (byte)33);
/* 648*/        Arrays.fill(CHARS, 7960, 7966, (byte)-19);
/* 649*/        Arrays.fill(CHARS, 7966, 7968, (byte)33);
/* 650*/        Arrays.fill(CHARS, 7968, 8006, (byte)-19);
/* 651*/        Arrays.fill(CHARS, 8006, 8008, (byte)33);
/* 652*/        Arrays.fill(CHARS, 8008, 8014, (byte)-19);
/* 653*/        Arrays.fill(CHARS, 8014, 8016, (byte)33);
/* 654*/        Arrays.fill(CHARS, 8016, 8024, (byte)-19);
/* 655*/        CHARS[8024] = 33;
/* 656*/        CHARS[8025] = -19;
/* 657*/        CHARS[8026] = 33;
/* 658*/        CHARS[8027] = -19;
/* 659*/        CHARS[8028] = 33;
/* 660*/        CHARS[8029] = -19;
/* 661*/        CHARS[8030] = 33;
/* 662*/        Arrays.fill(CHARS, 8031, 8062, (byte)-19);
/* 663*/        Arrays.fill(CHARS, 8062, 8064, (byte)33);
/* 664*/        Arrays.fill(CHARS, 8064, 8117, (byte)-19);
/* 665*/        CHARS[8117] = 33;
/* 666*/        Arrays.fill(CHARS, 8118, 8125, (byte)-19);
/* 667*/        CHARS[8125] = 33;
/* 668*/        CHARS[8126] = -19;
/* 669*/        Arrays.fill(CHARS, 8127, 8130, (byte)33);
/* 670*/        Arrays.fill(CHARS, 8130, 8133, (byte)-19);
/* 671*/        CHARS[8133] = 33;
/* 672*/        Arrays.fill(CHARS, 8134, 8141, (byte)-19);
/* 673*/        Arrays.fill(CHARS, 8141, 8144, (byte)33);
/* 674*/        Arrays.fill(CHARS, 8144, 8148, (byte)-19);
/* 675*/        Arrays.fill(CHARS, 8148, 8150, (byte)33);
/* 676*/        Arrays.fill(CHARS, 8150, 8156, (byte)-19);
/* 677*/        Arrays.fill(CHARS, 8156, 8160, (byte)33);
/* 678*/        Arrays.fill(CHARS, 8160, 8173, (byte)-19);
/* 679*/        Arrays.fill(CHARS, 8173, 8178, (byte)33);
/* 680*/        Arrays.fill(CHARS, 8178, 8181, (byte)-19);
/* 681*/        CHARS[8181] = 33;
/* 682*/        Arrays.fill(CHARS, 8182, 8189, (byte)-19);
/* 683*/        Arrays.fill(CHARS, 8189, 8400, (byte)33);
/* 684*/        Arrays.fill(CHARS, 8400, 8413, (byte)-87);
/* 685*/        Arrays.fill(CHARS, 8413, 8417, (byte)33);
/* 686*/        CHARS[8417] = -87;
/* 687*/        Arrays.fill(CHARS, 8418, 8486, (byte)33);
/* 688*/        CHARS[8486] = -19;
/* 689*/        Arrays.fill(CHARS, 8487, 8490, (byte)33);
/* 690*/        Arrays.fill(CHARS, 8490, 8492, (byte)-19);
/* 691*/        Arrays.fill(CHARS, 8492, 8494, (byte)33);
/* 692*/        CHARS[8494] = -19;
/* 693*/        Arrays.fill(CHARS, 8495, 8576, (byte)33);
/* 694*/        Arrays.fill(CHARS, 8576, 8579, (byte)-19);
/* 695*/        Arrays.fill(CHARS, 8579, 12293, (byte)33);
/* 696*/        CHARS[12293] = -87;
/* 697*/        CHARS[12294] = 33;
/* 698*/        CHARS[12295] = -19;
/* 699*/        Arrays.fill(CHARS, 12296, 12321, (byte)33);
/* 700*/        Arrays.fill(CHARS, 12321, 12330, (byte)-19);
/* 701*/        Arrays.fill(CHARS, 12330, 12336, (byte)-87);
/* 702*/        CHARS[12336] = 33;
/* 703*/        Arrays.fill(CHARS, 12337, 12342, (byte)-87);
/* 704*/        Arrays.fill(CHARS, 12342, 12353, (byte)33);
/* 705*/        Arrays.fill(CHARS, 12353, 12437, (byte)-19);
/* 706*/        Arrays.fill(CHARS, 12437, 12441, (byte)33);
/* 707*/        Arrays.fill(CHARS, 12441, 12443, (byte)-87);
/* 708*/        Arrays.fill(CHARS, 12443, 12445, (byte)33);
/* 709*/        Arrays.fill(CHARS, 12445, 12447, (byte)-87);
/* 710*/        Arrays.fill(CHARS, 12447, 12449, (byte)33);
/* 711*/        Arrays.fill(CHARS, 12449, 12539, (byte)-19);
/* 712*/        CHARS[12539] = 33;
/* 713*/        Arrays.fill(CHARS, 12540, 12543, (byte)-87);
/* 714*/        Arrays.fill(CHARS, 12543, 12549, (byte)33);
/* 715*/        Arrays.fill(CHARS, 12549, 12589, (byte)-19);
/* 716*/        Arrays.fill(CHARS, 12589, 19968, (byte)33);
/* 717*/        Arrays.fill(CHARS, 19968, 40870, (byte)-19);
/* 718*/        Arrays.fill(CHARS, 40870, 44032, (byte)33);
/* 719*/        Arrays.fill(CHARS, 44032, 55204, (byte)-19);
/* 720*/        Arrays.fill(CHARS, 55204, 55296, (byte)33);
/* 721*/        Arrays.fill(CHARS, 57344, 65534, (byte)33);
            }
}
