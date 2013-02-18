// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLTContext.java

package org.w3c.xsl;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface XSLTContext
{

    public abstract Node getContextNode();

    public abstract int getContextPosition();

    public abstract int getContextSize();

    public abstract Node getCurrentNode();

    public abstract Document getOwnerDocument();

    public abstract Object systemProperty(String s, String s1);

    public abstract String stringValue(Node node);
}
