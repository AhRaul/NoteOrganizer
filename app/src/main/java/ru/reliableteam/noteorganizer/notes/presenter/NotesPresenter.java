package ru.reliableteam.noteorganizer.notes.presenter;

import android.content.Context;

import java.util.ArrayList;

import ru.reliableteam.noteorganizer.R;
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


public class NotesPresenter {
    private Context context;
    private NotesFragment fragmentView;

    public NotesPresenter(Context context, NotesFragment view) {
        this.context = context;
        this.fragmentView = view;
        getNotes();
    }

    private String CLASS_TAG = "RecyclerViewPresenter";

    // class vars
    private ArrayList<Note> notesList = new ArrayList<>();

    private void getNotes() {
        for (int i = 0; i < 10; i ++) {
            if (i % 3 == 0) {
                notesList.add(
                        new Note(
                                context.getResources().getString(R.string.header_sample),
                                context.getResources().getString(R.string.subheader_sample),
                                0)
                );
            } else {
                notesList.add(
                        new Note(
                                context.getResources().getString(R.string.header_sample),
                                context.getResources().getString(R.string.subheader_sample),
                                R.drawable.ic_launcher_background)
                );
            }
        }
    }

    private void notifyDatasetChanged() {
        fragmentView.notifyDataChanged();
    }

    public void bindView(MyAdapter.MyViewHolder viewHolder) {
        int position = viewHolder.getPos();
        Note note = notesList.get(position);
        viewHolder.setNote(note);
    }

    public int getItemCount() {
        return notesList.size();
    }
}