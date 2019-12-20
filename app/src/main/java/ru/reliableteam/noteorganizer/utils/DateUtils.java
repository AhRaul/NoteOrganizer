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

    public static String dateToString(Long dateInMills) {
        Calendar calendar = getCalendar(dateInMills);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        return String.format(new Locale("ENG"), DATE_OUTPUT_FORMAT, day, month, year);
    }

    private static Calendar getCalendar(Long dateInMills) {
        Calendar calendar = new GregorianCalendar();
        Date date = new Date(dateInMills);
        calendar.setTime(date);

        return calendar;
    }
}
