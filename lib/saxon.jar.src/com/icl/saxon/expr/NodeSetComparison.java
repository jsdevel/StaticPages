// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   NodeSetComparison.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;
import java.io.PrintStream;

// Referenced classes of package com.icl.saxon.expr:
//            Expression, BooleanValue, SingletonExpression, SingletonComparison, 
//            NodeSetExpression, NodeSetValue, XPathException, Value, 
//            Tokenizer

public class NodeSetComparison extends Expression
{

            NodeSetExpression nodeset;
            int operator;
            Value value;

            public NodeSetComparison(NodeSetExpression nodesetexpression, int i, Value value1)
            {
/*  19*/        nodeset = nodesetexpression;
/*  20*/        operator = i;
/*  21*/        value = value1;
            }

            public Expression simplify()
            {
/*  30*/        return this;
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  40*/        return new BooleanValue(evaluateAsBoolean(context));
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  50*/        NodeEnumeration nodeenumeration = nodeset.enumerate(context, false);
/*  51*/        switch(operator)
                {
/*  53*/        case 11: // '\013'
/*  53*/            NodeSetComparison _tmp = this;
/*  53*/            if(value.getDataType() == 2)
                    {
/*  54*/                double d = value.asNumber();
/*  56*/                while(nodeenumeration.hasMoreElements()) 
                        {
/*  56*/                    NodeInfo nodeinfo2 = nodeenumeration.nextElement();
/*  57*/                    if(Value.stringToNumber(nodeinfo2.getStringValue()) == d)
/*  58*/                        return true;
                        }
/*  61*/                return false;
                    }
/*  63*/            String s = value.asString();
/*  65*/            while(nodeenumeration.hasMoreElements()) 
                    {
/*  65*/                NodeInfo nodeinfo = nodeenumeration.nextElement();
/*  66*/                if(nodeinfo.getStringValue().equals(s))
/*  67*/                    return true;
                    }
/*  70*/            return false;

/*  74*/        case 34: // '"'
/*  74*/            NodeSetComparison _tmp1 = this;
/*  74*/            if(value.getDataType() == 2)
                    {
/*  75*/                double d1 = value.asNumber();
/*  77*/                while(nodeenumeration.hasMoreElements()) 
                        {
/*  77*/                    NodeInfo nodeinfo3 = nodeenumeration.nextElement();
/*  78*/                    if(Value.stringToNumber(nodeinfo3.getStringValue()) != d1)
/*  79*/                        return true;
                        }
/*  82*/                return false;
                    }
/*  84*/            String s1 = value.asString();
/*  86*/            while(nodeenumeration.hasMoreElements()) 
                    {
/*  86*/                NodeInfo nodeinfo1 = nodeenumeration.nextElement();
/*  87*/                if(!nodeinfo1.getStringValue().equals(s1))
/*  88*/                    return true;
                    }
/*  91*/            return false;

/*  95*/        case 24: // '\030'
/*  95*/            double d2 = value.asNumber();
/*  97*/            while(nodeenumeration.hasMoreElements()) 
                    {
/*  97*/                NodeInfo nodeinfo4 = nodeenumeration.nextElement();
/*  98*/                if(Value.stringToNumber(nodeinfo4.getStringValue()) <= d2)
/*  99*/                    return true;
                    }
/* 102*/            return false;

/* 105*/        case 22: // '\026'
/* 105*/            double d3 = value.asNumber();
/* 107*/            while(nodeenumeration.hasMoreElements()) 
                    {
/* 107*/                NodeInfo nodeinfo5 = nodeenumeration.nextElement();
/* 108*/                if(Value.stringToNumber(nodeinfo5.getStringValue()) < d3)
/* 109*/                    return true;
                    }
/* 112*/            return false;

/* 115*/        case 23: // '\027'
/* 115*/            double d4 = value.asNumber();
/* 117*/            while(nodeenumeration.hasMoreElements()) 
                    {
/* 117*/                NodeInfo nodeinfo6 = nodeenumeration.nextElement();
/* 118*/                if(Value.stringToNumber(nodeinfo6.getStringValue()) >= d4)
/* 119*/                    return true;
                    }
/* 122*/            return false;

/* 125*/        case 21: // '\025'
/* 125*/            double d5 = value.asNumber();
/* 127*/            while(nodeenumeration.hasMoreElements()) 
                    {
/* 127*/                NodeInfo nodeinfo7 = nodeenumeration.nextElement();
/* 128*/                if(Value.stringToNumber(nodeinfo7.getStringValue()) > d5)
/* 129*/                    return true;
                    }
/* 132*/            return false;
                }
/* 135*/        return false;
            }

            public int getDataType()
            {
/* 147*/        return 1;
            }

            public int getDependencies()
            {
/* 157*/        return nodeset.getDependencies();
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/* 170*/        if((nodeset.getDependencies() & i) != 0)
                {
/* 171*/            Object obj = nodeset.reduce(i, context);
/* 172*/            if(obj instanceof SingletonExpression)
                    {
/* 173*/                obj = new SingletonComparison((SingletonExpression)obj, operator, value);
/* 177*/                ((Expression) (obj)).setStaticContext(getStaticContext());
/* 178*/                return ((Expression) (obj)).simplify();
                    }
/* 179*/            if(obj instanceof NodeSetExpression)
                    {
/* 180*/                obj = new NodeSetComparison((NodeSetExpression)obj, operator, value);
/* 184*/                ((Expression) (obj)).setStaticContext(getStaticContext());
/* 185*/                return ((Expression) (obj)).simplify();
                    }
/* 186*/            if(obj instanceof NodeSetValue)
/* 187*/                return new BooleanValue(((NodeSetValue)obj).compare(operator, value));
/* 189*/            else
/* 189*/                throw new XPathException("Failed to reduce NodeSetComparison: returned " + obj.getClass());
                } else
                {
/* 192*/            return this;
                }
            }

            public void display(int i)
            {
/* 201*/        System.err.println(Expression.indent(i) + Tokenizer.tokens[operator]);
/* 202*/        nodeset.display(i + 1);
/* 203*/        value.display(i + 1);
            }
}
