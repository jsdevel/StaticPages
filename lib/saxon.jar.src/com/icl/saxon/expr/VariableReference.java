// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   VariableReference.java

package com.icl.saxon.expr;

import com.icl.saxon.*;
import com.icl.saxon.style.StyleElement;
import com.icl.saxon.style.XSLGeneralVariable;
import com.icl.saxon.trace.TraceListener;
import java.io.PrintStream;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.expr:
//            Expression, XPathException, StaticContext, Value

public class VariableReference extends Expression
{

            int fingerprint;
            Binding binding;

            public VariableReference(int i, StaticContext staticcontext)
                throws XPathException
            {
/*  24*/        fingerprint = i;
/*  25*/        binding = staticcontext.bindVariable(i);
            }

            public int getDependencies()
            {
/*  35*/        return 1;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  48*/        if((i & 1) != 0)
/*  49*/            return evaluate(context);
/*  51*/        else
/*  51*/            return this;
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  65*/        Bindery bindery = context.getBindery();
/*  66*/        Value value = bindery.getValue(binding);
/*  68*/        if(value == null)
                {
/*  70*/            if(!binding.isGlobal())
/*  71*/                throw new XPathException("Variable " + binding.getVariableName() + " is undefined");
/*  80*/            try
                    {
/*  80*/                bindery.setExecuting(binding, true);
/*  82*/                if(binding instanceof XSLGeneralVariable)
/*  83*/                    if(context.getController().isTracing())
                            {
/*  84*/                        TraceListener tracelistener = context.getController().getTraceListener();
/*  86*/                        tracelistener.enter((XSLGeneralVariable)binding, context);
/*  87*/                        ((XSLGeneralVariable)binding).process(context);
/*  88*/                        tracelistener.leave((XSLGeneralVariable)binding, context);
                            } else
                            {
/*  91*/                        ((XSLGeneralVariable)binding).process(context);
                            }
/*  95*/                bindery.setExecuting(binding, false);
/*  97*/                value = bindery.getValue(binding);
                    }
/* 100*/            catch(TransformerException transformerexception)
                    {
/* 100*/                if(transformerexception instanceof XPathException)
/* 101*/                    throw (XPathException)transformerexception;
/* 103*/                else
/* 103*/                    throw new XPathException(transformerexception);
                    }
/* 107*/            if(value == null)
/* 108*/                throw new XPathException("Variable " + binding.getVariableName() + " is undefined");
                }
/* 111*/        return value;
            }

            public Binding getBinding()
            {
/* 120*/        return binding;
            }

            public int getDataType()
            {
/* 130*/        return binding.getDataType();
            }

            public Expression simplify()
            {
/* 139*/        Value value = binding.constantValue();
/* 140*/        if(value == null)
/* 141*/            return this;
/* 143*/        else
/* 143*/            return value;
            }

            public void display(int i)
            {
/* 152*/        System.err.println(Expression.indent(i) + "$" + binding.getVariableName());
            }
}
