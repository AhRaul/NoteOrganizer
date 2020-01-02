package ru.reliableteam.noteorganizer.todos.todos_fragment.presenter;

import ru.reliableteam.noteorganizer.BasePresenter;
import ru.reliableteam.noteorganizer.todos.todos_fragment.view.recycler.IViewHolder;

public interface ITodoPresenter extends BasePresenter {
    void getTodos();
    void bindView(IViewHolder viewHolder);
    int getItemCount();

    void deleteTodo();
    void saveTodo(String title, String description, Long dateTime, boolean timeChosen);
    public void editTodo(String title, String description, Long dateTime, boolean timeChosen, int action);
    void makeTodoDone(int position, boolean isDone);
    default void notifyItemChanged(int position) { }

    void longClicked(int position);
    void clicked(int position);
}
