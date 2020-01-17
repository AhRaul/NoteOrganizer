package ru.reliableteam.noteorganizer.notes.notes_list_fragment.presenter;

import android.text.TextWatcher;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.notes.notes_list_fragment.view.recycler.IViewHolder;

public interface INotesPresenter extends BasePresenter {
    void getNotes ();
    void bindView (IViewHolder viewHolder);
    int getItemCount ();
    void clicked (int position);
    void longClicked(int position, View v);

    void clearSearch();
    void searchNotes (String what);

    void enableMultiSelection();
    void disableMultiSelection();
    void createNewNote();
    void deleteNotes();
    void migrateSelectedNotes();

    void enableSort();
    void disableSort();

    TextWatcher getTextChangeListener(View buttonClear);
    ChipGroup.OnCheckedChangeListener getOnCheckedChangeListener();
    RecyclerView.OnScrollListener getRecyclerScrollListener(FloatingActionButton fab);
}
