package ru.reliableteam.noteorganizer.todos.presenter;

import java.util.ArrayList;
import java.util.List;

import ru.reliableteam.noteorganizer.todos.model.Todo;
import ru.reliableteam.noteorganizer.todos.view.TodosFragment;
import ru.reliableteam.noteorganizer.todos.view.recycler.IViewHolder;

public class TodoPresenter implements ITodoPresenter {

    private List<Todo> todos = new ArrayList<>();
    private TodosFragment view;

    public TodoPresenter(TodosFragment view) {
        this.view = view;
//        getTodos();
    }

    @Override
    public void notifyDatasetChanged(int messageId) {
        view.notifyDataChanged();
    }

    @Override
    public void getTodos() {
        Todo todo = new Todo();
        todo.title = "title";
        int n = 20;
        while (n-- > 0)
            todos.add(todo);
        view.notifyDataChanged();
    }

    @Override
    public void bindView(IViewHolder viewHolder) {
        int position = viewHolder.getPos();
        viewHolder.setTodo(todos.get(position));
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }


    @Override
    public void saveTodo() {

    }

    @Override
    public void deleteTodo() {

    }
}
