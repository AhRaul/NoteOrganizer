package ru.reliableteam.noteorganizer.notes.presenter;

import ru.reliableteam.noteorganizer.notes.view.MyAdapter;

public interface INotesPresenter extends BasePresenter {
    void bindView(MyAdapter.MyViewHolder viewHolder);
    int getItemCount();
    void clicked(int position);
}
