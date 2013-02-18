// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   FunctionProxy.java

package com.icl.saxon.expr;

import com.icl.saxon.Context;
import com.icl.saxon.Controller;
import com.icl.saxon.om.NodeEnumeration;
import com.icl.saxon.om.NodeInfo;
import java.lang.reflect.*;
import java.util.Vector;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Referenced classes of package com.icl.saxon.expr:
//            Function, XPathException, Value, BooleanValue, 
//            NumericValue, StringValue, EmptyNodeSet, Expression, 
//            NodeSetExtent, ObjectValue, SingletonNodeSet

public class FunctionProxy extends Function
{

            private Class theClass;
            private Vector candidateMethods;
            private XPathException theException;
            private String name;
            private Class resultClass;

            public FunctionProxy()
            {
/*  26*/        candidateMethods = new Vector();
/*  27*/        theException = null;
/*  29*/        resultClass = null;
            }

            public boolean setFunctionName(Class class1, String s)
            {
/*  54*/        boolean flag = false;
/*  57*/        name = s;
/*  58*/        int i = getNumberOfArguments();
/*  59*/        int j = i;
/*  61*/        theClass = class1;
/*  65*/        if(name.equals("new"))
                {
/*  67*/            int k = theClass.getModifiers();
/*  68*/            if(Modifier.isAbstract(k))
                    {
/*  69*/                theException = new XPathException("Class " + theClass + " is abstract");
/*  70*/                return false;
                    }
/*  72*/            if(Modifier.isInterface(k))
                    {
/*  73*/                theException = new XPathException(theClass + " is an interface");
/*  74*/                return false;
                    }
/*  76*/            if(Modifier.isPrivate(k))
                    {
/*  77*/                theException = new XPathException("Class " + theClass + " is private");
/*  78*/                return false;
                    }
/*  80*/            if(Modifier.isProtected(k))
                    {
/*  81*/                theException = new XPathException("Class " + theClass + " is protected");
/*  82*/                return false;
                    }
/*  85*/            resultClass = theClass;
/*  86*/            Constructor aconstructor[] = theClass.getConstructors();
/*  87*/            for(int l = 0; l < aconstructor.length; l++)
                    {
/*  88*/                flag = true;
/*  89*/                Constructor constructor = aconstructor[l];
/*  90*/                if(constructor.getParameterTypes().length == i)
                        {
/*  91*/                    flag = true;
/*  92*/                    candidateMethods.addElement(constructor);
                        }
                    }

/*  95*/            if(flag)
                    {
/*  96*/                return true;
                    } else
                    {
/*  98*/                theException = new XPathException("No constructor with " + i + (i != 1 ? " parameters" : " parameter") + " found in class " + theClass.getName());
/* 101*/                return false;
                    }
                }
/* 107*/        StringBuffer stringbuffer = new StringBuffer();
/* 108*/        boolean flag1 = false;
/* 109*/        for(int i1 = 0; i1 < name.length(); i1++)
                {
/* 110*/            char c = name.charAt(i1);
/* 111*/            if(c == '-')
                    {
/* 112*/                flag1 = true;
                    } else
                    {
/* 114*/                if(flag1)
/* 115*/                    stringbuffer.append(Character.toUpperCase(c));
/* 117*/                else
/* 117*/                    stringbuffer.append(c);
/* 119*/                flag1 = false;
                    }
                }

/* 123*/        name = stringbuffer.toString();
/* 127*/        if(name.equals("if"))
/* 128*/            name = "IF";
/* 133*/        Method amethod[] = theClass.getMethods();
/* 134*/        boolean flag2 = true;
/* 135*/        for(int j1 = 0; j1 < amethod.length; j1++)
                {
/* 137*/            Method method = amethod[j1];
/* 139*/            if(method.getName().equals(name) && Modifier.isPublic(method.getModifiers()))
                    {
/* 141*/                flag = true;
/* 142*/                if(flag2)
/* 143*/                    if(resultClass == null)
/* 144*/                        resultClass = method.getReturnType();
/* 146*/                    else
/* 146*/                        flag2 = method.getReturnType() == resultClass;
/* 150*/                Class aclass[] = method.getParameterTypes();
/* 151*/                boolean flag3 = Modifier.isStatic(method.getModifiers());
/* 156*/                j = flag3 ? i : i - 1;
/* 158*/                if(j >= 0)
                        {
/* 163*/                    if(aclass.length == j && (j == 0 || aclass[0] != (com.icl.saxon.Context.class)))
                            {
/* 167*/                        flag = true;
/* 168*/                        candidateMethods.addElement(method);
                            }
/* 173*/                    if(aclass.length == j + 1 && (aclass[0] == (com.icl.saxon.Context.class) || aclass[0] == (org.w3c.xsl.XSLTContext.class)))
                            {
/* 176*/                        flag = true;
/* 177*/                        candidateMethods.addElement(method);
                            }
                        }
                    }
                }

/* 183*/        if(!flag2)
/* 184*/            resultClass = null;
/* 189*/        if(flag)
                {
/* 190*/            return true;
                } else
                {
/* 192*/            theException = new XPathException("No method matching " + name + " with " + j + (j != 1 ? " parameters" : " parameter") + " found in class " + theClass.getName());
/* 196*/            return false;
                }
            }

