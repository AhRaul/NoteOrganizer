package ru.reliableteam.noteorganizer.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This class describes calendar utils.
 *
 * @author vershov
 * @version 2.0
 */
public class DateUtils {
    private final static int MILLISECONDS = 1000;
    private final static int SECONDS = 60;
    private final static int MINUTES = 60;
    private final static int HOURS = 24;
    private final static String DATE_OUTPUT_FORMAT = "%s/%s/%s";
    private final static String TIME_OUTPUT_FORMAT = "%s:%s";
    private final static Long NULL_DATE = 0L;
    private final static Long BEGIN_DATE = 2678400000L;
    private final static String BEGIN_DATE_STRING = "01/01/1970";
    private final static String BEGIN_TIME = "00:00";

    public static boolean isDateConfigured(String date) {
        Long dateInMills = stringToDate("", date);
        return notEqualsToBeginDate(dateInMills) && !date.equals(BEGIN_DATE_STRING);
    }
    public static boolean isDateConfigured(Long dateInMills) {
        return notEqualsToBeginDate(dateInMills);
    }
    private static boolean notEqualsToBeginDate(Long dateInMills) {
        return !NULL_DATE.equals(dateInMills);
    }

    public static String dateToString(Long dateInMills) {
        Calendar calendar = getCalendar(dateInMills);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String date = getNormalizedDate(day, month, year);
        String time = getNormalizedTime(hour, minute);

        return date + " " + time;
    }

    public static Long stringToDate(String time, String date) {

        Calendar calendar = getCalendar(NULL_DATE);
        if (!time.equals("")) {
            Integer[] timeArr = parseTime(time);
            calendar.set(Calendar.HOUR_OF_DAY, timeArr[0]);
            calendar.set(Calendar.MINUTE, timeArr[1]);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
        }

        if (!date.equals("")) {
            Integer[] dateArr = parseDate(date);
            calendar.set(Calendar.DAY_OF_MONTH, dateArr[0]);
            calendar.set(Calendar.MONTH, dateArr[1] - 1);
            calendar.set(Calendar.YEAR, dateArr[2]);
        }

        return calendar.getTimeInMillis();
    }


    public static String getNormalizedTime(Integer hour, Integer min) {
        String hourS = makeTwoNumbersFromOne(hour);
        String minS = makeTwoNumbersFromOne(min);

        return String.format(TIME_OUTPUT_FORMAT, hourS, minS);
    }
    public static String getNormalizedDate(Integer day, Integer month, Integer year) {
        String dayS = makeTwoNumbersFromOne(day);
        String monthS = makeTwoNumbersFromOne(month + 1);
        String yearS = makeTwoNumbersFromOne(year);
        return String.format(DATE_OUTPUT_FORMAT, dayS, monthS, yearS);
    }
    private static String makeTwoNumbersFromOne(Integer num) {
        return num < 10 ? "0" + num : num.toString();
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
