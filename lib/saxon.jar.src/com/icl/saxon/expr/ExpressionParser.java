// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   ExpressionParser.java

package com.icl.saxon.expr;

import com.icl.saxon.functions.*;
import com.icl.saxon.om.Axis;
import com.icl.saxon.om.Name;
import com.icl.saxon.pattern.*;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.expr:
//            XPathException, Tokenizer, BooleanExpression, RelationalExpression, 
//            ArithmeticExpression, NumericValue, UnionExpression, RootExpression, 
//            ContextNodeExpression, ParentNodeExpression, FilterExpression, VariableReference, 
//            StringValue, PathExpression, Step, Function, 
//            FunctionProxy, ErrorExpression, BooleanValue, Expression, 
//            StaticContext

public final class ExpressionParser
{

            private Tokenizer t;
            private StaticContext env;
            private static final int CHILD_AXIS = 0;
            private static final int ATTRIBUTE_AXIS = 1;

            public ExpressionParser()
            {
            }

            private void expect(int i)
                throws XPathException
            {
/*  33*/        if(t.currentToken != i)
/*  34*/            grumble("expected \"" + Tokenizer.tokens[i] + "\"" + ", found \"" + Tokenizer.tokens[t.currentToken] + "\"");
            }

            private void grumble(String s)
                throws XPathException
            {
/*  43*/        throw new XPathException("Error in expression " + t.pattern + ": " + s);
            }

            public Expression parse(String s, StaticContext staticcontext)
                throws XPathException
            {
/*  54*/        env = staticcontext;
/*  55*/        t = new Tokenizer();
/*  56*/        t.tokenize(s);
/*  57*/        Expression expression = parseExpression();
/*  58*/        if(t.currentToken != 0)
/*  59*/            grumble("Unexpected token " + Tokenizer.tokens[t.currentToken] + " beyond end of expression");
/*  60*/        expression.setStaticContext(staticcontext);
/*  61*/        return expression;
            }

            public Pattern parsePattern(String s, StaticContext staticcontext)
                throws XPathException
            {
/*  72*/        env = staticcontext;
/*  73*/        t = new Tokenizer();
/*  74*/        t.tokenize(s);
/*  75*/        Pattern pattern = parseUnionPattern();
/*  76*/        if(t.currentToken != 0)
/*  77*/            grumble("Unexpected token " + Tokenizer.tokens[t.currentToken] + " beyond end of pattern");
/*  78*/        pattern.setStaticContext(staticcontext);
/*  79*/        return pattern;
            }

            private Expression parseExpression()
                throws XPathException
            {
/*  94*/        Object obj = parseAndExpression();
/*  96*/        while(t.currentToken == 18) 
                {
/*  96*/            t.next();
/*  97*/            obj = new BooleanExpression(((Expression) (obj)), 18, parseAndExpression());
/*  98*/            ((Expression) (obj)).setStaticContext(env);
                }
/* 100*/        return ((Expression) (obj));
            }

            private Expression parseAndExpression()
                throws XPathException
            {
/* 109*/        Object obj = parseEqualityExpression();
/* 111*/        while(t.currentToken == 19) 
                {
/* 111*/            t.next();
/* 112*/            obj = new BooleanExpression(((Expression) (obj)), 19, parseEqualityExpression());
/* 113*/            ((Expression) (obj)).setStaticContext(env);
                }
/* 115*/        return ((Expression) (obj));
            }

            private Expression parseEqualityExpression()
                throws XPathException
            {
/* 124*/        Object obj = parseRelationalExpression();
/* 127*/        while(t.currentToken == 11 || t.currentToken == 34) 
                {
/* 127*/            int i = t.currentToken;
/* 128*/            t.next();
/* 129*/            obj = new RelationalExpression(((Expression) (obj)), i, parseRelationalExpression());
/* 130*/            ((Expression) (obj)).setStaticContext(env);
                }
/* 132*/        return ((Expression) (obj));
            }

            private Expression parseRelationalExpression()
                throws XPathException
            {
/* 141*/        Object obj = parseAdditiveExpression();
/* 146*/        while(t.currentToken == 22 || t.currentToken == 21 || t.currentToken == 24 || t.currentToken == 23) 
                {
/* 146*/            int i = t.currentToken;
/* 147*/            t.next();
/* 148*/            obj = new RelationalExpression(((Expression) (obj)), i, parseAdditiveExpression());
/* 149*/            ((Expression) (obj)).setStaticContext(env);
                }
/* 151*/        return ((Expression) (obj));
            }

