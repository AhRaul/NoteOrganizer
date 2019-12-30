package ru.reliableteam.noteorganizer.todos.add_todo_fragment.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.todos.add_todo_fragment.presenter.AddTodoPresenter;
import ru.reliableteam.noteorganizer.utils.DateUtils;


public class AddTodoBottomFragment extends BottomSheetDialogFragment {
    private final String CLASS_TAG = "NotesBtmDialogFragment";
    private final int REQUEST_NEW_TODO = 1;
    private final int ACTION_DELETE = -1;
    private final int ACTION_SAVE = 1;

    private final String EMPTY_TEXT = "";

    private View root;
    private AddTodoPresenter presenter;

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

    // todo prevent saving without title

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        root = View.inflate(getContext(), R.layout.fragment_add_todo, null);
        presenter = new AddTodoPresenter(this);

        BottomSheetDialog bottomSheet = initBottomSheet(savedInstanceState);
        initUI();

        return bottomSheet;
    }

    private BottomSheetDialog initBottomSheet(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheet.setContentView(root);
        BottomSheetBehavior bottomSheetBehavior = bottomSheet.getBehavior();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.addBottomSheetCallback(getCallback());

        return bottomSheet;
    }

    private void initUI() {
        initDatePicking();
        initTimePicking();
        initCancel();
        initSave();
        initDelete();
        title = root.findViewById(R.id.todo_title);
        description = root.findViewById(R.id.todo_description);

        setUIData();
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
                (view, year, month, dayOfMonth) -> showDateLayout(dayOfMonth, month, year)
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
        saveBtn.setOnClickListener( v -> saveTodo(ACTION_SAVE) );
    }
    private void initDelete() {
        ImageButton deleteBtn = root.findViewById(R.id.delete_button);
        deleteBtn.setOnClickListener( v -> saveTodo(ACTION_DELETE) );
        deleteBtn.setVisibility(presenter.isNewTodo() ? View.GONE : View.VISIBLE);
    }

    private void setUIData() {
        if (!presenter.isNewTodo()) {
            presenter.getUIData();
        }
    }

    private void saveTodo(int action) {
        int requestCode = getTargetRequestCode();
        Intent intent = getIntentWithExtras(action);
        if (isTodoEmpty()) {
            showVerification();
            return;
        }
        if (requestCode == REQUEST_NEW_TODO) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        } else {
            presenter.updateTodo(intent);
        }

        dismiss();
    }
    private boolean isTodoEmpty() {
        return title.getText().toString().isEmpty() && description.getText().toString().isEmpty();
    }
    private Intent getIntentWithExtras(int action) {
        Intent intent = new Intent();
        long dateTime = DateUtils.stringToDate(getTime(), getDate());
        intent.putExtra("title", title.getText().toString());
        intent.putExtra("description", description.getText().toString());
        intent.putExtra("endDate", dateTime);
        intent.putExtra("timeChosen", timeChosen);
        intent.putExtra("action", action);

        return intent;
    }

    private void clearDateLayout() {
        dateLayout.setVisibility(View.GONE);
        dateTv.setText(EMPTY_TEXT);
        dateBtn.setEnabled(true);
        timeBtn.setVisibility(View.GONE);
        timeChosen = false;
    }
    private void showDateLayout(int dayOfMonth, int month, int year) {
        dateLayout.setVisibility(View.VISIBLE);
        dateTv.setText(DateUtils.getNormalizedDate(dayOfMonth, month, year));
        dateBtn.setEnabled(false);
        timeBtn.setVisibility(View.VISIBLE);
        timeChosen = true;
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
    public void setDate(String date) {
        dateLayout.setVisibility(View.VISIBLE);
        dateBtn.setEnabled(false);
        dateTv.setText(date);
        timeBtn.setVisibility(View.VISIBLE);
    }
    public void setTime(String time) {
        timeLayout.setVisibility(View.VISIBLE);
        timeBtn.setEnabled(false);
        timeTv.setText(time);
    }
    public void setTitle(String title_) {
        title.setText(title_);
    }
    public void setDescription(String description_) {
        description.setText(description_);
    }

    private void showVerification() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.empty_body_hint);
        builder.setPositiveButton(R.string.positive, (dialog, which) -> dialog.dismiss() );
        builder.show();
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
