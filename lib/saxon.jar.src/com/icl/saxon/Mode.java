// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Mode.java

package com.icl.saxon;

import com.icl.saxon.expr.XPathException;
import com.icl.saxon.om.Navigator;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.pattern.NoNodeTest;
import com.icl.saxon.pattern.Pattern;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon:
//            Context, Controller

public class Mode
{
    private static class Rule
    {

                public Pattern pattern;
                public Object object;
                public int precedence;
                public double priority;
                public int sequence;
                public Rule next;

                public Rule(Pattern pattern1, Object obj, int i, double d, int j)
                {
/* 324*/            pattern = pattern1;
/* 325*/            object = obj;
/* 326*/            precedence = i;
/* 327*/            priority = d;
/* 328*/            sequence = j;
/* 329*/            next = null;
                }
    }


            private Rule ruleDict[];
            private int nameCode;
            private int sequence;

            public Mode()
            {
/*  18*/        ruleDict = new Rule[114];
/*  19*/        nameCode = -1;
/*  20*/        sequence = 0;
            }

            public void setNameCode(int i)
            {
/*  34*/        nameCode = i;
            }

            public int getNameCode()
            {
/*  42*/        return nameCode;
            }

            public void addRule(Pattern pattern, Object obj, int i, double d)
            {
/*  60*/        if(pattern instanceof NoNodeTest)
/*  61*/            return;
/*  69*/        int j = pattern.getFingerprint();
/*  70*/        short word0 = pattern.getNodeType();
/*  72*/        int k = getList(j, word0);
/*  75*/        Rule rule = new Rule(pattern, obj, i, d, sequence++);
/*  77*/        Rule rule1 = ruleDict[k];
/*  78*/        if(rule1 == null)
                {
/*  79*/            ruleDict[k] = rule;
/*  80*/            return;
                }
/*  85*/        Rule rule2 = null;
/*  87*/        for(; rule1 != null; rule1 = rule1.next)
                {
/*  87*/            if(rule1.precedence < i || rule1.precedence == i && rule1.priority <= d)
                    {
/*  89*/                rule.next = rule1;
/*  90*/                if(rule2 == null)
/*  91*/                    ruleDict[k] = rule;
/*  93*/                else
/*  93*/                    rule2.next = rule;
/*  95*/                break;
                    }
/*  97*/            rule2 = rule1;
                }

/* 101*/        if(rule1 == null)
                {
/* 102*/            rule2.next = rule;
/* 103*/            rule.next = null;
                }
            }

            public int getList(int i, int j)
            {
/* 113*/        if(j == 1)
                {
/* 114*/            if(i == -1)
/* 115*/                return 0;
/* 117*/            else
/* 117*/                return 13 + i % 101;
                } else
                {
/* 121*/            return j;
                }
            }

