// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   XmlParser.java

package com.icl.saxon.aelfred;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import org.xml.sax.SAXException;

// Referenced classes of package com.icl.saxon.aelfred:
//            SAXDriver

final class XmlParser
{
    static class EncodingException extends IOException
    {

                EncodingException(String s)
                {
/*4759*/            super(s);
                }
    }


            private static final boolean USE_CHEATS = true;
            private static final int DEFAULT_ATTR_COUNT = 23;
            public static final int CONTENT_UNDECLARED = 0;
            public static final int CONTENT_ANY = 1;
            public static final int CONTENT_EMPTY = 2;
            public static final int CONTENT_MIXED = 3;
            public static final int CONTENT_ELEMENTS = 4;
            public static final int ENTITY_UNDECLARED = 0;
            public static final int ENTITY_INTERNAL = 1;
            public static final int ENTITY_NDATA = 2;
            public static final int ENTITY_TEXT = 3;
            public static final int ATTRIBUTE_UNDECLARED = 0;
            public static final int ATTRIBUTE_CDATA = 1;
            public static final int ATTRIBUTE_ID = 2;
            public static final int ATTRIBUTE_IDREF = 3;
            public static final int ATTRIBUTE_IDREFS = 4;
            public static final int ATTRIBUTE_ENTITY = 5;
            public static final int ATTRIBUTE_ENTITIES = 6;
            public static final int ATTRIBUTE_NMTOKEN = 7;
            public static final int ATTRIBUTE_NMTOKENS = 8;
            public static final int ATTRIBUTE_ENUMERATED = 9;
            public static final int ATTRIBUTE_NOTATION = 10;
            private static Hashtable attributeTypeHash;
            private static final int ENCODING_EXTERNAL = 0;
            private static final int ENCODING_UTF_8 = 1;
            private static final int ENCODING_ISO_8859_1 = 2;
            private static final int ENCODING_UCS_2_12 = 3;
            private static final int ENCODING_UCS_2_21 = 4;
            private static final int ENCODING_UCS_4_1234 = 5;
            private static final int ENCODING_UCS_4_4321 = 6;
            private static final int ENCODING_UCS_4_2143 = 7;
            private static final int ENCODING_UCS_4_3412 = 8;
            private static final int ENCODING_ASCII = 9;
            public static final int ATTRIBUTE_DEFAULT_UNDECLARED = 30;
            public static final int ATTRIBUTE_DEFAULT_SPECIFIED = 31;
            public static final int ATTRIBUTE_DEFAULT_IMPLIED = 32;
            public static final int ATTRIBUTE_DEFAULT_REQUIRED = 33;
            public static final int ATTRIBUTE_DEFAULT_FIXED = 34;
            private static final int INPUT_NONE = 0;
            private static final int INPUT_INTERNAL = 1;
            private static final int INPUT_STREAM = 3;
            private static final int INPUT_BUFFER = 4;
            private static final int INPUT_READER = 5;
            private static final int LIT_ENTITY_REF = 2;
            private static final int LIT_NORMALIZE = 4;
            private static final int LIT_ATTRIBUTE = 8;
            private static final int LIT_DISABLE_PE = 16;
            private static final int LIT_DISABLE_CREF = 32;
            private static final int LIT_DISABLE_EREF = 64;
            private static final int LIT_ENTITY_CHECK = 128;
            private static final int LIT_PUBID = 256;
            private static final int CONTEXT_NORMAL = 0;
            private static final int CONTEXT_LITERAL = 1;
            private SAXDriver handler;
            private Reader reader;
            private InputStream is;
            private int line;
            private int column;
            private int sourceType;
            private Stack inputStack;
            private URLConnection externalEntity;
            private int encoding;
            private int currentByteCount;
            private char readBuffer[];
            private int readBufferPos;
            private int readBufferLength;
            private int readBufferOverflow;
            private static final int READ_BUFFER_MAX = 16384;
            private byte rawReadBuffer[];
            private static int DATA_BUFFER_INITIAL = 4096;
            private char dataBuffer[];
            private int dataBufferPos;
            private static int NAME_BUFFER_INITIAL = 1024;
            private char nameBuffer[];
            private int nameBufferPos;
            private Hashtable elementInfo;
            private Hashtable entityInfo;
            private Hashtable notationInfo;
            private String currentElement;
            private int currentElementContent;
            private String basePublicId;
            private String baseURI;
            private int baseEncoding;
            private Reader baseReader;
            private InputStream baseInputStream;
            private char baseInputBuffer[];
            private int baseInputBufferStart;
            private int baseInputBufferLength;
            private Stack entityStack;
            private boolean inLiteral;
            private boolean expandPE;
            private boolean peIsError;
            private static final int SYMBOL_TABLE_LENGTH = 1087;
            private Object symbolTable[][];
            private String tagAttributes[];
            private int tagAttributePos;
            private boolean sawCR;
            private boolean inCDATA;

            XmlParser()
            {
/*  96*/        cleanupVariables();
            }

            void setHandler(SAXDriver saxdriver)
            {
/* 108*/        handler = saxdriver;
            }

            void doParse(String s, String s1, Reader reader1, InputStream inputstream, String s2)
                throws Exception
            {
/* 138*/        if(handler == null)
/* 139*/            throw new IllegalStateException("no callback handler");
/* 141*/        basePublicId = s1;
/* 142*/        baseURI = s;
/* 143*/        baseReader = reader1;
/* 144*/        baseInputStream = inputstream;
/* 146*/        initializeVariables();
/* 151*/        setInternalEntity("amp", "&#38;");
/* 152*/        setInternalEntity("lt", "&#60;");
/* 153*/        setInternalEntity("gt", "&#62;");
/* 154*/        setInternalEntity("apos", "&#39;");
/* 155*/        setInternalEntity("quot", "&#34;");
/* 157*/        handler.startDocument();
/* 159*/        pushURL("[document]", basePublicId, baseURI, baseReader, baseInputStream, s2, false);
/* 163*/        try
                {
/* 163*/            parseDocument();
/* 164*/            handler.endDocument();
                }
/* 166*/        finally
                {
/* 166*/            if(baseReader != null)
/* 167*/                try
                        {
/* 167*/                    baseReader.close();
                        }
/* 168*/                catch(IOException ioexception) { }
/* 169*/            if(baseInputStream != null)
/* 170*/                try
                        {
/* 170*/                    baseInputStream.close();
                        }
/* 171*/                catch(IOException ioexception1) { }
/* 172*/            if(is != null)
/* 173*/                try
                        {
/* 173*/                    is.close();
                        }
/* 174*/                catch(IOException ioexception2) { }
/* 175*/            if(reader1 != null)
/* 177*/                try
                        {
/* 177*/                    reader1.close();
                        }
/* 178*/                catch(IOException ioexception3) { }
/* 180*/            cleanupVariables();
                }
            }

            private void error(String s, String s1, String s2)
                throws SAXException
            {
/* 452*/        if(s1 != null)
/* 453*/            s = s + " (found \"" + s1 + "\")";
/* 455*/        if(s2 != null)
/* 456*/            s = s + " (expected \"" + s2 + "\")";
/* 458*/        String s3 = null;
/* 460*/        if(externalEntity != null)
/* 461*/            s3 = externalEntity.getURL().toString();
/* 463*/        handler.error(s, s3, line, column);
/* 466*/        throw new SAXException(s);
            }

            private void error(String s, char c, String s1)
                throws SAXException
            {
/* 478*/        error(s, (new Character(c)).toString(), s1);
            }

            private void error(String s)
                throws SAXException
            {
/* 485*/        error(s, ((String) (null)), null);
            }

            private void parseDocument()
                throws Exception
            {
/* 508*/        try
                {
/* 508*/            parseProlog();
/* 509*/            require('<', "document prolog");
/* 510*/            parseElement();
                }
/* 512*/        catch(EOFException eofexception)
                {
/* 512*/            error("premature end of file", "[EOF]", null);
                }
/* 516*/        try
                {
/* 516*/            parseMisc();
/* 517*/            char c = readCh();
/* 518*/            error("unexpected characters after document end", c, null);
                }
/* 520*/        catch(EOFException eofexception1)
                {
/* 520*/            return;
                }
            }

            private void parseComment()
                throws Exception
            {
/* 536*/        boolean flag = expandPE;
/* 538*/        expandPE = false;
/* 539*/        parseUntil("--");
/* 540*/        require('>', "-- in comment");
/* 541*/        expandPE = flag;
/* 542*/        handler.comment(dataBuffer, 0, dataBufferPos);
/* 543*/        dataBufferPos = 0;
            }

            private void parsePI()
                throws SAXException, IOException
            {
/* 561*/        boolean flag = expandPE;
/* 563*/        expandPE = false;
/* 564*/        String s = readNmtoken(true);
/* 565*/        if("xml".equalsIgnoreCase(s))
/* 566*/            error("Illegal processing instruction target", s, null);
/* 567*/        if(!tryRead("?>"))
                {
/* 568*/            requireWhitespace();
/* 569*/            parseUntil("?>");
                }
/* 571*/        expandPE = flag;
/* 572*/        handler.processingInstruction(s, dataBufferToString());
            }

            private void parseCDSect()
                throws Exception
            {
/* 589*/        parseUntil("]]>");
/* 590*/        dataBufferFlush();
            }

            private void parseProlog()
                throws Exception
            {
/* 611*/        parseMisc();
/* 613*/        if(tryRead("<!DOCTYPE"))
                {
/* 614*/            parseDoctypedecl();
/* 615*/            parseMisc();
                }
            }

            private String parseXMLDecl(boolean flag)
                throws SAXException, IOException
            {
/* 642*/        String s1 = null;
/* 643*/        Object obj = null;
/* 644*/        byte byte0 = 112;
/* 647*/        require("version", "XML declaration");
/* 648*/        parseEq();
/* 649*/        String s = readLiteral(byte0);
/* 650*/        if(!s.equals("1.0"))
/* 651*/            error("unsupported XML version", s, "1.0");
/* 655*/        boolean flag1 = tryWhitespace();
/* 656*/        if(tryRead("encoding"))
                {
/* 657*/            if(!flag1)
/* 658*/                error("whitespace required before 'encoding='");
/* 659*/            parseEq();
/* 660*/            s1 = readLiteral(byte0);
/* 661*/            if(!flag)
/* 662*/                setupDecoding(s1);
                }
/* 666*/        if(s1 != null)
/* 667*/            flag1 = tryWhitespace();
/* 668*/        if(tryRead("standalone"))
                {
/* 669*/            if(!flag1)
/* 670*/                error("whitespace required before 'standalone='");
/* 671*/            parseEq();
/* 672*/            String s2 = readLiteral(byte0);
/* 673*/            if(!"yes".equals(s2) && !"no".equals(s2))
/* 674*/                error("standalone flag must be 'yes' or 'no'");
                }
/* 677*/        skipWhitespace();
/* 678*/        require("?>", "XML declaration");
/* 680*/        return s1;
            }

            private String parseTextDecl(boolean flag)
                throws SAXException, IOException
            {
/* 700*/        String s = null;
/* 701*/        byte byte0 = 112;
/* 704*/        if(tryRead("version"))
                {
/* 706*/            parseEq();
/* 707*/            String s1 = readLiteral(byte0);
/* 708*/            if(!s1.equals("1.0"))
/* 709*/                error("unsupported XML version", s1, "1.0");
/* 711*/            requireWhitespace();
                }
/* 716*/        require("encoding", "XML text declaration");
/* 717*/        parseEq();
/* 718*/        s = readLiteral(byte0);
/* 719*/        if(!flag)
/* 720*/            setupDecoding(s);
/* 722*/        skipWhitespace();
/* 723*/        require("?>", "XML text declaration");
/* 725*/        return s;
            }

