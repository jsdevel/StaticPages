// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   RelationalExpression.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.functions.*;

// Referenced classes of package com.icl.saxon.expr:
//            BinaryExpression, SingletonExpression, StringValue, NumericValue, 
//            FragmentValue, TextFragmentValue, SingletonComparison, Value, 
//            NodeSetExpression, NodeSetComparison, BooleanValue, PositionRange, 
//            IsLastExpression, XPathException, Expression, Function

final class RelationalExpression extends BinaryExpression
{

            public RelationalExpression()
            {
            }

            public RelationalExpression(Expression expression, int i, Expression expression1)
            {
/*  27*/        super(expression, i, expression1);
            }

            public Expression simplify()
                throws XPathException
            {
/*  37*/        super.p1 = super.p1.simplify();
/*  38*/        super.p2 = super.p2.simplify();
/*  42*/        if((super.p1 instanceof SingletonExpression) && ((super.p2 instanceof StringValue) || (super.p2 instanceof NumericValue) || (super.p2 instanceof FragmentValue) || (super.p2 instanceof TextFragmentValue)))
                {
/*  48*/            SingletonComparison singletoncomparison = new SingletonComparison((SingletonExpression)super.p1, super.operator, (Value)super.p2);
/*  49*/            singletoncomparison.setStaticContext(getStaticContext());
/*  50*/            return singletoncomparison;
                }
/*  53*/        if((super.p2 instanceof SingletonExpression) && ((super.p1 instanceof StringValue) || (super.p1 instanceof NumericValue) || (super.p1 instanceof FragmentValue) || (super.p1 instanceof TextFragmentValue)))
                {
/*  59*/            SingletonComparison singletoncomparison1 = new SingletonComparison((SingletonExpression)super.p2, Value.inverse(super.operator), (Value)super.p1);
/*  61*/            singletoncomparison1.setStaticContext(getStaticContext());
/*  62*/            return singletoncomparison1;
                }
/*  67*/        if((super.p1 instanceof NodeSetExpression) && ((super.p2 instanceof StringValue) || (super.p2 instanceof NumericValue) || (super.p2 instanceof FragmentValue) || (super.p2 instanceof TextFragmentValue)))
                {
/*  73*/            NodeSetComparison nodesetcomparison = new NodeSetComparison((NodeSetExpression)super.p1, super.operator, (Value)super.p2);
/*  74*/            nodesetcomparison.setStaticContext(getStaticContext());
/*  75*/            return nodesetcomparison;
                }
/*  78*/        if((super.p2 instanceof NodeSetExpression) && ((super.p1 instanceof StringValue) || (super.p1 instanceof NumericValue) || (super.p1 instanceof FragmentValue) || (super.p1 instanceof TextFragmentValue)))
                {
/*  84*/            NodeSetComparison nodesetcomparison1 = new NodeSetComparison((NodeSetExpression)super.p2, Value.inverse(super.operator), (Value)super.p1);
/*  86*/            nodesetcomparison1.setStaticContext(getStaticContext());
/*  87*/            return nodesetcomparison1;
                }
/*  92*/        if((super.p1 instanceof Value) && (super.p2 instanceof Value))
/*  93*/            return evaluate(null);
/*  98*/        if((super.p1 instanceof Count) && ((Count)super.p1).getNumberOfArguments() == 1 && ((Function) ((Count)super.p1)).argument[0].getDataType() == 4 && (super.p2 instanceof NumericValue) && ((Value)super.p2).asNumber() == 0.0D)
                {
/* 101*/            if(super.operator == 11 || super.operator == 24)
                    {
/* 103*/                Not not = new Not();
/* 104*/                not.addArgument(((Function) ((Count)super.p1)).argument[0]);
/* 105*/                not.setStaticContext(getStaticContext());
/* 106*/                return not;
                    }
/* 107*/            if(super.operator == 34 || super.operator == 21)
                    {
/* 109*/                BooleanFn booleanfn = new BooleanFn();
/* 110*/                booleanfn.addArgument(((Function) ((Count)super.p1)).argument[0]);
/* 111*/                booleanfn.setStaticContext(getStaticContext());
/* 112*/                return booleanfn;
                    }
/* 113*/            if(super.operator == 23)
/* 115*/                return new BooleanValue(true);
/* 118*/            else
/* 118*/                return new BooleanValue(false);
                }
/* 124*/        if((super.p2 instanceof Count) && (super.p1 instanceof NumericValue) && ((Value)super.p1).asNumber() == 0.0D)
                {
/* 126*/            Expression expression = (new RelationalExpression(super.p2, Value.inverse(super.operator), super.p1)).simplify();
/* 127*/            expression.setStaticContext(getStaticContext());
/* 128*/            return expression;
                }
/* 133*/        if((super.p1 instanceof StringLength) && ((StringLength)super.p1).getNumberOfArguments() == 1 && (super.p2 instanceof NumericValue) && ((Value)super.p2).asNumber() == 0.0D)
                {
/* 138*/            Object obj = ((Function) ((StringLength)super.p1)).argument[0];
/* 139*/            if(!(obj instanceof StringValue))
                    {
/* 140*/                StringFn stringfn = new StringFn();
/* 141*/                stringfn.addArgument(((Expression) (obj)));
/* 142*/                obj = stringfn;
                    }
/* 145*/            if(super.operator == 11 || super.operator == 24)
                    {
/* 147*/                Not not1 = new Not();
/* 148*/                not1.addArgument(((Expression) (obj)));
/* 149*/                not1.setStaticContext(getStaticContext());
/* 150*/                return not1;
                    }
/* 151*/            if(super.operator == 21 || super.operator == 34)
                    {
/* 153*/                BooleanFn booleanfn1 = new BooleanFn();
/* 154*/                booleanfn1.addArgument(((Expression) (obj)));
/* 155*/                booleanfn1.setStaticContext(getStaticContext());
/* 156*/                return booleanfn1;
                    }
/* 157*/            if(super.operator == 23)
/* 159*/                return new BooleanValue(true);
/* 162*/            else
/* 162*/                return new BooleanValue(false);
                }
/* 168*/        if((super.p2 instanceof StringLength) && (super.p1 instanceof NumericValue) && ((Value)super.p1).asNumber() == 0.0D)
                {
/* 170*/            Expression expression1 = (new RelationalExpression(super.p2, Value.inverse(super.operator), super.p1)).simplify();
/* 171*/            expression1.setStaticContext(getStaticContext());
/* 172*/            return expression1;
                }
/* 177*/        if((super.p1 instanceof Position) && (super.p2 instanceof NumericValue))
                {
/* 178*/            double d = ((NumericValue)super.p2).asNumber();
/* 179*/            switch(super.operator)
                    {
/* 181*/            case 11: // '\013'
/* 181*/                return new PositionRange((int)d, (int)d);

/* 183*/            case 23: // '\027'
/* 183*/                return new PositionRange((int)d, 0x7fffffff);

/* 187*/            case 22: // '\026'
/* 187*/                return new PositionRange(1, (int)Math.floor(d - 9.9999999999999994E-12D));

/* 189*/            case 21: // '\025'
/* 189*/                return new PositionRange((int)Math.ceil(d + 9.9999999999999994E-12D), 0x7fffffff);

/* 191*/            case 24: // '\030'
/* 191*/                return new PositionRange(1, (int)d);
                    }
                }
/* 194*/        if((super.p1 instanceof NumericValue) && (super.p2 instanceof Position))
                {
/* 195*/            double d1 = ((NumericValue)super.p1).asNumber();
/* 196*/            switch(super.operator)
                    {
/* 198*/            case 11: // '\013'
/* 198*/                return new PositionRange((int)d1, (int)d1);

/* 200*/            case 24: // '\030'
/* 200*/                return new PositionRange((int)d1, 0x7fffffff);

/* 204*/            case 21: // '\025'
/* 204*/                return new PositionRange(1, (int)Math.floor(d1 - 9.9999999999999994E-12D));

/* 206*/            case 22: // '\026'
/* 206*/                return new PositionRange((int)Math.ceil(d1 + 9.9999999999999994E-12D), 0x7fffffff);

/* 208*/            case 23: // '\027'
/* 208*/                return new PositionRange(1, (int)d1);
                    }
                }
/* 214*/        if((super.p1 instanceof Position) && (super.p2 instanceof Last))
/* 215*/            switch(super.operator)
                    {
/* 218*/            case 11: // '\013'
/* 218*/            case 23: // '\027'
/* 218*/                return new IsLastExpression(true);

/* 221*/            case 22: // '\026'
/* 221*/            case 34: // '"'
/* 221*/                return new IsLastExpression(false);

/* 223*/            case 21: // '\025'
/* 223*/                return new BooleanValue(false);

/* 225*/            case 24: // '\030'
/* 225*/                return new BooleanValue(true);
                    }
/* 228*/        if((super.p1 instanceof Last) && (super.p2 instanceof Position))
/* 229*/            switch(super.operator)
                    {
/* 232*/            case 11: // '\013'
/* 232*/            case 24: // '\030'
/* 232*/                return new IsLastExpression(true);

/* 235*/            case 21: // '\025'
/* 235*/            case 34: // '"'
/* 235*/                return new IsLastExpression(false);

/* 237*/            case 22: // '\026'
/* 237*/                return new BooleanValue(false);

/* 239*/            case 23: // '\027'
/* 239*/                return new BooleanValue(true);
                    }
/* 242*/        return this;
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/* 252*/        return new BooleanValue(evaluateAsBoolean(context));
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/* 262*/        Value value = super.p1.evaluate(context);
/* 263*/        Value value1 = super.p2.evaluate(context);
/* 264*/        return value.compare(super.operator, value1);
            }

            public int getDataType()
            {
/* 273*/        return 1;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/* 287*/        if((getDependencies() & i) != 0)
                {
/* 288*/            RelationalExpression relationalexpression = new RelationalExpression(super.p1.reduce(i, context), super.operator, super.p2.reduce(i, context));
/* 292*/            relationalexpression.setStaticContext(getStaticContext());
/* 293*/            return relationalexpression.simplify();
                } else
                {
/* 295*/            return this;
                }
            }
}
