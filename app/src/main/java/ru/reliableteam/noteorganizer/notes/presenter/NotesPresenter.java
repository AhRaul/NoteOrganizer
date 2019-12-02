package ru.reliableteam.noteorganizer.notes.presenter;

import android.content.Context;

import ru.reliableteam.noteorganizer.NoteDaoImpl;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.notes.model.Note;
import ru.reliableteam.noteorganizer.notes.view.MyAdapter;
import ru.reliableteam.noteorganizer.notes.view.NotesFragment;

/**
 * Base Notes Presenter
 *
 * Seems it has to implement BasePresenter interface and it should be given to Adapter class
 * to interact with data.
 *
 * Generates sample data.
 * Methods to add:
 *  -   database (insert, add, update, delete)
 *  -   async data getting and setting
 */


public class NotesPresenter extends NoteDaoImpl implements INotesPresenter {
    private String CLASS_TAG = "RecyclerViewPresenter";
    private final int NEW_NOTE = -1;

//    private Context context;
    private NotesFragment fragmentView;
    private SharedPreferencesManager appSettings;

    public NotesPresenter(NotesFragment view) {
        this.fragmentView = view;
        appSettings = getAppSettings();
        getNotes();
    }

    public void getNotes() {
        getFromDB(this);
    }

    @Override
    public void notifyDatasetChanged() { fragmentView.notifyDataChanged(); }

    @Override
    public void bindView(MyAdapter.MyViewHolder viewHolder) {
        int position = viewHolder.getPos();
        Note note = notesList.get(position);
        viewHolder.setNote(note);
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    @Override
    public void clicked(int position) {
        if (position == NEW_NOTE)
            appSettings.setClickedNoteId(NEW_NOTE);
        else {
            Note note = notesList.get(position);
            appSettings.setClickedNoteId(note.id);
        }
        fragmentView.viewNote();
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
    }



}