// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   KeyPattern.java

package com.icl.saxon.pattern;

import com.icl.saxon.*;
import com.icl.saxon.expr.XPathException;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;

// Referenced classes of package com.icl.saxon.pattern:
//            Pattern

public final class KeyPattern extends Pattern
{

            private int keyfingerprint;
            private String keyvalue;

            public KeyPattern(int i, String s)
            {
/*  25*/        keyfingerprint = i & 0xfffff;
/*  26*/        keyvalue = s;
            }

            public boolean matches(NodeInfo nodeinfo, Context context)
                throws XPathException
            {
/*  36*/        com.icl.saxon.om.DocumentInfo documentinfo = nodeinfo.getDocumentRoot();
/*  37*/        Controller controller = context.getController();
/*  38*/        KeyManager keymanager = controller.getKeyManager();
/*  39*/        for(NodeEnumeration nodeenumeration = keymanager.selectByKey(keyfingerprint, documentinfo, keyvalue, controller); nodeenumeration.hasMoreElements();)
/*  41*/            if(nodeenumeration.nextElement().isSameNodeInfo(nodeinfo))
/*  42*/                return true;

/*  45*/        return false;
            }
}
