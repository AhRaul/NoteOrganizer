package ru.reliableteam.noteorganizer.todos.presenter;

import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.todos.model.Todo;
import ru.reliableteam.noteorganizer.todos.view.recycler.IViewHolder;

public interface ITodoPresenter extends BasePresenter {
    void getTodos ();
    void bindView (IViewHolder viewHolder);
    int getItemCount ();

    void deleteTodo();
    void saveTodo(String title, String description, Long dateTime, boolean timeChosen);

    void clicked(int position);
}
