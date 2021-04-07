package org.arijit.stock.analyze.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
}
