package ru.reliableteam.noteorganizer.entering_app;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import ru.reliableteam.noteorganizer.BaseActivity;
import ru.reliableteam.noteorganizer.MainActivity;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.entity.AppConfig;
import ru.reliableteam.noteorganizer.entity.shared_prefs.SharedPreferencesManager;


public class EntranceActivity extends BaseActivity {

    SharedPreferencesManager appSettings = AppConfig.getInstance().getAppSettings();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme();
        setContentView(R.layout.activity_entrance);

        initUI();
    }
    private void initUI() {
        ImageView launcherIcon = findViewById(R.id.launcher_icon);
        TextView appNameTv = findViewById(R.id.app_name);
        TextView teamCopyright = findViewById(R.id.team_copyright);
        resetAlpha(launcherIcon);
        resetAlpha(appNameTv);
        resetAlpha(teamCopyright);
        setAnimation(launcherIcon);
        setAnimation(appNameTv);
        setAnimation(teamCopyright);
    }
    private void enterApp() {
        boolean passwordEnable = appSettings.enterOnPassword();
        startActivity(new Intent(this,
                passwordEnable ? PasswordActivity.class : MainActivity.class));
        this.finish();
    }
    private void resetAlpha(View v) {
        v.setAlpha(0f);
    }
    private void setAnimation(View v) {
        float alphaTo = 1f;
        long duration = 300;

        v.animate().alpha(alphaTo)
                .setDuration(duration)
                .setListener(getAnimationListener());
    }

    private Animator.AnimatorListener getAnimationListener() {
        return new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // do nothing
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                enterApp();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // do nothing
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // do nothing
            }
        };
    }
}
