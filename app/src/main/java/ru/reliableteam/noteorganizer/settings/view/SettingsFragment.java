package ru.reliableteam.noteorganizer.settings.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;

import ru.reliableteam.noteorganizer.MainActivity;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.settings.presenter.SettingsPresenter;

public class SettingsFragment extends Fragment implements ISettingsView, View.OnClickListener {
    private final String CLASS_TAG = "SettingsFragment";
    private View root;
    private ChipGroup themeSelector;
    private TextView lastSyncDate, notesCacheSize, todosCacheSize, appDirPath;
    private SwitchMaterial autoSyncSwitcher;
    private MaterialButton cleanNotesCache, cleanTodosCache, migrateToTxt;

    private SettingsPresenter presenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_settings, container, false);
        presenter = new SettingsPresenter(getContext(), this);

        initUI();

        return root;
    }

    private void initUI() {
        themeSelector = root.findViewById(R.id.theme_mode_selection);
        themeSelector.setOnCheckedChangeListener(presenter.themeChangeListener());
        themeSelector.check(presenter.getThemeId());

        autoSyncSwitcher = root.findViewById(R.id.auto_synchronization_switcher);
        autoSyncSwitcher.setOnCheckedChangeListener(presenter.autosyncChangeListener());
        autoSyncSwitcher.setChecked(presenter.isAutoSyncEnabled());

        lastSyncDate = root.findViewById(R.id.last_sync_date);

        notesCacheSize = root.findViewById(R.id.notes_cache_size_tv);
        setNotesCacheSize(presenter.getNotesCacheSize());

        todosCacheSize = root.findViewById(R.id.todos_cache_size_tv);

        cleanNotesCache = root.findViewById(R.id.notes_cache_size_clean_btn);
        cleanNotesCache.setOnClickListener(this);

        cleanTodosCache = root.findViewById(R.id.todos_photos_cache_size_clean_btn);

        appDirPath = root.findViewById(R.id.app_dir_path);
        setAppDirPath(presenter.getAppDirPath());

        migrateToTxt = root.findViewById(R.id.migrate_to_txt);
        migrateToTxt.setOnClickListener(this);
    }

    @Override
    public void reloadActivity(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        getActivity().finish();
        startActivity(intent);
    }

    @Override
    public void setNotesCacheSize(String size) {
        notesCacheSize.setText(size);
    }

    public void setAppDirPath(String path) {
        String prefix = getResources().getString(R.string.migrated_notes_dir_path_hint);
        appDirPath.setText(prefix + " " + path);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.notes_cache_size_clean_btn:
                presenter.cleanNotesCache();
                break;
            case R.id.migrate_to_txt:
                presenter.saveToTxt();
                break;
        }
    }
}
