// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   BooleanExpression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;

// Referenced classes of package com.icl.saxon.expr:
//            BinaryExpression, Value, PositionRange, BooleanValue, 
//            XPathException, Expression

class BooleanExpression extends BinaryExpression
{

            public BooleanExpression()
            {
            }

            public BooleanExpression(Expression expression, int i, Expression expression1)
            {
/*  15*/        super(expression, i, expression1);
            }

            public Expression simplify()
                throws XPathException
            {
/*  19*/        super.p1 = super.p1.simplify();
/*  20*/        super.p2 = super.p2.simplify();
/*  21*/        if((super.p1 instanceof Value) && (super.p2 instanceof Value))
/*  22*/            return evaluate(null);
/*  30*/        if((super.p1 instanceof PositionRange) && (super.p2 instanceof PositionRange))
                {
/*  31*/            PositionRange positionrange = (PositionRange)super.p1;
/*  32*/            PositionRange positionrange1 = (PositionRange)super.p2;
/*  33*/            if(positionrange.getMaxPosition() == 0x7fffffff && positionrange1.getMinPosition() == 1)
/*  34*/                return new PositionRange(positionrange.getMinPosition(), positionrange1.getMaxPosition());
/*  36*/            if(positionrange1.getMaxPosition() == 0x7fffffff && positionrange.getMinPosition() == 1)
/*  37*/                return new PositionRange(positionrange1.getMinPosition(), positionrange.getMaxPosition());
                }
/*  40*/        return this;
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  44*/        return new BooleanValue(evaluateAsBoolean(context));
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/*  48*/        switch(super.operator)
                {
/*  50*/        case 19: // '\023'
/*  50*/            return super.p1.evaluateAsBoolean(context) && super.p2.evaluateAsBoolean(context);

/*  52*/        case 18: // '\022'
/*  52*/            return super.p1.evaluateAsBoolean(context) || super.p2.evaluateAsBoolean(context);
                }
/*  54*/        throw new XPathException("Unknown operator in boolean expression");
            }

            public int getDataType()
            {
/*  64*/        return 1;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  77*/        if((getDependencies() & i) != 0)
                {
/*  78*/            BooleanExpression booleanexpression = new BooleanExpression(super.p1.reduce(i, context), super.operator, super.p2.reduce(i, context));
/*  82*/            booleanexpression.setStaticContext(getStaticContext());
/*  83*/            return booleanexpression.simplify();
                } else
                {
/*  85*/            return this;
                }
            }
}
