package ru.reliableteam.noteorganizer.entity.data_base.dao;

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
    Single<List<Note>> getAll();

    @Query("SELECT * FROM NOTE WHERE (title LIKE :what) OR (body like :what)")
    Single<List<Note>> getAll(String what);

    @Query("SELECT * FROM NOTE WHERE id = :id")
    Single<Note> getById(long id);

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT COUNT(*) FROM NOTE")
    Single<Integer> getNotesCount();

    @Query("DELETE FROM NOTE")
    void deleteAll();

}
