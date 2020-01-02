package ru.reliableteam.noteorganizer.todos.todos_fragment.presenter;

import ru.reliableteam.noteorganizer.entity.TodoDaoImpl;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.todos.model.Todo;
import ru.reliableteam.noteorganizer.todos.todos_fragment.TodoRequestCodes;
import ru.reliableteam.noteorganizer.todos.todos_fragment.view.TodosFragment;
import ru.reliableteam.noteorganizer.todos.todos_fragment.view.recycler.IViewHolder;

public class TodosPresenter extends TodoDaoImpl implements ITodoPresenter, TodoRequestCodes {
    private static final int NEW_TODO = -1;
    private SharedPreferencesManager appSettings;
    private TodosFragment view;

    public TodosPresenter(TodosFragment view) {
        this.view = view;
        appSettings = getAppSettings();
    }

    @Override
    public void notifyDatasetChanged(int messageId) {
        System.out.println("DATA CHANGED");
        view.notifyDataChanged();
    }

    @Override
    public void getTodos() {
        getTodosByState(this);
    }

    @Override
    public void bindView(IViewHolder viewHolder) {
        int position = viewHolder.getPos();
        viewHolder.setTodo(todoList.get(position));
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    @Override
    public void saveTodo(String title, String description, Long dateTime, boolean timeChosen) {
        dateTime = timeChosen ? dateTime : 0;
        saveTodo(title, description, dateTime, this);
    }

    @Override
    public void editTodo(String title, String description, Long dateTime, boolean timeChosen, int action) {
        if (action == ACTION_UPDATE)
            update(title, description, dateTime, this);
        if (action == ACTION_DELETE)
            deleteTodo();
    }

    @Override
    public void longClicked(int position) {
        todo = todoList.get(position);
        view.showConfirmationDialog();
    }
    @Override
    public void deleteTodo() {
        delete(todo, this);
    }

    @Override
    public void clicked(int position) {
        todo = todoList.get(position);
        appSettings.setClickedTodoId(todo.id);
        view.viewTodo();
    }

    public void newTodo() {
        appSettings.setClickedTodoId(NEW_TODO);
    }

    @Override
    public void makeTodoDone(int position, boolean isDone) {
        Todo todo = todoList.get(position);
        todo.isDone = isDone;
        update(todo, this);
    }

    public void showAllTodos() {
        showState = STATE.ALL;
        getTodos();
    }
    public void showDoneTodos() {
        showState = STATE.DONE;
        getTodos();
    }
    public void showCurrentTodos() {
        showState = STATE.CURRENT;
        getTodos();
    }
    public void showMissedTodos() {
        showState = STATE.MISSED;
        getTodos();
    }
}
