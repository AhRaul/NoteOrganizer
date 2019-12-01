package ru.reliableteam.noteorganizer.notes.model;


/**
 * initial Note class
 * definition is:
 *  -   title
 *  -   body (seems have to implement List, as the note could be very long)
 *  -   card image uri (the face of the Note in RecyclerView (NOT IMPORTANT))
 *
 *  todo add ID, data
 */
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
