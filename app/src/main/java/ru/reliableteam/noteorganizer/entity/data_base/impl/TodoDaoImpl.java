package ru.reliableteam.noteorganizer.entity.data_base.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.entity.AppConfig;
import ru.reliableteam.noteorganizer.entity.data_base.DataBase;
import ru.reliableteam.noteorganizer.entity.data_base.dao.TodoDAO;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.todos.model.Todo;
import ru.reliableteam.noteorganizer.todos.notifications.Alarm;

public class TodoDaoImpl {
    private final String CLASS_TAG = "TodoDaoImpl";
    private DataBase dataBase = AppConfig.getInstance().getDatabase();
    private TodoDAO todoDAO = dataBase.getTodoDao();
    private int NO_MESSAGE = 0;

    public List<Todo> todoList = new ArrayList<>();
    public Todo todo = new Todo();
    private Disposable disposable;

    public enum STATE {
        ALL, DONE, MISSED, CURRENT;
    }
    private STATE showState = STATE.ALL;

    private Alarm alarm = AppConfig.getInstance().getAlarm();

    public void getTodosByState(BasePresenter presenter) {
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

    public void saveTodo(String title, String description, Long dateTime, BasePresenter presenter) {
        Todo todo = new Todo();
        todo.title = title;
        todo.description = description;
        todo.endDate = dateTime;
        todo.createDate = System.currentTimeMillis();
        todo.isDone = false;
        todo.parentId = null;

        insertTodo(todo, presenter);
    }
    public void saveTodo(Todo todo, BasePresenter presenter) {
        insertTodo(todo, presenter);
    }

    private void insertTodo(Todo todo, BasePresenter presenter) {
        disposable = todoDAO.insert(todo)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        id -> {
                            System.out.println("id = " + id);
                            // todo id for alarm
                            alarm.startAlarm(todo.endDate, 1);
                            getTodosByState(presenter);
                        },
                        Throwable::printStackTrace
                );
    }

    public void getTodo(long id, BasePresenter presenter) {
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

    public void update(String title, String description, Long dateTime, BasePresenter presenter) {
        todo.title = title;
        todo.description = description;
        todo.endDate = dateTime;

        update(todo, presenter);
    }
    public void update(Todo todo, BasePresenter presenter) {
        disposable = Completable.fromAction( () -> todoDAO.update(todo) )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> getTodosByState(presenter),
                        Throwable::printStackTrace
                );
    }

    public void delete(BasePresenter presenter) {
        disposable = Completable.fromAction( () -> todoDAO.delete(todo) )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> getTodosByState(presenter),
                        Throwable::printStackTrace
                );
    }

    public void getCacheSize(Function<Integer, Void> callable) {
        disposable = todoDAO.getCacheSize()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        doneTodosCount -> {
                            System.out.println("GET");
                            callable.apply(doneTodosCount);
                        },
                        Throwable::printStackTrace
                );
    }
    public void cleanCacheSize(Function<Integer, Void> callable) {
        disposable = Completable.fromAction( () -> todoDAO.cleanCache() )
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> getCacheSize(callable),
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
                            System.out.println("GET");
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
                            todoList.clear();
                            todoList.addAll(currentTodoList);
                            presenter.notifyDatasetChanged(NO_MESSAGE);
                        },
                        Throwable::printStackTrace
                );
    }

    public Integer size() {
        return todoList.size();
    }
    public void setTodo(int position) {
        todo = todoList.get(position);
    }
    public Todo getTodo() {
        return todo;
    }
    public void showAll(BasePresenter presenter) {
        showState = TodoDaoImpl.STATE.ALL;
        getTodosByState(presenter);
    }
    public void showDone(BasePresenter presenter) {
        showState = STATE.DONE;
        getTodosByState(presenter);
    }
    public void showCurrent(BasePresenter presenter) {
        showState = STATE.CURRENT;
        getTodosByState(presenter);
    }
    public void showMissed(BasePresenter presenter) {
        showState = STATE.MISSED;
        getTodosByState(presenter);
    }

    public SharedPreferencesManager getAppSettings() {
        return AppConfig.getInstance().getAppSettings();
    }
}
