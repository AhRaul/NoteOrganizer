package ru.reliableteam.noteorganizer.notes.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import ru.reliableteam.noteorganizer.R;


public class NotesBottomDialogFragment extends BottomSheetDialogFragment {
    private final String CLASS_TAG = "NotesBtmDialogFragment";

    BottomSheetBehavior bottomSheetBehavior;
    LinearLayout appBarLayout;
    View extraSpace;
    LinearLayout noteLayout;
    TextInputEditText noteText, noteTitle;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        //inflating layout
        View view = View.inflate(getContext(), R.layout.fragment_bottom_note, null);
        appBarLayout = view.findViewById(R.id.appBarLayout);
        extraSpace = view.findViewById(R.id.extraSpace);
        noteLayout = view.findViewById(R.id.note_layout);
        noteText = view.findViewById(R.id.note_text);
        noteTitle = view.findViewById(R.id.note_title);

        //setting layout with bottom sheet
        bottomSheet.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));

        //setting Peek at the 16:9 ratio keyline of its parent.
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (BottomSheetBehavior.STATE_EXPANDED == i) {
                    Log.i(CLASS_TAG, "E");
                    showView(appBarLayout, getActionBarSize());
                    noteText.setMinimumHeight((Resources.getSystem().getDisplayMetrics().heightPixels) / 2);

                }
                if (BottomSheetBehavior.STATE_COLLAPSED == i) {
                    Log.i(CLASS_TAG, "C");
                    hideAppBar(appBarLayout);
                }

                if (BottomSheetBehavior.STATE_HIDDEN == i) {
                    dismiss();
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                Log.i(CLASS_TAG, "slide " + v);
            }
        });

        hideAppBar(appBarLayout);
        return bottomSheet;
    }

    @Override
    public void onStart() {
        super.onStart();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideAppBar(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);

    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

    private int getActionBarSize() {
        final TypedArray array = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) array.getDimension(0, 0);
        return size;
    }
}
