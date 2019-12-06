package ru.reliableteam.noteorganizer.settings;

import android.content.Context;
import android.util.Log;
import android.widget.CompoundButton;

import com.google.android.material.chip.ChipGroup;

import ru.reliableteam.noteorganizer.NoteDaoImpl;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.notes.presenter.BasePresenter;

public class SettingsPresenter extends NoteDaoImpl implements BasePresenter {
    private final String CLASS_TAG = "SettingsPresenter";
    private SharedPreferencesManager appSettings;
    private ISettingsView view;
    private int lastCheckedId = 0;
    private int APP_THEME;

    public SettingsPresenter(Context context, ISettingsView view) {
        appSettings = new SharedPreferencesManager(context);
        this.view = view;
        this.APP_THEME = appSettings.getAppTheme();
    }

    public int getThemeId() {
        if (APP_THEME == R.style.AppTheme)
            lastCheckedId = R.id.light_mode_theme_selection;
        else
            lastCheckedId = R.id.dark_mode_theme_selection;

        return lastCheckedId;
    }

    public boolean isAutoSyncEnabled() {
        return appSettings.isAutoSyncEnabled();
    }

    public void getNotesCacheSize() {
        getNotesCount(this);
    }

    @Override
    public void notifyDatasetChanged() {
        view.setNotesCacheSize(notesCacheSize);
    }

    public void cleanNotesCache() {
        deleteNotes(this);
    }
    // todo refactor
    public ChipGroup.OnCheckedChangeListener themeChangeListener() {
        return new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if (checkedId == -1) {
                    group.check(lastCheckedId);
                    return;
                }
                if (checkedId == lastCheckedId)
                    return;

                int themeId = R.style.AppTheme;
                switch (checkedId) {
                    case R.id.light_mode_theme_selection:
                        themeId = R.style.AppTheme;
                        break;
                    case R.id.dark_mode_theme_selection:
                        themeId = R.style.AppThemeDark;
                        break;
                }
                Log.i(CLASS_TAG, lastCheckedId + " " + checkedId);
                lastCheckedId = checkedId;
                appSettings.setAppTheme(themeId);

                view.reloadActivity();
            }
        };
    }

    public CompoundButton.OnCheckedChangeListener autosyncChangeListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appSettings.setAutoSyncEnabled(isChecked);
                Log.i(CLASS_TAG, appSettings.isAutoSyncEnabled() + "");
            }
        };
    }
}
