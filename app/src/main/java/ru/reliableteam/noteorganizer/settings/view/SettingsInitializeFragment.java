package ru.reliableteam.noteorganizer.settings.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.settings.presenter.SettingsPresenter;

class SettingsInitializeFragment extends Fragment {
    protected View root;
    protected TextView notesCacheSize;
    protected TextView todosCacheSize;
    protected TextView lastSyncDate;
    protected TextView appDirPath;
    protected MaterialButton cleanNotesCache, cleanTodosCache, migrateToTxt, syncBtn;

    protected SettingsPresenter presenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        System.out.println("SUPER");
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        initUI();

        return root;
    }
    private void initUI() {
        initThemeSelection();
        initSyncWithStorage();
        initNoteCacheSizeSettings();
        initTodosCacheSizeSettings();
        initNotesMigrationSettings();
    }
    private void initThemeSelection() {
        ChipGroup themeSelector = root.findViewById(R.id.theme_mode_selection);
        themeSelector.setOnCheckedChangeListener(presenter.themeChangeListener());
        themeSelector.check(presenter.getThemeId());
    }
    private void initSyncWithStorage() {
        syncBtn = root.findViewById(R.id.sync_button);
        syncBtn.setOnClickListener( v -> presenter.makeSyncWithStorage());

        lastSyncDate = root.findViewById(R.id.last_sync_date);
        presenter.setLastSyncDate();
    }
    private void initNoteCacheSizeSettings() {
        notesCacheSize = root.findViewById(R.id.notes_cache_size_tv);
//        setNotesCacheSize(presenter.getNotesCacheSize());
        cleanNotesCache = root.findViewById(R.id.notes_cache_size_clean_btn);
        cleanNotesCache.setOnClickListener( v -> presenter.cleanNotesCache() );
    }
    private void initTodosCacheSizeSettings() {
        todosCacheSize = root.findViewById(R.id.todos_cache_size_tv);
        presenter.getTodosCacheSize();
        cleanTodosCache = root.findViewById(R.id.todos_photos_cache_size_clean_btn);
        cleanTodosCache.setOnClickListener( v -> presenter.cleanTodosCache());
    }
    private void initNotesMigrationSettings() {
        appDirPath = root.findViewById(R.id.app_dir_path);
//        setAppDirPath(presenter.getAppDirPath());
        appDirPath.setOnClickListener( v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(presenter.getAppFullDirPath());
            intent.setDataAndType(uri, "*/*");
            startActivity(intent);
        });

        migrateToTxt = root.findViewById(R.id.migrate_to_txt);
        migrateToTxt.setOnClickListener( v -> presenter.saveToTxt() );
    }
}
