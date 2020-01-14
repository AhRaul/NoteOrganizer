package ru.reliableteam.noteorganizer.entity.data_base.interract;

import java.util.Comparator;

import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.notes.model.Note;

public interface INoteDao {
    void saveNote(Note note);
    void updateNote(Note note);
    void deleteNote(Note note);

    /**
     * Get all notes from database and notify recycler about data changed
     * @param presenter - base presenter for recycler {@link BasePresenter}
     */
    void getFromDB(BasePresenter presenter);
    void getNote(long id, BasePresenter presenter);
    void deleteSelectedNote(BasePresenter presenter, Note note);
    void deleteSelectedNotes(BasePresenter presenter);
    void search(BasePresenter presenter, String what);

    /**
     * Migrate notes to .txt format and notify about it
     * @param presenter - base presenter for recycler {@link BasePresenter}
     */
    void migrate(BasePresenter presenter);
    void migrate(Note note, BasePresenter presenter);
    void migrateSelected(BasePresenter presenter);
    SharedPreferencesManager getAppSettings();
    void unsubscribe();

    Note getNote();
    Note getNoteByPosition(int position);
    Integer size();
    void sortNotes(BasePresenter presenter, Comparator comparator);

    boolean wasSelected(Note note);
    void selectNote(Note note);
    void clearSelected();
}
