package ru.reliableteam.noteorganizer.notes.single_note_activity.presenter;

import android.content.Intent;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.entity.NoteDaoImpl;
import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.notes.single_note_activity.view.SingleNoteActivity;

public class SingleNotePresenter extends NoteDaoImpl implements BasePresenter {
    private SingleNoteActivity view;
    private SharedPreferencesManager appSettings;
    private final int NEW_NOTE = -1;

    public SingleNotePresenter (SingleNoteActivity activity) {
        this.view = activity;
        appSettings = getAppSettings();
    }

    public void getClickedNote() {
        int id = appSettings.getClickedNoteId();
        if (id != NEW_NOTE) {
            getNote(id, this);
        }
    }

    @Override
    public void notifyDatasetChanged(int messageId) {
        view.setNoteText(note.body);
        view.setNoteTitle(note.title);
    }

    public boolean isNewNote() { return appSettings.getClickedNoteId() == NEW_NOTE; }

    public void deleteNote() {
        super.deleteNote(note);
    }
    public void saveNote() {
        note.body = view.getNoteText();
        note.title = view.getNoteTitle();
        note.dataTime = System.currentTimeMillis();

        if (isNewNote())
            super.saveNote(note);
        else
            super.updateNote(note);
    }

    public void shareNote() {
        String textToShare = view.getNoteText();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        shareIntent.setType("text/plain");

        view.startActivity(Intent.createChooser(shareIntent,
                view.getResources().getText(R.string.send_to)));

    }
}
