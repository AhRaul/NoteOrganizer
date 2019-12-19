package ru.reliableteam.noteorganizer.notes.single_note_activity.view;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
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
import ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.view.CalculatorFragment;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;
import ru.reliableteam.noteorganizer.notes.model.Span;
import ru.reliableteam.noteorganizer.notes.single_note_activity.presenter.SingleNotePresenter;

// todo need text utils
public class SingleNoteActivity extends BaseActivity
        implements View.OnClickListener, MaterialButtonToggleGroup.OnButtonCheckedListener {
    private final String CLASS_TAG = "SingleNoteActivity";

    private SingleNotePresenter presenter;
    CalculatorFragment calculatorFragment;

    TextInputEditText noteText, noteTitle;
    MaterialButton boldBtn, italicBtn; //, strikeBtn, underlineBtn;
    ImageButton cancelBtn, saveBtn, deleteBtn, calcBtn, shareBtn;
    MaterialButtonToggleGroup toggleGroup;

    private static class StyleState {
        static final int BOLD = Typeface.BOLD; // 1
        static final int ITALIC = Typeface.ITALIC; // 2
        static final int BOLD_ITALIC = Typeface.BOLD_ITALIC; // 3
        static final int REGULAR = Typeface.NORMAL; // 0
    }

    private int styleState = StyleState.REGULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferencesManager appSettings = new SharedPreferencesManager(this);
        setTheme(appSettings.getAppTheme());

        setContentView(R.layout.activity_single_note);


        presenter = new SingleNotePresenter(this);

        calculatorFragment = new CalculatorFragment();//getSupportFragmentManager());

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

//        strikeBtn = findViewById(R.id.strike_style);
//        strikeBtn.setOnClickListener(this);

//        underlineBtn = findViewById(R.id.underline_style);
//        underlineBtn.setOnClickListener(this);

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
        List<Span> spanList = new ArrayList<>();

        if (endSelection != startSelection) {

            StyleSpan[] ss = sb.getSpans(0, noteTextLength, StyleSpan.class);

            // clear text span
            for (StyleSpan s : ss) {
                Span span = new Span(sb.getSpanStart(s), sb.getSpanEnd(s), s.getStyle());
                spanList.add(span);
                sb.removeSpan(s);
            }

            sb.setSpan(new StyleSpan(styleState), startSelection, endSelection, Spanned.SPAN_MARK_MARK);// Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            // recover span
            for (Span span : spanList)
                if (span.start != startSelection && span.end != endSelection)
                    sb.setSpan(new StyleSpan(span.style), span.start, span.end, Spanned.SPAN_MARK_MARK);//Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
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
                calculatorFragment.setTvOutResult(noteText);
                break;
            case R.id.share_button:
                presenter.shareNote();
                break;
        }
    }

    @Override
    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        switch (checkedId) {
            case R.id.bold_style:
                System.out.println("BOLD");
                changeStyleState(isChecked ? StyleState.BOLD : StyleState.REGULAR);
                setTextStyle();
                break;
            case R.id.italic_style:
                System.out.println("ITALIC");
                changeStyleState(isChecked ? StyleState.ITALIC : StyleState.REGULAR);
                setTextStyle();
                break;
            default:
                changeStyleState(StyleState.REGULAR);
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
        StyleSpan[] ss = new StyleSpan[]{};

        if (startSelection < 1)
            return;

        if (text.charAt(startSelection - 1) == ' ')
            ss = sb.getSpans(startSelection, startSelection + 1, StyleSpan.class);
        else
            ss = sb.getSpans(startSelection - 1, startSelection, StyleSpan.class);
        if (ss.length > 0) {
            for (StyleSpan s : ss) {
                System.out.println(s.getStyle());
                changeStyleState(s.getStyle());
            }
        } else {
            changeStyleState(StyleState.REGULAR);
        }
        setCheckedButtons();
    }

    public void setCheckedButtons() {
        System.out.println("prev style = " + styleState);
        switch (styleState) {
            case StyleState.BOLD:
                toggleGroup.check(R.id.bold_style);
                break;
            case StyleState.BOLD_ITALIC:
                toggleGroup.check(R.id.bold_style);
                toggleGroup.check(R.id.italic_style);
                break;
            case StyleState.ITALIC:
                toggleGroup.check(R.id.italic_style);
                break;
            default:
                toggleGroup.clearChecked();
                styleState = StyleState.REGULAR;
                break;
        }
        System.out.println("new style = " + styleState);
    }

    private void changeStyleState(int newStyleState) {
        styleState = newStyleState;
    }

    private void showHint(String hintText) {
        Toast.makeText(this, hintText, Toast.LENGTH_LONG).show();
    }

    private StyleSpan getStyle() {
        System.out.println("get style() " + styleState);
        return new StyleSpan(styleState);
    }
    private TextWatcher getTextChangeListener() {

        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                StyleSpan styleSpan = getStyle();
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
