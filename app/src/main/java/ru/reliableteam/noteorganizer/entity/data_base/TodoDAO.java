package ru.reliableteam.noteorganizer.entity.data_base;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import ru.reliableteam.noteorganizer.todos.model.Todo;

@Dao
public interface TodoDAO {
    @Query("SELECT * FROM Todo WHERE parentId IS NULL")
    Flowable<List<Todo>> getAll();

    @Update
    void update(Todo newTodo);

    @Insert
    void insert(Todo todo);

    @Delete
    void delete(Todo todo);

    @Query("SELECT * FROM Todo WHERE parentId = :parentId")
    Flowable<List<Todo>> getSubTodos(int parentId);
}
