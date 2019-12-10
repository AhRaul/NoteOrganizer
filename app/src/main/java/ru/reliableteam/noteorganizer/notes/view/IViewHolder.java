package ru.reliableteam.noteorganizer.notes.view;

import ru.reliableteam.noteorganizer.notes.model.Note;

public interface IViewHolder {
    void setNote(Note note);
    int getPos();
}
