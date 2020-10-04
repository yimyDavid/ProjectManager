package com.ctmy.expensemanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static String epochToString(Long dateMillis){
        return String.valueOf(dateMillis);
    }

    public static Long stringToEpoch(String stringDate){
        return Long.valueOf(stringDate);
    }

    /* The result is to display it in the views*/
    public static String convertUTCtoCurrentTime(Long dateMillis){
        Date date = new Date(dateMillis);

        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date datey = df.parse(date.toString());
        df.setTimeZone(TimeZone.getDefault());
        String fomrattedDate = df.format(datey);
        return fomrattedDate;
    }
}
