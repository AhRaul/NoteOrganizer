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
        return getLong("clickedNoteId", -1L);
    }
    public void setClickedNoteId(long id) {
        putLong("clickedNoteId", id);
    }
    public long getClickedTodoId() {
        return getLong("clickedTodoId", -1L);
    }
    public void setClickedTodoId(long id) {
        putLong("clickedTodoId", id);
    }

    // theme
    public int getAppTheme() {
        return getInt("appTheme", R.style.AppTheme);
    }
    public void setAppTheme(int appThemeId) {
        putInt("appTheme", appThemeId);
    }

    // sync with storage
    public void setLastSyncDate(Long timeInMills) {
        putLong("lastSyncWithStorage", timeInMills);
    }
    public Long getLastSyncDate() {
        return getLong("lastSyncWithStorage", 0L);
    }
    // data directory
    public String getAppDataDirectory() {
        return getString("appDataDirectory", "");
    }
    public void setAppDataDirectory(String path) {
        putString("appDataDirectory", path);
    }

    public boolean isAddingNoteForFirsTime() {
        return getBoolean("isAddingNoteForFirsTime", true);
    }
    public void setAddingNoteForFirsTime(boolean value) {
        putBoolean("isAddingNoteForFirsTime", value);
    }

    // local password
    public String getLocalPassword() {
        return getString("password", "");
    }
    public void setLocalPassword(String password) {
        putString("password", password);
    }



    // BASIC FOR SP
    private Boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }
    private String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }
    private Long getLong(String key, Long defaultValue) {
        return sp.getLong(key, defaultValue);
    }
    private Integer getInt(String key, Integer defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    private void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    private void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }
    private void putLong(String key, Long value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.apply();
    }
    private void putInt(String key, Integer value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
