// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NodeInfo.java

package com.icl.saxon.om;

import com.icl.saxon.output.Outputter;
import com.icl.saxon.pattern.NodeTest;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.om:
//            AxisEnumeration, DocumentInfo

public interface NodeInfo
    extends Source
{

    public static final short NODE = 0;
    public static final short ELEMENT = 1;
    public static final short ATTRIBUTE = 2;
    public static final short TEXT = 3;
    public static final short PI = 7;
    public static final short COMMENT = 8;
    public static final short ROOT = 9;
    public static final short NAMESPACE = 13;
    public static final short NUMBER_OF_TYPES = 13;
    public static final short NONE = 9999;

    public abstract short getNodeType();

    public abstract boolean isSameNodeInfo(NodeInfo nodeinfo);

    public abstract String getSystemId();

    public abstract String getBaseURI();

    public abstract int getLineNumber();

    public abstract int compareOrder(NodeInfo nodeinfo);

    public abstract String getStringValue();

    public abstract int getNameCode();

    public abstract int getFingerprint();

    public abstract String getLocalName();

    public abstract String getPrefix();

    public abstract String getURI();

    public abstract String getDisplayName();

    public abstract NodeInfo getParent();

    public abstract AxisEnumeration getEnumeration(byte byte0, NodeTest nodetest);

    public abstract String getAttributeValue(String s, String s1);

    public abstract String getAttributeValue(int i);

    public abstract DocumentInfo getDocumentRoot();

    public abstract boolean hasChildNodes();

    public abstract String generateId();

    public abstract void copy(Outputter outputter)
        throws TransformerException;

    public abstract void copyStringValue(Outputter outputter)
        throws TransformerException;

    public abstract void outputNamespaceNodes(Outputter outputter, boolean flag)
        throws TransformerException;
}
