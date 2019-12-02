package ru.reliableteam.noteorganizer.entity.data_base;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.reliableteam.noteorganizer.notes.model.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class DataBase extends RoomDatabase {
    public abstract NoteDAO noteDao();
}
