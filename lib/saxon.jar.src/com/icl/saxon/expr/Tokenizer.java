// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Tokenizer.java

package com.icl.saxon.expr;

import com.icl.saxon.om.Name;

// Referenced classes of package com.icl.saxon.expr:
//            XPathException

final class Tokenizer
{

            private static final int UNKNOWN = -1;
            public static final int EOF = 0;
            public static final int NAME = 1;
            public static final int FUNCTION = 2;
            public static final int LITERAL = 3;
            public static final int VBAR = 4;
            public static final int SLASH = 5;
            public static final int AT = 6;
            public static final int LSQB = 7;
            public static final int RSQB = 8;
            public static final int LPAR = 9;
            public static final int RPAR = 10;
            public static final int EQUALS = 11;
            public static final int DOT = 12;
            public static final int DOTDOT = 13;
            public static final int STAR = 14;
            public static final int COMMA = 15;
            public static final int SLSL = 16;
            public static final int PREFIX = 17;
            public static final int OR = 18;
            public static final int AND = 19;
            public static final int NUMBER = 20;
            public static final int GT = 21;
            public static final int LT = 22;
            public static final int GE = 23;
            public static final int LE = 24;
            public static final int PLUS = 25;
            public static final int MINUS = 26;
            public static final int MULT = 27;
            public static final int DIV = 28;
            public static final int MOD = 29;
            public static final int DOLLAR = 31;
            public static final int NODETYPE = 32;
            public static final int AXIS = 33;
            public static final int NE = 34;
            public static final int NEGATE = 99;
            public static String tokens[] = {
/*  56*/        "EOF", "<name>", "<function>", "<literal>", "|", "/", "@", "[", "]", "(", 
/*  56*/        ")", "=", ".", "..", "*", ",", "//", "^", "or", "and", 
/*  56*/        "<number>", ">", "<", ">=", "<=", "+", "-", "*", "div", "mod", 
/*  56*/        "--quo--", "$", "<nodetype>()", "<axis>()", "!="
            };
            public int currentToken;
            public String currentTokenValue;
            public double currentNumericValue;
            private int currentTokenStartIndex;
            public String pattern;
            private int patternIndex;
            private int patternLength;
            private int precedingToken;

            Tokenizer()
            {
/*  63*/        currentToken = 0;
/*  64*/        currentTokenValue = null;
/*  65*/        currentNumericValue = 0.0D;
/*  67*/        currentTokenStartIndex = 0;
/*  69*/        patternIndex = 0;
/*  72*/        precedingToken = -1;
            }

            public void tokenize(String s)
                throws XPathException
            {
/*  79*/        currentToken = 0;
/*  80*/        currentTokenValue = null;
/*  81*/        currentTokenStartIndex = 0;
/*  82*/        patternIndex = 0;
/*  83*/        pattern = s;
/*  84*/        patternLength = s.length();
/*  85*/        next();
            }

