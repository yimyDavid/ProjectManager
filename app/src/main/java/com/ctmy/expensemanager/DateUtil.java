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

    public static Long getEpochTimeStamp(){
        return System.currentTimeMillis();
    }

    public static String epochToDateString(Long epoch){
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        log.info("New Format " + dateFormat.format(epoch));

        return dateFormat.format(epoch);
    }
    /* The result is to display it in the views*/
    public static String convertUTCtoCurrentTime(Long dateMillis){
        return null;
    }

    public static Long dateStringToEpoch(String dateString, final String pattern) {
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
