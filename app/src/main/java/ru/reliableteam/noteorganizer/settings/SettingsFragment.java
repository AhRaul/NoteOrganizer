package ru.reliableteam.noteorganizer.settings;

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

public class SettingsFragment extends Fragment implements ISettingsView, View.OnClickListener {
    private final String CLASS_TAG = "SettingsFragment";
    private View root;
    private ChipGroup themeSelector;
    private TextView lastSyncDate, notesCacheSize, todosCacheSize;
    private SwitchMaterial autoSyncSwitcher;
    private MaterialButton cleanNotesCache, cleanTodosCache;

    private SettingsPresenter presenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_settings, container, false);
        presenter = new SettingsPresenter(getContext(), this);
        presenter.getNotesCacheSize();

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
        todosCacheSize = root.findViewById(R.id.todos_cache_size_tv);

        cleanNotesCache = root.findViewById(R.id.notes_cache_size_clean_btn);
        cleanNotesCache.setOnClickListener(this);

        cleanTodosCache = root.findViewById(R.id.todos_photos_cache_size_clean_btn);
    }

    @Override
    public void reloadActivity(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        getActivity().finish();
        startActivity(intent);
    }

    @Override
    public void setNotesCacheSize(int size) {
        notesCacheSize.setText(size + " mb");
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.notes_cache_size_clean_btn:
                presenter.cleanNotesCache();
                break;
        }
    }
}
