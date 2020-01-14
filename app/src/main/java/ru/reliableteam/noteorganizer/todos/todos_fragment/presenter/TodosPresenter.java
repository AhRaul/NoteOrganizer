package ru.reliableteam.noteorganizer.todos.todos_fragment.presenter;

import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.reliableteam.noteorganizer.entity.data_base.impl.TodoDaoImpl;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.todos.model.Todo;
import ru.reliableteam.noteorganizer.todos.todos_fragment.TodoRequestCodes;
import ru.reliableteam.noteorganizer.todos.todos_fragment.view.ITodosFragment;
import ru.reliableteam.noteorganizer.todos.todos_fragment.view.recycler.IViewHolder;
import ru.reliableteam.noteorganizer.utils.DateUtils;

@InjectViewState
public class TodosPresenter extends MvpPresenter<ITodosFragment> implements ITodoPresenter, TodoRequestCodes {
    private static final int NEW_TODO = -1;
    private SharedPreferencesManager appSettings;
    private TodoDaoImpl todoDao;

    public TodosPresenter() {
        todoDao = new TodoDaoImpl();
        appSettings = todoDao.getAppSettings();
    }

    @Override
    public void notifyDatasetChanged(int messageId) {
        System.out.println("DATA CHANGED");
        getViewState().notifyDataChanged();
    }

    @Override
    public void getTodos() {
        todoDao.getTodosByState(this);
    }

    @Override
    public void bindView(IViewHolder viewHolder) {
        int position = viewHolder.getPos();
        viewHolder.setTodo(todoDao.todoList.get(position));
    }

    @Override
    public int getItemCount() {
        return todoDao.size();
    }

    @Override
    public void saveTodo(String title, String description, Long dateTime, boolean timeChosen) {
        dateTime = timeChosen ? dateTime : 0;
        System.out.println(DateUtils.dateToString(dateTime));
        todoDao.saveTodo(title, description, dateTime, this);
    }

    @Override
    public void editTodo(String title, String description, Long dateTime, boolean timeChosen, int action) {
        if (action == ACTION_UPDATE)
            todoDao.update(title, description, dateTime, this);
        if (action == ACTION_DELETE)
            deleteTodo();
    }

    @Override
    public void longClicked(int position) {
        todoDao.setTodo(position);
        getViewState().showConfirmationDialog();
    }
    @Override
    public void deleteTodo() {
        todoDao.delete(this);
    }

    @Override
    public void clicked(int position) {
        todoDao.setTodo(position);
        appSettings.setClickedTodoId(todoDao.getTodo().id);
        getViewState().viewTodo();
    }

    public void newTodo() {
        appSettings.setClickedTodoId(NEW_TODO);
    }

    @Override
    public void makeTodoDone(int position, boolean isDone) {
        todoDao.setTodo(position);
        Todo todo = todoDao.getTodo();
        todo.isDone = isDone;
        todoDao.update(todo, this);
    }

    public void showAllTodos() {
        todoDao.showAll(this);
    }
    public void showDoneTodos() {
        todoDao.showDone(this);
    }
    public void showCurrentTodos() {
        todoDao.showCurrent(this);
    }
    public void showMissedTodos() {
        todoDao.showMissed(this);
    }
}
