// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   Date.java

package com.icl.saxon.exslt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public final class Date
{

            private Date()
            {
            }

            public static String dateTime()
            {
/*  30*/        GregorianCalendar gregoriancalendar = new GregorianCalendar();
/*  31*/        int i = gregoriancalendar.get(15) + gregoriancalendar.get(16);
/*  33*/        char c = '+';
/*  34*/        if(i < 0)
                {
/*  35*/            c = '-';
/*  36*/            i = -i;
                }
/*  38*/        int j = i / 60000;
/*  39*/        int k = j / 60;
/*  40*/        j %= 60;
                String s;
/*  41*/        for(s = "" + k; s.length() < 2; s = "0" + s);
                String s1;
/*  43*/        for(s1 = "" + j; s1.length() < 2; s1 = "0" + s1);
/*  46*/        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
/*  48*/        String s2 = simpledateformat.format(new java.util.Date());
/*  49*/        return s2 + c + s + ':' + s1;
            }

            public static String date(String s)
            {
/*  59*/        int i = 0;
/*  60*/        if(s.length() >= 1 && (s.charAt(0) == '-' || s.charAt(0) == '+'))
/*  62*/            i = 1;
/*  64*/        if(s.length() >= i + 10)
/*  65*/            return s.substring(0, i + 10);
/*  67*/        else
/*  67*/            return "";
            }

            public static String date()
            {
/*  76*/        return date(dateTime());
            }

            public static String time(String s)
            {
/*  86*/        int i = s.indexOf('T');
/*  87*/        if(i < 0 || i == s.length() - 1)
/*  88*/            return "";
/*  90*/        else
/*  90*/            return s.substring(i + 1);
            }

            public static String time()
            {
/*  99*/        return time(dateTime());
            }

            public static double year(String s)
            {
/* 109*/        if(s.startsWith("-"))
/* 110*/            return (0.0D / 0.0D);
/* 113*/        try
                {
/* 113*/            return (double)Integer.parseInt(s.substring(0, 4));
                }
/* 115*/        catch(Exception exception)
                {
/* 115*/            return (0.0D / 0.0D);
                }
            }

            public static double year()
            {
/* 124*/        return year(dateTime());
            }

            public static boolean leapYear(String s)
            {
/* 133*/        double d = year(s);
/* 134*/        if(Double.isNaN(d))
                {
/* 135*/            return false;
                } else
                {
/* 137*/            int i = (int)d;
/* 138*/            return i % 4 == 0 && (i % 100 != 0 || i % 400 == 0);
                }
            }

            public static boolean leapYear()
            {
/* 146*/        return leapYear(dateTime());
            }

            public static double monthInYear(String s)
            {
/* 156*/        try
                {
/* 156*/            if(s.startsWith("--"))
/* 157*/                return (double)Integer.parseInt(s.substring(2, 4));
/* 159*/            if(s.indexOf('-') != 4)
/* 160*/                return (0.0D / 0.0D);
/* 162*/            else
/* 162*/                return (double)Integer.parseInt(s.substring(5, 7));
                }
/* 165*/        catch(Exception exception)
                {
/* 165*/            return (0.0D / 0.0D);
                }
            }

            public static double monthInYear()
            {
/* 174*/        return monthInYear(date());
            }

            public static String monthName(String s)
            {
/* 184*/        String as[] = {
/* 184*/            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", 
/* 184*/            "November", "December"
                };
/* 186*/        double d = monthInYear(s);
/* 187*/        if(Double.isNaN(d))
/* 188*/            return "";
/* 190*/        else
/* 190*/            return as[(int)d - 1];
            }

            public static String monthName()
            {
/* 199*/        return monthName(date());
            }

            public static String monthAbbreviation(String s)
            {
/* 209*/        String as[] = {
/* 209*/            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", 
/* 209*/            "Nov", "Dec"
                };
/* 211*/        double d = monthInYear(s);
/* 212*/        if(Double.isNaN(d))
/* 213*/            return "";
/* 215*/        else
/* 215*/            return as[(int)d - 1];
            }

            public static String monthAbbreviation()
            {
/* 224*/        return monthAbbreviation(date());
            }

            public static double weekInYear(String s)
            {
/* 233*/        int i = (int)dayInYear(s);
/* 234*/        int j = ((int)dayInWeek(s) + 5) % 7;
/* 236*/        String s1 = s.substring(0, 4) + "-01-01";
/* 237*/        int k = ((int)dayInWeek(s1) + 5) % 7;
/* 238*/        int l = k != 0 ? 7 - k : 0;
/* 240*/        int i1 = ((i - l) + 6) / 7;
/* 242*/        if(l >= 4)
/* 243*/            return (double)(i1 + 1);
/* 245*/        if(i1 > 0)
                {
/* 246*/            return (double)i1;
                } else
                {
/* 249*/            int j1 = Integer.parseInt(s.substring(0, 4)) - 1;
/* 250*/            String s2 = j1 + "-12-31";
/* 252*/            return weekInYear(s2);
                }
            }

            public static double weekInYear()
            {
/* 263*/        return weekInYear(date());
            }

            public static double weekInMonth(String s)
            {
/* 273*/        return (double)(int)((dayInMonth(s) - 1.0D) / 7D + 1.0D);
            }

            public static double weekInMonth()
            {
/* 281*/        return weekInMonth(date());
            }

            public static double dayInYear(String s)
            {
/* 289*/        int i = (int)monthInYear(s);
/* 290*/        int j = (int)dayInMonth(s);
/* 291*/        int ai[] = {
/* 291*/            0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 
/* 291*/            304, 334, 365
                };
/* 304*/        int k = i <= 2 || !leapYear(s) ? 0 : 1;
/* 305*/        return (double)(ai[i - 1] + k + j);
            }

            public static double dayInYear()
            {
/* 313*/        return dayInYear(date());
            }

            public static double dayInMonth(String s)
            {
/* 323*/        try
                {
/* 323*/            if(s.startsWith("---"))
/* 324*/                return (double)Integer.parseInt(s.substring(3, 5));
/* 325*/            if(s.startsWith("--"))
/* 326*/                return (double)Integer.parseInt(s.substring(5, 7));
/* 328*/            else
/* 328*/                return (double)Integer.parseInt(s.substring(8, 10));
                }
/* 331*/        catch(Exception exception)
                {
/* 331*/            return (0.0D / 0.0D);
                }
            }

            public static double dayInMonth()
            {
/* 340*/        return dayInMonth(date());
            }

            public static double dayOfWeekInMonth(String s)
            {
/* 350*/        double d = dayInMonth(s);
/* 351*/        if(Double.isNaN(d))
/* 352*/            return d;
/* 354*/        else
/* 354*/            return (double)(((int)d - 1) / 7 + 1);
            }

            public static double dayOfWeekInMonth()
            {
/* 363*/        return dayOfWeekInMonth(date());
            }

            public static double dayInWeek(String s)
            {
/* 374*/        double d = year(s);
/* 375*/        double d1 = monthInYear(s);
/* 376*/        double d2 = dayInMonth(s);
/* 377*/        if(Double.isNaN(d) || Double.isNaN(d1) || Double.isNaN(d2))
                {
/* 378*/            return (0.0D / 0.0D);
                } else
                {
/* 380*/            GregorianCalendar gregoriancalendar = new GregorianCalendar((int)d, (int)d1 - 1, (int)d2);
/* 385*/            gregoriancalendar.setFirstDayOfWeek(1);
/* 386*/            return (double)gregoriancalendar.get(7);
                }
            }

            public static double dayInWeek()
            {
/* 396*/        return dayInWeek(date());
            }

            public static String dayName(String s)
            {
/* 406*/        String as[] = {
/* 406*/            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
                };
/* 408*/        double d = dayInWeek(s);
/* 409*/        if(Double.isNaN(d))
/* 410*/            return "";
/* 412*/        else
/* 412*/            return as[(int)d - 1];
            }

            public static String dayName()
            {
/* 421*/        return dayName(date());
            }

            public static String dayAbbreviation(String s)
            {
/* 431*/        String as[] = {
/* 431*/            "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
                };
/* 432*/        double d = dayInWeek(s);
/* 433*/        if(Double.isNaN(d))
/* 434*/            return "";
/* 436*/        else
/* 436*/            return as[(int)d - 1];
            }

            public static String dayAbbreviation()
            {
/* 445*/        return dayAbbreviation(date());
            }

            public static double hourInDay(String s)
            {
/* 454*/        int i = s.indexOf('T');
/* 456*/        try
                {
/* 456*/            int j = Integer.parseInt(s.substring(i + 1, i + 3));
/* 457*/            return (double)j;
                }
/* 459*/        catch(Exception exception)
                {
/* 459*/            return (0.0D / 0.0D);
                }
            }

            public static double hourInDay()
            {
/* 468*/        return hourInDay(time());
            }

            public static double minuteInHour(String s)
            {
/* 477*/        int i = s.indexOf('T');
/* 479*/        try
                {
/* 479*/            int j = Integer.parseInt(s.substring(i + 4, i + 6));
/* 480*/            return (double)j;
                }
/* 482*/        catch(Exception exception)
                {
/* 482*/            return (0.0D / 0.0D);
                }
            }

            public static double minuteInHour()
            {
/* 491*/        return minuteInHour(time());
            }

            public static double secondInMinute(String s)
            {
/* 500*/        int i = s.indexOf('T');
/* 502*/        try
                {
/* 502*/            int j = Integer.parseInt(s.substring(i + 7, i + 9));
/* 503*/            return (double)j;
                }
/* 505*/        catch(Exception exception)
                {
/* 505*/            return (0.0D / 0.0D);
                }
            }

            public static double secondInMinute()
            {
/* 514*/        return secondInMinute(time());
            }
}
