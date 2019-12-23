package ru.reliableteam.noteorganizer.notes.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
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
    public long id;
    public String title;
    public String body;
    public int cardImageUri;
    public long dataTime;

    // TODO Удалить
    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", cardImageUri=" + cardImageUri +
                ", dataTime=" + dataTime +
                '}';
    }

}
