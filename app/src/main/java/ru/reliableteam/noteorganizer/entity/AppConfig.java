package ru.reliableteam.noteorganizer.entity;

import android.Manifest;
import android.app.Application;
import android.os.Environment;

import androidx.room.Room;

import java.io.File;

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

    private static AppConfig instance;
    private DataBase database;
    private SharedPreferencesManager appSettings;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, DataBase.class, "database")
                .allowMainThreadQueries()
                .addMigrations(NoteDaoMigrations.MIGRATION_1_2)
                .build();
        appSettings = new SharedPreferencesManager(this);
        createDirectory();
    }

    public static AppConfig getInstance() {
        return instance;
    }

    public DataBase getDatabase() {
        return database;
    }
    public SharedPreferencesManager getAppSettings() { return appSettings; }

    private void createDirectory() {
        System.out.println("CREATING DIR");
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File myDirectory = new File(storageDir, "/ru.reliableteam.noteorganaizer");
        appSettings.setAppDataDirectory(myDirectory.getAbsolutePath());

        if(!myDirectory.exists()) {
            myDirectory.mkdir();
        }
    }
}