            private Expression parseAdditiveExpression()
                throws XPathException
            {
/* 160*/        Object obj = parseMultiplicativeExpression();
/* 163*/        while(t.currentToken == 25 || t.currentToken == 26) 
                {
/* 163*/            int i = t.currentToken;
/* 164*/            t.next();
/* 165*/            obj = new ArithmeticExpression(((Expression) (obj)), i, parseMultiplicativeExpression());
/* 166*/            ((Expression) (obj)).setStaticContext(env);
                }
/* 168*/        return ((Expression) (obj));
            }

            private Expression parseMultiplicativeExpression()
                throws XPathException
            {
/* 177*/        Object obj = parseUnaryExpression();
/* 181*/        while(t.currentToken == 27 || t.currentToken == 28 || t.currentToken == 29) 
                {
/* 181*/            int i = t.currentToken;
/* 182*/            t.next();
/* 183*/            obj = new ArithmeticExpression(((Expression) (obj)), i, parseUnaryExpression());
/* 184*/            ((Expression) (obj)).setStaticContext(env);
                }
/* 186*/        return ((Expression) (obj));
            }

            private Expression parseUnaryExpression()
                throws XPathException
            {
                Object obj;
/* 196*/        if(t.currentToken == 26)
                {
/* 197*/            t.next();
/* 198*/            obj = new ArithmeticExpression(new NumericValue(0.0D), 99, parseUnaryExpression());
/* 201*/            ((Expression) (obj)).setStaticContext(env);
                } else
                {
/* 204*/            obj = parseUnionExpression();
                }
/* 206*/        return ((Expression) (obj));
            }

            private Expression parseUnionExpression()
                throws XPathException
            {
/* 216*/        Object obj = parsePathExpression();
/* 218*/        while(t.currentToken == 4) 
                {
/* 218*/            t.next();
/* 219*/            obj = new UnionExpression(((Expression) (obj)), parsePathExpression());
/* 220*/            ((Expression) (obj)).setStaticContext(env);
                }
/* 222*/        return ((Expression) (obj));
            }

            private Expression parsePathExpression()
                throws XPathException
            {
/* 234*/        switch(t.currentToken)
                {
/* 236*/        case 5: // '\005'
/* 236*/            t.next();
/* 237*/            switch(t.currentToken)
                    {
/* 247*/            case 1: // '\001'
/* 247*/            case 6: // '\006'
/* 247*/            case 12: // '\f'
/* 247*/            case 13: // '\r'
/* 247*/            case 14: // '\016'
/* 247*/            case 17: // '\021'
/* 247*/            case 32: // ' '
/* 247*/            case 33: // '!'
/* 247*/                return parseRelativePath(new RootExpression());
                    }
/* 249*/            return new RootExpression();

/* 254*/        case 16: // '\020'
/* 254*/            return parsePathContinuation(new RootExpression());

/* 257*/        case 12: // '\f'
/* 257*/            t.next();
/* 258*/            return parsePathContinuation(new ContextNodeExpression());

/* 261*/        case 13: // '\r'
/* 261*/            t.next();
/* 262*/            return parsePathContinuation(new ParentNodeExpression());

/* 270*/        case 1: // '\001'
/* 270*/        case 6: // '\006'
/* 270*/        case 14: // '\016'
/* 270*/        case 17: // '\021'
/* 270*/        case 32: // ' '
/* 270*/        case 33: // '!'
/* 270*/            return parseRelativePath(new ContextNodeExpression());

/* 273*/        case 2: // '\002'
/* 273*/        case 3: // '\003'
/* 273*/        case 4: // '\004'
/* 273*/        case 7: // '\007'
/* 273*/        case 8: // '\b'
/* 273*/        case 9: // '\t'
/* 273*/        case 10: // '\n'
/* 273*/        case 11: // '\013'
/* 273*/        case 15: // '\017'
/* 273*/        case 18: // '\022'
/* 273*/        case 19: // '\023'
/* 273*/        case 20: // '\024'
/* 273*/        case 21: // '\025'
/* 273*/        case 22: // '\026'
/* 273*/        case 23: // '\027'
/* 273*/        case 24: // '\030'
/* 273*/        case 25: // '\031'
/* 273*/        case 26: // '\032'
/* 273*/        case 27: // '\033'
/* 273*/        case 28: // '\034'
/* 273*/        case 29: // '\035'
/* 273*/        case 30: // '\036'
/* 273*/        case 31: // '\037'
/* 273*/        default:
/* 273*/            Expression expression = parseFilterExpression();
/* 274*/            Expression expression1 = parsePathContinuation(expression);
/* 275*/            expression1.setStaticContext(env);
/* 276*/            return expression1;
                }
            }

