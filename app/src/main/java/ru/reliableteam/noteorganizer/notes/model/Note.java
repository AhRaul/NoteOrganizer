package ru.reliableteam.noteorganizer.notes.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    public int noteColor = 0;

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", cardImageUri=" + cardImageUri +
                ", dataTime=" + dataTime +
                ", noteColor=" + noteColor +
                '}';
    }

    public boolean isEmpty() {
        return this.body.isEmpty() && this.title.isEmpty();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Note other = (Note) obj;
        if (obj == null) return false;
        return this.title.equals(other.title) && this.body.equals(other.body);
    }
}
