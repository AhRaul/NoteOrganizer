package ru.reliableteam.noteorganizer.entity.data_base;

import ru.reliableteam.noteorganizer.entity.AppConfig;

/**
 * class that provides persistence interface for the app
 *
 * use entity to implement persistence
 * add new persistence to this manager to work through it
 */

public class PersistenceManager {
    private final DataBase db = AppConfig.getInstance().getDatabase();
    private NoteDAO noteDao;

    public synchronized NoteDAO getNoteDao() {
        synchronized (db) {
            if (noteDao == null)
                noteDao = db.noteDao();
            return noteDao;
        }
    }
}