            private Expression parseFilterExpression()
                throws XPathException
            {
/* 285*/        Object obj = parsePrimaryExpression();
/* 287*/        while(t.currentToken == 7) 
                {
/* 287*/            t.next();
/* 288*/            Expression expression = parseExpression();
/* 289*/            expect(8);
/* 290*/            obj = new FilterExpression(((Expression) (obj)), expression);
/* 291*/            ((Expression) (obj)).setStaticContext(env);
/* 292*/            t.next();
                }
/* 294*/        return ((Expression) (obj));
            }

            private Expression parsePrimaryExpression()
                throws XPathException
            {
/* 307*/        switch(t.currentToken)
                {
/* 309*/        case 31: // '\037'
/* 309*/            t.next();
/* 310*/            expect(1);
/* 311*/            String s = t.currentTokenValue;
/* 312*/            t.next();
/* 314*/            int i = env.makeNameCode(s, false) & 0xfffff;
/* 315*/            return new VariableReference(i, env);

/* 318*/        case 9: // '\t'
/* 318*/            t.next();
/* 319*/            Expression expression = parseExpression();
/* 320*/            expect(10);
/* 321*/            t.next();
/* 322*/            return expression;

/* 325*/        case 3: // '\003'
/* 325*/            StringValue stringvalue = new StringValue(t.currentTokenValue);
/* 326*/            t.next();
/* 327*/            return stringvalue;

/* 330*/        case 20: // '\024'
/* 330*/            NumericValue numericvalue = new NumericValue(t.currentTokenValue);
/* 331*/            t.next();
/* 332*/            return numericvalue;

/* 335*/        case 2: // '\002'
/* 335*/            return parseFunctionCall();
                }
/* 338*/        grumble("Unexpected token " + Tokenizer.tokens[t.currentToken] + " in expression");
/* 339*/        return null;
            }

            private Expression parsePathContinuation(Expression expression)
                throws XPathException
            {
/* 350*/        switch(t.currentToken)
                {
/* 352*/        case 5: // '\005'
/* 352*/            t.next();
/* 353*/            return parseRelativePath(expression);

/* 357*/        case 16: // '\020'
/* 357*/            PathExpression pathexpression = new PathExpression(expression, new Step((byte)5, AnyNodeTest.getInstance()));
/* 360*/            pathexpression.setStaticContext(env);
/* 361*/            t.next();
/* 362*/            return parseRelativePath(pathexpression);
                }
/* 365*/        return expression;
            }

            private Expression parseRelativePath(Expression expression)
                throws XPathException
            {
/* 375*/        Step step = parseStep();
/* 376*/        PathExpression pathexpression = new PathExpression(expression, step);
/* 377*/        pathexpression.setStaticContext(env);
/* 378*/        return parsePathContinuation(pathexpression);
            }

