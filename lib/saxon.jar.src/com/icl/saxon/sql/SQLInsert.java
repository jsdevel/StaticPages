// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SQLInsert.java

package com.icl.saxon.sql;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.expr.Value;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.style.StyleElement;
import com.icl.saxon.tree.ElementWithAttributes;
import java.sql.*;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Node;

// Referenced classes of package com.icl.saxon.sql:
//            SQLColumn

public class SQLInsert extends StyleElement
{

            String table;

            public SQLInsert()
            {
            }

            public boolean isInstruction()
            {
/*  27*/        return true;
            }

            public boolean mayContainTemplateBody()
            {
/*  37*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  42*/        table = getAttribute("table");
/*  43*/        if(table == null)
/*  43*/            reportAbsence("table");
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  48*/        checkWithinTemplate();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  55*/        Controller controller = context.getController();
/*  56*/        com.icl.saxon.om.DocumentInfo documentinfo = context.getCurrentNodeInfo().getDocumentRoot();
/*  57*/        Connection connection = (Connection)controller.getUserData(documentinfo, "sql:connection");
/*  59*/        if(connection == null)
/*  60*/            throw styleError("No SQL connection has been established");
/*  63*/        PreparedStatement preparedstatement = (PreparedStatement)controller.getUserData(this, "sql:statement");
/*  67*/        try
                {
/*  67*/            if(preparedstatement == null)
                    {
/*  69*/                StringBuffer stringbuffer = new StringBuffer();
/*  70*/                stringbuffer.append("INSERT INTO " + table + " (");
/*  74*/                Node node1 = getFirstChild();
/*  75*/                int j = 0;
/*  77*/                for(; node1 != null; node1 = node1.getNextSibling())
/*  77*/                    if(node1 instanceof SQLColumn)
                            {
/*  78*/                        if(j++ > 0)
/*  78*/                            stringbuffer.append(',');
/*  79*/                        String s = ((SQLColumn)node1).getColumnName();
/*  80*/                        stringbuffer.append(s);
                            }

/*  84*/                stringbuffer.append(") VALUES (");
/*  88*/                for(int l = 0; l < j; l++)
                        {
/*  89*/                    if(l != 0)
/*  90*/                        stringbuffer.append(',');
/*  91*/                    stringbuffer.append('?');
                        }

/*  94*/                stringbuffer.append(')');
/*  98*/                preparedstatement = connection.prepareStatement(stringbuffer.toString());
/*  99*/                controller.setUserData(this, "sql:statement", preparedstatement);
                    }
/* 104*/            Node node = getFirstChild();
/* 105*/            int i = 1;
/* 107*/            for(; node != null; node = node.getNextSibling())
/* 107*/                if(node instanceof SQLColumn)
                        {
/* 110*/                    Value value = ((SQLColumn)node).getColumnValue(context);
/* 113*/                    String s1 = value.asString();
/* 116*/                    if(s1.length() == 1)
/* 116*/                        s1 = s1 + " ";
/* 117*/                    preparedstatement.setObject(i++, s1);
                        }

/* 125*/            int k = preparedstatement.executeUpdate();
/* 126*/            if(!connection.getAutoCommit())
/* 127*/                connection.commit();
                }
/* 131*/        catch(SQLException sqlexception)
                {
/* 131*/            throw styleError("(SQL) " + sqlexception.getMessage());
                }
            }
}
