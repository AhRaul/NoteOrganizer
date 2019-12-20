package ru.reliableteam.noteorganizer;

import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;

public class BaseActivity extends AppCompatActivity {

    protected void hideKeyBoard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    protected void setAppTheme() {
        SharedPreferencesManager appSettings = new SharedPreferencesManager(this);
        setTheme(appSettings.getAppTheme());
    }
}
