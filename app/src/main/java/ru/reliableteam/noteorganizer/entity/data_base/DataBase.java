package ru.reliableteam.noteorganizer.entity.data_base;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.reliableteam.noteorganizer.entity.data_base.dao.NoteDAO;
import ru.reliableteam.noteorganizer.entity.data_base.dao.TodoDAO;
import ru.reliableteam.noteorganizer.notes.model.Note;
import ru.reliableteam.noteorganizer.todos.model.Todo;

@Database(entities = {Note.class, Todo.class}, version = 1)
public abstract class DataBase extends RoomDatabase {
    public abstract NoteDAO getNoteDao();
    public abstract TodoDAO getTodoDao();
}
