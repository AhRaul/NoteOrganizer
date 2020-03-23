package ru.reliableteam.noteorganizer.todos.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import ru.reliableteam.noteorganizer.entity.data_base.impl.TodoDaoImpl;

/**Этот класс описывает порядок действий при выводе уведомлений.
 * Используется только в момент вывода уведомления.
 */
public class AlertReceiver extends BroadcastReceiver {

    private TodoDaoImpl todoDao = new TodoDaoImpl();

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context, intent);
        int id = intent.getIntExtra("requestCode", -1);

        if(id != -1) {      //если id != -1, значит мы получили id задачи, выполняем вызов задачи
            long longId = (long)id;
            todoDao.getTodo(longId, notificationHelper);
        } else {            //если id не получен, выводим сообщение с ошибкой
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification("Error: No id", "AlertReceiver: notification cant get id");
            notificationHelper.getManager().notify(1, nb.build());
        }
    }

}
