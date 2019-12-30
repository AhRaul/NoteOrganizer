package ru.reliableteam.noteorganizer.entity;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

class NoteDaoMigrations {
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(final SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE NOTE ADD COLUMN noteColor INTEGER DEFAULT 0");
        }
    };
}
