package com.ctmy.expensemanager;

import com.google.firebase.database.ServerValue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static Long getEpochTimeStamp(){
        return System.currentTimeMillis();
    }

    /*public static Long stringToEpoch(String stringDate){
        return Long.valueOf(stringDate);
    }*/

    public static String epochToDateString(Long epoch, String timezone){
        Date date = new Date(System.currentTimeMillis());

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(date);
        System.out.println(formatted);
        format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
        formatted = format.format(date);
        System.out.println(formatted);

        return formatted;
    }
    /* The result is to display it in the views*/
    public static String convertUTCtoCurrentTime(Long dateMillis){

    }
}
