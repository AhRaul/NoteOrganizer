package ru.reliableteam.noteorganizer.notes.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import ru.reliableteam.noteorganizer.R;

// todo need text utils
public class SingleNoteActivity extends AppCompatActivity implements View.OnClickListener, Button.OnFocusChangeListener {
    private final String CLASS_TAG = "SingleNoteActivity";
    TextInputEditText noteText, noteTitle;
    MaterialButton boldBtn, italicBtn, strikeBtn, underlineBtn;

    private static class StyleState {
        static final StyleSpan BOLD = new StyleSpan(android.graphics.Typeface.BOLD);
        static final StyleSpan ITALIC = new StyleSpan(Typeface.ITALIC);
//        static final StyleSpan STRIKE = new StyleSpan(android.graphics.Typeface.
//        static final StyleSpan UNDERLINE
        static final StyleSpan REGULAR = new StyleSpan(Typeface.NORMAL);

    }
    Spannable sb;

    String noteTextS = "";
    private StyleSpan styleState = StyleState.REGULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_note);

        initUI();
    }

    private void initUI() {
        noteTitle = findViewById(R.id.note_title);
        noteText = findViewById(R.id.note_text);

        boldBtn = findViewById(R.id.bold_style);
        boldBtn.setOnClickListener(this);

        italicBtn = findViewById(R.id.italic_style);
        italicBtn.setOnClickListener(this);

        strikeBtn = findViewById(R.id.strike_style);
        strikeBtn.setOnClickListener(this);

        underlineBtn = findViewById(R.id.underline_style);
        underlineBtn.setOnClickListener(this);
        underlineBtn.setOnFocusChangeListener(this);
    }

    private void setStyle() {
        int startSelection = noteText.getSelectionStart();
        int endSelection = noteText.getSelectionEnd();

        if (endSelection != startSelection) {
            sb = new SpannableString(noteText.getText());
            sb.setSpan(styleState, startSelection, endSelection, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            noteText.setText(sb);
            noteText.setSelection(endSelection);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.i(CLASS_TAG, hasFocus + " " + v.getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bold_style:
                // todo make bold
                styleState = StyleState.BOLD;
                setStyle();
                break;
            case R.id.italic_style:
                // todo make italic
                styleState = StyleState.ITALIC;
                setStyle();
                break;
            case R.id.strike_style:
                // todo make strike
                break;
            case R.id.underline_style:
                // todo make underline
                break;
            default:
                styleState = StyleState.REGULAR;
                setStyle();
        }
    }
}
