// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SQLColumn.java

package com.icl.saxon.sql;

import com.icl.saxon.Context;
import com.icl.saxon.expr.Value;
import com.icl.saxon.om.AbstractNode;
import com.icl.saxon.style.StyleElement;
import com.icl.saxon.style.XSLGeneralVariable;
import com.icl.saxon.tree.ElementWithAttributes;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.sql:
//            SQLInsert

public class SQLColumn extends XSLGeneralVariable
{

            public SQLColumn()
            {
            }

            public boolean isInstruction()
            {
/*  24*/        return false;
            }

            public boolean mayContainTemplateBody()
            {
/*  33*/        return false;
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  37*/        if(!(getParentNode() instanceof SQLInsert))
/*  38*/            compileError("parent node must be sql:insert");
            }

            public void process(Context context)
            {
            }

            public String getColumnName()
            {
/*  47*/        return getAttributeValue("", "name");
            }

            public Value getColumnValue(Context context)
                throws TransformerException
            {
/*  51*/        return getSelectValue(context);
            }
}
