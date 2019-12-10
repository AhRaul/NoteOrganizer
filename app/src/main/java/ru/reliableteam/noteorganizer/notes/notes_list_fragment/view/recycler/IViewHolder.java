package ru.reliableteam.noteorganizer.notes.notes_list_fragment.view.recycler;

import ru.reliableteam.noteorganizer.notes.model.Note;

public interface IViewHolder {
    void setNote(Note note);
    int getPos();
}
