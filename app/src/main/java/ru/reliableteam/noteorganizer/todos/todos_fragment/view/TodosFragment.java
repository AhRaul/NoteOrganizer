package ru.reliableteam.noteorganizer.todos.todos_fragment.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.todos.add_todo_fragment.view.AddTodoBottomFragment;
import ru.reliableteam.noteorganizer.todos.todos_fragment.TodoRequestCodes;
import ru.reliableteam.noteorganizer.todos.todos_fragment.presenter.TodosPresenter;
import ru.reliableteam.noteorganizer.todos.todos_fragment.view.recycler.TodosRecyclerAdapter;

public class TodosFragment extends MvpAppCompatFragment implements TodoRequestCodes, ITodosFragment {

    @InjectPresenter
    public TodosPresenter presenter;

    private View root;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_todos, container, false);

        initialization();

        return root;
    }

    private void initialization() {
        initUI();
        initRecyclerView();
    }
    private void initRecyclerView() {
        recyclerView = root.findViewById(R.id.todos_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new TodosRecyclerAdapter(presenter));

        presenter.getTodos();
    }
    private void initUI() {
        initButtonAddTodo();
        initSortingButtons();
    }
    private void initButtonAddTodo() {
        FloatingActionButton addTodoBtn = root.findViewById(R.id.add_todo_fab);
        addTodoBtn.setOnClickListener(v -> {
            presenter.newTodo();
            openBottomSheet(REQUEST_NEW_TODO);
        });
    }
    private void initSortingButtons() {
        MaterialButtonToggleGroup toggleGroup = root.findViewById(R.id.selection_buttons_toggle_group);
        toggleGroup.addOnButtonCheckedListener( (group, checkedId, isChecked) -> {
            if (group.getCheckedButtonId() == View.NO_ID)
                presenter.showAllTodos();
        });
        MaterialButton showDone = root.findViewById(R.id.todos_done);
        showDone.addOnCheckedChangeListener( (button, isChecked) -> {
                if (isChecked)
                    presenter.showDoneTodos();
        });
        MaterialButton showCurrent = root.findViewById(R.id.todos_current);
        showCurrent.addOnCheckedChangeListener( (button, isChecked) -> {
            if (isChecked)
                presenter.showCurrentTodos();
        });
        MaterialButton showMissing = root.findViewById(R.id.todos_missed);
        showMissing.addOnCheckedChangeListener( (button, isChecked) -> {
            if (isChecked)
                presenter.showMissedTodos();
        });

    }

    @Override
    public void viewTodo() {
        openBottomSheet(REQUEST_EDIT_TODO);
    }
    @Override
    public void notifyDataChanged() {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void showConfirmationDialog() {
        AlertDialog.Builder db = new AlertDialog.Builder(getContext());
        db.setTitle(R.string.delete_hint);
        db.setPositiveButton(R.string.positive, (dialog, which) -> presenter.deleteTodo());
        db.setNegativeButton(R.string.negative, (dialog, which) -> dialog.dismiss());
        db.show();
    }

    private void openBottomSheet(int requestId) {
        AddTodoBottomFragment todoBottomFragment = new AddTodoBottomFragment();
        todoBottomFragment.setTargetFragment(TodosFragment.this, requestId);
        todoBottomFragment.show(getFragmentManager(), "add_todo");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            // todo convert to todos
            String title = data.getStringExtra("title");
            String description = data.getStringExtra("description");
            Long dateTime = data.getLongExtra("endDate", 0L);
            Boolean timeChosen = data.getBooleanExtra("timeChosen", false);
            Integer action = data.getIntExtra("action", 0);

            if (requestCode == REQUEST_NEW_TODO) {
                presenter.saveTodo(title, description, dateTime, timeChosen);
            }
            if (requestCode == REQUEST_EDIT_TODO) {
                presenter.editTodo(title, description, dateTime, timeChosen, action);
            }
        }
    }
}