            private void setupDecoding(String s)
                throws SAXException, IOException
            {
/* 748*/        s = s.toUpperCase();
/* 758*/        if(encoding == 1 || encoding == 0)
                {
/* 759*/            if(s.equals("ISO-8859-1") || s.equals("8859_1") || s.equals("ISO8859_1"))
                    {
/* 763*/                encoding = 2;
/* 764*/                return;
                    }
/* 765*/            if(s.equals("US-ASCII") || s.equals("ASCII"))
                    {
/* 767*/                encoding = 9;
/* 768*/                return;
                    }
/* 769*/            if(s.equals("UTF-8") || s.equals("UTF8"))
                    {
/* 771*/                encoding = 1;
/* 772*/                return;
                    }
/* 773*/            if(encoding != 0)
/* 775*/                throw new EncodingException(s);
                }
/* 782*/        if(encoding == 3 || encoding == 4)
                {
/* 783*/            if(!s.equals("ISO-10646-UCS-2") && !s.equals("UTF-16") && !s.equals("UTF-16BE") && !s.equals("UTF-16LE"))
/* 787*/                error("unsupported Unicode encoding", s, "UTF-16");
/* 790*/            return;
                }
/* 794*/        if(encoding == 5 || encoding == 6 || encoding == 7 || encoding == 8)
                {
/* 798*/            if(!s.equals("ISO-10646-UCS-4"))
/* 799*/                error("unsupported 32-bit encoding", s, "ISO-10646-UCS-4");
/* 802*/            return;
                }
/* 809*/        if(s.equals("UTF-16BE"))
                {
/* 810*/            encoding = 3;
/* 811*/            return;
                }
/* 813*/        if(s.equals("UTF-16LE"))
                {
/* 814*/            encoding = 4;
/* 815*/            return;
                }
/* 822*/        if(s.equals("UTF-16") || s.equals("ISO-10646-UCS-2"))
/* 824*/            s = "Unicode";
/* 827*/        reader = new InputStreamReader(is, s);
/* 828*/        sourceType = 5;
            }

            private void parseMisc()
                throws Exception
            {
/* 842*/        do
                {
/* 843*/            skipWhitespace();
/* 844*/            if(tryRead("<?"))
/* 845*/                parsePI();
/* 846*/            else
/* 846*/            if(tryRead("<!--"))
/* 847*/                parseComment();
/* 849*/            else
/* 849*/                return;
                } while(true);
            }

            private void parseDoctypedecl()
                throws Exception
            {
/* 869*/        requireWhitespace();
/* 870*/        String s = readNmtoken(true);
/* 873*/        skipWhitespace();
/* 874*/        String as[] = readExternalIds(false);
/* 877*/        handler.doctypeDecl(s, as[0], as[1]);
/* 880*/        skipWhitespace();
/* 881*/        if(tryRead('['))
/* 885*/            do
                    {
/* 885*/                expandPE = true;
/* 886*/                skipWhitespace();
/* 887*/                expandPE = false;
/* 888*/                if(tryRead(']'))
/* 889*/                    break;
/* 892*/                peIsError = expandPE = true;
/* 893*/                parseMarkupdecl();
/* 894*/                peIsError = expandPE = false;
                    } while(true);
/* 900*/        if(as[1] != null)
                {
/* 901*/            pushURL("[external subset]", as[0], as[1], null, null, null, false);
/* 905*/            do
                    {
/* 905*/                expandPE = true;
/* 906*/                skipWhitespace();
/* 907*/                expandPE = false;
/* 908*/                if(tryRead('>'))
/* 909*/                    break;
/* 911*/                expandPE = true;
/* 912*/                parseMarkupdecl();
/* 913*/                expandPE = false;
                    } while(true);
                } else
                {
/* 918*/            skipWhitespace();
/* 919*/            require('>', "internal DTD subset");
                }
/* 923*/        handler.endDoctype();
/* 924*/        expandPE = false;
            }

            private void parseMarkupdecl()
                throws Exception
            {
/* 942*/        if(tryRead("<!ELEMENT"))
/* 943*/            parseElementdecl();
/* 944*/        else
/* 944*/        if(tryRead("<!ATTLIST"))
/* 945*/            parseAttlistDecl();
/* 946*/        else
/* 946*/        if(tryRead("<!ENTITY"))
/* 947*/            parseEntityDecl();
/* 948*/        else
/* 948*/        if(tryRead("<!NOTATION"))
/* 949*/            parseNotationDecl();
/* 950*/        else
/* 950*/        if(tryRead("<?"))
/* 951*/            parsePI();
/* 952*/        else
/* 952*/        if(tryRead("<!--"))
/* 953*/            parseComment();
/* 954*/        else
/* 954*/        if(tryRead("<!["))
                {
/* 955*/            if(inputStack.size() > 0)
/* 956*/                parseConditionalSect();
/* 958*/            else
/* 958*/                error("conditional sections illegal in internal subset");
                } else
                {
/* 960*/            error("expected markup declaration");
                }
            }

            private void parseElement()
                throws Exception
            {
/* 981*/        int i = currentElementContent;
/* 982*/        String s1 = currentElement;
/* 987*/        tagAttributePos = 0;
/* 990*/        String s = readNmtoken(true);
/* 993*/        currentElement = s;
/* 994*/        Object aobj[] = (Object[])elementInfo.get(s);
/* 995*/        currentElementContent = getContentType(aobj, 1);
/* 999*/        boolean flag = tryWhitespace();
                char c;
/*1000*/        for(c = readCh(); c != '/' && c != '>'; c = readCh())
                {
/*1002*/            unread(c);
/*1003*/            if(!flag)
/*1004*/                error("need whitespace between attributes");
/*1005*/            parseAttribute(s);
/*1006*/            flag = tryWhitespace();
                }

/*1011*/        Enumeration enumeration = declaredAttributes(aobj);
/*1012*/        if(enumeration != null)
/*1016*/label0:
/*1016*/            while(enumeration.hasMoreElements()) 
                    {
/*1016*/                String s2 = (String)enumeration.nextElement();
/*1018*/                for(int j = 0; j < tagAttributePos; j++)
/*1019*/                    if(tagAttributes[j] == s2)
/*1020*/                        continue label0;

/*1024*/                String s3 = getAttributeExpandedValue(s, s2);
/*1025*/                if(s3 != null)
/*1026*/                    handler.attribute(s2, s3, false);
                    }
/*1034*/        switch(c)
                {
/*1036*/        case 62: // '>'
/*1036*/            handler.startElement(s);
/*1037*/            parseContent();
                    break;

/*1040*/        case 47: // '/'
/*1040*/            require('>', "empty element tag");
/*1041*/            handler.startElement(s);
/*1042*/            handler.endElement(s);
                    break;
                }
/*1047*/        currentElement = s1;
/*1048*/        currentElementContent = i;
            }

            private void parseAttribute(String s)
                throws Exception
            {
/*1066*/        byte byte0 = 10;
/*1069*/        String s1 = readNmtoken(true);
/*1070*/        int i = getAttributeType(s, s1);
/*1073*/        parseEq();
                String s2;
/*1077*/        if(i == 1 || i == 0)
/*1078*/            s2 = readLiteral(byte0);
/*1080*/        else
/*1080*/            s2 = readLiteral(byte0 | 4);
/*1084*/        for(int j = 0; j < tagAttributePos; j++)
/*1085*/            if(s1.equals(tagAttributes[j]))
/*1086*/                error("duplicate attribute", s1, null);

/*1090*/        handler.attribute(s1, s2, true);
/*1091*/        dataBufferPos = 0;
/*1095*/        if(tagAttributePos == tagAttributes.length)
                {
/*1096*/            String as[] = new String[tagAttributes.length * 2];
/*1097*/            System.arraycopy(tagAttributes, 0, as, 0, tagAttributePos);
/*1098*/            tagAttributes = as;
                }
/*1100*/        tagAttributes[tagAttributePos++] = s1;
            }

            private void parseEq()
                throws SAXException, IOException
            {
/*1113*/        skipWhitespace();
/*1114*/        require('=', "attribute name");
/*1115*/        skipWhitespace();
            }

            private void parseETag()
                throws Exception
            {
/*1130*/        require(currentElement, "element end tag");
/*1131*/        skipWhitespace();
/*1132*/        require('>', "name in end tag");
/*1133*/        handler.endElement(currentElement);
            }

            private void parseContent()
                throws Exception
            {
/*1152*/        do
                {
/*1170*/            parseCharData();
/*1174*/            char c = readCh();
/*1175*/            switch(c)
                    {
/*1152*/            default:
                        break;

/*1178*/            case 38: // '&'
/*1178*/                char c1 = readCh();
/*1179*/                if(c1 == '#')
                        {
/*1180*/                    parseCharRef();
                        } else
                        {
/*1182*/                    unread(c1);
/*1183*/                    parseEntityRef(true);
                        }
/*1185*/                continue;

/*1188*/            case 60: // '<'
/*1188*/                dataBufferFlush();
/*1189*/                char c2 = readCh();
/*1190*/                switch(c2)
                        {
/*1192*/                case 33: // '!'
/*1192*/                    c2 = readCh();
/*1193*/                    switch(c2)
                            {
/*1195*/                    case 45: // '-'
/*1195*/                        require('-', "start of comment");
/*1196*/                        parseComment();
                                break;

/*1199*/                    case 91: // '['
/*1199*/                        require("CDATA[", "CDATA section");
/*1200*/                        handler.startCDATA();
/*1201*/                        inCDATA = true;
/*1202*/                        parseCDSect();
/*1203*/                        inCDATA = false;
/*1204*/                        handler.endCDATA();
                                break;

/*1207*/                    default:
/*1207*/                        error("expected comment or CDATA section", c2, null);
                                break;
                            }
                            break;

/*1213*/                case 63: // '?'
/*1213*/                    parsePI();
                            break;

/*1217*/                case 47: // '/'
/*1217*/                    parseETag();
/*1218*/                    return;

/*1221*/                default:
/*1221*/                    unread(c2);
/*1222*/                    parseElement();
                            break;
                        }
                        break;
                    }
                } while(true);
            }

            private void parseElementdecl()
                throws Exception
            {
/*1242*/        requireWhitespace();
/*1244*/        String s = readNmtoken(true);
/*1246*/        requireWhitespace();
/*1248*/        parseContentspec(s);
/*1250*/        skipWhitespace();
/*1251*/        require('>', "element declaration");
            }

            private void parseContentspec(String s)
                throws Exception
            {
/*1264*/        if(tryRead("EMPTY"))
                {
/*1265*/            setElement(s, 2, null, null);
/*1266*/            return;
                }
/*1267*/        if(tryRead("ANY"))
                {
/*1268*/            setElement(s, 1, null, null);
/*1269*/            return;
                }
/*1271*/        require('(', "element name");
/*1272*/        dataBufferAppend('(');
/*1273*/        skipWhitespace();
/*1274*/        if(tryRead("#PCDATA"))
                {
/*1275*/            dataBufferAppend("#PCDATA");
/*1276*/            parseMixed();
/*1277*/            setElement(s, 3, dataBufferToString(), null);
                } else
                {
/*1279*/            parseElements();
/*1280*/            setElement(s, 4, dataBufferToString(), null);
                }
            }

            private void parseElements()
                throws Exception
            {
/*1304*/        skipWhitespace();
/*1305*/        parseCp();
/*1308*/        skipWhitespace();
/*1309*/        char c = readCh();
                char c1;
/*1310*/        switch(c)
                {
/*1312*/        case 41: // ')'
/*1312*/            dataBufferAppend(')');
/*1313*/            c = readCh();
/*1314*/            switch(c)
                    {
/*1318*/            case 42: // '*'
/*1318*/            case 43: // '+'
/*1318*/            case 63: // '?'
/*1318*/                dataBufferAppend(c);
                        break;

/*1321*/            default:
/*1321*/                unread(c);
                        break;
                    }
/*1323*/            return;

/*1326*/        case 44: // ','
/*1326*/        case 124: // '|'
/*1326*/            c1 = c;
/*1327*/            dataBufferAppend(c);
                    break;

/*1330*/        default:
/*1330*/            error("bad separator in content model", c, null);
/*1331*/            return;
                }
/*1336*/        do
                {
/*1336*/            skipWhitespace();
/*1337*/            parseCp();
/*1338*/            skipWhitespace();
/*1339*/            c = readCh();
/*1340*/            if(c == ')')
                    {
/*1341*/                dataBufferAppend(')');
/*1342*/                break;
                    }
/*1343*/            if(c != c1)
                    {
/*1344*/                error("bad separator in content model", c, null);
/*1345*/                return;
                    }
/*1347*/            dataBufferAppend(c);
                } while(true);
/*1352*/        c = readCh();
/*1353*/        switch(c)
                {
/*1357*/        case 42: // '*'
/*1357*/        case 43: // '+'
/*1357*/        case 63: // '?'
/*1357*/            dataBufferAppend(c);
/*1358*/            return;
                }
/*1360*/        unread(c);
            }

