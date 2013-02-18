// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ArithmeticExpression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;

// Referenced classes of package com.icl.saxon.expr:
//            BinaryExpression, NumericValue, XPathException, Expression, 
//            Value

class ArithmeticExpression extends BinaryExpression
{

            public ArithmeticExpression()
            {
            }

            public ArithmeticExpression(Expression expression, int i, Expression expression1)
            {
/*  15*/        super(expression, i, expression1);
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/*  25*/        return new NumericValue(evaluateAsNumber(context));
            }

            public double evaluateAsNumber(Context context)
                throws XPathException
            {
/*  35*/        switch(super.operator)
                {
/*  37*/        case 25: // '\031'
/*  37*/            return super.p1.evaluateAsNumber(context) + super.p2.evaluateAsNumber(context);

/*  39*/        case 26: // '\032'
/*  39*/            return super.p1.evaluateAsNumber(context) - super.p2.evaluateAsNumber(context);

/*  41*/        case 27: // '\033'
/*  41*/            return super.p1.evaluateAsNumber(context) * super.p2.evaluateAsNumber(context);

/*  43*/        case 28: // '\034'
/*  43*/            return super.p1.evaluateAsNumber(context) / super.p2.evaluateAsNumber(context);

/*  45*/        case 29: // '\035'
/*  45*/            return super.p1.evaluateAsNumber(context) % super.p2.evaluateAsNumber(context);

/*  47*/        case 99: // 'c'
/*  47*/            return -super.p2.evaluateAsNumber(context);
                }
/*  50*/        throw new XPathException("Unknown operator in arithmetic expression");
            }

            public int getDataType()
            {
/*  60*/        return 2;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/*  73*/        if((getDependencies() & i) != 0)
                {
/*  74*/            ArithmeticExpression arithmeticexpression = new ArithmeticExpression(super.p1.reduce(i, context), super.operator, super.p2.reduce(i, context));
/*  78*/            arithmeticexpression.setStaticContext(getStaticContext());
/*  79*/            return arithmeticexpression.simplify();
                } else
                {
/*  81*/            return this;
                }
            }
}
