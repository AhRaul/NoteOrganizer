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
        presenter.getTodos();

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
            switch (requestCode) {
                case REQUEST_NEW_TODO:
                    // todo convert to todos
                    String title = data.getStringExtra("title");
                    String body = data.getStringExtra("description");
                    System.out.println(title + " " + body);
                    //String title = data.getStringExtra("title");
                    break;
            }
        }
    }
}