            public void next()
                throws XPathException
            {
/*  96*/        precedingToken = currentToken;
/*  97*/        currentTokenValue = null;
/*  98*/        currentTokenStartIndex = patternIndex;
/* 100*/label0:
/* 100*/        do
                {
/* 100*/            if(patternIndex >= patternLength)
                    {
/* 101*/                currentToken = 0;
/* 102*/                return;
                    }
/* 104*/            char c = pattern.charAt(patternIndex++);
/* 105*/            switch(c)
                    {
/* 107*/            case 47: // '/'
/* 107*/                if(patternIndex < patternLength && pattern.charAt(patternIndex) == '/')
                        {
/* 109*/                    patternIndex++;
/* 110*/                    currentToken = 16;
/* 111*/                    return;
                        } else
                        {
/* 113*/                    currentToken = 5;
/* 114*/                    return;
                        }

/* 116*/            case 64: // '@'
/* 116*/                currentToken = 6;
/* 117*/                return;

/* 119*/            case 91: // '['
/* 119*/                currentToken = 7;
/* 120*/                return;

/* 122*/            case 93: // ']'
/* 122*/                currentToken = 8;
/* 123*/                return;

/* 125*/            case 40: // '('
/* 125*/                currentToken = 9;
/* 126*/                return;

/* 128*/            case 41: // ')'
/* 128*/                currentToken = 10;
/* 129*/                return;

/* 131*/            case 43: // '+'
/* 131*/                currentToken = 25;
/* 132*/                return;

/* 134*/            case 45: // '-'
/* 134*/                currentToken = 26;
/* 135*/                return;

/* 137*/            case 61: // '='
/* 137*/                currentToken = 11;
/* 138*/                return;

/* 140*/            case 33: // '!'
/* 140*/                if(patternIndex < patternLength && pattern.charAt(patternIndex) == '=')
                        {
/* 142*/                    patternIndex++;
/* 143*/                    currentToken = 34;
/* 144*/                    return;
                        } else
                        {
/* 146*/                    throw new XPathException("\"!\" without \"=\" in expression " + pattern);
                        }

/* 148*/            case 42: // '*'
/* 148*/                if(precedingToken == 0 || precedingToken == 6 || precedingToken == 9 || precedingToken == 7 || precedingToken == 15 || precedingToken == 2 || precedingToken == 33 || isOperator(precedingToken))
/* 156*/                    currentToken = 14;
/* 158*/                else
/* 158*/                    currentToken = 27;
/* 160*/                return;

/* 162*/            case 44: // ','
/* 162*/                currentToken = 15;
/* 163*/                return;

/* 165*/            case 36: // '$'
/* 165*/                currentToken = 31;
/* 167*/                if(patternIndex < patternLength)
                        {
/* 168*/                    char c2 = pattern.charAt(patternIndex);
/* 169*/                    if(" \r\t\n".indexOf(c2) >= 0)
/* 170*/                        throw new XPathException("Whitespace is not allowed after '$' sign");
                        }
/* 173*/                return;

/* 175*/            case 124: // '|'
/* 175*/                currentToken = 4;
/* 176*/                return;

/* 178*/            case 60: // '<'
/* 178*/                if(patternIndex < patternLength && pattern.charAt(patternIndex) == '=')
                        {
/* 180*/                    patternIndex++;
/* 181*/                    currentToken = 24;
/* 182*/                    return;
                        } else
                        {
/* 184*/                    currentToken = 22;
/* 185*/                    return;
                        }

/* 187*/            case 62: // '>'
/* 187*/                if(patternIndex < patternLength && pattern.charAt(patternIndex) == '=')
                        {
/* 189*/                    patternIndex++;
/* 190*/                    currentToken = 23;
/* 191*/                    return;
                        } else
                        {
/* 193*/                    currentToken = 21;
/* 194*/                    return;
                        }

/* 196*/            case 46: // '.'
/* 196*/                if(patternIndex < patternLength && pattern.charAt(patternIndex) == '.')
                        {
/* 198*/                    patternIndex++;
/* 199*/                    currentToken = 13;
/* 200*/                    return;
                        }
/* 202*/                if(patternIndex == patternLength || pattern.charAt(patternIndex) < '0' || pattern.charAt(patternIndex) > '9')
                        {
/* 205*/                    currentToken = 12;
/* 206*/                    return;
                        }
                        // fall through

/* 220*/            case 48: // '0'
/* 220*/            case 49: // '1'
/* 220*/            case 50: // '2'
/* 220*/            case 51: // '3'
/* 220*/            case 52: // '4'
/* 220*/            case 53: // '5'
/* 220*/            case 54: // '6'
/* 220*/            case 55: // '7'
/* 220*/            case 56: // '8'
/* 220*/            case 57: // '9'
/* 220*/                for(; patternIndex < patternLength; patternIndex++)
                        {
/* 220*/                    c = pattern.charAt(patternIndex);
/* 221*/                    if(c != '.' && !Character.isDigit(c))
/* 221*/                        break;
                        }

/* 223*/                currentTokenValue = pattern.substring(currentTokenStartIndex, patternIndex);
/* 225*/                try
                        {
/* 225*/                    currentNumericValue = (new Double(currentTokenValue)).doubleValue();
                        }
/* 227*/                catch(NumberFormatException numberformatexception)
                        {
/* 227*/                    throw new XPathException("Invalid number (" + currentTokenValue + ") in expression " + pattern);
                        }
/* 229*/                currentToken = 20;
/* 230*/                return;

/* 233*/            case 34: // '"'
/* 233*/            case 39: // '\''
/* 233*/                patternIndex = pattern.indexOf(c, patternIndex);
/* 234*/                if(patternIndex < 0)
                        {
/* 235*/                    patternIndex = currentTokenStartIndex + 1;
/* 236*/                    throw new XPathException("Unmatched quote in expression " + pattern);
                        } else
                        {
/* 238*/                    currentTokenValue = pattern.substring(currentTokenStartIndex + 1, patternIndex++).intern();
/* 240*/                    currentToken = 3;
/* 241*/                    return;
                        }

/* 246*/            case 9: // '\t'
/* 246*/            case 10: // '\n'
/* 246*/            case 13: // '\r'
/* 246*/            case 32: // ' '
/* 246*/                currentTokenStartIndex = patternIndex;
/* 247*/                break;

/* 249*/            case 11: // '\013'
/* 249*/            case 12: // '\f'
/* 249*/            case 14: // '\016'
/* 249*/            case 15: // '\017'
/* 249*/            case 16: // '\020'
/* 249*/            case 17: // '\021'
/* 249*/            case 18: // '\022'
/* 249*/            case 19: // '\023'
/* 249*/            case 20: // '\024'
/* 249*/            case 21: // '\025'
/* 249*/            case 22: // '\026'
/* 249*/            case 23: // '\027'
/* 249*/            case 24: // '\030'
/* 249*/            case 25: // '\031'
/* 249*/            case 26: // '\032'
/* 249*/            case 27: // '\033'
/* 249*/            case 28: // '\034'
/* 249*/            case 29: // '\035'
/* 249*/            case 30: // '\036'
/* 249*/            case 31: // '\037'
/* 249*/            case 35: // '#'
/* 249*/            case 37: // '%'
/* 249*/            case 38: // '&'
/* 249*/            case 58: // ':'
/* 249*/            case 59: // ';'
/* 249*/            case 63: // '?'
/* 249*/            case 65: // 'A'
/* 249*/            case 66: // 'B'
/* 249*/            case 67: // 'C'
/* 249*/            case 68: // 'D'
/* 249*/            case 69: // 'E'
/* 249*/            case 70: // 'F'
/* 249*/            case 71: // 'G'
/* 249*/            case 72: // 'H'
/* 249*/            case 73: // 'I'
/* 249*/            case 74: // 'J'
/* 249*/            case 75: // 'K'
/* 249*/            case 76: // 'L'
/* 249*/            case 77: // 'M'
/* 249*/            case 78: // 'N'
/* 249*/            case 79: // 'O'
/* 249*/            case 80: // 'P'
/* 249*/            case 81: // 'Q'
/* 249*/            case 82: // 'R'
/* 249*/            case 83: // 'S'
/* 249*/            case 84: // 'T'
/* 249*/            case 85: // 'U'
/* 249*/            case 86: // 'V'
/* 249*/            case 87: // 'W'
/* 249*/            case 88: // 'X'
/* 249*/            case 89: // 'Y'
/* 249*/            case 90: // 'Z'
/* 249*/            case 92: // '\\'
/* 249*/            case 94: // '^'
/* 249*/            case 96: // '`'
/* 249*/            case 97: // 'a'
/* 249*/            case 98: // 'b'
/* 249*/            case 99: // 'c'
/* 249*/            case 100: // 'd'
/* 249*/            case 101: // 'e'
/* 249*/            case 102: // 'f'
/* 249*/            case 103: // 'g'
/* 249*/            case 104: // 'h'
/* 249*/            case 105: // 'i'
/* 249*/            case 106: // 'j'
/* 249*/            case 107: // 'k'
/* 249*/            case 108: // 'l'
/* 249*/            case 109: // 'm'
/* 249*/            case 110: // 'n'
/* 249*/            case 111: // 'o'
/* 249*/            case 112: // 'p'
/* 249*/            case 113: // 'q'
/* 249*/            case 114: // 'r'
/* 249*/            case 115: // 's'
/* 249*/            case 116: // 't'
/* 249*/            case 117: // 'u'
/* 249*/            case 118: // 'v'
/* 249*/            case 119: // 'w'
/* 249*/            case 120: // 'x'
/* 249*/            case 121: // 'y'
/* 249*/            case 122: // 'z'
/* 249*/            case 123: // '{'
/* 249*/            default:
/* 249*/                if(c < '\200' && !Character.isLetter(c))
/* 250*/                    throw new XPathException("Invalid character (" + c + ") in expression " + pattern);
                        // fall through

/* 255*/            case 95: // '_'
/* 255*/label1:
/* 255*/                for(; patternIndex < patternLength; patternIndex++)
                        {
/* 255*/                    char c1 = pattern.charAt(patternIndex);
/* 256*/                    switch(c1)
                            {
/*  96*/                    case 45: // '-'
/*  96*/                    case 46: // '.'
/*  96*/                    case 95: // '_'
                                break;

/* 258*/                    case 58: // ':'
/* 258*/                        if(patternIndex + 1 < patternLength && pattern.charAt(patternIndex + 1) == ':')
                                {
/* 260*/                            currentTokenValue = pattern.substring(currentTokenStartIndex, patternIndex).intern();
/* 262*/                            currentToken = 33;
/* 263*/                            patternIndex += 2;
/* 264*/                            return;
                                }
/* 266*/                        if(patternIndex + 1 < patternLength && pattern.charAt(patternIndex + 1) == '*')
                                {
/* 268*/                            currentTokenValue = pattern.substring(currentTokenStartIndex, patternIndex).intern();
/* 270*/                            currentToken = 17;
/* 271*/                            patternIndex += 2;
/* 272*/                            return;
                                }
/* 278*/                        break;

/* 280*/                    case 40: // '('
/* 280*/                        currentTokenValue = pattern.substring(currentTokenStartIndex, patternIndex).intern();
/* 282*/                        int i = getBinaryOp(currentTokenValue);
/* 283*/                        if(i != -1)
                                {
/* 284*/                            currentToken = i;
/* 285*/                            return;
                                } else
                                {
/* 287*/                            patternIndex++;
/* 288*/                            currentToken = getFunctionType(currentTokenValue);
/* 289*/                            return;
                                }

/* 291*/                    default:
/* 291*/                        if(c1 < '\200' && !Character.isLetterOrDigit(c1))
/* 292*/                            break label1;
                                break;
                            }
                        }

/* 296*/                break label0;
                    }
                } while(true);
/* 296*/        currentTokenValue = pattern.substring(currentTokenStartIndex, patternIndex).intern();
/* 299*/        for(int j = patternIndex; j < patternLength; j++)
                {
/* 300*/            switch(pattern.charAt(j))
                    {
/*  96*/            default:
                        break;

/* 305*/            case 9: // '\t'
/* 305*/            case 10: // '\n'
/* 305*/            case 13: // '\r'
/* 305*/            case 32: // ' '
/* 305*/                continue;

/* 307*/            case 58: // ':'
/* 307*/                if(j + 1 < patternLength && pattern.charAt(j + 1) == ':')
                        {
/* 308*/                    currentToken = 33;
/* 309*/                    patternIndex = j + 2;
/* 310*/                    return;
                        }
                        break;

/* 314*/            case 40: // '('
/* 314*/                int k = getBinaryOp(currentTokenValue);
/* 315*/                if(k != -1)
                        {
/* 316*/                    currentToken = k;
/* 317*/                    return;
                        } else
                        {
/* 319*/                    currentToken = getFunctionType(currentTokenValue);
/* 320*/                    patternIndex = j + 1;
/* 321*/                    return;
                        }
                    }
/* 325*/            break;
                }

/* 328*/        int l = getBinaryOp(currentTokenValue);
/* 329*/        if(l != -1 && precedingToken != 0 && precedingToken != 6 && precedingToken != 9 && precedingToken != 7 && precedingToken != 15 && precedingToken != 2 && precedingToken != 33 && precedingToken != 31 && !isOperator(precedingToken))
                {
/* 340*/            currentToken = l;
                } else
                {
/* 342*/            currentToken = 1;
/* 343*/            if(!Name.isQName(currentTokenValue))
/* 344*/                throw new XPathException("Invalid QName: " + currentTokenValue);
                }
            }

            private static int getBinaryOp(String s)
            {
/* 358*/        if(s == "and")
/* 358*/            return 19;
/* 359*/        if(s == "or")
/* 359*/            return 18;
/* 360*/        if(s == "div")
/* 360*/            return 28;
/* 361*/        return s != "mod" ? -1 : 29;
            }

            private static int getFunctionType(String s)
            {
/* 372*/        if(s == "node")
/* 372*/            return 32;
/* 373*/        if(s == "text")
/* 373*/            return 32;
/* 374*/        if(s == "comment")
/* 374*/            return 32;
/* 375*/        return s != "processing-instruction" ? 2 : 32;
            }

            private static boolean isOperator(int i)
            {
/* 384*/        return i == 5 || i == 16 || i == 4 || i == 11 || i == 18 || i == 19 || i == 21 || i == 22 || i == 34 || i == 23 || i == 24 || i == 25 || i == 26 || i == 27 || i == 28 || i == 29;
            }

}
