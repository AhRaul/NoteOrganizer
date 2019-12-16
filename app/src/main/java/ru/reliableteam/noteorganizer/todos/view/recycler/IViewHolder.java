package ru.reliableteam.noteorganizer.todos.view.recycler;

import ru.reliableteam.noteorganizer.todos.model.Todo;

public interface IViewHolder {
    void setTodo(Todo todo);
    int getPos();
}
