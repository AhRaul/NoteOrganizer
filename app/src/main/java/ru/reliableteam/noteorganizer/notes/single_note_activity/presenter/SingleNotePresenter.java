package ru.reliableteam.noteorganizer.notes.single_note_activity.presenter;

import android.content.Intent;

import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.entity.data_base.impl.NoteDaoImpl;
import ru.reliableteam.noteorganizer.entity.data_base.interract.INoteDao;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.notes.model.Note;
import ru.reliableteam.noteorganizer.notes.single_note_activity.view.SingleNoteActivity;

public class SingleNotePresenter implements BasePresenter {
    private final int NEW_NOTE = -1;
    private static final int MAX_SIZE = 1000000;
    private final String MIME_TYPE_DEFAULT = "text/plain";

    private SingleNoteActivity view;
    private SharedPreferencesManager appSettings;
    private final INoteDao noteDao = new NoteDaoImpl();

    public SingleNotePresenter(SingleNoteActivity activity) {
        this.view = activity;
        appSettings = noteDao.getAppSettings();
    }

    public void getClickedNote() {
        long id = appSettings.getClickedNoteId();
        if (id != NEW_NOTE) {
            noteDao.getNote(id, this);
        }
    }

    @Override
    public void notifyDatasetChanged(int messageId) {
        Note note = noteDao.getNote();
        view.setNoteText(note.body);
        view.setNoteTitle(note.title);
    }

    public boolean isNewNote() {
        return appSettings.getClickedNoteId() == NEW_NOTE;
    }

    public void deleteNote() {
        Note note = noteDao.getNote();
        noteDao.deleteNote(note);
    }

    public void saveNote() {
        Note note = noteDao.getNote();
        note.body = view.getNoteText();
        note.title = view.getNoteTitle();
        note.dataTime = System.currentTimeMillis();

        if (note.isEmpty()) {
            view.showVerification();
            return;
        }

        if (isNewNote())
            noteDao.saveNote(note);
        else
            noteDao.updateNote(note);

        view.onBackPressed();
    }

    public void shareNote() {
        String textToShare = view.getNoteText();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        shareIntent.setType("text/plain");

        view.startActivity(Intent.createChooser(shareIntent,
                view.getResources().getText(R.string.send_to)));

    }

    public void migrate() {
        Note note = noteDao.getNote();
        noteDao.migrate(note, this);
    }

    public void checkSharedIntent() {
        Intent externalIntent = view.getIntent();

        String action = externalIntent.getAction();
        String type = externalIntent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (MIME_TYPE_DEFAULT.equals(type)) {
                getTextFromIntent(externalIntent);
            }
        }
    }

    private void getTextFromIntent(Intent outterIntent) {
        String sharedText = outterIntent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null && sharedText.length() < MAX_SIZE) {
           view.setNoteText(sharedText);
        }
    }
}