package ru.reliableteam.noteorganizer.todos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.material.textfield.TextInputEditText;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.todos.view.TodosFragment;


public class AddTodoBottomFragment extends BottomSheetDialogFragment {
    private final String CLASS_TAG = "NotesBtmDialogFragment";

    BottomSheetBehavior bottomSheetBehavior;
    ImageButton cancelBtn;
    ImageButton saveBtn;

    TextInputEditText title, description;

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
        saveBtn = view.findViewById(R.id.save_button);
        saveBtn.setOnClickListener(v -> saveTodo());
        title = view.findViewById(R.id.todo_title);
        description = view.findViewById(R.id.todo_description);

        bottomSheet.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));

        return bottomSheet;
    }

    private void saveTodo() {
        Intent intent = new Intent();
        intent.putExtra("title", title.getText().toString());
        intent.putExtra("description", description.getText().toString());
        System.out.println(getTargetRequestCode());
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        dismiss();
    }
}
