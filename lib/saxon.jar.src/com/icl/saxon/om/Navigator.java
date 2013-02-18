// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Navigator.java

package com.icl.saxon.om;

import com.icl.saxon.Context;
import com.icl.saxon.expr.XPathException;
import com.icl.saxon.pattern.*;
import java.util.Vector;

// Referenced classes of package com.icl.saxon.om:
//            NodeInfo, NodeEnumeration

public class Navigator
{

            public Navigator()
            {
            }

            public static boolean isWhite(String s)
            {
/*  25*/        for(int i = 0; i < s.length(); i++)
                {
/*  26*/            char c = s.charAt(i);
/*  28*/            if(c > ' ')
/*  29*/                return false;
                }

/*  32*/        return true;
            }

            public static boolean isAncestor(NodeInfo nodeinfo, NodeInfo nodeinfo1)
            {
/*  43*/        NodeInfo nodeinfo2 = nodeinfo1.getParent();
/*  44*/        if(nodeinfo2 == null)
/*  44*/            return false;
/*  45*/        if(nodeinfo2.isSameNodeInfo(nodeinfo))
/*  45*/            return true;
/*  46*/        else
/*  46*/            return isAncestor(nodeinfo, nodeinfo2);
            }

            public static String getPath(NodeInfo nodeinfo)
            {
/*  55*/        switch(nodeinfo.getNodeType())
                {
/*  57*/        case 9: // '\t'
/*  57*/            return "/";

/*  59*/        case 1: // '\001'
/*  59*/            String s = getPath(nodeinfo.getParent());
/*  60*/            return (s.equals("/") ? "" : s) + "/" + nodeinfo.getDisplayName() + "[" + getNumberSimple(nodeinfo) + "]";

/*  63*/        case 2: // '\002'
/*  63*/            return getPath(nodeinfo.getParent()) + "/@" + nodeinfo.getDisplayName();

/*  65*/        case 3: // '\003'
/*  65*/            String s1 = getPath(nodeinfo.getParent());
/*  66*/            return (s1.equals("/") ? "" : s1) + "/text()[" + getNumberSimple(nodeinfo) + "]";

/*  69*/        case 8: // '\b'
/*  69*/            String s2 = getPath(nodeinfo.getParent());
/*  70*/            return (s2.equals("/") ? "" : s2) + "/comment()[" + getNumberSimple(nodeinfo) + "]";

/*  73*/        case 7: // '\007'
/*  73*/            String s3 = getPath(nodeinfo.getParent());
/*  74*/            return (s3.equals("/") ? "" : s3) + "/processing-instruction()[" + getNumberSimple(nodeinfo) + "]";

/*  77*/        case 13: // '\r'
/*  77*/            return getPath(nodeinfo.getParent()) + "/namespace::" + nodeinfo.getLocalName();

/*  79*/        case 4: // '\004'
/*  79*/        case 5: // '\005'
/*  79*/        case 6: // '\006'
/*  79*/        case 10: // '\n'
/*  79*/        case 11: // '\013'
/*  79*/        case 12: // '\f'
/*  79*/        default:
/*  79*/            return "";
                }
            }

            public static int getNumberSimple(NodeInfo nodeinfo, Context context)
                throws XPathException
            {
/*  93*/        int i = nodeinfo.getFingerprint();
                Object obj;
/*  96*/        if(i == -1)
/*  97*/            obj = new NodeTypeTest(nodeinfo.getNodeType());
/*  99*/        else
/*  99*/            obj = new NameTest(nodeinfo);
/* 102*/        AxisEnumeration axisenumeration = nodeinfo.getEnumeration((byte)11, ((com.icl.saxon.pattern.NodeTest) (obj)));
                int j;
/* 104*/        for(j = 1; axisenumeration.hasMoreElements(); j++)
                {
/* 106*/            NodeInfo nodeinfo1 = axisenumeration.nextElement();
/* 108*/            int k = context.getRememberedNumber(nodeinfo1);
/* 109*/            if(k > 0)
                    {
/* 110*/                k += j;
/* 111*/                context.setRememberedNumber(nodeinfo, k);
/* 112*/                return k;
                    }
                }

/* 118*/        context.setRememberedNumber(nodeinfo, j);
/* 119*/        return j;
            }

