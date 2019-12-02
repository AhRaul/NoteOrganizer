package ru.reliableteam.noteorganizer.entity.data_base;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.reliableteam.noteorganizer.notes.model.Note;

@Dao
public interface NoteDAO {
    @Query("SELECT * FROM NOTE")
    Flowable<List<Note>> getAll();

    // todo use single
    @Query("SELECT * FROM NOTE WHERE id = :id")
    Single<Note> getById(long id);

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

}
