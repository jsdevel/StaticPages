// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   StyleSheet.java

package com.icl.saxon;

import com.icl.saxon.expr.StringValue;
import com.icl.saxon.om.NamePool;
import com.icl.saxon.style.TerminationException;
import com.icl.saxon.trace.SimpleTraceListener;
import com.icl.saxon.trace.TraceListener;
import java.io.File;
import java.io.PrintStream;
import java.util.Date;
import java.util.Properties;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

// Referenced classes of package com.icl.saxon:
//            TransformerFactoryImpl, ParameterSet, StandardURIResolver, ExtendedInputSource, 
//            Controller, Version, Loader

public class StyleSheet
{

            protected TransformerFactoryImpl factory;
            protected NamePool namePool;
            boolean showTime;
            int repeat;

            public StyleSheet()
            {
/*  28*/        factory = new TransformerFactoryImpl();
/*  30*/        namePool = NamePool.getDefaultNamePool();
/*  31*/        showTime = false;
/*  32*/        repeat = 1;
            }

            public static void main(String args[])
                throws Exception
            {
/*  47*/        (new StyleSheet()).doMain(args, new StyleSheet(), " java com.icl.saxon.StyleSheet");
            }

            protected void doMain(String as[], StyleSheet stylesheet, String s)
            {
/*  61*/        Object obj = null;
/*  62*/        String s2 = null;
/*  63*/        File file = null;
/*  64*/        Object obj1 = null;
/*  65*/        File file1 = null;
/*  66*/        boolean flag = false;
/*  67*/        ParameterSet parameterset = new ParameterSet();
/*  68*/        Properties properties = new Properties();
/*  69*/        String s3 = null;
/*  70*/        boolean flag1 = false;
/*  71*/        boolean flag2 = false;
/*  76*/        try
                {
/*  76*/            int i = 0;
/*  78*/            do
                    {
/*  78*/                if(i >= as.length)
/*  78*/                    badUsage(s, "No source file name");
/*  80*/                if(as[i].charAt(0) != '-')
/*  82*/                    break;
/*  82*/                if(as[i].equals("-a"))
                        {
/*  83*/                    flag1 = true;
/*  84*/                    i++;
                        } else
/*  87*/                if(as[i].equals("-ds"))
                        {
/*  88*/                    factory.setAttribute("http://icl.com/saxon/feature/treeModel", new Integer(0));
/*  91*/                    i++;
                        } else
/*  94*/                if(as[i].equals("-dt"))
                        {
/*  95*/                    factory.setAttribute("http://icl.com/saxon/feature/treeModel", new Integer(1));
/*  98*/                    i++;
                        } else
/* 102*/                if(as[i].equals("-l"))
                        {
/* 103*/                    factory.setAttribute("http://icl.com/saxon/feature/linenumbering", new Boolean(true));
/* 106*/                    i++;
                        } else
/* 109*/                if(as[i].equals("-u"))
                        {
/* 110*/                    flag = true;
/* 111*/                    i++;
                        } else
/* 114*/                if(as[i].equals("-t"))
                        {
/* 115*/                    System.err.println(Version.getProductName());
/* 116*/                    System.err.println("Java version " + System.getProperty("java.version"));
/* 117*/                    factory.setAttribute("http://icl.com/saxon/feature/timing", new Boolean(true));
/* 121*/                    Loader.setTracing(true);
/* 122*/                    showTime = true;
/* 123*/                    i++;
                        } else
/* 126*/                if(as[i].equals("-3"))
                        {
/* 127*/                    i++;
/* 128*/                    repeat = 3;
                        } else
/* 131*/                if(as[i].equals("-9"))
                        {
/* 132*/                    i++;
/* 133*/                    repeat = 9;
                        } else
/* 136*/                if(as[i].equals("-o"))
                        {
/* 137*/                    i++;
/* 138*/                    if(as.length < i + 2)
/* 138*/                        badUsage(s, "No output file name");
/* 139*/                    s3 = as[i++];
                        } else
/* 142*/                if(as[i].equals("-x"))
                        {
/* 143*/                    i++;
/* 144*/                    if(as.length < i + 2)
/* 144*/                        badUsage(s, "No source parser class");
/* 145*/                    String s4 = as[i++];
/* 146*/                    factory.setAttribute("http://icl.com/saxon/feature/sourceParserClass", s4);
                        } else
/* 151*/                if(as[i].equals("-y"))
                        {
/* 152*/                    i++;
/* 153*/                    if(as.length < i + 2)
/* 153*/                        badUsage(s, "No style parser class");
/* 154*/                    String s5 = as[i++];
/* 155*/                    factory.setAttribute("http://icl.com/saxon/feature/styleParserClass", s5);
                        } else
/* 160*/                if(as[i].equals("-r"))
                        {
/* 161*/                    i++;
/* 162*/                    if(as.length < i + 2)
/* 162*/                        badUsage(s, "No URIResolver class");
/* 163*/                    String s6 = as[i++];
/* 164*/                    factory.setURIResolver(makeURIResolver(s6));
                        } else
/* 167*/                if(as[i].equals("-T"))
                        {
/* 168*/                    i++;
/* 169*/                    SimpleTraceListener simpletracelistener = new SimpleTraceListener();
/* 170*/                    factory.setAttribute("http://icl.com/saxon/feature/traceListener", simpletracelistener);
/* 173*/                    factory.setAttribute("http://icl.com/saxon/feature/linenumbering", Boolean.TRUE);
                        } else
/* 178*/                if(as[i].equals("-TL"))
                        {
/* 179*/                    i++;
/* 180*/                    if(as.length < i + 2)
/* 180*/                        badUsage(s, "No TraceListener class");
/* 181*/                    TraceListener tracelistener = makeTraceListener(as[i++]);
/* 182*/                    factory.setAttribute("http://icl.com/saxon/feature/traceListener", tracelistener);
/* 185*/                    factory.setAttribute("http://icl.com/saxon/feature/linenumbering", Boolean.TRUE);
                        } else
/* 190*/                if(as[i].equals("-w0"))
                        {
/* 191*/                    i++;
/* 192*/                    factory.setAttribute("http://icl.com/saxon/feature/recoveryPolicy", new Integer(0));
                        } else
/* 196*/                if(as[i].equals("-w1"))
                        {
/* 197*/                    i++;
/* 198*/                    factory.setAttribute("http://icl.com/saxon/feature/recoveryPolicy", new Integer(1));
                        } else
/* 202*/                if(as[i].equals("-w2"))
                        {
/* 203*/                    i++;
/* 204*/                    factory.setAttribute("http://icl.com/saxon/feature/recoveryPolicy", new Integer(2));
                        } else
/* 209*/                if(as[i].equals("-m"))
                        {
/* 210*/                    i++;
/* 211*/                    if(as.length < i + 2)
/* 211*/                        badUsage(s, "No message Emitter class");
/* 212*/                    factory.setAttribute("http://icl.com/saxon/feature/messageEmitterClass", as[i++]);
                        } else
/* 217*/                if(as[i].equals("-noext"))
                        {
/* 218*/                    i++;
/* 219*/                    factory.setAttribute("http://icl.com/saxon/feature/allow-external-functions", new Boolean(false));
                        } else
                        {
/* 224*/                    badUsage(s, "Unknown option " + as[i]);
                        }
                    } while(true);
/* 230*/            if(as.length < i + 1)
/* 230*/                badUsage(s, "No source file name");
/* 231*/            String s1 = as[i++];
/* 233*/            if(!flag1)
                    {
/* 234*/                if(as.length < i + 1)
/* 234*/                    badUsage(s, "No stylesheet file name");
/* 235*/                s2 = as[i++];
                    }
/* 238*/            for(int j = i; j < as.length; j++)
                    {
/* 239*/                String s7 = as[j];
/* 240*/                int k = s7.indexOf("=");
/* 241*/                if(k < 1 || k >= s7.length() - 1)
/* 241*/                    badUsage(s, "Bad param=value pair on command line");
/* 242*/                String s8 = s7.substring(0, k);
/* 243*/                int i1 = namePool.allocate("", "", s8);
/* 244*/                parameterset.put(i1, new StringValue(s7.substring(k + 1)));
                    }

/* 247*/            Object obj2 = null;
/* 249*/            if(flag || s1.startsWith("http:") || s1.startsWith("file:"))
                    {
/* 250*/                obj2 = factory.getURIResolver().resolve(s1, null);
/* 251*/                if(obj2 == null)
/* 253*/                    obj2 = (new StandardURIResolver(factory)).resolve(s1, null);
                    } else
                    {
/* 257*/                file = new File(s1);
/* 258*/                if(!file.exists())
/* 259*/                    quit("Source file " + file + " does not exist", 2);
/* 261*/                if(file.isDirectory())
                        {
/* 262*/                    flag2 = true;
/* 263*/                    if(s3 == null)
/* 264*/                        quit("To process a directory, -o must be specified", 2);
/* 265*/                    else
/* 265*/                    if(s3.equals(s1))
                            {
/* 266*/                        quit("Output directory must be different from input", 2);
                            } else
                            {
/* 268*/                        file1 = new File(s3);
/* 269*/                        if(!file1.isDirectory())
/* 270*/                            quit("Input is a directory, but output is not", 2);
                            }
                        } else
                        {
/* 274*/                    ExtendedInputSource extendedinputsource = new ExtendedInputSource(file);
/* 275*/                    obj2 = new SAXSource(factory.getSourceParser(), extendedinputsource);
/* 276*/                    extendedinputsource.setEstimatedLength((int)file.length());
                        }
                    }
/* 280*/            if(s3 != null && !flag2)
                    {
/* 281*/                file1 = new File(s3);
/* 282*/                if(file1.isDirectory())
/* 283*/                    quit("Output is a directory, but input is not", 2);
                    }
/* 287*/            if(flag1)
                    {
/* 288*/                if(flag2)
/* 289*/                    processDirectoryAssoc(file, file1, parameterset);
/* 291*/                else
/* 291*/                    processFileAssoc(((Source) (obj2)), null, file1, parameterset);
                    } else
                    {
/* 295*/                long l = (new Date()).getTime();
                        Object obj3;
/* 298*/                if(flag || s2.startsWith("http:") || s2.startsWith("file:"))
                        {
/* 300*/                    obj3 = factory.getURIResolver().resolve(s2, null);
/* 301*/                    if(obj3 == null)
/* 303*/                        obj2 = (new StandardURIResolver(factory)).resolve(s2, null);
                        } else
                        {
/* 307*/                    File file2 = new File(s2);
/* 308*/                    if(!file2.exists())
/* 309*/                        quit("Stylesheet file " + file2 + " does not exist", 2);
/* 311*/                    ExtendedInputSource extendedinputsource1 = new ExtendedInputSource(file2);
/* 312*/                    obj3 = new SAXSource(factory.getStyleParser(), extendedinputsource1);
                        }
/* 315*/                if(obj3 == null)
/* 316*/                    quit("URIResolver for stylesheet file must return a Source", 2);
/* 319*/                Templates templates = factory.newTemplates(((Source) (obj3)));
/* 321*/                if(showTime)
                        {
/* 322*/                    long l1 = (new Date()).getTime();
/* 323*/                    System.err.println("Preparation time: " + (l1 - l) + " milliseconds");
/* 324*/                    l = l1;
                        }
/* 327*/                if(flag2)
/* 328*/                    processDirectory(file, templates, file1, parameterset);
/* 330*/                else
/* 330*/                    processFile(((Source) (obj2)), templates, file1, parameterset);
                    }
                }
/* 334*/        catch(TerminationException terminationexception)
                {
/* 334*/            quit(terminationexception.getMessage(), 1);
                }
/* 336*/        catch(TransformerException transformerexception)
                {
/* 336*/            quit("Transformation failed: " + transformerexception.getMessage(), 2);
                }
/* 338*/        catch(Exception exception)
                {
/* 338*/            exception.printStackTrace();
                }
            }

