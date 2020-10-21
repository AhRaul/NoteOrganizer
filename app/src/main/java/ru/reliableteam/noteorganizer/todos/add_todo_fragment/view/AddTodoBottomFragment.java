package ru.reliableteam.noteorganizer.todos.add_todo_fragment.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import moxy.MvpBottomSheetDialogFragment;
import moxy.presenter.InjectPresenter;
import ru.reliableteam.noteorganizer.Action;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.todos.add_todo_fragment.presenter.AddTodoPresenter;
import ru.reliableteam.noteorganizer.todos.todos_fragment.TodoRequestCodes;
import ru.reliableteam.noteorganizer.utils.DateUtils;


public class AddTodoBottomFragment extends BottomSheetDialogFragment implements TodoRequestCodes, IAddTodoFragment {
    private final String CLASS_TAG = "NotesBtmDialogFragment";
    private final String EMPTY_TEXT = "";

    private View root;

    public AddTodoPresenter presenter;

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

    private ImageButton cancelBtn;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        root = View.inflate(getContext(), R.layout.fragment_add_todo, null);

        BottomSheetDialog bottomSheet = initBottomSheet(savedInstanceState);
        presenter = new AddTodoPresenter(this);
        initUI();

        bottomSheet.setOnKeyListener( onBackPressed() );

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
        initDatePickingLayout();
        initDatePickingDialog();
    }
    private void initDatePickingLayout() {
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
        setDescriptions(dateBtn);
    }
    private void initDatePickingDialog() {
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        DatePickerDialog.OnDateSetListener dateSetListener =
                (view, year_, month_, dayOfMonth_) -> showDateLayout(dayOfMonth_, month_, year_);

        datePickerDialog = new DatePickerDialog(getContext(),dateSetListener, year, month, day);
    }
    private void initTimePicking() {
        initTimePickingLayout();
        initTimePickingDialog();
    }
    private void initTimePickingLayout() {
        timeBtn = root.findViewById(R.id.time_btn);
        timeBtn.setOnClickListener( v -> timePickerDialog.show() );
        timeLayout = root.findViewById(R.id.time_layout);
        timeTv = root.findViewById(R.id.todo_time);
        timeTv.setOnClickListener( v -> timePickerDialog.show() );
        ImageButton timeDelete = root.findViewById(R.id.todo_time_delete);
        timeDelete.setOnClickListener(v -> clearTimeLayout() );
        setDescriptions(timeBtn);
    }
    private void initTimePickingDialog() {
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
        cancelBtn = root.findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(v -> {
            if (isTodoEmpty()) {
                dismiss();
                return;
            }
            showConfirmation(
                    () -> saveTodo(ACTION_UPDATE),
                    () -> dismiss(),
                    R.string.save_before_exit_hint);
        });
        setDescriptions(cancelBtn);
    }
    private void initSave() {
        ImageButton saveBtn = root.findViewById(R.id.save_button);
        saveBtn.setOnClickListener( v -> saveTodo(ACTION_UPDATE) );
        setDescriptions(saveBtn);
    }
    private void initDelete() {
        ImageButton deleteBtn = root.findViewById(R.id.delete_button);
        deleteBtn.setOnClickListener( v -> saveTodo(ACTION_DELETE) );
        deleteBtn.setVisibility(presenter.isNewTodo() ? View.GONE : View.VISIBLE);
        setDescriptions(deleteBtn);
    }
    private void setDescriptions(View... views) {
        for (View view : views) {
            view.setOnLongClickListener(v -> {
                showHint(v.getContentDescription().toString());
                return true;
            });
        }
    }
    private void showHint(String hintText) {
        Toast.makeText(getContext(), hintText, Toast.LENGTH_LONG).show();
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
        getTargetFragment().onActivityResult(requestCode, Activity.RESULT_OK, intent);

        dismiss();
    }
    private boolean isTodoEmpty() {
        return title.getText().toString().isEmpty() && description.getText().toString().isEmpty();
    }
    private Intent getIntentWithExtras(int action) {
        Intent intent = new Intent();
        System.out.println(getDate() + "_" + getTime());
        long dateTime = DateUtils.stringToDate(getTime(), getDate());
        System.out.println("dt = " + dateTime);
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
    @Override
    public void setDate(String date) {
        dateLayout.setVisibility(View.VISIBLE);
        dateBtn.setEnabled(false);
        dateTv.setText(date);
        timeBtn.setVisibility(View.VISIBLE);
    }
    @Override
    public void setTime(String time) {
        timeLayout.setVisibility(View.VISIBLE);
        timeBtn.setEnabled(false);
        timeTv.setText(time);
    }
    @Override
    public void setTitle(String title_) {
        title.setText(title_);
    }
    @Override
    public void setDescription(String description_) {
        description.setText(description_);
    }

    private void showVerification() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.empty_body_hint);
        builder.setPositiveButton(R.string.understand, (dialog, which) -> dialog.dismiss() );
        builder.show();
    }
    private void showConfirmation(Action actionPositive, Action actionNegative, int messageId) {
        AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
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

    private DialogInterface.OnKeyListener onBackPressed() {
        return (dialog, code, event) -> {
            if (code == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                cancelBtn.performClick();
                return true;
            } else
                return false;
        };
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
