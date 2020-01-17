package ru.reliableteam.noteorganizer.notes.single_note_activity.view;

import android.os.Bundle;

public class SingleNoteActivity extends SingleNoteActivityInitialize {
    private final String CLASS_TAG = "SingleNoteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initPresenters(this);
        super.onCreate(savedInstanceState);
    }

    public void setNoteTitle(String title) {
        noteTitle.setText(title);
    }
    public void setNoteText(String title) {
        noteText.setText(title);
    }
    public String getNoteTitle() {
        return noteTitle.getText().toString();
    }
    public String getNoteText() {
        return noteText.getText().toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideKeyBoard();
    }

    interface Action {
        void doAction();
    }
}