            private Step parseStep()
                throws XPathException
            {
/* 386*/        Step step = null;
/* 388*/        switch(t.currentToken)
                {
/* 391*/        case 12: // '\f'
/* 391*/            step = new Step((byte)12, AnyNodeTest.getInstance());
/* 392*/            t.next();
/* 393*/            break;

/* 396*/        case 13: // '\r'
/* 396*/            step = new Step((byte)9, AnyNodeTest.getInstance());
/* 397*/            t.next();
/* 398*/            break;

/* 401*/        case 1: // '\001'
/* 401*/            step = new Step((byte)3, env.makeNameTest((short)1, t.currentTokenValue, false));
/* 404*/            t.next();
/* 406*/            while(t.currentToken == 7) 
/* 406*/                step = parseStepPredicate(step);
/* 408*/            break;

/* 411*/        case 17: // '\021'
/* 411*/            com.icl.saxon.pattern.NamespaceTest namespacetest = env.makeNamespaceTest((short)1, t.currentTokenValue);
/* 414*/            step = new Step((byte)3, namespacetest);
/* 415*/            t.next();
/* 417*/            while(t.currentToken == 7) 
/* 417*/                step = parseStepPredicate(step);
/* 419*/            break;

/* 422*/        case 14: // '\016'
/* 422*/            step = new Step((byte)3, new NodeTypeTest((short)1));
/* 423*/            t.next();
/* 425*/            while(t.currentToken == 7) 
/* 425*/                step = parseStepPredicate(step);
/* 427*/            break;

/* 430*/        case 6: // '\006'
/* 430*/            t.next();
/* 431*/            switch(t.currentToken)
                    {
/* 434*/            case 1: // '\001'
/* 434*/                step = new Step((byte)2, env.makeNameTest((short)2, t.currentTokenValue, false));
/* 437*/                t.next();
                        break;

/* 441*/            case 17: // '\021'
/* 441*/                com.icl.saxon.pattern.NamespaceTest namespacetest1 = env.makeNamespaceTest((short)2, t.currentTokenValue);
/* 444*/                step = new Step((byte)2, namespacetest1);
/* 446*/                t.next();
                        break;

/* 450*/            case 14: // '\016'
/* 450*/                step = new Step((byte)2, AnyNodeTest.getInstance());
/* 452*/                t.next();
                        break;

/* 456*/            case 32: // ' '
/* 456*/                String s = t.currentTokenValue;
/* 457*/                t.next();
/* 460*/                if(s == "text")
/* 461*/                    step = new Step((byte)2, NoNodeTest.getInstance());
/* 462*/                else
/* 462*/                if(s == "node")
/* 463*/                    step = new Step((byte)2, AnyNodeTest.getInstance());
/* 464*/                else
/* 464*/                if(s == "comment")
/* 465*/                    step = new Step((byte)2, NoNodeTest.getInstance());
/* 466*/                else
/* 466*/                if(s == "processing-instruction")
                        {
/* 468*/                    if(t.currentToken == 3)
/* 469*/                        t.next();
/* 471*/                    step = new Step((byte)2, NoNodeTest.getInstance());
                        }
/* 473*/                expect(10);
/* 474*/                t.next();
                        break;

/* 478*/            default:
/* 478*/                grumble("@ must be followed by a NameTest or NodeTest");
                        break;
                    }
/* 481*/            while(t.currentToken == 7) 
/* 481*/                step = parseStepPredicate(step);
/* 483*/            break;

/* 486*/        case 32: // ' '
/* 486*/            String s1 = t.currentTokenValue;
/* 487*/            t.next();
/* 489*/            if(s1 == "text")
/* 490*/                step = new Step((byte)3, new NodeTypeTest((short)3));
/* 491*/            else
/* 491*/            if(s1 == "node")
/* 492*/                step = new Step((byte)3, AnyNodeTest.getInstance());
/* 493*/            else
/* 493*/            if(s1 == "comment")
/* 494*/                step = new Step((byte)3, new NodeTypeTest((short)8));
/* 495*/            else
/* 495*/            if(s1 == "processing-instruction")
/* 497*/                if(t.currentToken == 3)
                        {
/* 498*/                    if(Name.isNCName(t.currentTokenValue))
/* 499*/                        step = new Step((byte)3, env.makeNameTest((short)7, t.currentTokenValue, false));
/* 504*/                    else
/* 504*/                        step = new Step((byte)3, NoNodeTest.getInstance());
/* 506*/                    t.next();
                        } else
                        {
/* 508*/                    step = new Step((byte)3, new NodeTypeTest((short)7));
                        }
/* 511*/            expect(10);
/* 512*/            t.next();
/* 515*/            while(t.currentToken == 7) 
/* 515*/                step = parseStepPredicate(step);
/* 517*/            break;

/* 520*/        case 33: // '!'
/* 520*/            byte byte0 = Axis.getAxisNumber(t.currentTokenValue);
/* 521*/            short word0 = Axis.principalNodeType[byte0];
/* 522*/            t.next();
/* 523*/            switch(t.currentToken)
                    {
/* 526*/            case 1: // '\001'
/* 526*/                step = new Step(byte0, env.makeNameTest(word0, t.currentTokenValue, false));
/* 529*/                t.next();
                        break;

/* 533*/            case 17: // '\021'
/* 533*/                com.icl.saxon.pattern.NamespaceTest namespacetest2 = env.makeNamespaceTest(word0, t.currentTokenValue);
/* 536*/                step = new Step(byte0, namespacetest2);
/* 537*/                t.next();
                        break;

/* 541*/            case 14: // '\016'
/* 541*/                step = new Step(byte0, new NodeTypeTest(word0));
/* 542*/                t.next();
                        break;

/* 546*/            case 32: // ' '
/* 546*/                String s2 = t.currentTokenValue;
/* 547*/                t.next();
/* 549*/                if(s2 == "node")
/* 550*/                    step = new Step(byte0, AnyNodeTest.getInstance());
/* 551*/                else
/* 551*/                if(s2 == "text")
/* 552*/                    step = new Step(byte0, new NodeTypeTest((short)3));
/* 553*/                else
/* 553*/                if(s2 == "comment")
/* 554*/                    step = new Step(byte0, new NodeTypeTest((short)8));
/* 555*/                else
/* 555*/                if(s2 == "processing-instruction")
                        {
/* 557*/                    if(t.currentToken == 3)
                            {
/* 558*/                        if(Name.isNCName(t.currentTokenValue))
/* 559*/                            step = new Step(byte0, env.makeNameTest((short)7, t.currentTokenValue, false));
/* 564*/                        else
/* 564*/                            step = new Step(byte0, NoNodeTest.getInstance());
/* 566*/                        t.next();
                            } else
                            {
/* 568*/                        step = new Step(byte0, new NodeTypeTest((short)7));
                            }
                        } else
                        {
/* 571*/                    grumble("Unsupported node type");
                        }
/* 574*/                expect(10);
/* 575*/                t.next();
                        break;

/* 578*/            default:
/* 578*/                grumble("Unexpected token [" + Tokenizer.tokens[t.currentToken] + "] after axis name");
                        break;
                    }
/* 581*/            while(t.currentToken == 7) 
/* 581*/                step = parseStepPredicate(step);
                    break;

/* 586*/        default:
/* 586*/            grumble("Unexpected token [" + Tokenizer.tokens[t.currentToken] + "] in path expression");
                    break;
                }
/* 589*/        return step;
            }

