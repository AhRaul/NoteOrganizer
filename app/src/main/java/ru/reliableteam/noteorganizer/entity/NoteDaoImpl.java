package ru.reliableteam.noteorganizer.entity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.utils.MigrationManager;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.entity.data_base.NoteDAO;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.notes.model.Note;

public class NoteDaoImpl {
    private AppConfig appConfig = AppConfig.getInstance();
    private NoteDAO noteDao = appConfig.getDatabase().getNoteDao();
    private Disposable disposable = null;

    private int NO_MESSAGE = 0;

    // class vars
    protected ArrayList<Note> notesList = new ArrayList<>();
    protected List<Note> selectedNotes = new ArrayList<>();
    protected Note note = new Note("", "", 0);

    protected void saveNote(Note note) {
        disposable = Completable.fromAction( () -> noteDao.insert(note) )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        () ->  System.out.println("SUCCEEDED"),
                        Throwable::printStackTrace
                );
    }

    protected void updateNote(Note note) {
        disposable = Completable.fromAction( () -> noteDao.update(note) )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        () ->  System.out.println("NOTE UPDATED"),
                        Throwable::printStackTrace
                );
    }

    protected void deleteNote(Note note) {
        disposable = Completable.fromAction( () -> noteDao.delete(note) )
                .subscribeOn(Schedulers.io())
                .subscribe(
                        () ->  System.out.println("NOTE DELETED"),
                        Throwable::printStackTrace
                );
    }

    protected void getFromDB(BasePresenter presenter) {
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

    protected void getNote(int id, BasePresenter presenter) {
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
    protected void deleteSelectedNote(BasePresenter presenter, Note note) {
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
    protected void search(BasePresenter presenter, String what) {
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
    protected void migrate(BasePresenter presenter) {
        disposable = noteDao.getAll()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        list -> migration(list, presenter),
                        Throwable::printStackTrace
                );
    }
    protected void migrateSelected(BasePresenter presenter) {
        migration(selectedNotes, presenter);
        selectedNotes.clear();
        presenter.notifyDatasetChanged(R.string.migrated_hint);
    }

    protected void unsubscribe() {
        disposable.dispose();
    }
    protected SharedPreferencesManager getAppSettings() { return AppConfig.getInstance().getAppSettings(); }

    private void migration(List<Note> list, BasePresenter presenter) {
        MigrationManager manager = new MigrationManager(getAppSettings());
        for (Note note : list) {
            manager.saveToDir(note);
            presenter.notifyDatasetChanged(NO_MESSAGE);
        }
    }
}
