package ru.reliableteam.noteorganizer.notes.notes_list_fragment.presenter;

import android.view.View;

import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.notes.notes_list_fragment.view.recycler.IViewHolder;

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

    void sortByTitle();
    void sortByDate();
    void sortByDefault();
}
