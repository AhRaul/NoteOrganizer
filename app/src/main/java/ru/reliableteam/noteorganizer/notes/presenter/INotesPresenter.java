package ru.reliableteam.noteorganizer.notes.presenter;

import android.view.View;

import ru.reliableteam.noteorganizer.entity.BasePresenter;
import ru.reliableteam.noteorganizer.notes.view.IViewHolder;

public interface INotesPresenter extends BasePresenter {
    void getNotes ();
    void bindView (IViewHolder viewHolder);
    int getItemCount ();
    void clicked (int position);
    void longClicked(int position, View v);
    void searchNotes (String what);
    void enableSort();
    void disableSort();
    void enableMultiSelection();
    void disableMultiSelection();
    void createNewNote();
    void deleteNotes();
    void migrateSelectedNotes();
}
