// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SQLClose.java

package com.icl.saxon.sql;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.style.StyleElement;
import java.sql.Connection;
import java.sql.SQLException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

public class SQLClose extends StyleElement
{

            Expression database;
            Expression driver;
            Expression user;
            Expression password;

            public SQLClose()
            {
            }

            public boolean isInstruction()
            {
/*  29*/        return true;
            }

            public boolean mayContainTemplateBody()
            {
/*  38*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  45*/        checkWithinTemplate();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  52*/        com.icl.saxon.om.DocumentInfo documentinfo = context.getCurrentNodeInfo().getDocumentRoot();
/*  53*/        Connection connection = (Connection)context.getController().getUserData(documentinfo, "sql:connection");
/*  55*/        if(connection == null)
/*  56*/            throw styleError("No SQL connection has been established");
/*  60*/        try
                {
/*  60*/            connection.close();
                }
/*  62*/        catch(SQLException sqlexception)
                {
/*  62*/            throw styleError("(SQL) Failed to close connection: " + sqlexception.getMessage());
                }
            }
}
