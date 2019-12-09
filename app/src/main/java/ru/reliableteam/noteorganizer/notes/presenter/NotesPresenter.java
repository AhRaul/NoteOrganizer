package ru.reliableteam.noteorganizer.notes.presenter;

import android.view.View;
import java.util.ArrayList;
import java.util.List;
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

    private NotesFragment fragmentView;
    private SharedPreferencesManager appSettings;

    enum State { SELECTION, DEFAULT }
    private State state = State.DEFAULT;

    public NotesPresenter(NotesFragment view) {
        this.fragmentView = view;
        appSettings = getAppSettings();
        getNotes();
    }

    public void getNotes() {
        String searchText = fragmentView.getSearchText();
        if (searchText.equals(""))
            getFromDB(this);
        else
            searchNotes(searchText);

    }

    @Override
    public void notifyDatasetChanged(int messageId) {
        if (messageId == 0)
            fragmentView.notifyDataChanged();
        else
            fragmentView.showToast(messageId);
    }

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
        System.out.println("clicked = " + position + " state = " + state);
        if (state == State.DEFAULT)
            viewNote(position);
        else
            selectNote(position);
    }
    private void viewNote(int position) {
        if (position == NEW_NOTE)
            appSettings.setClickedNoteId(NEW_NOTE);
        else {
            Note note = notesList.get(position);
            appSettings.setClickedNoteId(note.id);
        }
        fragmentView.viewNote();
    }
    private void selectNote(int position) {
        Note note = notesList.get(position);
        if (selectedNotes.contains(note)) {
            System.out.println("remove note");
            selectedNotes.remove(note);
            fragmentView.setSelected(false, position);
            System.out.println("selected list size = " + selectedNotes.size());
        }
        else {
            System.out.println("add note");
            selectedNotes.add(note);
            fragmentView.setSelected(true, position);
            System.out.println("selected list size = " + selectedNotes.size());
        }
    }

    @Override
    public void longClicked(int position, View v) {
        System.out.println("state = " + state);
        if (state != State.SELECTION) {
            changeState();
            fragmentView.setExtraOptionsLayoutVisibility(true);
            selectNote(position);
        }
    }

    @Override
    public void searchNotes(String s) {
        if (s.length() == 0)
            getNotes();
        else
            search(this, s);
    }

    @Override
    public void changeState() {
        if (state == State.DEFAULT)
            state = State.SELECTION;
        else {
            state = State.DEFAULT;
            fragmentView.setExtraOptionsLayoutVisibility(false);
            fragmentView.toDefaultStyle();
            selectedNotes.clear();
        }
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
    }

    @Override
    public void deleteNotes() {
        for (Note note : selectedNotes)
            deleteSelectedNote(this, note);
        changeState();
    }

    @Override
    public void migrateSelectedNotes() {
//        for (Note note : selectedNotes)
            migrateSelected(this);
        changeState();
    }
}