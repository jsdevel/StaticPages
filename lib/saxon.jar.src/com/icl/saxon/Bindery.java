// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Bindery.java

package com.icl.saxon;

import com.icl.saxon.expr.Value;
import com.icl.saxon.expr.XPathException;

// Referenced classes of package com.icl.saxon:
//            ParameterSet, Binding

public final class Bindery
{

            private Object globals[];
            private boolean busy[];
            private Object stack[][];
            private Object currentStackFrame[];
            private ParameterSet globalParameters;
            private int top;
            private int allocated;
            private int globalSpace;
            private int localSpace;

            public Bindery()
            {
/*  16*/        stack = new Object[20][];
/*  19*/        top = -1;
/*  20*/        allocated = 0;
/*  21*/        globalSpace = 0;
/*  22*/        localSpace = 0;
            }

            public void allocateGlobals(int i)
            {
/*  29*/        globalSpace = i;
/*  30*/        globals = new Object[i];
/*  31*/        busy = new boolean[i];
/*  32*/        for(int j = 0; j < i; j++)
                {
/*  33*/            globals[j] = null;
/*  34*/            busy[j] = false;
                }

            }

            public void defineGlobalParameters(ParameterSet parameterset)
            {
/*  44*/        globalParameters = parameterset;
            }

            public boolean useGlobalParameter(int i, Binding binding)
            {
/*  57*/        if(globalParameters == null)
/*  57*/            return false;
/*  58*/        Value value = globalParameters.get(i);
/*  59*/        if(value == null)
                {
/*  59*/            return false;
                } else
                {
/*  60*/            globals[binding.getSlotNumber()] = value;
/*  61*/            return true;
                }
            }

            public void defineGlobalVariable(Binding binding, Value value)
            {
/*  72*/        globals[binding.getSlotNumber()] = value;
            }

            public void setExecuting(Binding binding, boolean flag)
                throws XPathException
            {
/*  84*/        int i = binding.getSlotNumber();
/*  85*/        if(flag)
                {
/*  86*/            if(busy[i])
/*  87*/                throw new XPathException("Circular definition of variable " + binding.getVariableName());
/*  94*/            busy[i] = true;
                } else
                {
/*  96*/            busy[i] = false;
                }
            }

            public boolean isEvaluated(Binding binding)
            {
/* 105*/        return globals[binding.getSlotNumber()] != null;
            }

            public void allocateLocals(int i)
            {
/* 114*/        if(i > localSpace)
/* 115*/            localSpace = i;
            }

            public void openStackFrame(ParameterSet parameterset)
            {
/* 124*/        if(++top >= allocated)
                {
/* 125*/            if(allocated == stack.length)
                    {
/* 126*/                Object aobj[][] = new Object[allocated * 2][];
/* 127*/                System.arraycopy(((Object) (stack)), 0, ((Object) (aobj)), 0, allocated);
/* 128*/                stack = aobj;
                    }
/* 130*/            currentStackFrame = new Object[localSpace + 1];
/* 131*/            stack[top] = currentStackFrame;
/* 132*/            allocated++;
                } else
                {
/* 134*/            currentStackFrame = stack[top];
                }
/* 137*/        currentStackFrame[0] = parameterset;
/* 138*/        for(int i = 1; i < currentStackFrame.length; i++)
/* 139*/            currentStackFrame[i] = null;

            }

            public void closeStackFrame()
            {
/* 148*/        top--;
/* 149*/        currentStackFrame = top >= 0 ? stack[top] : null;
            }

            public boolean useLocalParameter(int i, Binding binding)
            {
/* 162*/        ParameterSet parameterset = (ParameterSet)currentStackFrame[0];
/* 163*/        if(parameterset == null)
                {
/* 163*/            return false;
                } else
                {
/* 164*/            Value value = parameterset.get(i);
/* 165*/            currentStackFrame[binding.getSlotNumber() + 1] = value;
/* 166*/            return value != null;
                }
            }

            public Value getLocalParameter(int i)
            {
/* 178*/        ParameterSet parameterset = (ParameterSet)currentStackFrame[0];
/* 179*/        if(parameterset == null)
/* 179*/            return null;
/* 180*/        else
/* 180*/            return parameterset.get(i);
            }

            public void defineLocalVariable(Binding binding, Value value)
            {
/* 190*/        if(currentStackFrame == null)
                {
/* 191*/            throw new IllegalArgumentException("Can't define local variable: stack is empty");
                } else
                {
/* 193*/            currentStackFrame[binding.getSlotNumber() + 1] = value;
/* 194*/            return;
                }
            }

            public Value getValue(Binding binding)
            {
/* 203*/        if(binding.isGlobal())
/* 204*/            return (Value)globals[binding.getSlotNumber()];
/* 206*/        if(currentStackFrame != null)
/* 207*/            return (Value)currentStackFrame[binding.getSlotNumber() + 1];
/* 209*/        else
/* 209*/            return null;
            }

            public Value getValue(Binding binding, int i)
            {
/* 222*/        if(binding.isGlobal())
/* 223*/            return (Value)globals[binding.getSlotNumber()];
/* 225*/        Object aobj[] = stack[i];
/* 226*/        if(aobj != null)
/* 227*/            return (Value)aobj[binding.getSlotNumber() + 1];
/* 229*/        else
/* 229*/            return null;
            }

            public int getFrameId()
            {
/* 240*/        return top;
            }

            public void assignVariable(Binding binding, Value value)
            {
/* 251*/        if(binding.isGlobal())
/* 252*/            defineGlobalVariable(binding, value);
/* 254*/        else
/* 254*/            defineLocalVariable(binding, value);
            }
}
