package ru.reliableteam.noteorganizer.notes.presenter;

import android.view.View;

import ru.reliableteam.noteorganizer.notes.view.MyAdapter;

public interface INotesPresenter extends BasePresenter {
    void getNotes ();
    void bindView (MyAdapter.MyViewHolder viewHolder);
    int getItemCount ();
    void clicked (int position);
    void longClicked(int position, View v);
    void searchNotes (String what);
    void changeState();
    void deleteNotes();
    void migrateSelectedNotes();
}
