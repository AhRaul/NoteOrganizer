package ru.reliableteam.noteorganizer.todos.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.reliableteam.noteorganizer.R;
import ru.reliableteam.noteorganizer.todos.AddTodoBottomFragment;
import ru.reliableteam.noteorganizer.todos.presenter.TodoPresenter;
import ru.reliableteam.noteorganizer.todos.view.recycler.TodosRecyclerAdapter;

public class TodosFragment extends Fragment {

    private TodoPresenter presenter;

    private View root;
    private RecyclerView recyclerView;
    private FloatingActionButton addTodoBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_todos, container, false);

        addTodoBtn = root.findViewById(R.id.add_todo_fab);
        addTodoBtn.setOnClickListener( v -> new AddTodoBottomFragment().show(getFragmentManager(), "add_todo"));

        presenter = new TodoPresenter(this);
        initRecyclerView();
        presenter.getTodos();

        return root;
    }

    private void initRecyclerView() {
        recyclerView = root.findViewById(R.id.todos_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new TodosRecyclerAdapter(presenter));
//        recyclerView.addOnScrollListener(getRecyclerScrollListener());
    }

    public void notifyDataChanged() {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

}
