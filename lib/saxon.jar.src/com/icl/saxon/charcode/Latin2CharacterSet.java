// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Latin2CharacterSet.java

package com.icl.saxon.charcode;


// Referenced classes of package com.icl.saxon.charcode:
//            CharacterSet

public class Latin2CharacterSet
    implements CharacterSet
{

            private static boolean c[] = null;

            private static void init()
            {
/*  13*/        c = new boolean[750];
/*  15*/        for(int i = 0; i < 127; i++)
/*  16*/            c[i] = true;

/*  18*/        for(int j = 128; j < 750; j++)
/*  19*/            c[j] = false;

/*  22*/        c[160] = true;
/*  23*/        c[164] = true;
/*  24*/        c[167] = true;
/*  25*/        c[168] = true;
/*  26*/        c[173] = true;
/*  27*/        c[176] = true;
/*  28*/        c[180] = true;
/*  29*/        c[184] = true;
/*  30*/        c[193] = true;
/*  31*/        c[194] = true;
/*  32*/        c[196] = true;
/*  33*/        c[199] = true;
/*  34*/        c[201] = true;
/*  35*/        c[203] = true;
/*  36*/        c[205] = true;
/*  37*/        c[206] = true;
/*  38*/        c[211] = true;
/*  39*/        c[212] = true;
/*  40*/        c[214] = true;
/*  41*/        c[215] = true;
/*  42*/        c[218] = true;
/*  43*/        c[220] = true;
/*  44*/        c[221] = true;
/*  45*/        c[223] = true;
/*  46*/        c[225] = true;
/*  47*/        c[226] = true;
/*  48*/        c[228] = true;
/*  49*/        c[231] = true;
/*  50*/        c[233] = true;
/*  51*/        c[235] = true;
/*  52*/        c[237] = true;
/*  53*/        c[238] = true;
/*  54*/        c[243] = true;
/*  55*/        c[244] = true;
/*  56*/        c[246] = true;
/*  57*/        c[247] = true;
/*  58*/        c[250] = true;
/*  59*/        c[252] = true;
/*  60*/        c[253] = true;
/*  61*/        c[258] = true;
/*  62*/        c[259] = true;
/*  63*/        c[260] = true;
/*  64*/        c[261] = true;
/*  65*/        c[262] = true;
/*  66*/        c[263] = true;
/*  67*/        c[268] = true;
/*  68*/        c[269] = true;
/*  69*/        c[270] = true;
/*  70*/        c[271] = true;
/*  71*/        c[272] = true;
/*  72*/        c[273] = true;
/*  73*/        c[280] = true;
/*  74*/        c[281] = true;
/*  75*/        c[282] = true;
/*  76*/        c[283] = true;
/*  77*/        c[313] = true;
/*  78*/        c[314] = true;
/*  79*/        c[317] = true;
/*  80*/        c[318] = true;
/*  81*/        c[321] = true;
/*  82*/        c[322] = true;
/*  83*/        c[323] = true;
/*  84*/        c[324] = true;
/*  85*/        c[327] = true;
/*  86*/        c[328] = true;
/*  87*/        c[336] = true;
/*  88*/        c[337] = true;
/*  89*/        c[340] = true;
/*  90*/        c[341] = true;
/*  91*/        c[344] = true;
/*  92*/        c[345] = true;
/*  93*/        c[346] = true;
/*  94*/        c[347] = true;
/*  95*/        c[350] = true;
/*  96*/        c[351] = true;
/*  97*/        c[352] = true;
/*  98*/        c[353] = true;
/*  99*/        c[354] = true;
/* 100*/        c[355] = true;
/* 101*/        c[356] = true;
/* 102*/        c[357] = true;
/* 103*/        c[366] = true;
/* 104*/        c[367] = true;
/* 105*/        c[368] = true;
/* 106*/        c[369] = true;
/* 107*/        c[377] = true;
/* 108*/        c[378] = true;
/* 109*/        c[379] = true;
/* 110*/        c[380] = true;
/* 111*/        c[381] = true;
/* 112*/        c[382] = true;
/* 113*/        c[711] = true;
/* 114*/        c[728] = true;
/* 115*/        c[729] = true;
/* 116*/        c[731] = true;
/* 117*/        c[733] = true;
            }

            public Latin2CharacterSet()
            {
/* 122*/        if(c == null)
/* 122*/            init();
            }

            public final boolean inCharset(int i)
            {
/* 126*/        return i < 750 && c[i];
            }

}
