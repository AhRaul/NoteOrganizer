package ru.reliableteam.noteorganizer.settings.view;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.ChipGroup;

import ru.reliableteam.noteorganizer.Action;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.entering_app.PasswordActivity;
import ru.reliableteam.noteorganizer.settings.help_activity.HelpActivity;
import ru.reliableteam.noteorganizer.settings.presenter.SettingsPresenter;

class SettingsFragmentInitialize extends Fragment {
    View root;
    TextView notesCacheSize;
    TextView todosCacheSize;
    TextView lastSyncDate;
    TextView appDirPath;
    private ImageButton cleanTodosCache, cleanNotesCache, syncBtn, migrateToTxt;

    protected SettingsPresenter presenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        System.out.println("SUPER");
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        initUI();
        return root;
    }
    private void initUI() {
        initThemeSelection();
        initSyncWithStorage();
        initNoteCacheSizeSettings();
        initTodosCacheSizeSettings();
        initNotesMigrationSettings();
        initHelp();
        initTutorial();
        initSecurity();
    }
    private void initThemeSelection() {
        ChipGroup themeSelector = root.findViewById(R.id.theme_mode_selection);
        themeSelector.setOnCheckedChangeListener(presenter.themeChangeListener());
        themeSelector.check(presenter.getThemeId());
    }
    private void initSyncWithStorage() {
        syncBtn = root.findViewById(R.id.sync_button);
        syncBtn.setOnClickListener( v -> {
            rotate(syncBtn);
            presenter.makeSyncWithStorage();
        });

        lastSyncDate = root.findViewById(R.id.last_sync_date);
        presenter.setLastSyncDate();
    }
    private void initNoteCacheSizeSettings() {
        notesCacheSize = root.findViewById(R.id.notes_cache_size_tv);
        cleanNotesCache = root.findViewById(R.id.notes_cache_size_clean_btn);
        cleanNotesCache.setOnClickListener( v -> {
            circularReveal(cleanNotesCache);
            showConfirmation( () -> presenter.cleanNotesCache(), R.string.delete_hint );
        });
    }
    private void initTodosCacheSizeSettings() {
        todosCacheSize = root.findViewById(R.id.todos_cache_size_tv);
        presenter.getTodosCacheSize();
        cleanTodosCache = root.findViewById(R.id.todos_photos_cache_size_clean_btn);
        cleanTodosCache.setOnClickListener( v -> {
            circularReveal(cleanTodosCache);
            showConfirmation( () -> presenter.cleanTodosCache(), R.string.delete_hint );
        });
    }
    private void initNotesMigrationSettings() {
        appDirPath = root.findViewById(R.id.app_dir_path);
        appDirPath.setOnClickListener( v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(presenter.getAppFullDirPath());
            intent.setDataAndType(uri, "*/*");
            startActivity(intent);
        });

        migrateToTxt = root.findViewById(R.id.migrate_to_txt);
        migrateToTxt.setOnClickListener( v -> {
            fading(migrateToTxt);
            presenter.saveToTxt();
        } );
    }

    private void initHelp() {
        ImageButton infoSync = root.findViewById(R.id.info_sync_button);
        infoSync.setOnClickListener(this::showExplanation);
        ImageButton infoCleanNotesCache = root.findViewById(R.id.info_notes_cache_size_clean_btn);
        infoCleanNotesCache.setOnClickListener(this::showExplanation);
        ImageButton infoCleanTodosCache = root.findViewById(R.id.info_todos_cache_size_clean_btn);
        infoCleanTodosCache.setOnClickListener(this::showExplanation);
        ImageButton infoMigrateToTxt = root.findViewById(R.id.info_migrate_to_txt_btn);
        infoMigrateToTxt.setOnClickListener(this::showExplanation);
        ImageButton helpWithApp = root.findViewById(R.id.help_with_app);
        helpWithApp.setOnClickListener( v -> showHelpWithAppActivity() );
        ImageButton infoEnablePassword = root.findViewById(R.id.info_enable_password);
        infoEnablePassword.setOnClickListener(this::showExplanation);
    }
    private void initTutorial() {
        ImageButton tutorial = root.findViewById(R.id.tutorial_button);
        int tutorialPassed = Color.rgb(125, 125, 125);
        int tutorialEnable = Color.rgb(0, 133, 119);
        tutorial.setColorFilter( presenter.isTutorialPassed() ? tutorialPassed : tutorialEnable );
        tutorial.setOnClickListener( v -> {
            presenter.enableTutorial();
            tutorial.setColorFilter(tutorialEnable);
        });
    }
    private void initSecurity() {
        ImageButton passwordSecurity = root.findViewById(R.id.enable_password);
        int disabledColor = Color.rgb(125, 125, 125);
        int enabledColor = Color.rgb(0, 133, 119);
        passwordSecurity.setColorFilter( presenter.isSecurityEnabled() ? enabledColor : disabledColor );
        int messageId = presenter.isSecurityEnabled() ? R.string.reset_password_hint : R.string.enter_password_hint;
        passwordSecurity.setOnClickListener( v ->
                showConfirmation(() -> {
                    presenter.changeSecuritySettings();
                    initSecurity();
                    if (presenter.isSecurityEnabled()) {
                        startActivity(new Intent(getActivity(), PasswordActivity.class));
                        getActivity().finish();
                    }

                }, messageId)
        );
    }

    private void showExplanation (View v) {
        showExplanation(v.getContentDescription().toString());
    }

    private void showExplanation(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage(message);
        dialog.setPositiveButton(R.string.understand, (d, w) -> d.dismiss());
        dialog.show();
    }

    private void showConfirmation(Action action, int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(messageId);
        builder.setPositiveButton(R.string.positive, (dialog, which) -> {
            dialog.dismiss();
            action.doAction();
        });
        builder.setNegativeButton(R.string.negative, (d, w) -> {
            d.dismiss();
        });
        builder.show();
    }

    private void showHelpWithAppActivity() {
        startActivity(new Intent(getContext(), HelpActivity.class));
    }

    private void rotate(View v) {
        int pivotX = v.getWidth() / 2;
        int pivotY = v.getHeight() / 2;

        Animation anim = new RotateAnimation(0, -360f, pivotX, pivotY);

        anim.setDuration(1000);
        v.startAnimation(anim);
    }
    private void circularReveal(View v) {
        int cx = v.getWidth() / 2;
        int cy = v.getHeight() / 2;

        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0f, finalRadius);

        anim.setDuration(1000);
        anim.start();
    }
    private void fading(View v) {
        v.setAlpha(0f);
        v.animate().alpha(1f).setDuration(1000).start();
    }
}
