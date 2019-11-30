package ru.reliableteam.noteorganizer.notes.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ru.reliableteam.noteorganizer.R;

public class NotesBottomDialogFragment extends BottomSheetDialogFragment {

    public static NotesBottomDialogFragment newInstance() {
        return new NotesBottomDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_note_fragment, container, false);

        // get the views and attach the listener

        return view;

    }
}