            public int getDataType()
            {
/* 208*/        if(resultClass == null || resultClass == (com.icl.saxon.expr.Value.class))
/* 209*/            return -1;
/* 210*/        if(resultClass.toString().equals("void"))
/* 211*/            return 4;
/* 212*/        if(resultClass == (java.lang.String.class) || resultClass == (com.icl.saxon.expr.StringValue.class))
/* 213*/            return 3;
/* 214*/        if(resultClass == (java.lang.Boolean.class) || resultClass == Boolean.TYPE || resultClass == (com.icl.saxon.expr.BooleanValue.class))
/* 216*/            return 1;
/* 217*/        if(resultClass == (java.lang.Double.class) || resultClass == Double.TYPE || resultClass == (java.lang.Float.class) || resultClass == Float.TYPE || resultClass == (java.lang.Long.class) || resultClass == Long.TYPE || resultClass == (java.lang.Integer.class) || resultClass == Integer.TYPE || resultClass == (java.lang.Short.class) || resultClass == Short.TYPE || resultClass == (java.lang.Byte.class) || resultClass == Byte.TYPE || resultClass == (com.icl.saxon.expr.NumericValue.class))
/* 224*/            return 2;
/* 225*/        return !(com.icl.saxon.expr.NodeSetValue.class).isAssignableFrom(resultClass) && !(com.icl.saxon.om.NodeEnumeration.class).isAssignableFrom(resultClass) && !(org.w3c.dom.NodeList.class).isAssignableFrom(resultClass) && !(org.w3c.dom.Node.class).isAssignableFrom(resultClass) ? 6 : 4;
            }

            public String getName()
            {
/* 240*/        return name;
            }

            public Expression simplify()
                throws XPathException
            {
/* 248*/        for(int i = 0; i < getNumberOfArguments(); i++)
/* 249*/            super.argument[i] = super.argument[i].simplify();

/* 255*/        if(candidateMethods.size() > 1)
                {
/* 256*/            boolean flag = true;
/* 257*/            for(int j = 0; j < getNumberOfArguments(); j++)
                    {
/* 258*/                int k = super.argument[j].getDataType();
/* 259*/                if(k != -1 && k != 6)
/* 260*/                    continue;
/* 260*/                flag = false;
/* 261*/                break;
                    }

/* 264*/            if(flag)
                    {
/* 266*/                Value avalue[] = new Value[getNumberOfArguments()];
/* 267*/                for(int l = 0; l < getNumberOfArguments(); l++)
/* 268*/                    switch(super.argument[l].getDataType())
                            {
/* 270*/                    case 1: // '\001'
/* 270*/                        avalue[l] = new BooleanValue(true);
                                break;

/* 273*/                    case 2: // '\002'
/* 273*/                        avalue[l] = new NumericValue(1.0D);
                                break;

/* 276*/                    case 3: // '\003'
/* 276*/                        avalue[l] = new StringValue("");
                                break;

/* 279*/                    case 4: // '\004'
/* 279*/                        avalue[l] = new EmptyNodeSet();
                                break;
                            }

/* 284*/                try
                        {
/* 284*/                    Object obj = getBestFit(avalue);
/* 285*/                    candidateMethods = new Vector();
/* 286*/                    candidateMethods.addElement(obj);
                        }
/* 288*/                catch(XPathException xpathexception)
                        {
/* 288*/                    theException = xpathexception;
                        }
                    }
                }
/* 292*/        return this;
            }