            private Step parseStepPredicate(Step step)
                throws XPathException
            {
/* 597*/        t.next();
/* 598*/        Expression expression = parseExpression();
/* 599*/        expect(8);
/* 600*/        t.next();
/* 601*/        return step.addFilter(expression);
            }

            private Expression parseFunctionCall()
                throws XPathException
            {
/* 611*/        String s = t.currentTokenValue;
/* 612*/        Object obj = null;
/* 614*/        int i = s.indexOf(":");
/* 615*/        if(i < 0)
                {
/* 616*/            Expression expression = makeSystemFunction(s);
/* 617*/            if(expression == null)
/* 617*/                grumble("Unknown system function: " + s);
/* 618*/            expression.setStaticContext(env);
/* 620*/            if(expression instanceof Function)
                    {
/* 621*/                obj = (Function)expression;
                    } else
                    {
/* 623*/                t.next();
/* 624*/                expect(10);
/* 625*/                t.next();
/* 626*/                return expression;
                    }
                } else
                {
/* 629*/            obj = env.getStyleSheetFunction(env.makeNameCode(s, false) & 0xfffff);
                }
/* 631*/        if(obj == null)
/* 632*/            obj = new FunctionProxy();
/* 634*/        ((Expression) (obj)).setStaticContext(env);
/* 638*/        t.next();
/* 639*/        if(t.currentToken != 10)
                {
/* 641*/            Expression expression1 = parseExpression();
/* 642*/            ((Function) (obj)).addArgument(expression1);
/* 644*/            while(t.currentToken == 15) 
                    {
/* 644*/                t.next();
/* 645*/                Expression expression2 = parseExpression();
/* 646*/                ((Function) (obj)).addArgument(expression2);
                    }
/* 648*/            expect(10);
                }
/* 650*/        t.next();
/* 651*/        if(obj instanceof FunctionProxy)
                {
/* 652*/            String s1 = env.getURIForPrefix(s.substring(0, i));
/* 653*/            String s2 = s.substring(i + 1);
/* 654*/            Class class1 = null;
/* 656*/            try
                    {
/* 656*/                class1 = env.getExternalJavaClass(s1);
                    }
/* 658*/            catch(TransformerException transformerexception)
                    {
/* 658*/                XPathException xpathexception1 = new XPathException("Failed to load external Java class for uri " + s1);
/* 660*/                return new ErrorExpression(xpathexception1);
                    }
/* 663*/            if(class1 == null)
                    {
/* 664*/                XPathException xpathexception = new XPathException("The URI " + s1 + " does not identify an external Java class");
/* 666*/                return new ErrorExpression(xpathexception);
                    }
/* 669*/            ((FunctionProxy)obj).setFunctionName(class1, s2);
                }
/* 671*/        return ((Expression) (obj));
            }

