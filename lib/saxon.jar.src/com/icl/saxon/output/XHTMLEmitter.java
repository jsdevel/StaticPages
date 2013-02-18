// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XHTMLEmitter.java

package com.icl.saxon.output;

import com.icl.saxon.om.NamePool;

// Referenced classes of package com.icl.saxon.output:
//            XMLEmitter, Emitter, HTMLEmitter

public class XHTMLEmitter extends XMLEmitter
{

            public XHTMLEmitter()
            {
            }

            protected String emptyElementTagCloser(int i)
            {
/*  22*/        String s = super.namePool.getDisplayName(i);
/*  23*/        if(HTMLEmitter.isEmptyTag(s))
/*  24*/            return " />";
/*  26*/        else
/*  26*/            return "></" + s + ">";
            }
}
