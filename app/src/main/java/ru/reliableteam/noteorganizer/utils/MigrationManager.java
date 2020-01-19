package ru.reliableteam.noteorganizer.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.notes.model.Note;

public class MigrationManager {

    private SharedPreferencesManager appSettings;

    public MigrationManager(SharedPreferencesManager appSettings) {
        this.appSettings = appSettings;
    }

    public void saveToDir(Note note) {
        String date = DateUtils.dateToString(note.dataTime).split(" ")[0].replace("/", "-");
        System.out.println(date);

        File f = new File(appSettings.getAppDataDirectory(), note.title + "_" + date + ".txt");
        f.setWritable(true);
        try {
            f.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write(note.body);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File[] getFilesPathsFromDir() {
        File dir = new File(appSettings.getAppDataDirectory());
        return dir.listFiles();
    }
    private BufferedReader getFileReader(String uri) {
        File file = new File(uri);
        BufferedReader br = null;
        try {
             br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return br;
    }
    private Note readNote(File f) {
        if (isNotCorrect(f.getName()))
            return null;

        BufferedReader br = getFileReader(f.getAbsolutePath());
        if (br == null)
            return null;
        Note note = new Note();
        note.body = getBody(br);
        note.title = getTitle(f);
        note.dataTime = getDate(f);

        return note;
    }
    private boolean isNotCorrect(String name) {
        String[] nameStruct = name.replace(".txt", "").split("_");
        if (nameStruct.length != 2)
            return true;

        String date = nameStruct[1];

        Pattern pattern = Pattern.compile("^(0[1-9]|1[0-9]|2[0-9]|3[01])-(0[1-9]|1[0-2])-[0-9]{4}$");

        System.out.println(date + " matches? = " + pattern.matcher(date).matches());

        return !pattern.matcher(date).matches();
    }
    private Long getDate (File f) {
        String[] fileName = f.getName().replace(".txt", "").split("_");
        String date = "";
        if (fileName.length == 2)
            date = fileName[1];
        date = date.replace("-", "/");

        return DateUtils.stringToDate("", date);
    }
    private String getTitle (File f) {
        return f.getName().split("_")[0];
    }
    private String getBody(BufferedReader br) {
        StringBuilder text = new StringBuilder("");
        String line;
        while (true) {
            try {
                line = br.readLine();
                if (line == null)
                    break;
                text.append(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return text.toString();
    }

    public List<Note> getNotesFromStorage() {
        File[] notesInDir = getFilesPathsFromDir();
        List<Note> noteList = new ArrayList<>();
        if (notesInDir != null) {
            for (File f : notesInDir) {
                Note note = readNote(f);
                if (note != null)
                    noteList.add(note);
            }
        }

        return noteList;
    }
}