            public static Expression makeSystemFunction(String s)
            {
/* 680*/        if(s == "last")
/* 680*/            return new Last();
/* 681*/        if(s == "position")
/* 681*/            return new Position();
/* 683*/        if(s == "count")
/* 683*/            return new Count();
/* 684*/        if(s == "current")
/* 684*/            return new Current();
/* 685*/        if(s == "id")
/* 685*/            return new Id();
/* 686*/        if(s == "key")
/* 686*/            return new Key();
/* 687*/        if(s == "document")
/* 687*/            return new Document();
/* 688*/        if(s == "local-name")
/* 688*/            return new LocalName();
/* 689*/        if(s == "namespace-uri")
/* 689*/            return new NamespaceURI();
/* 690*/        if(s == "name")
/* 690*/            return new NameFn();
/* 691*/        if(s == "generate-id")
/* 691*/            return new GenerateId();
/* 693*/        if(s == "not")
/* 693*/            return new Not();
/* 694*/        if(s == "true")
/* 694*/            return new BooleanValue(true);
/* 695*/        if(s == "false")
/* 695*/            return new BooleanValue(false);
/* 696*/        if(s == "boolean")
/* 696*/            return new BooleanFn();
/* 697*/        if(s == "lang")
/* 697*/            return new Lang();
/* 699*/        if(s == "number")
/* 699*/            return new NumberFn();
/* 700*/        if(s == "floor")
/* 700*/            return new Floor();
/* 701*/        if(s == "ceiling")
/* 701*/            return new Ceiling();
/* 702*/        if(s == "round")
/* 702*/            return new Round();
/* 703*/        if(s == "sum")
/* 703*/            return new Sum();
/* 705*/        if(s == "string")
/* 705*/            return new StringFn();
/* 707*/        if(s == "starts-with")
/* 707*/            return new StartsWith();
/* 708*/        if(s == "string-length")
/* 708*/            return new StringLength();
/* 709*/        if(s == "substring")
/* 709*/            return new Substring();
/* 710*/        if(s == "contains")
/* 710*/            return new Contains();
/* 711*/        if(s == "substring-before")
/* 711*/            return new SubstringBefore();
/* 712*/        if(s == "substring-after")
/* 712*/            return new SubstringAfter();
/* 713*/        if(s == "normalize-space")
/* 713*/            return new NormalizeSpace();
/* 714*/        if(s == "translate")
/* 714*/            return new Translate();
/* 715*/        if(s == "concat")
/* 715*/            return new Concat();
/* 716*/        if(s == "format-number")
/* 716*/            return new FormatNumber();
/* 718*/        if(s == "system-property")
/* 718*/            return new SystemProperty();
/* 719*/        if(s == "function-available")
/* 719*/            return new FunctionAvailable();
/* 720*/        if(s == "element-available")
/* 720*/            return new ElementAvailable();
/* 721*/        if(s == "unparsed-entity-uri")
/* 721*/            return new UnparsedEntityURI();
/* 722*/        else
/* 722*/            return null;
            }

            private Pattern parseUnionPattern()
                throws XPathException
            {
/* 736*/        Object obj = parsePathPattern();
/* 739*/        while(t.currentToken == 4) 
                {
/* 739*/            t.next();
/* 740*/            Pattern pattern = parsePathPattern();
/* 741*/            obj = new UnionPattern(((Pattern) (obj)), pattern);
/* 742*/            ((Pattern) (obj)).setStaticContext(env);
/* 743*/            pattern.setStaticContext(env);
                }
/* 746*/        return ((Pattern) (obj));
            }

