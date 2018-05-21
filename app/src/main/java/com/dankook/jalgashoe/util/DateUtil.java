package com.dankook.jalgashoe.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yeseul on 2018-05-20.
 */

public class DateUtil {

    public static String getCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date current = new Date(System.currentTimeMillis());
        return dateFormat.format(current);
    }

    public static String getDateOnly(String dateInput){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date input = dateFormat.parse(dateInput);
            return dateOnly.format(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