            public int getDependencies()
            {
/* 302*/        int i = 0;
/* 304*/        i = 56;
/* 306*/        for(int j = 0; j < getNumberOfArguments(); j++)
/* 307*/            i |= super.argument[j].getDependencies();

/* 309*/        return i;
            }

            public Expression reduce(int i, Context context)
                throws XPathException
            {
/* 324*/        if((i & 0x38) != 0)
/* 327*/            return evaluate(context);
/* 331*/        FunctionProxy functionproxy = new FunctionProxy();
/* 332*/        functionproxy.theClass = theClass;
/* 333*/        functionproxy.candidateMethods = candidateMethods;
/* 334*/        functionproxy.theException = theException;
/* 335*/        functionproxy.name = name;
/* 336*/        functionproxy.argument = new Expression[getNumberOfArguments()];
/* 337*/        for(int j = 0; j < getNumberOfArguments(); j++)
/* 338*/            functionproxy.addArgument(super.argument[j].reduce(i, context));

/* 340*/        return functionproxy;
            }

            public Object getBestFit(Value avalue[])
                throws XPathException
            {
/* 353*/        if(candidateMethods.size() == 1)
/* 355*/            return candidateMethods.elementAt(0);
/* 361*/        int i = candidateMethods.size();
/* 362*/        boolean aflag[] = new boolean[i];
/* 363*/        for(int j = 0; j < i; j++)
/* 364*/            aflag[j] = false;

/* 367*/        for(int k = 0; k < i - 1; k++)
                {
/* 368*/            int ai[] = getConversionPreferences(avalue, candidateMethods.elementAt(k));
/* 371*/            if(!aflag[k])
                    {
/* 372*/                for(int i1 = k + 1; i1 < i; i1++)
/* 373*/                    if(!aflag[i1])
                            {
/* 374*/                        int ai1[] = getConversionPreferences(avalue, candidateMethods.elementAt(i1));
/* 377*/                        for(int k1 = 0; k1 < ai1.length; k1++)
                                {
/* 378*/                            if(ai[k1] > ai1[k1])
/* 379*/                                aflag[k] = true;
/* 381*/                            if(ai[k1] < ai1[k1])
/* 382*/                                aflag[i1] = true;
                                }

                            }

                    }
                }

/* 390*/        int l = 0;
/* 391*/        Object obj = null;
/* 392*/        for(int j1 = 0; j1 < i; j1++)
/* 393*/            if(!aflag[j1])
                    {
/* 394*/                obj = candidateMethods.elementAt(j1);
/* 395*/                l++;
                    }

/* 399*/        if(l == 0)
/* 400*/            throw new XPathException("There is no Java method that is a unique best match");
/* 403*/        if(l > 1)
/* 404*/            throw new XPathException("There are several Java methods that match equally well");
/* 407*/        else
/* 407*/            return obj;
            }

            public Value evaluate(Context context)
                throws XPathException
            {
/* 419*/        Object obj = call(context);
/* 420*/        return convertJavaObjectToXPath(obj, context.getController());
            }

