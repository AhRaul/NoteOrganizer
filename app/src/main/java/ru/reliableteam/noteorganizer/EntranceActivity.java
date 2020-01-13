package ru.reliableteam.noteorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class EntranceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme();
        setContentView(R.layout.activity_entrance);

        initUI();
        initEnteringAppTimer();
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
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
    private void resetAlpha(View v) {
        v.setAlpha(0f);
    }
    private void setAnimation(View v) {
        float alphaTo = 1f;
        long duration = 1000;

        v.animate().alpha(alphaTo)
                .setDuration(duration)
                .setListener(null);
    }
    private void initEnteringAppTimer() {
        Handler delayedOpen = new Handler();
        delayedOpen.postDelayed(
                this::enterApp, 1000
        );
    }
}
