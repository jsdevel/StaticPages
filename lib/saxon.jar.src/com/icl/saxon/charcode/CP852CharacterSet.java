// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   CP852CharacterSet.java

package com.icl.saxon.charcode;


// Referenced classes of package com.icl.saxon.charcode:
//            PluggableCharacterSet

public class CP852CharacterSet
    implements PluggableCharacterSet
{

            private static boolean c[] = null;

            private static void init()
            {
/*  12*/        c = new boolean[400];
/*  13*/        for(int i = 0; i < 127; i++)
/*  14*/            c[i] = true;

/*  16*/        for(int j = 127; j < 400; j++)
/*  17*/            c[j] = false;

/*  20*/        c[167] = true;
/*  21*/        c[171] = true;
/*  22*/        c[172] = true;
/*  23*/        c[187] = true;
/*  24*/        c[193] = true;
/*  25*/        c[194] = true;
/*  26*/        c[196] = true;
/*  27*/        c[199] = true;
/*  28*/        c[201] = true;
/*  29*/        c[203] = true;
/*  30*/        c[205] = true;
/*  31*/        c[206] = true;
/*  32*/        c[211] = true;
/*  33*/        c[212] = true;
/*  34*/        c[214] = true;
/*  35*/        c[218] = true;
/*  36*/        c[220] = true;
/*  37*/        c[221] = true;
/*  38*/        c[223] = true;
/*  39*/        c[225] = true;
/*  40*/        c[226] = true;
/*  41*/        c[228] = true;
/*  42*/        c[231] = true;
/*  43*/        c[233] = true;
/*  44*/        c[235] = true;
/*  45*/        c[237] = true;
/*  46*/        c[238] = true;
/*  47*/        c[243] = true;
/*  48*/        c[244] = true;
/*  49*/        c[246] = true;
/*  50*/        c[250] = true;
/*  51*/        c[252] = true;
/*  52*/        c[253] = true;
/*  53*/        c[258] = true;
/*  54*/        c[259] = true;
/*  55*/        c[260] = true;
/*  56*/        c[261] = true;
/*  57*/        c[262] = true;
/*  58*/        c[263] = true;
/*  59*/        c[268] = true;
/*  60*/        c[269] = true;
/*  61*/        c[270] = true;
/*  62*/        c[271] = true;
/*  63*/        c[272] = true;
/*  64*/        c[273] = true;
/*  65*/        c[280] = true;
/*  66*/        c[281] = true;
/*  67*/        c[282] = true;
/*  68*/        c[283] = true;
/*  69*/        c[313] = true;
/*  70*/        c[314] = true;
/*  71*/        c[317] = true;
/*  72*/        c[318] = true;
/*  73*/        c[321] = true;
/*  74*/        c[322] = true;
/*  75*/        c[323] = true;
/*  76*/        c[324] = true;
/*  77*/        c[327] = true;
/*  78*/        c[328] = true;
/*  79*/        c[336] = true;
/*  80*/        c[337] = true;
/*  81*/        c[340] = true;
/*  82*/        c[341] = true;
/*  83*/        c[344] = true;
/*  84*/        c[345] = true;
/*  85*/        c[346] = true;
/*  86*/        c[347] = true;
/*  87*/        c[350] = true;
/*  88*/        c[351] = true;
/*  89*/        c[352] = true;
/*  90*/        c[353] = true;
/*  91*/        c[355] = true;
/*  92*/        c[356] = true;
/*  93*/        c[357] = true;
/*  94*/        c[366] = true;
/*  95*/        c[367] = true;
/*  96*/        c[368] = true;
/*  97*/        c[369] = true;
/*  98*/        c[377] = true;
/*  99*/        c[378] = true;
/* 100*/        c[379] = true;
/* 101*/        c[380] = true;
/* 102*/        c[381] = true;
/* 103*/        c[382] = true;
            }

            public CP852CharacterSet()
            {
/* 107*/        if(c == null)
/* 107*/            init();
            }

            public final boolean inCharset(int i)
            {
/* 111*/        return i < 400 && c[i];
            }

            public final String getEncodingName()
            {
/* 115*/        return "cp852";
            }

}
