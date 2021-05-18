package org.arijit.stock.analyze.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class DateUtil {

    public static Date convertToDate(String date) throws ParseException {
        Date date1=new SimpleDateFormat("MMM-yyyy").parse(date);
        return date1;
    }
    public static long convertToEpochMilli(String date) throws ParseException {
        Date date1=new SimpleDateFormat("MMM-yyyy").parse(date);
        return date1.toInstant().toEpochMilli();

    }

    public static String dateFomratConverter(String date){
        String splitter = " ";
        if(date.contains("'"))
            splitter = "'";
        String[] brk = date.split(splitter);
        String newDate = brk[0].trim()+"-20"+brk[1].trim();
        return newDate;
    }

    public static String dateFormatConverter(long epoch) throws ParseException {
        Date date = new Date(epoch);
        Date date1 = new SimpleDateFormat("MMM-yyyy").parse(date.toString());
        return date1.toString();
    }
}
