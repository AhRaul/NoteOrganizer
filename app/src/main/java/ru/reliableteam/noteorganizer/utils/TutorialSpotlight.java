package ru.reliableteam.noteorganizer.utils;

import android.app.Activity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.takusemba.spotlight.OnSpotlightListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.Target;
import com.takusemba.spotlight.shape.Circle;

import java.util.ArrayList;
import java.util.List;

import ru.reliableteam.noteorganizer.R;

public class TutorialSpotlight {

    private Activity activity;
    private Spotlight.Builder tutorialBuilder;
    private Spotlight tutorial;

    public TutorialSpotlight(Activity activity) {
        this.activity = activity;
    }

    public TutorialSpotlight buildTutorialFor(View... views) {
        List<Target> targetList = buildTargets(views);

        tutorialBuilder =  new Spotlight.Builder(activity)
                .setTargets(targetList)
                .setDuration(500L)
                .setAnimation(new DecelerateInterpolator(2f));

        return this;
    }

    private List<Target> buildTargets(View... views) {
        List<Target> targetList = new ArrayList<>();
        for (View v : views) {
            targetList.add(buildTarget(v));
        }

        return targetList;
    }

    private Target buildTarget(View v) {

        View root = activity.getLayoutInflater().inflate(R.layout.layout_spotlight, new FrameLayout(activity));
        MaterialButton next = root.findViewById(R.id.next_target);
        next.setOnClickListener(view -> tutorial.next());
        MaterialButton exit = root.findViewById(R.id.close_spotlight);
        exit.setOnClickListener(view -> showConformation(R.string.tutorial_close, () -> tutorial.finish() ));
        TextView spotlightText = root.findViewById(R.id.spotlight_text);
        spotlightText.setText(v.getContentDescription());

        return new Target.Builder()
                .setOverlay(root)
                .setAnchor(v)
                .setShape(new Circle(100f, 500L, Circle.Companion.getDEFAULT_INTERPOLATOR()))
                .build();
    }

    public void start() {
        if (tutorial == null) {
            tutorial = tutorialBuilder.build();
            tutorial.start();
        }
    }

    public TutorialSpotlight setOnEndTutorialListener(Action listener) {
        tutorialBuilder.setOnSpotlightListener(new OnSpotlightListener() {
            @Override
            public void onStarted() { }

            @Override
            public void onEnded() {
                listener.doAction();
            }
        });

        return this;
    }

    private void showConformation(int messageId, Action action) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
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

    public interface Action {
        void doAction();
    }
}
