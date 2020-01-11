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

        if (c.before(Calendar.getInstance())) { //если устанавливаемое время уже наступило, то true, устанавливаем на следующий день

            c.add(Calendar.DATE, 1);// при amount = 1 выбирается завтрашний день календаря.
                                            // Например: для выбора сегодняшнего дня, сделать amount = 0.
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
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

        alarmManager.cancel(pendingIntent);
    }
}
