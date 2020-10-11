package com.ctmy.expensemanager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    private static Context contextCaller;

    public static Long getEpochTimeStamp(){
        return System.currentTimeMillis();
    }

    public static String getDatePattern(final Context caller){
        contextCaller = caller;
        Format dateFormat = android.text.format.DateFormat.getDateFormat(contextCaller.getApplicationContext());
        String pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
        String fullPattern = pattern + " HH:mm:ss";
        Log.d("pattern", fullPattern);

        return fullPattern;
    }

    public static String epochToDateString(Long epoch, final Context caller){
        contextCaller = caller;
        String pattern = getDatePattern(contextCaller);
        DateFormat format = new SimpleDateFormat(pattern);
        String timeZone = TimeZone.getDefault().getID();
        //format.setTimeZone(TimeZone.getTimeZone(timeZone));
        Log.d("timezone", timeZone);
        String formatted = format.format(epoch);

        return formatted;
    }
    /* The result is to display it in the views*/
    public static String convertUTCtoCurrentTime(Long dateMillis){
        return null;
    }

    public static Long dateStringToEpoch(String dateString, final Activity caller) {
        contextCaller = caller;
       String pattern = getDatePattern(contextCaller);
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
            Log.d("dateStringToEp", String.valueOf(epochTime));
            return epochTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
