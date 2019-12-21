package ru.reliableteam.noteorganizer.entity;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.entity.data_base.DataBase;
import ru.reliableteam.noteorganizer.entity.data_base.TodoDAO;
import ru.reliableteam.noteorganizer.todos.model.Todo;

public class TodoDaoImpl {
    private final String CLASS_TAG = "TodoDaoImpl";
    private DataBase dataBase = AppConfig.getInstance().getDatabase();
    private TodoDAO todoDAO = dataBase.getTodoDao();
    private int NO_MESSAGE = 0;

    protected List<Todo> todoList = new ArrayList<>();
    protected Disposable disposable;

    protected void getAll(BasePresenter presenter) {
        disposable = todoDAO.getAll()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        listFromDB -> {
                            todoList.clear();
                            todoList.addAll(listFromDB);
                            Log.i(CLASS_TAG, "todo list size = " + todoList.size());
                            presenter.notifyDatasetChanged(NO_MESSAGE);
                        },
                        Throwable::printStackTrace
                );
    }

    protected void insertTodo(Todo todo, BasePresenter presenter) {
        disposable = Completable.fromAction( () -> todoDAO.insert(todo) )
                .subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                .subscribe(
                        () -> presenter.notifyDatasetChanged(R.string.saved_todo_hint),
                        Throwable::printStackTrace
                );
    }
}
