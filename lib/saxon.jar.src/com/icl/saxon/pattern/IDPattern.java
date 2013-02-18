// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   IDPattern.java

package com.icl.saxon.pattern;

import com.icl.saxon.Context;
import com.icl.saxon.expr.XPathException;
import com.icl.saxon.om.DocumentInfo;
import com.icl.saxon.om.NodeInfo;
import java.util.StringTokenizer;

// Referenced classes of package com.icl.saxon.pattern:
//            Pattern

public final class IDPattern extends Pattern
{

            private String id;
            private boolean containsSpaces;

            public IDPattern(String s)
            {
/*  19*/        id = s;
/*  20*/        containsSpaces = id.indexOf(' ') >= 0 || id.indexOf('\t') >= 0 || id.indexOf('\n') >= 0 || id.indexOf('\f') >= 0;
            }

            public boolean matches(NodeInfo nodeinfo, Context context)
                throws XPathException
            {
/*  34*/        if(nodeinfo.getNodeType() != 1)
/*  34*/            return false;
/*  35*/        DocumentInfo documentinfo = nodeinfo.getDocumentRoot();
/*  36*/        if(!containsSpaces)
                {
/*  37*/            NodeInfo nodeinfo1 = documentinfo.selectID(id);
/*  38*/            if(nodeinfo1 == null)
/*  38*/                return false;
/*  39*/            else
/*  39*/                return nodeinfo1.isSameNodeInfo(nodeinfo);
                }
/*  41*/        for(StringTokenizer stringtokenizer = new StringTokenizer(id); stringtokenizer.hasMoreElements();)
                {
/*  43*/            String s = (String)stringtokenizer.nextElement();
/*  44*/            NodeInfo nodeinfo2 = documentinfo.selectID(s);
/*  45*/            if(nodeinfo2 != null && nodeinfo.isSameNodeInfo(nodeinfo2))
/*  46*/                return true;
                }

/*  49*/        return false;
            }

            public short getNodeType()
            {
/*  59*/        return 1;
            }
}
