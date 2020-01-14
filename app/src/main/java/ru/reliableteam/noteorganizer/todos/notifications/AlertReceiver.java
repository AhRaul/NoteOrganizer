package ru.reliableteam.noteorganizer.todos.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.entity.AppConfig;
import ru.reliableteam.noteorganizer.entity.data_base.DataBase;
import ru.reliableteam.noteorganizer.entity.data_base.dao.TodoDAO;
import ru.reliableteam.noteorganizer.entity.data_base.impl.TodoDaoImpl;
import ru.reliableteam.noteorganizer.todos.model.Todo;

/**Этот класс описывает порядок действий при выводе уведомлений.
 * Используется только в момент вывода уведомления.
 */
public class AlertReceiver extends BroadcastReceiver {

    private TodoDaoImpl todoDao = new TodoDaoImpl();

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        int id = intent.getIntExtra("requestCode", -1);

        if(id != -1) {
            long longId = (long)id;
//            System.out.println("this a notification id is:" + id);
            todoDao.getTodo(longId, notificationHelper);
        } else {
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification("No id", "Cant get notification id");
            notificationHelper.getManager().notify(1, nb.build());
        }
    }

}
