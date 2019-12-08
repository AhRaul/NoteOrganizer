package ru.reliableteam.noteorganizer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.notes.model.Note;

public class MigrationManager {

    private SharedPreferencesManager appSettings;

    public MigrationManager(SharedPreferencesManager appSettings) {
        this.appSettings = appSettings;
    }

    public void saveToDir(Note note) {
        StringBuffer text = new StringBuffer();
        text.append(note.dataTime).append("\n").append(note.title).append("\n\n").append(note.body);

        File f = new File(appSettings.getAppDataDirectory(), note.title + ".txt");
        f.setWritable(true);
        try {
            f.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write(text.toString());

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
