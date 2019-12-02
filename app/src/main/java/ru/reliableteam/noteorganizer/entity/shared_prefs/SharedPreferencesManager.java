package ru.reliableteam.noteorganizer.entity.shared_prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    private final String CLASS_TAG = "SharedPreferencesManager";
    private SharedPreferences sp;

    public SharedPreferencesManager(Context context) {
        sp = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE);
    }

    public int getClickedNoteId() {
        return sp.getInt("clickedNoteId", -1);
    }
    public void setClickedNoteId(int id) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("clickedNoteId", id);
        editor.apply();
    }
}
