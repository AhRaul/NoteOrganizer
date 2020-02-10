package ru.reliableteam.noteorganizer.settings.presenter;

import com.google.android.material.chip.ChipGroup;

import java.io.File;
import java.util.function.Function;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.reliableteam.noteorganizer.Action;
import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.ParameterizedAction;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.entity.data_base.impl.NoteDaoImpl;
import ru.reliableteam.noteorganizer.entity.data_base.impl.TodoDaoImpl;
import ru.reliableteam.noteorganizer.entity.data_base.interract.INoteDao;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.settings.view.ISettingsView;
import ru.reliableteam.noteorganizer.utils.DateUtils;

public class SettingsPresenter implements BasePresenter {
    private final String CLASS_TAG = "SettingsPresenter";
    private SharedPreferencesManager appSettings;
    private ISettingsView view;
    private TodoDaoImpl todoDao;
    private final INoteDao noteDao = new NoteDaoImpl();

    private int lastCheckedId = 0;
    private int APP_THEME;

    private ParameterizedAction todosCacheSizeListener = new ParameterizedAction() {
        @Override
        public void doAction(String s) {
            view.setTodosCacheSize(s);
        }
    };

    public SettingsPresenter(ISettingsView view) {
        todoDao = new TodoDaoImpl();
        appSettings = todoDao.getAppSettings();
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
    public void getTodosCacheSize() {
        Disposable disposable = Completable.fromAction( () -> todoDao.getCacheSize(todosCacheSizeListener) )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            System.out.println("SET");
                            view.setTodosCacheSize(todoDao.size().toString());
                        },
                        Throwable::printStackTrace
                );
    }
    public void cleanTodosCache() {
        todoDao.cleanCacheSize(todosCacheSizeListener);
        notifyDatasetChanged(R.string.cleaned_hint);
    }

    public void cleanNotesCache() {
        File dir = new File(appSettings.getAppDataDirectory());
        File[] files = dir.listFiles();
        for (File f : files)
            f.delete();

        notifyDatasetChanged(R.string.cleaned_hint);
    }
    @Override
    public void notifyDatasetChanged(int messageId) {
        getTodosCacheSize();
        view.setNotesCacheSize(getNotesCacheSize());
        view.showHint(messageId);
    }

    public String getAppFullDirPath() {
        return appSettings.getAppDataDirectory();
    }
    public String getAppDirPath() {
        return appSettings.getAppDataDirectory().replace("/storage/emulated/0/", "");
    }

    public void saveToTxt() {
        noteDao.migrate(this);
    }

    public ChipGroup.OnCheckedChangeListener themeChangeListener() {
        return new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if (isRechecked(checkedId))
                    return;

                updateAppTheme(checkedId);

                lastCheckedId = checkedId;
                view.reloadActivity();
            }
            private boolean isRechecked(int checkedId) {
                return checkedId == lastCheckedId;
            }
            private void updateAppTheme(int checkedId) {
                int themeId = R.style.AppTheme;
                switch (checkedId) {
                    case R.id.light_mode_theme_selection:
                        themeId = R.style.AppTheme;
                        break;
                    case R.id.dark_mode_theme_selection:
                        themeId = R.style.AppThemeDark;
                        break;
                }
                appSettings.setAppTheme(themeId);
            }
        };
    }

    public void makeSyncWithStorage() {
        noteDao.syncDataWithStorage();
        appSettings.setLastSyncDate(System.currentTimeMillis());
        setLastSyncDate();
    }

    public void setLastSyncDate() {
        Long date = appSettings.getLastSyncDate();
        view.setLastSyncDate(DateUtils.isDateConfigured(date) ? DateUtils.dateToString(date) : "");
    }

    public boolean isTutorialPassed() {
        return !appSettings.isAddingNoteForFirsTime();
    }
    public void enableTutorial() {
        appSettings.setAddingNoteForFirsTime(true);
    }

    public boolean isSecurityEnabled() {
        return appSettings.enterOnPassword();
    }
    public void changeSecuritySettings() {
        appSettings.setEnterOnPassword( !appSettings.enterOnPassword() );
        if (!appSettings.enterOnPassword())
            appSettings.setLocalPassword("");
    }
}
