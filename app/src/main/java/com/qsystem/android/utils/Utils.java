package com.qsystem.android.utils;

import com.amex.mobileapps.model.SettingsModel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String getCurrentDatetime(){
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        return datetime;
    }

    public static String formatDateTime(String strDateTime,String pattern){
        // pattern : "dd MMMM yyyy HH:mm:ss" => 01 January 2019
        String formatedDateTime;
        Date dateTime;

        try {
            dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDateTime);
        }catch (ParseException e){
            e.printStackTrace();
            try {
                dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2000-01-01 12:00:00");
            }catch (ParseException e1){ return null; }
        }

        formatedDateTime = new SimpleDateFormat(pattern).format(dateTime);
        return  formatedDateTime;

    }

    public static String formatDate(String strDate,String pattern){
        String formatedDate;
        Date date;
        // pattern : "dd MMMM yyyy" => 01 January 2019

        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
        }catch (ParseException e){
            e.printStackTrace();
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01");
            }catch (ParseException e1){ return null; }
        }

        formatedDate = new SimpleDateFormat(pattern).format(date);
        return  formatedDate;
    }




    public static String formatNumber(double number){
        return formatNumber(number,"#,###.##");
    }

    public static String formatNumber(double number,String pattern){
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);
    }
}