            protected static void quit(String s, int i)
            {
/* 350*/        System.err.println(s);
/* 351*/        System.exit(i);
            }

            public void processDirectoryAssoc(File file, File file1, ParameterSet parameterset)
                throws Exception
            {
/* 362*/        String as[] = file.list();
/* 363*/        int i = 0;
/* 364*/        for(int j = 0; j < as.length; j++)
                {
/* 365*/            File file2 = new File(file, as[j]);
/* 366*/            if(!file2.isDirectory())
                    {
/* 367*/                String s = file2.getName();
/* 369*/                try
                        {
/* 369*/                    ExtendedInputSource extendedinputsource = new ExtendedInputSource(file2);
/* 370*/                    SAXSource saxsource = new SAXSource(factory.getSourceParser(), extendedinputsource);
/* 371*/                    processFileAssoc(saxsource, s, file1, parameterset);
                        }
/* 373*/                catch(TransformerException transformerexception)
                        {
/* 373*/                    i++;
/* 374*/                    System.err.println("While processing " + s + ": " + transformerexception.getMessage() + "\n");
                        }
                    }
                }

/* 379*/        if(i > 0)
/* 380*/            throw new TransformerException(i + " transformation" + (i != 1 ? "s" : "") + " failed");
/* 383*/        else
/* 383*/            return;
            }

