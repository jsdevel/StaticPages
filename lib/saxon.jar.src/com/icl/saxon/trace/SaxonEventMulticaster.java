// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SaxonEventMulticaster.java

package com.icl.saxon.trace;

import com.icl.saxon.Context;
import com.icl.saxon.NodeHandler;
import com.icl.saxon.om.NodeInfo;
import java.util.EventListener;

// Referenced classes of package com.icl.saxon.trace:
//            TraceListener

public class SaxonEventMulticaster
    implements TraceListener
{

            protected final EventListener a;
            protected final EventListener b;

            protected SaxonEventMulticaster(EventListener eventlistener, EventListener eventlistener1)
            {
/*  25*/        a = eventlistener;
/*  25*/        b = eventlistener1;
            }

            protected EventListener remove(EventListener eventlistener)
            {
/*  34*/        if(eventlistener == a)
/*  34*/            return b;
/*  35*/        if(eventlistener == b)
/*  35*/            return a;
/*  36*/        EventListener eventlistener1 = removeInternal(a, eventlistener);
/*  37*/        EventListener eventlistener2 = removeInternal(b, eventlistener);
/*  38*/        if(eventlistener1 == a && eventlistener2 == b)
/*  39*/            return this;
/*  41*/        else
/*  41*/            return addInternal(eventlistener1, eventlistener2);
            }

            public void open()
            {
/*  49*/        ((TraceListener)a).open();
/*  50*/        ((TraceListener)b).open();
            }

            public void close()
            {
/*  58*/        ((TraceListener)a).close();
/*  59*/        ((TraceListener)b).close();
            }

            public void toplevel(NodeInfo nodeinfo)
            {
/*  68*/        ((TraceListener)a).toplevel(nodeinfo);
/*  69*/        ((TraceListener)b).toplevel(nodeinfo);
            }

            public void enterSource(NodeHandler nodehandler, Context context)
            {
/*  77*/        ((TraceListener)a).enterSource(nodehandler, context);
/*  78*/        ((TraceListener)b).enterSource(nodehandler, context);
            }

            public void leaveSource(NodeHandler nodehandler, Context context)
            {
/*  86*/        ((TraceListener)a).leaveSource(nodehandler, context);
/*  87*/        ((TraceListener)b).leaveSource(nodehandler, context);
            }

            public void enter(NodeInfo nodeinfo, Context context)
            {
/*  95*/        ((TraceListener)a).enter(nodeinfo, context);
/*  96*/        ((TraceListener)b).enter(nodeinfo, context);
            }

            public void leave(NodeInfo nodeinfo, Context context)
            {
/* 104*/        ((TraceListener)a).leave(nodeinfo, context);
/* 105*/        ((TraceListener)b).leave(nodeinfo, context);
            }

            public static TraceListener add(TraceListener tracelistener, TraceListener tracelistener1)
            {
/* 115*/        return (TraceListener)addInternal(tracelistener, tracelistener1);
            }

            public static TraceListener remove(TraceListener tracelistener, TraceListener tracelistener1)
            {
/* 125*/        return (TraceListener)removeInternal(tracelistener, tracelistener1);
            }

            protected static EventListener addInternal(EventListener eventlistener, EventListener eventlistener1)
            {
/* 139*/        if(eventlistener == null)
/* 139*/            return eventlistener1;
/* 140*/        if(eventlistener1 == null)
/* 140*/            return eventlistener;
/* 141*/        else
/* 141*/            return new SaxonEventMulticaster(eventlistener, eventlistener1);
            }

            protected static EventListener removeInternal(EventListener eventlistener, EventListener eventlistener1)
            {
/* 156*/        if(eventlistener == eventlistener1 || eventlistener == null)
/* 157*/            return null;
/* 158*/        if(eventlistener instanceof SaxonEventMulticaster)
/* 159*/            return ((SaxonEventMulticaster)eventlistener).remove(eventlistener1);
/* 161*/        else
/* 161*/            return eventlistener;
            }
}
