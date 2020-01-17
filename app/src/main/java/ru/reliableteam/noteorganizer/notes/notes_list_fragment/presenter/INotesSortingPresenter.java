package ru.reliableteam.noteorganizer.notes.notes_list_fragment.presenter;

public interface INotesSortingPresenter {
    void enableSort();
    void disableSort();

    void sortByDate();
    void sortByTitle();
    void sortByDefault();

    void searchNotes(String whatToSearch);
}
