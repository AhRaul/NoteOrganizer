package ru.reliableteam.noteorganizer.todos.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Класс Alarm дает доступ приложению к созданию в планировщике системы уведомлений.
 */
public class Alarm {

    private Context context;

    public Alarm(Context context) {
        this.context = context;
    }

    /**Метод startAlarm() создает в AlarmManager-е оповещение
     *
     * @param c Пример возможных данных:
     *          <<c.set(Calendar.HOUR_OF_DAY, hourOfDay);
     *            c.set(Calendar.MINUTE, minute);
     *            c.set(Calendar.SECOND, 0);           0 секунд
     *
     *            c.set(Calendar.DAY_OF_MONTH, 10);   10-е число
     *            c.set(Calendar.MONTH, 0);            0 = январь
     *            c.set(Calendar.YEAR, 2020);>>        2020 год
     * @param requestCode id уведомления.
     */
    private void startAlarm(Calendar c, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this.context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, requestCode, intent, 0);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }

    /**
     * перегруженный startAlarm(Calendar c, int requestCode)
     *
     * @param dateOutputFormat  "%s/%s/%s"  день/месяц/год
     * @param timeOutputFormat  "%s:%s";    часы:минуты
     * @param requestCode        id уведомления.
     */
    private void startAlarm(String dateOutputFormat, String timeOutputFormat, int requestCode) {
        Calendar c = importStringDateAndTimeToCalendar(dateOutputFormat, timeOutputFormat);
        AlarmManager alarmManager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this.context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, requestCode, intent, 0);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }
    }

    /**
     * Возвращает календарь, заполненный данными даты и времени
     * Конвертирует "%s/%s/%s" "%s:%s" в Calendar
     *
     * @param dateOutputFormat  "%s/%s/%s"  день/месяц/год
     * @param timeOutputFormat  "%s:%s"     часы:минуты
     * @return calendar;
     */
    private Calendar importStringDateAndTimeToCalendar(String dateOutputFormat, String timeOutputFormat) {
        String[] subStrDate;
        String[] subStrTime;
        String delimeterDate = "/";
        String delimeterTime = ":";
        subStrDate = dateOutputFormat.split(delimeterDate);
        subStrTime = timeOutputFormat.split(delimeterTime);

        Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(subStrDate[0]));
            calendar.set(Calendar.MONTH, (Integer.parseInt(subStrDate[1])-1));
            calendar.set(Calendar.YEAR, Integer.parseInt(subStrDate[2]));

            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(subStrTime[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(subStrTime[1]));
            calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

    /**
     * Метод cancelAlarm() удаляет уведомление из планировщика по его ID (requestCode)
     *
     * @param requestCode Private request code for the sender.
     */
    private void cancelAlarm(int requestCode) {
        AlarmManager alarmManager =(AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this.context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, requestCode, intent, 0);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
