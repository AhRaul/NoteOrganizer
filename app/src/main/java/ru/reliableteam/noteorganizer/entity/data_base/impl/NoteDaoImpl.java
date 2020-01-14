package ru.reliableteam.noteorganizer.entity.data_base.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.entity.AppConfig;
import ru.reliableteam.noteorganizer.entity.data_base.dao.NoteDAO;
import ru.reliableteam.noteorganizer.entity.data_base.interract.INoteDao;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.notes.model.Note;
import ru.reliableteam.noteorganizer.utils.MigrationManager;

public class NoteDaoImpl implements INoteDao {
    private final AppConfig appConfig = AppConfig.getInstance();
    private final NoteDAO noteDao = appConfig.getDatabase().getNoteDao();
    private Disposable disposable = null;

    private int NO_MESSAGE = 0;

    // class vars
    private ArrayList<Note> notesList = new ArrayList<>();
    private List<Note> selectedNotes = new ArrayList<>();
    private Note note = new Note();

    @Override
    public Note getNote() {
        return note;
    }
    @Override
    public Note getNoteByPosition(int position) {
        return notesList == null ? new Note() : notesList.get(position);
    }
    @Override
    public Integer size() {
        return notesList == null ? 0 : notesList.size();
    }

    @Override
    public boolean wasSelected(Note note) {
        return selectedNotes.contains(note);
    }

    @Override
    public void selectNote(Note note) {
        if (wasSelected(note))
            selectedNotes.remove(note);
        else
            selectedNotes.add(note);
    }

    @Override
    public void sortNotes(BasePresenter presenter, Comparator comparator) {
        Collections.sort(notesList, comparator);
        presenter.notifyDatasetChanged(NO_MESSAGE);
    }

    @Override
    public void saveNote(Note note) {
        disposable = Completable.fromAction( () -> noteDao.insert(note) )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        () ->  System.out.println("SUCCEEDED"),
                        Throwable::printStackTrace
                );
    }

    @Override
    public void updateNote(Note note) {
        disposable = Completable.fromAction( () -> noteDao.update(note) )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        () ->  System.out.println("NOTE UPDATED"),
                        Throwable::printStackTrace
                );
    }

    @Override
    public void deleteNote(Note note) {
        disposable = Completable.fromAction( () -> noteDao.delete(note) )
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () ->  System.out.println("NOTE DELETED"),
                        Throwable::printStackTrace
                );
    }

    @Override
    public void getFromDB(BasePresenter presenter) {
        disposable = noteDao.getAll()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    list -> {
                        notesList.clear();
                        notesList.addAll(list);
                        System.out.println(notesList.size());
                        presenter.notifyDatasetChanged(NO_MESSAGE);
                    },
                    Throwable::printStackTrace
                );
    }

    @Override
    public void getNote(long id, BasePresenter presenter) {
        disposable = noteDao.getById(id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                foundNote -> {
                    this.note = foundNote;
                    presenter.notifyDatasetChanged(NO_MESSAGE);
                },
                Throwable::printStackTrace
        );
    }
    @Override
    public void deleteSelectedNote(BasePresenter presenter, Note note) {
        disposable = Completable.fromAction( () -> noteDao.delete(note) )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            selectedNotes.remove(note);
                            presenter.notifyDatasetChanged(NO_MESSAGE);
                        },
                        Throwable::printStackTrace
                );
    }

    @Override
    public void deleteSelectedNotes(BasePresenter presenter) {
        for (Note note : selectedNotes)
            deleteSelectedNote(presenter, note);
    }

    @Override
    public void clearSelected() {
        selectedNotes.clear();
    }

    @Override
    public void search(BasePresenter presenter, String what) {
        disposable = noteDao.getAll("%" + what + "%")
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> {
                            notesList.clear();
                            notesList.addAll(list);
                            presenter.notifyDatasetChanged(NO_MESSAGE);
                        },
                        Throwable::printStackTrace
                );
    }
    @Override
    public void migrate(BasePresenter presenter) {
        disposable = noteDao.getAll()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> migration(list, presenter),
                        Throwable::printStackTrace
                );
    }

    @Override
    public void migrate(Note note, BasePresenter presenter) {
        migration(note, presenter);
    }

    @Override
    public void migrateSelected(BasePresenter presenter) {
        migration(selectedNotes, presenter);
        selectedNotes.clear();
        presenter.notifyDatasetChanged(R.string.migrated_hint);
    }
    @Override
    public SharedPreferencesManager getAppSettings() { return AppConfig.getInstance().getAppSettings(); }

    @Override
    public void unsubscribe() {
        disposable.dispose();
    }

    private void migration(Note note, BasePresenter presenter) {
        MigrationManager manager = new MigrationManager(getAppSettings());
        manager.saveToDir(note);
        presenter.notifyDatasetChanged(NO_MESSAGE);
    }

    private void migration(List<Note> list, BasePresenter presenter) {
        MigrationManager manager = new MigrationManager(getAppSettings());
        for (Note note : list) {
            manager.saveToDir(note);
            presenter.notifyDatasetChanged(NO_MESSAGE);
        }
    }
}
