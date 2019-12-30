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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.todos.add_todo_fragment.view.AddTodoBottomFragment;
import ru.reliableteam.noteorganizer.todos.todos_fragment.presenter.TodoPresenter;
import ru.reliableteam.noteorganizer.todos.todos_fragment.view.recycler.TodosRecyclerAdapter;
import ru.reliableteam.noteorganizer.utils.DateUtils;

public class TodosFragment extends Fragment {
    private final int REQUEST_NEW_TODO = 1;
    private final int REQUEST_DELETE_TODO = 2;

    private TodoPresenter presenter;

    private View root;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_todos, container, false);

        initUI();

        presenter = new TodoPresenter(this);
        initRecyclerView();

        return root;
    }
    private void initUI() {
        initButtonAddTodo();
        initSortingButtons();
    }
    private void initButtonAddTodo() {
        FloatingActionButton addTodoBtn = root.findViewById(R.id.add_todo_fab);
        addTodoBtn.setOnClickListener(v -> {
            presenter.newTodo();
            boolean needResponse = true;
            openBottomSheet(needResponse, REQUEST_NEW_TODO);
        });
    }
    private void initSortingButtons() {
        MaterialButton showDone = root.findViewById(R.id.todos_done);
        showDone.addOnCheckedChangeListener( (button, isChecked) -> {
            System.out.println(isChecked);
                if (isChecked)
                    presenter.showDoneTodos();
                else
                    presenter.showAllTodos();
        });
        MaterialButton showCurrent = root.findViewById(R.id.todos_current);
        MaterialButton showMissing = root.findViewById(R.id.todos_missed);

    }

    public void viewTodo() {
        boolean needResponse = false;
        openBottomSheet(needResponse, REQUEST_NEW_TODO);
    }

    private void openBottomSheet(Boolean needResponse, int requestId) {
        AddTodoBottomFragment todoBottomFragment = new AddTodoBottomFragment();
        if (needResponse)
            todoBottomFragment.setTargetFragment(TodosFragment.this, requestId);
        todoBottomFragment.show(getFragmentManager(), "add_todo");
    }

    private void initRecyclerView() {
        recyclerView = root.findViewById(R.id.todos_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new TodosRecyclerAdapter(presenter));

        System.out.println("init");
        presenter.getTodos();
    }

    public void notifyDataChanged() {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_NEW_TODO) {
                    // todo convert to todos
                    String title = data.getStringExtra("title");
                    String description = data.getStringExtra("description");
                    Long dateTime = data.getLongExtra("endDate", 0L);
                    Boolean timeChosen = data.getBooleanExtra("timeChosen", false);

                    presenter.saveTodo(title, description, dateTime, timeChosen);

                    String date = DateUtils.dateToString(dateTime);

                    System.out.println(title + " " + description + " " + date + " time chosen = " + timeChosen);
            }
        }
    }

    public void showConfirmationDialog() {
        AlertDialog.Builder db = new AlertDialog.Builder(getContext());
        db.setTitle(R.string.delete_hint);
        db.setPositiveButton(R.string.positive, (dialog, which) -> presenter.deleteTodo());
        db.setNegativeButton(R.string.negative, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = db.show();
    }
}
