package ru.reliableteam.noteorganizer.todos.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.todos.AddTodoBottomFragment;
import ru.reliableteam.noteorganizer.todos.presenter.TodoPresenter;
import ru.reliableteam.noteorganizer.todos.view.recycler.TodosRecyclerAdapter;
import ru.reliableteam.noteorganizer.utils.DateUtils;

public class TodosFragment extends Fragment {
    private final int REQUEST_NEW_TODO = 1;

    private TodoPresenter presenter;

    private View root;
    private RecyclerView recyclerView;
    private FloatingActionButton addTodoBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_todos, container, false);

        initUI();

        presenter = new TodoPresenter(this);
        initRecyclerView();

        return root;
    }
    private void initUI() {
        addTodoBtn = root.findViewById(R.id.add_todo_fab);
        addTodoBtn.setOnClickListener( v -> {
            AddTodoBottomFragment todoBottomFragment = new AddTodoBottomFragment();
            todoBottomFragment.setTargetFragment(TodosFragment.this, REQUEST_NEW_TODO);
            todoBottomFragment.show(getFragmentManager(), "add_todo");
        });
    }

    private void initRecyclerView() {
        recyclerView = root.findViewById(R.id.todos_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new TodosRecyclerAdapter(presenter));

        presenter.getTodos();
    }

    public void notifyDataChanged() {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null)
            adapter.notifyDataSetChanged();
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
}