            public String evaluateAsString(Context context)
                throws XPathException
            {
/* 424*/        if(resultClass == (java.lang.String.class))
/* 425*/            return (String)call(context);
/* 426*/        if(resultClass == (com.icl.saxon.om.NodeEnumeration.class))
                {
/* 427*/            NodeEnumeration nodeenumeration = enumerate(context, true);
/* 428*/            if(nodeenumeration.hasMoreElements())
/* 429*/                return nodeenumeration.nextElement().getStringValue();
/* 431*/            else
/* 431*/                return "";
                } else
                {
/* 434*/            return evaluate(context).asString();
                }
            }

            public double evaluateAsNumber(Context context)
                throws XPathException
            {
/* 439*/        if(resultClass == Double.TYPE)
/* 440*/            return ((Double)call(context)).doubleValue();
/* 441*/        if(resultClass == (com.icl.saxon.om.NodeEnumeration.class))
                {
/* 442*/            NodeEnumeration nodeenumeration = enumerate(context, true);
/* 443*/            if(nodeenumeration.hasMoreElements())
/* 444*/                return Value.stringToNumber(nodeenumeration.nextElement().getStringValue());
/* 446*/            else
/* 446*/                return (0.0D / 0.0D);
                } else
                {
/* 449*/            return evaluate(context).asNumber();
                }
            }

            public boolean evaluateAsBoolean(Context context)
                throws XPathException
            {
/* 454*/        if(resultClass == Boolean.TYPE)
/* 455*/            return ((Boolean)call(context)).booleanValue();
/* 456*/        if(resultClass == (com.icl.saxon.om.NodeEnumeration.class))
                {
/* 457*/            NodeEnumeration nodeenumeration = enumerate(context, false);
/* 458*/            return nodeenumeration.hasMoreElements();
                } else
                {
/* 460*/            return evaluate(context).asBoolean();
                }
            }

            public NodeEnumeration enumerate(Context context, boolean flag)
                throws XPathException
            {
/* 465*/        if(resultClass == (com.icl.saxon.om.NodeEnumeration.class))
                {
/* 466*/            NodeEnumeration nodeenumeration = (NodeEnumeration)call(context);
/* 467*/            if(flag && !nodeenumeration.isSorted())
                    {
/* 468*/                NodeSetExtent nodesetextent = new NodeSetExtent(nodeenumeration, context.getController());
/* 469*/                nodesetextent.sort();
/* 470*/                return nodesetextent.enumerate();
                    } else
                    {
/* 472*/                return nodeenumeration;
                    }
                } else
                {
/* 475*/            return super.enumerate(context, flag);
                }
            }

