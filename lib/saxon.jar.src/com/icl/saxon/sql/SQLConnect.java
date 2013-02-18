// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SQLConnect.java

package com.icl.saxon.sql;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.expr.StringValue;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.style.StyleElement;
import com.icl.saxon.tree.AttributeCollection;
import com.icl.saxon.tree.ElementWithAttributes;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

public class SQLConnect extends StyleElement
{

            Expression database;
            Expression driver;
            Expression user;
            Expression password;

            public SQLConnect()
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
/*  45*/        String s = super.attributeList.getValue("database");
/*  46*/        if(s == null)
/*  47*/            reportAbsence("database");
/*  48*/        database = makeAttributeValueTemplate(s);
/*  52*/        String s1 = super.attributeList.getValue("driver");
/*  53*/        if(s1 == null)
/*  54*/            if(s.substring(0, 9).equals("jdbc:odbc"))
/*  55*/                s1 = "sun.jdbc.odbc.JdbcOdbcDriver";
/*  57*/            else
/*  57*/                reportAbsence("driver");
/*  60*/        driver = makeAttributeValueTemplate(s1);
/*  65*/        String s2 = super.attributeList.getValue("user");
/*  66*/        if(s2 == null)
/*  67*/            user = new StringValue("");
/*  69*/        else
/*  69*/            user = makeAttributeValueTemplate(s2);
/*  74*/        String s3 = super.attributeList.getValue("password");
/*  75*/        if(s3 == null)
/*  76*/            password = new StringValue("");
/*  78*/        else
/*  78*/            password = makeAttributeValueTemplate(s3);
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  83*/        checkWithinTemplate();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  90*/        Connection connection = null;
/*  91*/        java.sql.Statement statement = null;
/*  93*/        String s = database.evaluateAsString(context);
/*  94*/        String s1 = driver.evaluateAsString(context);
/*  95*/        String s2 = user.evaluateAsString(context);
/*  96*/        String s3 = password.evaluateAsString(context);
/* 100*/        try
                {
/* 100*/            Class.forName(s1);
/* 102*/            connection = DriverManager.getConnection(s, s2, s3);
/* 103*/            statement = connection.createStatement();
                }
/* 105*/        catch(Exception exception)
                {
/* 105*/            throw new TransformerException("JDBC Connection Failure: " + exception.getMessage());
                }
/* 108*/        com.icl.saxon.om.DocumentInfo documentinfo = context.getCurrentNodeInfo().getDocumentRoot();
/* 109*/        context.getController().setUserData(documentinfo, "sql:connection", connection);
/* 110*/        context.getController().setUserData(documentinfo, "sql:statement", statement);
            }
}
