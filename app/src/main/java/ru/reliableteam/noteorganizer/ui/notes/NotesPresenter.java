package ru.reliableteam.noteorganizer.ui.notes;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;

import ru.reliableteam.noteorganizer.R;


class NotesPresenter {
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

//        notifyDatasetChanged();
    }

    private void notifyDatasetChanged() {
        fragmentView.notifyDataChanged();
    }

    public void bindView(MyAdapter.MyViewHolder viewHolder) {
        int position = viewHolder.getPos();
        Note note = notesList.get(position);
//        iViewHolder.setImageOne(photoModel.src.landscape)
        viewHolder.setNote(note);
    }

    public int getItemCount() {
        return notesList.size();
    }
}