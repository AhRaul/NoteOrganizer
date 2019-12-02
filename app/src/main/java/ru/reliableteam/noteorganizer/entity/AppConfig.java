package ru.reliableteam.noteorganizer.entity;

import android.app.Application;
import androidx.room.Room;
import ru.reliableteam.noteorganizer.entity.data_base.DataBase;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;

/**
 * class to implement singleton variables like:
 *  -   db
 *  -   shared preferences
 *
 *  used for initial config of persistence
 */


public class AppConfig extends Application {

    public static AppConfig instance;

    private DataBase database;
    private SharedPreferencesManager appSettings;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, DataBase.class, "database")
                .build();
        appSettings = new SharedPreferencesManager(this);
    }

    public static AppConfig getInstance() {
        return instance;
    }

    public DataBase getDatabase() {
        return database;
    }
    public SharedPreferencesManager getAppSettings() { return appSettings; }
}