            private void parseCp()
                throws Exception
            {
/*1375*/        if(tryRead('('))
                {
/*1376*/            dataBufferAppend('(');
/*1377*/            parseElements();
                } else
                {
/*1379*/            dataBufferAppend(readNmtoken(true));
/*1380*/            char c = readCh();
/*1381*/            switch(c)
                    {
/*1385*/            case 42: // '*'
/*1385*/            case 43: // '+'
/*1385*/            case 63: // '?'
/*1385*/                dataBufferAppend(c);
                        break;

/*1388*/            default:
/*1388*/                unread(c);
                        break;
                    }
                }
            }

            private void parseMixed()
                throws Exception
            {
/*1407*/        skipWhitespace();
/*1408*/        if(tryRead(')'))
                {
/*1409*/            dataBufferAppend(")*");
/*1410*/            tryRead('*');
/*1411*/            return;
                }
/*1415*/        skipWhitespace();
/*1417*/        for(; !tryRead(")*"); skipWhitespace())
                {
/*1417*/            require('|', "alternative");
/*1418*/            dataBufferAppend('|');
/*1419*/            skipWhitespace();
/*1420*/            dataBufferAppend(readNmtoken(true));
                }

/*1423*/        dataBufferAppend(")*");
            }

            private void parseAttlistDecl()
                throws Exception
            {
/*1439*/        requireWhitespace();
/*1440*/        String s = readNmtoken(true);
/*1441*/        for(boolean flag = tryWhitespace(); !tryRead('>'); flag = tryWhitespace())
                {
/*1443*/            if(!flag)
/*1444*/                error("whitespace required before attribute definition");
/*1445*/            parseAttDef(s);
                }

            }

            private void parseAttDef(String s)
                throws Exception
            {
/*1462*/        String s2 = null;
/*1465*/        String s1 = readNmtoken(true);
/*1468*/        requireWhitespace();
/*1469*/        int i = readAttType();
/*1473*/        if(i == 9 || i == 10)
/*1474*/            s2 = dataBufferToString();
/*1478*/        requireWhitespace();
/*1479*/        parseDefault(s, s1, i, s2);
            }

            private int readAttType()
                throws Exception
            {
/*1496*/        if(tryRead('('))
                {
/*1497*/            parseEnumeration(false);
/*1498*/            return 9;
                }
/*1500*/        String s = readNmtoken(true);
/*1501*/        if(s.equals("NOTATION"))
/*1502*/            parseNotationType();
/*1504*/        Integer integer = (Integer)attributeTypeHash.get(s);
/*1505*/        if(integer == null)
                {
/*1506*/            error("illegal attribute type", s, null);
/*1507*/            return 0;
                } else
                {
/*1509*/            return integer.intValue();
                }
            }

            private void parseEnumeration(boolean flag)
                throws Exception
            {
/*1525*/        dataBufferAppend('(');
/*1528*/        skipWhitespace();
/*1529*/        dataBufferAppend(readNmtoken(flag));
/*1531*/        skipWhitespace();
/*1533*/        for(; !tryRead(')'); skipWhitespace())
                {
/*1533*/            require('|', "enumeration value");
/*1534*/            dataBufferAppend('|');
/*1535*/            skipWhitespace();
/*1536*/            dataBufferAppend(readNmtoken(flag));
                }

/*1539*/        dataBufferAppend(')');
            }

            private void parseNotationType()
                throws Exception
            {
/*1554*/        requireWhitespace();
/*1555*/        require('(', "NOTATION");
/*1557*/        parseEnumeration(true);
            }

            private void parseDefault(String s, String s1, int i, String s2)
                throws Exception
            {
/*1575*/        byte byte0 = 31;
/*1576*/        String s3 = null;
/*1577*/        char c = '\270';
/*1590*/        if(tryRead('#'))
                {
/*1591*/            if(tryRead("FIXED"))
                    {
/*1592*/                byte0 = 34;
/*1593*/                requireWhitespace();
/*1594*/                s3 = readLiteral(c);
                    } else
/*1595*/            if(tryRead("REQUIRED"))
/*1596*/                byte0 = 33;
/*1597*/            else
/*1597*/            if(tryRead("IMPLIED"))
/*1598*/                byte0 = 32;
/*1600*/            else
/*1600*/                error("illegal keyword for attribute default value");
                } else
                {
/*1603*/            s3 = readLiteral(c);
                }
/*1604*/        setAttribute(s, s1, i, s2, s3, byte0);
            }

            private void parseConditionalSect()
                throws Exception
            {
/*1625*/        skipWhitespace();
/*1626*/        if(tryRead("INCLUDE"))
                {
/*1627*/            skipWhitespace();
/*1628*/            require('[', "INCLUDE");
/*1629*/            skipWhitespace();
/*1631*/            for(; !tryRead("]]>"); skipWhitespace())
/*1631*/                parseMarkupdecl();

                } else
/*1634*/        if(tryRead("IGNORE"))
                {
/*1635*/            skipWhitespace();
/*1636*/            require('[', "IGNORE");
/*1637*/            boolean flag = true;
/*1639*/            expandPE = false;
/*1640*/            for(int i = 1; i > 0;)
                    {
/*1641*/                char c = readCh();
/*1642*/                switch(c)
                        {
/*1625*/                default:
                            break;

/*1644*/                case 60: // '<'
/*1644*/                    if(tryRead("!["))
/*1645*/                        i++;
                            // fall through

/*1648*/                case 93: // ']'
/*1648*/                    if(tryRead("]>"))
/*1649*/                        i--;
                            break;
                        }
                    }

/*1653*/            expandPE = true;
                } else
                {
/*1655*/            error("conditional section must begin with INCLUDE or IGNORE");
                }
            }

            private void parseCharRef()
                throws SAXException, IOException
            {
/*1670*/        int i = 0;
                char c;
/*1673*/        if(tryRead('x'))
/*1676*/            do
                    {
/*1676*/                c = readCh();
/*1677*/                switch(c)
                        {
/*1700*/                case 48: // '0'
/*1700*/                case 49: // '1'
/*1700*/                case 50: // '2'
/*1700*/                case 51: // '3'
/*1700*/                case 52: // '4'
/*1700*/                case 53: // '5'
/*1700*/                case 54: // '6'
/*1700*/                case 55: // '7'
/*1700*/                case 56: // '8'
/*1700*/                case 57: // '9'
/*1700*/                case 65: // 'A'
/*1700*/                case 66: // 'B'
/*1700*/                case 67: // 'C'
/*1700*/                case 68: // 'D'
/*1700*/                case 69: // 'E'
/*1700*/                case 70: // 'F'
/*1700*/                case 97: // 'a'
/*1700*/                case 98: // 'b'
/*1700*/                case 99: // 'c'
/*1700*/                case 100: // 'd'
/*1700*/                case 101: // 'e'
/*1700*/                case 102: // 'f'
/*1700*/                    i *= 16;
/*1701*/                    i += Integer.parseInt((new Character(c)).toString(), 16);
/*1703*/                    continue;

/*1670*/                case 59: // ';'
                            break;

/*1707*/                case 58: // ':'
/*1707*/                case 60: // '<'
/*1707*/                case 61: // '='
/*1707*/                case 62: // '>'
/*1707*/                case 63: // '?'
/*1707*/                case 64: // '@'
/*1707*/                case 71: // 'G'
/*1707*/                case 72: // 'H'
/*1707*/                case 73: // 'I'
/*1707*/                case 74: // 'J'
/*1707*/                case 75: // 'K'
/*1707*/                case 76: // 'L'
/*1707*/                case 77: // 'M'
/*1707*/                case 78: // 'N'
/*1707*/                case 79: // 'O'
/*1707*/                case 80: // 'P'
/*1707*/                case 81: // 'Q'
/*1707*/                case 82: // 'R'
/*1707*/                case 83: // 'S'
/*1707*/                case 84: // 'T'
/*1707*/                case 85: // 'U'
/*1707*/                case 86: // 'V'
/*1707*/                case 87: // 'W'
/*1707*/                case 88: // 'X'
/*1707*/                case 89: // 'Y'
/*1707*/                case 90: // 'Z'
/*1707*/                case 91: // '['
/*1707*/                case 92: // '\\'
/*1707*/                case 93: // ']'
/*1707*/                case 94: // '^'
/*1707*/                case 95: // '_'
/*1707*/                case 96: // '`'
/*1707*/                default:
/*1707*/                    error("illegal character in character reference", c, null);
                            break;
                        }
/*1708*/                break;
                    } while(true);
/*1714*/        else
/*1714*/label0:
/*1714*/            do
                    {
/*1714*/                c = readCh();
/*1715*/                switch(c)
                        {
/*1726*/                case 48: // '0'
/*1726*/                case 49: // '1'
/*1726*/                case 50: // '2'
/*1726*/                case 51: // '3'
/*1726*/                case 52: // '4'
/*1726*/                case 53: // '5'
/*1726*/                case 54: // '6'
/*1726*/                case 55: // '7'
/*1726*/                case 56: // '8'
/*1726*/                case 57: // '9'
/*1726*/                    i *= 10;
/*1727*/                    i += Integer.parseInt((new Character(c)).toString(), 10);
/*1729*/                    break;

/*1726*/                case 59: // ';'
/*1726*/                    break label0;

/*1733*/                case 58: // ':'
/*1733*/                default:
/*1733*/                    error("illegal character in character reference", c, null);
/*1734*/                    break label0;
                        }
                    } while(true);
/*1740*/        if(i < 32 && i != 10 && i != 9 && i != 13 || i >= 55296 && i <= 57343 || i == 65534 || i == 65535 || i > 0x10ffff)
/*1745*/            error("illegal XML character reference U+" + Integer.toHexString(i));
/*1750*/        if(i <= 65535)
/*1752*/            dataBufferAppend((char)i);
/*1753*/        else
/*1753*/        if(i <= 0x10ffff)
                {
/*1754*/            i -= 0x10000;
/*1756*/            dataBufferAppend((char)(0xd800 | i >> 10));
/*1757*/            dataBufferAppend((char)(0xdc00 | i & 0x3ff));
                } else
                {
/*1760*/            error("character reference " + i + " is too large for UTF-16", (new Integer(i)).toString(), null);
                }
            }

            private void parseEntityRef(boolean flag)
                throws SAXException, IOException
            {
/*1779*/        String s = readNmtoken(true);
/*1780*/        require(';', "entity reference");
/*1781*/        switch(getEntityType(s))
                {
/*1779*/        default:
                    break;

/*1783*/        case 0: // '\0'
/*1783*/            error("reference to undeclared entity", s, null);
/*1784*/            break;

/*1786*/        case 1: // '\001'
/*1786*/            pushString(s, getEntityValue(s));
/*1787*/            break;

/*1789*/        case 3: // '\003'
/*1789*/            if(flag)
/*1790*/                pushURL(s, getEntityPublicId(s), getEntitySystemId(s), null, null, null, true);
/*1794*/            else
/*1794*/                error("reference to external entity in attribute value.", s, null);
/*1797*/            break;

/*1799*/        case 2: // '\002'
/*1799*/            if(flag)
/*1800*/                error("unparsed entity reference in content", s, null);
/*1802*/            else
/*1802*/                error("reference to external entity in attribute value.", s, null);
                    break;
                }
            }

