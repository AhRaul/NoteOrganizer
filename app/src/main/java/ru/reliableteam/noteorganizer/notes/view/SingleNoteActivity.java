package ru.reliableteam.noteorganizer.notes.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.calculator.view.CalculatorFragment;
import ru.reliableteam.noteorganizer.notes.presenter.SingleNotePresenter;

// todo need text utils
// todo need full text implementation
public class SingleNoteActivity extends AppCompatActivity implements View.OnClickListener {
    private final String CLASS_TAG = "SingleNoteActivity";

    private SingleNotePresenter presenter;

    TextInputEditText noteText, noteTitle;
    MaterialButton boldBtn, italicBtn, strikeBtn, underlineBtn;
    ImageButton cancelBtn, saveBtn, deleteBtn, calcBtn;

    CalculatorFragment calculatorFragment;

    private static class StyleState {
        static final StyleSpan BOLD = new StyleSpan(android.graphics.Typeface.BOLD);
        static final StyleSpan ITALIC = new StyleSpan(Typeface.ITALIC);
//        static final StyleSpan STRIKE = new StyleSpan(android.graphics.Typeface.
//        static final StyleSpan UNDERLINE
        static final StyleSpan REGULAR = new StyleSpan(Typeface.NORMAL);

    }
    private Spannable sb;
    private StyleSpan styleState = StyleState.REGULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_note);

        presenter = new SingleNotePresenter(this);

        calculatorFragment = new CalculatorFragment(getSupportFragmentManager());

        initUI();
        getClickedNote();
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

        cancelBtn = findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(this);

        saveBtn = findViewById(R.id.save_button);
        saveBtn.setOnClickListener(this);

        deleteBtn = findViewById(R.id.delete_button);
        deleteBtn.setOnClickListener(this);
        deleteBtn.setVisibility(presenter.isNewNote() ? View.GONE : View.VISIBLE);

        calcBtn = findViewById(R.id.calc_button);
        calcBtn.setOnClickListener(this);
    }

    private void getClickedNote() {
        presenter.getClickedNote();
    }
    public void setNoteTitle(String title) {
        noteTitle.setText(title);
    }
    public void setNoteText(String title) {
        noteText.setText(title);
    }
    public String getNoteTitle() {
        return noteTitle.getText().toString();
    }
    public String getNoteText() {
        return noteText.getText().toString();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bold_style:
                styleState = StyleState.BOLD;
                setStyle();
                break;
            case R.id.italic_style:
                styleState = StyleState.ITALIC;
                setStyle();
                break;
            case R.id.strike_style:
                // todo make strike
                break;
            case R.id.underline_style:
                // todo make underline
                break;
            case R.id.cancel_button:
                onBackPressed();
                break;
            case R.id.save_button:
                // todo save note
                presenter.saveNote();
                showHint("Ваша заметка была сохранена!");
                onBackPressed();
                break;
            case R.id.delete_button:
                // todo delete note
                presenter.deleteNote();
                onBackPressed();
                break;
            case R.id.calc_button:
                calculatorFragment.open();
                break;
            default:
                styleState = StyleState.REGULAR;
                setStyle();
        }
    }

    private void showHint(String hintText) {
        Toast.makeText(this, hintText, Toast.LENGTH_LONG).show();
    }
}
