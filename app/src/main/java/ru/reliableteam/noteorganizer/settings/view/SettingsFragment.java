package ru.reliableteam.noteorganizer.settings.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import ru.reliableteam.noteorganizer.MainActivity;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.settings.presenter.SettingsPresenter;


// todo add to help activity transition
// todo add help info
public class SettingsFragment extends SettingsInitializeFragment implements ISettingsView {
    private final String CLASS_TAG = "SettingsFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new SettingsPresenter(this);

        super.onCreateView(inflater, container, savedInstanceState);

        setNotesCacheSize(presenter.getNotesCacheSize());
        setAppDirPath(presenter.getAppDirPath());

        return root;
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

    @Override
    public void setTodosCacheSize(String size) {
        todosCacheSize.setText(size + " " + getString(R.string.todos_cache_size_prefix));
    }
    public void setLastSyncDate(String date) {
        lastSyncDate.setText(date.equals("") ? getString(R.string.not_sync) : date);
    }

    @Override
    public void showHint(int messageId) {
        Toast.makeText(getContext(), getString(messageId), Toast.LENGTH_LONG).show();
    }

    private void setAppDirPath(String path) {
        String prefix = getResources().getString(R.string.migrated_notes_dir_path_hint);
        appDirPath.setText(prefix + " " + path);
    }
}