            private void parsePEReference()
                throws SAXException, IOException
            {
/*1822*/        String s = "%" + readNmtoken(true);
/*1823*/        require(';', "parameter entity reference");
/*1824*/        switch(getEntityType(s))
                {
/*1822*/        case 0: // '\0'
/*1822*/        case 2: // '\002'
/*1822*/        default:
                    break;

/*1833*/        case 1: // '\001'
/*1833*/            if(inLiteral)
/*1834*/                pushString(s, getEntityValue(s));
/*1836*/            else
/*1836*/                pushString(s, ' ' + getEntityValue(s) + ' ');
/*1837*/            break;

/*1839*/        case 3: // '\003'
/*1839*/            if(!inLiteral)
/*1840*/                pushString(null, " ");
/*1841*/            pushURL(s, getEntityPublicId(s), getEntitySystemId(s), null, null, null, true);
/*1844*/            if(!inLiteral)
/*1845*/                pushString(null, " ");
                    break;
                }
            }

            private void parseEntityDecl()
                throws Exception
            {
/*1867*/        boolean flag = false;
/*1870*/        expandPE = false;
/*1871*/        requireWhitespace();
/*1872*/        if(tryRead('%'))
                {
/*1873*/            flag = true;
/*1874*/            requireWhitespace();
                }
/*1876*/        expandPE = true;
/*1880*/        String s = readNmtoken(true);
/*1881*/        if(flag)
/*1882*/            s = "%" + s;
/*1886*/        requireWhitespace();
/*1887*/        char c = readCh();
/*1888*/        unread(c);
/*1889*/        if(c == '"' || c == '\'')
                {
/*1892*/            String s1 = readLiteral(0);
/*1893*/            setInternalEntity(s, s1);
                } else
                {
/*1896*/            String as[] = readExternalIds(false);
/*1897*/            if(as[1] == null)
/*1898*/                error("system identifer missing", s, null);
/*1902*/            boolean flag1 = tryWhitespace();
/*1903*/            if(!flag && tryRead("NDATA"))
                    {
/*1904*/                if(!flag1)
/*1905*/                    error("whitespace required before NDATA");
/*1906*/                requireWhitespace();
/*1907*/                String s2 = readNmtoken(true);
/*1908*/                setExternalDataEntity(s, as[0], as[1], s2);
                    } else
                    {
/*1910*/                setExternalTextEntity(s, as[0], as[1]);
                    }
                }
/*1915*/        skipWhitespace();
/*1916*/        require('>', "NDATA");
            }

            private void parseNotationDecl()
                throws Exception
            {
/*1935*/        requireWhitespace();
/*1936*/        String s = readNmtoken(true);
/*1938*/        requireWhitespace();
/*1941*/        String as[] = readExternalIds(true);
/*1942*/        if(as[0] == null && as[1] == null)
/*1943*/            error("external identifer missing", s, null);
/*1947*/        setNotation(s, as[0], as[1]);
/*1949*/        skipWhitespace();
/*1950*/        require('>', "notation declaration");
            }

            private void parseCharData()
                throws Exception
            {
/*1971*/        int i = 0;
/*1972*/        int j = 0;
/*1975*/        for(int k = readBufferPos; k < readBufferLength; k++)
                {
                    char c;
/*1977*/            switch(c = readBuffer[k])
                    {
/*1979*/            case 10: // '\n'
/*1979*/                i++;
/*1980*/                j = 0;
                        break;

/*1984*/            case 38: // '&'
/*1984*/            case 60: // '<'
/*1984*/                int l = readBufferPos;
/*1985*/                j++;
/*1986*/                readBufferPos = k;
/*1987*/                if(i > 0)
                        {
/*1988*/                    line += i;
/*1989*/                    column = j;
                        } else
                        {
/*1991*/                    column += j;
                        }
/*1993*/                dataBufferAppend(readBuffer, l, k - l);
/*1994*/                return;

/*1997*/            case 93: // ']'
/*1997*/                if(k + 2 < readBufferLength && readBuffer[k + 1] == ']' && readBuffer[k + 2] == '>')
/*2000*/                    error("character data may not contain ']]>'");
/*2003*/                j++;
                        break;

/*2006*/            default:
/*2006*/                if(c < ' ' || c > '\uFFFD')
/*2007*/                    error("illegal XML character U+" + Integer.toHexString(c));
                        // fall through

/*2012*/            case 9: // '\t'
/*2012*/            case 13: // '\r'
/*2012*/                j++;
                        break;
                    }
                }

/*2020*/        i = 0;
/*2022*/        do
                {
/*2022*/            char c1 = readCh();
/*2023*/            switch(c1)
                    {
/*2026*/            case 38: // '&'
/*2026*/            case 60: // '<'
/*2026*/                unread(c1);
/*2027*/                return;

/*2029*/            case 93: // ']'
/*2029*/                i++;
/*2030*/                dataBufferAppend(c1);
/*2031*/                continue;

/*2033*/            case 62: // '>'
/*2033*/                if(i >= 2)
                        {
/*2035*/                    error("']]>' is not allowed here");
/*2036*/                    continue;
                        }
                        // fall through

/*2040*/            default:
/*2040*/                i = 0;
/*2041*/                dataBufferAppend(c1);
                        break;
                    }
                } while(true);
            }

            private void requireWhitespace()
                throws SAXException, IOException
            {
/*2058*/        char c = readCh();
/*2059*/        if(isWhitespace(c))
/*2060*/            skipWhitespace();
/*2062*/        else
/*2062*/            error("whitespace required", c, null);
            }

            private void parseWhitespace()
                throws Exception
            {
                char c;
/*2073*/        for(c = readCh(); isWhitespace(c); c = readCh())
/*2075*/            dataBufferAppend(c);

/*2078*/        unread(c);
            }

            private void skipWhitespace()
                throws SAXException, IOException
            {
/*2096*/        int i = 0;
/*2097*/        int j = 0;
/*2100*/label0:
/*2100*/        for(int k = readBufferPos; k < readBufferLength; k++)
                {
/*2101*/            switch(readBuffer[k])
                    {
/*2105*/            case 9: // '\t'
/*2105*/            case 13: // '\r'
/*2105*/            case 32: // ' '
/*2105*/                j++;
/*2106*/                continue;

/*2108*/            case 10: // '\n'
/*2108*/                i++;
/*2109*/                j = 0;
/*2110*/                continue;

/*2112*/            case 37: // '%'
/*2112*/                if(expandPE)
/*2113*/                    break label0;
                        break;
                    }
/*2116*/            readBufferPos = k;
/*2117*/            if(i > 0)
                    {
/*2118*/                line += i;
/*2119*/                column = j;
                    } else
                    {
/*2121*/                column += j;
                    }
/*2123*/            return;
                }

/*2129*/        for(i = readCh(); isWhitespace(i); i = readCh());
/*2133*/        unread(i);
            }

            private String readNmtoken(boolean flag)
                throws SAXException, IOException
            {
/*2151*/label0:
/*2151*/        for(int i = readBufferPos; i < readBufferLength;)
                {
/*2152*/            char c = readBuffer[i];
/*2153*/            switch(c)
                    {
/*2155*/            case 37: // '%'
/*2155*/                if(expandPE)
/*2156*/                    break label0;
                        // fall through

/*2169*/            case 9: // '\t'
/*2169*/            case 10: // '\n'
/*2169*/            case 13: // '\r'
/*2169*/            case 32: // ' '
/*2169*/            case 34: // '"'
/*2169*/            case 38: // '&'
/*2169*/            case 39: // '\''
/*2169*/            case 41: // ')'
/*2169*/            case 42: // '*'
/*2169*/            case 43: // '+'
/*2169*/            case 44: // ','
/*2169*/            case 47: // '/'
/*2169*/            case 59: // ';'
/*2169*/            case 60: // '<'
/*2169*/            case 61: // '='
/*2169*/            case 62: // '>'
/*2169*/            case 63: // '?'
/*2169*/            case 91: // '['
/*2169*/            case 124: // '|'
/*2169*/                int j = readBufferPos;
/*2170*/                if(i == j)
/*2171*/                    error("name expected", readBuffer[i], null);
/*2172*/                readBufferPos = i;
/*2173*/                return intern(readBuffer, j, i - j);

/*2178*/            default:
/*2178*/                if(i == readBufferPos && flag)
                        {
/*2179*/                    if(!Character.isUnicodeIdentifierStart(c) && c != ':' && c != '_')
/*2181*/                        error("Not a name start character, U+" + Integer.toHexString(c));
                        } else
/*2183*/                if(!Character.isUnicodeIdentifierPart(c) && c != '-' && c != ':' && c != '_' && c != '.' && !isExtender(c))
/*2186*/                    error("Not a name character, U+" + Integer.toHexString(c));
/*2151*/                i++;
                        break;
                    }
                }

/*2192*/        nameBufferPos = 0;
/*2197*/        do
                {
/*2197*/            char c1 = readCh();
/*2198*/            switch(c1)
                    {
/*2209*/            case 9: // '\t'
/*2209*/            case 10: // '\n'
/*2209*/            case 13: // '\r'
/*2209*/            case 32: // ' '
/*2209*/            case 34: // '"'
/*2209*/            case 37: // '%'
/*2209*/            case 38: // '&'
/*2209*/            case 39: // '\''
/*2209*/            case 41: // ')'
/*2209*/            case 42: // '*'
/*2209*/            case 43: // '+'
/*2209*/            case 44: // ','
/*2209*/            case 47: // '/'
/*2209*/            case 59: // ';'
/*2209*/            case 60: // '<'
/*2209*/            case 61: // '='
/*2209*/            case 62: // '>'
/*2209*/            case 63: // '?'
/*2209*/            case 91: // '['
/*2209*/            case 124: // '|'
/*2209*/                unread(c1);
/*2210*/                if(nameBufferPos == 0)
/*2211*/                    error("name expected");
/*2214*/                if(flag && !Character.isUnicodeIdentifierStart(nameBuffer[0]) && ":_".indexOf(nameBuffer[0]) == -1)
/*2218*/                    error("Not a name start character, U+" + Integer.toHexString(nameBuffer[0]));
/*2220*/                String s = intern(nameBuffer, 0, nameBufferPos);
/*2221*/                nameBufferPos = 0;
/*2222*/                return s;
                    }
/*2226*/            if((nameBufferPos != 0 || !flag) && !Character.isUnicodeIdentifierPart(c1) && ":-_.".indexOf(c1) == -1 && !isExtender(c1))
/*2230*/                error("Not a name character, U+" + Integer.toHexString(c1));
/*2232*/            if(nameBufferPos >= nameBuffer.length)
/*2233*/                nameBuffer = (char[])extendArray(nameBuffer, nameBuffer.length, nameBufferPos);
/*2236*/            nameBuffer[nameBufferPos++] = c1;
                } while(true);
            }

            private static boolean isExtender(char c)
            {
/*2244*/        return c == '\267' || c == '\u02D0' || c == '\u02D1' || c == '\u0387' || c == '\u0640' || c == '\u0E46' || c == '\u0EC6' || c == '\u3005' || c >= '\u3031' && c <= '\u3035' || c >= '\u309D' && c <= '\u309E' || c >= '\u30FC' && c <= '\u30FE';
            }

