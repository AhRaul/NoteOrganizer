package ru.reliableteam.noteorganizer.notes.single_note_activity;

import android.graphics.Typeface;

public interface StyleState {
        int BOLD = Typeface.BOLD; // 1
        int ITALIC = Typeface.ITALIC; // 2
        //        static final int BOLD_ITALIC = Typeface.BOLD_ITALIC; // 3
        int UNDERLINE = 4;//new UnderlineSpan();
        int STRIKE = 5;//new StrikethroughSpan();
        int REGULAR = Typeface.NORMAL; // 0
}