            private Pattern parsePathPattern()
                throws XPathException
            {
/* 755*/        LocationPathPattern locationpathpattern = new LocationPathPattern();
/* 756*/        locationpathpattern.setStaticContext(env);
/* 757*/        Object obj = locationpathpattern;
/* 758*/        Object obj1 = null;
/* 759*/        int i = -1;
/* 760*/        boolean flag = false;
/* 764*/        switch(t.currentToken)
                {
/* 766*/        case 5: // '\005'
/* 766*/            i = t.currentToken;
/* 767*/            t.next();
/* 768*/            obj1 = new NodeTypeTest((short)9);
/* 769*/            flag = true;
                    break;

/* 773*/        case 16: // '\020'
/* 773*/            i = t.currentToken;
/* 774*/            t.next();
/* 775*/            obj1 = new NodeTypeTest((short)9);
/* 776*/            flag = false;
                    break;
                }
/* 782*/        boolean flag1 = true;
/* 785*/        while(flag1) 
                {
/* 785*/            switch(t.currentToken)
                    {
/* 787*/            case 33: // '!'
/* 787*/                if(t.currentTokenValue.equals("child"))
                        {
/* 788*/                    t.next();
/* 789*/                    obj = patternStep(0, locationpathpattern, ((Pattern) (obj1)), i);
/* 789*/                    break;
                        }
/* 790*/                if(t.currentTokenValue.equals("attribute"))
                        {
/* 791*/                    t.next();
/* 792*/                    obj = patternStep(1, locationpathpattern, ((Pattern) (obj1)), i);
                        } else
                        {
/* 794*/                    grumble("Axis in pattern must be child or attribute");
                        }
/* 796*/                break;

/* 802*/            case 1: // '\001'
/* 802*/            case 14: // '\016'
/* 802*/            case 17: // '\021'
/* 802*/            case 32: // ' '
/* 802*/                obj = patternStep(0, locationpathpattern, ((Pattern) (obj1)), i);
/* 803*/                break;

/* 806*/            case 6: // '\006'
/* 806*/                t.next();
/* 807*/                obj = patternStep(1, locationpathpattern, ((Pattern) (obj1)), i);
/* 808*/                break;

/* 811*/            case 2: // '\002'
/* 811*/                if(obj1 != null)
/* 812*/                    grumble("Function may appear only at the start of a pattern");
/* 814*/                if(t.currentTokenValue.equals("id"))
                        {
/* 815*/                    t.next();
/* 816*/                    expect(3);
/* 817*/                    obj = new IDPattern(t.currentTokenValue);
/* 818*/                    ((Pattern) (obj)).setStaticContext(env);
/* 819*/                    t.next();
/* 820*/                    expect(10);
/* 821*/                    t.next();
/* 821*/                    break;
                        }
/* 822*/                if(t.currentTokenValue.equals("key"))
                        {
/* 823*/                    t.next();
/* 824*/                    expect(3);
/* 825*/                    String s = t.currentTokenValue;
/* 826*/                    t.next();
/* 827*/                    expect(15);
/* 828*/                    t.next();
/* 829*/                    expect(3);
/* 830*/                    if(!env.allowsKeyFunction())
/* 831*/                        grumble("key() function cannot be used here");
/* 833*/                    obj = new KeyPattern(env.makeNameCode(s, false), t.currentTokenValue);
/* 835*/                    ((Pattern) (obj)).setStaticContext(env);
/* 836*/                    t.next();
/* 837*/                    expect(10);
/* 838*/                    t.next();
                        } else
                        {
/* 840*/                    grumble("The only functions allowed in a pattern are id() and key()");
                        }
/* 842*/                break;

/* 845*/            default:
/* 845*/                if(flag)
/* 845*/                    return ((Pattern) (obj1));
/* 846*/                grumble("Unexpected token in pattern, found " + Tokenizer.tokens[t.currentToken]);
                        break;
                    }
/* 849*/            i = t.currentToken;
/* 850*/            flag = false;
/* 851*/            flag1 = i == 5 || i == 16;
/* 852*/            if(flag1)
                    {
/* 853*/                obj1 = obj;
/* 854*/                locationpathpattern = new LocationPathPattern();
/* 855*/                locationpathpattern.setStaticContext(env);
/* 856*/                if(i == 5)
/* 857*/                    locationpathpattern.parentPattern = ((Pattern) (obj1));
/* 859*/                else
/* 859*/                    locationpathpattern.ancestorPattern = ((Pattern) (obj1));
/* 861*/                t.next();
                    }
                }
/* 864*/        ((Pattern) (obj)).setStaticContext(env);
/* 865*/        return ((Pattern) (obj));
            }

