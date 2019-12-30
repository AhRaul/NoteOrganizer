package ru.reliableteam.noteorganizer.todos.todos_fragment.presenter;

import ru.reliableteam.noteorganizer.entity.TodoDaoImpl;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.todos.model.Todo;
import ru.reliableteam.noteorganizer.todos.todos_fragment.view.TodosFragment;
import ru.reliableteam.noteorganizer.todos.todos_fragment.view.recycler.IViewHolder;

public class TodoPresenter extends TodoDaoImpl implements ITodoPresenter {
    public static final int REQUEST_NEW_TODO = 1;
    private static final int NEW_TODO = -1;
    private SharedPreferencesManager appSettings;
    private TodosFragment view;
    private enum STATE {
        ALL, DONE, MISSED, CURRENT;
    }
    private STATE showState = STATE.ALL;

    public TodoPresenter(TodosFragment view) {
        this.view = view;
        appSettings = getAppSettings();
    }

    @Override
    public void notifyDatasetChanged(int messageId) {
        view.notifyDataChanged();
    }

    @Override
    public void getTodos() {
        System.out.println("state = " + showState);
        switch (showState) {
            case ALL:
                System.out.println("GET ALL TODOS");
                getAll(this);
                break;
            case DONE:
                System.out.println("GET DONE TODOS");
                getDoneTodos(this);
                break;
            case MISSED:
                break;
            case CURRENT:
                break;
        }
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
        Todo todo = todoList.get(position);
        appSettings.setClickedTodoId(todo.id);
        view.viewTodo();
    }

    public void newTodo() {
        appSettings.setClickedTodoId(NEW_TODO);
    }

    @Override
    public void makeTodoDone(int position, boolean isDone) {
        System.out.println("isDone = " + isDone);
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
//        getCurrentTodos(this);
    }
    public void showMissedTodos() {
//        getMissedTodos(this);
    }
}
