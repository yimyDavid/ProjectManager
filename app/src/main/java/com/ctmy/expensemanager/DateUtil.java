package com.ctmy.expensemanager;

import android.app.Activity;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateUtil {

    private static Activity contextCaller;

    public static Long getEpochTimeStamp(){
        return System.currentTimeMillis();
    }

    public static String epochToDateString(Long epoch, final Activity caller){
        contextCaller = caller;
        Format dateFormat = android.text.format.DateFormat.getDateFormat(caller.getApplicationContext());
        String pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();

        DateFormat format = new SimpleDateFormat(pattern);
        String timeZone = TimeZone.getDefault().getID();
        format.setTimeZone(TimeZone.getTimeZone(timeZone));
        String formatted = format.format(epoch);

        return formatted;
    }
    /* The result is to display it in the views*/
    public static String convertUTCtoCurrentTime(Long dateMillis){
        return null;
    }
}
