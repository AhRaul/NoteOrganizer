package ru.reliableteam.noteorganizer.notes.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * initial Note class
 * definition is:
 *  -   title
 *  -   body (seems have to implement List, as the note could be very long)
 *  -   card image uri (the face of the Note in RecyclerView (NOT IMPORTANT))
 *
 */
@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String body;
    public int cardImageUri;
    public long dataTime;

    public Note(String title, String body, int cardImageUri) {
        this.title = title;
        this.body = body;
        this.cardImageUri = cardImageUri;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", cardImageUri=" + cardImageUri +
//                ", dataTime=" + dataTime +
                '}';
    }
}