            private File makeOutputFile(File file, String s, Templates templates)
            {
/* 392*/        String s1 = templates.getOutputProperties().getProperty("media-type");
/* 394*/        String s2 = ".xml";
/* 395*/        if("text/html".equals(s1))
/* 396*/            s2 = ".html";
/* 397*/        else
/* 397*/        if("text/plain".equals(s1))
/* 398*/            s2 = ".txt";
/* 400*/        String s3 = s;
/* 401*/        if(s.endsWith(".xml") || s.endsWith(".XML"))
/* 402*/            s3 = s.substring(0, s.length() - 4);
/* 404*/        return new File(file, s3 + s2);
            }

            public void processFileAssoc(Source source, String s, File file, ParameterSet parameterset)
                throws TransformerException
            {
/* 416*/        if(showTime)
/* 417*/            System.err.println("Processing " + source.getSystemId() + " using associated stylesheet");
/* 419*/        long l = (new Date()).getTime();
/* 421*/        Source source1 = factory.getAssociatedStylesheet(source, null, null, null);
/* 422*/        Templates templates = factory.newTemplates(source1);
/* 423*/        if(showTime)
/* 424*/            System.err.println("Prepared associated stylesheet " + source1.getSystemId());
/* 426*/        Transformer transformer = templates.newTransformer();
/* 427*/        ((Controller)transformer).setParams(parameterset);
/* 429*/        File file1 = file;
/* 431*/        if(file1 != null && file1.isDirectory())
/* 432*/            file1 = makeOutputFile(file1, s, templates);
/* 435*/        StreamResult streamresult = file1 != null ? new StreamResult(file1) : new StreamResult(System.out);
/* 439*/        try
                {
/* 439*/            transformer.transform(source, streamresult);
                }
/* 441*/        catch(TerminationException terminationexception)
                {
/* 441*/            throw terminationexception;
                }
/* 444*/        catch(TransformerException transformerexception)
                {
/* 444*/            throw new TransformerException("Run-time errors were reported");
                }
/* 447*/        if(showTime)
                {
/* 448*/            long l1 = (new Date()).getTime();
/* 449*/            System.err.println("Execution time: " + (l1 - l) + " milliseconds");
/* 450*/            l = l1;
                }
            }