            private Pattern patternStep(int i, LocationPathPattern locationpathpattern, Pattern pattern, int j)
                throws XPathException
            {
/* 872*/        if(i == 0)
                {
/* 873*/            if(t.currentToken == 14)
/* 874*/                locationpathpattern.nodeTest = new NodeTypeTest((short)1);
/* 875*/            else
/* 875*/            if(t.currentToken == 1)
/* 876*/                locationpathpattern.nodeTest = env.makeNameTest((short)1, t.currentTokenValue, false);
/* 878*/            else
/* 878*/            if(t.currentToken == 17)
/* 879*/                locationpathpattern.nodeTest = env.makeNamespaceTest((short)1, t.currentTokenValue);
/* 881*/            else
/* 881*/            if(t.currentToken == 32)
                    {
/* 882*/                String s = t.currentTokenValue;
/* 883*/                t.next();
/* 884*/                if(s == "text")
/* 885*/                    locationpathpattern.nodeTest = new NodeTypeTest((short)3);
/* 886*/                else
/* 886*/                if(s == "node")
/* 887*/                    locationpathpattern.nodeTest = new AnyChildNodePattern();
/* 888*/                else
/* 888*/                if(s == "comment")
/* 889*/                    locationpathpattern.nodeTest = new NodeTypeTest((short)8);
/* 890*/                else
/* 890*/                if(s == "processing-instruction")
/* 892*/                    if(t.currentToken == 3)
                            {
/* 893*/                        if(Name.isNCName(t.currentTokenValue))
/* 894*/                            locationpathpattern.nodeTest = env.makeNameTest((short)7, t.currentTokenValue, false);
/* 898*/                        else
/* 898*/                            locationpathpattern.nodeTest = NoNodeTest.getInstance();
/* 900*/                        t.next();
                            } else
                            {
/* 902*/                        locationpathpattern.nodeTest = new NodeTypeTest((short)7);
                            }
/* 905*/                expect(10);
                    } else
                    {
/* 907*/                grumble("Unexpected token in pattern, found " + Tokenizer.tokens[t.currentToken]);
                    }
/* 910*/            if(pattern != null)
/* 911*/                if(j == 5)
/* 912*/                    locationpathpattern.parentPattern = pattern;
/* 914*/                else
/* 914*/                    locationpathpattern.ancestorPattern = pattern;
/* 917*/            t.next();
/* 918*/            parseFilters(locationpathpattern);
/* 919*/            return locationpathpattern;
                }
/* 921*/        if(i == 1)
                {
/* 923*/            if(t.currentToken == 14)
/* 924*/                locationpathpattern.nodeTest = new NodeTypeTest((short)2);
/* 925*/            else
/* 925*/            if(t.currentToken == 1)
/* 926*/                locationpathpattern.nodeTest = env.makeNameTest((short)2, t.currentTokenValue, false);
/* 928*/            else
/* 928*/            if(t.currentToken == 17)
/* 929*/                locationpathpattern.nodeTest = env.makeNamespaceTest((short)2, t.currentTokenValue);
/* 931*/            else
/* 931*/            if(t.currentToken == 32)
                    {
/* 932*/                String s1 = t.currentTokenValue;
/* 933*/                t.next();
/* 936*/                if(s1 == "text")
/* 937*/                    locationpathpattern.nodeTest = NoNodeTest.getInstance();
/* 938*/                else
/* 938*/                if(s1 == "node")
/* 939*/                    locationpathpattern.nodeTest = new NodeTypeTest((short)2);
/* 940*/                else
/* 940*/                if(s1 == "comment")
/* 941*/                    locationpathpattern.nodeTest = NoNodeTest.getInstance();
/* 942*/                else
/* 942*/                if(s1 == "processing-instruction")
                        {
/* 944*/                    locationpathpattern.nodeTest = NoNodeTest.getInstance();
/* 945*/                    if(t.currentToken == 3)
/* 947*/                        t.next();
                        }
/* 950*/                expect(10);
                    } else
                    {
/* 953*/                grumble("@ in pattern not followed by NameTest or NodeTest");
                    }
/* 955*/            t.next();
/* 956*/            parseFilters(locationpathpattern);
/* 957*/            return locationpathpattern;
                } else
                {
/* 960*/            grumble("Axis in pattern must be child or attribute");
/* 961*/            return null;
                }
            }

            private void parseFilters(LocationPathPattern locationpathpattern)
                throws XPathException
            {
/* 970*/        while(t.currentToken == 7) 
                {
/* 971*/            t.next();
/* 972*/            Expression expression = parseExpression();
/* 973*/            expect(8);
/* 974*/            t.next();
/* 975*/            locationpathpattern.addFilter(expression);
/* 976*/            if(expression.usesCurrent())
/* 977*/                grumble("The current() function may not be used in a pattern");
                }
            }
}
