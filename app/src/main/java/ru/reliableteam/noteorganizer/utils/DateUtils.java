package ru.reliableteam.noteorganizer.utils;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {
    private final static int MILLISECONDS = 1000;
    private final static int SECONDS = 60;
    private final static int MINUTES = 60;
    private final static int HOURS = 24;
    private final static String DATE_OUTPUT_FORMAT = "%d/%d/%d";
    private final static String TIME_OUTPUT_FORMAT = "%s : %s";
    private final static String BEGIN_DATE = "1/0/1970";
    private final static String BEGIN_TIME = "00 : 00";

    public static String dateToString(Long dateInMills) {
        Calendar calendar = getCalendar(dateInMills);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

//  todo delete

//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);

        String date = getNormalizedDate(day, month, year);
//        String time = getNormalizedTime(hour, minute);

        return date;
    }

    public static Long stringToDate(String time, String date) {

        Calendar calendar = getCalendar(0L);
        if (!time.equals("")) {
            Integer[] timeArr = parseTime(time);
            calendar.set(Calendar.HOUR_OF_DAY, timeArr[0]);
            calendar.set(Calendar.MINUTE, timeArr[1]);
        }

        if (!date.equals("")) {
            Integer[] dateArr = parseDate(date);
            calendar.set(Calendar.DAY_OF_MONTH, dateArr[0]);
            calendar.set(Calendar.MONTH, dateArr[1]);
            calendar.set(Calendar.YEAR, dateArr[2]);
        }

        return calendar.getTimeInMillis();
    }


    public static String getNormalizedTime(Integer hour, Integer min) {
        return String.format(new Locale("ENG"), TIME_OUTPUT_FORMAT,
                (hour == 0) ? "00" : hour + "",
                (min == 0) ? "00" : min + ""
        );
    }
    public static String getNormalizedDate(Integer day, Integer month, Integer year) {
        return String.format(new Locale("ENG"), DATE_OUTPUT_FORMAT, day, month, year);
    }
    private static Integer[] parseTime(String time) {
        String[] timeStringArr = time.replace(" ", "").split(":");
        Integer[] timeArr = new Integer[2];
        if (timeStringArr.length > 0) {
            for (int i = 0; i < timeStringArr.length; i++)
                timeArr[i] = Integer.valueOf(timeStringArr[i]);
        }
        return timeArr;
    }
    private static Integer[] parseDate(String date) {
        String[] dateStringArr = date.replace(" ", "").split("/");
        Integer[] dateArr = new Integer[3];
        if (dateStringArr.length > 0) {
            for (int i = 0; i < dateStringArr.length; i++)
                dateArr[i] = Integer.valueOf(dateStringArr[i]);
        }
        return dateArr;
    }

    private static Calendar getCalendar(Long dateInMills) {
        Calendar calendar = new GregorianCalendar();
        Date date = new Date(dateInMills);
        calendar.setTime(date);

        return calendar;
    }
}