            public void processDirectory(File file, Templates templates, File file1, ParameterSet parameterset)
                throws TransformerException
            {
/* 463*/        String as[] = file.list();
/* 464*/        int i = 0;
/* 465*/        for(int j = 0; j < as.length; j++)
                {
/* 466*/            File file2 = new File(file, as[j]);
/* 467*/            String s = file2.getName();
/* 469*/            try
                    {
/* 469*/                if(!file2.isDirectory())
                        {
/* 470*/                    File file3 = makeOutputFile(file1, s, templates);
/* 471*/                    ExtendedInputSource extendedinputsource = new ExtendedInputSource(file2);
/* 472*/                    SAXSource saxsource = new SAXSource(factory.getSourceParser(), extendedinputsource);
/* 473*/                    processFile(saxsource, templates, file3, parameterset);
                        }
                    }
/* 476*/            catch(TransformerException transformerexception)
                    {
/* 476*/                i++;
/* 477*/                System.err.println("While processing " + s + ": " + transformerexception.getMessage() + "\n");
                    }
                }

/* 480*/        if(i > 0)
/* 481*/            throw new TransformerException(i + " transformation" + (i != 1 ? "s" : "") + " failed");
/* 484*/        else
/* 484*/            return;
            }

            public void processFile(Source source, Templates templates, File file, ParameterSet parameterset)
                throws TransformerException
            {
/* 494*/        for(int i = 0; i < repeat; i++)
                {
/* 495*/            if(showTime)
/* 496*/                System.err.println("Processing " + source.getSystemId());
/* 498*/            long l = (new Date()).getTime();
/* 499*/            Transformer transformer = templates.newTransformer();
/* 500*/            ((Controller)transformer).setParams(parameterset);
/* 502*/            StreamResult streamresult = file != null ? new StreamResult(file) : new StreamResult(System.out);
/* 508*/            try
                    {
/* 508*/                transformer.transform(source, streamresult);
                    }
/* 510*/            catch(TerminationException terminationexception)
                    {
/* 510*/                throw terminationexception;
                    }
/* 513*/            catch(TransformerException transformerexception)
                    {
/* 513*/                throw new TransformerException("Run-time errors were reported");
                    }
/* 516*/            if(showTime)
                    {
/* 517*/                long l1 = (new Date()).getTime();
/* 518*/                System.err.println("Execution time: " + (l1 - l) + " milliseconds");
/* 519*/                l = l1;
                    }
                }

            }

