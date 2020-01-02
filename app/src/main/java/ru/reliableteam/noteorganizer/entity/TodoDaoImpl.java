package ru.reliableteam.noteorganizer.entity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.entity.data_base.DataBase;
import ru.reliableteam.noteorganizer.entity.data_base.TodoDAO;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.todos.model.Todo;

public class TodoDaoImpl {
    private final String CLASS_TAG = "TodoDaoImpl";
    private DataBase dataBase = AppConfig.getInstance().getDatabase();
    private TodoDAO todoDAO = dataBase.getTodoDao();
    private int NO_MESSAGE = 0;

    protected List<Todo> todoList = new ArrayList<>();
    protected Todo todo = new Todo();
    private Disposable disposable;

    protected enum STATE {
        ALL, DONE, MISSED, CURRENT;
    }
    protected STATE showState = STATE.ALL;

    protected void getTodosByState(BasePresenter presenter) {
        switch (showState) {
            case ALL:
                getAll(presenter);
                break;
            case DONE:
                getDoneTodos(presenter);
                break;
            case MISSED:
                getMissedTodos(presenter);
                break;
            case CURRENT:
                getCurrentTodos(presenter);
                break;
        }
    }

    protected void saveTodo(String title, String description, Long dateTime, BasePresenter presenter) {
        Todo todo = new Todo();
        todo.title = title;
        todo.description = description;
        todo.endDate = dateTime;
        todo.createDate = System.currentTimeMillis();
        todo.isDone = false;
        todo.parentId = null;

        insertTodo(todo, presenter);
    }
    protected void saveTodo(Todo todo, BasePresenter presenter) {
        insertTodo(todo, presenter);
    }

    private void insertTodo(Todo todo, BasePresenter presenter) {
        disposable = Completable.fromAction( () -> todoDAO.insert(todo) )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> getTodosByState(presenter),
                        Throwable::printStackTrace
                );
    }

    protected void getTodo(long id, BasePresenter presenter) {
        System.out.println("id = " + id);
        disposable = todoDAO.getById(id)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        foundTodo -> {
                            this.todo = foundTodo;
                            presenter.notifyDatasetChanged(NO_MESSAGE);
                        },
                        Throwable::printStackTrace
                );
    }

    protected void update(Todo todo, BasePresenter presenter) {
        disposable = Completable.fromAction( () -> todoDAO.update(todo) )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> getTodosByState(presenter),
                        Throwable::printStackTrace
                );
    }

    protected void delete(Todo todo, BasePresenter presenter) {
        disposable = Completable.fromAction( () -> todoDAO.delete(todo) )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> presenter.notifyDatasetChanged(NO_MESSAGE),
                        Throwable::printStackTrace
                );
    }

    /**
     * Getting todos from data-source
     * @param presenter, which has
     */
    private void getAll(BasePresenter presenter) {
        disposable = todoDAO.getAll()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        listFromDB -> {
                            todoList.clear();
                            todoList.addAll(listFromDB);
                            presenter.notifyDatasetChanged(NO_MESSAGE);
                        },
                        Throwable::printStackTrace
                );
    }
    private void getDoneTodos(BasePresenter presenter) {
        disposable = todoDAO.getDoneTodos()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        doneTodos -> {
                            todoList.clear();
                            todoList.addAll(doneTodos);
                            presenter.notifyDatasetChanged(NO_MESSAGE);
                        },
                        Throwable::printStackTrace
                );
    }
    private void getMissedTodos(BasePresenter presenter) {
        long timeNow = System.currentTimeMillis();
        disposable = todoDAO.getMissedTodos(timeNow)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        missedTodoList -> {
                            missedTodoList.stream().forEach(System.out::println);
                            todoList.clear();
                            todoList.addAll(missedTodoList);
                            presenter.notifyDatasetChanged(NO_MESSAGE);
                        },
                        Throwable::printStackTrace
                );
    }
    private void getCurrentTodos(BasePresenter presenter) {
        long timeNow = System.currentTimeMillis();
        System.out.println(timeNow);
        disposable = todoDAO.getCurrentTodos(timeNow)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        currentTodoList -> {
                            currentTodoList.stream().forEach(System.out::println);
                            todoList.clear();
                            todoList.addAll(currentTodoList);
                            presenter.notifyDatasetChanged(NO_MESSAGE);
                        },
                        Throwable::printStackTrace
                );
    }
    protected SharedPreferencesManager getAppSettings() { return AppConfig.getInstance().getAppSettings(); }

}