            public Object getRule(NodeInfo nodeinfo, Context context)
                throws TransformerException
            {
/* 133*/        int i = nodeinfo.getFingerprint();
/* 134*/        short word0 = nodeinfo.getNodeType();
/* 135*/        int j = getList(i, word0);
/* 136*/        int k = context.getController().getRecoveryPolicy();
/* 138*/        Rule rule = null;
/* 139*/        Rule rule1 = null;
/* 140*/        int l = -1;
/* 141*/        double d = (-1.0D / 0.0D);
/* 147*/        if(j != 0)
                {
/* 148*/            for(Rule rule2 = ruleDict[j]; rule2 != null; rule2 = rule2.next)
                    {
/* 152*/                if(rule != null && (rule2.precedence < l || rule2.precedence == l && rule2.priority < d))
/* 155*/                    break;
/* 159*/                if(!rule2.pattern.matches(nodeinfo, context))
/* 163*/                    continue;
/* 163*/                if(rule != null)
                        {
/* 164*/                    if(rule2.precedence == l && rule2.priority == d)
/* 165*/                        reportAmbiguity(nodeinfo, rule.pattern, rule2.pattern, context);
/* 165*/                    break;
                        }
/* 169*/                rule = rule2;
/* 170*/                l = rule2.precedence;
/* 171*/                d = rule2.priority;
/* 172*/                if(k == 0)
/* 173*/                    break;
                    }

                }
/* 182*/        for(Rule rule3 = ruleDict[0]; rule3 != null; rule3 = rule3.next)
                {
/* 184*/            if(rule3.precedence < l || rule3.precedence == l && rule3.priority < d)
/* 186*/                break;
/* 188*/            if(!rule3.pattern.matches(nodeinfo, context))
/* 190*/                continue;
/* 190*/            if(rule1 != null)
                    {
/* 191*/                if(rule3.precedence == rule1.precedence && rule3.priority == rule1.priority)
/* 192*/                    reportAmbiguity(nodeinfo, rule3.pattern, rule1.pattern, context);
/* 192*/                break;
                    }
/* 196*/            rule1 = rule3;
/* 197*/            if(k == 0)
/* 198*/                break;
                }

/* 205*/        if(rule != null && rule1 == null)
/* 206*/            return rule.object;
/* 207*/        if(rule == null && rule1 != null)
/* 208*/            return rule1.object;
/* 209*/        if(rule != null && rule1 != null)
                {
/* 210*/            if(rule.precedence == rule1.precedence && rule.priority == rule1.priority)
                    {
/* 216*/                Object obj = rule.sequence <= rule1.sequence ? rule1.object : rule.object;
/* 220*/                if(k != 0)
/* 221*/                    reportAmbiguity(nodeinfo, rule.pattern, rule1.pattern, context);
/* 223*/                return obj;
                    }
/* 225*/            if(rule.precedence > rule1.precedence || rule.precedence == rule1.precedence && rule.priority >= rule1.priority)
/* 228*/                return rule.object;
/* 230*/            else
/* 230*/                return rule1.object;
                } else
                {
/* 233*/            return null;
                }
            }

            public Object getRule(NodeInfo nodeinfo, int i, int j, Context context)
                throws XPathException
            {
/* 244*/        int k = nodeinfo.getFingerprint();
/* 245*/        short word0 = nodeinfo.getNodeType();
/* 246*/        int l = getList(k, word0);
/* 248*/        Rule rule = null;
/* 249*/        Rule rule1 = null;
/* 253*/        if(l != 0)
                {
/* 254*/            for(Rule rule2 = ruleDict[l]; rule2 != null; rule2 = rule2.next)
                    {
/* 256*/                if(rule2.precedence < i || rule2.precedence > j || !rule2.pattern.matches(nodeinfo, context))
/* 258*/                    continue;
/* 258*/                rule = rule2;
/* 259*/                break;
                    }

                }
/* 267*/        for(Rule rule3 = ruleDict[0]; rule3 != null; rule3 = rule3.next)
                {
/* 269*/            if(rule3.precedence < i || rule3.precedence > j || !rule3.pattern.matches(nodeinfo, context))
/* 270*/                continue;
/* 270*/            rule1 = rule3;
/* 271*/            break;
                }

/* 275*/        if(rule != null && rule1 == null)
/* 276*/            return rule.object;
/* 277*/        if(rule == null && rule1 != null)
/* 278*/            return rule1.object;
/* 279*/        if(rule != null && rule1 != null)
                {
/* 280*/            if(rule.precedence > rule1.precedence || rule.precedence == rule1.precedence && rule.priority >= rule1.priority)
/* 283*/                return rule.object;
/* 285*/            else
/* 285*/                return rule1.object;
                } else
                {
/* 288*/            return null;
                }
            }

            private void reportAmbiguity(NodeInfo nodeinfo, Pattern pattern, Pattern pattern1, Context context)
                throws TransformerException
            {
/* 300*/        if(pattern.getStaticContext() == pattern1.getStaticContext())
                {
/* 301*/            return;
                } else
                {
/* 303*/            context.getController().reportRecoverableError("Ambiguous rule match for " + Navigator.getPath(nodeinfo) + "\n" + "Matches both \"" + pattern + "\" on line " + pattern.getLineNumber() + " of " + pattern.getSystemId() + "\nand \"" + pattern1 + "\" on line " + pattern1.getLineNumber() + " of " + pattern1.getSystemId(), null);
/* 308*/            return;
                }
            }
}
