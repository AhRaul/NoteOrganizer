package ru.reliableteam.noteorganizer.todos.model;


public class Todo {
    public Long id;
    public Long createDate;
    public Long endDate;
    public String title;
    public String description;
    public Long parentId;
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
