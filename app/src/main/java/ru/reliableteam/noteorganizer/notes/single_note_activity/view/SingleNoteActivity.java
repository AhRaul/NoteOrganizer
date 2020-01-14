package ru.reliableteam.noteorganizer.notes.single_note_activity.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;

import ru.reliableteam.noteorganizer.BaseActivity;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.notes.single_note_activity.StyleState;
import ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.view.CalculatorFragment;
import ru.reliableteam.noteorganizer.notes.single_note_activity.presenter.SingleNotePresenter;
import ru.reliableteam.noteorganizer.notes.single_note_activity.presenter.TextSpanPresenter;

public class SingleNoteActivity extends BaseActivity
        implements MaterialButtonToggleGroup.OnButtonCheckedListener, StyleState {
    private final String CLASS_TAG = "SingleNoteActivity";

    private SingleNotePresenter presenter;
    private TextSpanPresenter spanPresenter;
    CalculatorFragment calculatorFragment;

    TextInputEditText noteText, noteTitle;
    MaterialButton boldBtn, italicBtn, underlineBtn, strikeBtn;
    ImageButton cancelBtn, saveBtn, deleteBtn, calcBtn, shareBtn;
    MaterialButtonToggleGroup toggleGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setAppTheme();
        setContentView(R.layout.activity_single_note);
        hideKeyBoard();

        initPresenters();
        calculatorFragment = new CalculatorFragment();

        initUI();
        getClickedNote();

        presenter.checkSharedIntent();
    }

    private void initPresenters() {
        presenter = new SingleNotePresenter(this);
        spanPresenter = new TextSpanPresenter(this);
    }
    private void initUI() {
        initNoteTextUI();
        initTextDecorationUI();
        initNoteInteractOptionsUI();
    }
    private void initNoteTextUI() {
        noteTitle = findViewById(R.id.note_title);
        noteText = findViewById(R.id.note_text);

        Spannable.Factory spannableFactory = new Spannable.Factory() {
            @Override
            public Spannable newSpannable(CharSequence source){
                return (Spannable) source;
            }
        };
        noteText.setSpannableFactory(spannableFactory);
        noteText.setOnClickListener( v -> onSelectionChanged() );
        noteText.addTextChangedListener(getTextChangeListener());

    }
    private void initTextDecorationUI() {
        toggleGroup = findViewById(R.id.toggle_style_button_group);
        toggleGroup.addOnButtonCheckedListener(this);

        boldBtn = findViewById(R.id.bold_style);
        italicBtn = findViewById(R.id.italic_style);
        strikeBtn = findViewById(R.id.strike_style);
        underlineBtn = findViewById(R.id.underline_style);
    }
    private void initNoteInteractOptionsUI() {
        cancelBtn = findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener( v -> this.finish() );

        saveBtn = findViewById(R.id.save_button);
        saveBtn.setOnClickListener( saveNote );

        deleteBtn = findViewById(R.id.delete_button);
        deleteBtn.setOnClickListener( deleteNote );
        deleteBtn.setVisibility(presenter.isNewNote() ? View.GONE : View.VISIBLE);

        calcBtn = findViewById(R.id.calc_button);
        calcBtn.setOnClickListener( v -> {
            calculatorFragment.show(getSupportFragmentManager(), "calculator");
            calculatorFragment.setTvOutResult(noteText);
        } );

        shareBtn = findViewById(R.id.share_button);
        shareBtn.setOnClickListener( v -> presenter.shareNote() );
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

    private void setTextStyle() {
        int startSelection = noteText.getSelectionStart();
        int endSelection = noteText.getSelectionEnd();
        int noteTextLength = noteText.getText().length();
        Spannable sb = noteText.getText();

        sb = spanPresenter.setTextStyle(sb, startSelection, endSelection, noteTextLength);
        noteText.setText(sb);
        noteText.setSelection(endSelection);
    }

    private final View.OnClickListener saveNote = v -> {
        presenter.saveNote();
    };
    private final View.OnClickListener deleteNote = v -> {
        AlertDialog.Builder db = new AlertDialog.Builder(this);
        db.setTitle(R.string.delete_hint);
        db.setPositiveButton(R.string.positive, (dialog, which) -> {
            presenter.deleteNote();
            showHint(getResources().getString(R.string.deleted_note_hint));
            onBackPressed();
        });
        db.setNegativeButton(R.string.negative, (dialog, which) -> dialog.dismiss());
        db.show();
    };

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        System.out.println(isChecked);
        switch (checkedId) {
            case R.id.bold_style:
                System.out.println("BOLD");
                spanPresenter.setStyle(isChecked, StyleState.BOLD);
                setTextStyle();
                break;
            case R.id.italic_style:
                System.out.println("ITALIC");
                spanPresenter.setStyle(isChecked, StyleState.ITALIC);
                setTextStyle();
                break;
            case R.id.underline_style:
                System.out.println("UNDERLINE");
                spanPresenter.setStyle(isChecked, StyleState.UNDERLINE);
                setTextStyle();
                break;
            case R.id.strike_style:
                System.out.println("STRIKE");
                spanPresenter.setStyle(isChecked, StyleState.STRIKE);
                setTextStyle();
                break;
            default:
                System.out.println("REGULAR");
                spanPresenter.changeStyleStateToDefault();//(StyleState.REGULAR);
                setTextStyle();
                break;
        }
    }

    protected void onSelectionChanged() {
        System.out.println("selected");
        Spannable sb = noteText.getText();
        int startSelection = noteText.getSelectionStart();
        System.out.println("selection changed : " + startSelection);
        Editable text = noteText.getText();

        spanPresenter.onTextSelectionChanged(sb, startSelection, text);
    }

    private void showHint(String hintText) {
        Toast.makeText(this, hintText, Toast.LENGTH_LONG).show();
    }

    public void setCheckedButtons(int buttonId) {
        toggleGroup.check(buttonId);
    }
    public void clearCheckedButtons() {
        toggleGroup.clearChecked();
    }

    private TextWatcher getTextChangeListener() {

        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                ParcelableSpan styleSpan = spanPresenter.getStyle();
                if (s.length() > 1) {
                    s.setSpan(styleSpan, s.length() - 1, s.length(), Spanned.SPAN_MARK_MARK);
                }
            }
        };
    }

    public void showVerification() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.empty_body_hint);
        builder.setPositiveButton(R.string.positive, (dialog, which) -> dialog.dismiss() );
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideKeyBoard();
    }
}
