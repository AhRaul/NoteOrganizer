package ru.reliableteam.noteorganizer.entity.data_base.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Single;
import ru.reliableteam.noteorganizer.todos.model.Todo;

@Dao
public interface TodoDAO {
    @Query("SELECT * FROM Todo WHERE parentId IS NULL")
    Single<List<Todo>> getAll();

    @Query("SELECT * FROM Todo WHERE id = :id")
    Single<Todo> getById(Long id);

    @Query("SELECT COUNT(*) FROM Todo WHERE isDone is 1")
    Single<Integer> getCacheSize();

    @Update
    void update(Todo newTodo);

    @Insert
    void insert(Todo todo);

    @Delete
    void delete(Todo todo);

    @Query("DELETE FROM Todo WHERE isDone is 1")
    void cleanCache();

    @Query("SELECT * FROM Todo WHERE isDone is 1")
    Single<List<Todo>> getDoneTodos();

    @Query("SELECT * FROM Todo WHERE (endDate - :sysDateInMills > 0 or endDate = 0) AND isDone = 0")
    Single<List<Todo>> getCurrentTodos(long sysDateInMills);

    @Query("SELECT * FROM Todo WHERE endDate - :sysDateInMills < 0 AND isDone = 0 and endDate is not 0")
    Single<List<Todo>> getMissedTodos(long sysDateInMills);
}