            private Object call(Context context)
                throws XPathException
            {
/* 487*/        if(theException != null)
/* 488*/            throw theException;
/* 490*/        context.setException(null);
/* 492*/        Value avalue[] = new Value[getNumberOfArguments()];
/* 493*/        for(int i = 0; i < getNumberOfArguments(); i++)
/* 494*/            avalue[i] = super.argument[i].evaluate(context);

/* 499*/        Object obj = getBestFit(avalue);
/* 506*/        if(obj instanceof Constructor)
                {
/* 507*/            Constructor constructor = (Constructor)obj;
/* 508*/            Class aclass[] = constructor.getParameterTypes();
/* 509*/            Object aobj[] = new Object[aclass.length];
/* 511*/            setupParams(avalue, aobj, aclass, 0, 0);
/* 514*/            try
                    {
/* 514*/                Object obj1 = constructor.newInstance(aobj);
/* 515*/                if(context.getException() != null)
/* 516*/                    throw context.getException();
/* 518*/                else
/* 518*/                    return obj1;
                    }
/* 521*/            catch(InstantiationException instantiationexception)
                    {
/* 521*/                throw new XPathException("Cannot instantiate class", instantiationexception);
                    }
/* 523*/            catch(IllegalAccessException illegalaccessexception)
                    {
/* 523*/                throw new XPathException("Constructor access is illegal", illegalaccessexception);
                    }
/* 525*/            catch(IllegalArgumentException illegalargumentexception)
                    {
/* 525*/                throw new XPathException("Argument is of wrong type", illegalargumentexception);
                    }
/* 527*/            catch(InvocationTargetException invocationtargetexception)
                    {
/* 527*/                Throwable throwable = invocationtargetexception.getTargetException();
/* 528*/                if(throwable instanceof XPathException)
/* 529*/                    throw (XPathException)throwable;
/* 531*/                if(context.getController().isTracing())
/* 532*/                    invocationtargetexception.getTargetException().printStackTrace();
/* 534*/                throw new XPathException("Exception in extension function " + invocationtargetexception.getTargetException().toString());
                    }
                }
/* 539*/        Method method = (Method)obj;
/* 540*/        boolean flag = Modifier.isStatic(method.getModifiers());
/* 542*/        Class aclass1[] = method.getParameterTypes();
/* 543*/        boolean flag1 = aclass1.length > 0 && (aclass1[0] == (com.icl.saxon.Context.class) || aclass1[0] == (org.w3c.xsl.XSLTContext.class));
                Object obj2;
/* 546*/        if(flag)
                {
/* 547*/            obj2 = null;
                } else
                {
/* 549*/            int j = getNumberOfArguments();
/* 550*/            if(j == 0)
/* 551*/                throw new XPathException("Must supply an argument for an instance-level extension function");
/* 553*/            Value value = super.argument[0].evaluate(context);
/* 554*/            if(value instanceof ObjectValue)
/* 556*/                obj2 = ((ObjectValue)value).getObject();
/* 557*/            else
/* 557*/            if(theClass == (java.lang.String.class))
/* 558*/                obj2 = value.asString();
/* 559*/            else
/* 559*/            if(theClass == (java.lang.Boolean.class))
/* 560*/                obj2 = new Boolean(value.asBoolean());
/* 561*/            else
/* 561*/            if(theClass == (java.lang.Double.class))
/* 562*/                obj2 = new Double(value.asNumber());
/* 564*/            else
/* 564*/                throw new XPathException("First argument is not an object instance");
                }
/* 569*/        int k = (aclass1.length - (flag1 ? 1 : 0)) + (flag ? 0 : 1);
/* 573*/        checkArgumentCount(k, k);
/* 574*/        Object aobj1[] = new Object[aclass1.length];
/* 576*/        if(flag1)
/* 577*/            aobj1[0] = context;
/* 580*/        setupParams(avalue, aobj1, aclass1, flag1 ? 1 : 0, flag ? 0 : 1);
/* 586*/        try
                {
/* 586*/            Object obj3 = method.invoke(obj2, aobj1);
/* 587*/            if(context.getException() != null)
/* 588*/                throw context.getException();
/* 590*/            if(method.getReturnType().toString().equals("void"))
/* 593*/                return new EmptyNodeSet();
/* 595*/            else
/* 595*/                return obj3;
                }
/* 598*/        catch(IllegalAccessException illegalaccessexception1)
                {
/* 598*/            throw new XPathException("Method access is illegal", illegalaccessexception1);
                }
/* 600*/        catch(IllegalArgumentException illegalargumentexception1)
                {
/* 600*/            throw new XPathException("Argument is of wrong type", illegalargumentexception1);
                }
/* 602*/        catch(InvocationTargetException invocationtargetexception1)
                {
/* 602*/            Throwable throwable1 = invocationtargetexception1.getTargetException();
/* 603*/            if(throwable1 instanceof XPathException)
/* 604*/                throw (XPathException)throwable1;
/* 606*/            if(context.getController().isTracing())
/* 607*/                invocationtargetexception1.getTargetException().printStackTrace();
/* 609*/            throw new XPathException("Exception in extension function " + invocationtargetexception1.getTargetException().toString());
                }
            }

