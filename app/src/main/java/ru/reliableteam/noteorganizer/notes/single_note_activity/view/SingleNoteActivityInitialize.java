package ru.reliableteam.noteorganizer.notes.single_note_activity.view;

import android.os.Bundle;
import android.text.Spannable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;

import ru.reliableteam.noteorganizer.BaseActivity;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.notes.single_note_activity.calculator_fragment.view.CalculatorFragment;
import ru.reliableteam.noteorganizer.notes.single_note_activity.presenter.SingleNotePresenter;

class SingleNoteActivityInitialize extends BaseActivity {
    protected SingleNotePresenter presenter;
    protected CalculatorFragment calculatorFragment;

    protected TextInputEditText noteText, noteTitle;
    protected ImageButton cancelBtn, saveBtn, deleteBtn, calcBtn, shareBtn, migrateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        getClickedNote();

        presenter.checkSharedIntent();
    }

    protected void initPresenters(SingleNoteActivity activity) {
        presenter = new SingleNotePresenter(activity);
    }
    private void initUI() {
        initThemeAndLayer();
        initNoteTextUI();
        initNoteInteractOptionsUI();
    }
    private void initThemeAndLayer() {
        setAppTheme();
        setContentView(R.layout.activity_single_note);
        hideKeyBoard();
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

    }
    private void initNoteInteractOptionsUI() {
        initCancel();
        initSave();
        initDelete();
        initCalc();
        initShare();
        initMigrate();
        setDescriptions(cancelBtn, saveBtn, deleteBtn, calcBtn, shareBtn, migrateBtn);
    }
    private void initCancel() {
        cancelBtn = findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener( v ->
                showConformation( () -> {
                    presenter.saveNote();
                    showHint(getString(R.string.saved_note_hint));
                }, R.string.save_before_exit_hint)
        );
    }
    private void initSave() {
        saveBtn = findViewById(R.id.save_button);
        saveBtn.setOnClickListener( v -> {
            boolean wasSaved = presenter.saveNote();
            if (wasSaved)
                showHint(getString(R.string.saved_note_hint));
        });
    }
    private void initDelete() {
        deleteBtn = findViewById(R.id.delete_button);
        deleteBtn.setOnClickListener( v -> showConformation( () -> presenter.deleteNote(), R.string.delete_hint ));
        deleteBtn.setVisibility(presenter.isNewNote() ? View.GONE : View.VISIBLE);
    }
    private void initCalc() {
        calculatorFragment = new CalculatorFragment();

        calcBtn = findViewById(R.id.calc_button);
        calcBtn.setOnClickListener( v -> {
            calculatorFragment.show(getSupportFragmentManager(), "calculator");
            calculatorFragment.setTvOutResult(noteText);
        });
    }
    private void initShare() {
        shareBtn = findViewById(R.id.share_button);
        shareBtn.setOnClickListener( v -> presenter.shareNote() );
    }
    private void initMigrate() {
        migrateBtn = findViewById(R.id.migrate_button);
        migrateBtn.setOnClickListener( v -> {
            presenter.migrate();
            showHint(getString(R.string.migrated_hint));
            onBackPressed();
        });
        migrateBtn.setVisibility(presenter.isNewNote() ? View.GONE : View.VISIBLE);
    }
    private void setDescriptions(View... views) {
        for (View view : views) {
            view.setOnLongClickListener(v -> {
                showHint(v.getContentDescription().toString());
                return true;
            });
        }
    }

    private void getClickedNote() {
        presenter.getClickedNote();
    }

    public void showVerification() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.empty_body_hint);
        builder.setPositiveButton(R.string.understand, (dialog, which) -> dialog.dismiss() );
        builder.show();
    }
    private void showConformation(SingleNoteActivity.Action action, int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(messageId);
        builder.setPositiveButton(R.string.positive, (dialog, which) -> {
            action.doAction();
            dialog.dismiss();
            this.finish();
        });
        builder.setNegativeButton(R.string.negative, (d, w) -> {
            d.dismiss();
            this.finish();
        });
        builder.show();
    }
    private void showHint(String hintText) {
        Toast.makeText(this, hintText, Toast.LENGTH_LONG).show();
    }

}
