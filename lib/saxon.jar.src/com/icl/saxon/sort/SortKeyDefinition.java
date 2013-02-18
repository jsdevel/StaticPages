// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SortKeyDefinition.java

package com.icl.saxon.sort;

import com.icl.saxon.Context;
import com.icl.saxon.Loader;
import com.icl.saxon.expr.*;
import com.icl.saxon.om.Name;
import java.io.PrintStream;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.sort:
//            StringComparer, Compare_en, DoubleComparer, TextComparer, 
//            Comparer

public class SortKeyDefinition
{

            private Expression sortKey;
            private Expression order;
            private Expression dataType;
            private Expression caseOrder;
            private Expression language;
            private StaticContext staticContext;
            private Comparer comparer;

            public SortKeyDefinition()
            {
/*  28*/        comparer = null;
            }

            public void setSortKey(Expression expression)
            {
/*  35*/        sortKey = expression;
            }

            public void setOrder(Expression expression)
            {
/*  45*/        order = expression;
            }

            public void setDataType(Expression expression)
            {
/*  55*/        dataType = expression;
            }

            public void setCaseOrder(Expression expression)
            {
/*  65*/        caseOrder = expression;
            }

            public void setLanguage(Expression expression)
            {
/*  75*/        language = expression;
            }

            public void setStaticContext(StaticContext staticcontext)
            {
/*  84*/        staticContext = staticcontext;
            }

            public Expression getSortKey()
            {
/*  89*/        return sortKey;
            }

            public Expression getOrder()
            {
/*  93*/        return ((Expression) (order != null ? order : new StringValue("ascending")));
            }

            public Expression getDataType()
            {
/*  97*/        return ((Expression) (dataType != null ? dataType : new StringValue("text")));
            }

            public Expression getCaseOrder()
            {
/* 101*/        return ((Expression) (caseOrder != null ? caseOrder : new StringValue("#default")));
            }

            public Expression getLanguage()
            {
/* 105*/        return ((Expression) (language != null ? language : new StringValue("en")));
            }

            public void bindComparer()
                throws XPathException
            {
/* 113*/        if((dataType instanceof StringValue) && (order instanceof StringValue) && (caseOrder instanceof StringValue) && (language instanceof StringValue))
/* 117*/            comparer = makeComparer(null);
            }

            public Comparer getComparer(Context context)
                throws XPathException
            {
/* 126*/        if(comparer == null)
/* 127*/            return makeComparer(context);
/* 129*/        else
/* 129*/            return comparer;
            }

            private Comparer makeComparer(Context context)
                throws XPathException
            {
                String s;
/* 144*/        if(order == null)
/* 145*/            s = "ascending";
/* 147*/        else
/* 147*/            s = order.evaluateAsString(context);
                boolean flag;
/* 150*/        if(s.equals("ascending"))
/* 151*/            flag = true;
/* 152*/        else
/* 152*/        if(s.equals("descending"))
/* 153*/            flag = false;
/* 155*/        else
/* 155*/            throw new XPathException("order must be ascending or descending");
                String s3;
/* 162*/        if(dataType == null)
/* 163*/            s3 = "text";
/* 165*/        else
/* 165*/            s3 = dataType.evaluateAsString(context);
                String s1;
                String s2;
/* 168*/        if(s3.equals("text"))
                {
/* 169*/            s1 = null;
/* 170*/            s2 = null;
                } else
/* 171*/        if(s3.equals("number"))
                {
/* 172*/            s1 = null;
/* 173*/            s2 = null;
                } else
                {
/* 175*/            String s4 = Name.getPrefix(s3);
/* 176*/            if(s4.equals(""))
/* 177*/                throw new XPathException("data-type must be text, number, or a prefixed name");
/* 180*/            s1 = staticContext.getURIForPrefix(s4);
/* 181*/            s2 = Name.getLocalName(s3);
                }
                String s5;
/* 186*/        if(caseOrder == null)
/* 187*/            s5 = "#default";
/* 189*/        else
/* 189*/            s5 = caseOrder.evaluateAsString(context);
                byte byte0;
/* 192*/        if(s5.equals("#default"))
/* 193*/            byte0 = 0;
/* 194*/        else
/* 194*/        if(s5.equals("lower-first"))
/* 195*/            byte0 = 1;
/* 196*/        else
/* 196*/        if(s5.equals("upper-first"))
/* 197*/            byte0 = 2;
/* 199*/        else
/* 199*/            throw new XPathException("case-order must be lower-first or upper-first");
                Object obj;
/* 204*/        if(s3.equals("text"))
                {
/* 205*/            if(language == null)
                    {
/* 206*/                obj = new StringComparer();
                    } else
                    {
/* 208*/                String s6 = language.evaluateAsString(context);
/* 209*/                String s7 = "com.icl.saxon.sort.Compare_";
/* 210*/                for(int i = 0; i < s6.length(); i++)
/* 211*/                    if(Character.isLetter(s6.charAt(i)))
/* 212*/                        s7 = s7 + s6.charAt(i);

/* 216*/                try
                        {
/* 216*/                    obj = loadComparer(s7);
                        }
/* 219*/                catch(Exception exception1)
                        {
/* 219*/                    obj = new Compare_en();
                        }
                    }
                } else
/* 223*/        if(s3.equals("number"))
/* 224*/            obj = new DoubleComparer();
/* 227*/        else
/* 227*/            try
                    {
/* 227*/                obj = loadComparer(s2);
                    }
/* 229*/            catch(Exception exception)
                    {
/* 229*/                System.err.println("Warning: no comparer " + s2 + " found; using default");
/* 230*/                obj = new StringComparer();
                    }
/* 234*/        obj = ((Comparer) (obj)).setDataType(s1, s2);
/* 235*/        obj = ((Comparer) (obj)).setOrder(flag);
/* 236*/        if(obj instanceof TextComparer)
/* 237*/            obj = ((TextComparer)obj).setCaseOrder(byte0);
/* 241*/        return ((Comparer) (obj));
            }

            private static TextComparer loadComparer(String s)
                throws XPathException
            {
/* 251*/        try
                {
/* 251*/            return (TextComparer)Loader.getInstance(s);
                }
/* 253*/        catch(ClassCastException classcastexception)
                {
/* 253*/            throw new XPathException("Failed to load TextComparer  " + s + ": it does not implement the TextComparer interface");
                }
/* 256*/        catch(TransformerException transformerexception)
                {
/* 256*/            if(transformerexception instanceof XPathException)
/* 257*/                throw (XPathException)transformerexception;
/* 259*/            else
/* 259*/                throw new XPathException(transformerexception);
                }
            }
}
