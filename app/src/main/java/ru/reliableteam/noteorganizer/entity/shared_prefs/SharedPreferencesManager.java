package ru.reliableteam.noteorganizer.entity.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

import ru.reliableteam.noteorganizer.R;

public class SharedPreferencesManager {
    private final String CLASS_TAG = "SharedPreferencesManager";
    private SharedPreferences sp;

    public SharedPreferencesManager(Context context) {
        sp = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE);
    }

    public long getClickedNoteId() {
        return sp.getLong("clickedNoteId", -1);
    }
    public void setClickedNoteId(long id) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("clickedNoteId", id);
        editor.apply();
    }
    public long getClickedTodoId() {
        return sp.getLong("clickedTodoId", -1);
    }
    public void setClickedTodoId(long id) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("clickedTodoId", id);
        editor.apply();
    }

    // theme
    public int getAppTheme() {
        return sp.getInt("appTheme", R.style.AppTheme);
    }
    public void setAppTheme(int appThemeId) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("appTheme", appThemeId);
        editor.apply();
    }

    // autosync
    public boolean isAutoSyncEnabled() {
        return sp.getBoolean("isAutosycEnabled", false);
    }
    public void setAutoSyncEnabled(boolean isEnabled) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isAutosycEnabled", isEnabled);
        editor.apply();
    }

    // data directory
    public String getAppDataDirectory() {
        return sp.getString("appDataDirectory", "");
    }
    public void setAppDataDirectory(String path) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("appDataDirectory", path);
        editor.apply();
    }
}
