package ru.reliableteam.noteorganizer.notes.single_note_activity.view;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.*;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import ru.reliableteam.noteorganizer.BaseActivity;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.notes.single_note_activity.StyleState;
import ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.view.CalculatorFragment;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.notes.model.Span;
import ru.reliableteam.noteorganizer.notes.single_note_activity.presenter.SingleNotePresenter;
import ru.reliableteam.noteorganizer.notes.single_note_activity.presenter.TextSpanPresenter;

// todo need text utils
public class SingleNoteActivity extends BaseActivity
        implements View.OnClickListener, MaterialButtonToggleGroup.OnButtonCheckedListener,
        StyleState {
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

        SharedPreferencesManager appSettings = new SharedPreferencesManager(this);
        setTheme(appSettings.getAppTheme());

        setContentView(R.layout.activity_single_note);


        presenter = new SingleNotePresenter(this);
        spanPresenter = new TextSpanPresenter(this);

        calculatorFragment = new CalculatorFragment();

        initUI();
        getClickedNote();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        presenter.checkSharedIntent();
    }

    private void initUI() {
        noteTitle = findViewById(R.id.note_title);
        noteText = findViewById(R.id.note_text);

        Spannable.Factory spannableFactory = new Spannable.Factory() {
            @Override
            public Spannable newSpannable(CharSequence source){
                return (Spannable) source;
            }
        };
        noteText.setSpannableFactory(spannableFactory);
        noteText.setOnClickListener(this);
        noteText.addTextChangedListener(getTextChangeListener());

        toggleGroup = findViewById(R.id.toggle_style_button_group);
        toggleGroup.addOnButtonCheckedListener(this);

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

        shareBtn = findViewById(R.id.share_button);
        shareBtn.setOnClickListener(this);

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



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                this.finish();
                break;
            case R.id.save_button:
                presenter.saveNote();
                showHint("Ваша заметка была сохранена!");
                onBackPressed();
                break;
            case R.id.delete_button:
                showHint("Ваша заметка была удалена!");
                presenter.deleteNote();
                onBackPressed();
                break;
            case R.id.note_text:
                onSelectionChanged();
                break;
            case R.id.calc_button:
                calculatorFragment.show(getSupportFragmentManager(), "calculator");
                break;
            case R.id.share_button:
                presenter.shareNote();
                break;
        }
    }

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

    // todo remake with underline and strike
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideKeyBoard();
    }

}
