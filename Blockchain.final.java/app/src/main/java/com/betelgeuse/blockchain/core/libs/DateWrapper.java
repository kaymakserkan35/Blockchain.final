package com.betelgeuse.blockchain.core.libs;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class DateWrapper {
    public static class TimeZones {
        public static final String TimeZoneOfLondra_UTC = "GMT";
    }

    public static class DateFormats {
        public static final String complexDateFormat = "yyyy-MM-dd HH:mm:ss";
        public static final String simpleDateFormat  = "yyy-MM-dd";
    }

    public Date getDateAsDateObject(){
        return  new Date();
    }
    public String getDate_AsSimpleDateFormatString ( ) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String now = null;
            SimpleDateFormat ISO_8601_FORMAT = null;
            ISO_8601_FORMAT = new SimpleDateFormat(DateFormats.simpleDateFormat);
            now = ISO_8601_FORMAT.format(new Date());
            return now; // output : 18-03-2022
        }
        return null;
    }
    public String getDateUTC_AsSimpleDateFormatString ( ) {
        Calendar cal = new GregorianCalendar();
        Date _date = cal.getTime();
        Date date = convertDateToDateUTC(_date);
        return    getDate_AsSimpleDateFormatString(date);

    }
    public String getDate_AsSimpleDateFormatString (Date date) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            SimpleDateFormat ISO_8601_FORMAT = null;
            ISO_8601_FORMAT = new SimpleDateFormat(DateFormats.simpleDateFormat);
            String now = ISO_8601_FORMAT.format(date);
            return now;
        }
       return null;
        //System.out.printf(now); // 1992-03-06
    }
    public String getYear () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Calendar calendar = Calendar.getInstance();
            return String.valueOf(calendar.getWeekYear());
        }
        return null;
    }
    public String getYearUTC ( ) {
        Date date = new Date();
        int year = convertDateToDateUTC(date).getYear()+1900;
        return String.valueOf(year);
    }
    private Date convertDateToDateUTC (Date date) {
        Date dateUTC = new Date();
        dateUTC.setTime(date.getTime() + date.getTimezoneOffset() * 100 * 60);
        return dateUTC;
    }
    public Date convertSimpleDateFormatStringToDateObject (String date) {

        String year = date.split("-")[0];
        String month = date.split("-")[1];
        String dayOfMonth = date.split("-")[2];
        Date _date =
                new Date(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(dayOfMonth));
        return _date;
    }
    public String getDaysAgoUTC_AsSimpleDateFormatString(int daysAgo) {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_MONTH, (-1*daysAgo));
        Date daysAgoDate = cal.getTime();
        Date date = convertDateToDateUTC(daysAgoDate);
        return    getDate_AsSimpleDateFormatString(date);

    }
}
