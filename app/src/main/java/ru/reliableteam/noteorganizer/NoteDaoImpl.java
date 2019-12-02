package ru.reliableteam.noteorganizer;

import android.annotation.SuppressLint;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import ru.reliableteam.noteorganizer.entity.AppConfig;
import ru.reliableteam.noteorganizer.entity.data_base.NoteDAO;
import ru.reliableteam.noteorganizer.entity.data_base.PersistenceManager;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.notes.model.Note;
import ru.reliableteam.noteorganizer.notes.presenter.BasePresenter;
import ru.reliableteam.noteorganizer.notes.presenter.INotesPresenter;
import ru.reliableteam.noteorganizer.notes.presenter.NotesPresenter;

public class NoteDaoImpl {
    private PersistenceManager persistenceManager = new PersistenceManager();
    private NoteDAO noteDao = persistenceManager.getNoteDao();
    protected Disposable disposable = null;
    // class vars
    protected ArrayList<Note> notesList = new ArrayList<>();
    protected Note note = new Note("", "", 0);

    // todo move implementation to another .class
    @SuppressLint("CheckResult")
    public void addNote(Note note) {
        disposable = Completable.fromAction( () -> noteDao.insert(note) )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        () ->  System.out.println("SUCCEEDED"),
                        Throwable::printStackTrace
                );
    }
    @SuppressLint("CheckResult")
    public void getFromDB(BasePresenter presenter) {
        disposable = noteDao.getAll().subscribeOn(Schedulers.io()).subscribe(
                list -> {
                    notesList.addAll(list);
                    presenter.notifyDatasetChanged();
                },
                Throwable::printStackTrace
        );
    }
    @SuppressLint("CheckResult")
    public void getNote(int id, BasePresenter presenter) {
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

    protected void unsubscribe() {
        disposable.dispose();
    }
    protected SharedPreferencesManager getAppSettings() { return AppConfig.getInstance().getAppSettings(); }
}
