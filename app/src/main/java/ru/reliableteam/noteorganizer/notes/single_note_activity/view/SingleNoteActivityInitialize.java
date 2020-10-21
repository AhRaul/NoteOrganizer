package ru.reliableteam.noteorganizer.notes.single_note_activity.view;

import android.os.Bundle;
import android.text.Spannable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;

import ru.reliableteam.noteorganizer.Action;
import ru.reliableteam.noteorganizer.BaseActivity;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.notes.single_note_activity.presenter.SingleNotePresenter;
import ru.reliableteam.noteorganizer.utils.TutorialSpotlight;

class SingleNoteActivityInitialize extends BaseActivity {
    protected SingleNotePresenter presenter;

    protected TextInputEditText noteText, noteTitle;
    protected ImageButton cancelBtn, saveBtn, deleteBtn, shareBtn, migrateBtn;

    private TutorialSpotlight tutorialSpotlight;
    private boolean isTutorialShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isTutorialShowing = !presenter.wasAddingNoteTutorialWatched();
        initUI();
        getClickedNote();

        presenter.checkSharedIntent();
        tutorialSpotlight = new TutorialSpotlight(this);
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
            public Spannable newSpannable(CharSequence source) {
                return (Spannable) source;
            }
        };
        noteText.setSpannableFactory(spannableFactory);

    }

    private void initNoteInteractOptionsUI() {
        initCancel();
        initSave();
        initDelete();
        initShare();
        initMigrate();
        setDescriptions(cancelBtn, saveBtn, deleteBtn, shareBtn, migrateBtn);
        if (presenter.isNewNote() && !isTutorialShowing)
            hideViews(deleteBtn, shareBtn, migrateBtn);
    }

    private void initCancel() {
        cancelBtn = findViewById(R.id.cancel_button);
        if (!isTutorialShowing) {
            cancelBtn.setOnClickListener(v -> {
                        if (presenter.isNoteEmpty()) {
                            finish();
                            return;
                        }

                        showConformation(
                                () -> saveBtn.performClick(),
                                this::finish,
                                R.string.save_before_exit_hint
                        );
                    }
            );
        }
    }

    private void initSave() {
        saveBtn = findViewById(R.id.save_button);
        if (!isTutorialShowing) {
            saveBtn.setOnClickListener(v -> {
                boolean wasSaved = presenter.saveNote();
                if (wasSaved) {
                    showHint(getString(R.string.saved_note_hint));
                    this.finish();
                }
            });
        }
    }

    private void initDelete() {
        deleteBtn = findViewById(R.id.delete_button);
        if (!isTutorialShowing) {
            deleteBtn.setOnClickListener(v -> showConformation(
                    () -> {
                        presenter.deleteNote();
                        this.finish();

                    },
                    this::finish,
                    R.string.delete_hint
            ));
        }
    }

    private void initShare() {
        shareBtn = findViewById(R.id.share_button);
        if (!isTutorialShowing) {
            shareBtn.setOnClickListener( v -> presenter.shareNote() );
        }
    }

    private void initMigrate() {
        migrateBtn = findViewById(R.id.migrate_button);
        if (!isTutorialShowing) {
            migrateBtn.setOnClickListener(v -> {
                presenter.migrate();
                showHint(getString(R.string.migrated_hint));
                this.finish();
            });
        }
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

    public void showVerification(int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(messageId);
        builder.setPositiveButton(R.string.understand, (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showConformation(Action actionPositive, Action actionNegative, int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(messageId);
        builder.setPositiveButton(R.string.positive, (dialog, which) -> {
            dialog.dismiss();
            actionPositive.doAction();
        });
        builder.setNegativeButton(R.string.negative, (d, w) -> {
            d.dismiss();
            actionNegative.doAction();
        });
        builder.setNeutralButton(R.string.cancel, (d, w) -> d.dismiss() );
        builder.show();
    }

    private void showHint(String hintText) {
        Toast.makeText(this, hintText, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!presenter.wasAddingNoteTutorialWatched()) {
            showTutorial();
        }
    }
    private void showTutorial() {
        isTutorialShowing = true;
        tutorialSpotlight
                .buildTutorialFor(cancelBtn, saveBtn, shareBtn, migrateBtn, deleteBtn)
                .setOnEndTutorialListener( () -> doWhenTutorialEnds() )
                .start();
    }
    private void doWhenTutorialEnds() {
        presenter.setAddingNoteTutorialWatched();

        isTutorialShowing = false;
        initUI();

        boolean isNewNote = presenter.isNewNote();
        if (isNewNote) {
            hideViews(shareBtn, migrateBtn, deleteBtn);
        }
    }
    private void hideViews(View... views) {
        for (View v : views)
            v.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        cancelBtn.performClick();
    }

}
