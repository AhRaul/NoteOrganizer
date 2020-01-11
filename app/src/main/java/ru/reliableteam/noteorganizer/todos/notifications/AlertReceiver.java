package ru.reliableteam.noteorganizer.todos.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

/**Этот класс описывает порядок действий при выводе уведомлений.
 * Используется только в момент вывода уведомления.
 */
public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        //TODO получить по id задачи заголовок и текст уведомления, отправить через метод getChannelNotification()
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
    }
}
