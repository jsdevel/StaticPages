// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Numberer_de.java

package com.icl.saxon.number;


// Referenced classes of package com.icl.saxon.number:
//            Numberer_en

public class Numberer_de extends Numberer_en
{

            public Numberer_de()
            {
            }

            public String format(int i, String s, int j, String s1, String s2)
            {
/*  27*/        if(s2.equals("traditional") && s.equals("eins"))
                {
/*  28*/            switch(i)
                    {
/*  29*/            case 1: // '\001'
/*  29*/                return "eins";

/*  30*/            case 2: // '\002'
/*  30*/                return "zwei";

/*  31*/            case 3: // '\003'
/*  31*/                return "drei";

/*  32*/            case 4: // '\004'
/*  32*/                return "vier";

/*  33*/            case 5: // '\005'
/*  33*/                return "funf";

/*  34*/            case 6: // '\006'
/*  34*/                return "sechs";

/*  35*/            case 7: // '\007'
/*  35*/                return "sieben";

/*  36*/            case 8: // '\b'
/*  36*/                return "acht";

/*  37*/            case 9: // '\t'
/*  37*/                return "neun";

/*  38*/            case 10: // '\n'
/*  38*/                return "zehn";
                    }
/*  39*/            return "" + i;
                } else
                {
/*  42*/            return super.format(i, s, j, s1, s2);
                }
            }
}
