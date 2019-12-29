package ru.reliableteam.noteorganizer.todos.presenter;

import java.util.ArrayList;
import java.util.List;

import ru.reliableteam.noteorganizer.entity.TodoDaoImpl;
import ru.reliableteam.noteorganizer.todos.model.Todo;
import ru.reliableteam.noteorganizer.todos.view.TodosFragment;
import ru.reliableteam.noteorganizer.todos.view.recycler.IViewHolder;

public class TodoPresenter extends TodoDaoImpl implements ITodoPresenter {
    public static final int REQUEST_NEW_TODO = 1;

    private TodosFragment view;

    public TodoPresenter(TodosFragment view) {
        this.view = view;
    }

    @Override
    public void notifyDatasetChanged(int messageId) {
        view.notifyDataChanged();
    }

    @Override
    public void getTodos() {
        getAll(this);
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
        System.out.println("SAVE TODO");
        Todo todo = new Todo();
        todo.title = title;
        todo.description = description;
        todo.endDate = timeChosen ? dateTime : 0;
        todo.createDate = System.currentTimeMillis();
        todo.isDone = false;
        todo.parentId = null;

        saveTodo(todo);
    }
    private void saveTodo(Todo todo) {
        insertTodo(todo, this);
    }

    @Override
    public void deleteTodo() {

    }

    // todo open existing
    // todo delete existing
    // todo sorting
}
