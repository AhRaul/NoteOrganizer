package ru.reliableteam.noteorganizer.ui.notes;

public class Note {
    public String title;
    public String body;
    public int cardImageUri;

    public Note(String title, String body, int cardImageUri) {
        this.title = title;
        this.body = body;
        this.cardImageUri = cardImageUri;
    }
}
