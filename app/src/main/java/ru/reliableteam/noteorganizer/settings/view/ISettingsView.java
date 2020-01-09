package ru.reliableteam.noteorganizer.settings.view;

public interface ISettingsView {
    void reloadActivity();
    void setNotesCacheSize(String size);
    void setTodosCacheSize(String size);
}