            protected void badUsage(String s, String s1)
            {
/* 525*/        System.err.println(s1);
/* 526*/        System.err.println(Version.getProductName());
/* 527*/        System.err.println("Usage: " + s + " [options] source-doc style-doc {param=value}...");
/* 528*/        System.err.println("Options: ");
/* 529*/        System.err.println("  -a              Use xml-stylesheet PI, not style-doc argument ");
/* 530*/        System.err.println("  -ds             Use standard tree data structure ");
/* 531*/        System.err.println("  -dt             Use tinytree data structure (default)");
/* 532*/        System.err.println("  -o filename     Send output to named file or directory ");
/* 533*/        System.err.println("  -m classname    Use specified Emitter class for xsl:message output ");
/* 534*/        System.err.println("  -r classname    Use specified URIResolver class ");
/* 535*/        System.err.println("  -t              Display version and timing information ");
/* 536*/        System.err.println("  -T              Set standard TraceListener");
/* 537*/        System.err.println("  -TL classname   Set a specific TraceListener");
/* 538*/        System.err.println("  -u              Names are URLs not filenames ");
/* 539*/        System.err.println("  -w0             Recover silently from recoverable errors ");
/* 540*/        System.err.println("  -w1             Report recoverable errors and continue (default)");
/* 541*/        System.err.println("  -w2             Treat recoverable errors as fatal");
/* 542*/        System.err.println("  -x classname    Use specified SAX parser for source file ");
/* 543*/        System.err.println("  -y classname    Use specified SAX parser for stylesheet ");
/* 544*/        System.err.println("  -?              Display this message ");
/* 545*/        System.exit(2);
            }

            public static URIResolver makeURIResolver(String s)
                throws TransformerException
            {
/* 551*/        Object obj = Loader.getInstance(s);
/* 552*/        if(obj instanceof URIResolver)
/* 553*/            return (URIResolver)obj;
/* 555*/        else
/* 555*/            throw new TransformerException("Class " + s + " is not a URIResolver");
            }

            public static TraceListener makeTraceListener(String s)
                throws TransformerException
            {
/* 561*/        Object obj = Loader.getInstance(s);
/* 562*/        if(obj instanceof TraceListener)
/* 563*/            return (TraceListener)obj;
/* 565*/        else
/* 565*/            throw new TransformerException("Class " + s + " is not a TraceListener");
            }
}
