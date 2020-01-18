package ru.reliableteam.noteorganizer.todos.notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import ru.reliableteam.noteorganizer.R;

/**
 * Класс NotificationHelper описывает свойства уведомления.
 * Используется только в сценарии AlarmReceiver -а
 */
public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static String channelName;

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        channelName = getString(R.string.title_todo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    /** Создание канала уведомлений для версии Android Oreo, и более поздней.
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[] {1000, 1000, 1000, 1000});

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String description) {
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.outline_done_all_24);
    }
}