            private String readLiteral(int i)
                throws SAXException, IOException
            {
/*2269*/        int j = line;
/*2270*/        boolean flag = expandPE;
/*2273*/        char c = readCh();
/*2274*/        if(c != '"' && c != '\'' && c != 0)
                {
/*2275*/            error("expected '\"' or \"'\"", c, null);
/*2276*/            return null;
                }
/*2278*/        inLiteral = true;
/*2279*/        if((i & 0x10) != 0)
/*2280*/            expandPE = false;
/*2285*/        char ac[] = readBuffer;
/*2289*/        try
                {
/*2289*/            char c1 = readCh();
/*2292*/            while(c1 != c || readBuffer != ac) 
                    {
/*2292*/                switch(c1)
                        {
/*2269*/                default:
                            break;

/*2297*/                case 10: // '\n'
/*2297*/                case 13: // '\r'
/*2297*/                    if((i & 0x108) != 0)
/*2298*/                        c1 = ' ';
/*2298*/                    break;

/*2301*/                case 9: // '\t'
/*2301*/                    if((i & 8) != 0)
/*2302*/                        c1 = ' ';
/*2302*/                    break;

/*2305*/                case 38: // '&'
/*2305*/                    c1 = readCh();
/*2308*/                    if(c1 == '#')
                            {
/*2309*/                        if((i & 0x20) != 0)
                                {
/*2310*/                            dataBufferAppend('&');
/*2311*/                            continue;
                                }
/*2313*/                        parseCharRef();
                            } else
                            {
/*2317*/                        unread(c1);
/*2319*/                        if((i & 2) > 0)
/*2320*/                            parseEntityRef(false);
/*2323*/                        else
/*2323*/                        if((i & 0x40) != 0)
                                {
/*2324*/                            dataBufferAppend('&');
                                } else
                                {
/*2328*/                            String s = readNmtoken(true);
/*2329*/                            require(';', "entity reference");
/*2330*/                            if((i & 0x80) != 0 && getEntityType(s) == 0)
/*2334*/                                error("General entity '" + s + "' must be declared before use");
/*2337*/                            dataBufferAppend('&');
/*2338*/                            dataBufferAppend(s);
/*2339*/                            dataBufferAppend(';');
                                }
                            }
/*2342*/                    c1 = readCh();
/*2343*/                    continue;

/*2348*/                case 60: // '<'
/*2348*/                    if((i & 8) != 0)
/*2349*/                        error("attribute values may not contain '<'");
                            break;
                        }
/*2357*/                dataBufferAppend(c1);
/*2358*/                c1 = readCh();
                    }
                }
/*2361*/        catch(EOFException eofexception)
                {
/*2361*/            error("end of input while looking for delimiter (started on line " + j + ')', ((String) (null)), (new Character(c)).toString());
                }
/*2364*/        inLiteral = false;
/*2365*/        expandPE = flag;
/*2368*/        if((i & 4) > 0)
/*2369*/            dataBufferNormalize();
/*2373*/        return dataBufferToString();
            }

            private String[] readExternalIds(boolean flag)
                throws Exception
            {
/*2387*/        String as[] = new String[2];
/*2388*/        byte byte0 = 112;
/*2390*/        if(tryRead("PUBLIC"))
                {
/*2391*/            requireWhitespace();
/*2392*/            as[0] = readLiteral(0x104 | byte0);
/*2393*/            if(flag)
                    {
/*2394*/                skipWhitespace();
/*2395*/                char c = readCh();
/*2396*/                unread(c);
/*2397*/                if(c == '"' || c == '\'')
/*2398*/                    as[1] = readLiteral(byte0);
                    } else
                    {
/*2401*/                requireWhitespace();
/*2402*/                as[1] = readLiteral(byte0);
                    }
/*2405*/            for(int i = 0; i < as[0].length(); i++)
                    {
/*2406*/                char c1 = as[0].charAt(i);
/*2407*/                if((c1 < 'a' || c1 > 'z') && (c1 < 'A' || c1 > 'Z') && " \r\n0123456789-' ()+,./:=?;!*#@$_%".indexOf(c1) == -1)
/*2413*/                    error("illegal PUBLIC id character U+" + Integer.toHexString(c1));
                    }

                } else
/*2416*/        if(tryRead("SYSTEM"))
                {
/*2417*/            requireWhitespace();
/*2418*/            as[1] = readLiteral(byte0);
                }
/*2425*/        return as;
            }

            private final boolean isWhitespace(char c)
            {
/*2439*/        if(c > ' ')
/*2440*/            return false;
/*2441*/        return c == ' ' || c == '\n' || c == '\t' || c == '\r';
            }

            private void dataBufferAppend(char c)
            {
/*2458*/        if(dataBufferPos >= dataBuffer.length)
/*2459*/            dataBuffer = (char[])extendArray(dataBuffer, dataBuffer.length, dataBufferPos);
/*2462*/        dataBuffer[dataBufferPos++] = c;
            }

            private void dataBufferAppend(String s)
            {
/*2471*/        dataBufferAppend(s.toCharArray(), 0, s.length());
            }

            private void dataBufferAppend(char ac[], int i, int j)
            {
/*2480*/        dataBuffer = (char[])extendArray(dataBuffer, dataBuffer.length, dataBufferPos + j);
/*2484*/        System.arraycopy(ac, i, dataBuffer, dataBufferPos, j);
/*2485*/        dataBufferPos += j;
            }

            private void dataBufferNormalize()
            {
/*2494*/        int i = 0;
/*2495*/        int j = 0;
                int k;
/*2496*/        for(k = dataBufferPos; j < k && dataBuffer[j] == ' '; j++);
/*2505*/        for(; k > j && dataBuffer[k - 1] == ' '; k--);
/*2511*/        while(j < k) 
                {
/*2511*/            char c = dataBuffer[j++];
/*2515*/            if(c == ' ')
                    {
/*2516*/                while(j < k && dataBuffer[j++] == ' ') ;
/*2518*/                dataBuffer[i++] = ' ';
/*2519*/                dataBuffer[i++] = dataBuffer[j - 1];
                    } else
                    {
/*2521*/                dataBuffer[i++] = c;
                    }
                }
/*2526*/        dataBufferPos = i;
            }

            private String dataBufferToString()
            {
/*2535*/        String s = new String(dataBuffer, 0, dataBufferPos);
/*2536*/        dataBufferPos = 0;
/*2537*/        return s;
            }

            private void dataBufferFlush()
                throws SAXException
            {
/*2548*/        if(currentElementContent == 4 && dataBufferPos > 0 && !inCDATA)
                {
/*2554*/            for(int i = 0; i < dataBufferPos; i++)
/*2555*/                if(!isWhitespace(dataBuffer[i]))
                        {
/*2556*/                    handler.charData(dataBuffer, 0, dataBufferPos);
/*2557*/                    dataBufferPos = 0;
                        }

/*2560*/            if(dataBufferPos > 0)
                    {
/*2561*/                handler.ignorableWhitespace(dataBuffer, 0, dataBufferPos);
/*2562*/                dataBufferPos = 0;
                    }
                } else
/*2564*/        if(dataBufferPos > 0)
                {
/*2565*/            handler.charData(dataBuffer, 0, dataBufferPos);
/*2566*/            dataBufferPos = 0;
                }
            }

            private void require(String s, String s1)
                throws SAXException, IOException
            {
/*2580*/        int i = s.length();
                char ac[];
/*2583*/        if(i < dataBuffer.length)
                {
/*2584*/            ac = dataBuffer;
/*2585*/            s.getChars(0, i, ac, 0);
                } else
                {
/*2587*/            ac = s.toCharArray();
                }
/*2589*/        if(i <= readBufferLength - readBufferPos)
                {
/*2591*/            int j = readBufferPos;
/*2593*/            for(int l = 0; l < i;)
                    {
/*2594*/                if(ac[l] != readBuffer[j])
/*2595*/                    error("unexpected characters in " + s1, ((String) (null)), s);
/*2593*/                l++;
/*2593*/                j++;
                    }

/*2596*/            readBufferPos = j;
                } else
                {
/*2599*/            for(int k = 0; k < i; k++)
/*2600*/                require(ac[k], s);

                }
            }

            private void require(char c, String s)
                throws SAXException, IOException
            {
/*2611*/        char c1 = readCh();
/*2613*/        if(c1 != c)
/*2614*/            error("unexpected character after " + s, c1, c + "");
            }

            public String intern(char ac[], int i, int j)
            {
/*2637*/        int k = 0;
/*2638*/        int l = 0;
/*2642*/        for(int i1 = i; i1 < i + j; i1++)
/*2643*/            l = 31 * l + ac[i1];

/*2644*/        l = (l & 0x7fffffff) % 1087;
                Object aobj[];
/*2647*/        if((aobj = symbolTable[l]) == null)
                {
/*2649*/            aobj = new Object[8];
                } else
                {
/*2655*/            for(; k < aobj.length; k += 2)
                    {
/*2655*/                char ac1[] = (char[])aobj[k];
/*2658*/                if(ac1 == null)
/*2659*/                    break;
/*2662*/                if(ac1.length == j)
                        {
/*2663*/                    for(int j1 = 0; j1 < ac1.length; j1++)
                            {
/*2665*/                        if(ac[i + j1] != ac1[j1])
/*2666*/                            break;
/*2667*/                        if(j1 == j - 1)
/*2669*/                            return (String)aobj[k + 1];
                            }

                        }
                    }

/*2678*/            aobj = (Object[])extendArray(((Object) (aobj)), aobj.length, k);
                }
/*2680*/        symbolTable[l] = aobj;
/*2684*/        String s = (new String(ac, i, j)).intern();
/*2685*/        aobj[k] = s.toCharArray();
/*2686*/        aobj[k + 1] = s;
/*2687*/        return s;
            }

            private Object extendArray(Object obj, int i, int j)
            {
/*2697*/        if(j < i)
/*2698*/            return obj;
/*2700*/        char ac[] = null;
/*2701*/        int k = i * 2;
/*2703*/        if(k <= j)
/*2704*/            k = j + 1;
/*2706*/        if(obj instanceof char[])
/*2707*/            ac = new char[k];
/*2708*/        else
/*2708*/        if(obj instanceof Object[])
/*2709*/            ac = ((char []) (new Object[k]));
/*2711*/        else
/*2711*/            throw new RuntimeException();
/*2713*/        System.arraycopy(obj, 0, ac, 0, i);
/*2714*/        return ac;
            }

            public Enumeration declaredElements()
            {
/*2739*/        return elementInfo.keys();
            }

            private int getContentType(Object aobj[], int i)
            {
/*2758*/        if(aobj == null)
/*2759*/            return i;
/*2760*/        int j = ((Integer)aobj[0]).intValue();
/*2761*/        if(j == 0)
/*2762*/            j = i;
/*2763*/        return j;
            }

            public int getElementContentType(String s)
            {
/*2780*/        Object aobj[] = (Object[])elementInfo.get(s);
/*2781*/        return getContentType(aobj, 0);
            }

            public String getElementContentModel(String s)
            {
/*2795*/        Object aobj[] = (Object[])elementInfo.get(s);
/*2796*/        if(aobj == null)
/*2797*/            return null;
/*2799*/        else
/*2799*/            return (String)aobj[1];
            }

            private void setElement(String s, int i, String s1, Hashtable hashtable)
                throws Exception
            {
/*2815*/        Object aobj[] = (Object[])elementInfo.get(s);
/*2818*/        if(aobj == null)
                {
/*2819*/            aobj = new Object[3];
/*2820*/            aobj[0] = new Integer(i);
/*2821*/            aobj[1] = s1;
/*2822*/            aobj[2] = hashtable;
/*2823*/            elementInfo.put(s, ((Object) (aobj)));
/*2824*/            return;
                }
/*2828*/        if(i != 0)
                {
/*2830*/            if(((Integer)aobj[0]).intValue() == 0)
                    {
/*2831*/                aobj[0] = new Integer(i);
/*2832*/                aobj[1] = s1;
                    }
                } else
/*2840*/        if(hashtable != null)
/*2841*/            aobj[2] = hashtable;
            }

            private Hashtable getElementAttributes(String s)
            {
/*2853*/        Object aobj[] = (Object[])elementInfo.get(s);
/*2854*/        if(aobj == null)
/*2855*/            return null;
/*2857*/        else
/*2857*/            return (Hashtable)aobj[2];
            }

            private Enumeration declaredAttributes(Object aobj[])
            {
/*2883*/        if(aobj == null)
/*2884*/            return null;
                Hashtable hashtable;
/*2885*/        if((hashtable = (Hashtable)aobj[2]) == null)
/*2886*/            return null;
/*2887*/        else
/*2887*/            return hashtable.keys();
            }

            public Enumeration declaredAttributes(String s)
            {
/*2904*/        return declaredAttributes((Object[])elementInfo.get(s));
            }

            public int getAttributeType(String s, String s1)
            {
/*2927*/        Object aobj[] = getAttribute(s, s1);
/*2928*/        if(aobj == null)
/*2929*/            return 0;
/*2931*/        else
/*2931*/            return ((Integer)aobj[0]).intValue();
            }

            public String getAttributeEnumeration(String s, String s1)
            {
/*2946*/        Object aobj[] = getAttribute(s, s1);
/*2947*/        if(aobj == null)
/*2948*/            return null;
/*2950*/        else
/*2950*/            return (String)aobj[3];
            }

