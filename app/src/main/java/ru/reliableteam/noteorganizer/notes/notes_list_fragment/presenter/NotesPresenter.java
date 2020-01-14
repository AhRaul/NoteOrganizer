package ru.reliableteam.noteorganizer.notes.notes_list_fragment.presenter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.chip.ChipGroup;

import java.util.Comparator;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.entity.data_base.impl.NoteDaoImpl;
import ru.reliableteam.noteorganizer.entity.data_base.interract.INoteDao;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.notes.model.Note;
import ru.reliableteam.noteorganizer.notes.notes_list_fragment.view.NotesFragment;
import ru.reliableteam.noteorganizer.notes.notes_list_fragment.view.recycler.IViewHolder;
import ru.reliableteam.noteorganizer.utils.SortListComparator;

/**
 * Base Notes Presenter
 *
 * Seems it has to implement BasePresenter interface and it should be given to Adapter class
 * to interact with data.
 *
 * Generates sample data.
 */

public class NotesPresenter implements INotesPresenter {
    private String CLASS_TAG = "RecyclerViewPresenter";
    private final int NEW_NOTE = -1;
    private final int NO_MESSAGE = 0;

    private final INoteDao noteDao = new NoteDaoImpl();

    private NotesFragment fragmentView;
    private SharedPreferencesManager appSettings;

    enum State {MULTI_SELECTION, SINGLE_SELECTION}
    private State state = State.SINGLE_SELECTION;

    private Comparator<Note> comparator = SortListComparator.getDateComparator();

    public NotesPresenter(NotesFragment view) {
        this.fragmentView = view;
        appSettings = noteDao.getAppSettings();
    }

    public void getNotes() {
        String searchText = fragmentView.getSearchText();
        if (searchText.equals(""))
            noteDao.getFromDB(this);
        else
            searchNotes(searchText);
    }

    @Override
    public void notifyDatasetChanged(int messageId) {
        if (messageId == NO_MESSAGE)
            fragmentView.notifyDataChanged();
        else
            fragmentView.showToast(messageId);
    }

    @Override
    public void bindView(IViewHolder viewHolder) {
        int position = viewHolder.getPos();
        Note note = noteDao.getNoteByPosition(position);
        viewHolder.setNote(note);
    }

    @Override
    public int getItemCount() {
        return noteDao.size();
    }

    @Override
    public void clicked(int position) {
        if (state == State.SINGLE_SELECTION)
            viewNote(position);
        else
            selectNote(position);
    }
    @Override
    public void createNewNote() {
        disableSort();
        disableMultiSelection();
        appSettings.setClickedNoteId(NEW_NOTE);
        fragmentView.viewNote();
    }

    private void viewNote(int position) {
        if (position == NEW_NOTE)
            appSettings.setClickedNoteId(NEW_NOTE);
        else {
            Note note = noteDao.getNoteByPosition(position);
            appSettings.setClickedNoteId(note.id);
        }
        fragmentView.viewNote();
    }
    private void selectNote(int position) {
        Note note = noteDao.getNoteByPosition(position);
        noteDao.selectNote(note);
        fragmentView.setCardSelected(noteDao.wasSelected(note), position);
//        if (selectedNotes.contains(note)) {
//            selectedNotes.remove(note);
//            fragmentView.setCardSelected(false, position);
//        }
//        else {
//            selectedNotes.add(note);
//            fragmentView.setCardSelected(true, position);
//        }
    }

    @Override
    public void longClicked(int position, View v) {
        if (state != State.MULTI_SELECTION) {
            enableMultiSelection();
            selectNote(position);
        }
    }

    @Override
    public void searchNotes(String s) {
        if (s.length() == 0)
            getNotes();
        else
            noteDao.search(this, s);
    }

    @Override
    public void enableSort(){
        disableMultiSelection();
        fragmentView.setSortLayoutVisibility(true);
        fragmentView.setExtraOptionsLayoutVisibility(false);
        changeStateTo(State.SINGLE_SELECTION);
        updateByState();
    }

    @Override
    public void disableSort() {
        fragmentView.setSortLayoutVisibility(false);
        changeStateTo(State.SINGLE_SELECTION);
        updateByState();
    }

    private void sortByTitle() {
        comparator = SortListComparator.getNameComparator();
        sort();
    }

    private void sortByDate() {
        comparator = SortListComparator.getDateComparator();
        sort();
    }

    private void sortByDefault() {
        comparator = SortListComparator.getNumberComparator();
        sort();
    }

    private void sort() {
        noteDao.sortNotes(this, comparator);
    }

    @Override
    public void enableMultiSelection() {
        disableSort();
        fragmentView.setExtraOptionsLayoutVisibility(true);
        changeStateTo(State.MULTI_SELECTION);
    }

    @Override
    public void disableMultiSelection() {
        fragmentView.setExtraOptionsLayoutVisibility(false);
        changeStateTo(State.SINGLE_SELECTION);
        fragmentView.setCardsToDefaultStyle();
        updateByState();
    }

    private void changeStateTo(State newState) {
        state = newState;
    }
    private void updateByState() {
        if (state == State.SINGLE_SELECTION) {
            noteDao.clearSelected();
        }
    }


    public void unsubscribe() {
        noteDao.unsubscribe();
    }

    @Override
    public void deleteNotes() {
        noteDao.deleteSelectedNotes(this);
        fragmentView.setCardsToDefaultStyle();
    }

    @Override
    public void migrateSelectedNotes() {
        noteDao.migrateSelected(this);
    }


    // LISTENERS
    @Override
    public TextWatcher getTextChangeListener(View buttonClear) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setClearButtonVisibility(s);
                searchNotes(s.toString());
            }
            private void setClearButtonVisibility(CharSequence s) {
                if (s.length() == 0) {
                    buttonClear.setVisibility(View.GONE);
                } else {
                    if (buttonClear.getVisibility() != View.VISIBLE)
                        buttonClear.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
    }
    @Override
    public ChipGroup.OnCheckedChangeListener getOnCheckedChangeListener() {
        return (group, checkedId) -> {
            switch (checkedId) {
                case R.id.sort_by_date:
                    sortByDate();
                    break;
                case  R.id.sort_by_title:
                    sortByTitle();
                    break;
                default:
                    sortByDefault();
                    break;
            }
        };
    }
}