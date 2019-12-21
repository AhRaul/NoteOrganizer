package ru.reliableteam.noteorganizer.entity;

import android.app.Application;
import android.os.Environment;

import androidx.room.Room;

import java.io.File;

import ru.reliableteam.noteorganizer.entity.dagger.AppComponent;
import ru.reliableteam.noteorganizer.entity.dagger.AppModule;
import ru.reliableteam.noteorganizer.entity.dagger.DaggerAppComponent;
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
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appComponent = generateAppComponent();
        database = Room.databaseBuilder(this, DataBase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
        appSettings = new SharedPreferencesManager(this);
        createDirectory();
    }

    private AppComponent generateAppComponent() {
        return DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
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

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
