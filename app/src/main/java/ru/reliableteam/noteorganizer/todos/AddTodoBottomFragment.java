package ru.reliableteam.noteorganizer.todos;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.todos.view.TodosFragment;
import ru.reliableteam.noteorganizer.utils.DateUtils;


public class AddTodoBottomFragment extends BottomSheetDialogFragment {
    private final String CLASS_TAG = "NotesBtmDialogFragment";
    private final String EMPTY_TEXT = "";

    private View root;

    private TextInputEditText title, description;

    // date picking and showing
    private LinearLayoutCompat dateLayout;
    private DatePickerDialog datePickerDialog;
    private ImageButton dateBtn;
    private TextView dateTv;

    // time picking and showing
    private LinearLayoutCompat timeLayout;
    private TimePickerDialog timePickerDialog;
    private ImageButton timeBtn;
    private TextView timeTv;
    private Boolean timeChosen = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        root = View.inflate(getContext(), R.layout.fragment_add_todo, null);

        BottomSheetDialog bottomSheet = initBottomSheet(savedInstanceState);
        initUI();

        return bottomSheet;
    }

    private BottomSheetDialog initBottomSheet(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheet.setContentView(root);
        BottomSheetBehavior bottomSheetBehavior = bottomSheet.getBehavior();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setBottomSheetCallback(getCallback());

        return bottomSheet;
    }

    private void initUI() {
        initDatePicking();
        initTimePicking();
        initCancel();
        initSave();
        title = root.findViewById(R.id.todo_title);
        description = root.findViewById(R.id.todo_description);
    }

    private void initDatePicking() {
        datePickerDialog = new DatePickerDialog(getContext());
        dateBtn = root.findViewById(R.id.date_btn);
        dateBtn.setOnClickListener( v -> datePickerDialog.show() );
        dateLayout = root.findViewById(R.id.date_layout);
        dateTv = root.findViewById(R.id.todo_date);
        dateTv.setOnClickListener( v -> datePickerDialog.show() );
        ImageButton dateDelete = root.findViewById(R.id.todo_date_delete);
        dateDelete.setOnClickListener(v -> {
            clearDateLayout();
            clearTimeLayout();
        });
        datePickerDialog.setOnDateSetListener(
                (view, year, month, dayOfMonth) -> showDateLayout(year, month, dayOfMonth)
        );
    }
    private void initTimePicking() {
        timeBtn = root.findViewById(R.id.time_btn);
        timeBtn.setOnClickListener( v -> timePickerDialog.show() );
        timeLayout = root.findViewById(R.id.time_layout);
        timeTv = root.findViewById(R.id.todo_time);
        timeTv.setOnClickListener( v -> timePickerDialog.show() );
        ImageButton timeDelete = root.findViewById(R.id.todo_time_delete);
        timeDelete.setOnClickListener(v -> clearTimeLayout() );
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, hourOfDay_, minute_) -> {
                showTimeLayout(hourOfDay_, minute_);
        };
        timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
    private void initCancel() {
        ImageButton cancelBtn = root.findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(v -> dismiss() );
    }
    private void initSave() {
        ImageButton saveBtn = root.findViewById(R.id.save_button);
        saveBtn.setOnClickListener(v -> saveTodo());
    }
    private void saveTodo() {
        Intent intent = getIntentWithExtras();
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        dismiss();
    }
    private Intent getIntentWithExtras() {
        Intent intent = new Intent();
        long dateTime = DateUtils.stringToDate(getTime(), getDate());
        intent.putExtra("title", title.getText().toString());
        intent.putExtra("description", description.getText().toString());
        intent.putExtra("endDate", dateTime);
        intent.putExtra("timeChosen", timeChosen);

        return intent;
    }

    private void clearDateLayout() {
        dateLayout.setVisibility(View.GONE);
        dateTv.setText(EMPTY_TEXT);
        dateBtn.setEnabled(true);
        timeBtn.setVisibility(View.GONE);
    }
    private void showDateLayout(int dayOfMonth, int month, int year) {
        dateLayout.setVisibility(View.VISIBLE);
        dateTv.setText(DateUtils.getNormalizedDate(dayOfMonth, month, year));
        dateBtn.setEnabled(false);
        timeBtn.setVisibility(View.VISIBLE);
    }
    private void clearTimeLayout() {
        timeLayout.setVisibility(View.GONE);
        timeTv.setText(EMPTY_TEXT);
        timeBtn.setEnabled(true);
        timeChosen = false;
    }
    private void showTimeLayout(int hourOfDay, int minute) {
        timeLayout.setVisibility(View.VISIBLE);
        timeTv.setText(DateUtils.getNormalizedTime(hourOfDay, minute));
        timeBtn.setEnabled(false);
        timeChosen = true;
    }
    private String getTime() {
        return timeTv.getText().toString();
    }
    private String getDate() {
        return dateTv.getText().toString();
    }

    private BottomSheetBehavior.BottomSheetCallback getCallback() {
        return new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN)
                    dismiss();
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
        };
    }
}
