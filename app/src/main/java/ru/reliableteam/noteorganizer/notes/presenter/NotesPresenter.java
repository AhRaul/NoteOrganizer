package ru.reliableteam.noteorganizer.notes.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.entity.data_base.NoteDAO;
import ru.reliableteam.noteorganizer.entity.data_base.PersistenceManager;
import ru.reliableteam.noteorganizer.notes.model.Note;
import ru.reliableteam.noteorganizer.notes.view.MyAdapter;
import ru.reliableteam.noteorganizer.notes.view.NotesFragment;

/**
 * Base Notes Presenter
 *
 * Seems it has to implement BasePresenter interface and it should be given to Adapter class
 * to interact with data.
 *
 * Generates sample data.
 * Methods to add:
 *  -   database (insert, add, update, delete)
 *  -   async data getting and setting
 */


public class NotesPresenter {
    private String CLASS_TAG = "RecyclerViewPresenter";

    private Context context;
    private NotesFragment fragmentView;

    private PersistenceManager persistenceManager = new PersistenceManager();
    private NoteDAO noteDao = persistenceManager.getNoteDao();
    private Disposable disposable = null;

    public NotesPresenter(Context context, NotesFragment view) {
        this.context = context;
        this.fragmentView = view;
        getNotes();
    }

    // class vars
    private ArrayList<Note> notesList = new ArrayList<>();

    private void getNotes() {
//        for (int i = 0; i < 10; i ++) {
//            if (i % 3 == 0) {
//                notesList.add(
//                        new Note(
//                                context.getResources().getString(R.string.header_sample),
//                                context.getResources().getString(R.string.subheader_sample),
//                                0)
//                );
//            } else {
//                notesList.add(
//                        new Note(
//                                context.getResources().getString(R.string.header_sample),
//                                context.getResources().getString(R.string.subheader_sample),
//                                R.drawable.ic_launcher_background)
//                );
//            }
//        }
        //addNotes();
        getFromDB();
    }

    // todo move implementation to another .class
    @SuppressLint("CheckResult")
    private void addNotes() {
        disposable = Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                for (Note n : notesList)
                    noteDao.insert(n);
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe(
                () -> { System.out.println("SUCCEEDED"); },
                Throwable::printStackTrace
        );
    }
    // todo move implementation to another .class
    @SuppressLint("CheckResult")
    private void getFromDB() {
        disposable = noteDao.getAll().subscribeOn(Schedulers.io()).subscribe(
                list -> {
                    for (Note n : list)
                        System.out.println(n);
                    notesList.addAll(list);
                    notifyDatasetChanged();
                },
                Throwable::printStackTrace
        );
    }

    private void notifyDatasetChanged() {
        fragmentView.notifyDataChanged();
    }

    public void bindView(MyAdapter.MyViewHolder viewHolder) {
        int position = viewHolder.getPos();
        Note note = notesList.get(position);
        viewHolder.setNote(note);
    }

    public int getItemCount() {
        return notesList.size();
    }

    // todo use in Fragment.onDetach()
    public void unsubscribe() {
        disposable.dispose();
    }
}