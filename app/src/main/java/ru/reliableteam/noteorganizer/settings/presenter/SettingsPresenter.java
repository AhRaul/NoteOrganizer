package ru.reliableteam.noteorganizer.settings.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.CompoundButton;

import com.google.android.material.chip.ChipGroup;

import java.io.File;

import ru.reliableteam.noteorganizer.NoteDaoImpl;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.notes.presenter.BasePresenter;
import ru.reliableteam.noteorganizer.settings.view.ISettingsView;

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

    public String getNotesCacheSize() {
        File f = new File(appSettings.getAppDataDirectory());
        File[] allFiles = f.listFiles();
        long space = 0;
        for (File tmp : allFiles)
            space += tmp.length();

        if (space == 0)
            return "0 bytes";

        if (space / 1024 == 0)
            return String.format("%.0f bytes", space / 1.0);

        if (space / 1024 / 1024 == 0) {
            return String.format("%.3f kb", space / 1024.0);
        }

        return String.format("%.3f mb", space / 1024.0 / 1024.0);
    }

    @Override
    public void notifyDatasetChanged(int messageId) {
        view.setNotesCacheSize(getNotesCacheSize());
    }

    public void cleanNotesCache() {
        File dir = new File(appSettings.getAppDataDirectory());
        File[] files = dir.listFiles();
        for (File f : files)
            f.delete();

        notifyDatasetChanged(R.string.cleaned_hint);
    }

    public String getAppDirPath() {
        return appSettings.getAppDataDirectory().replace("/storage/emulated/0/", "");
    }

    public void saveToTxt() {
        migrate(this);
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
