package ru.reliableteam.noteorganizer;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.reliableteam.noteorganizer.entity.AppConfig;
import ru.reliableteam.noteorganizer.entity.data_base.NoteDAO;
import ru.reliableteam.noteorganizer.entity.data_base.PersistenceManager;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.notes.model.Note;
import ru.reliableteam.noteorganizer.notes.presenter.BasePresenter;

public class NoteDaoImpl {
    private PersistenceManager persistenceManager = new PersistenceManager();
    private NoteDAO noteDao = persistenceManager.getNoteDao();
    private Disposable disposable = null;

    protected int notesCacheSize = 0;

    // class vars
    protected ArrayList<Note> notesList = new ArrayList<>();
    protected Note note = new Note("", "", 0);

    protected void saveNote(Note note) {
        disposable = Completable.fromAction( () -> noteDao.insert(note) )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        () ->  {
                            System.out.println("SUCCEEDED");
                        },
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
        disposable = noteDao.getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                list -> {
                    notesList.clear();
                    notesList.addAll(list);
                    System.out.println(notesList.size());
                    presenter.notifyDatasetChanged();
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
                    presenter.notifyDatasetChanged();
                },
                Throwable::printStackTrace
        );
    }

    protected void getNotesCount(BasePresenter presenter) {
        disposable = noteDao.getNotesCount()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        count -> {
                            notesCacheSize = count;
                            System.out.println("DB SIZE = " + notesCacheSize);
                            presenter.notifyDatasetChanged();
                        },
                        Throwable::printStackTrace
                );
    }
    protected void deleteNotes(BasePresenter presenter) {
        disposable = Completable.fromAction( () -> noteDao.deleteAll() )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            notesCacheSize = 0;
                            presenter.notifyDatasetChanged();
                        },
                        Throwable::printStackTrace
                );
    }

    protected void unsubscribe() {
        disposable.dispose();
    }
    protected SharedPreferencesManager getAppSettings() { return AppConfig.getInstance().getAppSettings(); }
}
