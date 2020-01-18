package ru.reliableteam.noteorganizer.settings.help_activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import ru.reliableteam.noteorganizer.BaseActivity;
import ru.reliableteam.noteorganizer.R;

public class HelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme();
        setContentView(R.layout.activity_help);

        initLayout();
    }

    private void initLayout() {
        initBackPressing();
        initHelpWithNoteNaming();
        initHelpWithNoteSaving();
    }

    private void initBackPressing() {
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener( v -> onBackPressed() );
    }

    private void initHelpWithNoteNaming() {
        LinearLayoutCompat howToMakeNotes = findViewById(R.id.how_to_make_notes_layout);
        LinearLayoutCompat howToMakeNotesDescription = findViewById(R.id.how_to_make_notes_layout_description);
        TextView howToMakeNotesTv = findViewById(R.id.how_to_make_notes_tv);
        TextView howToMakeNotesTvSubheader = findViewById(R.id.how_to_make_notes_tv_subheader);

        howToMakeNotes.setOnClickListener( v -> showDescription(howToMakeNotesDescription));
        howToMakeNotesTv.setOnClickListener( v -> showDescription(howToMakeNotesDescription));
        howToMakeNotesTvSubheader.setOnClickListener( v -> showDescription(howToMakeNotesDescription));
    }
    private void initHelpWithNoteSaving() {
        LinearLayoutCompat whereToSaveNotes = findViewById(R.id.where_to_save_notes_layout);
        LinearLayoutCompat whereToSaveNotesDescription = findViewById(R.id.where_to_save_notes_layout_description);
        TextView whereToSaveNotesTv = findViewById(R.id.where_to_save_notes_tv);
        TextView whereToSaveNotesTvSubheader = findViewById(R.id.where_to_save_notes_tv_subheader);

        whereToSaveNotes.setOnClickListener( v -> showDescription(whereToSaveNotesDescription));
        whereToSaveNotesTv.setOnClickListener( v -> showDescription(whereToSaveNotesDescription));
        whereToSaveNotesTvSubheader.setOnClickListener( v -> showDescription(whereToSaveNotesDescription));
    }

    private void showDescription(View v) {
        v.setVisibility(v.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }
}