            public String getAttributeDefaultValue(String s, String s1)
            {
/*2965*/        Object aobj[] = getAttribute(s, s1);
/*2966*/        if(aobj == null)
/*2967*/            return null;
/*2969*/        else
/*2969*/            return (String)aobj[1];
            }

            public String getAttributeExpandedValue(String s, String s1)
                throws Exception
            {
/*2986*/        Object aobj[] = getAttribute(s, s1);
/*2988*/        if(aobj == null)
/*2989*/            return null;
/*2990*/        if(aobj[4] == null && aobj[1] != null)
                {
/*2993*/            char ac[] = new char[1];
/*2994*/            int i = 10;
/*2995*/            int j = getAttributeType(s, s1);
/*2997*/            if(j != 1 && j != 0)
/*2998*/                i |= 4;
/*2999*/            ac[0] = '"';
/*3000*/            pushCharArray(null, ac, 0, 1);
/*3001*/            pushString(null, (String)aobj[1]);
/*3002*/            pushCharArray(null, ac, 0, 1);
/*3003*/            aobj[4] = readLiteral(i);
                }
/*3005*/        return (String)aobj[4];
            }

            public int getAttributeDefaultValueType(String s, String s1)
            {
/*3018*/        Object aobj[] = getAttribute(s, s1);
/*3019*/        if(aobj == null)
/*3020*/            return 30;
/*3022*/        else
/*3022*/            return ((Integer)aobj[2]).intValue();
            }

            private void setAttribute(String s, String s1, int i, String s2, String s3, int j)
                throws Exception
            {
/*3042*/        Hashtable hashtable = getElementAttributes(s);
/*3043*/        if(hashtable == null)
/*3044*/            hashtable = new Hashtable();
/*3048*/        if(hashtable.get(s1) != null)
                {
/*3050*/            return;
                } else
                {
/*3052*/            Object aobj[] = new Object[5];
/*3053*/            aobj[0] = new Integer(i);
/*3054*/            aobj[1] = s3;
/*3055*/            aobj[2] = new Integer(j);
/*3056*/            aobj[3] = s2;
/*3057*/            aobj[4] = null;
/*3058*/            hashtable.put(s1, ((Object) (aobj)));
/*3061*/            setElement(s, 0, null, hashtable);
/*3063*/            return;
                }
            }

            private Object[] getAttribute(String s, String s1)
            {
/*3072*/        Hashtable hashtable = getElementAttributes(s);
/*3073*/        if(hashtable == null)
/*3074*/            return null;
/*3077*/        else
/*3077*/            return (Object[])hashtable.get(s1);
            }

            public Enumeration declaredEntities()
            {
/*3098*/        return entityInfo.keys();
            }

            public int getEntityType(String s)
            {
/*3112*/        Object aobj[] = (Object[])entityInfo.get(s);
/*3113*/        if(aobj == null)
/*3114*/            return 0;
/*3116*/        else
/*3116*/            return ((Integer)aobj[0]).intValue();
            }

            public String getEntityPublicId(String s)
            {
/*3132*/        Object aobj[] = (Object[])entityInfo.get(s);
/*3133*/        if(aobj == null)
/*3134*/            return null;
/*3136*/        else
/*3136*/            return (String)aobj[1];
            }

            public String getEntitySystemId(String s)
            {
/*3153*/        Object aobj[] = (Object[])entityInfo.get(s);
/*3154*/        if(aobj == null)
/*3155*/            return null;
/*3158*/        try
                {
/*3158*/            String s1 = (String)aobj[2];
/*3159*/            URL url = (URL)aobj[5];
/*3160*/            if(url == null)
                    {
/*3160*/                return s1;
                    } else
                    {
/*3161*/                URL url1 = new URL(url, s1);
/*3162*/                return url1.toString();
                    }
                }
/*3166*/        catch(IOException ioexception)
                {
/*3166*/            return (String)aobj[2];
                }
            }

            public String getEntityValue(String s)
            {
/*3181*/        Object aobj[] = (Object[])entityInfo.get(s);
/*3182*/        if(aobj == null)
/*3183*/            return null;
/*3185*/        else
/*3185*/            return (String)aobj[3];
            }

            public String getEntityNotationName(String s)
            {
/*3200*/        Object aobj[] = (Object[])entityInfo.get(s);
/*3201*/        if(aobj == null)
/*3202*/            return null;
/*3204*/        else
/*3204*/            return (String)aobj[4];
            }

            private void setInternalEntity(String s, String s1)
            {
/*3214*/        setEntity(s, 1, null, null, s1, null);
            }

            private void setExternalDataEntity(String s, String s1, String s2, String s3)
            {
/*3224*/        setEntity(s, 2, s1, s2, null, s3);
            }

            private void setExternalTextEntity(String s, String s1, String s2)
            {
/*3234*/        setEntity(s, 3, s1, s2, null, null);
            }

            private void setEntity(String s, int i, String s1, String s2, String s3, String s4)
            {
/*3247*/        if(entityInfo.get(s) == null)
                {
/*3248*/            Object aobj[] = new Object[6];
/*3249*/            aobj[0] = new Integer(i);
/*3250*/            aobj[1] = s1;
/*3251*/            aobj[2] = s2;
/*3252*/            aobj[3] = s3;
/*3253*/            aobj[4] = s4;
/*3254*/            aobj[5] = externalEntity != null ? ((Object) (externalEntity.getURL())) : null;
/*3257*/            entityInfo.put(s, ((Object) (aobj)));
                }
            }

            public Enumeration declaredNotations()
            {
/*3276*/        return notationInfo.keys();
            }

            public String getNotationPublicId(String s)
            {
/*3292*/        Object aobj[] = (Object[])notationInfo.get(s);
/*3293*/        if(aobj == null)
/*3294*/            return null;
/*3296*/        else
/*3296*/            return (String)aobj[0];
            }

            public String getNotationSystemId(String s)
            {
/*3312*/        Object aobj[] = (Object[])notationInfo.get(s);
/*3313*/        if(aobj == null)
/*3314*/            return null;
/*3316*/        else
/*3316*/            return (String)aobj[1];
            }

            private void setNotation(String s, String s1, String s2)
                throws Exception
            {
/*3332*/        if(notationInfo.get(s) == null)
                {
/*3333*/            Object aobj[] = new Object[2];
/*3334*/            aobj[0] = s1;
/*3335*/            aobj[1] = s2;
/*3336*/            notationInfo.put(s, ((Object) (aobj)));
                }
            }

            public int getLineNumber()
            {
/*3354*/        return line;
            }

            public int getColumnNumber()
            {
/*3363*/        return column;
            }

            private char readCh()
                throws SAXException, IOException
            {
/*3398*/        while(readBufferPos >= readBufferLength) 
/*3399*/            switch(sourceType)
                    {
/*3402*/            case 3: // '\003'
/*3402*/            case 5: // '\005'
/*3402*/                readDataChunk();
/*3404*/                while(readBufferLength < 1) 
                        {
/*3404*/                    popInput();
/*3405*/                    if(readBufferLength < 1)
/*3406*/                        readDataChunk();
                        }
                        break;

/*3413*/            default:
/*3413*/                popInput();
                        break;
                    }
/*3418*/        char c = readBuffer[readBufferPos++];
/*3420*/        if(c == '\n')
                {
/*3421*/            line++;
/*3422*/            column = 0;
                } else
                {
/*3424*/            if(c != '<')
/*3426*/                if(c < ' ' && c != '\t' && c != '\r' || c > '\uFFFD')
/*3427*/                    error("illegal XML character U+" + Integer.toHexString(c));
/*3434*/                else
/*3434*/                if(c == '%' && expandPE)
                        {
/*3435*/                    if(peIsError && entityStack.size() == 1)
/*3437*/                        error("PE reference within declaration in internal subset.");
/*3438*/                    parsePEReference();
/*3439*/                    return readCh();
                        }
/*3441*/            column++;
                }
/*3444*/        return c;
            }

            private void unread(char c)
                throws SAXException
            {
/*3467*/        if(c == '\n')
                {
/*3468*/            line--;
/*3469*/            column = -1;
                }
/*3471*/        if(readBufferPos > 0)
/*3472*/            readBuffer[--readBufferPos] = c;
/*3474*/        else
/*3474*/            pushString(null, (new Character(c)).toString());
            }

            private void unread(char ac[], int i)
                throws SAXException
            {
/*3492*/        for(int j = 0; j < i; j++)
/*3493*/            if(ac[j] == '\n')
                    {
/*3494*/                line--;
/*3495*/                column = -1;
                    }

/*3498*/        if(i < readBufferPos)
                {
/*3499*/            readBufferPos -= i;
                } else
                {
/*3501*/            pushCharArray(null, ac, 0, i);
/*3502*/            sourceType = 4;
                }
            }

            private void pushURL(String s, String s1, String s2, Reader reader1, InputStream inputstream, String s3, boolean flag)
                throws SAXException, IOException
            {
/*3534*/        boolean flag1 = false;
/*3537*/        pushInput(s);
/*3541*/        readBuffer = new char[16388];
/*3542*/        readBufferPos = 0;
/*3543*/        readBufferLength = 0;
/*3544*/        readBufferOverflow = -1;
/*3545*/        is = null;
/*3546*/        line = 1;
/*3547*/        column = 0;
/*3548*/        currentByteCount = 0;
/*3550*/        if(!flag)
/*3556*/            try
                    {
/*3556*/                if(s2 != null && externalEntity != null)
/*3557*/                    s2 = (new URL(externalEntity.getURL(), s2)).toString();
/*3558*/                else
/*3558*/                if(baseURI != null)
/*3559*/                    s2 = (new URL(new URL(baseURI), s2)).toString();
                    }
/*3563*/            catch(IOException ioexception)
                    {
/*3563*/                popInput();
/*3564*/                error("Invalid URL " + s2 + " (" + ioexception.getMessage() + ")");
                    }
/*3571*/        if(reader1 == null && inputstream == null && s2 != null)
                {
/*3572*/            Object obj = null;
/*3574*/            try
                    {
/*3574*/                obj = handler.resolveEntity(s1, s2);
                    }
/*3576*/            catch(IOException ioexception1)
                    {
/*3576*/                popInput();
/*3577*/                error("Failure resolving entity " + s2 + " (" + ioexception1.getMessage() + ")");
                    }
/*3579*/            if(obj != null)
/*3580*/                if(obj instanceof String)
                        {
/*3581*/                    s2 = (String)obj;
/*3582*/                    flag = true;
                        } else
/*3583*/                if(obj instanceof InputStream)
/*3584*/                    inputstream = (InputStream)obj;
/*3585*/                else
/*3585*/                if(obj instanceof Reader)
/*3586*/                    reader1 = (Reader)obj;
                }
/*3592*/        if(s2 != null)
/*3593*/            handler.startExternalEntity(s2);
/*3595*/        else
/*3595*/            handler.startExternalEntity("[unidentified data stream]");
/*3600*/        if(reader1 != null)
                {
/*3601*/            sourceType = 5;
/*3602*/            reader = reader1;
/*3603*/            tryEncodingDecl(true);
/*3604*/            return;
                }
/*3609*/        sourceType = 3;
/*3610*/        if(inputstream != null)
                {
/*3611*/            is = inputstream;
                } else
                {
/*3614*/            URL url = new URL(s2);
/*3616*/            try
                    {
/*3616*/                externalEntity = url.openConnection();
/*3617*/                externalEntity.connect();
/*3618*/                is = externalEntity.getInputStream();
                    }
/*3620*/            catch(IOException ioexception2)
                    {
/*3620*/                popInput();
/*3621*/                error("Cannot read from " + s2 + (s2.equals(ioexception2.getMessage()) ? "" : " (" + ioexception2.getMessage() + ")"));
                    }
                }
/*3628*/        if(!is.markSupported())
/*3629*/            is = new BufferedInputStream(is);
/*3633*/        if(s3 == null && externalEntity != null && !"file".equals(externalEntity.getURL().getProtocol()))
                {
/*3642*/            s3 = externalEntity.getContentType();
                    int i;
/*3645*/            if(s3 == null)
/*3646*/                i = -1;
/*3648*/            else
/*3648*/                i = s3.indexOf("charset");
/*3654*/            if(i < 0)
                    {
/*3655*/                s3 = null;
                    } else
                    {
/*3657*/                i = s3.indexOf('=', i + 7);
/*3658*/                s3 = s3.substring(i + 1);
/*3659*/                if((i = s3.indexOf(';')) > 0)
/*3660*/                    s3 = s3.substring(0, i);
/*3663*/                if((i = s3.indexOf('(')) > 0)
/*3664*/                    s3 = s3.substring(0, i);
/*3666*/                if((i = s3.indexOf('"')) > 0)
/*3667*/                    s3 = s3.substring(i + 1, s3.indexOf('"', i + 2));
/*3669*/                s3.trim();
                    }
                }
/*3675*/        if(s3 != null)
                {
/*3676*/            encoding = 0;
/*3677*/            setupDecoding(s3);
/*3678*/            flag1 = true;
                } else
                {
/*3682*/            detectEncoding();
/*3683*/            flag1 = false;
                }
/*3685*/        is.mark(100);
/*3689*/        try
                {
/*3689*/            tryEncodingDecl(flag1);
                }
/*3691*/        catch(EncodingException encodingexception)
                {
/*3691*/            s3 = encodingexception.getMessage();
/*3696*/            try
                    {
/*3696*/                if(sourceType != 3)
/*3697*/                    throw encodingexception;
/*3699*/                is.reset();
/*3700*/                readBufferPos = 0;
/*3701*/                readBufferLength = 0;
/*3702*/                readBufferOverflow = -1;
/*3703*/                line = 1;
/*3704*/                currentByteCount = column = 0;
/*3706*/                sourceType = 5;
/*3707*/                reader = new InputStreamReader(is, s3);
/*3708*/                is = null;
/*3710*/                tryEncodingDecl(true);
                    }
/*3713*/            catch(IOException ioexception3)
                    {
/*3713*/                error("unsupported text encoding", s3, null);
                    }
                }
            }

