package com.ctmy.expensemanager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    static Logger log = LoggerFactory.getLogger(DateUtil.class);

    private static Context contextCaller;

    public static Long getEpochTimeStamp(){
        return System.currentTimeMillis();
    }

    public static String getDatePattern(final Context caller){
        contextCaller = caller;
        Format dateFormat = android.text.format.DateFormat.getDateFormat(contextCaller.getApplicationContext());
        String pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
        //String fullPattern = pattern + " HH:mm:ss";
        log.info("getDatePattern " + pattern );

        return pattern;
    }

    public static String getLongDatePattern(final Context caller){
        contextCaller = caller;
        return getDatePattern(contextCaller) + " " + "HH:mm:ss";
    }

    public static String epochToDateString(Long epoch, final String pattern){
        //contextCaller = caller;
        //String pattern = getDatePattern(contextCaller);
        DateFormat format = new SimpleDateFormat(pattern);

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

        String timeZone = TimeZone.getDefault().getID();
        //format.setTimeZone(TimeZone.getTimeZone(timeZone));
        log.info("epochToDateString " + timeZone);
        String formatted = format.format(epoch);

        //return formatted;
        Log.d("New Format", dateFormat.format(epoch));
        log.info("New Format " + dateFormat.format(epoch));
        return dateFormat.format(epoch);
    }
    /* The result is to display it in the views*/
    public static String convertUTCtoCurrentTime(Long dateMillis){
        return null;
    }

    public static Long dateStringToEpoch(String dateString, final String pattern) {
        //contextCaller = caller;
       //String pattern = getDatePattern(contextCaller);
    //TODO: convert date in textview back to epoch GMT/UTC. fix epoch time conversion
        //If for some reason the string is empty, return the current time in milliseconds
        if(dateString == null || dateString.isEmpty()){
            return getEpochTimeStamp();
        }

        try {
            DateFormat sdf = new SimpleDateFormat(pattern);
            //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date dt = sdf.parse(dateString);
            long epochTime = dt.getTime();
            log.info("dateStringToEpock " + String.valueOf(epochTime));
            return epochTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
