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

    public static String getTime(String input){
        try{
            int second = Integer.parseInt(input);
            int minute = second / 60; // 분으로 변경

            if(minute >= 60){ // 시간 단위로 변경
                int time = minute / 60;
                minute = minute % 60;
                return time + "시간\n" + minute + "분";
            } else {
                return minute + "분";
            }
        } catch (Exception e){
            return null;
        }

    }
}