            public static Value convertJavaObjectToXPath(Object obj, Controller controller)
                throws XPathException
            {
/* 624*/        if(obj == null)
/* 625*/            return new ObjectValue(null);
/* 627*/        if(obj instanceof String)
/* 628*/            return new StringValue((String)obj);
/* 630*/        if(obj instanceof Boolean)
/* 631*/            return new BooleanValue(((Boolean)obj).booleanValue());
/* 633*/        if(obj instanceof Double)
/* 634*/            return new NumericValue(((Double)obj).doubleValue());
/* 635*/        if(obj instanceof Float)
/* 636*/            return new NumericValue(((Float)obj).floatValue());
/* 637*/        if(obj instanceof Short)
/* 638*/            return new NumericValue(((Short)obj).shortValue());
/* 639*/        if(obj instanceof Integer)
/* 640*/            return new NumericValue(((Integer)obj).intValue());
/* 641*/        if(obj instanceof Long)
/* 642*/            return new NumericValue(((Long)obj).longValue());
/* 643*/        if(obj instanceof Character)
/* 644*/            return new NumericValue(((Character)obj).charValue());
/* 645*/        if(obj instanceof Byte)
/* 646*/            return new NumericValue(((Byte)obj).byteValue());
/* 648*/        if(obj instanceof Value)
/* 649*/            return (Value)obj;
/* 651*/        if(obj instanceof NodeInfo)
/* 652*/            return new SingletonNodeSet((NodeInfo)obj);
/* 653*/        if(obj instanceof NodeEnumeration)
/* 656*/            return new NodeSetExtent((NodeEnumeration)obj, controller);
/* 658*/        if(obj instanceof NodeList)
                {
/* 659*/            NodeList nodelist = (NodeList)obj;
/* 660*/            NodeInfo anodeinfo[] = new NodeInfo[nodelist.getLength()];
/* 661*/            for(int i = 0; i < nodelist.getLength(); i++)
/* 662*/                if(nodelist.item(i) instanceof NodeInfo)
/* 663*/                    anodeinfo[i] = (NodeInfo)nodelist.item(i);
/* 665*/                else
/* 665*/                    throw new XPathException("Supplied NodeList contains non-Saxon DOM Nodes");

/* 669*/            return new NodeSetExtent(anodeinfo, controller);
                }
/* 670*/        if(obj instanceof Node)
/* 671*/            throw new XPathException("Result is a non-Saxon DOM Node");
/* 673*/        else
/* 673*/            return new ObjectValue(obj);
            }

            private int[] getConversionPreferences(Value avalue[], Object obj)
            {
                Class aclass[];
                int i;
/* 691*/        if(obj instanceof Constructor)
                {
/* 692*/            i = 0;
/* 693*/            aclass = ((Constructor)obj).getParameterTypes();
                } else
                {
/* 695*/            boolean flag = Modifier.isStatic(((Method)obj).getModifiers());
/* 696*/            i = flag ? 0 : 1;
/* 697*/            aclass = ((Method)obj).getParameterTypes();
                }
/* 700*/        int ai[] = new int[avalue.length];
/* 701*/        if(i == 1)
/* 702*/            ai[0] = (avalue[0] instanceof ObjectValue) ? 0 : 20;
/* 704*/        int j = 0;
/* 706*/        if(aclass[0] == (com.icl.saxon.Context.class) || aclass[0] == (org.w3c.xsl.XSLTContext.class))
/* 707*/            j = 1;
/* 710*/        for(int k = i; k < avalue.length; k++)
/* 711*/            ai[k] = avalue[k - i].conversionPreference(aclass[(k + j) - i]);

/* 714*/        return ai;
            }

            private void setupParams(Value avalue[], Object aobj[], Class aclass[], int i, int j)
                throws XPathException
            {
/* 723*/        int k = i;
/* 724*/        for(int l = j; l < getNumberOfArguments(); l++)
                {
/* 725*/            aobj[k] = avalue[l].convertToJava(aclass[k]);
/* 726*/            k++;
                }

            }
}
