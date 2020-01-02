package ru.reliableteam.noteorganizer.entity.data_base;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import ru.reliableteam.noteorganizer.todos.model.Todo;

@Dao
public interface TodoDAO {
    @Query("SELECT * FROM Todo WHERE parentId IS NULL")
    Single<List<Todo>> getAll();

    @Query("SELECT * FROM Todo WHERE id = :id")
    Single<Todo> getById(Long id);

    @Update
    void update(Todo newTodo);

    @Insert
    void insert(Todo todo);

    @Delete
    void delete(Todo todo);

    @Query("SELECT * FROM Todo WHERE isDone is 1")
    Single<List<Todo>> getDoneTodos();

    @Query("SELECT * FROM Todo WHERE endDate > :sysDateInMills AND isDone = 0")
    Flowable<List<Todo>> getCurrentTodos(long sysDateInMills);

    @Query("SELECT * FROM Todo WHERE endDate < :sysDateInMills AND isDone = 0")
    Flowable<List<Todo>> getMissedTodos(long sysDateInMills);
}
