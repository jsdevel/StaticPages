// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SimpleTraceListener.java

package com.icl.saxon.trace;

import com.icl.saxon.*;
import com.icl.saxon.om.*;
import com.icl.saxon.style.StyleElement;
import com.icl.saxon.tree.ElementImpl;
import com.icl.saxon.tree.NodeImpl;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.trace:
//            TraceListener

public class SimpleTraceListener
    implements TraceListener
{

            String indent;

            public SimpleTraceListener()
            {
/*  16*/        indent = "";
            }

            public void open()
            {
/*  23*/        System.err.println("<trace>");
            }

            public void close()
            {
/*  31*/        System.err.println("</trace>");
            }

            public void toplevel(NodeInfo nodeinfo)
            {
/*  41*/        StyleElement styleelement = (StyleElement)nodeinfo;
/*  42*/        System.err.println("<Top-level element=\"" + styleelement.getDisplayName() + "\" line=\"" + styleelement.getLineNumber() + "\" file=\"" + styleelement.getSystemId() + "\" precedence=\"" + styleelement.getPrecedence() + "\"/>");
            }

            public void enterSource(NodeHandler nodehandler, Context context)
            {
/*  51*/        NodeInfo nodeinfo = context.getContextNodeInfo();
/*  52*/        System.err.println(indent + "<Source node=\"" + Navigator.getPath(nodeinfo) + "\" line=\"" + nodeinfo.getLineNumber() + "\" mode=\"" + getModeName(context) + "\">");
/*  55*/        indent += " ";
            }

            public void leaveSource(NodeHandler nodehandler, Context context)
            {
/*  63*/        indent = indent.substring(0, indent.length() - 1);
/*  64*/        System.err.println(indent + "</Source><!-- " + Navigator.getPath(context.getContextNodeInfo()) + " -->");
            }

            public void enter(NodeInfo nodeinfo, Context context)
            {
/*  73*/        if(nodeinfo.getNodeType() == 1)
                {
/*  74*/            System.err.println(indent + "<Instruction element=\"" + nodeinfo.getDisplayName() + "\" line=\"" + nodeinfo.getLineNumber() + "\">");
/*  75*/            indent += " ";
                }
            }

            public void leave(NodeInfo nodeinfo, Context context)
            {
/*  84*/        if(nodeinfo.getNodeType() == 1)
                {
/*  85*/            indent = indent.substring(0, indent.length() - 1);
/*  86*/            System.err.println(indent + "</Instruction> <!-- " + nodeinfo.getDisplayName() + " -->");
                }
            }

            String getModeName(Context context)
            {
/*  93*/        Mode mode = context.getMode();
/*  94*/        if(mode == null)
/*  94*/            return "#none";
/*  95*/        int i = mode.getNameCode();
/*  96*/        if(i == -1)
/*  97*/            return "#default";
/*  99*/        else
/*  99*/            return context.getController().getNamePool().getDisplayName(i);
            }
}