            public static int getNumberSimple(NodeInfo nodeinfo)
            {
/* 131*/        try
                {
/* 131*/            int i = nodeinfo.getFingerprint();
                    Object obj;
/* 134*/            if(i == -1)
/* 135*/                obj = new NodeTypeTest(nodeinfo.getNodeType());
/* 137*/            else
/* 137*/                obj = new NameTest(nodeinfo);
/* 140*/            AxisEnumeration axisenumeration = nodeinfo.getEnumeration((byte)11, ((com.icl.saxon.pattern.NodeTest) (obj)));
                    int j;
/* 142*/            for(j = 1; axisenumeration.hasMoreElements(); j++)
                    {
/* 144*/                NodeInfo nodeinfo1 = axisenumeration.nextElement();
                    }

/* 148*/            return j;
                }
/* 151*/        catch(XPathException xpathexception)
                {
/* 151*/            return 1;
                }
            }

            public static int getNumberSingle(NodeInfo nodeinfo, Pattern pattern, Pattern pattern1, Context context)
                throws XPathException
            {
/* 174*/        if(pattern == null && pattern1 == null)
/* 175*/            return getNumberSimple(nodeinfo, context);
/* 178*/        boolean flag = false;
/* 179*/        if(pattern == null)
                {
/* 180*/            if(nodeinfo.getFingerprint() == -1)
/* 181*/                pattern = new NodeTypeTest(nodeinfo.getNodeType());
/* 183*/            else
/* 183*/                pattern = new NameTest(nodeinfo);
/* 185*/            flag = true;
                }
                NodeInfo nodeinfo1;
/* 188*/        for(nodeinfo1 = nodeinfo; !flag && !pattern.matches(nodeinfo1, context);)
                {
/* 190*/            nodeinfo1 = nodeinfo1.getParent();
/* 191*/            if(nodeinfo1 == null)
/* 192*/                return 0;
/* 194*/            if(pattern1 != null && pattern1.matches(nodeinfo1, context))
/* 195*/                return 0;
                }

/* 201*/        AxisEnumeration axisenumeration = nodeinfo1.getEnumeration((byte)11, AnyNodeTest.getInstance());
/* 203*/        int i = 1;
/* 205*/        while(axisenumeration.hasMoreElements()) 
                {
/* 205*/            NodeInfo nodeinfo2 = axisenumeration.nextElement();
/* 206*/            if(pattern.matches(nodeinfo2, context))
/* 207*/                i++;
                }
/* 210*/        return i;
            }

            public static int getNumberAny(NodeInfo nodeinfo, Pattern pattern, Pattern pattern1, Context context)
                throws XPathException
            {
/* 230*/        int i = 0;
/* 231*/        if(pattern == null)
                {
/* 232*/            if(nodeinfo.getFingerprint() == -1)
/* 233*/                pattern = new NodeTypeTest(nodeinfo.getNodeType());
/* 235*/            else
/* 235*/                pattern = new NameTest(nodeinfo);
/* 237*/            i = 1;
                } else
/* 238*/        if(pattern.matches(nodeinfo, context))
/* 239*/            i = 1;
/* 245*/        for(AxisEnumeration axisenumeration = nodeinfo.getEnumeration((byte)13, AnyNodeTest.getInstance()); axisenumeration.hasMoreElements();)
                {
/* 249*/            NodeInfo nodeinfo1 = axisenumeration.nextElement();
/* 250*/            if(pattern1 != null && pattern1.matches(nodeinfo1, context))
/* 251*/                return i;
/* 253*/            if(pattern.matches(nodeinfo1, context))
/* 254*/                i++;
                }

/* 257*/        return i;
            }

            public static Vector getNumberMulti(NodeInfo nodeinfo, Pattern pattern, Pattern pattern1, Context context)
                throws XPathException
            {
/* 278*/        Vector vector = new Vector();
/* 280*/        if(pattern == null)
/* 281*/            if(nodeinfo.getFingerprint() == -1)
/* 282*/                pattern = new NodeTypeTest(nodeinfo.getNodeType());
/* 284*/            else
/* 284*/                pattern = new NameTest(nodeinfo);
/* 288*/        NodeInfo nodeinfo1 = nodeinfo;
/* 291*/        do
                {
/* 291*/            if(pattern.matches(nodeinfo1, context))
                    {
/* 292*/                int i = getNumberSingle(nodeinfo1, pattern, null, context);
/* 293*/                vector.insertElementAt(new Integer(i), 0);
                    }
/* 295*/            nodeinfo1 = nodeinfo1.getParent();
                } while(nodeinfo1 != null && (pattern1 == null || !pattern1.matches(nodeinfo1, context)));
/* 300*/        return vector;
            }
}
