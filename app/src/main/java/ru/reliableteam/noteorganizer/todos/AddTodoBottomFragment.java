package ru.reliableteam.noteorganizer.todos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import ru.reliableteam.noteorganizer.R;


public class AddTodoBottomFragment extends BottomSheetDialogFragment {
    private final String CLASS_TAG = "NotesBtmDialogFragment";

    BottomSheetBehavior bottomSheetBehavior;
    ImageButton cancelBtn;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

//        inflating layout
        View view = View.inflate(getContext(), R.layout.fragment_add_todo, null);
//
//         TODO: 21.12.2019 add todo saving
//
        cancelBtn = view.findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener( v -> dismiss() );

        bottomSheet.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));

//        setting Peek at the 16:9 ratio keyline of its parent.
//        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
//
        return bottomSheet;
    }

    @Override
    public void onStart() {
        super.onStart();
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

    }
}
