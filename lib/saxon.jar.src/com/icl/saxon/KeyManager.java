// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   KeyManager.java

package com.icl.saxon;

import com.icl.saxon.expr.Expression;
import com.icl.saxon.expr.NodeSetExtent;
import com.icl.saxon.expr.NodeSetValue;
import com.icl.saxon.expr.Value;
import com.icl.saxon.expr.XPathException;
import com.icl.saxon.om.DocumentInfo;
import com.icl.saxon.om.EmptyEnumeration;
import com.icl.saxon.om.NamePool;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.pattern.AnyNodeTest;
import com.icl.saxon.pattern.Pattern;
import com.icl.saxon.sort.LocalOrderComparer;
import java.util.Hashtable;
import java.util.Vector;

// Referenced classes of package com.icl.saxon:
//            KeyDefinition, Controller, Context

public class KeyManager
{

            private Hashtable keyList;

            public KeyManager()
            {
/*  35*/        keyList = new Hashtable();
            }

            public void setKeyDefinition(KeyDefinition keydefinition)
            {
/*  45*/        Integer integer = new Integer(keydefinition.getFingerprint());
/*  46*/        Vector vector = (Vector)keyList.get(integer);
/*  47*/        if(vector == null)
                {
/*  48*/            vector = new Vector();
/*  49*/            keyList.put(integer, vector);
                }
/*  51*/        vector.addElement(keydefinition);
            }

            public Vector getKeyDefinitions(int i)
            {
/*  61*/        return (Vector)keyList.get(new Integer(i));
            }

            private synchronized Hashtable buildIndex(int i, DocumentInfo documentinfo, Controller controller)
                throws XPathException
            {
/*  76*/        Vector vector = getKeyDefinitions(i);
/*  77*/        if(vector == null)
/*  78*/            throw new XPathException("Key " + controller.getNamePool().getDisplayName(i) + " has not been defined");
/*  83*/        Hashtable hashtable = new Hashtable();
/*  85*/        for(int j = 0; j < vector.size(); j++)
/*  86*/            constructIndex(documentinfo, hashtable, (KeyDefinition)vector.elementAt(j), controller, j == 0);

/*  90*/        return hashtable;
            }

            private void constructIndex(DocumentInfo documentinfo, Hashtable hashtable, KeyDefinition keydefinition, Controller controller, boolean flag)
                throws XPathException
            {
/* 104*/        Pattern pattern = keydefinition.getMatch();
/* 105*/        Expression expression = keydefinition.getUse();
/* 106*/        DocumentInfo documentinfo1 = documentinfo;
/* 107*/        DocumentInfo documentinfo2 = documentinfo1;
/* 108*/        Context context = controller.makeContext(documentinfo);
/* 110*/        short word0 = pattern.getNodeType();
/* 112*/        com.icl.saxon.om.AxisEnumeration axisenumeration = documentinfo.getEnumeration((byte)4, AnyNodeTest.getInstance());
/* 117*/        if(word0 == 2 || word0 == 0)
                {
/* 119*/            while(axisenumeration.hasMoreElements()) 
                    {
/* 119*/                NodeInfo nodeinfo = axisenumeration.nextElement();
/* 120*/                if(nodeinfo.getNodeType() == 1)
                        {
/* 121*/                    for(com.icl.saxon.om.AxisEnumeration axisenumeration1 = nodeinfo.getEnumeration((byte)2, AnyNodeTest.getInstance()); axisenumeration1.hasMoreElements(); processKeyNode(axisenumeration1.nextElement(), pattern, expression, hashtable, context, flag));
/* 126*/                    if(word0 == 0)
/* 128*/                        processKeyNode(nodeinfo, pattern, expression, hashtable, context, flag);
                        } else
                        {
/* 131*/                    processKeyNode(nodeinfo, pattern, expression, hashtable, context, flag);
                        }
                    }
                } else
                {
                    NodeInfo nodeinfo1;
/* 137*/            for(; axisenumeration.hasMoreElements(); processKeyNode(nodeinfo1, pattern, expression, hashtable, context, flag))
/* 137*/                nodeinfo1 = axisenumeration.nextElement();

                }
            }

            private void processKeyNode(NodeInfo nodeinfo, Pattern pattern, Expression expression, Hashtable hashtable, Context context, boolean flag)
                throws XPathException
            {
/* 149*/        if(pattern.matches(nodeinfo, context))
                {
/* 150*/            context.setContextNode(nodeinfo);
/* 151*/            context.setCurrentNode(nodeinfo);
/* 152*/            context.setPosition(1);
/* 153*/            context.setLast(1);
/* 154*/            Value value = expression.evaluate(context);
/* 155*/            if(value instanceof NodeSetValue)
                    {
/* 157*/                for(NodeEnumeration nodeenumeration = ((NodeSetValue)value).enumerate(); nodeenumeration.hasMoreElements();)
                        {
/* 159*/                    NodeInfo nodeinfo1 = nodeenumeration.nextElement();
/* 160*/                    String s1 = nodeinfo1.getStringValue();
/* 161*/                    NodeSetExtent nodesetextent1 = (NodeSetExtent)hashtable.get(s1);
/* 162*/                    if(nodesetextent1 == null)
                            {
/* 163*/                        nodesetextent1 = new NodeSetExtent(LocalOrderComparer.getInstance());
/* 164*/                        nodesetextent1.setSorted(true);
/* 165*/                        hashtable.put(s1, nodesetextent1);
                            }
/* 167*/                    nodesetextent1.append(nodeinfo);
/* 168*/                    if(!flag)
                            {
/* 172*/                        nodesetextent1.setSorted(false);
/* 173*/                        nodesetextent1.sort();
                            }
                        }

                    } else
                    {
/* 177*/                String s = value.asString();
/* 178*/                context.setContextNode(nodeinfo);
/* 179*/                NodeSetExtent nodesetextent = (NodeSetExtent)hashtable.get(s);
/* 180*/                if(nodesetextent == null)
                        {
/* 181*/                    nodesetextent = new NodeSetExtent(LocalOrderComparer.getInstance());
/* 182*/                    nodesetextent.setSorted(true);
/* 183*/                    hashtable.put(s, nodesetextent);
                        }
/* 185*/                nodesetextent.append(nodeinfo);
/* 186*/                if(!flag)
                        {
/* 190*/                    nodesetextent.setSorted(false);
/* 191*/                    nodesetextent.sort();
                        }
                    }
                }
            }

            public NodeEnumeration selectByKey(int i, DocumentInfo documentinfo, String s, Controller controller)
                throws XPathException
            {
/* 211*/        Hashtable hashtable = documentinfo.getKeyIndex(this, i);
/* 212*/        if(hashtable == null)
                {
/* 213*/            hashtable = buildIndex(i, documentinfo, controller);
/* 214*/            documentinfo.setKeyIndex(this, i, hashtable);
                }
/* 216*/        NodeSetExtent nodesetextent = (NodeSetExtent)hashtable.get(s);
/* 217*/        return ((NodeEnumeration) (nodesetextent != null ? nodesetextent.enumerate() : EmptyEnumeration.getInstance()));
            }
}