            private String tryEncodingDecl(boolean flag)
                throws SAXException, IOException
            {
/*3739*/        if(tryRead("<?xml"))
                {
/*3740*/            dataBufferFlush();
/*3741*/            if(tryWhitespace())
/*3742*/                if(inputStack.size() > 0)
/*3743*/                    return parseTextDecl(flag);
/*3745*/                else
/*3745*/                    return parseXMLDecl(flag);
/*3748*/            unread("xml".toCharArray(), 3);
/*3749*/            parsePI();
                }
/*3752*/        return null;
            }

            private void detectEncoding()
                throws SAXException, IOException
            {
/*3781*/        byte abyte0[] = new byte[4];
/*3785*/        is.mark(4);
/*3786*/        is.read(abyte0);
/*3787*/        is.reset();
/*3792*/        if(tryEncoding(abyte0, (byte)0, (byte)0, (byte)0, (byte)60))
/*3796*/            encoding = 5;
/*3798*/        else
/*3798*/        if(tryEncoding(abyte0, (byte)60, (byte)0, (byte)0, (byte)0))
/*3801*/            encoding = 6;
/*3803*/        else
/*3803*/        if(tryEncoding(abyte0, (byte)0, (byte)0, (byte)60, (byte)0))
/*3806*/            encoding = 7;
/*3808*/        else
/*3808*/        if(tryEncoding(abyte0, (byte)0, (byte)60, (byte)0, (byte)0))
/*3811*/            encoding = 8;
/*3820*/        else
/*3820*/        if(tryEncoding(abyte0, (byte)-17, (byte)-69, (byte)-65))
                {
/*3821*/            encoding = 1;
/*3822*/            is.read();
/*3822*/            is.read();
/*3822*/            is.read();
                } else
/*3831*/        if(tryEncoding(abyte0, (byte)-2, (byte)-1))
                {
/*3834*/            encoding = 3;
/*3835*/            is.read();
/*3835*/            is.read();
                } else
/*3837*/        if(tryEncoding(abyte0, (byte)-1, (byte)-2))
                {
/*3840*/            encoding = 4;
/*3841*/            is.read();
/*3841*/            is.read();
                } else
/*3843*/        if(tryEncoding(abyte0, (byte)0, (byte)60, (byte)0, (byte)63))
                {
/*3847*/            encoding = 3;
/*3848*/            error("no byte-order mark for UCS-2 entity");
                } else
/*3850*/        if(tryEncoding(abyte0, (byte)60, (byte)0, (byte)63, (byte)0))
                {
/*3854*/            encoding = 4;
/*3855*/            error("no byte-order mark for UCS-2 entity");
                } else
/*3861*/        if(tryEncoding(abyte0, (byte)60, (byte)63, (byte)120, (byte)109))
                {
/*3865*/            encoding = 1;
/*3866*/            read8bitEncodingDeclaration();
                } else
                {
/*3873*/            encoding = 1;
                }
            }

            private static boolean tryEncoding(byte abyte0[], byte byte0, byte byte1, byte byte2, byte byte3)
            {
/*3892*/        return abyte0[0] == byte0 && abyte0[1] == byte1 && abyte0[2] == byte2 && abyte0[3] == byte3;
            }

            private static boolean tryEncoding(byte abyte0[], byte byte0, byte byte1)
            {
/*3908*/        return abyte0[0] == byte0 && abyte0[1] == byte1;
            }

            private static boolean tryEncoding(byte abyte0[], byte byte0, byte byte1, byte byte2)
            {
/*3923*/        return abyte0[0] == byte0 && abyte0[1] == byte1 && abyte0[2] == byte2;
            }

            private void pushString(String s, String s1)
                throws SAXException
            {
/*3937*/        char ac[] = s1.toCharArray();
/*3938*/        pushCharArray(s, ac, 0, ac.length);
            }

            private void pushCharArray(String s, char ac[], int i, int j)
                throws SAXException
            {
/*3959*/        pushInput(s);
/*3960*/        sourceType = 1;
/*3961*/        readBuffer = ac;
/*3962*/        readBufferPos = i;
/*3963*/        readBufferLength = j;
/*3964*/        readBufferOverflow = -1;
            }

            private void pushInput(String s)
                throws SAXException
            {
/*3997*/        Object aobj[] = new Object[12];
/*4000*/        if(s != null)
                {
/*4001*/            for(Enumeration enumeration = entityStack.elements(); enumeration.hasMoreElements();)
                    {
/*4003*/                String s1 = (String)enumeration.nextElement();
/*4004*/                if(s1 == s)
/*4005*/                    error("recursive reference to entity", s, null);
                    }

                }
/*4009*/        entityStack.push(s);
/*4012*/        if(sourceType == 0)
                {
/*4013*/            return;
                } else
                {
/*4018*/            aobj[0] = new Integer(sourceType);
/*4019*/            aobj[1] = externalEntity;
/*4020*/            aobj[2] = readBuffer;
/*4021*/            aobj[3] = new Integer(readBufferPos);
/*4022*/            aobj[4] = new Integer(readBufferLength);
/*4023*/            aobj[5] = new Integer(line);
/*4024*/            aobj[6] = new Integer(encoding);
/*4025*/            aobj[7] = new Integer(readBufferOverflow);
/*4026*/            aobj[8] = is;
/*4027*/            aobj[9] = new Integer(currentByteCount);
/*4028*/            aobj[10] = new Integer(column);
/*4029*/            aobj[11] = reader;
/*4032*/            inputStack.push(((Object) (aobj)));
/*4033*/            return;
                }
            }

            private void popInput()
                throws SAXException, IOException
            {
                String s;
/*4056*/        if(externalEntity != null)
/*4057*/            s = externalEntity.getURL().toString();
/*4059*/        else
/*4059*/            s = baseURI;
/*4061*/        switch(sourceType)
                {
/*4063*/        case 3: // '\003'
/*4063*/            if(is != null)
                    {
/*4064*/                if(s != null)
/*4065*/                    handler.endExternalEntity(baseURI);
/*4067*/                is.close();
                    }
                    break;

/*4071*/        case 5: // '\005'
/*4071*/            if(reader != null)
                    {
/*4072*/                if(s != null)
/*4073*/                    handler.endExternalEntity(baseURI);
/*4075*/                reader.close();
                    }
                    break;
                }
/*4082*/        if(inputStack.isEmpty())
                {
/*4083*/            throw new EOFException("no more input");
                } else
                {
/*4086*/            Object aobj[] = (Object[])inputStack.pop();
/*4087*/            entityStack.pop();
/*4089*/            sourceType = ((Integer)aobj[0]).intValue();
/*4090*/            externalEntity = (URLConnection)aobj[1];
/*4091*/            readBuffer = (char[])aobj[2];
/*4092*/            readBufferPos = ((Integer)aobj[3]).intValue();
/*4093*/            readBufferLength = ((Integer)aobj[4]).intValue();
/*4094*/            line = ((Integer)aobj[5]).intValue();
/*4095*/            encoding = ((Integer)aobj[6]).intValue();
/*4096*/            readBufferOverflow = ((Integer)aobj[7]).intValue();
/*4097*/            is = (InputStream)aobj[8];
/*4098*/            currentByteCount = ((Integer)aobj[9]).intValue();
/*4099*/            column = ((Integer)aobj[10]).intValue();
/*4100*/            reader = (Reader)aobj[11];
/*4101*/            return;
                }
            }

            private boolean tryRead(char c)
                throws SAXException, IOException
            {
/*4121*/        char c1 = readCh();
/*4125*/        if(c1 == c)
                {
/*4126*/            return true;
                } else
                {
/*4128*/            unread(c1);
/*4129*/            return false;
                }
            }

            private boolean tryRead(String s)
                throws SAXException, IOException
            {
/*4152*/        char ac[] = s.toCharArray();
/*4158*/        for(int i = 0; i < ac.length; i++)
                {
/*4159*/            char c = readCh();
/*4160*/            if(c != ac[i])
                    {
/*4161*/                unread(c);
/*4162*/                if(i != 0)
/*4163*/                    unread(ac, i);
/*4165*/                return false;
                    }
                }

/*4168*/        return true;
            }

            private boolean tryWhitespace()
                throws SAXException, IOException
            {
/*4184*/        char c = readCh();
/*4185*/        if(isWhitespace(c))
                {
/*4186*/            skipWhitespace();
/*4187*/            return true;
                } else
                {
/*4189*/            unread(c);
/*4190*/            return false;
                }
            }

            private void parseUntil(String s)
                throws SAXException, IOException
            {
/*4208*/        int i = line;
/*4212*/        try
                {
                    char c;
/*4212*/            for(; !tryRead(s); dataBufferAppend(c))
/*4212*/                c = readCh();

                }
/*4216*/        catch(EOFException eofexception)
                {
/*4216*/            error("end of input while looking for delimiter (started on line " + i + ')', ((String) (null)), s);
                }
            }

            private void read8bitEncodingDeclaration()
                throws SAXException, IOException
            {
/*4238*/        readBufferPos = readBufferLength = 0;
/*4241*/        do
                {
/*4241*/            do
                    {
/*4241*/                int i = is.read();
/*4242*/                readBuffer[readBufferLength++] = (char)i;
/*4243*/                switch(i)
                        {
/*4245*/                case 62: // '>'
/*4245*/                    return;

/*4247*/                case -1: 
/*4247*/                    error("end of file before end of XML or encoding declaration.", ((String) (null)), "?>");
                            break;
                        }
                    } while(readBuffer.length != readBufferLength);
/*4251*/            error("unfinished XML or encoding declaration");
                } while(true);
            }

