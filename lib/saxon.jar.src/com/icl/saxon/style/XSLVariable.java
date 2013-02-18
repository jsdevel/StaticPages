// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XSLVariable.java

package com.icl.saxon.style;

import com.icl.saxon.*;
import com.icl.saxon.expr.Expression;
import com.icl.saxon.expr.Value;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            XSLGeneralVariable, StyleElement, XSLStyleSheet, Procedure

public class XSLVariable extends XSLGeneralVariable
    implements Binding
{

            private int slotNumber;

            public XSLVariable()
            {
            }

            public int getSlotNumber()
            {
/*  20*/        return slotNumber;
            }

            public boolean isInstruction()
            {
/*  29*/        return true;
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  37*/        super.validate();
/*  38*/        checkDuplicateDeclaration();
/*  39*/        if(super.global && !super.redundant)
                {
/*  40*/            slotNumber = getPrincipalStyleSheet().allocateSlotNumber();
                } else
                {
/*  42*/            Procedure procedure = getOwningProcedure();
/*  43*/            slotNumber = procedure.allocateSlotNumber();
                }
            }

            public int getDataType()
            {
/*  53*/        if(super.assignable)
/*  54*/            return -1;
/*  56*/        if(super.select != null)
/*  57*/            return super.select.getDataType();
/*  59*/        else
/*  59*/            return 4;
            }

            public Value constantValue()
            {
/*  69*/        if(super.assignable)
/*  70*/            return null;
/*  72*/        if(super.select != null && (super.select instanceof Value))
/*  73*/            return (Value)super.select;
/*  75*/        else
/*  75*/            return null;
            }

            public void process(Context context)
                throws TransformerException
            {
/*  86*/        Bindery bindery = context.getBindery();
/*  87*/        if(super.global)
                {
/*  88*/            if(!super.redundant && !bindery.isEvaluated(this))
                    {
/*  89*/                Value value = getSelectValue(context);
/*  90*/                bindery.defineGlobalVariable(this, value);
                    }
                } else
                {
/*  93*/            Value value1 = getSelectValue(context);
/*  94*/            bindery.defineLocalVariable(this, value1);
                }
            }
}
