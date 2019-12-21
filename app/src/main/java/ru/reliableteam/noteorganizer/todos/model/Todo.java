package ru.reliableteam.noteorganizer.todos.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Todo {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long createDate;
    public long endDate;
    public String title;
    public String description;
    public long parentId;
    public boolean isDone;

    public Todo() {
        createDate = 0L;
        endDate = 0L;
        title = "";
        description = "";
        isDone = false;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", endDate=" + endDate +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", parentId=" + parentId +
                ", isDone=" + isDone +
                '}';
    }
}