            private void readDataChunk()
                throws SAXException, IOException
            {
/*4280*/        if(readBufferOverflow > -1)
                {
/*4281*/            readBuffer[0] = (char)readBufferOverflow;
/*4282*/            readBufferOverflow = -1;
/*4283*/            readBufferPos = 1;
/*4284*/            sawCR = true;
                } else
                {
/*4286*/            readBufferPos = 0;
/*4287*/            sawCR = false;
                }
/*4291*/        if(sourceType == 5)
                {
/*4292*/            int i = reader.read(readBuffer, readBufferPos, 16384 - readBufferPos);
/*4294*/            if(i < 0)
/*4295*/                readBufferLength = readBufferPos;
/*4297*/            else
/*4297*/                readBufferLength = readBufferPos + i;
/*4298*/            if(readBufferLength > 0)
/*4299*/                filterCR(i >= 0);
/*4300*/            sawCR = false;
/*4301*/            return;
                }
/*4305*/        int j = is.read(rawReadBuffer, 0, 16384);
/*4310*/        if(j > 0)
/*4311*/            switch(encoding)
                    {
/*4314*/            case 9: // '\t'
/*4314*/                copyIso8859_1ReadBuffer(j, '\200');
                        break;

/*4317*/            case 1: // '\001'
/*4317*/                copyUtf8ReadBuffer(j);
                        break;

/*4320*/            case 2: // '\002'
/*4320*/                copyIso8859_1ReadBuffer(j, '\0');
                        break;

/*4325*/            case 3: // '\003'
/*4325*/                copyUcs2ReadBuffer(j, 8, 0);
                        break;

/*4328*/            case 4: // '\004'
/*4328*/                copyUcs2ReadBuffer(j, 0, 8);
                        break;

/*4333*/            case 5: // '\005'
/*4333*/                copyUcs4ReadBuffer(j, 24, 16, 8, 0);
                        break;

/*4336*/            case 6: // '\006'
/*4336*/                copyUcs4ReadBuffer(j, 0, 8, 16, 24);
                        break;

/*4339*/            case 7: // '\007'
/*4339*/                copyUcs4ReadBuffer(j, 16, 24, 0, 8);
                        break;

/*4342*/            case 8: // '\b'
/*4342*/                copyUcs4ReadBuffer(j, 8, 0, 24, 16);
                        break;
                    }
/*4346*/        else
/*4346*/            readBufferLength = readBufferPos;
/*4348*/        readBufferPos = 0;
/*4352*/        if(sawCR)
                {
/*4353*/            filterCR(j >= 0);
/*4354*/            sawCR = false;
/*4357*/            if(readBufferLength == 0 && j >= 0)
/*4358*/                readDataChunk();
                }
/*4361*/        if(j > 0)
/*4362*/            currentByteCount += j;
            }

            private void filterCR(boolean flag)
            {
/*4378*/        readBufferOverflow = -1;
                int j;
/*4381*/        int i = j = readBufferPos;
/*4382*/label0:
/*4382*/        for(; j < readBufferLength; j++)
                {
/*4382*/            switch(readBuffer[j])
                    {
/*4384*/            case 13: // '\r'
/*4384*/                if(j == readBufferLength - 1)
                        {
/*4385*/                    if(flag)
                            {
/*4386*/                        readBufferOverflow = 13;
/*4387*/                        readBufferLength--;
                            } else
                            {
/*4389*/                        readBuffer[i++] = '\n';
                            }
/*4390*/                    break label0;
                        }
/*4391*/                if(readBuffer[j + 1] == '\n')
/*4392*/                    j++;
/*4394*/                readBuffer[i] = '\n';
                        break;

/*4399*/            case 10: // '\n'
/*4399*/            default:
/*4399*/                readBuffer[i] = readBuffer[j];
                        break;
                    }
/*4381*/            i++;
                }

/*4403*/        readBufferLength = i;
            }

            private void copyUtf8ReadBuffer(int i)
                throws SAXException, IOException
            {
/*4420*/        int j = 0;
/*4421*/        int k = readBufferPos;
/*4423*/        boolean flag = false;
/*4432*/        while(j < i) 
                {
/*4432*/            byte byte0 = rawReadBuffer[j++];
                    char c;
/*4437*/            if(byte0 < 0)
                    {
/*4438*/                if((byte0 & 0xe0) == 192)
/*4440*/                    c = (char)((byte0 & 0x1f) << 6 | getNextUtf8Byte(j++, i));
/*4442*/                else
/*4442*/                if((byte0 & 0xf0) == 224)
/*4446*/                    c = (char)((byte0 & 0xf) << 12 | getNextUtf8Byte(j++, i) << 6 | getNextUtf8Byte(j++, i));
/*4449*/                else
/*4449*/                if((byte0 & 0xf8) == 240)
                        {
/*4454*/                    int l = byte0 & 7;
/*4455*/                    l = (l << 6) + getNextUtf8Byte(j++, i);
/*4456*/                    l = (l << 6) + getNextUtf8Byte(j++, i);
/*4457*/                    l = (l << 6) + getNextUtf8Byte(j++, i);
/*4459*/                    if(l <= 65535)
                            {
/*4460*/                        c = (char)l;
                            } else
                            {
/*4462*/                        if(l > 0x10ffff)
/*4463*/                            encodingError("UTF-8 value out of range for Unicode", l, 0);
/*4466*/                        l -= 0x10000;
/*4467*/                        readBuffer[k++] = (char)(0xd800 | l >> 10);
/*4468*/                        readBuffer[k++] = (char)(0xdc00 | l & 0x3ff);
/*4469*/                        continue;
                            }
                        } else
                        {
/*4474*/                    encodingError("invalid UTF-8 byte (check the XML declaration)", 0xff & byte0, j);
/*4478*/                    c = '\0';
                        }
                    } else
                    {
/*4483*/                c = (char)byte0;
                    }
/*4485*/            readBuffer[k++] = c;
/*4486*/            if(c == '\r')
/*4487*/                sawCR = true;
                }
/*4490*/        readBufferLength = k;
            }

            private int getNextUtf8Byte(int i, int j)
                throws SAXException, IOException
            {
                int k;
/*4511*/        if(i < j)
                {
/*4512*/            k = rawReadBuffer[i];
                } else
                {
/*4514*/            k = is.read();
/*4515*/            if(k == -1)
/*4516*/                encodingError("unfinished multi-byte UTF-8 sequence at EOF", -1, i);
                }
/*4522*/        if((k & 0xc0) != 128)
/*4523*/            encodingError("bad continuation of multi-byte UTF-8 sequence", k, i + 1);
/*4528*/        return k & 0x3f;
            }

            private void copyIso8859_1ReadBuffer(int i, char c)
                throws IOException
            {
/*4550*/        int j = 0;
                int k;
/*4550*/        for(k = readBufferPos; j < i; k++)
                {
/*4551*/            char c1 = (char)(rawReadBuffer[j] & 0xff);
/*4552*/            if((c1 & c) != 0)
/*4553*/                throw new CharConversionException("non-ASCII character U+" + Integer.toHexString(c1));
/*4555*/            readBuffer[k] = c1;
/*4556*/            if(c1 == '\r')
/*4557*/                sawCR = true;
/*4550*/            j++;
                }

/*4560*/        readBufferLength = k;
            }

            private void copyUcs2ReadBuffer(int i, int j, int k)
                throws SAXException
            {
/*4581*/        int l = readBufferPos;
/*4583*/        if(i > 0 && i % 2 != 0)
/*4584*/            encodingError("odd number of bytes in UCS-2 encoding", -1, i);
/*4587*/        if(j == 0)
                {
/*4588*/            for(int i1 = 0; i1 < i; i1 += 2)
                    {
/*4589*/                char c = (char)(rawReadBuffer[i1 + 1] << 8);
/*4590*/                c |= 0xff & rawReadBuffer[i1];
/*4591*/                readBuffer[l++] = c;
/*4592*/                if(c == '\r')
/*4593*/                    sawCR = true;
                    }

                } else
                {
/*4596*/            for(int j1 = 0; j1 < i; j1 += 2)
                    {
/*4597*/                char c1 = (char)(rawReadBuffer[j1] << 8);
/*4598*/                c1 |= 0xff & rawReadBuffer[j1 + 1];
/*4599*/                readBuffer[l++] = c1;
/*4600*/                if(c1 == '\r')
/*4601*/                    sawCR = true;
                    }

                }
/*4604*/        readBufferLength = l;
            }

            private void copyUcs4ReadBuffer(int i, int j, int k, int l, int i1)
                throws SAXException
            {
/*4632*/        int j1 = readBufferPos;
/*4634*/        if(i > 0 && i % 4 != 0)
/*4635*/            encodingError("number of bytes in UCS-4 encoding not divisible by 4", -1, i);
/*4639*/        for(int k1 = 0; k1 < i; k1 += 4)
                {
/*4640*/            int l1 = (rawReadBuffer[k1] & 0xff) << j | (rawReadBuffer[k1 + 1] & 0xff) << k | (rawReadBuffer[k1 + 2] & 0xff) << l | (rawReadBuffer[k1 + 3] & 0xff) << i1;
/*4644*/            if(l1 < 65535)
                    {
/*4645*/                readBuffer[j1++] = (char)l1;
/*4646*/                if(l1 == 13)
/*4647*/                    sawCR = true;
                    } else
/*4649*/            if(l1 < 0x10ffff)
                    {
/*4650*/                l1 -= 0x10000;
/*4651*/                readBuffer[j1++] = (char)(0xd8 | l1 >> 10 & 0x3ff);
/*4652*/                readBuffer[j1++] = (char)(0xdc | l1 & 0x3ff);
                    } else
                    {
/*4654*/                encodingError("UCS-4 value out of range for Unicode", l1, k1);
                    }
                }

/*4658*/        readBufferLength = j1;
            }

            private void encodingError(String s, int i, int j)
                throws SAXException
            {
/*4670*/        if(i != -1)
/*4671*/            s = s + " (code: 0x" + Integer.toHexString(i) + ')';
                String s1;
/*4674*/        if(externalEntity != null)
/*4675*/            s1 = externalEntity.getURL().toString();
/*4677*/        else
/*4677*/            s1 = baseURI;
/*4679*/        handler.error(s, s1, -1, j + currentByteCount);
            }

            private void initializeVariables()
            {
/*4693*/        line = 1;
/*4694*/        column = 0;
/*4697*/        dataBufferPos = 0;
/*4698*/        dataBuffer = new char[DATA_BUFFER_INITIAL];
/*4699*/        nameBufferPos = 0;
/*4700*/        nameBuffer = new char[NAME_BUFFER_INITIAL];
/*4703*/        elementInfo = new Hashtable();
/*4704*/        entityInfo = new Hashtable();
/*4705*/        notationInfo = new Hashtable();
/*4709*/        currentElement = null;
/*4710*/        currentElementContent = 0;
/*4713*/        sourceType = 0;
/*4714*/        inputStack = new Stack();
/*4715*/        entityStack = new Stack();
/*4716*/        externalEntity = null;
/*4717*/        tagAttributePos = 0;
/*4718*/        tagAttributes = new String[100];
/*4719*/        rawReadBuffer = new byte[16384];
/*4720*/        readBufferOverflow = -1;
/*4722*/        inLiteral = false;
/*4723*/        expandPE = false;
/*4724*/        peIsError = false;
/*4726*/        inCDATA = false;
/*4728*/        symbolTable = new Object[1087][];
            }

            private void cleanupVariables()
            {
/*4737*/        dataBuffer = null;
/*4738*/        nameBuffer = null;
/*4740*/        elementInfo = null;
/*4741*/        entityInfo = null;
/*4742*/        notationInfo = null;
/*4744*/        currentElement = null;
/*4746*/        inputStack = null;
/*4747*/        entityStack = null;
/*4748*/        externalEntity = null;
/*4750*/        tagAttributes = null;
/*4751*/        rawReadBuffer = null;
/*4753*/        symbolTable = null;
            }

            static 
            {
/* 334*/        attributeTypeHash = new Hashtable(13);
/* 335*/        attributeTypeHash.put("CDATA", new Integer(1));
/* 336*/        attributeTypeHash.put("ID", new Integer(2));
/* 337*/        attributeTypeHash.put("IDREF", new Integer(3));
/* 338*/        attributeTypeHash.put("IDREFS", new Integer(4));
/* 339*/        attributeTypeHash.put("ENTITY", new Integer(5));
/* 340*/        attributeTypeHash.put("ENTITIES", new Integer(6));
/* 341*/        attributeTypeHash.put("NMTOKEN", new Integer(7));
/* 342*/        attributeTypeHash.put("NMTOKENS", new Integer(8));
/* 343*/        attributeTypeHash.put("NOTATION", new Integer(10));
            }
}
