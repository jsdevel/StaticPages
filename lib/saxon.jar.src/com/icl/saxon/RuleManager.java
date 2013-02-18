// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   RuleManager.java

package com.icl.saxon;

import com.icl.saxon.expr.StandaloneContext;
import com.icl.saxon.expr.XPathException;
import com.icl.saxon.om.NamePool;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.pattern.Pattern;
import com.icl.saxon.pattern.UnionPattern;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon:
//            Mode, NodeHandler, Context

public class RuleManager
{

            private Mode defaultMode;
            private Hashtable modes;
            private NamePool namePool;
            private StandaloneContext standaloneContext;

            public RuleManager(NamePool namepool)
            {
/*  32*/        namePool = namepool;
/*  33*/        resetHandlers();
            }

            public void setStandaloneContext(StandaloneContext standalonecontext)
            {
/*  43*/        standaloneContext = standalonecontext;
            }

            public StandaloneContext getStandaloneContext()
            {
/*  55*/        if(standaloneContext == null)
/*  56*/            standaloneContext = new StandaloneContext(namePool);
/*  58*/        return standaloneContext;
            }

            public void resetHandlers()
            {
/*  66*/        defaultMode = new Mode();
/*  67*/        modes = new Hashtable();
            }

            public Mode getMode(int i)
            {
/*  78*/        if(i == -1)
/*  79*/            return defaultMode;
/*  81*/        Integer integer = new Integer(i & 0xfffff);
/*  82*/        Mode mode = (Mode)modes.get(integer);
/*  83*/        if(mode == null)
                {
/*  84*/            mode = new Mode();
/*  85*/            mode.setNameCode(i);
/*  86*/            modes.put(integer, mode);
                }
/*  88*/        return mode;
            }

            public void setHandler(String s, NodeHandler nodehandler)
                throws XPathException
            {
/* 101*/        Pattern pattern = Pattern.make(s, getStandaloneContext());
/* 102*/        setHandler(pattern, nodehandler, defaultMode, 0);
            }

            public void setHandler(Pattern pattern, NodeHandler nodehandler, Mode mode, int i)
            {
/* 117*/        if(pattern instanceof UnionPattern)
                {
/* 118*/            UnionPattern unionpattern = (UnionPattern)pattern;
/* 119*/            Pattern pattern1 = unionpattern.getLHS();
/* 120*/            Pattern pattern2 = unionpattern.getRHS();
/* 121*/            setHandler(pattern1, nodehandler, mode, i);
/* 122*/            setHandler(pattern2, nodehandler, mode, i);
/* 123*/            return;
                } else
                {
/* 126*/            double d = pattern.getDefaultPriority();
/* 127*/            setHandler(pattern, nodehandler, mode, i, d);
/* 128*/            return;
                }
            }

            public void setHandler(Pattern pattern, NodeHandler nodehandler, Mode mode, int i, double d)
            {
/* 147*/        if(pattern instanceof UnionPattern)
                {
/* 148*/            UnionPattern unionpattern = (UnionPattern)pattern;
/* 149*/            Pattern pattern1 = unionpattern.getLHS();
/* 150*/            Pattern pattern2 = unionpattern.getRHS();
/* 151*/            setHandler(pattern1, nodehandler, mode, i, d);
/* 152*/            setHandler(pattern2, nodehandler, mode, i, d);
/* 153*/            return;
                } else
                {
/* 156*/            mode.addRule(pattern, nodehandler, i, d);
/* 157*/            return;
                }
            }

            public NodeHandler getHandler(NodeInfo nodeinfo, Context context)
                throws TransformerException
            {
/* 168*/        return getHandler(nodeinfo, defaultMode, context);
            }

            public NodeHandler getHandler(NodeInfo nodeinfo, Mode mode, Context context)
                throws TransformerException
            {
/* 182*/        if(mode == null)
/* 183*/            mode = defaultMode;
/* 186*/        NodeHandler nodehandler = (NodeHandler)mode.getRule(nodeinfo, context);
/* 188*/        if(nodehandler != null)
/* 188*/            return nodehandler;
/* 190*/        else
/* 190*/            return null;
            }

            public NodeHandler getHandler(NodeInfo nodeinfo, Mode mode, int i, int j, Context context)
                throws XPathException
            {
/* 200*/        if(mode == null)
/* 200*/            mode = defaultMode;
/* 201*/        return (NodeHandler)mode.getRule(nodeinfo, i, j, context);
            }

            public Enumeration getAllModes()
            {
/* 210*/        return modes.keys();
            }
}
